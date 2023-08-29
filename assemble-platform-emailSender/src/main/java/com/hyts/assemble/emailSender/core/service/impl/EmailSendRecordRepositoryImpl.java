package com.hyts.assemble.emailSender.core.service.impl;

import com.hyts.assemble.emailSender.core.bean.EmailSendRecordEntity;
import com.hyts.assemble.emailSender.core.mapper.IEmailSendRecordMapper;
import com.hyts.assemble.emailSender.core.service.MPEmailSendRecordRepository;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 邮箱发送记录表 服务实现类
 * </p>
 *
 * @author libo
 * @since 2022-08-13
 */
@Service
public class EmailSendRecordRepositoryImpl extends ServiceImpl<IEmailSendRecordMapper, EmailSendRecordEntity> implements MPEmailSendRecordRepository {

}
