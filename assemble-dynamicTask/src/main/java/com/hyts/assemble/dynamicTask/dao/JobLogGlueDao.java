package com.hyts.assemble.dynamicTask.dao;

import com.hyts.assemble.dynamicTask.model.JobLogGlue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * job log for glue
 * @author xuxueli 2016-5-19 18:04:56
 */
@Mapper
public interface JobLogGlueDao {
	
	public int save(JobLogGlue xxlJobLogGlue);
	
	public List<JobLogGlue> findByJobId(@Param("jobId") int jobId);

	public int removeOld(@Param("jobId") int jobId, @Param("limit") int limit);

	public int deleteByJobId(@Param("jobId") int jobId);
	
}
