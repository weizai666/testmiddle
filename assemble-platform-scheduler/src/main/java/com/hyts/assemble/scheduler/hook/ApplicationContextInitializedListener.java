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
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @project-name:assemble-platform
 * @package-name:com.hyts.assemble.console.hook
 * @author:LiBo/Alex
 * @create-date:2022-06-27 17:43
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:  context上下文本身创建和初始化好了，ApplicationContextInitializer都已经执行了,上下文初始化完成
 */
@Component
@Slf4j
public class ApplicationContextInitializedListener implements ApplicationListener<ApplicationContextInitializedEvent> {


    @Override
    public void onApplicationEvent(ApplicationContextInitializedEvent applicationContextInitializedEvent) {
        log.info("ApplicationContextInitializedListener object is to be call -" +
                " context上下文本身创建和初始化好了，ApplicationContextInitializer都已经执行了:{}",
                applicationContextInitializedEvent.getSource());
    }
}
