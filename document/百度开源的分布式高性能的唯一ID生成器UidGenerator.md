### UidGenerator是什么

UidGenerator是百度开源的一款分布式高性能的唯一ID生成器，更详细的情况可以查看[官网集成文档](https://github.com/baidu/uid-generator/blob/master/README.zh_cn.md)

> uid-generator是基于Twitter开源的snowflake算法实现的一款唯一主键生成器(数据库表的主键要求全局唯一是相当重要的)。要求java8及以上版本。

#### snowflake算法

Snowflake算法描述：指定机器 & 同一时刻 & 某一并发序列，是唯一的。据此可生成一个64 bits的唯一ID（long）。

> 将long的64位分为3部分，时间戳、工作机器id和序列号，位数分配如下：

![](https://oscimg.oschina.net/oscnet/up-62a7df74e1d4727834fc3c3626c9789389a.png)

时间戳部分的时间单位一般为毫秒，也就是说1台工作机器1毫秒可产生4096个id（2的12次方）。

#### UidGenerator算法

与原始的snowflake算法不同，uid-generator支持自定义时间戳、工作机器id和序列号等各部分的位数，以应用于不同场景。

![](https://oscimg.oschina.net/oscnet/up-45bd0e949a376bf82e9ff2044bb1d0abda4.png)

- sign(1bit)：固定1bit符号标识，即生成的UID为正数。
- delta seconds (28 bits)：当前时间，相对于时间基点"2016-05-20"的增量值，单位：秒，最多可支持约8.7年
- worker id (22 bits)：机器id，最多可支持约420w次机器启动。内置实现为在启动时由数据库分配，默认分配策略为用后即弃，后续可提供复用策略。
- sequence (13 bits)：每秒下的并发序列，13 bits可支持每秒8192个并发。

> 这些字段的长度可以根据具体的应用需要进行动态的调整，满足总长度为64位即可。

#### Snowflake和UidGenerator的对比

百度的worker id的生成策略和美团的生成策略不太一样，美团的snowflake主要利用本地配置的port和IP来唯一确定一个workid，美团的这种生成方式还是可以由于手工配置错误造成port重复，最终产生重复ID的风险，百度的这种生成方式每次都是新增的，可能会一段时间后worker id用完的情况，人工配置错误的可能性很小了。

### 源码分析

#### DefaultUidGenerator

DefaultUidGenerator的产生id的方法与基本上就是常见的snowflake算法实现，仅有一些不同，如以秒为为单位而不是毫秒。DefaultUidGenerator的产生id的方法如下。

```java
 protected synchronized long nextId() {
        long currentSecond = getCurrentSecond();
        // Clock moved backwards, refuse to generate uid
        if (currentSecond < lastSecond) {
            long refusedSeconds = lastSecond - currentSecond;
            throw new UidGenerateException("Clock moved backwards. Refusing for %d seconds", refusedSeconds);
        }
        // At the same second, increase sequence
        if (currentSecond == lastSecond) {
            sequence = (sequence + 1) & bitsAllocator.getMaxSequence();
            // Exceed the max sequence, we wait the next second to generate uid
            if (sequence == 0) {
                currentSecond = getNextSecond(lastSecond);
            }
        // At the different second, sequence restart from zero
        } else {
            sequence = 0L;
        }
        lastSecond = currentSecond;
        // Allocate bits for UID
        return bitsAllocator.allocate(currentSecond - epochSeconds, workerId, sequence);
    }
```

nextId方法主要负责ID的生成，这种实现方式很简单，如果毫秒数未发生变化，在序列号加一即可，毫秒数发生变化，重置Sequence为0（Leaf文章中讲过，重置为0会造成如果利用这个ID分表的时候，并发量不大的时候，sequence字段会一直为0等，会出现数据倾斜）


#### CachedUidGenerator

CachedUidGenerator支持缓存生成的id。

- 【采用RingBuffer来缓存已生成的UID, 并行化UID的生产和消费】
- 【UidGenerator通过借用未来时间来解决sequence天然存在的并发限制】

##### 基本实现原理

正如名字体现的那样，这是一种缓存型的ID生成方式，当剩余ID不足的时候，会异步的方式重新生成一批ID缓存起来，后续请求的时候直接的时候直接返回现成的ID即可。

在实现上, UidGenerator通过借用未来时间来解决sequence天然存在的并发限制; 采用RingBuffer来缓存已生成的UID, 并行化UID的生产和消费, 同时对CacheLine补齐，避免了由RingBuffer带来的硬件级「伪共享」问题. 最终单机QPS可达600万。

使用RingBuffer缓存生成的id。RingBuffer是个环形数组，默认大小为8192个，里面缓存着生成的id。

CachedUidGenerator采用了双RingBuffer，Uid-RingBuffer用于存储Uid、Flag-RingBuffer用于存储Uid状态(是否可填充、是否可消费)

> 由于数组元素在内存中是连续分配的，可最大程度利用CPU cache以提升性能。但同时会带来「伪共享」FalseSharing问题，为此在Tail、Cursor指针、Flag-RingBuffer中采用了CacheLine 补齐方式。

##### 获取id

会从ringbuffer中拿一个id，支持并发获取

```java
@Override
    public long getUID() {
        try {
            return ringBuffer.take();
        } catch (Exception e) {
            LOGGER.error("Generate unique id exception. ", e);
            throw new UidGenerateException(e);
        }
    }
```

##### RingBuffer缓存已生成的id

RingBuffer为环形数组，默认容量为sequence可容纳的最大值（8192个），可以通过boostPower参数设置大小。几个重要的数据结构，采用了RingBuffer的方式来缓存相关UID信息。

tail指针、Cursor指针用于环形数组上读写slot：

###### Tail指针

指向当前最后一个可用的UID位置：表示Producer生产的最大序号(此序号从0开始，持续递增)。Tail不能超过Cursor，即生产者不能覆盖未消费的slot。当Tail已赶上curosr，此时可通过rejectedPutBufferHandler指定PutRejectPolicy

###### Cursor指针

指向下一个获取UID的位置，其一定是小于Tail：表示Consumer消费到的最小序号(序号序列与Producer序列相同)。Cursor不能超过Tail，即不能消费未生产的slot。当Cursor已赶上tail，此时可通过rejectedTakeBufferHandler指定TakeRejectPolicy

> Tail - Cursor表示的是现在可用的UID数量，当可用UID数量小于一定阈值的时候会重新添加一批新的UID在RingBuffer中。

![](https://oscimg.oschina.net/oscnet/up-f170879c7005261c7099b3cb2d4a9a0c068.png)

##### 填充id

- RingBuffer填充时机

	- 程序启动时，将RingBuffer填充满，缓存着8192个id

	- 在调用getUID()获取id时，检测到RingBuffer中的剩余id个数小于总个数的50%，将RingBuffer填充满，使其缓存8192个id。

	- 定时填充（可配置是否使用以及定时任务的周期）

因为delta seconds部分是以秒为单位的，所以1个worker 1秒内最多生成的id书为8192个（2的13次方）。从上可知，支持的最大qps为8192，所以通过缓存id来提高吞吐量。

##### 为什么叫借助未来时间？

因为每秒最多生成8192个id，当1秒获取id数多于8192时，RingBuffer中的id很快消耗完毕，在填充RingBuffer时，生成的id的delta seconds 部分只能使用未来的时间。（因为使用了未来的时间来生成id，所以上面说的是，【最多】可支持约8.7年）

> 注意：这里的RingBuffer不是Disruptor框架中的RingBuffer，但是借助了很多Disruptor中RingBuffer的设计思想，比如使用缓存行填充解决伪共享问题。

###### 填充RingBuffer

```java
/**
     * Padding buffer fill the slots until to catch the cursor
     */
    public void paddingBuffer() {
        LOGGER.info("Ready to padding buffer lastSecond:{}. {}", lastSecond.get(), ringBuffer);

        // is still running
        if (!running.compareAndSet(false, true)) {
            LOGGER.info("Padding buffer is still running. {}", ringBuffer);
            return;
        }

        // fill the rest slots until to catch the cursor
        boolean isFullRingBuffer = false;
        while (!isFullRingBuffer) {
            //获取生成的id，放到RingBuffer中。
            List<Long> uidList = uidProvider.provide(lastSecond.incrementAndGet());
            for (Long uid : uidList) {
                isFullRingBuffer = !ringBuffer.put(uid);
                if (isFullRingBuffer) {
                    break;
                }
            }
        }

        // not running now
        running.compareAndSet(true, false);
        LOGGER.info("End to padding buffer lastSecond:{}. {}", lastSecond.get(), ringBuffer);
    }
```

生成id（上面代码中的uidProvider.provide调用的就是这个方法)
```java
/**
     * Get the UIDs in the same specified second under the max sequence
     * 
     * @param currentSecond
     * @return UID list, size of {@link BitsAllocator#getMaxSequence()} + 1
     */
    protected List<Long> nextIdsForOneSecond(long currentSecond) {
        // Initialize result list size of (max sequence + 1)
        int listSize = (int) bitsAllocator.getMaxSequence() + 1;
        List<Long> uidList = new ArrayList<>(listSize);

        // Allocate the first sequence of the second, the others can be calculated with the offset
        //这里的实现很取巧
        //因为1秒内生成的id是连续的，所以利用第1个id来生成后面的id，而不用频繁调用snowflake算法
        long firstSeqUid = bitsAllocator.allocate(currentSecond - epochSeconds, workerId, 0L);
        for (int offset = 0; offset < listSize; offset++) {
            uidList.add(firstSeqUid + offset);
        }

        return uidList;
    }
```



#### RingBuffer的代码

```java
public class RingBuffer {
    private static final Logger LOGGER = LoggerFactory.getLogger(RingBuffer.class);

    /** Constants */
    private static final int START_POINT = -1; 
    private static final long CAN_PUT_FLAG = 0L; //用于标记当前slot的状态，表示可以put一个id进去
    private static final long CAN_TAKE_FLAG = 1L; //用于标记当前slot的状态，表示可以take一个id
    public static final int DEFAULT_PADDING_PERCENT = 50; //用于控制何时填充slots的默认阈值：当剩余的可用的slot的个数，小于bufferSize的50%时，需要生成id将slots填满

    /** The size of RingBuffer's slots, each slot hold a UID */
    private final int bufferSize; //slots的大小，默认为sequence可容量的最大值，即8192个
    private final long indexMask; 
  
    private final long[] slots;  //slots用于缓存已经生成的id
    private final PaddedAtomicLong[] flags; //flags用于存储id的状态(是否可填充、是否可消费)

    /** Tail: last position sequence to produce */
    //Tail指针
    //表示Producer生产的最大序号(此序号从0开始，持续递增)。Tail不能超过Cursor，即生产者不能覆盖未消费的slot。当Tail已赶上curosr，此时可通过rejectedPutBufferHandler指定PutRejectPolicy
    private final AtomicLong tail = new PaddedAtomicLong(START_POINT); //

    /** Cursor: current position sequence to consume */
    //表示Consumer消费到的最小序号(序号序列与Producer序列相同)。Cursor不能超过Tail，即不能消费未生产的slot。当Cursor已赶上tail，此时可通过rejectedTakeBufferHandler指定TakeRejectPolicy
    private final AtomicLong cursor = new PaddedAtomicLong(START_POINT);

    /** Threshold for trigger padding buffer*/
    private final int paddingThreshold; //用于控制何时填充slots的阈值
    
    /** Reject put/take buffer handle policy */
    //当slots满了，无法继续put时的处理策略。默认实现：无法进行put，仅记录日志
    private RejectedPutBufferHandler rejectedPutHandler = this::discardPutBuffer;
    //当slots空了，无法继续take时的处理策略。默认实现：仅抛出异常
    private RejectedTakeBufferHandler rejectedTakeHandler = this::exceptionRejectedTakeBuffer; 
    
    /** Executor of padding buffer */
    //用于运行【生成id将slots填满】任务
    private BufferPaddingExecutor bufferPaddingExecutor;
```

#### 代码层面的优化

代码中通过字节的填充，来避免伪共享的产生。

多核处理器处理相互独立的变量时，一旦这些变量处于同一个缓存行，不同变量的操作均会造成这一个缓存行失效，影响缓存的实际效果，造成很大的缓存失效的性能问题。下面图中线程处理不同的两个变量，但这两个变量的修改都会造成整个整个缓存行的失效，导致无效的加载、失效，出现了伪共享的问题

RingBuffer中通过定义一个PaddedAtomicLong来独占一个缓存行，代码中的实现填充可能需要根据具体的执行系统做一些调整，保证其独占一个缓存行即可。

#### take先关id的源码

下面我们来看下如何获取相关的UID

```java
public long take() {
        // spin get next available cursor
        long currentCursor = cursor.get();
        long nextCursor = cursor.updateAndGet(old -> old == tail.get() ? old : old + 1);

        // check for safety consideration, it never occurs
        Assert.isTrue(nextCursor >= currentCursor, "Curosr can't move back");

        // trigger padding in an async-mode if reach the threshold
        long currentTail = tail.get();
        if (currentTail - nextCursor < paddingThreshold) {
            LOGGER.info("Reach the padding threshold:{}. tail:{}, cursor:{}, rest:{}", paddingThreshold, currentTail,
                    nextCursor, currentTail - nextCursor);
            bufferPaddingExecutor.asyncPadding();
        }

        // cursor catch the tail, means that there is no more available UID to take
        if (nextCursor == currentCursor) {
            rejectedTakeHandler.rejectTakeBuffer(this);
        }

        // 1. check next slot flag is CAN_TAKE_FLAG
        int nextCursorIndex = calSlotIndex(nextCursor);
        Assert.isTrue(flags[nextCursorIndex].get() == CAN_TAKE_FLAG, "Curosr not in can take status");

        // 2. get UID from next slot
        // 3. set next slot flag as CAN_PUT_FLAG.
        long uid = slots[nextCursorIndex];
        flags[nextCursorIndex].set(CAN_PUT_FLAG);

        // Note that: Step 2,3 can not swap. If we set flag before get value of slot, the producer may overwrite the
        // slot with a new UID, and this may cause the consumer take the UID twice after walk a round the ring
        return uid;
    }
```

通过AtomicLong.updateAndGet来避免对整个方法进行加锁，获取一个可以访问的UID的游标值，根据这个下标获取slots中相关的uid直接返回
缓存中可用的uid(Tail - Cursor)小于一定阈值的时候，需要启动另外一个线程来生成一批UID
UID 的生成

public synchronized boolean put(long uid) {
        long currentTail = tail.get();
        long currentCursor = cursor.get();

        // tail catches the cursor, means that you can't put any cause of RingBuffer is full
        long distance = currentTail - (currentCursor == START_POINT ? 0 : currentCursor);
        if (distance == bufferSize - 1) {
            rejectedPutHandler.rejectPutBuffer(this, uid);
            return false;
        }

        // 1. pre-check whether the flag is CAN_PUT_FLAG
        int nextTailIndex = calSlotIndex(currentTail + 1);
        if (flags[nextTailIndex].get() != CAN_PUT_FLAG) {
            rejectedPutHandler.rejectPutBuffer(this, uid);
            return false;
        }

        // 2. put UID in the next slot
        // 3. update next slot' flag to CAN_TAKE_FLAG
        // 4. publish tail with sequence increase by one
        slots[nextTailIndex] = uid;
        flags[nextTailIndex].set(CAN_TAKE_FLAG);
        tail.incrementAndGet();

        // The atomicity of operations above, guarantees by 'synchronized'. In another word,
        // the take operation can't consume the UID we just put, until the tail is published(tail.incrementAndGet())
        return true;
    }

获取Tail的下标值，如果缓存区满的话直接调用RejectedPutHandler.rejectPutBuffer方法
未满的话将UID放置在slots数组相应的位置上，同时将Flags数组相应的位置改为CAN_TAKE_FLAG
CachedUidGenerator通过缓存的方式预先生成一批UID列表，可以解决UID获取时候的耗时，但这种方式也有不好点，一方面需要耗费内存来缓存这部分数据，另外如果访问量不大的情况下，提前生成的UID中的时间戳可能是很早之前的，DefaultUidGenerator应该在大部分的场景中就可以满足相关的需求了。

##### 填充缓存行解决“伪共享”

关于伪共享，可以参考这篇文章[《伪共享（false sharing），并发编程无声的性能杀手》](https://www.cnblogs.com/cyfonly/p/5800758.html)

```java
//数组在物理上是连续存储的，flags数组用来保存id的状态（是否可消费、是否可填充），在填入id和消费id时，会被频繁的修改。
    //如果不进行缓存行填充，会导致频繁的缓存行失效，直接从内存中读数据。
    private final PaddedAtomicLong[] flags;

    //tail和cursor都使用缓存行填充，是为了避免tail和cursor落到同一个缓存行上。
    /** Tail: last position sequence to produce */
    private final AtomicLong tail = new PaddedAtomicLong(START_POINT);

    /** Cursor: current position sequence to consume */
    private final AtomicLong cursor = new PaddedAtomicLong(START_POINT)
```

##### PaddedAtomicLong的设计

```java
/**
 * Represents a padded {@link AtomicLong} to prevent the FalseSharing problem<p>
 * 
 * The CPU cache line commonly be 64 bytes, here is a sample of cache line after padding:<br>
 * 64 bytes = 8 bytes (object reference) + 6 * 8 bytes (padded long) + 8 bytes (a long value)
 * @author yutianbao
 */
public class PaddedAtomicLong extends AtomicLong {
    private static final long serialVersionUID = -3415778863941386253L;

    /** Padded 6 long (48 bytes) */
    public volatile long p1, p2, p3, p4, p5, p6 = 7L;

    /**
     * Constructors from {@link AtomicLong}
     */
    public PaddedAtomicLong() {
        super();
    }

    public PaddedAtomicLong(long initialValue) {
        super(initialValue);
    }

    /**
     * To prevent GC optimizations for cleaning unused padded references
     */
    public long sumPaddingToPreventOptimization() {
        return p1 + p2 + p3 + p4 + p5 + p6;
    }

}
```

### Spring Boot工程集成全局唯一ID生成器 UidGenerator

#### 基础工程创建

[官网集成文档](https://github.com/baidu/uid-generator/blob/master/README.zh_cn.md)

#### 创建数据表

执行如下SQL

```sql
DROP TABLE IF EXISTS WORKER_NODE;
CREATE TABLE WORKER_NODE
(
ID BIGINT NOT NULL AUTO_INCREMENT COMMENT 'auto increment id',
HOST_NAME VARCHAR(64) NOT NULL COMMENT 'host name',
PORT VARCHAR(64) NOT NULL COMMENT 'port',
TYPE INT NOT NULL COMMENT 'node type: ACTUAL or CONTAINER',
LAUNCH_DATE DATE NOT NULL COMMENT 'launch date',
MODIFIED TIMESTAMP NOT NULL COMMENT 'modified time',
CREATED TIMESTAMP NOT NULL COMMENT 'created time',
PRIMARY KEY(ID)
)
 COMMENT='DB WorkerID Assigner for UID Generator',ENGINE = INNODB;
```

在使用的数据库中创建表WORKER_NODE。(如果数据库版本较低,需要将TIMESTAMP类型换成datetime(3)，一劳永逸的做法就是直接将TIMESTAMP换成datetime(3))


#### 引入Maven依赖

```xml
<dependencies>
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-web</artifactId>
	</dependency>
	<dependency>
		<groupId>org.mybatis.spring.boot</groupId>
		<artifactId>mybatis-spring-boot-starter</artifactId>
		<version>2.1.0</version>
	</dependency>
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-test</artifactId>
		<scope>test</scope>
	</dependency>
	<!--for Mysql-->
	<dependency>
		<groupId>mysql</groupId>
		<artifactId>mysql-connector-java</artifactId>
		<scope>runtime</scope>
		<version>8.0.12</version>
	</dependency>
	<!-- druid -->
	<dependency>
		<groupId>com.alibaba</groupId>
		<artifactId>druid-spring-boot-starter</artifactId>
		<version>1.1.9</version>
	</dependency>
	<!--必须放在最后-->
	<dependency>
		<groupId>com.baidu.fsg</groupId>
		<artifactId>uid-generator</artifactId>
		<version>1.0.0-SNAPSHOT</version>
	</dependency>
</dependencies>

```

#### 互联网jar包引入(本文用的是此方式)

在maven仓库只找到了一个jar包。

```xml
<dependency>
    <groupId>com.xfvape.uid</groupId>
    <artifactId>uid-generator</artifactId>
    <version>0.0.4-RELEASE</version>
</dependency>
```

###### 排除冲突的依赖

uid-generator中依赖了logback和mybatis。一般在项目搭建过程中，springboot中已经有了logback依赖，mybatis会作为单独的依赖引入。如果版本和uid-generator中的依赖不一致的话，就会导致冲突。为了防止出现这些问题，直接排除一劳永逸。

```xml
<dependency>
    <groupId>com.baidu.fsg</groupId>
    <artifactId>uid-generator</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <exclusions>
        <exclusion>
            <groupId>org.mybatis</groupId>
            <artifactId>*</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

排除冲突的依赖如下：(使用本地项目引入的方式也需要排除以下依赖)

```
<dependency>
    <groupId>com.xfvape.uid</groupId>
    <artifactId>uid-generator</artifactId>
    <version>0.0.4-RELEASE</version>
    <exclusions>
        <exclusion>
            <groupId>org.mybatis</groupId>
            <artifactId>*</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

我这里用的是mybatis-plus，mybatis-plus官方要求的是，如果要使用mybatis-plus，就不能再单独引入mybatis了，所以我这里也是必须排除mybatis的。

#### 配置SpringBoot核心配置

修改配置文件application.properties（注意MySQL地址、数据库名称账户等于之前建表的保持一致）

````
server.port=9999
spring.datasource.url=jdbc:mysql://*.*.*.*:3306/baiduUidGenerator?useUnicode=true&characterEncoding=utf-8&useSSL=false
spring.datasource.username=root
spring.datasource.password=*
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
mybatis.mapper-locations=classpath:mapper/*.xml
mybatis.configuration.map-underscore-to-camel-case=true
````

#### @MapperScan的dao层接口扫描：

```java
@MapperScan({"com.xxx.xx.dao"})
```

#### 核心对象装配为spring的bean。

uid-generator提供了两种生成器: DefaultUidGenerator、CachedUidGenerator。

如对UID生成性能有要求, 请使用CachedUidGenerator。这里装配CachedUidGenerator，DefaultUidGenerator装配方式是一样的。

#### 自定义DisposableWorkerIdAssigner

将源码DisposableWorkerIdAssigner类加入到自己的项目中，并将其中的mapper方法修改成自己项目中的方法与启动类同级目录新建DisposableWorkerIdAssigner内容如下

```java

/**
 * Represents an implementation of {@link WorkerIdAssigner},
 * the worker id will be discarded after assigned to the UidGenerator
 * 
 * @author yutianbao
 */
public class DisposableWorkerIdAssigner implements WorkerIdAssigner {
    private static final Logger LOGGER = LoggerFactory.getLogger(DisposableWorkerIdAssigner.class);

    @Autowired
    private WorkerNodeMapper workerNodeMapper;

    /**
     * Assign worker id base on database.<p>
     * If there is host name & port in the environment, we considered that the node runs in Docker container<br>
     * Otherwise, the node runs on an actual machine.
     * 
     * @return assigned worker id
     */
    @Override
    @Transactional
    public long assignWorkerId() {
        // build worker node entity
        WorkerNodeEntity workerNodeEntity = buildWorkerNode();

        // add worker node for new (ignore the same IP + PORT)
        workerNodeMapper.addWorkerNode(workerNodeEntity);
        LOGGER.info("Add worker node:" + workerNodeEntity);

        return workerNodeEntity.getId();
    }

    /**
     * Build worker node entity by IP and PORT
     */
    private WorkerNodeEntity buildWorkerNode() {
        WorkerNodeEntity workerNodeEntity = new WorkerNodeEntity();
        if (DockerUtils.isDocker()) {
            workerNodeEntity.setType(WorkerNodeType.CONTAINER.value());
            workerNodeEntity.setHostName(DockerUtils.getDockerHost());
            workerNodeEntity.setPort(DockerUtils.getDockerPort());
        } else {
            workerNodeEntity.setType(WorkerNodeType.ACTUAL.value());
            workerNodeEntity.setHostName(NetUtils.getLocalAddress());
            workerNodeEntity.setPort(System.currentTimeMillis() + "-" + RandomUtils.nextInt(100000));
        }
        return workerNodeEntity;
    }
}
```


```java
/**
 * <p> 百度  Uid-Generator配置
 * @author zepal
 * */
@Configuration
public class UidGeneratorConfig {


@Bean("disposableWorkerIdAssigner")
public DisposableWorkerIdAssigner disposableWorkerIdAssigner(){
	DisposableWorkerIdAssigner disposableWorkerIdAssigner = new DisposableWorkerIdAssigner();
	return  disposableWorkerIdAssigner;
}

@Bean("cachedUidGenerator")
	public CachedUidGenerator initCachedUidGenerator(WorkerIdAssigner workerIdAssigner) {
		CachedUidGenerator cachedUidGenerator = new CachedUidGenerator();
		cachedUidGenerator.setWorkerIdAssigner(workerIdAssigner);
		// 属性参考链接  https://github.com/baidu/uid-generator/blob/master/README.zh_cn.md
        // 以下为可选配置, 如未指定将采用默认值
		// cachedUidGenerator.setTimeBits(28);
		// cachedUidGenerator.setWorkerBits(22);
		// cachedUidGenerator.setSeqBits(13);
		// cachedUidGenerator.setEpochStr("2016-09-20");
		cachedUidGenerator.setBoostPower(3);
		cachedUidGenerator.setPaddingFactor(50);
		cachedUidGenerator.setScheduleInterval(60L);
//		// 拒绝策略: 当环已满, 无法继续填充时
		// 默认无需指定, 将丢弃Put操作, 仅日志记录. 如有特殊需求, 请实现RejectedPutBufferHandler接口(支持Lambda表达式)
		// 拒绝策略: 当环已空, 无法继续获取时
		// 默认无需指定, 将记录日志, 并抛出UidGenerateException异常. 如有特殊需求, 请实现RejectedTakeBufferHandler接口(支持Lambda表达式)
		return cachedUidGenerator;
	}
}
```

##### 详细配置信息控制

```java
 /**
     * disposableWorkerIdAssigner的入参对象类型最好使用 WorkerIdAssigner，
     * 否则其他地方引入CGLib动态代理的时候可能会导致代理混用的问题
     *
     * @param disposableWorkerIdAssigner
     * @return
     */
    @Bean
    public DefaultUidGenerator defaultUidGenerator(WorkerIdAssigner disposableWorkerIdAssigner) {
        DefaultUidGenerator defaultUidGenerator = new DefaultUidGenerator();
     defaultUidGenerator.setWorkerIdAssigner(disposableWorkerIdAssigner);
        /**
         * 关于UID比特分配的建议：
         * 对于并发数要求不高、期望长期使用的应用, 可增加timeBits位数, 减少seqBits位数.
         * 例如节点采取用完即弃的WorkerIdAssigner策略, 重启频率为12次/天, 那么配置成：
         * {"workerBits":23,"timeBits":31,"seqBits":9}时, 可支持28个节点以整体并发量14400 UID/s的速度持续运行68年.
         *
         * 对于节点重启频率频繁、期望长期使用的应用, 可增加workerBits和timeBits位数, 减少seqBits位数.
         * 例如节点采取用完即弃的WorkerIdAssigner策略, 重启频率为24*12次/天, 那么配置成：
         * {"workerBits":27,"timeBits":30,"seqBits":6}时, 可支持37个节点以整体并发量2400 UID/s的速度持续运行34年.
         */
        //以下为可选配置, 如未指定将采用默认值
        defaultUidGenerator.setTimeBits(32);
        // 机器id，最多可支持2^22约420w次机器启动。内置实现为在启动时由数据库分配，默认分配策略为用后即弃，后续可提供复用策略。
        defaultUidGenerator.setWorkerBits(22);
        // 每秒下的并发序列，9 bits可支持每台服务器每秒512个并发。
        defaultUidGenerator.setSeqBits(9);
        defaultUidGenerator.setEpochStr("2020-01-01");

        return defaultUidGenerator;
    }
  /**
     * disposableWorkerIdAssigner的入参对象类型最好使用 WorkerIdAssigner，
     * 否则其他地方引入CGLib动态代理的时候可能会导致代理混用的问题
     *
     * @param disposableWorkerIdAssigner
     * @return
     */
    @Bean
    public CachedUidGenerator cachedUidGenerator(WorkerIdAssigner disposableWorkerIdAssigner) {
        CachedUidGenerator cachedUidGenerator = new CachedUidGenerator();
        cachedUidGenerator.setWorkerIdAssigner(disposableWorkerIdAssigner);
        /**
         * 关于UID比特分配的建议：
         * 对于并发数要求不高、期望长期使用的应用, 可增加timeBits位数, 减少seqBits位数.
         * 例如节点采取用完即弃的WorkerIdAssigner策略, 重启频率为12次/天, 那么配置成：
         * {"workerBits":23,"timeBits":31,"seqBits":9}时, 可支持28个节点以整体并发量14400 UID/s的速度持续运行68年.
         *
         * 对于节点重启频率频繁、期望长期使用的应用, 可增加workerBits和timeBits位数, 减少seqBits位数.
         * 例如节点采取用完即弃的WorkerIdAssigner策略, 重启频率为24*12次/天, 那么配置成：
         * {"workerBits":27,"timeBits":30,"seqBits":6}时, 可支持37个节点以整体并发量2400 UID/s的速度持续运行34年.
         */
        //以下为可选配置, 如未指定将采用默认值
        cachedUidGenerator.setTimeBits(32);
        // 机器id，最多可支持2^22约420w次机器启动。内置实现为在启动时由数据库分配，默认分配策略为用后即弃，后续可提供复用策略。
        cachedUidGenerator.setWorkerBits(22);
        // 每秒下的并发序列，9 bits可支持每台服务器每秒512个并发。
        cachedUidGenerator.setSeqBits(9);
        cachedUidGenerator.setEpochStr("2020-01-01");

        //RingBuffer size扩容参数, 可提高UID生成的吞吐量
        //默认:3， 原bufferSize=8192, 扩容后bufferSize= 8192 << 3 = 65536
        cachedUidGenerator.setBoostPower(3);
        // 指定何时向RingBuffer中填充UID, 取值为百分比(0, 100), 默认为50
        // 举例: bufferSize=1024, paddingFactor=50 -> threshold=1024 * 50 / 100 = 512.
        // 当环上可用UID数量 < 512时, 将自动对RingBuffer进行填充补全
        //<property name="paddingFactor" value="50"></property>

        //另外一种RingBuffer填充时机, 在Schedule线程中, 周期性检查填充
        //默认:不配置此项, 即不实用Schedule线程. 如需使用, 请指定Schedule线程时间间隔, 单位:秒
        cachedUidGenerator.setScheduleInterval(60L);

        //拒绝策略: 当环已满, 无法继续填充时
        //默认无需指定, 将丢弃Put操作, 仅日志记录. 如有特殊需求, 请实现RejectedPutBufferHandler接口(支持Lambda表达式)
        //<property name="rejectedPutBufferHandler" ref="XxxxYourPutRejectPolicy"></property>
        //cachedUidGenerator.setRejectedPutBufferHandler();
        //拒绝策略: 当环已空, 无法继续获取时 -->
        //默认无需指定, 将记录日志, 并抛出UidGenerateException异常. 如有特殊需求, 请实现RejectedTakeBufferHandler接口(支持Lambda表达式) -->
        //<property name="rejectedTakeBufferHandler" ref="XxxxYourTakeRejectPolicy"></property>

        return cachedUidGenerator;
    }
```





#### mapper服务接口

与启动类同级目录新建WorkerNodeMapper内容如下

```java
@Repository
public interface WorkerNodeMapper {
    /**
     * Get {@link WorkerNodeEntity} by node host
     * 
     * @param host
     * @param port
     * @return
     */
    WorkerNodeEntity getWorkerNodeByHostPort(@Param("host") String host, @Param("port") String port);

    /**
     * Add {@link WorkerNodeEntity}
     * 
     * @param workerNodeEntity
     */
    void addWorkerNode(WorkerNodeEntity workerNodeEntity);
}
```

#### WorkerNodeMapper

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="org.zxp.uidgeneratortest.WorkerNodeMapper">
    <resultMap id="workerNodeRes"
               type="com.baidu.fsg.uid.worker.entity.WorkerNodeEntity">
        <id column="ID" jdbcType="BIGINT" property="id"/>
        <result column="HOST_NAME" jdbcType="VARCHAR" property="hostName"/>
        <result column="PORT" jdbcType="VARCHAR" property="port"/>
        <result column="TYPE" jdbcType="INTEGER" property="type"/>
        <result column="LAUNCH_DATE" jdbcType="DATE" property="launchDate"/>
        <result column="MODIFIED" jdbcType="TIMESTAMP" property="modified"/>
        <result column="CREATED" jdbcType="TIMESTAMP" property="created"/>
    </resultMap>

    <insert id="addWorkerNode" useGeneratedKeys="true" keyProperty="id"
            parameterType="com.baidu.fsg.uid.worker.entity.WorkerNodeEntity">
		INSERT INTO WORKER_NODE
		(HOST_NAME,
		PORT,
		TYPE,
		LAUNCH_DATE,
		MODIFIED,
		CREATED)
		VALUES (
		#{hostName},
		#{port},
		#{type},
		#{launchDate},
		NOW(),
		NOW())
	</insert>

    <select id="getWorkerNodeByHostPort" resultMap="workerNodeRes">
		SELECT
		ID,
		HOST_NAME,
		PORT,
		TYPE,
		LAUNCH_DATE,
		MODIFIED,
		CREATED
		FROM
		WORKER_NODE
		WHERE
		HOST_NAME = #{host} AND PORT = #{port}
	</select>
</mapper>

```




#### 创建UidGenService逻辑类

```java
@Service
public class UidGenService {
    @Resource
    private UidGenerator uidGenerator;
    public long getUid() {
        return uidGenerator.getUID();
    }
}
```



### 参考资料

[一个Java对象到底占用多大内存？](https://www.cnblogs.com/magialmoon/p/3757767.html)

[写Java也得了解CPU--伪共享]()https://www.cnblogs.com/techyc/p/3625701.html 

https://www.jianshu.com/p/947bff7be2da

https://blog.csdn.net/qq_43578870/article/details/105495740
