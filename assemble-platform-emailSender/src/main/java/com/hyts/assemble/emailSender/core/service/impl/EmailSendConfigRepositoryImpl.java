package com.hyts.assemble.emailSender.core.service.impl;

import com.hyts.assemble.emailSender.core.bean.EmailSendConfigEntity;
import com.hyts.assemble.emailSender.core.mapper.IEmailSendConfigMapper;
import com.hyts.assemble.emailSender.core.service.MPEmailSendConfigRepository;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 邮箱发送配置 服务实现类
 * </p>
 *
 * @author libo
 * @since 2022-08-13
 */
@Service
public class EmailSendConfigRepositoryImpl extends ServiceImpl<IEmailSendConfigMapper, EmailSendConfigEntity> implements MPEmailSendConfigRepository {

}
