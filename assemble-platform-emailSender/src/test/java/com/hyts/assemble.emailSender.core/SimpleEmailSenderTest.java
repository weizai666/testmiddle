package com.hyts.assemble.emailSender.core;

import com.hyts.assemble.emailSender.core.exception.EmailException;
import com.hyts.assemble.emailSender.core.model.SimpleEmail;
import com.hyts.assemble.emailSender.core.sender.EmailSender;
import com.hyts.assemble.emailSender.core.sender.QQSimpleEmailSender;
import com.hyts.assemble.emailSender.core.sender.SimpleEmailSender;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SimpleEmailSenderTest
 *
 * @author kancy
 * @date 2020/2/20 1:36
 */
public class SimpleEmailSenderTest {


    Logger logger = LoggerFactory.getLogger(SimpleEmailSenderTest.class);

    private EmailSender emailSender;

    private EmailSender qqSimpleEmailSender = new QQSimpleEmailSender("569590478@qq.com", "ENC(0Xn6xLVRo/JnOaY9e9RwtcFf2mKIw5tmB9W67pVCOR0=)");

    @Before
    public void initSimpleEmailSender(){
        SimpleEmailSender simpleEmailSender = new SimpleEmailSender();
        simpleEmailSender.setHost("smtp.163.com");
        simpleEmailSender.setUsername("haoyutianshang@163.com");
        simpleEmailSender.setPassword("AXMKEONFJKSXAVJR");
        emailSender = simpleEmailSender;
    }

    @Test
    public void testSimpleEmailSender(){
        try {
            emailSender.send(new SimpleEmail("bo.li@wiz.ai", "test success"));
        } catch (EmailException e) {
            logger.error("失败",e);
        }
    }

    @Test
    public void testQQSimpleEmailSender(){
        try {
            qqSimpleEmailSender.send(new SimpleEmail("huangchengkang@vcredit.com", "Handsome Guy!"));
        } catch (EmailException e) {
            Assert.fail();
        }
    }
}
