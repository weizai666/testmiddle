
## Resilience4j

### 基础介绍

- Resilience4j是一款轻量级，易于使用的容错库，其灵感来自于Netflix Hystrix，但是专为Java 8和函数式编程而设计。轻量级，因为库只使用了Vavr（前身是 Javaslang），它没有任何其他外部依赖下。相比之下，Netflix Hystrix对Archaius具有编译依赖性，Archaius具有更多的外部库依赖性，例如Guava和Apache Commons Configuration。

- Resilience4j提供高阶函数（decorators）来增强任何功能接口、lambda表达式或方法引用，包括断路器、速率限制器、重试或舱壁。可以在任何函数接口、lambda表达式或方法引用上使用多个装饰器。
circuitbreaker组件实现了断路器功能，是基于内存的断路器，采用
ConcurrentHashMap来实现。

#### 舱壁（Bulkhead）

Resilience4j提供了两种舱壁模式的实现，可用于限制并发执行的次数：

- SemaphoreBulkhead（信号量舱壁，默认），基于Java并发库中的Semaphore实现。
- FixedThreadPoolBulkhead（固定线程池舱壁），它使用一个有界队列和一个固定线程池。

SemaphoreBulkhead应该在各种线程和I / O模型上都能很好地工作。它基于信号量，与Hystrix不同，它不提供“影子”线程池选项。取决于客户端，以确保正确的线程池大小将与舱壁配置保持一致。
信号量舱壁（SemaphoreBulkhead）

当信号量存在剩余时进入系统的请求会直接获取信号量并开始业务处理。当信号量全被占用时，接下来的请求将会进入阻塞状态，SemaphoreBulkhead提供了一个阻塞计时器，如果阻塞状态的请求在阻塞计时内无法获取到信号量则系统会拒绝这些请求。若请求在阻塞计时内获取到了信号量，那将直接获取信号量并执行相应的业务处理。
固定线程池舱壁（FixedThreadPoolBulkhead）

FixedThreadPoolBulkhead的功能与SemaphoreBulkhead一样也是用于限制并发执行的次数的，但是二者的实现原理存在差别而且表现效果也存在细微的差别。FixedThreadPoolBulkhead使用一个固定线程池和一个等待队列来实现舱壁。当线程池中存在空闲时，则此时进入系统的请求将直接进入线程池开启新线程或使用空闲线程来处理请求。当线程池无空闲时接下来的请求将进入等待队列，若等待队列仍然无剩余空间时接下来的请求将直接被拒绝。在队列中的请求等待线程池出现空闲时，将进入线程池进行业务处理。

可以看到FixedThreadPoolBulkhead和SemaphoreBulkhead一个明显的差别是FixedThreadPoolBulkhead没有阻塞的概念，而SemaphoreBulkhead没有一个队列容量的限制。

#### 限速器（RateLimiter）

限速器（RateLimiter）的功能是防止突然的过量请求导致系统不堪重负，RateLimiter使用一个刷新周期的概念，限定在一个固定刷新周期内可处理的最大请求数量。若在某一个刷新周期内的请求数量已经达到最大，则本周期内接下来的请求将进入阻塞状态，如果在最大阻塞计时内新的刷新周期开启，则阻塞状态的请求将进入新的周期内进行处理。如最大的阻塞计时内新的刷新周期并未开启，则此时超出阻塞计时的那些请求将被直接拒绝。

#### 断路器（CircuitBreaker）

断路器（CircuitBreaker）相对于前面几个熔断机制更复杂，CircuitBreaker通常存在三种状态（CLOSE、OPEN、HALF_OPEN），并通过一个时间或数量窗口来记录当前的请求成功率或慢速率，从而根据这些指标来作出正确的容错响应。

当CircuitBreaker为CLOSE状态时客户端发起的请求将正常进入服务端系统，CircuitBreaker会计算出当前请求前的一个窗口里所有请求的异常率（失败率或慢速率），若异常率低于预期配置值，则系统将继续正常处理接下来的请求。当异常率不低于预期配置值时，此时服务端会进入OPEN状态，此时服务端将会暂时性的拒绝所有请求。在一段冷却时间（自定义配置）之后，服务端将自动进入HALF_OPEN状态，在半开状态服务端将尝试接受一定数量的请求（自定义配置），若这一定数量的请求的异常率低于预期，则此时服务端将再次恢复CLOSE状态，正常处理请求。而如果异常率还是高于预期则会继续退回到OPEN状态。

#### 重试（Retry）

重试机制比较简单，当服务端处理客户端请求异常时，服务端将会开启重试机制，重试期间内，服务端将每隔一段时间重试业务逻辑处理。 如果最大重试次数内成功处理业务，则停止重试，视为处理成功。如果在最大重试次数内处理业务逻辑依然异常，则此时系统将拒绝该请求。


![](https://oscimg.oschina.net/oscnet/up-dbdf0bdff91135293b9a3636af14151c541.png)

### 使用Resilience4j

要使用Resilience4j，不需要引入所有依赖，只需要选择你需要的。Resilience4j提供了以下的核心模块和拓展模块:

### 核心模块

**Resilience4j 提供了如下几款核心组件：**

- resilience4j-circuitbreaker: (Circuit breaking)
- resilience4j-ratelimiter: Rate limiting(频率控制)
- resilience4j-bulkhead: Bulkheading(依赖隔离&负载保护)
- resilience4j-retry: Automatic retrying (sync and async)（自动重试）
- resilience4j-cache: Result caching（应答缓存）
- resilience4j-timelimiter: Timeout handling（超时控制）



**附件组件包括：**

- resilience4j-reactor: Spring Reactor adapter
- resilience4j-rxjava2: RxJava2 adapter
- resilience4j-micrometer: Micrometer Metrics exporter
- resilience4j-metrics: Dropwizard Metrics exporter
- resilience4j-prometheus: Prometheus Metrics exporter
- resilience4j-spring-boot: Spring Boot Starter
- resilience4j-ratpack: Ratpack Starter
- resilience4j-retrofit: Retrofit Call Adapter Factories
- resilience4j-vertx: Vertx Future decorator
- resilience4j-consumer: Circular Buffer Event consumer

### Circuitbreaker

**CircuitBreaker**通过具有三种正常状态的有限状态机实现：**CLOSED，OPEN和HALF_OPEN以及两个特殊状态DISABLED和FORCED_OPEN**。

![](https://oscimg.oschina.net/oscnet/up-14769214408cfde2075b09d7ed8546b44db.png)

- 当熔断器关闭时，所有的请求都会通过熔断器。

- 如果失败率超过设定的阈值，熔断器就会从关闭状态转换到打开状态，这时所有的请求都会被拒绝。

- 当经过一段时间后，熔断器会从打开状态转换到半开状态，这时仅有一定数量的请求会被放入，并重新计算失败率，如果失败率超过阈值，则变为打开状态，如果失败率低于阈值，则变为关闭状态。

#### Ring Bit Buffer(环形缓冲区)

Resilience4j记录请求状态的数据结构和Hystrix不同，Hystrix是使用滑动窗口来进行存储的，而Resilience4j采用的是Ring Bit Buffer(环形缓冲区)。

Ring Bit Buffer在内部使用BitSet这样的数据结构来进行存储，BitSet的结构如下图所示：

![](https://oscimg.oschina.net/oscnet/up-87dee6ed67b8b23381c5a410c8a2b49ede7.png)

每一次请求的成功或失败状态只占用一个bit位，与boolean数组相比更节省内存。BitSet使用long[]数组来存储这些数据，意味着16个值(64bit)的数组可以存储1024个调用状态。

#### 执行监控范围

计算失败率需要填满环形缓冲区。如果环形缓冲区的大小为10，则必须至少请求满10次，才会进行故障率的计算，如果仅仅请求了9次，即使9个请求都失败，熔断器也不会打开。

#### 请求拦截控制

但是CLOSE状态下的缓冲区大小设置为10并不意味着只会进入10个请求，在熔断器打开之前的所有请求都会被放入。

#### 状态转换机制

- 当故障率高于设定的阈值时，熔断器状态会从由CLOSE变为OPEN。这时所有的请求都会抛出CallNotPermittedException异常。

- 当经过一段时间后，熔断器的状态会从OPEN变为HALF_OPEN，HALF_OPEN状态下同样会有一个Ring Bit Buffer，用来计算HALF_OPEN状态下的故障率，如果高于配置的阈值，会转换为OPEN，低于阈值则装换为CLOSE。

- CLOSE状态下的缓冲区不同的地方在于，HALF_OPEN状态下的缓冲区大小会限制请求数，只有缓冲区大小的请求数会被放入。

- DISABLED（始终允许访问）和FORCED_OPEN（始终拒绝访问）。这两个状态不会生成熔断器事件（除状态装换外），并且不会记录事件的成功或者失败。退出这两个状态的唯一方法是触发状态转换或者重置熔断器。


#### 熔断器关于线程安全的保证措施有以下几个部分：

- 熔断器的状态使用AtomicReference保存的

- 更新熔断器状态是通过无状态的函数或者原子操作进行的 

- 更新事件的状态用synchronized关键字保护

意味着同一时间只有一个线程能够修改熔断器状态或者记录事件的状态。


### 可配置参数

| 配置参数 | 默认值 | 描述 |
| :-----| ----: | :----: |
| failureRateThreshold | 50 | 熔断器关闭状态和半开状态使用的同一个失败率阈值 |
| ringBufferSizeInHalfOpenState | 10 | 熔断器半开状态的缓冲区大小，会限制线程的并发量，例如缓冲区为10则每次只会允许10个请求调用后端服务 |
| ringBufferSizeInClosedState | 100 | 熔断器关闭状态的缓冲区大小，不会限制线程的并发量，在熔断器发生状态转换前所有请求都会调用后端服务 |
| waitDurationInOpenState | 60(s) | 熔断器从打开状态转变为半开状态等待的时间 |
| automaticTransitionFromOpenToHalfOpenEnabled | 60(s) | 熔断器从打开状态转变为半开状态等待的时间 |
| waitDurationInOpenState | false | 如果置为true，当等待时间结束会自动由打开变为半开，若置为false，则需要一个请求进入来触发熔断器状态转换 |
| recordExceptions | empty | 需要记录为失败的异常列表 |
| ignoreExceptions | empty | 需要忽略的异常列表 |
| recordFailure | throwable -> true | 自定义的谓词逻辑用于判断异常是否需要记录或者需要忽略，默认所有异常都进行记录 |


#### SpringBoot的整合方式

resilience4j-spring-boot集成了circuitbeaker、retry、bulkhead、ratelimiter几个模块，因为后续还要学习其他模块，就直接引入resilience4j-spring-boot依赖。


##### maven 的配置 pom.xml

测试使用的IDE为idea，使用的springboot进行学习测试，首先引入maven依赖：

```java
<dependency>
    <groupId>io.github.resilience4j</groupId>
    <artifactId>resilience4j-spring-boot</artifactId>
    <version>0.9.0</version>
</dependency>
```

##### application.yml配置

````
resilience4j:
  circuitbreaker:
    configs:
      default:
        ringBufferSizeInClosedState: 5 # 熔断器关闭时的缓冲区大小
        ringBufferSizeInHalfOpenState: 2 # 熔断器半开时的缓冲区大小
        waitDurationInOpenState: 10000 # 熔断器从打开到半开需要的时间
        failureRateThreshold: 60 # 熔断器打开的失败阈值
        eventConsumerBufferSize: 10 # 事件缓冲区大小
        registerHealthIndicator: true # 健康监测
        automaticTransitionFromOpenToHalfOpenEnabled: false # 是否自动从打开到半开，不需要触发
        recordFailurePredicate:    com.example.resilience4j.exceptions.RecordFailurePredicate # 谓词设置异常是否为失败
        recordExceptions: # 记录的异常
          - com.hyts.resilience4j.exceptions.Service1Exception
          - com.hyts.resilience4j.exceptions.Service2Exception
        ignoreExceptions: # 忽略的异常
          - com.example.resilience4j.exceptions.BusinessAException
    instances:
      service1:
        baseConfig: default
        waitDurationInOpenState: 5000
        failureRateThreshold: 20
      service2:
        baseConfig: default
````

可以配置多个熔断器实例，使用不同配置或者覆盖配置。

##### 保护的后端服务

以一个后端服务为例，利用熔断器保护该服务。

```java
interface RemoteService {
    List<User> process() throws TimeoutException, InterruptedException;
}
```

##### 连接器调用该服务

这是调用远端服务的连接器，我们通过调用连接器中的方法来调用后端服务。

```java
public RemoteServiceConnector{
    public List<User> process() throws TimeoutException, InterruptedException {
        List<User> users;
        users = remoteServic.process();
        return users;
    }
}
```

##### 监控熔断器状态及事件

各个配置项的作用，需要获取特定时候的熔断器状态：

```java
@Log4j2
public class CircuitBreakerUtil {

    /**
     * @Description: 获取熔断器的状态
     */
    public static void getCircuitBreakerStatus(String time, CircuitBreaker circuitBreaker){
        CircuitBreaker.Metrics metrics = circuitBreaker.getMetrics();
        // Returns the failure rate in percentage.
        float failureRate = metrics.getFailureRate();
        // Returns the current number of buffered calls.
        int bufferedCalls = metrics.getNumberOfBufferedCalls();
        // Returns the current number of failed calls.
        int failedCalls = metrics.getNumberOfFailedCalls();
        // Returns the current number of successed calls.
        int successCalls = metrics.getNumberOfSuccessfulCalls();
        // Returns the max number of buffered calls.
        int maxBufferCalls = metrics.getMaxNumberOfBufferedCalls();
        // Returns the current number of not permitted calls.
        long notPermittedCalls = metrics.getNumberOfNotPermittedCalls();
        log.info(time + "state=" +circuitBreaker.getState() + " , metrics[ failureRate=" + failureRate +
                ", bufferedCalls=" + bufferedCalls +
                ", failedCalls=" + failedCalls +
                ", successCalls=" + successCalls +
                ", maxBufferCalls=" + maxBufferCalls +
                ", notPermittedCalls=" + notPermittedCalls +
                " ]"
        );
    }

    /**
     * @Description: 监听熔断器事件
     */
    public static void addCircuitBreakerListener(CircuitBreaker circuitBreaker){
        circuitBreaker.getEventPublisher()
                .onSuccess(event -> log.info("服务调用成功：" + event.toString()))
                .onError(event -> log.info("服务调用失败：" + event.toString()))
                .onIgnoredError(event -> log.info("服务调用失败，但异常被忽略：" + event.toString()))
                .onReset(event -> log.info("熔断器重置：" + event.toString()))
                .onStateTransition(event -> log.info("熔断器状态改变：" + event.toString()))
                .onCallNotPermitted(event -> log.info(" 熔断器已经打开：" + event.toString()))
        ;
    }
```

#### 调用方法

CircuitBreaker支持两种方式调用，一种是程序式调用，一种是AOP使用注解的方式调用。

##### 程序式的调用方法

在CircuitService中先注入注册器，然后用注册器通过熔断器名称获取熔断器。如果不需要使用降级函数，可以直接调用熔断器的executeSupplier方法或executeCheckedSupplier方法：

```java
public class CircuitBreakerServiceImpl{
    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;
    public List<User> circuitBreakerNotAOP() throws Throwable {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("service1");
        CircuitBreakerUtil.getCircuitBreakerStatus("执行开始前：", circuitBreaker);
        circuitBreaker.executeCheckedSupplier(remotServiceConnector::process);
    }
}
```

如果需要使用降级函数，则要使用decorate包装服务的方法，再使用Try.of().recover()进行降级处理，同时也可以根据不同的异常使用不同的降级方法：

```java
public class CircuitBreakerServiceImpl {
    @Autowired
    private RemoteServiceConnector remoteServiceConnector;
    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;
    public List<User> circuitBreakerNotAOP(){
        // 通过注册器获取熔断器的实例
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("service1");
        CircuitBreakerUtil.getCircuitBreakerStatus("执行开始前：", circuitBreaker);
        // 使用熔断器包装连接器的方法
        CheckedFunction0<List<User>> checkedSupplier = CircuitBreaker.
            decorateCheckedSupplier(circuitBreaker, remoteServiceConnector::process);
        // 使用Try.of().recover()调用并进行降级处理
        Try<List<User>> result = Try.of(checkedSupplier).
                    recover(CallNotPermittedException.class, throwable -> {
                        log.info("熔断器已经打开，拒绝访问被保护方法~");
                        CircuitBreakerUtil
                        .getCircuitBreakerStatus("熔断器打开中:", circuitBreaker);
                        List<User> users = new ArrayList();
                        return users;
                    })
                    .recover(throwable -> {
                        log.info(throwable.getLocalizedMessage() + ",方法被降级了~~");
                        CircuitBreakerUtil
                        .getCircuitBreakerStatus("降级方法中:",circuitBreaker);
                        List<User> users = new ArrayList();
                        return users;
                    });
            CircuitBreakerUtil.getCircuitBreakerStatus("执行结束后：", circuitBreaker);
            return result.get();
    }
}
```

##### AOP式的调用方法

首先在连接器方法上使用@CircuitBreaker(name="",fallbackMethod="")注解，其中name是要使用的熔断器的名称，fallbackMethod是要使用的降级方法，降级方法必须和原方法放在同一个类中，且降级方法的返回值需要和原方法相同，输入参数需要添加额外的exception参数，类似这样：

```java
public RemoteServiceConnector{
    
    @CircuitBreaker(name = "backendA", fallbackMethod = "fallBack")
    public List<User> process() throws TimeoutException, InterruptedException {
        List<User> users;
        users = remoteServic.process();
        return users;
    }
    
    private List<User> fallBack(Throwable throwable){
        log.info(throwable.getLocalizedMessage() + ",方法被降级了~~");
        CircuitBreakerUtil.getCircuitBreakerStatus("降级方法中:", circuitBreakerRegistry.circuitBreaker("backendA"));
        List<User> users = new ArrayList();
        return users;
    }
    
    private List<User> fallBack(CallNotPermittedException e){
        log.info("熔断器已经打开，拒绝访问被保护方法~");
        CircuitBreakerUtil.getCircuitBreakerStatus("熔断器打开中:", circuitBreakerRegistry.circuitBreaker("backendA"));
        List<User> users = new ArrayList();
        return users;
    }
    
} 
```

可使用多个降级方法，保持方法名相同，同时满足的条件的降级方法会触发最接近的一个（这里的接近是指类型的接近，先会触发离它最近的子类异常），例如如果process()方法抛出CallNotPermittedException，将会触发fallBack(CallNotPermittedException e)方法而不会触发fallBack(Throwable throwable)方法。

之后直接调用方法就可以了：

```java
public class CircuitBreakerServiceImpl {
    
    @Autowired
    private RemoteServiceConnector remoteServiceConnector;
    
    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;
    
    public List<User> circuitBreakerAOP() throws TimeoutException, InterruptedException {
        CircuitBreakerUtil
            .getCircuitBreakerStatus("执行开始前：",circuitBreakerRegistry.circuitBreaker("backendA"));
        List<User> result = remoteServiceConnector.process();
        CircuitBreakerUtil
            .getCircuitBreakerStatus("执行结束后：", circuitBreakerRegistry.circuitBreaker("backendA"));
        return result;
    }
}
```

##### 使用测试

接下来进入测试，首先我们定义了两个异常，异常A同时在黑白名单中，异常B只在黑名单中：

recordExceptions: # 记录的异常
- com.example.resilience4j.exceptions.BusinessBException
- com.example.resilience4j.exceptions.BusinessAException
ignoreExceptions: # 忽略的异常
- com.example.resilience4j.exceptions.BusinessAException
然后对被保护的后端接口进行如下的实现：

```java
public class RemoteServiceImpl implements RemoteService {
    
    private static AtomicInteger count = new AtomicInteger(0);

    public List<User> process() {
        int num = count.getAndIncrement();
        log.info("count的值 = " + num);
        if (num % 4 == 1){
            throw new BusinessAException("异常A，不需要被记录");
        }
        if (num % 4 == 2 || num % 4 == 3){
            throw new BusinessBException("异常B，需要被记录");
        }
        log.info("服务正常运行，获取用户列表");
        // 模拟数据库的正常查询
        return repository.findAll();
    }
}
```
使用CircuitBreakerServiceImpl中的AOP或者程序式调用方法进行单元测试，循环调用10次：

```java
public class CircuitBreakerServiceImplTest{
    
    @Autowired
    private CircuitBreakerServiceImpl circuitService;
    
    @Test
    public void circuitBreakerTest() {
        for (int i=0; i<10; i++){
            // circuitService.circuitBreakerAOP();
            circuitService.circuitBreakerNotAOP();
        }
    }
}
```

同时也可以看出白名单所谓的忽略，是指不计入缓冲区中（即不算成功也不算失败），有降级方法会调用降级方法，没有降级方法会抛出异常，和其他异常无异。


public class CircuitBreakerServiceImplTest{

    @Autowired
    private CircuitBreakerServiceImpl circuitService;
    
    @Test
    public void circuitBreakerThreadTest() throws InterruptedException {
        ExecutorService pool = Executors.newCachedThreadPool();
        for (int i=0; i<15; i++){
            pool.submit(
                // circuitService::circuitBreakerAOP
                circuitService::circuitBreakerNotAOP);
        }
        pool.shutdown();

        while (!pool.isTerminated());

        Thread.sleep(10000);
        log.info("熔断器状态已转为半开");
        pool = Executors.newCachedThreadPool();
        for (int i=0; i<15; i++){
            pool.submit(
                // circuitService::circuitBreakerAOP
                circuitService::circuitBreakerNotAOP);
        }
        pool.shutdown();

        while (!pool.isTerminated());
        for (int i=0; i<10; i++){
            
        }
    }
}

````
resilience4j:
  circuitbreaker:
    configs:
      myDefault:
        automaticTransitionFromOpenToHalfOpenEnabled: true # 是否自动从打开到半开
````




