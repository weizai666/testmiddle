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
package com.hyts.assemble.dubbo3.comp.anno.consumer;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.stream.IntStream;

/**
 * @project-name:assemble-platform
 * @package-name:com.hyts.assemble.dubbo3.comp.anno
 * @author:LiBo/Alex
 * @create-date:2022-06-06 22:30
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
public class AnnotationConsumerApplication {



    public static void main(String[] args) {
        System.setProperty("server.port","8802");
        System.setProperty("user.dir",".");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AnnotationConsumerBootstrap.class);
        AnnotationProcessAction annotationProcessAction = context.getBean("annotationAction",AnnotationProcessAction.class);
        IntStream.range(0,10).forEach(param->{
            annotationProcessAction.multriVersionProcess("test");
        });
        context.start();
    }

}
