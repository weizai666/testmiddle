package com.hyts.assemble.emailSender.core.selector;

import com.hyts.assemble.emailSender.core.sender.EmailSender;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机策略
 * RandomEmailSenderSelector
 *
 * @author kancy
 * @date 2020/2/20 2:58
 */
public class RandomEmailSenderSelector implements EmailSenderSelector {

    private static final int RANDOM_FACTOR = 10000;
    /**
     * 找到一个可用的EmailSender
     *
     * @param emailSenders
     * @return
     */
    @Override
    public EmailSender findEmailSender(List<EmailSender> emailSenders) {
        return emailSenders.get(ThreadLocalRandom.current()
                .nextInt(0, emailSenders.size() * RANDOM_FACTOR) / RANDOM_FACTOR);
    }
}
