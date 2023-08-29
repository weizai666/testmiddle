/**
 * Copyright [2019] [LiBo/Alex of copyright liboware@gmail.com ]
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hyts.assemble.taskqueue.core;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.thread.ThreadUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hyts.assemble.taskqueue.model.ExecuteTaskDataDTO;
import com.hyts.assemble.taskqueue.model.HashQueueElement;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @project-name:wiz-sound-ai2
 * @package-name:com.wiz.soundai.task.config.redis
 * @author:LiBo/Alex
 * @create-date:2021-09-24 16:38
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description: 简单的分区队列控制执行操作服务管理器
 */


@Slf4j
@Component
public class PartitionQueueManager {

    /**
     * 数据信息
     */
    public static final int CURRENT_EXECUTE_DO_SIZE = Runtime.getRuntime().availableProcessors();
//Math.ceil(
////        // Num IO threads (top out at 4, since most I/O devices won't scale better than this)
////        Math.min(4.0, Runtime.getRuntime().availableProcessors() * 0.75) +
////            // Num scanning threads (higher than available processors, because some threads can be blocked)
////            Runtime.getRuntime().availableProcessors() * 1.25)
    /**
     * 缩编化的数据信息
     */
    public static final int CURRENT_EXECUTE_DO_SIZE_SW = Runtime.getRuntime().availableProcessors()>>1;



    public static final String HASH_SEQ_NUMBER_1 = "CONTACT_INFO_PUSH_COUNTER";


    public static final String HASH_SEQ_NUMBER_2 = "CONTACT_INFO_POLL_COUNTER";


    @Autowired
    ApplicationContext applicationContext;


    @Getter
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${taskQueue.workIncrementFactor:1}")
    private int checkIntervalTimeoutSeconds;


    @Value("${taskQueue.sleepIdleTimeoutSeconds:30}")
    private int sleepIdleTimeoutSeconds;


    @Value("${taskQueue.printTaskIntervalSeconds:5}")
    private int printTaskIntervalSeconds;


    @PostConstruct
    public void initRedisTemplate(){
        try {
            redisTemplate = applicationContext.getBean("redisTemplate",RedisTemplate.class);
        } catch (Exception e) {
            log.error("init the redistemplate is faliure!",e);
        }
    }

    /**
     * 工厂方法
     * @param BizNo
     * @param element
     * @param <T>
     * @return
     */
    public static <T> HashQueueElement createHashQueueElement(Long BizNo, T element){
        // TODO 未来可以实现拷贝，提高创建速度     @Contended 防止伪共享
        return new HashQueueElement(BizNo,element);
    }


    /**
     * add阻塞队列元素控制机制
     * @param key
     * @param entity
     * @param <T>
     */
    public <T> Long addBlockingElement(String key,  T entity) {
        Long length = redisTemplate.opsForList().leftPush(key, entity);
        Long expireTime = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        if(Objects.nonNull(expireTime) && expireTime < 60){
            log.debug("key:{}  ttl  {} is less than 60 seconds will be update ttl to long",key,expireTime);
            redisTemplate.expire(key, 3, TimeUnit.HOURS);
        }
        // 添加计数器操作控制
        try {
            redisTemplate.opsForHash().increment(HASH_SEQ_NUMBER_1,key,1L);
        } catch (Exception e) {
            log.error("add hash counter is failure!",e);
        }
        return length;
    }

    /**
     * get阻塞队列元素控制机制
     * @param key
     */
    public <T> T popBlockingElement(String key) {
        // 添加计数器操作控制
        return (T) redisTemplate.opsForList().rightPop(key, 3, TimeUnit.SECONDS);
    }

    /**
     *
     * @param partionPrefix
     * @param hashSlotNum
     * @return
     */
    public Long  size(String partionPrefix, int hashSlotNum) {
        return redisTemplate.opsForList().size(partionPrefix+hashSlotNum);
    }
    /**
     * 算法执行值
     * @param paramNo
     * @param hashslot
     * @return
     */
    public static Long hashSlotToModValue(Long paramNo, int hashslot){
        return Objects.requireNonNull(paramNo,"paramNo is not null!") & ((hashslot)-1);
    }

    /**
     * 计算hash值
     * @param paramNo
     * @param hashslot
     * @return
     */
    public static Long hashSlotToValueComp(Long paramNo,int hashslot){
        Objects.requireNonNull(paramNo,"paramNo is not null!");
        while(!(((hashslot - 1) & hashslot) == 0 )){
            hashslot++;
        }
        log.debug("the hash slot value:{}",hashslot);
        return hashSlotToModValue(paramNo,hashslot);
    }

    /**
     * hash算法计算模运算机制
     * @param paramNo
     * @return
     */
    public static String hashModTransferPartition(String prefixName,Long paramNo,int hashslot){
        // 必须是2的n次方
        long hashslotValue = hashSlotToValueComp(paramNo,hashslot);
        return String.format(prefixName+"%s",hashslotValue);
    }

    /**
     * 自动分区操作控制
     * @param partionPrefix 队列前缀
     * @param entity
     * @param <T>
     * @return
     */
    public <T> Long addAutoPartitionBlockingElement(String partionPrefix,HashQueueElement<T> entity,int hashSlotNum){
        //2 个 hash母槽 算法机制 会直接衍生为 4 个 hash槽
        Preconditions.checkArgument(StringUtils.isNotBlank(partionPrefix),"partitionPrefix is not null!");
        Preconditions.checkNotNull(entity,"entity is not null!");
        //将同一个类型下的数据作为同一个队列的数据信息，串行处理机制。
        String key = hashModTransferPartition(partionPrefix,entity.getHashResourceId(),hashSlotNum);
        return addBlockingElement(key, entity.getElement());
    }

    /**
     * 充分发挥CPU能力，伴随这核心数据越多，划分的数据越多
     * @param partionPrefix
     * @param entity
     * @param <T>
     * @return
     */
    public <T> Long addAutoPartitionBlockingElement(String partionPrefix, HashQueueElement<T> entity){
        return addAutoPartitionBlockingElement(partionPrefix,entity,CURRENT_EXECUTE_DO_SIZE);
    }

    /**
     * 获取相关的数据信息
     * @param partionPrefix 队列队列分区前缀
     * @param hashResourceId hash算法的输入条件值
     * @return
     */
    public <T> Long pollAutoPartitionBlockingElement(String partionPrefix,Long hashResourceId){
        return pollAutoPartitionBlockingElement(partionPrefix,hashResourceId,CURRENT_EXECUTE_DO_SIZE);
    }

    /**
     * 获取相关的数据信息
     * @param partionPrefix 队列队列分区前缀
     * @param hashResourceId hash算法的输入条件值
     * @param hashSlotNum 手动指定hash slot值数据
     * @return
     */
    public <T> T pollAutoPartitionBlockingElement(String partionPrefix,Long hashResourceId,int hashSlotNum){
        //2 个 hash母槽 算法机制 会直接衍生为 4 个 hash槽
        Preconditions.checkArgument(StringUtils.isNotBlank(partionPrefix),"partitionPrefix is not null!");
        Preconditions.checkNotNull(hashResourceId,"hashResourceId is not null!");
        String key = hashModTransferPartition(partionPrefix,hashResourceId,hashSlotNum);
        Long expireTime = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        if(Objects.nonNull(expireTime) && expireTime < 60){
            log.debug("key:{}  ttl  {} is less than 60 seconds will be update ttl to long",key,expireTime);
            redisTemplate.expire(key, 5, TimeUnit.HOURS);
        }
        try {
            T result = (T) redisTemplate.opsForList().rightPop(key, 3, TimeUnit.SECONDS);
            if(Objects.isNull(result)) {
                redisTemplate.opsForHash().increment(HASH_SEQ_NUMBER_2, key, 1L);
            }
            return result;
        } catch (Exception e) {
            log.error("remove hash counter is failure!",e);
            return null;
        }
    }



    /**
     * get阻塞队列元素控制机制
     * @param partionPrefix 前缀编码
     * @param hashSlotNum hashslot值
     */
    public <T> T pollPartitionBlockingElement(String partionPrefix, int hashSlotNum) {
        String key = partionPrefix+hashSlotNum;
        Long expireTime = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        if(Objects.nonNull(expireTime) && expireTime < 60){
            log.debug("key:{}  ttl  {} is less than 60 seconds will be update ttl to long",key,expireTime);
            redisTemplate.expire(key, 5, TimeUnit.HOURS);
        }
        try {
            T result =  (T) redisTemplate.opsForList().rightPop(key, 5, TimeUnit.SECONDS);
            if(Objects.nonNull(result)){
                redisTemplate.opsForHash().increment(HASH_SEQ_NUMBER_2,key,1L);
            }
            return result;
        } catch (Exception e) {
            log.error("remove hash counter is failure!",e);
            return null;
        }
    }

    /**
     * 启动所有针对于hashslot的线程执行器
     * @param workerName
     * @param hashSlotNum
     */
    public ExecutorService initPollExecuteWorker(String workerName, int hashSlotNum, Consumer runnable,
                                                 ExecutorService executorService){
        return initPollExecuteWorker(workerName,hashSlotNum,runnable,executorService,param->
            createThreadPoolExecutor(workerName,hashSlotNum,param,Sets.newConcurrentHashSet())
        );
    }

    /**
     * 创建线程池
     * @param workerName
     * @return
     */
    public static ThreadPoolExecutor createThreadPoolExecutor(String workerName,int hashSlotNum,int groupId,Set<Thread> threadSet){
        return ExecutorBuilder.create().setWorkQueue(new SynchronousQueue<>()).setCorePoolSize(0).setMaxPoolSize(1)
                .setThreadFactory(new WorkerThreadFactory(threadSet,workerName,groupId)).
                        setHandler((r, executor) -> resetThreadPoolExecutor(executor,groupId,hashSlotNum,workerName,threadSet)
        ).build();
    }

    /**
     * 重置线程池
     * @param workerExecutor
     * @return
     */
    public static void resetThreadPoolExecutor(ThreadPoolExecutor workerExecutor,int groupId,int hashSlotNum,String queuePrefix,Set<Thread> threadSet){
            try{
                log.info("reset the worker execute pool: {} current running taskCount :{} and pool size " +
                        " value:{}",queuePrefix+groupId,workerExecutor.getActiveCount(),workerExecutor.getPoolSize());
                workerExecutor.purge();
                workerExecutor.shutdownNow();
                threadSet.clear();
            }catch (Exception e){
                log.error("reset the execute pool is failure!",e);
            }
    }

    //线程工厂
    @Data
    @RequiredArgsConstructor
    private static class WorkerThreadFactory implements ThreadFactory {


        private final Set<Thread> threadsContainer;


        private final String workerName;


        private AtomicInteger integer = new AtomicInteger(0);


        private final int groupId;


        @Override
        public Thread newThread(Runnable r) {
            ThreadGroup threadGroup = new ThreadGroup(workerName+groupId);
            log.info("create the worker thread and group name is :{}",threadGroup.getName());
            Thread thread = new Thread(threadGroup,r,workerName+"WORK-"+integer.incrementAndGet());
            //cache thread 记录线程
            threadsContainer.add(thread);
            //删除不存活的线程
            return thread;
        }


        /**
         * 活跃性
         * @param threadGroupName
         * @return
         */
        public boolean isAlive(String threadGroupName){
            List<Thread> isAliveThread = threadsContainer.stream().filter(Thread::isAlive)
                    .filter(thread->thread.getThreadGroup().getName().equals(threadGroupName)).collect(Collectors.toList());
            log.debug("current container all thread:{} isAlive thread:{} validate groupName:{}",isAliveThread.size(),threadsContainer.size(),threadGroupName);
            boolean result = CollectionUtil.isNotEmpty(isAliveThread);
            threadsContainer.removeIf(next -> !next.isAlive());
            return result;
        }

    }


    /**
     * 启动所有针对于hashslot的线程执行器
     * @param workerName
     * @param hashSlotNum
     * @param runnable
     * @param executorService
     * @param workerExecutorFunction
     * @return
     */
    public ExecutorService initPollExecuteWorker(String workerName, int hashSlotNum, Consumer runnable,
                                                 ExecutorService executorService, Function<Integer,ExecutorService> workerExecutorFunction){
        ThreadPoolExecutor threadPoolTaskExecutor = (ThreadPoolExecutor) executorService;
        // 操作处理
        threadPoolTaskExecutor.setThreadFactory(new ThreadFactory() {
            private AtomicInteger integer = new AtomicInteger(0);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r,workerName+"POLL-"+integer.incrementAndGet());
            }
        });
        Map<String,Long> monitorThreadMonitorStatistic = Maps.newConcurrentMap();
        IntStream.range(0, hashSlotNum).forEach(param -> {
            log.info("bootstrap worker {}{}", workerName,param);
            threadPoolTaskExecutor.execute(new Thread(() ->
                    executeFinishData(hashSlotNum,param,workerName,
                            checkIntervalTimeoutSeconds,sleepIdleTimeoutSeconds,
                            runnable,
                            workerExecutorFunction.apply(param),
                            monitorThreadMonitorStatistic),
                    workerName.concat(String.valueOf(param))));
        });
        return threadPoolTaskExecutor;
    }

    /**
     * 初始化线程池指定的数据线程池
     * @param workerName
     * @param runnable
     * @param executorService
     */
    public ExecutorService initPollExecuteWorker(String workerName, Consumer runnable,ExecutorService executorService){
        return initPollExecuteWorker(workerName,CURRENT_EXECUTE_DO_SIZE,runnable, executorService);
    }

    /**
     * 初始化线程池指定相应的线程池
     * @param workerName
     * @param hashSlotNum
     * @param runnable
     */
    public ExecutorService initPollExecuteWorker(String workerName, int hashSlotNum, Consumer runnable){
        return initPollExecuteWorker(workerName,hashSlotNum,runnable,ThreadUtil.newExecutor(hashSlotNum,
                hashSlotNum+CURRENT_EXECUTE_DO_SIZE,
                200));
    }

    /**
     * 启动所有针对于hashslot的线程执行器
     * @param partionPrefix
     * @param runnable
     */
    public ExecutorService initPollExecuteWorker(String partionPrefix, Consumer runnable){
        return initPollExecuteWorker(partionPrefix,CURRENT_EXECUTE_DO_SIZE,runnable);
    }


    @Deprecated
    /**
     * 执行数据迁移操作控制机制
     */
    public void executeFinishData(int param, String queuePrefix, Consumer runnable){
        //循环获取
        while(true){
            try {
                Object dataElement =
                        pollPartitionBlockingElement(queuePrefix,param);
                if(Objects.isNull(dataElement)){
                    try {
                        Thread.sleep(2000);
                        // 暂时交出使用权
                    } catch (InterruptedException e) {
                        log.error("deal with process failure InterruptedException",e);
                    }
                }else {

                    log.info("queue read element data : {} ", dataElement);
                    runnable.accept(dataElement);
                    log.info("deal with process finished");
                }
            } catch (Exception e) {
                log.error("deal with process failure",e);
            }
        }
    }

    /**
     * execute finished data process
     * @param param
     * @param queuePrefix
     * @param runnable
     * @param executorService
     */
    public void executeFinishData(int hashSlotNum,int param, String queuePrefix,
                                  int timeOutIntervalSecond,int resetExecuteorThreshold,
                                  Consumer runnable,ExecutorService executorService,
                                  Map<String,Long> monitorThreadMonitorStatistic){
        ThreadPoolExecutor workerExecutor = (ThreadPoolExecutor)executorService;
        long lastTimestamp = System.currentTimeMillis(),currentTimeStamp = lastTimestamp;
        while(true){
            try {
                long offsetTimes = TimeUnit.SECONDS.toMillis(printTaskIntervalSeconds);
                if(offsetTimes < System.currentTimeMillis() - lastTimestamp){
                    log.info("poll thread:{} is running! current worker thread pool size:{} worker running task count:{} worker queue size:{}",
                            Thread.currentThread().getName(),
                            workerExecutor.getPoolSize(),workerExecutor.getActiveCount(),workerExecutor.getQueue().size());
                    lastTimestamp = System.currentTimeMillis();
                }
                // 判断是否关闭
                if (workerExecutor.isShutdown() || workerExecutor.isTerminated() || workerExecutor.isTerminating()) {
                    synchronized (monitorThreadMonitorStatistic) {
                        if (workerExecutor.isShutdown() || workerExecutor.isTerminated() || workerExecutor.isTerminating()) {
                            workerExecutor = createThreadPoolExecutor(queuePrefix,hashSlotNum,param,Sets.newConcurrentHashSet());
                        }
                    }
                }
                // 判断处理队列
                if(System.currentTimeMillis() > monitorThreadMonitorStatistic.
                        getOrDefault(queuePrefix+"-"+param,0L)+TimeUnit.SECONDS.toMillis(timeOutIntervalSecond)){
                    Object dataElement = pollPartitionBlockingElement(queuePrefix,param);
                    if(Objects.nonNull(dataElement)){
                        log.info("queue read element data : {} ", ((ExecuteTaskDataDTO)dataElement).getExecuteTaskId());
                        workerExecutor.execute(()->{
                            runnable.accept(dataElement);
                        });
                        log.info("deal with process finished workerExecutor running size: {}, poolsize:{},queue size:{}",
                                workerExecutor.getActiveCount(),workerExecutor.getPoolSize(),workerExecutor.getQueue().size());
                    }
                    monitorThreadMonitorStatistic.put(queuePrefix+"-"+param,System.currentTimeMillis());
                }else{
                    WorkerThreadFactory workerThreadFactory = (WorkerThreadFactory) workerExecutor.getThreadFactory();
                    Boolean isAlive = workerThreadFactory.isAlive(queuePrefix+param);
                    long size = size(queuePrefix,param);
                    if(size == 0 || isAlive) {
                        log.debug("sleep the task {} seconds!",sleepIdleTimeoutSeconds);
                        TimeUnit.SECONDS.sleep(sleepIdleTimeoutSeconds);
                    }
                }
            } catch (Exception e) {
                log.error("deal with process failure",e);
            }
        }
    }

}
