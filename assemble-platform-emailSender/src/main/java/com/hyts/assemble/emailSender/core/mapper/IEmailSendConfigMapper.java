package com.hyts.assemble.emailSender.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hyts.assemble.emailSender.core.bean.EmailSendConfigEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 邮箱发送配置 Mapper 接口
 * </p>
 *
 * @author libo
 * @since 2022-08-13
 */
@Mapper
public interface IEmailSendConfigMapper extends BaseMapper<EmailSendConfigEntity> {

}
