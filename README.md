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

这里Bean MAP 类似一个 “bean 容器”，里面有bean类和bean实例关系，只要调用getBean 方法就可以获取Bean 实例。

实现依赖注入
----
1 .建立Iochelp 类，通过BeanHelper获取到所有 controller 和server 注解的 beanMap, 循环beanMap, 
获取当前注解有@Inject成员变量的类，
通过beanMap 得到 带有@inject 成员变量的实例， 通过ReflectUtils 设置当前类 @inject成员变量的值，

