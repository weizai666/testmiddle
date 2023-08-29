/**
 * Copyright [2020] [LiBo/Alex of copyright liboware@gmail.com ]
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
package com.hyts.assemble.scheduler.hook;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @project-name:assemble-platform
 * @package-name:com.hyts.assemble.console.hook
 * @author:LiBo/Alex
 * @create-date:2022-06-27 17:45
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description: 容器完全准备好了，但是bean definition还没有加载 refresh() --- 具体的注入bean
 * 应用准备完成，开始定义bean，但还没有刷新到上下文
 */
@Component
@Slf4j
public class ApplicationPreparedListener implements ApplicationListener<ApplicationPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationPreparedEvent applicationPreparedEvent) {
        log.info("ApplicationPreparedListener object is to be call -" +
                " 容器完全准备好了，但是bean definition还没有加载 refresh() --- 具体的注入bean:{}",applicationPreparedEvent.getSource());
    }
}
