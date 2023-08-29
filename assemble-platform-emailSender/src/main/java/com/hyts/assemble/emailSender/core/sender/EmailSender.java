package com.hyts.assemble.emailSender.core.sender;


import com.hyts.assemble.emailSender.core.model.Email;

/**
 * EmailSender
 *
 * @author kancy
 * @date 2020/2/19 23:02
 */
public interface EmailSender {
    /**
     * 发送邮件
     * @param message
     */
    void send(Email message);

    /**
     * 获取EmailSender一个名词标识
     * @return
     */
    String getSenderName();

}
