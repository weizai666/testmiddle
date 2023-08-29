### TimeLimiter限时器

> 与Hystrix不同，Resilience4j将超时控制器从熔断器中独立出来，成为了一个单独的组件，主要的作用就是对方法调用进行超时控制。实现的原理和Hystrix相似，都是通过调用Future的get方法来进行超时控制。


#### 限时器的技术配置参数

![模式种类](timelimiter-parameter.png)


#### maven配置分析

```xml

<dependency>
    <groupId>io.github.resilience4j</groupId>
    <artifactId>resilience4j-timelimiter</artifactId>
    <version>${resilience4j.version}</version>
</dependency>

```

#### springboot的配置介绍

> TimeLimiter没有配置自动注入，需要自己进行注入，写下面两个文件进行配置自动注入：

##### TimeLimiterProperties

用于将application.yml中的配置转换为TimeLimiterProperties对象：

 
```java
    @Data
    @Component
    @ConfigurationProperties(prefix = "resilience4j.timelimiter")
    public class TimeLimiterProperties {
     
        private Duration timeoutDuration;
     
        private boolean cancelRunningFuture;
    }
```



##### yml文件配置


```yaml

resilience4j:
  timelimiter:
    timeoutDuration: 3000 
    cancelRunningFuture: true 
```

#### 开发使用

> TimeLimiter目前仅支持程序式调用，还不能使用AOP的方式调用。 因为TimeLimiter通常与CircuitBreaker联合使用，很少单独使用，所以直接介绍联合使用的步骤。
TimeLimiter没有注册器，所以通过@Autowired注解自动注入依赖直接使用，因为TimeLimter是基于Future的get方法的，所以需要创建线程池，然后通过线程池的submit方法获取Future对象：


##### TimeLimiter限时器基础配置

```java

@Configuration
public class TimeLimiterConfiguration {
 
    @Autowired
    private TimeLimiterProperties timeLimiterProperties;
 
    @Bean
    public TimeLimiter timeLimiter(){
        return TimeLimiter.of(timeLimiterConfig());
    }
 
    private TimeLimiterConfig timeLimiterConfig(){
        return TimeLimiterConfig.custom()
                .timeoutDuration(timeLimiterProperties.getTimeoutDuration())
                .cancelRunningFuture(timeLimiterProperties.isCancelRunningFuture()).build();
    }
}


```


##### TimeLimiter搭配着熔断器处理


```java

public class CircuitBreakerServiceImpl {

    @Autowired
    private RemoteServiceConnector remoteServiceConnector;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;


    @Autowired
    private TimeLimiter timeLimiter;


    public List<User> circuitBreakerTimeLimiter(){
        // 通过注册器获取熔断器的实例
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("backendA");
        CircuitBreakerUtil.getCircuitBreakerStatus("执行开始前：", circuitBreaker);
        // 创建单线程的线程池
        ExecutorService pool = Executors.newSingleThreadExecutor();
        //将被保护方法包装为能够返回Future的supplier函数
        Supplier<Future<List<User>>> futureSupplier = () -> pool.submit(remoteServiceConnector::process);
        // 先用限时器包装，再用熔断器包装
        Callable<List<User>> restrictedCall = TimeLimiter.decorateFutureSupplier(timeLimiter, futureSupplier);
        Callable<List<User>> chainedCallable = CircuitBreaker.decorateCallable(circuitBreaker, restrictedCall);
        // 使用Try.of().recover()调用并进行降级处理
        Try<List<User>> result = Try.of(chainedCallable::call)
            .recover(CallNotPermittedException.class, throwable ->{
                log.info("熔断器已经打开，拒绝访问被保护方法~");
                CircuitBreakerUtil.getCircuitBreakerStatus("熔断器打开中", circuitBreaker);
                List<User> users = new ArrayList();
                return users;
            })
            .recover(throwable -> {
                log.info(throwable.getLocalizedMessage() + ",方法被降级了~~");
                CircuitBreakerUtil.getCircuitBreakerStatus("降级方法中:",circuitBreaker);
                List<User> users = new ArrayList();
                return users;
            });
        CircuitBreakerUtil.getCircuitBreakerStatus("执行结束后：", circuitBreaker);
        return result.get();
    }
}

```
