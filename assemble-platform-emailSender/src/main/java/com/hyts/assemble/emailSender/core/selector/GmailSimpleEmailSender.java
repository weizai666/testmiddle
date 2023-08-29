package com.hyts.assemble.emailSender.core.selector;

import com.hyts.assemble.emailSender.core.sender.SimpleEmailSender;
import org.springframework.stereotype.Component;

/**
 * 谷歌
 * GmailSimpleEmailSender
 *
 * @author kancy
 * @date 2020/2/20 1:59
 */
@Component
public class GmailSimpleEmailSender extends SimpleEmailSender {
    public GmailSimpleEmailSender() {
        setHost("smtp.gmail.com");
    }

    public GmailSimpleEmailSender(String username, String password) {
        this();
        setUsername(username);
        setPassword(password);
    }
}
