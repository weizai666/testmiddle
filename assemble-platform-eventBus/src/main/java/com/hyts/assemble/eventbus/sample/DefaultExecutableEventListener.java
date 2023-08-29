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
package com.hyts.assemble.eventbus.sample;

import com.hyts.assemble.eventbus.ExecutableEventListener;
import com.hyts.assemble.eventbus.disruptor.EventModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * @project-name:assemble
 * @package-name:com.hyts.assemble.eventbus.sample
 * @author:LiBo/Alex
 * @create-date:2022-05-24 21:40
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@Slf4j
@Component
public class DefaultExecutableEventListener extends ExecutableEventListener {

    @Override
    public void handle(EventModel message) {
        log.info("{}",message);
    }

    @Override
    public String topic() {
        return "default";
    }
}
