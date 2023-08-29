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
package com.hyts.assemble.zookeeper.command;

import com.hyts.assemble.zookeeper.EmbeddedZooKeeper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @project-name:assemble-platform
 * @package-name:com.hyts.assemble.zookeeper.command
 * @author:LiBo/Alex
 * @create-date:2022-06-06 21:42
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@Slf4j
@Component
public class ZookeeperBootStrap implements CommandLineRunner {


    @Value("${zookeeper.port:2181}")
    Integer zookeeperPort;


    public static void run(Integer zookeeperPort){
        new EmbeddedZooKeeper(Integer.parseInt(zookeeperPort+""),false).start();
    }

    @Override
    public void run(String... args) throws Exception {
        if(args.length > 0){
            String portStr = StringUtils.split(String.valueOf(args[0]),"=")[1];
            zookeeperPort = Integer.valueOf(portStr);
        }
        ZookeeperBootStrap.run(zookeeperPort);
        log.info("-------------zookeeper server is started:{}-------------",zookeeperPort);
    }
}
