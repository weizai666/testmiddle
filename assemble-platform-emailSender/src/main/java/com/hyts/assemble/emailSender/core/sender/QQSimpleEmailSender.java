package com.hyts.assemble.emailSender.core.sender;

/**
 * 腾讯
 * QQSimpleEmailSender
 *
 * @author kancy
 * @date 2020/2/20 1:50
 */
public class QQSimpleEmailSender extends SimpleEmailSender {

    public QQSimpleEmailSender() {
        setHost("smtp.qq.com");
    }

    public QQSimpleEmailSender(String username, String password) {
        this();
        setUsername(username);
        setPassword(password);
    }
}
