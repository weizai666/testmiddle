/**
 * Copyright [2020] [LiBo/Alex of copyright liboware@gmail.com ]
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
package com.hyts.assemble.eventbus.disruptor;

import cn.hutool.core.thread.ExecutorBuilder;
import com.hyts.assemble.eventbus.EventListener;
import com.hyts.assemble.eventbus.EventListenerRegistry;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @project-name:lhy-report
 * @package-name:com.lhy.lhy.report.event
 * @author:LiBo/Alex
 * @create-date:2021-09-25 00:36
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@Slf4j
//@ConditionalOnExpression("'${event-bus.model}'.equalsIgnoreCase('disruptor')")
@Scope("prototype")
@Component("disruptor")
public class DisruptorEventListenerRegistry implements AutoCloseable, EventListenerRegistry<EventModel> {

    /**
     * disruptor事件处理器
     */
    @Setter
    @Getter
    private Disruptor<EventModel> disruptor;

    /**
     * 事件处理链表
     */
    @NonNull
    final List<EventListener> eventHandlers;



    public Translator TRANSLATOR = new Translator();

    /**
     * RingBuffer 大小，必须是 2 的 N 次方；
     */
    private final int DEFAULT_RING_BUFFER_SIZE = 1024 * 1024;

    /**
     * 事件工厂类
     */
    private EventFactory<EventModel> eventFactory = new EventModelFactory();

    /**
     * 线程工厂类
     */
   // private ThreadFactory threadFactory =  r -> new Thread(r,"EventModelManager"+System.currentTimeMillis());




    public DisruptorEventListenerRegistry(@NonNull List<EventListener> eventHandlers) {
        this.eventHandlers = eventHandlers;
    }


    /**
     * EventFactory构造器服务机制
     */
    @SuppressWarnings("all")
    @PostConstruct
    public void init(){
        log.info("开始初始化Disruptor事件监听器的组件服务");
        initRegistryEventListener(eventHandlers);
        log.info("完成初始化Disruptor事件监听器的组件服务");
    }


    @Override
    public void initRegistryEventListener(List<EventListener> eventConsumerList) {
// 构造器实际线程池
        disruptor = new Disruptor<>(eventFactory, DEFAULT_RING_BUFFER_SIZE,
                createThreadFactory(), ProducerType.SINGLE, new BlockingWaitStrategy());
        EventHandler[] dataListener = eventConsumerList.stream().
                map(param->{
            EventListener<EventModel> eventModelEventListener = param;
            return eventModelEventListener;
        }).collect(Collectors.toList()).toArray(new EventHandler[eventConsumerList.size()]);
        log.info("注册服务信息接口:{}",dataListener);
        disruptor.handleEventsWith(dataListener);
        //顾名思义：执行完t1后执行t2。（对同一个任务线性执行）
//        disruptor.after(t1).handleEventsWith(t2)。
        disruptor.start();
    }




    @Override
    public void publish(EventModel param) {
        publishEvent(param);
    }



    /**
     * publish 发布事件数据对象模型
     */
    @SuppressWarnings("all")
    public void publishEvent(EventModel... eventModels){
        Objects.requireNonNull(disruptor,"当前disruptor核心控制器不可以为null！");
        Objects.requireNonNull(eventModels,"当前eventModels事件控制器不可以为null！");
        // 发布事件；
        RingBuffer ringBuffer = disruptor.getRingBuffer();
        try {
            //获取要通过事件传递的业务数据；
            List<EventModel> dataList = Arrays.stream(eventModels).
                    collect(Collectors.toList());
            for(EventModel element:dataList){
                //请求下一个事件序号；
                long sequence = ringBuffer.next();
                //获取该序号对应的事件对象；
                EventModel event = (EventModel) ringBuffer.get(sequence);
                event.setTopic(element.getTopic());
                event.setEntity(element.getEntity());
                ringBuffer.publish(sequence);
            }
        }catch (Exception e){
            log.error("error",e);
        };
    }


    /**
     * 关闭操作处理机制
     * @throws Exception
     */
    @Override
    public void close() throws Exception {
        if(Objects.nonNull(disruptor)){
            disruptor.shutdown();
        }
    }



    /**
     * 转换器模型
     */
    public class Translator implements EventTranslatorOneArg<EventModel, EventModel> {
        @Override
        public void translateTo(EventModel event, long sequence, EventModel data) {

        }
    }


    /**
     * 发送事件模型
     */
    @SuppressWarnings("all")
    public void sendEvent(EventModel... events){
       // 注意，最后的 ringBuffer.
       // publish 方法必须包含在 finally 中以确保必须得到调用；如果某个请求的 sequence 未被提交，将会堵塞后续的发布操作或者其它的 producer。
       // Disruptor 还提供另外一种形式的调用来简化以上操作，并确保 publish 总是得到调用。
        RingBuffer ringBuffer = disruptor.getRingBuffer();
        //获取要通过事件传递的业务数据；
        for(EventModel event:events){
            ringBuffer.publishEvent(TRANSLATOR,event);
        }
    }


    /**
     * 线程工厂类
     * @return
     */
    public static ThreadFactory createThreadFactory(){
        AtomicInteger integer = new AtomicInteger();
        return  r ->
            new Thread(r,"disruptor-"+integer.incrementAndGet());
    }

    /**
     * 创建执行器
     * @return
     */
    public static Executor createExecutor(){
        return  ExecutorBuilder.create().setCorePoolSize(Runtime.getRuntime().availableProcessors()).
                setMaxPoolSize(100).setKeepAliveTime(60, TimeUnit.SECONDS).setWorkQueue(new ArrayBlockingQueue(200)).
                setThreadFactory(createThreadFactory()).setHandler(new
                ThreadPoolExecutor.DiscardOldestPolicy()).build();
    }


}
