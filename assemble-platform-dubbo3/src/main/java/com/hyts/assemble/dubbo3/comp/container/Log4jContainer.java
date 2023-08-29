/**
 * Copyright [2019] [LiBo/Alex of copyright liboware@gmail.com ]
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hyts.assemble.dubbo3.comp.container;

import ch.qos.logback.core.Appender;
import org.apache.dubbo.common.config.ConfigurationUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.container.Container;
import java.util.Enumeration;
import java.util.Properties;
import org.apache.dubbo.common.config.ConfigurationUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.container.Container;
import org.apache.log4j.FileAppender;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import java.util.Enumeration;
import java.util.Properties;

/**
 * @project-name:middleware
 * @package-name:com.hyts.assemble.dubbo3.comp.container
 * @author:LiBo/Alex
 * @create-date:2022-11-05 23:19
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
public class Log4jContainer implements Container {

    public static final String LOG4J_FILE = "dubbo.log4j.file";
    public static final String LOG4J_LEVEL = "dubbo.log4j.level";
    public static final String LOG4J_SUBDIRECTORY = "dubbo.log4j.subdirectory";
    public static final String DEFAULT_LOG4J_LEVEL = "ERROR";

    public Log4jContainer() {
    }

    @Override
    public void start() {
        String file = ConfigurationUtils.getProperty("dubbo.log4j.file");
        String subdirectory;
        if (file != null && file.length() > 0) {
            subdirectory = ConfigurationUtils.getProperty("dubbo.log4j.level");
            if (StringUtils.isEmpty(subdirectory)) {
                subdirectory = "ERROR";
            }

            Properties properties = new Properties();
            properties.setProperty("log4j.rootLogger", subdirectory + ",application");
            properties.setProperty("log4j.appender.application", "org.apache.log4j.DailyRollingFileAppender");
            properties.setProperty("log4j.appender.application.File", file);
            properties.setProperty("log4j.appender.application.Append", "true");
            properties.setProperty("log4j.appender.application.DatePattern", "'.'yyyy-MM-dd");
            properties.setProperty("log4j.appender.application.layout", "org.apache.log4j.PatternLayout");
            properties.setProperty("log4j.appender.application.layout.ConversionPattern", "%d [%t] %-5p %C{6} (%F:%L) - %m%n");
            PropertyConfigurator.configure(properties);
        }

        subdirectory = ConfigurationUtils.getProperty("dubbo.log4j.subdirectory");
        if (subdirectory != null && subdirectory.length() > 0) {
            Enumeration<Logger> ls = LogManager.getCurrentLoggers();

            while(true) {
                Logger l;
                do {
                    if (!ls.hasMoreElements()) {
                        return;
                    }

                    l = (Logger)ls.nextElement();
                } while(l == null);

                Enumeration<Appender> as = l.getAllAppenders();

                while(as.hasMoreElements()) {
                    Appender a = (Appender)as.nextElement();
                    if (a instanceof FileAppender) {
                        FileAppender fa = (FileAppender)a;
                        String f = fa.getFile();
                        if (f != null && f.length() > 0) {
                            int i = f.replace('\\', '/').lastIndexOf(47);
                            String path;
                            if (i == -1) {
                                path = subdirectory;
                            } else {
                                path = f.substring(0, i);
                                if (!path.endsWith(subdirectory)) {
                                    path = path + "/" + subdirectory;
                                }

                                f = f.substring(i + 1);
                            }

                            fa.setFile(path + "/" + f);
                            fa.activateOptions();
                        }
                    }
                }
            }
        }
    }


    @Override
    public void stop() {
    }
}
