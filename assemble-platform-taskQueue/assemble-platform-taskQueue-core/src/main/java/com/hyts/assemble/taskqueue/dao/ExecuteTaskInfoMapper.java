package com.hyts.assemble.taskqueue.dao;

import com.hyts.assemble.taskqueue.model.ExecuteTaskInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ExecuteTaskInfoMapper {

    long countByExample(ExecuteTaskInfoExample example);

    int deleteByExample(ExecuteTaskInfoExample example);

    int deleteByPrimaryKey(Long executeTaskId);

    int insert(ExecuteTaskInfo record);

    int insertSelective(ExecuteTaskInfo record);

    List<ExecuteTaskInfo> selectByExample(ExecuteTaskInfoExample example);

    ExecuteTaskInfo selectByPrimaryKey(Long executeTaskId);

    int updateByExampleSelective(@Param("record") ExecuteTaskInfo record, @Param("example") ExecuteTaskInfoExample example);

    int updateByExample(@Param("record") ExecuteTaskInfo record, @Param("example") ExecuteTaskInfoExample example);

    int updateByPrimaryKeySelective(ExecuteTaskInfo record);

    int updateByPrimaryKey(ExecuteTaskInfo record);
}