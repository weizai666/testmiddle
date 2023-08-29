package com.hyts.assemble.taskqueue.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExecuteTaskInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ExecuteTaskInfoExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andExecuteTaskIdIsNull() {
            addCriterion("execute_task_id is null");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskIdIsNotNull() {
            addCriterion("execute_task_id is not null");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskIdEqualTo(Long value) {
            addCriterion("execute_task_id =", value, "executeTaskId");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskIdNotEqualTo(Long value) {
            addCriterion("execute_task_id <>", value, "executeTaskId");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskIdGreaterThan(Long value) {
            addCriterion("execute_task_id >", value, "executeTaskId");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskIdGreaterThanOrEqualTo(Long value) {
            addCriterion("execute_task_id >=", value, "executeTaskId");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskIdLessThan(Long value) {
            addCriterion("execute_task_id <", value, "executeTaskId");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskIdLessThanOrEqualTo(Long value) {
            addCriterion("execute_task_id <=", value, "executeTaskId");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskIdIn(List<Long> values) {
            addCriterion("execute_task_id in", values, "executeTaskId");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskIdNotIn(List<Long> values) {
            addCriterion("execute_task_id not in", values, "executeTaskId");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskIdBetween(Long value1, Long value2) {
            addCriterion("execute_task_id between", value1, value2, "executeTaskId");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskIdNotBetween(Long value1, Long value2) {
            addCriterion("execute_task_id not between", value1, value2, "executeTaskId");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskCodeIsNull() {
            addCriterion("execute_task_code is null");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskCodeIsNotNull() {
            addCriterion("execute_task_code is not null");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskCodeEqualTo(String value) {
            addCriterion("execute_task_code =", value, "executeTaskCode");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskCodeNotEqualTo(String value) {
            addCriterion("execute_task_code <>", value, "executeTaskCode");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskCodeGreaterThan(String value) {
            addCriterion("execute_task_code >", value, "executeTaskCode");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskCodeGreaterThanOrEqualTo(String value) {
            addCriterion("execute_task_code >=", value, "executeTaskCode");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskCodeLessThan(String value) {
            addCriterion("execute_task_code <", value, "executeTaskCode");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskCodeLessThanOrEqualTo(String value) {
            addCriterion("execute_task_code <=", value, "executeTaskCode");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskCodeLike(String value) {
            addCriterion("execute_task_code like", value, "executeTaskCode");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskCodeNotLike(String value) {
            addCriterion("execute_task_code not like", value, "executeTaskCode");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskCodeIn(List<String> values) {
            addCriterion("execute_task_code in", values, "executeTaskCode");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskCodeNotIn(List<String> values) {
            addCriterion("execute_task_code not in", values, "executeTaskCode");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskCodeBetween(String value1, String value2) {
            addCriterion("execute_task_code between", value1, value2, "executeTaskCode");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskCodeNotBetween(String value1, String value2) {
            addCriterion("execute_task_code not between", value1, value2, "executeTaskCode");
            return (Criteria) this;
        }

        public Criteria andExecuteBizCodeIsNull() {
            addCriterion("execute_biz_code is null");
            return (Criteria) this;
        }

        public Criteria andExecuteBizCodeIsNotNull() {
            addCriterion("execute_biz_code is not null");
            return (Criteria) this;
        }

        public Criteria andExecuteBizCodeEqualTo(String value) {
            addCriterion("execute_biz_code =", value, "executeBizCode");
            return (Criteria) this;
        }

        public Criteria andExecuteBizCodeNotEqualTo(String value) {
            addCriterion("execute_biz_code <>", value, "executeBizCode");
            return (Criteria) this;
        }

        public Criteria andExecuteBizCodeGreaterThan(String value) {
            addCriterion("execute_biz_code >", value, "executeBizCode");
            return (Criteria) this;
        }

        public Criteria andExecuteBizCodeGreaterThanOrEqualTo(String value) {
            addCriterion("execute_biz_code >=", value, "executeBizCode");
            return (Criteria) this;
        }

        public Criteria andExecuteBizCodeLessThan(String value) {
            addCriterion("execute_biz_code <", value, "executeBizCode");
            return (Criteria) this;
        }

        public Criteria andExecuteBizCodeLessThanOrEqualTo(String value) {
            addCriterion("execute_biz_code <=", value, "executeBizCode");
            return (Criteria) this;
        }

        public Criteria andExecuteBizCodeLike(String value) {
            addCriterion("execute_biz_code like", value, "executeBizCode");
            return (Criteria) this;
        }

        public Criteria andExecuteBizCodeNotLike(String value) {
            addCriterion("execute_biz_code not like", value, "executeBizCode");
            return (Criteria) this;
        }

        public Criteria andExecuteBizCodeIn(List<String> values) {
            addCriterion("execute_biz_code in", values, "executeBizCode");
            return (Criteria) this;
        }

        public Criteria andExecuteBizCodeNotIn(List<String> values) {
            addCriterion("execute_biz_code not in", values, "executeBizCode");
            return (Criteria) this;
        }

        public Criteria andExecuteBizCodeBetween(String value1, String value2) {
            addCriterion("execute_biz_code between", value1, value2, "executeBizCode");
            return (Criteria) this;
        }

        public Criteria andExecuteBizCodeNotBetween(String value1, String value2) {
            addCriterion("execute_biz_code not between", value1, value2, "executeBizCode");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskTypeIsNull() {
            addCriterion("execute_task_type is null");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskTypeIsNotNull() {
            addCriterion("execute_task_type is not null");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskTypeEqualTo(Byte value) {
            addCriterion("execute_task_type =", value, "executeTaskType");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskTypeNotEqualTo(Byte value) {
            addCriterion("execute_task_type <>", value, "executeTaskType");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskTypeGreaterThan(Byte value) {
            addCriterion("execute_task_type >", value, "executeTaskType");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("execute_task_type >=", value, "executeTaskType");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskTypeLessThan(Byte value) {
            addCriterion("execute_task_type <", value, "executeTaskType");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskTypeLessThanOrEqualTo(Byte value) {
            addCriterion("execute_task_type <=", value, "executeTaskType");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskTypeIn(List<Byte> values) {
            addCriterion("execute_task_type in", values, "executeTaskType");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskTypeNotIn(List<Byte> values) {
            addCriterion("execute_task_type not in", values, "executeTaskType");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskTypeBetween(Byte value1, Byte value2) {
            addCriterion("execute_task_type between", value1, value2, "executeTaskType");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("execute_task_type not between", value1, value2, "executeTaskType");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskStatusIsNull() {
            addCriterion("execute_task_status is null");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskStatusIsNotNull() {
            addCriterion("execute_task_status is not null");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskStatusEqualTo(Byte value) {
            addCriterion("execute_task_status =", value, "executeTaskStatus");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskStatusNotEqualTo(Byte value) {
            addCriterion("execute_task_status <>", value, "executeTaskStatus");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskStatusGreaterThan(Byte value) {
            addCriterion("execute_task_status >", value, "executeTaskStatus");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("execute_task_status >=", value, "executeTaskStatus");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskStatusLessThan(Byte value) {
            addCriterion("execute_task_status <", value, "executeTaskStatus");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskStatusLessThanOrEqualTo(Byte value) {
            addCriterion("execute_task_status <=", value, "executeTaskStatus");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskStatusIn(List<Byte> values) {
            addCriterion("execute_task_status in", values, "executeTaskStatus");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskStatusNotIn(List<Byte> values) {
            addCriterion("execute_task_status not in", values, "executeTaskStatus");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskStatusBetween(Byte value1, Byte value2) {
            addCriterion("execute_task_status between", value1, value2, "executeTaskStatus");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("execute_task_status not between", value1, value2, "executeTaskStatus");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskNameIsNull() {
            addCriterion("execute_task_name is null");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskNameIsNotNull() {
            addCriterion("execute_task_name is not null");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskNameEqualTo(String value) {
            addCriterion("execute_task_name =", value, "executeTaskName");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskNameNotEqualTo(String value) {
            addCriterion("execute_task_name <>", value, "executeTaskName");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskNameGreaterThan(String value) {
            addCriterion("execute_task_name >", value, "executeTaskName");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskNameGreaterThanOrEqualTo(String value) {
            addCriterion("execute_task_name >=", value, "executeTaskName");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskNameLessThan(String value) {
            addCriterion("execute_task_name <", value, "executeTaskName");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskNameLessThanOrEqualTo(String value) {
            addCriterion("execute_task_name <=", value, "executeTaskName");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskNameLike(String value) {
            addCriterion("execute_task_name like", value, "executeTaskName");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskNameNotLike(String value) {
            addCriterion("execute_task_name not like", value, "executeTaskName");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskNameIn(List<String> values) {
            addCriterion("execute_task_name in", values, "executeTaskName");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskNameNotIn(List<String> values) {
            addCriterion("execute_task_name not in", values, "executeTaskName");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskNameBetween(String value1, String value2) {
            addCriterion("execute_task_name between", value1, value2, "executeTaskName");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskNameNotBetween(String value1, String value2) {
            addCriterion("execute_task_name not between", value1, value2, "executeTaskName");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskDurationIsNull() {
            addCriterion("execute_task_duration is null");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskDurationIsNotNull() {
            addCriterion("execute_task_duration is not null");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskDurationEqualTo(Long value) {
            addCriterion("execute_task_duration =", value, "executeTaskDuration");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskDurationNotEqualTo(Long value) {
            addCriterion("execute_task_duration <>", value, "executeTaskDuration");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskDurationGreaterThan(Long value) {
            addCriterion("execute_task_duration >", value, "executeTaskDuration");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskDurationGreaterThanOrEqualTo(Long value) {
            addCriterion("execute_task_duration >=", value, "executeTaskDuration");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskDurationLessThan(Long value) {
            addCriterion("execute_task_duration <", value, "executeTaskDuration");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskDurationLessThanOrEqualTo(Long value) {
            addCriterion("execute_task_duration <=", value, "executeTaskDuration");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskDurationIn(List<Long> values) {
            addCriterion("execute_task_duration in", values, "executeTaskDuration");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskDurationNotIn(List<Long> values) {
            addCriterion("execute_task_duration not in", values, "executeTaskDuration");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskDurationBetween(Long value1, Long value2) {
            addCriterion("execute_task_duration between", value1, value2, "executeTaskDuration");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskDurationNotBetween(Long value1, Long value2) {
            addCriterion("execute_task_duration not between", value1, value2, "executeTaskDuration");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskResultIsNull() {
            addCriterion("execute_task_result is null");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskResultIsNotNull() {
            addCriterion("execute_task_result is not null");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskResultEqualTo(String value) {
            addCriterion("execute_task_result =", value, "executeTaskResult");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskResultNotEqualTo(String value) {
            addCriterion("execute_task_result <>", value, "executeTaskResult");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskResultGreaterThan(String value) {
            addCriterion("execute_task_result >", value, "executeTaskResult");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskResultGreaterThanOrEqualTo(String value) {
            addCriterion("execute_task_result >=", value, "executeTaskResult");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskResultLessThan(String value) {
            addCriterion("execute_task_result <", value, "executeTaskResult");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskResultLessThanOrEqualTo(String value) {
            addCriterion("execute_task_result <=", value, "executeTaskResult");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskResultLike(String value) {
            addCriterion("execute_task_result like", value, "executeTaskResult");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskResultNotLike(String value) {
            addCriterion("execute_task_result not like", value, "executeTaskResult");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskResultIn(List<String> values) {
            addCriterion("execute_task_result in", values, "executeTaskResult");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskResultNotIn(List<String> values) {
            addCriterion("execute_task_result not in", values, "executeTaskResult");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskResultBetween(String value1, String value2) {
            addCriterion("execute_task_result between", value1, value2, "executeTaskResult");
            return (Criteria) this;
        }

        public Criteria andExecuteTaskResultNotBetween(String value1, String value2) {
            addCriterion("execute_task_result not between", value1, value2, "executeTaskResult");
            return (Criteria) this;
        }

        public Criteria andExecuteStartTimeIsNull() {
            addCriterion("execute_start_time is null");
            return (Criteria) this;
        }

        public Criteria andExecuteStartTimeIsNotNull() {
            addCriterion("execute_start_time is not null");
            return (Criteria) this;
        }

        public Criteria andExecuteStartTimeEqualTo(Date value) {
            addCriterion("execute_start_time =", value, "executeStartTime");
            return (Criteria) this;
        }

        public Criteria andExecuteStartTimeNotEqualTo(Date value) {
            addCriterion("execute_start_time <>", value, "executeStartTime");
            return (Criteria) this;
        }

        public Criteria andExecuteStartTimeGreaterThan(Date value) {
            addCriterion("execute_start_time >", value, "executeStartTime");
            return (Criteria) this;
        }

        public Criteria andExecuteStartTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("execute_start_time >=", value, "executeStartTime");
            return (Criteria) this;
        }

        public Criteria andExecuteStartTimeLessThan(Date value) {
            addCriterion("execute_start_time <", value, "executeStartTime");
            return (Criteria) this;
        }

        public Criteria andExecuteStartTimeLessThanOrEqualTo(Date value) {
            addCriterion("execute_start_time <=", value, "executeStartTime");
            return (Criteria) this;
        }

        public Criteria andExecuteStartTimeIn(List<Date> values) {
            addCriterion("execute_start_time in", values, "executeStartTime");
            return (Criteria) this;
        }

        public Criteria andExecuteStartTimeNotIn(List<Date> values) {
            addCriterion("execute_start_time not in", values, "executeStartTime");
            return (Criteria) this;
        }

        public Criteria andExecuteStartTimeBetween(Date value1, Date value2) {
            addCriterion("execute_start_time between", value1, value2, "executeStartTime");
            return (Criteria) this;
        }

        public Criteria andExecuteStartTimeNotBetween(Date value1, Date value2) {
            addCriterion("execute_start_time not between", value1, value2, "executeStartTime");
            return (Criteria) this;
        }

        public Criteria andExecuteStopTimeIsNull() {
            addCriterion("execute_stop_time is null");
            return (Criteria) this;
        }

        public Criteria andExecuteStopTimeIsNotNull() {
            addCriterion("execute_stop_time is not null");
            return (Criteria) this;
        }

        public Criteria andExecuteStopTimeEqualTo(Date value) {
            addCriterion("execute_stop_time =", value, "executeStopTime");
            return (Criteria) this;
        }

        public Criteria andExecuteStopTimeNotEqualTo(Date value) {
            addCriterion("execute_stop_time <>", value, "executeStopTime");
            return (Criteria) this;
        }

        public Criteria andExecuteStopTimeGreaterThan(Date value) {
            addCriterion("execute_stop_time >", value, "executeStopTime");
            return (Criteria) this;
        }

        public Criteria andExecuteStopTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("execute_stop_time >=", value, "executeStopTime");
            return (Criteria) this;
        }

        public Criteria andExecuteStopTimeLessThan(Date value) {
            addCriterion("execute_stop_time <", value, "executeStopTime");
            return (Criteria) this;
        }

        public Criteria andExecuteStopTimeLessThanOrEqualTo(Date value) {
            addCriterion("execute_stop_time <=", value, "executeStopTime");
            return (Criteria) this;
        }

        public Criteria andExecuteStopTimeIn(List<Date> values) {
            addCriterion("execute_stop_time in", values, "executeStopTime");
            return (Criteria) this;
        }

        public Criteria andExecuteStopTimeNotIn(List<Date> values) {
            addCriterion("execute_stop_time not in", values, "executeStopTime");
            return (Criteria) this;
        }

        public Criteria andExecuteStopTimeBetween(Date value1, Date value2) {
            addCriterion("execute_stop_time between", value1, value2, "executeStopTime");
            return (Criteria) this;
        }

        public Criteria andExecuteStopTimeNotBetween(Date value1, Date value2) {
            addCriterion("execute_stop_time not between", value1, value2, "executeStopTime");
            return (Criteria) this;
        }

        public Criteria andExecuteInputParamIsNull() {
            addCriterion("execute_input_param is null");
            return (Criteria) this;
        }

        public Criteria andExecuteInputParamIsNotNull() {
            addCriterion("execute_input_param is not null");
            return (Criteria) this;
        }

        public Criteria andExecuteInputParamEqualTo(String value) {
            addCriterion("execute_input_param =", value, "executeInputParam");
            return (Criteria) this;
        }

        public Criteria andExecuteInputParamNotEqualTo(String value) {
            addCriterion("execute_input_param <>", value, "executeInputParam");
            return (Criteria) this;
        }

        public Criteria andExecuteInputParamGreaterThan(String value) {
            addCriterion("execute_input_param >", value, "executeInputParam");
            return (Criteria) this;
        }

        public Criteria andExecuteInputParamGreaterThanOrEqualTo(String value) {
            addCriterion("execute_input_param >=", value, "executeInputParam");
            return (Criteria) this;
        }

        public Criteria andExecuteInputParamLessThan(String value) {
            addCriterion("execute_input_param <", value, "executeInputParam");
            return (Criteria) this;
        }

        public Criteria andExecuteInputParamLessThanOrEqualTo(String value) {
            addCriterion("execute_input_param <=", value, "executeInputParam");
            return (Criteria) this;
        }

        public Criteria andExecuteInputParamLike(String value) {
            addCriterion("execute_input_param like", value, "executeInputParam");
            return (Criteria) this;
        }

        public Criteria andExecuteInputParamNotLike(String value) {
            addCriterion("execute_input_param not like", value, "executeInputParam");
            return (Criteria) this;
        }

        public Criteria andExecuteInputParamIn(List<String> values) {
            addCriterion("execute_input_param in", values, "executeInputParam");
            return (Criteria) this;
        }

        public Criteria andExecuteInputParamNotIn(List<String> values) {
            addCriterion("execute_input_param not in", values, "executeInputParam");
            return (Criteria) this;
        }

        public Criteria andExecuteInputParamBetween(String value1, String value2) {
            addCriterion("execute_input_param between", value1, value2, "executeInputParam");
            return (Criteria) this;
        }

        public Criteria andExecuteInputParamNotBetween(String value1, String value2) {
            addCriterion("execute_input_param not between", value1, value2, "executeInputParam");
            return (Criteria) this;
        }

        public Criteria andIsDeleteIsNull() {
            addCriterion("is_delete is null");
            return (Criteria) this;
        }

        public Criteria andIsDeleteIsNotNull() {
            addCriterion("is_delete is not null");
            return (Criteria) this;
        }

        public Criteria andIsDeleteEqualTo(Byte value) {
            addCriterion("is_delete =", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteNotEqualTo(Byte value) {
            addCriterion("is_delete <>", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteGreaterThan(Byte value) {
            addCriterion("is_delete >", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteGreaterThanOrEqualTo(Byte value) {
            addCriterion("is_delete >=", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteLessThan(Byte value) {
            addCriterion("is_delete <", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteLessThanOrEqualTo(Byte value) {
            addCriterion("is_delete <=", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteIn(List<Byte> values) {
            addCriterion("is_delete in", values, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteNotIn(List<Byte> values) {
            addCriterion("is_delete not in", values, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteBetween(Byte value1, Byte value2) {
            addCriterion("is_delete between", value1, value2, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteNotBetween(Byte value1, Byte value2) {
            addCriterion("is_delete not between", value1, value2, "isDelete");
            return (Criteria) this;
        }

        public Criteria andDepartmentIdIsNull() {
            addCriterion("department_id is null");
            return (Criteria) this;
        }

        public Criteria andDepartmentIdIsNotNull() {
            addCriterion("department_id is not null");
            return (Criteria) this;
        }

        public Criteria andDepartmentIdEqualTo(Long value) {
            addCriterion("department_id =", value, "departmentId");
            return (Criteria) this;
        }

        public Criteria andDepartmentIdNotEqualTo(Long value) {
            addCriterion("department_id <>", value, "departmentId");
            return (Criteria) this;
        }

        public Criteria andDepartmentIdGreaterThan(Long value) {
            addCriterion("department_id >", value, "departmentId");
            return (Criteria) this;
        }

        public Criteria andDepartmentIdGreaterThanOrEqualTo(Long value) {
            addCriterion("department_id >=", value, "departmentId");
            return (Criteria) this;
        }

        public Criteria andDepartmentIdLessThan(Long value) {
            addCriterion("department_id <", value, "departmentId");
            return (Criteria) this;
        }

        public Criteria andDepartmentIdLessThanOrEqualTo(Long value) {
            addCriterion("department_id <=", value, "departmentId");
            return (Criteria) this;
        }

        public Criteria andDepartmentIdIn(List<Long> values) {
            addCriterion("department_id in", values, "departmentId");
            return (Criteria) this;
        }

        public Criteria andDepartmentIdNotIn(List<Long> values) {
            addCriterion("department_id not in", values, "departmentId");
            return (Criteria) this;
        }

        public Criteria andDepartmentIdBetween(Long value1, Long value2) {
            addCriterion("department_id between", value1, value2, "departmentId");
            return (Criteria) this;
        }

        public Criteria andDepartmentIdNotBetween(Long value1, Long value2) {
            addCriterion("department_id not between", value1, value2, "departmentId");
            return (Criteria) this;
        }

        public Criteria andEnterpriseIdIsNull() {
            addCriterion("enterprise_id is null");
            return (Criteria) this;
        }

        public Criteria andEnterpriseIdIsNotNull() {
            addCriterion("enterprise_id is not null");
            return (Criteria) this;
        }

        public Criteria andEnterpriseIdEqualTo(Long value) {
            addCriterion("enterprise_id =", value, "enterpriseId");
            return (Criteria) this;
        }

        public Criteria andEnterpriseIdNotEqualTo(Long value) {
            addCriterion("enterprise_id <>", value, "enterpriseId");
            return (Criteria) this;
        }

        public Criteria andEnterpriseIdGreaterThan(Long value) {
            addCriterion("enterprise_id >", value, "enterpriseId");
            return (Criteria) this;
        }

        public Criteria andEnterpriseIdGreaterThanOrEqualTo(Long value) {
            addCriterion("enterprise_id >=", value, "enterpriseId");
            return (Criteria) this;
        }

        public Criteria andEnterpriseIdLessThan(Long value) {
            addCriterion("enterprise_id <", value, "enterpriseId");
            return (Criteria) this;
        }

        public Criteria andEnterpriseIdLessThanOrEqualTo(Long value) {
            addCriterion("enterprise_id <=", value, "enterpriseId");
            return (Criteria) this;
        }

        public Criteria andEnterpriseIdIn(List<Long> values) {
            addCriterion("enterprise_id in", values, "enterpriseId");
            return (Criteria) this;
        }

        public Criteria andEnterpriseIdNotIn(List<Long> values) {
            addCriterion("enterprise_id not in", values, "enterpriseId");
            return (Criteria) this;
        }

        public Criteria andEnterpriseIdBetween(Long value1, Long value2) {
            addCriterion("enterprise_id between", value1, value2, "enterpriseId");
            return (Criteria) this;
        }

        public Criteria andEnterpriseIdNotBetween(Long value1, Long value2) {
            addCriterion("enterprise_id not between", value1, value2, "enterpriseId");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(Long value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(Long value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(Long value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(Long value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(Long value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(Long value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<Long> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<Long> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(Long value1, Long value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(Long value1, Long value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andResultDataSizeIsNull() {
            addCriterion("result_data_size is null");
            return (Criteria) this;
        }

        public Criteria andResultDataSizeIsNotNull() {
            addCriterion("result_data_size is not null");
            return (Criteria) this;
        }

        public Criteria andResultDataSizeEqualTo(Long value) {
            addCriterion("result_data_size =", value, "resultDataSize");
            return (Criteria) this;
        }

        public Criteria andResultDataSizeNotEqualTo(Long value) {
            addCriterion("result_data_size <>", value, "resultDataSize");
            return (Criteria) this;
        }

        public Criteria andResultDataSizeGreaterThan(Long value) {
            addCriterion("result_data_size >", value, "resultDataSize");
            return (Criteria) this;
        }

        public Criteria andResultDataSizeGreaterThanOrEqualTo(Long value) {
            addCriterion("result_data_size >=", value, "resultDataSize");
            return (Criteria) this;
        }

        public Criteria andResultDataSizeLessThan(Long value) {
            addCriterion("result_data_size <", value, "resultDataSize");
            return (Criteria) this;
        }

        public Criteria andResultDataSizeLessThanOrEqualTo(Long value) {
            addCriterion("result_data_size <=", value, "resultDataSize");
            return (Criteria) this;
        }

        public Criteria andResultDataSizeIn(List<Long> values) {
            addCriterion("result_data_size in", values, "resultDataSize");
            return (Criteria) this;
        }

        public Criteria andResultDataSizeNotIn(List<Long> values) {
            addCriterion("result_data_size not in", values, "resultDataSize");
            return (Criteria) this;
        }

        public Criteria andResultDataSizeBetween(Long value1, Long value2) {
            addCriterion("result_data_size between", value1, value2, "resultDataSize");
            return (Criteria) this;
        }

        public Criteria andResultDataSizeNotBetween(Long value1, Long value2) {
            addCriterion("result_data_size not between", value1, value2, "resultDataSize");
            return (Criteria) this;
        }

        public Criteria andResultExpireTimeIsNull() {
            addCriterion("result_expire_time is null");
            return (Criteria) this;
        }

        public Criteria andResultExpireTimeIsNotNull() {
            addCriterion("result_expire_time is not null");
            return (Criteria) this;
        }

        public Criteria andResultExpireTimeEqualTo(Date value) {
            addCriterion("result_expire_time =", value, "resultExpireTime");
            return (Criteria) this;
        }

        public Criteria andResultExpireTimeNotEqualTo(Date value) {
            addCriterion("result_expire_time <>", value, "resultExpireTime");
            return (Criteria) this;
        }

        public Criteria andResultExpireTimeGreaterThan(Date value) {
            addCriterion("result_expire_time >", value, "resultExpireTime");
            return (Criteria) this;
        }

        public Criteria andResultExpireTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("result_expire_time >=", value, "resultExpireTime");
            return (Criteria) this;
        }

        public Criteria andResultExpireTimeLessThan(Date value) {
            addCriterion("result_expire_time <", value, "resultExpireTime");
            return (Criteria) this;
        }

        public Criteria andResultExpireTimeLessThanOrEqualTo(Date value) {
            addCriterion("result_expire_time <=", value, "resultExpireTime");
            return (Criteria) this;
        }

        public Criteria andResultExpireTimeIn(List<Date> values) {
            addCriterion("result_expire_time in", values, "resultExpireTime");
            return (Criteria) this;
        }

        public Criteria andResultExpireTimeNotIn(List<Date> values) {
            addCriterion("result_expire_time not in", values, "resultExpireTime");
            return (Criteria) this;
        }

        public Criteria andResultExpireTimeBetween(Date value1, Date value2) {
            addCriterion("result_expire_time between", value1, value2, "resultExpireTime");
            return (Criteria) this;
        }

        public Criteria andResultExpireTimeNotBetween(Date value1, Date value2) {
            addCriterion("result_expire_time not between", value1, value2, "resultExpireTime");
            return (Criteria) this;
        }

        public Criteria andFailureRetryCountIsNull() {
            addCriterion("failure_retry_count is null");
            return (Criteria) this;
        }

        public Criteria andFailureRetryCountIsNotNull() {
            addCriterion("failure_retry_count is not null");
            return (Criteria) this;
        }

        public Criteria andFailureRetryCountEqualTo(Integer value) {
            addCriterion("failure_retry_count =", value, "failureRetryCount");
            return (Criteria) this;
        }

        public Criteria andFailureRetryCountNotEqualTo(Integer value) {
            addCriterion("failure_retry_count <>", value, "failureRetryCount");
            return (Criteria) this;
        }

        public Criteria andFailureRetryCountGreaterThan(Integer value) {
            addCriterion("failure_retry_count >", value, "failureRetryCount");
            return (Criteria) this;
        }

        public Criteria andFailureRetryCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("failure_retry_count >=", value, "failureRetryCount");
            return (Criteria) this;
        }

        public Criteria andFailureRetryCountLessThan(Integer value) {
            addCriterion("failure_retry_count <", value, "failureRetryCount");
            return (Criteria) this;
        }

        public Criteria andFailureRetryCountLessThanOrEqualTo(Integer value) {
            addCriterion("failure_retry_count <=", value, "failureRetryCount");
            return (Criteria) this;
        }

        public Criteria andFailureRetryCountIn(List<Integer> values) {
            addCriterion("failure_retry_count in", values, "failureRetryCount");
            return (Criteria) this;
        }

        public Criteria andFailureRetryCountNotIn(List<Integer> values) {
            addCriterion("failure_retry_count not in", values, "failureRetryCount");
            return (Criteria) this;
        }

        public Criteria andFailureRetryCountBetween(Integer value1, Integer value2) {
            addCriterion("failure_retry_count between", value1, value2, "failureRetryCount");
            return (Criteria) this;
        }

        public Criteria andFailureRetryCountNotBetween(Integer value1, Integer value2) {
            addCriterion("failure_retry_count not between", value1, value2, "failureRetryCount");
            return (Criteria) this;
        }

        public Criteria andFailureReasonIsNull() {
            addCriterion("failure_reason is null");
            return (Criteria) this;
        }

        public Criteria andFailureReasonIsNotNull() {
            addCriterion("failure_reason is not null");
            return (Criteria) this;
        }

        public Criteria andFailureReasonEqualTo(String value) {
            addCriterion("failure_reason =", value, "failureReason");
            return (Criteria) this;
        }

        public Criteria andFailureReasonNotEqualTo(String value) {
            addCriterion("failure_reason <>", value, "failureReason");
            return (Criteria) this;
        }

        public Criteria andFailureReasonGreaterThan(String value) {
            addCriterion("failure_reason >", value, "failureReason");
            return (Criteria) this;
        }

        public Criteria andFailureReasonGreaterThanOrEqualTo(String value) {
            addCriterion("failure_reason >=", value, "failureReason");
            return (Criteria) this;
        }

        public Criteria andFailureReasonLessThan(String value) {
            addCriterion("failure_reason <", value, "failureReason");
            return (Criteria) this;
        }

        public Criteria andFailureReasonLessThanOrEqualTo(String value) {
            addCriterion("failure_reason <=", value, "failureReason");
            return (Criteria) this;
        }

        public Criteria andFailureReasonLike(String value) {
            addCriterion("failure_reason like", value, "failureReason");
            return (Criteria) this;
        }

        public Criteria andFailureReasonNotLike(String value) {
            addCriterion("failure_reason not like", value, "failureReason");
            return (Criteria) this;
        }

        public Criteria andFailureReasonIn(List<String> values) {
            addCriterion("failure_reason in", values, "failureReason");
            return (Criteria) this;
        }

        public Criteria andFailureReasonNotIn(List<String> values) {
            addCriterion("failure_reason not in", values, "failureReason");
            return (Criteria) this;
        }

        public Criteria andFailureReasonBetween(String value1, String value2) {
            addCriterion("failure_reason between", value1, value2, "failureReason");
            return (Criteria) this;
        }

        public Criteria andFailureReasonNotBetween(String value1, String value2) {
            addCriterion("failure_reason not between", value1, value2, "failureReason");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}