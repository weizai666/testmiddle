package com.hyts.assemble.dynamicTask.dao;

import com.hyts.assemble.dynamicTask.model.JobLogReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * job log
 * @author xuxueli 2019-11-22
 */
@Mapper
public interface JobLogReportDao {

	public int save(JobLogReport xxlJobLogReport);

	public int update(JobLogReport xxlJobLogReport);

	public List<JobLogReport> queryLogReport(@Param("triggerDayFrom") Date triggerDayFrom,
												@Param("triggerDayTo") Date triggerDayTo);

	public JobLogReport queryLogReportTotal();

}
