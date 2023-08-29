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
package com.hyts.assemble.dynamicTask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @project-name:callcenter
 * @package-name:com.hyts.callcenter.schedule
 * @author:LiBo/Alex
 * @create-date:2022-05-02 17:05
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@SpringBootApplication
public class ScheduleApplication {

//    @XxlJob("demoJobHandler")
//    public void demoJobHandler() throws Exception {
//        XxlJobHelper.log("XXL-JOB, Hello World.");
//
//        for (int i = 0; i < 5; i++) {
//            XxlJobHelper.log("beat at:" + i);
//            TimeUnit.SECONDS.sleep(2);
//        }
//        // default success
//    }
    public static void main(String[] args) {
        SpringApplication.run(ScheduleApplication.class,args);
    }
}
