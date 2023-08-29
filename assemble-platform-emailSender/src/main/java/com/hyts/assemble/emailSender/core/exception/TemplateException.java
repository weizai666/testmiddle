package com.hyts.assemble.emailSender.core.exception;

/**
 * TemplateException
 *
 * @author kancy
 * @date 2020/2/22 16:25
 */
public class TemplateException extends RuntimeException {
    public TemplateException(String message) {
        super(message);
    }

    public TemplateException(String message, Throwable cause) {
        super(message, cause);
    }

    public TemplateException(Throwable cause) {
        super(cause);
    }
}
