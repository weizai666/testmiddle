package com.hyts.assemble.emailSender.core.datasource;

import com.hyts.assemble.emailSender.core.model.EmailSenderProperties;

import java.util.List;

/**
 * EmailSenderPropertiesDataSource
 *
 * @author kancy
 * @date 2020/2/20 3:24
 */
public interface EmailSenderPropertiesDataSource {

    /**
     * 加载
     * @return
     */
    List<EmailSenderProperties> load();

}
