package com.zheng.ioc;

import com.zheng.annotation.*;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: ZhengTianLiang
 * @date: 2021/08/21  16:06
 * @desc: spring 注解方式导入的上下文
 */


public class HAnnotationConfigApplicationContext {

    /**
     * 用来存 spring的bean对象，
     * key： class 对象   比如  UserController.class
     * value: Object      比如   UserController的实例信息，可以是 new UserController()  // 只不过我们是通过反射创建的
     */
    private final Map<Class<?>, Object> beanMap = new ConcurrentHashMap<Class<?>, Object>();

    // 用来存获取到的 class 对象(复合要求的bean对象)
    private Set<Class> classSet = new HashSet<Class>();

    // 维护一个list集合，用来存储 bean的相关信息  比如说 context.getBeanDefinitionNames();
    private List<Class<?>> list = new ArrayList<Class<?>>();

    public HAnnotationConfigApplicationContext(Class<?> classes) {
        // 1、判断当前传进来的类，是不是一个配置类(类上有没有加注解 @HConfiguration)
        if (!classes.isAnnotationPresent(HConfiguration.class)) {
            throw new RuntimeException("抱歉，" + classes + "不是一个配置类，并没有加@HConfiguration注解");
        }
        // 2、因为咱们这个是通过注解的扫描方式进来的，所以还应该加一个是否含有 @HComponentScan 的注解校验
        if (!classes.isAnnotationPresent(HComponentScan.class)) {
            throw new RuntimeException("抱歉，" + classes + "中没有@HComponentScan注解");
        }
        // 3、拿到 HComponentScan 中的要扫描的包的路径
        HComponentScan hComponentScan = classes.getAnnotation(HComponentScan.class);
        String basePackage = hComponentScan.basePackage();
//        System.out.println(basePackage); // com.zheng.controller
        // 4、此时只是相对路径 com.zheng.controller  需要拿到在磁盘中的路径(也就是说编译后的target目录下的东西)，此时是其中一种方式
        // 其中的  Thread.currentThread().getContextClassLoader.getResource 要求是 / 分割的  com.zheng.controller     --->  com/zheng/controller
        // url是   file:/E:/java/ruanjian/idea/file/spring_frame/spring_handwritten/target/classes/com/zheng/controller
        URL url = Thread.currentThread().getContextClassLoader().getResource(basePackage.replace(".", "/"));
        if (url == null) {
            throw new RuntimeException("获取当前类在磁盘中的位置失败，请联系管理员");
        }
//        System.out.println(url.getPath());  //  /E:/java/ruanjian/idea/file/spring_frame/spring_handwritten/target/classes/com/zheng/controller
        File file = new File(url.getPath());
        // 5、迭代(递归) 当前目录，拿到所有的 .class文件
        resursionFile(file, basePackage);
        // 6、判断这些对象，是否包含了 这些注解： @HController、@HService、@HComponent、@HRepo
        for (Class<?> clazz : classSet) { // 遍历这个set，因为上面已经把这个包下的全部的 .class 文件放到了set集合中
            if (clazz.isAnnotationPresent(HController.class) || clazz.isAnnotationPresent(HService.class) ||
                    clazz.isAnnotationPresent(HRepositiry.class) || clazz.isAnnotationPresent(HComponent.class)) { // 需要交给spring的ioc容器管理
                // 此时的 class 是个类文件的对象，需要根据这个类文件的对象去创建 java的bean对象
                try {
                    list.add(clazz);

                    Constructor<?> constructor = clazz.getDeclaredConstructor();
                    Object o = constructor.newInstance();   // 创建了java的bean对象，下一步是交给ioc容器管理

                    // 向map中存储对应的信息
                    beanMap.put(clazz, o);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * todo: 上面的是ioc的代码，下面的是di的代码。
         * (若你希望在容器启动的时候就完成了di的功能，则将di的代码放在这，
         * 若你是希望延迟注入，则将di的逻辑放到延迟的逻辑里面去)
         *
         *  @HController
         * public class UserController {
         *
         *
         *     @HAutowired
         *     private UserService userService;
         *
         *     // 测试，看能够正常的注入进来
         *     public void userController(){
         *         userService.say();
         *     }
         * }
         */
        // 获取到 beanMap 中的全部的 key
        Set<Class<?>> classes1 = getClasses(); // PayController、UserController、OrderController
        for (Class<?> clazz : classes1) {
            // 某个PayController的全部的属性 比如说@Autowired UserService userService
            Field[] declaredFields = clazz.getDeclaredFields();
            if (declaredFields == null){ // 这个controller中没有任何属性
                continue;
            }
            for (Field field : declaredFields){ // 某个controller中有属性  private UserService userService;
                if (field.isAnnotationPresent(HAutowired.class)){ // 属性上有 @Autowired 注解，需要将其注入回去的
                    // 对这个属性进行依赖注入
                    // 拿到这个以后，可以判断是否为空什么的 spring的Autowired中就有 Required 是否需要处理 之类的
                    HAutowired annotation = field.getAnnotation(HAutowired.class);
                    Class<?> fieldType = field.getType(); // 此次是 UserController 中的 UserService 属性

                    // 从ioc容器中找找，看看有没有这个 fieldType 的bean对象  此时的fieldType 是UserService  ioc容器中可能没有这个接口，可能会有实现类
                    Object fieldBean = getBean(fieldType);

                    if (fieldBean == null){ // 虽然现在ioc中没有这个bean类型，可以试试从ioc容器中，获取这个类型的实现类(子类)
                        // 继续在ioc容器中找找这个类型的实现类(子类)
                        Set<Class<?>> resultSet = getClassBySuperOrInterface(fieldType);

                        // 从全部的实现类中，选择一个实例去赋值给变量(比如有俩实现类，我根据名字或者随机挑出来一个去赋值)
                        fieldBean = beanMap.get(resultSet.iterator().next()); // 现在是拿的第一个，可以循环遍历，再根据名称去匹配
                        System.out.println(123);
                    }

                    // 将当前的实例对象，去赋值给这个属性 field
                    field.setAccessible(true);
                    try {
                        field.set(getBean(clazz),fieldBean); // field.set(对象,属性的值)
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println(123);
        }


    }

    /**
     * @author: ZhengTianLiang
     * @date: 2021/08/21  16:41
     * @desc: 从ioc容器中，找到一个类型的全部的子类实现    传参 UserService  结果  UserServiceImpl1  UserServiceImpl2 ...
     */
    private Set<Class<?>> getClassBySuperOrInterface(Class<?> superOrInterface) {
        // 获取到ioc容器中的所有key，
        Set<Class<?>> classes = getClasses();
        // 准备一个返回结果
        Set<Class<?>> result = new HashSet<Class<?>>();

        for (Class<?> clazz : classes){
            // superOrInterface.isAssignableFrom(clazz); clazz是superOrInterface的子类或者实现类时，返回true
            if (superOrInterface.isAssignableFrom(clazz) && !clazz.equals(superOrInterface)){ // 是其实现类，且不是他本身
                result.add(clazz);
            }
        }
        return result;
    }

    /**
     * @author: ZhengTianLiang
     * @date: 2021/08/21  16:41
     * @desc: 递归一个路径，拿到这个路径下的全部的  .class 文件。并将这些文件存到了  set 集合中
     */
    private void resursionFile(File file, String basePackage) {
        File[] files = file.listFiles();

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File absoluteFile = files[i].getAbsoluteFile();
                if (absoluteFile.isDirectory()) { // 是文件夹，则继续调用本方法去调用
                    resursionFile(absoluteFile, basePackage);
                } else if (absoluteFile.toString().endsWith(".class")) { // 是文件，判断这个文件是否是  .class 类型
                    // E:\java\ruanjian\idea\file\spring_frame\spring_handwritten\target\classes\com\zheng\controller\OrderController.class
                    // // E:.java.ruanjian.idea.file.spring_frame.spring_handwritten.target.classes.com.zheng.controller.OrderController.class
                    String temp = absoluteFile.toString().replace(File.separator, ".");
                    String className = temp.substring(temp.indexOf(basePackage)); // com.zheng.controller.OrderController.class
                    // 完全类路径
                    className = className.substring(0, className.lastIndexOf(".")); // com.zheng.controller.OrderController

                    try {
//                        System.out.println(className); // com.zheng.controller.OrderController
                        Class<?> clazz = Class.forName(className);
                        classSet.add(clazz);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    /**
     * @author: ZhengTianLiang
     * @date: 2021/08/21  16:41
     * @desc: 是用来模拟spring 的context的  context.getBeanDefinitionNames()  的
     */
    public List<Class<?>> getBeanDefinitionNames() {
        return list;
    }

    /**
     * @author: ZhengTianLiang
     * @date: 2021/08/21  17:36
     * @desc: 根据key获取value(根据clazz名获取实例对象)      是模拟的  context.getBean();
     */
    public Object getBean(Class<?> key) {
        return beanMap.get(key);
    }

    public Set<Class<?>> getClasses() {
        return beanMap.keySet();
    }
}
