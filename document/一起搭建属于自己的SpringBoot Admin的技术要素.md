一起搭建属于自己的SpringBoot Admin的技术要素

### SpringBoot  Admin的介绍说明

SpringBoot Admin是开源社区孵化的项目，用于对SpringBoot应用的管理和监控。SpringBoot Admin 分为服务端(spring-boot-admin-server)和客户端(spring-boot-admin-client)，服务端和客户端之间采用http通讯方式实现数据交互；

单体项目中需要整合spring-boot-admin-client才能让应用被监控。在SpringCloud项目中，spring-boot-admin-server 是直接从注册中心抓取应用信息，不需要每个微服务应用整合spring-boot-admin-client就可以实现应用的管理和监控。

### SpringBoot Admin的技术分析

Spring Boot提供的监控接口，例如：/health、/info等等，实际上除了之前提到的信息，还有其他信息业需要监控：当前处于活跃状态的会话数量、当前应用的并发数、延迟以及其他度量信息。

了解如何利用Spring-boot-admin对应用信息进行可视化，如何添加度量信息。

![](https://oscimg.oschina.net/oscnet/up-904f6ad5c3b9318a9f62d519d34f35c7334.png)

### 准备

官方入门指南：https://codecentric.github.io/spring-boot-admin/current/

官网参考链接：https://codecentric.github.io/spring-boot-admin/2.2.4/

Github地址在：https://github.com/codecentric/spring-boot-admin
它在Spring Boot Actuator的基础上提供简洁的可视化WEB UI。

首先在start.spring.io中创建简单的admin应用，主要步骤如下：

1. 在Ops组选项中选择Actuator
2. 选择Generate Project下载应用
3. 使用IDEA打开工程，在pom.xml文件中添加下列依赖


```xml
<dependency>
   <groupId>de.codecentric</groupId>
   <artifactId>spring-boot-admin-server</artifactId>
  <version>2.2.4</version>
</dependency>
<dependency>
   <groupId>de.codecentric</groupId>
   <artifactId>spring-boot-admin-server-ui</artifactId>
   <version>2.2.4</version>
</dependency>
```

> 如果springboot版本是2.2.10.RELEASE，那么springboot admin 也要用2.2.x版

4. 在SpringBootAdminWebApplication.java文件中添加@EnableAdminServer注解

```java
@SpringBootApplication
@EnableAdminServer
public class SpringBootAdminWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootAdminWebApplication.class, args);
    }
}
```

5. 在application.properties文件中添加如下配置

```properties
server.port = 8090
spring.application.name=Spring Boot Admin Web
spring.boot.admin.url=http://localhost:${server.port}
spring.jackson.serialization.indent_output=true
endpoints.health.sensitive=false
```

6. 启动Admin Server应用后，现在可以添加针对应用的度量信息了。

#### 定制化自己的Indicator

在Spring Boot应用的健康监控中，我们可以定制自己的Health Indicator，用来监控四个数据库接口的健康状态，这次我将利用spring-boot-admin对这些信息进行可视化管理。

在服务模块下添加代码，首先在建立下添加SampleCountMetrics类：

```java
public class SampleCountMetrics implements PublicMetrics {
    private Collection<CrudRepository> repositories;
    public SampleCountMetrics(Collection<CrudRepository> repositories) {
        this.repositories = repositories;
    }
    @Override
    public Collection<Metric<?>> metrics() {
        List<Metric<?>> metrics = new LinkedList<>();
        for (CrudRepository repository: repositories) {
            String name =
 DbCountRunner.getRepositoryName(repository.getClass());
            String metricName = "properties.datasource." + name;
            metrics.add(new Metric(metricName, repository.count()));
        }
        return metrics;
    }
}
```

在Configuration定义对应的Bean，由Spring Boot完成自动注册

```java
@Bean
public PublicMetrics sampleCountMetrics(Collection<CrudRepository> repositories) {
    return new SampleCountMetrics(repositories);
}
```

访问http://localhost:port/metrics，可以看到SampleCountMetrics已经添加到metrics列表中了。

#### 总结分析

Spring Boot Admin就是将Spring Boot Actuator中提供的endpoint信息可视化表示，在应用（被监控）的这一端，只需要进行一点配置即可。

### 部署Admin Client端服务

```xml
<dependency>
    <groupId>de.codecentric</groupId>
    <artifactId>spring-boot-admin-starter-client</artifactId>
     <version>2.2.4</version>
</dependency>
```

在应用下的application.properties中配置下列属性值

```
spring.application.name=@project.description@
server.port=8080
spring.boot.admin.url=http://localhost:8090
```

开放端点用于SpringBoot Admin的监控

````
management.endpoints.web.exposure.include: '*'
````

spring-boot-admin-starter-client，作为客户端，用于与Spring Boot Admin Web的服务器沟通；

spring.boot.client.admin.url=http:localhost:8090用于将当前应用注册到Spring Boot Admin。

![](https://oscimg.oschina.net/oscnet/up-55b62c3b5ad8a6433b7fd74e66b109e8082.png)

如果希望通过Web控制系统的日志级别，则需要在应用中添加Jolokia JMX库（org.jolokia:jolokia-core），同时在项目资源目录下添加logback.xml文件，内容如下：

```xml
<configuration>
    <include resource="org/springframework/boot/logging/logback/base.xml"/>
    <jmxConfigurator/>
</configuration>
```

然后再次启动应用，然后在Spring Boot Admin的页面中查看LOGGING


#### Spring Boot Admin的目的是什么

Spring Boot提供的度量工具功能强大且具备良好的扩展性，除了我们配置的SampleCountMetrics，还监控应用的其他信息，例如内存消耗、线程数量、系统时间以及http会话数量。

#### gague和counter的定制

gague和counter度量通过GagueService和CountService实例提供，这些实例可以导入到任何Spring管理的对象中，用于度量应用信息。

例如，我们可以统计某个方法的调用次数，如果要统计所有RESTful接口的调用次数，则可以通过AOP实现，在调用指定的接口之前，首先调用counterService.increment("objectName.methodName.invoked");，某个方法被调用之后，则对它的统计值+1。

#### 在pom文件中添加AOP依赖

```xml
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

应用中添加Aspect组件，表示在每个Controller的方法调用之前，首先增加调用次数。

```java
@Aspect
@Component
public class ServiceMonitor {
    @Autowired
    private CounterService counterService;
    @Before("execution(* com.xx.xx.xx.*.*(..))")
    public void countServiceInvoke(JoinPoint joinPoint) {
        counterService.increment(joinPoint.getSignature() + "");
    }
}
```

在application.properties中设置打开AOP功能：spring.aop.auto=true

如果希望统计每个接口的调用时长，则需要借助GagueService来实现，同样使用AOP实现，则需要环绕通知：在接口调用之前，利用long start = System.currentTimeMillis();，在接口调用之后，计算耗费的时间，单位是ms，然后使用gugeService.submit(latency)更新该接口的调用延时。

在ServiceMonitor类中添加对应的监控代码

```java
@Autowired
private GaugeService gaugeService;
@Around("execution(* com.xx.xx.xx.*.*(..))")
public void latencyService(ProceedingJoinPoint pjp) throws Throwable {
    long start = System.currentTimeMillis();
    pjp.proceed();
    long end = System.currentTimeMillis();
    gaugeService.submit(pjp.getSignature().toString(), end - start);
}
```

然后在Spring Boot Admin后台可以看到对应接口的调用延迟

这两个service可以应付大多数应用需求，如果需要监控其他的度量信息，则可以定制我们自己的Metrics，例如在之前的例子中我们要统计四个数据库接口的调用状态，则我们定义了SampleCountMetrics，该类实现了PublishMetrics，在这个类中我们统计每个数据库接口的记录数量。

PublishMetrics这个接口只有一个方法：Collection<Metric<?>> metrics();，在该方法中定义具体的监控信息；该接口的实现类需要在配置文件中通过@Bean注解，让Spring Boot在启动过程中初始化，并自动注册到MetricsEndpoint处理器中，这样每次有访问/metrics的请求到来时，就会执行对应的metrics方法。

### 安全性

#### admin-server端安全加固

- SpringBoot Admin的管理后台如果没密码就能访问，那实在太不安全了，因此我们要给它加上登录的功能。

- 参考SpringBoot Admin的官方文档，可以在Admin-Server端添加Spring Security 相关依赖及就可以实现需要登录后才能访问网页管理面板。

- 官网参考链接为：https://codecentric.github.io/spring-boot-admin/2.2.4/#_securing_client_actuator_endpoints

#### 下面开始具体的改造

##### admin-server 添加Spring Security 相关依赖

```
        <!--springboot admin 安全相关-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
```

##### admin-server 设置账号和密码

在application.yml配置账号和密码

```
# 配置一个账号和密码
spring:
  security:
    user:
      name: admin
      password: root123456
```

##### admin-server 添加一个Spring Security 配置类

```java
@Configuration
public class SecuritySecureConfig extends WebSecurityConfigurerAdapter {
        private final String adminContextPath;
        public SecuritySecureConfig(AdminServerProperties adminServerProperties) {
            this.adminContextPath = adminServerProperties.getContextPath();
        }
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
            successHandler.setTargetUrlParameter("redirectTo");
            successHandler.setDefaultTargetUrl(adminContextPath + "/");
            http.authorizeRequests()
                    //1.配置所有静态资源和登录页可以公开访问
                    .antMatchers(adminContextPath + "/assets/**").permitAll()
                    .antMatchers(adminContextPath + "/login").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    //2.配置登录和登出路径
                    .formLogin().loginPage(adminContextPath + "/login").successHandler(successHandler).and()
                    .logout().logoutUrl(adminContextPath + "/logout").and()
                    //3.开启http basic支持，admin-client注册时需要使用
                    .httpBasic().and()
                    .csrf()
                    //4.开启基于cookie的csrf保护
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    //5.忽略这些路径的csrf保护以便admin-client注册
                    .ignoringAntMatchers(
                            adminContextPath + "/instances",
                            adminContextPath + "/actuator/**"
                    );
        }
    }
```

##### admin-server 安全加固后访问测试

再次访问http://localhost:port/  ，发现需要登录

![](https://oscimg.oschina.net/oscnet/up-e215b843a04d87b81593fd30dbcee56d815.png)

当我们输入正确的账号密码登录后，情况如下图

![](https://oscimg.oschina.net/oscnet/up-e1992338c62fbc840c4a2cb9775fb98d095.png)

- 这个时候的应用数居然变成了0了，在我们没进行安全加固时是有一个admin-client应用的，为什么就不见了？

- 原因是添加了账号密码认证后，admin-client端也需要配置下 admin-server的账号和密码。

#### admin-client 端设置 admin-server的账号密码

admin-client 注册到 admin-server时，admin-server端有个http Basic认证，通过了认证后 admin-client才能注册到 admin-server上。
admin-client的application.yml中配置访问密码配置可参考下面代码

```
spring:
  application:
    name: admin-client # 给client应用取个名字
  boot:
    admin:
      client:
        url:  http://localhost:port #这里配置admin server 的地址
        # 配置 admin-server的账号和密码
        username: admin 
        password: root123456
```

#### 再次访问 admin-server 管理后台

当我们登录后，终于再次看到了我们的admin-client这个应用

![](https://oscimg.oschina.net/oscnet/up-a65655da227a8191df0630499f01949a14a.png)

#### admin-client端的安全

admin-client端如果把actuator 端点都暴露出来，是非常不安全的。因此我们可以添加Spring Security对admin-client 也进行安全加固。

下面所有操作均在admin-client中进行

##### 添加Spring Security依赖

```xml
<!--spring security-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
```


##### yml中需要设置client的账号和密码

本次演示的admin-client的相关yml配置参考下面代码

```
spring:
  application:
    name: admin-client # 给client应用取个名字
  boot:
    admin:
      client:
        url:  http://localhost:23333 #这里配置admin server 的地址
        # 配置 admin-server的账号和密码
        username: admin
        password: root123456
        instance:
          metadata:
            # 这里配置admin-client的账号和密码
            user.name: ${spring.security.user.name}
            user.password: ${spring.security.user.password}
  # admin-client 的用户名和密码
  security:
    user:
      name: clientAdmin
      password: 123456
```

#### 添加Spring Security 配置类

为何要到配置？因为Spring Security不配置时会把所有请求都拦截的，而我们这里只需要拦截监控端点/actuator/**即可。同时，官网中提到admin-server访问admin-client时，也是采用http Basic认证方式的；因此需要配置Spring Security支持Http Basic认证方式。

```java
@Configuration
@Slf4j
public class SpringSecurityActuatorConfig extends WebSecurityConfigurerAdapter {
        public SpringSecurityActuatorConfig() {
            log.info("SpringSecurityActuatorConfig... start");
        }
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            //  这个配置只针对  /actuator/** 的请求生效
            http.antMatcher("/actuator/**")
                    // /actuator/下所有请求都要认证
                    .authorizeRequests().anyRequest().authenticated()
                    // 启用httpBasic认证模式，当springboot admin-client 配置了密码时，
                    // admin-server走httpbasic的认证方式来拉取client的信息
                    .and().httpBasic()
                    // 禁用csrf
                    .and().csrf().disable();

        }
    }
```

当访问admin-client的监控端点 http://localhost:port/actuator/health 时，发现需要进行http Basic认证；这也证明了我们的认证拦截只拦截了监控端点。效果如下图:

![](https://oscimg.oschina.net/oscnet/up-21369c99dabe49e6fd930eb794e5fbd739c.png)

#### 存在的问题

通过上面的一通配置，admin-client 添加 Spring Security 对actuator的端点进行安全认证的功能是实现了，但也存在着问题。

当我们项目本来就是使用SpringSecurity 安全框架进行认证和授权时。上述的配置就要做修改了。因为我们一般都不用HttpBasic认证，而是用的表单登录认证。也就出现了配置多个Spring Security的问题。虽然有这个问题，但是网上还是有解决方案的。

 这个方案是在Spring Security官方文档里面找到的：https://docs.spring.io/spring-security/site/docs/5.3.5.RELEASE/reference/html5/#multiple-httpsecurity 

```java
  /**
     * 表单登录认证方式配置，由于没有指定Order，所以默认是最大2147483647，数值越大，优先级越低
     * @author ZENG.XIAO.YAN
     * @Date 2020-11-11
     * @version 1.0
     */
    @Configuration
    public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
        
        public FormLoginWebSecurityConfigurerAdapter() {
            log.info("FormLoginWebSecurityConfigurerAdapter... start");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin();
        }
    }
```