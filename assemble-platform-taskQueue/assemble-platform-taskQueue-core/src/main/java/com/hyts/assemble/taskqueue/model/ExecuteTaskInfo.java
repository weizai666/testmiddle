package com.hyts.assemble.taskqueue.model;

import java.util.Date;

public class ExecuteTaskInfo {
    private Long executeTaskId;

    private String executeTaskCode;

    private String executeBizCode;

    private Byte executeTaskType;

    private Byte executeTaskStatus;

    private String executeTaskName;

    private Long executeTaskDuration;

    private String executeTaskResult;

    private Date executeStartTime;

    private Date executeStopTime;

    private String executeInputParam;

    private Byte isDelete;

    private Long departmentId;

    private Long enterpriseId;

    private Long userId;

    private Date updateTime;

    private Date createTime;

    private Long resultDataSize;

    private Date resultExpireTime;

    private Integer failureRetryCount;

    private String failureReason;

    public Long getExecuteTaskId() {
        return executeTaskId;
    }

    public void setExecuteTaskId(Long executeTaskId) {
        this.executeTaskId = executeTaskId;
    }

    public String getExecuteTaskCode() {
        return executeTaskCode;
    }

    public void setExecuteTaskCode(String executeTaskCode) {
        this.executeTaskCode = executeTaskCode == null ? null : executeTaskCode.trim();
    }

    public String getExecuteBizCode() {
        return executeBizCode;
    }

    public void setExecuteBizCode(String executeBizCode) {
        this.executeBizCode = executeBizCode == null ? null : executeBizCode.trim();
    }

    public Byte getExecuteTaskType() {
        return executeTaskType;
    }

    public void setExecuteTaskType(Byte executeTaskType) {
        this.executeTaskType = executeTaskType;
    }

    public Byte getExecuteTaskStatus() {
        return executeTaskStatus;
    }

    public void setExecuteTaskStatus(Byte executeTaskStatus) {
        this.executeTaskStatus = executeTaskStatus;
    }

    public String getExecuteTaskName() {
        return executeTaskName;
    }

    public void setExecuteTaskName(String executeTaskName) {
        this.executeTaskName = executeTaskName == null ? null : executeTaskName.trim();
    }

    public Long getExecuteTaskDuration() {
        return executeTaskDuration;
    }

    public void setExecuteTaskDuration(Long executeTaskDuration) {
        this.executeTaskDuration = executeTaskDuration;
    }

    public String getExecuteTaskResult() {
        return executeTaskResult;
    }

    public void setExecuteTaskResult(String executeTaskResult) {
        this.executeTaskResult = executeTaskResult == null ? null : executeTaskResult.trim();
    }

    public Date getExecuteStartTime() {
        return executeStartTime;
    }

    public void setExecuteStartTime(Date executeStartTime) {
        this.executeStartTime = executeStartTime;
    }

    public Date getExecuteStopTime() {
        return executeStopTime;
    }

    public void setExecuteStopTime(Date executeStopTime) {
        this.executeStopTime = executeStopTime;
    }

    public String getExecuteInputParam() {
        return executeInputParam;
    }

    public void setExecuteInputParam(String executeInputParam) {
        this.executeInputParam = executeInputParam == null ? null : executeInputParam.trim();
    }

    public Byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getResultDataSize() {
        return resultDataSize;
    }

    public void setResultDataSize(Long resultDataSize) {
        this.resultDataSize = resultDataSize;
    }

    public Date getResultExpireTime() {
        return resultExpireTime;
    }

    public void setResultExpireTime(Date resultExpireTime) {
        this.resultExpireTime = resultExpireTime;
    }

    public Integer getFailureRetryCount() {
        return failureRetryCount;
    }

    public void setFailureRetryCount(Integer failureRetryCount) {
        this.failureRetryCount = failureRetryCount;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason == null ? null : failureReason.trim();
    }
}