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

package com.hyts.assemble.dubbo3.comp.anno.consumer;

import com.hyts.assemble.dubbo3.comp.service.AnnotationBaseProcessService;
import org.apache.dubbo.common.constants.LoadbalanceRules;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

@Component("annotationAction")
public class AnnotationProcessAction {



    @DubboReference(version = "*",loadbalance = LoadbalanceRules.ROUND_ROBIN)
//            (interfaceClass = BaseProcessService.class, version = "*",
//                    timeout = 1000,
//                    group = "dubbo3",
//                    sticky = false,
//                    methods = {@Method(name = "process", timeout = 3000, retries = 1)}
//
//                    /*,
//
//                    methods = {
//                            @Method(
//                                    name = "sayHello",
//                                    oninvoke = "notify.oninvoke",
//                                    onreturn = "notify.onreturn",
//                                    onthrow = "notify.onthrow")
//                    }
//                     */
//            )
    private AnnotationBaseProcessService annotationBaseProcessService;



    public String multriVersionProcess(String name) {
        try {
            return (String) annotationBaseProcessService.process(name);
        } catch (Exception e) {
            e.printStackTrace();
            return "Throw Exception";
        }
    }
}
