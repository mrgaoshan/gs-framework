# gs-framework

框架实现主要流程


1. 通过 ConfigHelper 读取应用程序 application.properties配置文件信息，配置文件包含DB，扫描类路径，JSP路径等，用到PropsUtil获取属性文件

2. 定义注解 controller，service，inject，requestMapping

3. ClassUtil 类获取包名下的所有class文件，通过 ClassHelper 获取包下所有 带有 controller 和service 注解的类 class

4.