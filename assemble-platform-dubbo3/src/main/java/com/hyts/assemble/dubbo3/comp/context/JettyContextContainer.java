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
package com.hyts.assemble.dubbo3.comp.context;

import com.hyts.assemble.dubbo3.comp.container.JettyContainer;
import lombok.SneakyThrows;
import org.apache.dubbo.common.config.ConfigurationUtils;
import org.apache.dubbo.container.Container;
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


/**
 * @project-name:middleware
 * @package-name:com.hyts.assemble.dubbo3.comp.context
 * @author:LiBo/Alex
 * @create-date:2022-11-24 22:07
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
public class JettyContextContainer implements Container {


    public static final String JETTY_PORT = "dubbo.jetty.port";

    private static final Logger logger = LoggerFactory.getLogger(JettyContainer.class);


    private static Server server =
            new Server(Integer.valueOf(ConfigurationUtils.getProperty(JETTY_PORT,"80")));

    /**
     * jetty的核心处理器
     * @param handler
     */
    private void setHandler(Handler handler){
        server.setHandler(handler);
    }


    @Override
    public void start() {
        try {
            server.setHandler(new ProcessorHandler());
            server.start();
            server.join();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static class ProcessorHandler extends AbstractHandler {

        @Override
        public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
            if(s.equalsIgnoreCase("/shutdown")){
                try {
                    server.stop();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            httpServletResponse.setContentType("text/html;charset=utf-8");
            request.setHandled(Boolean.TRUE);
            httpServletResponse.getWriter().write("hello 这是jetty容器服务");
            httpServletResponse.getWriter().close();
        }
    }

}
