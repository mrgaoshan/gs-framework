# gs-framework

框架实现主要流程
=====

实现一个BEAN容器
-----


1. 通过 ConfigHelper 读取应用程序 application.properties配置文件信息，配置文件包含DB，扫描类路径，JSP路径等，用到PropsUtil获取属性文件

2. 定义注解 controller，service，inject，requestMapping

3. ClassUtil 类获取包名下的所有class文件，通过 ClassHelper 获取包下所有 带有 controller 和service 注解的类 class

4. 通过ReflectionUtil 封装java 反射相关api， 创建class实例

5.通过 BeanHelper 帮助类，调用ClassHelper 获取所有class类，通过 ReflectionUtil 实例化对象，放入到BEAN MAP<class<?>>,Object> 中。

这里Bean MAP 就是一个 “bean 容器”，里面有bean类和bean实例关系，只要调用getBean 方法就可以获取Bean 实例。

实现依赖注入
----
1 .建立BeanHelper类， 

通过BeanHelper获取到所有 controller 和server 注解的 Class 集合，循环class 集合，通过 class.newInstance 创建class 实例，放入 beanMap<Class<?>, ClassInstance> 

2. 建立IOChelper类， 

循环beanMap, 获取类里面注解有@Inject成员变量的类(beanFieldClass)，  如：@Inject XXXXservice 

通过beanMap 得到 带有@inject 成员变量的实例(beanFieldInstance), 

通过ReflectUtils 设置当前类 @inject成员变量的值 (ReflectionUtil.setField(beanInstance, beanField, beanFieldInstance)),从而实现依赖注入

此时的IOChelper 需要在一个地方初始化


建立请求地址RequestMapping与Handler 映射关系 
----
#### （请求URL映射到哪一个controller和哪一个方法）

1. 建立ControllerHelper 

通过ClassHelper 获取到所有control注解的class 

循环class的方法，找到带有@RquestMapping 注解的方法， 封装RequstMapping定义url，请求URL和请求类型，放到UrlObJect对象

把 当前类 和当前方法放入 Handdler对象 

放入RequestMap<UrlObJect, Handdler> 中,从而建立了URL 和 Contoller 映射关系

框架的初始化
----
##### 建立 LoaderHelper 来做统一初始化入口 

通过以上步骤，我们建立了： 

ClassHelper -- 获取包下所有 带有 controller 和service 注解的类 class Helper

BeanHelper -- bean 容器 Helper

Iochelper -- 依赖注入Helper

ControllerHelper  -- 请求URL与 Method ，controller 对应关系 Helper

因为初始的内容都放到静态块中的，所以需要在一个地方集中初始化话，只需要调用class.forName ，已达到框架的初始化


DispatcherServlet 请求转发器实现与初始化
----

1.新建 DispatcherServlet 继承 HttpServlet  加入注解@WebServlet(urlPatterns = "/*", loadOnStartup = 0) 

表示每个请求 都经过改servlet ，loadOnStartup 数字越小优先级越高

2.重写init 方法，在该方法加载时 就通过LoaderHelper.init() 初始化 helper 类

3.初始化 jsp 和 静态资源路径，把 index.jsp, ./WEB-INF/View/* 下的jsp交给 org.apache.jasper.servlet.JspServlet 处理 

把 /ico, /asset/* 下的静态文件 交给 org.apache.catalina.servlets.DefaultServlet 处理 

所以其他请求的URL 都会 经过重写的Service 方法，进入我们框架处理

4.重写service 方法， ControllerHelper.getHandler 获取 请求http 映射到 哪一个类(controllerBeanClass)，哪一个方法(actionMethod)

5.实例化类，通过Bean Map 获取到（controllerBean） 

6.通过反射调用类方法 ( result = actionMethod.invoke(controllerBean, args))   并返回调用结果 (result)
 
7. 根据返回的 result 是View 还是 Data类型 , 返回页面  request.getRequestDispatcher 或   PrintWriter writer = response.getWriter() 写入 json。


### 8. 至此，实现了简单的Web 框架


## 框架加入AOP功能

实现AOP 主要是通过CGLIB 动态代理试下的，一下是一个简单的CGLIB 动态代理 例子
   public class CglibProxy implements MethodInterceptor {  
       private Object target;    
         
      public Object getProxyInstance(Object target) {    
           this.target = target;  
           Enhancer enhancer = new Enhancer();    
          enhancer.setSuperclass(this.target.getClass());    
          enhancer.setCallback(this);  // call back method  
          return enhancer.create();  // create proxy instance  
       }    
         
     @Override  
       public Object intercept(Object target, Method method, Object[] args, MethodProxy proxy) throws Throwable {  
          System.out.println("before target method...");  
          Object result = proxy.invokeSuper(target, args);  
          System.out.println("after target method...");  
           return result;  
      }  


