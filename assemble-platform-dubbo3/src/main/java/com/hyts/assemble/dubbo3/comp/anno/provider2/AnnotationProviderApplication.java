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
package com.hyts.assemble.dubbo3.comp.anno.provider2;


import com.hyts.assemble.dubbo3.comp.anno.consumer.AnnotationConsumerBootstrap;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.CountDownLatch;

/**
 * @project-name:assemble-platform
 * @package-name:com.hyts.assemble.dubbo3.comp.anno
 * @author:LiBo/Alex
 * @create-date:2022-06-06 22:30
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
public class AnnotationProviderApplication {


    public static void main(String[] args) {
        System.setProperty("server.port","8803");
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AnnotationProviderBootstrap.class);
        context.start();
        System.out.println("dubbo service started.");
        try {
            new CountDownLatch(1).await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
