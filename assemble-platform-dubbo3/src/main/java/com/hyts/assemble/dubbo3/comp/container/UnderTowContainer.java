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
import com.hyts.assemble.dubbo3.comp.auth.AuthService;
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

    public static final String UNDERTOW_DEFAULT_PATH = "dubbo.undertow.path";

    private AuthService authService = new AuthService();

    //设置HttpHandler回调方法
   final Undertow server = Undertow.builder()
            .addHttpListener(NumberUtil.parseInt(ConfigurationUtils.getProperty(UNDERTOW_PORT)), "localhost")
            .setHandler(exchange -> {
                if(exchange.getRequestPath().equals(ConfigurationUtils.getProperty(UNDERTOW_DEFAULT_PATH))){
                    String appKey = String.valueOf(exchange.getQueryParameters().get("appKey").poll());
                    String secretKey = String.valueOf(exchange.getQueryParameters().get("secretKey").poll());
                    System.out.println("ak:"+appKey+"  --- sk:"+secretKey);
                    authService.setAppKey(ConfigurationUtils.getProperty(authService.AUTH_APP_KEY));
                    authService.setSecretKey(ConfigurationUtils.getProperty(authService.AUTH_SECRET_KEY));
                    boolean result = authService.matchSecretKey(appKey,secretKey);
                    System.out.println("auth the sk and ak pass:"+result);
                    exchange.getResponseSender().send(String.valueOf(result));
                }else {
                    exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/html;charset=utf-8");
                    exchange.getResponseSender().send(Files.readAllLines(Paths.get(ConfigurationUtils.getProperty(UNDERTOW_DIR) + exchange.getRequestPath())).stream().collect(Collectors.joining()));
                }
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
