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
