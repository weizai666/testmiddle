> **了解 Dubbo 3 中服务自定义容器类型和使用**

## 背景介绍

> **Dubbo3的服务容器是一个standalone的启动程序，因为后台服务不需要 Tomcat 或 JBoss 等 Web 容器的功能，如果硬要用 Web 容器去加载服务提供方，增加复杂性，也浪费资源。所以服务通常不需要Tomcat/JBoss等Web容器的特性，没必要用 Web 容器去加载服务**。

## 特性说明

- **Dubbo3服务容器只是一个简单的 Main 方法，并加载一个简单的Spring容器，用于暴露服务**。
- **服务容器的加载内容可以扩展，内置了spring, jetty, log4j等加载，可通过容器扩展点进行扩展。配置配在java命令的-D参数或者dubbo.properties中**。

## 使用场景

- **web容器主要是用来响应http请求以及静态页面的**。
- **Dubbo服务提供方只是对外提供 dubbo 服务，用web容器不太适合，单独作为dubbo服务提供方，只需要通过一个 main方法加载一个简单的 spring 容器将服务暴露**。

## 使用方式

### Spring Container

> **自动加载 META-INF/spring 目录下的所有 Spring 配置**。

#### 配置 spring 配置加载位置：

```xml
dubbo.spring.config=classpath*:META-INF/spring/*.xml
```

### Jetty Container

> **启动一个内嵌 Jetty，用于汇报状态。**

#### 配置：

```
dubbo.jetty.port=8080：配置 jetty 启动端口
dubbo.jetty.directory=/foo/bar：配置可通过 jetty 直接访问的目录，用于存放静态文件
dubbo.jetty.page=log,status,system：配置显示的页面，缺省加载所有页面
```

### Log4j Container

> **自动配置 log4j 的配置，在多进程启动时，自动给日志文件按进程分目录。**


#### 配置

```
dubbo.log4j.file=/foo/bar.log：配置日志文件路径
dubbo.log4j.level=WARN：配置日志级别
dubbo.log4j.subdirectory=20880：配置日志子目录，用于多进程启动，避免冲突容器加载说明 
```

## 容器加载说明

### 缺省只加载 spring

```
java org.apache.dubbo.container.Main
```

### 通过main函数参数传入要加载的容器

```
java org.apache.dubbo.container.Main spring jetty log4j
```

### 通过 JVM 启动参数传入要加载的容器

```
java org.apache.dubbo.container.Main -Ddubbo.container=spring,jetty,log4j
```

### 通过 classpath 下的 dubbo.properties 配置传入要加载的容器

```
dubbo.container=spring,jetty,log4j
```

> 以上在低版本中还存在，但是高版本都已经移除了，所以本次来带着大家定义实现以下容器的实现机制

## 容器扩展

### 扩展说明

> 服务容器扩展，用于自定义加载内容。

### 扩展接口

> **org.apache.dubbo.container.Container**

#### 扩展配置

> **java org.apache.dubbo.container.Main spring jetty log4j**

#### 已知扩展
```
org.apache.dubbo.container.spring.SpringContainer
org.apache.dubbo.container.spring.JettyContainer
org.apache.dubbo.container.spring.Log4jContainer
```

#### 扩展示例

##### Maven 项目结构：

```java
src
 |-main
    |-java
        |-com
            |-xxx
                |-XxxContainer.java (实现Container接口)
    |-resources
        |-META-INF
            |-dubbo
                |-org.apache.dubbo.container.Container (纯文本文件，内容为：xxx=com.xxx.XxxContainer)
```

##### XxxContainer.java：

```java
package com.xxx;
import org.apache.dubbo.container.Container;
public class XxxContainer implements Container {
    public Status start() {
        // ...
    }
    public Status stop() {
        // ...
    }
}
```

##### 扩展容器配置方式

```
META-INF/dubbo/org.apache.dubbo.container.Container：
xxx=com.xxx.XxxContainer
```

### 代码实战案例

#### java代码案例

##### 配置Container扩展点

>配置 META-INF.dubbo下的文件org.apache.dubbo.container.Container

```
undertow=com.hyts.assemble.dubbo3.comp.container.UnderTowContainer
jetty=com.hyts.assemble.dubbo3.comp.container.JettyContainer
log4j=com.hyts.assemble.dubbo3.comp.container.Log4jContainer
```

##### JettyContainer代码类
```java
/*
 *
 *   Licensed to the Apache Software Foundation (ASF) under one or more
 *   contributor license agreements.  See the NOTICE file distributed with
 *   this work for additional information regarding copyright ownership.
 *   The ASF licenses this file to You under the Apache License, Version 2.0
 *   (the "License"); you may not use this file except in compliance with
 *   the License.  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package com.hyts.assemble.dubbo3.comp.container;

import cn.hutool.core.util.NumberUtil;
import com.alibaba.dubbo.container.Container;
import io.undertow.Undertow;
import io.undertow.util.Headers;
import org.apache.dubbo.common.config.ConfigurationUtils;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * Dubbo Container Extension, used to customize the load content.
 */
public class JettyContainer implements Container {


    public static final String JETTY_PORT = "dubbo.jetty.port";
    public static final String JETTY_DIR = "dubbo.jetty.directory";
    public static final String JETTY_DEFAULT_PAGE = "dubbo.jetty.page";

    private static final Logger logger = LoggerFactory.getLogger(JettyContainer.class);

    private static Server server = new
            Server(NumberUtil.parseInt(ConfigurationUtils.getProperty(JETTY_PORT)));


    public void setServerHandler(Handler handler){
        server.setHandler(handler);
    }


    @Override
    public void start() {
        try {
            setServerHandler(new RestProcessHandler());
            server.start();
            server.join();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to start jetty" + e.getMessage(), e);
        }
    }

    @Override
    public void stop() {
        if (server != null) {
            try {
                server.stop();
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
            }
        }
    }


    public class RestProcessHandler extends AbstractHandler
    {
        @Override
        public void handle(String target,
                           Request baseRequest,
                           HttpServletRequest request,
                           HttpServletResponse response)
                throws IOException, ServletException
        {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            baseRequest.setHandled(true);
            response.getWriter().println(Files.readAllLines(Paths.get(ConfigurationUtils.getProperty(JETTY_DIR)+target)).
                    stream().collect(Collectors.joining()));
        }
    }
}
```
##### Log4jContainer代码类

```java
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
```

##### UnderTowContainer代码类
```java
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

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.NumberUtil;
import io.undertow.Undertow;
import io.undertow.util.Headers;
import org.apache.dubbo.common.config.ConfigurationUtils;
import org.apache.dubbo.container.Container;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * @project-name:dubbo-samples
 * @package-name:org.apache.dubbo.samples.group
 * @author:LiBo/Alex
 * @create-date:2022-11-05 22:56
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
public class UnderTowContainer implements Container {

    public static final String UNDERTOW_PORT = "dubbo.undertow.port";
    public static final String UNDERTOW_DIR = "dubbo.undertow.directory";
    public static final String UNDERTOW_DEFAULT_PAGE = "dubbo.undertow.page";

    //设置HttpHandler回调方法
   final Undertow server = Undertow.builder()
            .addHttpListener(NumberUtil.parseInt(ConfigurationUtils.getProperty(UNDERTOW_PORT)), "localhost")
            .setHandler(exchange -> {
                exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/html;charset=utf-8");
                exchange.getResponseSender().send(Files.readAllLines(Paths.get(ConfigurationUtils.getProperty(UNDERTOW_DIR)+exchange.getRequestPath())).
                        stream().collect(Collectors.joining()));
            }).build();


    @Override
    public void start() {
        server.start();
    }

    @Override
    public void stop() {
        server.stop();
    }
}
```

##### 容器启动类

```java
/*
 *
 *   Licensed to the Apache Software Foundation (ASF) under one or more
 *   contributor license agreements.  See the NOTICE file distributed with
 *   this work for additional information regarding copyright ownership.
 *   The ASF licenses this file to You under the Apache License, Version 2.0
 *   (the "License"); you may not use this file except in compliance with
 *   the License.  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package com.hyts.assemble.dubbo3.comp.container;

public class ContainerBootstrap {
    public static void main(String[] args) {
        org.apache.dubbo.container.Main.main(args);
    }
}
```

##### dubbo.properties

```properties
dubbo.container=spring,jetty,log4j,undertow
dubbo.jetty.port=8080
dubbo.jetty.directory=D:\\project-other\\middleware\\assemble-platform-dubbo3\\src\\main\\resources\\statics\\
dubbo.undertow.port=8081
dubbo.undertow.directory=D:\\project-other\\middleware\\assemble-platform-dubbo3\\src\\main\\resources\\statics\\
```

##### log4j.properties

```properties
#
#
#   Licensed to the Apache Software Foundation (ASF) under one or more
#   contributor license agreements.  See the NOTICE file distributed with
#   this work for additional information regarding copyright ownership.
#   The ASF licenses this file to You under the Apache License, Version 2.0
#   (the "License"); you may not use this file except in compliance with
#   the License.  You may obtain a copy of the License at
#
#       http://www.apache.org/licenses/LICENSE-2.0
#
#   Unless required by applicable law or agreed to in writing, software
#   distributed under the License is distributed on an "AS IS" BASIS,
#   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#   See the License for the specific language governing permissions and
#   limitations under the License.
#
#

###set log levels###
log4j.rootLogger=info, stdout
###output to the console###
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.Target=System.out
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=[%d{dd/MM/yy hh:mm:ss:sss z}] %t %5p %c{2}: %m%n
```