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
package com.hyts.assemble.taskqueue.aop;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.thread.threadlocal.NamedInheritableThreadLocal;
import com.alibaba.fastjson.JSONArray;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.hyts.assemble.taskqueue.anno.ExecuteMethodProxy;
import com.hyts.assemble.taskqueue.anno.GenericType;
import com.hyts.assemble.taskqueue.anno.PrimaryKeyField;
import com.hyts.assemble.taskqueue.constant.ExecuteAnalysisExpressionType;
import com.hyts.assemble.taskqueue.constant.ExecuteResultType;
import com.hyts.assemble.taskqueue.logic.AbstractExecuteTaskService;
import com.hyts.assemble.taskqueue.model.ExecuteMethodCallPoint;
import com.hyts.assemble.taskqueue.model.ExecuteTaskDataDTO;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.*;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.IntStream;

/**
 * @project-name:wiz-sound-ai2
 * @package-name:com.wiz.soundai.task.aop
 * @author:LiBo/Alex
 * @create-date:2021-09-28 9:52
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description: 执行调用目标代理
 */
@Slf4j
@Aspect
@Component
public class ExecuteTargetProxy {



    @Autowired(required = false)
    private Map<String,AbstractExecuteTaskService> executeTaskService;


    private RedisTemplate<String, Object> redisTemplate;


    @Autowired
    private ApplicationContext applicationContext;


    @PostConstruct
    public void initRedisTemplate(){
        try {
            redisTemplate = applicationContext.getBean("redisTemplate",RedisTemplate.class);
        } catch (BeansException e) {
            log.error("init the redistemplate is faliure!",e);
        }
    }


    @Getter
    private static final ExecuteTaskCurrentContext executeTaskCurrentContext = new ExecuteTaskCurrentContext();


    private static ExpressionParser expressionParser = new SpelExpressionParser();




    public static final Joiner JOINER = Joiner.on(",").skipNulls();


    /**
     * 配置织入点
     */
    @Pointcut("@annotation(com.hyts.assemble.taskqueue.anno.ExecuteMethodProxy)")
    public void methodPointCut() {}


    /**
     * 是否属于不需要序列化的参数操作
     * @param type
     */
    public static final Class isTransientClassType(Object type){
        // 判断是否属于忽略序列化属性
        if(type instanceof HttpServletRequest || type instanceof HttpServletResponse ){
            return type.getClass();
        }
        return null;
    }


    /**
     *  是否属于web请求服务
     * @return
     */
    public static final boolean isWebServletRequest(){
        return Objects.nonNull(RequestContextHolder
                .getRequestAttributes());
    }

    /**
     * 是否属于代理请求操作处理控制
     * @return
     */
    public static final boolean isProxyExecuteRequest(){
        return StringUtils.isNotEmpty(ExecuteTargetProxy.getExecuteTaskCurrentContext().getProxyEnv());
    }

    /**
     * 是否应该调用原始方法
     * @return
     */
    public static final boolean isCallMethodByNoWebEnvrionmentAndProxyEnv(){
        return !isWebServletRequest() || !isProxyExecuteRequest();
    }


    /**
     * 是否开启拦截动态代理模式
     * @param executeMethodProxy
     * @return
     */
    public static final boolean isEnableMethodProxySwitch(ExecuteMethodProxy executeMethodProxy){
        return executeMethodProxy.enable();
    }

    /**
     * 过滤参数列表数据信息
     * @param args
     * @return
     */
    public static Object[] filterArgumentList(Object... args){
        List<Object> argsTemp = Lists.newArrayListWithExpectedSize(args.length);
        for(int i = 0 ; i < args.length ; i++){
            // 当返回有class说明匹配到了不可序列化类
            Class clazz = isTransientClassType(args[i]);
            if(Objects.isNull(clazz)){
                argsTemp.add(args[i]);
                continue;
            }
            // 操作处理机制
            argsTemp.add(new AbstractExecuteTaskService.TransientClassWrapper(clazz.getName()));
        }
        return argsTemp.toArray(new Object[argsTemp.size()]);
    }


    /**
     * 处理完请求后执行
     */
    @Around("methodPointCut()")
    public Object doMethod(ProceedingJoinPoint proceedingJoinPoint){
        // 获得注解
        ExecuteMethodProxy executeMethodProxy = null;
        AbstractExecuteTaskService abstractExecuteTaskService = null;
        try {
            // 获得注解
            executeMethodProxy = getAnnotationByMethod(proceedingJoinPoint);
            abstractExecuteTaskService = executeTaskService.get(executeMethodProxy.executeTaskType().getSimpleName());
            if(isCallMethodByNoWebEnvrionmentAndProxyEnv()){
                // 校验开关是否打开
                if(isEnableMethodProxySwitch(executeMethodProxy)){
                    log.info("enable method to process the proxy method function !");
                    if(Objects.isNull(executeTaskCurrentContext.get())){
                        log.error("current the task is not the taskId to execute! ");
                        throw new RuntimeException("current the task is not the taskId to execute!");
                        //TODO 需要研究一下此种场景的问题！
                    }
//                    Object userCacheVO = redisTemplate.boundValueOps
//                            (ExecuteTaskService.DEFAULT_TASK_EXECUTE_CODE_PREFIX + executeTaskCurrentContext.get()).get();
//                    if(Objects.isNull(userCacheVO)){
//                        log.error("current the task is not the userCacheVO to execute! ");
//                        //TODO 需要研究一下此种场景的问题！
//                        throw new RuntimeException("current the task is not the userCacheVO to execute!");
//                    }
//                    //设置相关当前服务上下文中的authentication字段值
//                    executeTaskService.preparedCurrentExecuteTaskSession(userCacheVO);
                }
                return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
            }else{
                // 默认获取相关的用户注入信息数据
                log.info("find enable or target method to process the proxy method function !");
                // 请求的参数
                Object[] args = proceedingJoinPoint.getArgs();
                MethodSignature methodSignature = (MethodSignature)proceedingJoinPoint.getSignature();
                Parameter[] parameters = methodSignature.getMethod().getParameters();
                if(Objects.nonNull(args) && args.length > 0){
                    // 设置方法名称
                    String className = proceedingJoinPoint.getTarget().getClass().getName();
                    String methodName = proceedingJoinPoint.getSignature().getName();
                    Object[] realCallParam = filterArgumentList(args);
                    //注意此部分是带有非序列化的参数信息
                    String[] parameterClassList = new String[realCallParam.length];
                    // 顺序执行操作
                    IntStream.range(0,realCallParam.length).forEach(param->{
                        //控制集合属性
                        if(realCallParam[param] instanceof Collection || realCallParam[param] instanceof Map){
                            parameterClassList[param] = getParameterGenerateTypeByGenericType(parameters[param]).getName();
                        }else {
                            parameterClassList[param] = Objects.nonNull(realCallParam[param])?realCallParam[param].getClass().
                                    getName():parameters[param].getType().getName();
                        }
                    });
                    ExecuteMethodCallPoint executeMethodCallPoint = new
                            ExecuteMethodCallPoint(className,methodName,JOINER.join(parameterClassList) ,JSONArray.toJSONString(realCallParam));
                    // 获取参数上面的指定的参数值
                    Object[] value = getParameterValueByPrimaryKeyField(proceedingJoinPoint);
                    log.info("get the annotation the primary key value is:{}",value);
                    ExecuteTaskDataDTO executeTaskDataDTO = new ExecuteTaskDataDTO(0L,
                            String.valueOf(value[0]), // primaryKey
                            Byte.valueOf(String.valueOf(value[1])), // taskExecuteType
                            executeMethodProxy.executeTaskType(),// executeTaskType
                            executeMethodCallPoint);
                    // 错误的是也要进入任务列表，保障数据的一致性！
                    log.info("the data is finished to task queue:{}",executeTaskDataDTO);
                    abstractExecuteTaskService.addExecuteTask(executeTaskDataDTO);
                }
                return null;
            }
        } catch (Throwable exp) {
            // 记录本地异常日志
            // 监控在系统层面执行任务入队的时候出现的错误问题！
            log.error("execute proxy method is failure!",exp);
            // 只有在初始化阶段调用错误，才会进入该方法！
            if(isCallMethodByNoWebEnvrionmentAndProxyEnv() && isEnableMethodProxySwitch(executeMethodProxy) &&
                    Objects.isNull(getExecuteTaskCurrentContext().get())) {
                Object[] value = new Object[0];
                try {
                    value = getParameterValueByPrimaryKeyField(proceedingJoinPoint);
                } catch (Exception e) {
                    log.error("system error is not record  the data info!", e);
                }
                // 更新相关的数据信息状态操作
                ExecuteTaskDataDTO executeTaskDataDTO = new ExecuteTaskDataDTO(0L, String.valueOf(value[0]), // primaryKey
                        Byte.valueOf(String.valueOf(value[1])), // taskExecuteType
                        null,// executeTaskType
                        ExceptionUtil.stacktraceToOneLineString((ExceptionUtil.getRootCause(exp)), 250),
                        ExecuteResultType.FAILURE.getType());
                abstractExecuteTaskService.addExecuteTask(executeTaskDataDTO);
                return null;
            }
            throw new RuntimeException(exp);
        }finally {
            ExecuteTargetProxy.getExecuteTaskCurrentContext().clearProxyEnv();
            //  延迟到了任务执行阶段进行清空数据
            //executeTaskCurrentContext.clear();
        }
    }


    /**
     * 获取相关注解，如果存在就获取
     */
    private ExecuteMethodProxy getAnnotationByMethod(ProceedingJoinPoint joinPoint) throws Exception {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (Objects.nonNull(method)) {
            return method.getAnnotation(ExecuteMethodProxy.class);
        }
        return null;
    }




    private Class[] processTheParameterGenericType(ProceedingJoinPoint proceedingJoinPoint){
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        System.out.println(ResolvableType.forMethodParameter(MethodParameter.forParameter(methodSignature.getMethod().getParameters()[0])).getRawClass());
        System.out.println(ResolvableType.forMethodParameter(MethodParameter.forParameter(methodSignature.getMethod().getParameters()[0])).getRawClass());
        Type[] classes = methodSignature.getMethod().getGenericParameterTypes();
        System.out.println(((ParameterizedType)classes[0]).getRawType().getTypeName());
        System.out.println(((ParameterizedType)classes[0]).getActualTypeArguments()[0].getTypeName());
        System.out.println(((ParameterizedType)classes[0]).getOwnerType().getTypeName());
        return null;
    }



    /**
     * 执行获取泛型操作处理机制控制
     * @return
     */
    public static Class getParameterGenerateTypeByGenericType(Parameter parameter){
        GenericType genericType = parameter.getDeclaredAnnotation(GenericType.class);
        return Objects.isNull(genericType)?parameter.getType():genericType.value();
    }


    /**
     *  执行参数获取机制
     * @param joinPoint
     *  @return
     */
    private static Object[] getParameterValueByPrimaryKeyField(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        return  getParameterValueByPrimaryKeyField(method,args);
    }

    /**
     *  执行参数获取机制
     * @param method
     * @param args
     * @return
     */
    public static Object[] getParameterValueByPrimaryKeyField(Method method,Object... args) {

        if(Objects.nonNull(method) && ArrayUtils.isNotEmpty(args)){
            Parameter[] parameters = method.getParameters();
            if(ArrayUtils.isNotEmpty(parameters)){
                // 存在多个，只会获取第一个！
                Object targetPrimaryParameterValue = null;
                Parameter targetPrimaryParameter = null;
                for(int i = 0 ; i < parameters.length &&
                        Objects.isNull(targetPrimaryParameterValue) &&
                        Objects.isNull(targetPrimaryParameter); i++){
                    PrimaryKeyField primaryKeyField = parameters[i].getDeclaredAnnotation(PrimaryKeyField.class);
                    if(Objects.nonNull(primaryKeyField)){
                        targetPrimaryParameterValue = args[i];
                        targetPrimaryParameter = parameters[i];
                    }
                }
                // 如果存在对应的注解
                if(Objects.nonNull(targetPrimaryParameterValue) && Objects.nonNull(targetPrimaryParameter)){
                    PrimaryKeyField primaryKeyField = targetPrimaryParameter.getDeclaredAnnotation(PrimaryKeyField.class);
                    String primarykeyFieldValue = primaryKeyField.value();
                    String taskExecuteType = primaryKeyField.taskExecuteType();
                    // 获取相关的主键标识值
                    if(StringUtils.isNoneEmpty(primarykeyFieldValue,taskExecuteType)){
                        Object primaryKeyObject = Optional.ofNullable(getValueByExpression(targetPrimaryParameterValue,primarykeyFieldValue)).
                                orElse(System.currentTimeMillis());
                        Object taskExecuteTypeObject = getValueByExpression(targetPrimaryParameterValue,taskExecuteType);
                        if(Objects.isNull(primaryKeyObject) || Objects.isNull(taskExecuteTypeObject)){
                            throw new RuntimeException("CONFIG  'PrimaryKeyField or taskType' ANNOTATION to the field actual value is null !");
                        }
                        return new Object[]{primaryKeyObject,taskExecuteTypeObject};
                    }else{
                        throw new RuntimeException("CONFIG  'PrimaryKeyField or taskType ' ANNOTATION the value is not null or empty !");
                    }
                }else{
                    throw new RuntimeException("NOT CONFIG OR FOUND THE PAREMETER IS CONFIG THE PrimaryKeyField or taskType ANNOTATION !");
                }
            }
        }
        return null;
    }


    /**
     * 执行任务的当前上下文！
     */
    public static class ExecuteTaskCurrentContext {


        NamedInheritableThreadLocal<Long> currentTaskExecuteAuthContext = new NamedInheritableThreadLocal<>("executeTaskCurrentContext");


        NamedInheritableThreadLocal<String> currentProxyEnvContext = new NamedInheritableThreadLocal<>("currentProxyEnvContext");

        /**
         * 设置数据信息
         * @param param
         */
        public void set(Long param){
            currentTaskExecuteAuthContext.set(param);
        }

        /**
         * 获取数据
         */
        public Long get(){
            return currentTaskExecuteAuthContext.get();
        }

        /**
         * 设置数据信息
         * @param param
         */
        public void setProxyEnv(String param){
            currentProxyEnvContext.set(param);
        }

        /**
         * 获取数据
         */
        public String getProxyEnv(){
            return currentProxyEnvContext.get();
        }


        /**
         * 清空当前信息所有内容
         */
        public void clear(){
            // 情况环境中所有数据
            currentTaskExecuteAuthContext.set(null);
            currentTaskExecuteAuthContext.remove();
        }

        public void clearProxyEnv(){
            // 情况环境中所有数据
            currentProxyEnvContext.set(null);
            currentProxyEnvContext.remove();
        }
    }

    /**
     * 通过el表达式读取对象中的数据信息！
     * @param targetObject
     * @param propertyName
     * @return
     */
    public static Object getValueByExpression(Object targetObject,String propertyName){
        try {
            EvaluationContext evaluationContext = new StandardEvaluationContext();
            evaluationContext.setVariable("model",targetObject);
            Expression expression =expressionParser.parseExpression(String.format("#{#model.%s}",propertyName),new TemplateParserContext());
            return expression.getValue(evaluationContext,Object.class);
        } catch (ParseException e) {
            log.warn("parse el is failure lead to ParseException analysis the parameter is {}",propertyName);
            return propertyName;
        } catch (EvaluationException e) {
            log.warn("not found the properties in targetObject , so lead to EvaluationException analysis the parameter is {}",propertyName);
            return ExecuteAnalysisExpressionType.getThisByPropertyName(propertyName).getValue();
        }
    }

}
