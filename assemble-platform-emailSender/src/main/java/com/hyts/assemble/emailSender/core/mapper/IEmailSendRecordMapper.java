package com.hyts.assemble.emailSender.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hyts.assemble.emailSender.core.bean.EmailSendRecordEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 邮箱发送记录表 Mapper 接口
 * </p>
 *
 * @author libo
 * @since 2022-08-13
 */
@Mapper
public interface IEmailSendRecordMapper extends BaseMapper<EmailSendRecordEntity> {

}
