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
package com.hyts.assemble.taskqueue.logic;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.db.PageResult;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.hyts.assemble.taskqueue.aop.ExecuteTargetProxy;
import com.hyts.assemble.taskqueue.constant.ExecuteResultType;
import com.hyts.assemble.taskqueue.constant.ExecuteTaskType;
import com.hyts.assemble.taskqueue.core.PartitionQueueManager;
import com.hyts.assemble.taskqueue.dao.ExecuteTaskInfoExample;
import com.hyts.assemble.taskqueue.dao.ExecuteTaskInfoMapper;
import com.hyts.assemble.taskqueue.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @project-name:wiz-sound-ai2
 * @package-name:com.wiz.soundai.task.service.impl
 * @author:LiBo/Alex
 * @create-date:2021-09-27 11:42
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description: 执行任务队列中的数据信息惹怒
 */
@Slf4j
@Service
public abstract class AbstractExecuteTaskService implements ExecuteTaskService {


    @Autowired
    PartitionQueueManager redisPartitionQueueManager;


    @Autowired
    ExecuteTaskInfoMapper executeTaskInfoMapper;

    /**
     * 任务对象执行队列前缀=任务线程名称
     */

    /**
     * 任务对象执行队列-重试执行队列操作（暂停+失败）
     */
    private static final String DEFAULT_TASK_REDIS_RETRY_QUEUE_NAME = "EXECUTE:TASK:RETRY:QUEUE:";


    /**
     * 任务数量(二进制标识，方便移位运算帮助理解)
     */
    private static final int WORK_NUM = 0b010;


    /**
     * 暂时考虑为90天
     */
    @Value("${execute.task.data.expire:90}")
    private int executeTaskDataExpireDays;

    /**
     * 暂时考虑为3小时
     */
    @Value("${execute.task.session.expire:3}")
    private int executeTaskSessionExpireHours;



    private RedisTemplate<String, Object> redisTemplate;



    @Resource
    ApplicationContext applicationContext;



    private List<AbstractExecuteTaskService> executeTaskService = Lists.newArrayList();




    @PostConstruct
    public void initRedisTemplate(){
        try {
            redisTemplate = applicationContext.getBean("redisTemplate",RedisTemplate.class);
            Map<String,AbstractExecuteTaskService> abstractExecuteTaskServiceMap = applicationContext.getBeansOfType(AbstractExecuteTaskService.class);
            if(CollectionUtil.isNotEmpty(abstractExecuteTaskServiceMap)){
                executeTaskService = new ArrayList(abstractExecuteTaskServiceMap.values());
            }
        } catch (BeansException e) {
            log.error("init the redistemplate is faliure!",e);
        }
    }


    /**
     * 初始化任务队列执行池操作机制控制(4个任务)
     * 初始化执行任务
     */
    @Override
    public void initExecuteWorkTask() {
        //初始化机制控制
        try {
//            TimeUnit.SECONDS.sleep(5);
            redisPartitionQueueManager.initPollExecuteWorker(getTaskExecuteQueue(), WORK_NUM,
                    // 服务上层进行处理，不会为空！放心“食用”。
                    // callExecuteTask不会爆出错误！！！！，否则会引起线程销毁！
                    (model) -> callExecuteTask((ExecuteTaskDataDTO) model));
        }catch (Exception e){

        }
    }


    /**
     * 执行分发执行任务机制控制
     * @param clazz 需要被执行代理的类的类名称
     * @param currentMethodName 代理执行的方法
     * @param primaryKey 主键关键子业务逻辑编码
     * @param taskExecuteType 任务执行类型编码
     * @param executeTaskType 执行类的类型
     * @param args 执行的参数信息     * @param clazz

     */
    public void addExecuteTargetProxy(Class clazz, String currentMethodName,String primaryKey,Byte taskExecuteType,
                                      Class executeTaskType, Object... args) {
        // 查询相关元数据信息
        String className = clazz.getName();
        Object targetObject = applicationContext.getBean(clazz);
        if (Objects.isNull(targetObject)) {
            // TODO 待完成相关有参构造器的构建实例化机制
            try {
                targetObject = Class.forName(className).newInstance();
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                log.error("create the new instance is success!",e);
            }
        }
        // 暂时不支持重载方法机制，暂时不支持重载机制控制，因为获取参数类型的接口暂时未完成！
        Method currentMethod = MethodUtils.getMatchingMethod(targetObject.getClass(), currentMethodName, ClassUtils.toClass(args));
        try {
            // 参数类型列表数据
            Object[] realArgs = ExecuteTargetProxy.filterArgumentList(args);
            String[] parameterClassList = dealWithFunctionMetadata(currentMethod.getParameters(),realArgs);
            ExecuteMethodCallPoint executeMethodCallPoint = new
                    ExecuteMethodCallPoint(className,currentMethodName, ExecuteTargetProxy.JOINER.join(parameterClassList) , JSONArray.toJSONString(realArgs));
            // 获取参数上面的指定的参数值
            ExecuteTaskDataDTO executeTaskDataDTO = new ExecuteTaskDataDTO(0L,
                    primaryKey, // primaryKey
                    taskExecuteType, // taskExecuteType
                    executeTaskType,// executeTaskType
                    executeMethodCallPoint);
            // 错误的是也要进入任务列表，保障数据的一致性！
            log.info("the data is finished to task queue:{}",executeTaskDataDTO);
            addExecuteTask(executeTaskDataDTO);
        } catch (Exception e) {
            log.error("manual the execute task is failure!", e);
            // 更新相关的数据信息状态操作
            ExecuteTaskDataDTO executeTaskDataDTO = new ExecuteTaskDataDTO(0L, primaryKey, // primaryKey
                    taskExecuteType, // taskExecuteType
                    executeTaskType,// executeTaskType
                    ExceptionUtil.stacktraceToOneLineString((ExceptionUtil.getRootCause(e)), 250),
                    ExecuteResultType.FAILURE.getType());
            addExecuteTask(executeTaskDataDTO);
        }
    }


    /**
     * 执行相关的方法元数据信息
     * @param parameters
     * @param args
     * @return
     */
    public static String[] dealWithFunctionMetadata(Parameter[] parameters, Object... args) {
        // 默认获取相关的用户注入信息数据
        log.info("find enable or target method to process the proxy method function !");
        Object[] realCallParam = args;
        //注意此部分是带有非序列化的参数信息
        String[] parameterClassList = new String[realCallParam.length];
        // 顺序执行操作
        IntStream.range(0,realCallParam.length).forEach(param->{
            //控制集合属性
            if(realCallParam[param] instanceof Collection || realCallParam[param] instanceof Map){
                parameterClassList[param] = ExecuteTargetProxy.getParameterGenerateTypeByGenericType(parameters[param]).getName();
            }else {
                parameterClassList[param] = Objects.nonNull(realCallParam[param])?realCallParam[param].getClass().
                        getName():parameters[param].getType().getName();
            }
        });
        return parameterClassList;
    }


    /**
     * 添加执行队列任务机制控制
     */
    @Override
    public void addExecuteTask(ExecuteTaskDataDTO executeTaskDataDTO) {
        // 执行任务操作业务编码
        try {
            //保存当前的SpringSecurity的session会话数据对象信息
//            Object userTokenModel = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            log.info("userTokenModel data info:{}",userTokenModel);
            ExecuteTaskInfo executeTaskInfo = new ExecuteTaskInfo();
//            executeTaskInfo.setEnterpriseId(ThreadCacheMgr.getEntId());
            executeTaskInfo.setExecuteBizCode(String.valueOf(executeTaskDataDTO.getExecuteBizCode()));
            executeTaskInfo.setExecuteInputParam(JSONObject.toJSONString(executeTaskDataDTO.getExecuteMethodCallPoint()));
            executeTaskInfo.setExecuteTaskCode(DEFAULT_TASK_EXECUTE_CODE_DATA_PREFIX + System.currentTimeMillis());
            executeTaskInfo.setExecuteTaskType(executeTaskDataDTO.getExecuteBizType());
            executeTaskInfo.setResultExpireTime(Date.from(LocalDateTime.now().plusDays(executeTaskDataExpireDays)
                    .atZone(ZoneId.systemDefault()).toInstant()));
            executeTaskInfo.setExecuteTaskName(currentExecuteType().
                    getMeByTypeCode(executeTaskDataDTO.getExecuteBizType()).getName());
//            executeTaskInfo.setDepartmentId(); TODO 部门id处理操作机制
            //初始化执行任务对象
            executeTaskInfoMapper.insertSelective(executeTaskInfo);
//            redisTemplate.opsForValue().set(DEFAULT_TASK_EXECUTE_CODE_PREFIX+executeTaskInfo.
//                    getExecuteTaskId(),userTokenModel,executeTaskSessionExpireHours,TimeUnit.HOURS);
            // 此部分为新增的功能，通过状态来区分是否需要进入任务队列取执行相关的文件服务任务机制
            if(Objects.isNull(executeTaskDataDTO.getExecuteTaskStatus()) || executeTaskDataDTO.getExecuteTaskStatus()
                    == ExecuteResultType.UNFINISHED.getType()) {
                // 加入到等待队列中取,方便检索处理数据使用！
                executeTaskDataDTO.setExecuteTaskId(executeTaskInfo.getExecuteTaskId());
                AbstractExecuteTaskService abstractExecuteTaskService = smartChoiceTheTaskExecutor(executeTaskDataDTO.getExecuteBizType());
                String queueName = abstractExecuteTaskService.getTaskExecuteQueue();
                redisPartitionQueueManager.addAutoPartitionBlockingElement(queueName,
                        new HashQueueElement(executeTaskInfo.getExecuteTaskId(), executeTaskDataDTO), WORK_NUM);
            }
        } catch (Exception e) {
            log.error("add the execute task is failure!", e);
            // TODO 是否考虑重试?
        }
    }


    /**
     * 执行调用对应的方法操作处理机制
     * 可虑重试操作处理机制控制
     * @param executeTaskDataDTO
     */
    @Override
    public void callExecuteTask(ExecuteTaskDataDTO executeTaskDataDTO) {
        //处理操作控制
        ExecuteTaskInfo executeTaskInfo = new ExecuteTaskInfo();
        try {
            // 赋值修改标志
            executeTaskInfo.setExecuteTaskId(executeTaskDataDTO.getExecuteTaskId());
            //TODO 不同的业务类型调用不同的导出功能实现机制控制
            executeTaskInfo.setExecuteStartTime(new Date());
            // 设置系统线程内部全局参数，用于传递当前处理的执行操作对象
            ExecuteTargetProxy.getExecuteTaskCurrentContext().set(executeTaskDataDTO.getExecuteTaskId());
            // 开始真正执行任务
            try {
                ExecuteProcessResult result  = preExecuteTask(executeTaskDataDTO);
                if(Objects.nonNull(result) && result.isSuccess()){
                    Object targetCallProxyObject = invokeExecuteMethod(executeTaskDataDTO);
                    result = postExecuteTask(executeTaskDataDTO,targetCallProxyObject);
                    // 如果执行失败了！
                    if(result.isSuccess()){
                        // 执行相关操作处理机制
                        executeTaskInfo.setResultDataSize(result.getExecuteProcessResultSize());
                        // 存储停止时间操作
                        executeTaskInfo.setExecuteStopTime(new Date());
                        //计算耗时事件长度计算
                        executeTaskInfo.setExecuteTaskDuration(DateUtil.between(executeTaskInfo.getExecuteStartTime(),executeTaskInfo.getExecuteStopTime(), DateUnit.SECOND));
                        //更新数据信息操作机制
                        executeTaskInfo.setExecuteTaskResult(result.getExecuteProcessResult());
                        //添加相关的过期失效时间
                        executeTaskInfo.setResultExpireTime(Date.from(LocalDateTime.now().plusDays(executeTaskDataExpireDays).toInstant(ZoneOffset.UTC)));
                    }else{
                        //判断是否需要重试操作
                        if(result.isNeedRetry()){
                            //TODO 重试操作处理机制！
                        }
                        //执行失败的原因(失败后，隔日可以通过清理线程清除!)
                        executeTaskInfo.setFailureReason(result.getExecuteProcessFailureMessage());
                        executeTaskInfo.setResultExpireTime(Date.from(LocalDateTime.now().
                                plusDays(executeTaskDataExpireDays).atZone(ZoneId.systemDefault()).toInstant()));
                    }
                    executeTaskInfo.setExecuteTaskStatus(result.isSuccess()? ExecuteResultType.FINISHED.getType():
                            ExecuteResultType.FAILURE.getType());
                }
            } catch (Exception e) {
                // 获取业务异常技术信息
                //执行失败的原因(失败后，隔日可以通过清理线程清除!)
                log.error("execute the invokeExecuteMethod is failure",e);
                executeTaskInfo.setExecuteTaskStatus(ExecuteResultType.FAILURE.getType());
                executeTaskInfo.setResultExpireTime(Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant()));
                executeTaskInfo.setFailureReason(ExceptionUtil.stacktraceToOneLineString((ExceptionUtil.getRootCause(e)),250));
            }
            // 真正意义上取执行操作机制控制
            executeTaskInfoMapper.updateByPrimaryKeySelective(executeTaskInfo);
        } catch (Exception e) {
            log.error("update the execute task info is failure！",e);
            //TODO 是否考虑重试操作机制控制，是否考虑放在一个重试队列里面进行执行。
        }finally {
            // 清理数据会话
            clearCurrentExecuteTaskSession(executeTaskInfo.getExecuteTaskId());
        }
    }


    /**
     * 调用执行方法
     * @param executeTaskDataDTO
     */
    @Override
    public Object invokeExecuteMethod(ExecuteTaskDataDTO executeTaskDataDTO){
        try {
            ExecuteMethodCallPoint executeMethodCallPoint = executeTaskDataDTO.getExecuteMethodCallPoint();
            ExecuteTaskInnerTranfser executeTaskInnerTranfser = analysisSnapShotSavePoint(executeMethodCallPoint.getCallSnapShotParamValue(),
                    executeMethodCallPoint.getCallSnapShotParamType());
            log.debug("request parameter is :{}",executeTaskInnerTranfser);
            // 是否需要特殊做适配处理？
            Object proxyTargetObject = applicationContext.getBean(Class.forName(executeMethodCallPoint.getCallSnapShotClassName()));
            if(Objects.isNull(proxyTargetObject)){
                // TODO 暂时先不支持其他构造器的方式进行控制 未来扩展！
                try {
                    proxyTargetObject = Class.forName(executeMethodCallPoint.getCallSnapShotClassName()).newInstance();
                } catch (InstantiationException e) {
                    log.error("instance object is failure!",e);
                }
            }
            // 代理执行目标对象控制
            if(Objects.isNull(proxyTargetObject)){
                return ExecuteProcessResult.faliure(0L,ExecuteResultType.FAILURE.getMessage(),
                        String.format("not create or find the target class object：[{}] in spring context!",
                                executeMethodCallPoint.getCallSnapShotClassName()));
            }
            Object proxyTargetResult = MethodUtils.
                    invokeMethod(proxyTargetObject,Boolean.TRUE,executeMethodCallPoint.
                                    getCallSnapShotMethodName(),executeTaskInnerTranfser.getParamValues(),
                            executeTaskInnerTranfser.getClasses());
            return proxyTargetResult;
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            // 交给调用者进行处理操作
            throw new RuntimeException(e);
        }
    }


    /**
     * 删除执行任务队列对象
     * @param executeTaskRequestDTO
     */
    @Override
    public void deleteExecuteTask(ExecuteTaskRequestDTO executeTaskRequestDTO) {
        //TODO 待完成！直接进行物理删除操作，暂时这么考虑！
        try {
            executeTaskInfoMapper.deleteByPrimaryKey(executeTaskRequestDTO.getTaskId());
            redisTemplate.delete(DEFAULT_TASK_EXECUTE_CODE_PREFIX + executeTaskRequestDTO.getTaskId());
        } catch (Exception e) {
            log.error("delete the task is failure!",e);
        }
    }


    /**
     * 停止对应的执行任务操作
     * @param executeTaskRequestDTO
     */
    @Override
    public void stopExecuteTask(ExecuteTaskRequestDTO executeTaskRequestDTO) {
        //TODO 待完成！
        // 需要设置Safepoint机制
    }



    /**
     * 更新任务执行状态以及相关信息，暂时仅支持更新状态以及相关的错误信息数据！其他数据更新无意义！
     * @param executeTaskRequestDTO
     */
    @Override
    public void updateExecuteTask(ExecuteTaskDataDTO executeTaskRequestDTO) {
        ExecuteTaskInfo executeTaskInfo = new ExecuteTaskInfo();
        executeTaskInfo.setExecuteTaskId(executeTaskRequestDTO.getExecuteTaskId());
        executeTaskInfo.setExecuteTaskStatus(executeTaskRequestDTO.getExecuteTaskStatus());
        executeTaskInfo.setFailureReason(executeTaskRequestDTO.getErrorMessage());
        executeTaskInfoMapper.updateByPrimaryKeySelective(executeTaskInfo);
    }

    /**
     * 查询任务队列中的数据信息分布
     * @param executeTaskRequestDTO
     * @return
     */
    @Override
    public PageResult<? extends ExecuteTaskVO> queryExecuteTask(ExecuteTaskRequestDTO executeTaskRequestDTO) {
        Page<? extends ExecuteTaskVO> page = PageHelper.startPage(executeTaskRequestDTO.getPageNo(),
                executeTaskRequestDTO.getPageSize(),Boolean.TRUE);
        ExecuteTaskInfoExample executeTaskInfoExample = new ExecuteTaskInfoExample();
        ExecuteTaskInfoExample.Criteria criteria = executeTaskInfoExample.createCriteria();
        // 查询三个月范围内的数据信息
        criteria.andCreateTimeBetween(Date.from(LocalDateTime.now().minusDays(executeTaskDataExpireDays).toInstant(ZoneOffset.UTC)),
                Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
        criteria.andIsDeleteEqualTo(NumberUtils.BYTE_ZERO).andUserIdEqualTo(Optional.ofNullable(executeTaskRequestDTO.getUserId())
                .orElseGet(()->null));
        if(StringUtils.isNotBlank(executeTaskRequestDTO.getTaskStatus())){
            criteria.andExecuteTaskStatusIn(SPLITTER.splitToList(executeTaskRequestDTO.getTaskStatus()).stream()
                    .map(Byte::valueOf).collect(Collectors.toList()));
        }
        // 添加倒叙排列的条件机制
        executeTaskInfoExample.setOrderByClause("create_time desc");
        List<ExecuteTaskInfo> executeTaskInfos = executeTaskInfoMapper.selectByExample(executeTaskInfoExample);
        if(CollectionUtil.isNotEmpty(executeTaskInfos)){
            /******************************可活动的代码区********************************************/
            List executeTaskVOS = executeTaskInfos.stream().
                    map(this::viewModelResolver).collect(Collectors.toList());
                    PageResult pageResult =  new PageResult<>(executeTaskRequestDTO.getPageNo(),
                    executeTaskRequestDTO.getPageSize(), (int) page.getTotal());
            pageResult.addAll(executeTaskVOS);
            /******************************可活动的代码区********************************************/
        }
        return new PageResult(executeTaskRequestDTO.getPageNo(),executeTaskRequestDTO.getPageSize(),0);
    }


    /**
     * 获取原始形式参数列表数据值
     * @return
     */
    protected ExecuteTaskInnerTranfser analysisSnapShotSavePoint(String jsonParam, String jsonTypeParam){
        // 判断校验是否为空
        if(StringUtils.isNotEmpty(jsonParam) && StringUtils.isNotBlank(jsonTypeParam)){
            JSONArray jsonArray = JSONArray.parseArray(jsonParam);
            List<String> jsonTypeParamArray = SPLITTER.splitToList(jsonTypeParam);
            try {
                ExecuteTaskInnerTranfser executeTaskInnerTranfser = new ExecuteTaskInnerTranfser();
                // 以类型为准，值可以为空！
                if(CollectionUtil.isNotEmpty(jsonTypeParamArray)){
                    Object [] paramValue = new Object[jsonTypeParamArray.size()];
                    Class [] classes = new Class[jsonTypeParamArray.size()];
                    for(int i = 0 ; i < jsonArray.size();i++){
                        Class clazz = Class.forName(jsonTypeParamArray.get(i));
                        // 校验是否属于不需要序列化类型
                        if(TransientClassWrapper.class.isAssignableFrom(clazz)){
                            // 流出形式参数表的占位，以防止extractMethod调用不到！
                            classes[i] = Class.forName((JSONObject.parseObject(jsonArray.get(i).toString(),
                                    TransientClassWrapper.class))
                                    .getClazzType());
                            paramValue[i] = null;
                            continue;
                        }
                        classes[i] = clazz;
                        if(Objects.isNull(jsonArray.get(i))){
                            continue;
                        }
                        String paramJsonStr = jsonArray.get(i).toString();
                        if(isValidArray(paramJsonStr)){
                            paramValue[i] = JSONArray.parseArray(paramJsonStr,clazz);
                            if(Collection.class.isAssignableFrom(clazz) || Map.class.isAssignableFrom(clazz)){
                                paramValue[i] = JSONArray.parseArray(paramJsonStr,Object.class);
                            }else {
                                paramValue[i] = JSONArray.parseArray(JSONArray.parseArray(paramJsonStr).toString(), clazz);
                                // 重新覆盖真实的类型信息
                                classes[i] = paramValue[i].getClass();
                            }
                        }else if(isValidObject(paramJsonStr)){
                            paramValue[i] = JSONObject.parseObject(paramJsonStr,clazz);
                        }else{
                            paramValue[i] = jsonArray.get(i);
                        }
                    }
                    executeTaskInnerTranfser.setClasses(classes);
                    executeTaskInnerTranfser.setParamValues(paramValue);
                    return executeTaskInnerTranfser;

                }
                return executeTaskInnerTranfser;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("analaysis the call parameter is failure!",e);
            }
        }
        return null;
    }



    public static boolean isValidArray(String paramJsonStr){
        try {
            JSONArray.parseArray(paramJsonStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public static boolean isValidObject(String paramJsonStr){
        try {
            JSONObject.parseObject(paramJsonStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 准备任务执行的可执行环境内容（创建）
     */
    public void preparedCurrentExecuteTaskSession(Object userCacheVO){
        // 符合重载方法
        if(Objects.nonNull(userCacheVO)){
            // 如果属于相关的数据模型对象机制
//            UserCacheVO cacheVO = (UserCacheVO)userCacheVO;
            // 如果还拿不到，是否要考虑查询相关的数据库，检索对应的值？
//            ThreadCacheMgr.push("userId",cacheVO.getUserId());
//            ThreadCacheMgr.push("entId",cacheVO.getEntId());
        }else{
            Long taskExecuteId = ExecuteTargetProxy.getExecuteTaskCurrentContext().get();
            ExecuteTaskInfo executeTaskInfo = executeTaskInfoMapper.selectByPrimaryKey(taskExecuteId);
//            ThreadCacheMgr.push("userId",executeTaskInfo.getUserId());
//            ThreadCacheMgr.push("entId",executeTaskInfo.getEnterpriseId());
        }
        setCurrentUserAuthenSession(userCacheVO);
    }

    /**
     * 清除当前执行的任务会话信息控制（清空）
     * @param executeTaskId
     */
    public void clearCurrentExecuteTaskSession(Long executeTaskId){
        try {
            //清除手动存入的用户信息当前会话信息，防止影响下一次执行。（以防万一！）防止内存泄露！
//            ExecuteTargetProxy.getExecuteTaskCurrentContext().clear();
//            ThreadCacheMgr.removeKey("userId");
//            ThreadCacheMgr.removeKey("entId");
//            ThreadCacheMgr.removeKey(ExecuteTaskService.DEFAULT_TASK_EXECUTE_CODE_PREFIX);
//            redisTemplate.delete(DEFAULT_TASK_EXECUTE_CODE_PREFIX + executeTaskId);
        } catch (Exception e) {
            log.error("clear the data is not finished",e);
        }
    }

    /**
     * 获取当前用户的认证session会话对象
     */
    public static Object getCurrentUserAuthenSession(){
        return null;
//        return ThreadCacheMgr.get(ExecuteTaskService.DEFAULT_TASK_EXECUTE_CODE_PREFIX);
    }


    /**
     * 设置当前用户的认证session会话对象
     */
    public static void setCurrentUserAuthenSession(Object userCacheVO){
//        ThreadCacheMgr.push(ExecuteTaskService.DEFAULT_TASK_EXECUTE_CODE_PREFIX,userCacheVO);
    }



    /**
     * 重试任何状态下的任务操作-属于重放操作机制控制
     * @param executeTaskDataDTO
     */
    @Override
    public void retryExecuteTask(ExecuteTaskDataDTO executeTaskDataDTO) {
        //TODO 未来希望可以控制优先级操作机制控制
        // 执行任务操作业务编码
        try {
            //初始化执行任务对象
            ExecuteTaskInfo executeTaskInfo = executeTaskInfoMapper.selectByPrimaryKey(executeTaskDataDTO.getExecuteTaskId());
            // 如果等于空
            if(Objects.isNull(executeTaskInfo)){
                throw new RuntimeException("the task is not exist!");
            }
//            Object userTokenModel = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            // 此部分为新增的功能，通过状态来区分是否需要进入任务队列取执行相关的文件服务任务机制，没有条件状态的限制
            executeTaskDataDTO = new ExecuteTaskDataDTO(executeTaskInfo.
                    getExecuteTaskId(),
                    executeTaskInfo.getExecuteBizCode(), // primaryKey
                    executeTaskInfo.getExecuteTaskType(), // taskExecuteType
                    currentExecuteType().getClass(),// executeTaskType
                    JSONObject.parseObject(executeTaskInfo.getExecuteInputParam(), ExecuteMethodCallPoint.class));
            redisTemplate.opsForValue().set(DEFAULT_TASK_EXECUTE_CODE_PREFIX+executeTaskInfo.
                    getExecuteTaskId(),null,executeTaskSessionExpireHours,TimeUnit.HOURS);
            AbstractExecuteTaskService abstractExecuteTaskService = smartChoiceTheTaskExecutor(executeTaskDataDTO.getExecuteBizType());
            String queueName = abstractExecuteTaskService.getTaskExecuteQueue();
            redisPartitionQueueManager.addAutoPartitionBlockingElement(queueName,
                    new HashQueueElement(executeTaskInfo.getExecuteTaskId(), executeTaskDataDTO), WORK_NUM);
            executeTaskDataDTO.setExecuteTaskStatus(ExecuteResultType.UNFINISHED.getType());
            updateExecuteTask(executeTaskDataDTO);
        } catch (Exception e) {
            log.error("retry the execute task is failure!", e);
            // TODO 是否考虑重试?
            throw new RuntimeException("execute retry task is failure!",e);
        }
    }


    /**
     * 智能选择相关的任务执行类执行器
     * @param executeTaskType
     */
    protected AbstractExecuteTaskService smartChoiceTheTaskExecutor(Byte executeTaskType){
        // 判断是否进行任务队列的选择处理方式
        Preconditions.checkNotNull(currentExecuteType(),"not allowed the executeTaskType is null!");
        Preconditions.checkArgument(CollectionUtil.isNotEmpty(executeTaskService),"the executor is null!");
        return executeTaskService.stream().filter(param->
                        executeTaskType.intValue() >=
                                param.currentExecuteType().getMinRange() && executeTaskType.intValue()<= param.currentExecuteType().getMaxRange()
                // TODO 未来可以通过排序处理优先操作机制！
        ).findAny().get();
    }


    /**
     * 无作为类
     * ExportFileTaskServiceImpl.TransientClassType
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class TransientClassWrapper {
        /**
         * 内部包装类型
         */
        private String clazzType;

    }

    /**
     * 前置服务处理控制
     * @param executeTaskDataDTO
     * @return
     */
    protected abstract ExecuteProcessResult preExecuteTask(ExecuteTaskDataDTO executeTaskDataDTO);

    /**
     * 下沉到子类进行实现机制控制
     * @param executeTaskDataDTO
     */
    protected abstract ExecuteProcessResult postExecuteTask(ExecuteTaskDataDTO executeTaskDataDTO, Object proxyMethodResult);

    /**
     下沉到子类进行实现机制控制
     * 控制当前的执行类型
     * @return
     */
    protected abstract ExecuteTaskType currentExecuteType();


    /**
     * 子类需要进行数据转换，将实际模型进行输出转换机制
     * @return
     */
    protected abstract <T extends ExecuteTaskVO> T  viewModelResolver(ExecuteTaskInfo executeTaskInfo);

    /**
     * 子类需要进行获取相关的支持执行队列：为了支撑和为了未来的动态创建队列支持机制！
     * 而不是静态队列控制！
     * @return
     */
    protected abstract String  getTaskExecuteQueue();

}


