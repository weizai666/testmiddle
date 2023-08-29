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
package com.hyts.assemble.uuidkey.web;

import com.hyts.assemble.common.model.http.ResultResponse;
import com.hyts.assemble.uuidkey.base.UUIDGenerator;
import com.hyts.assemble.uuidkey.hutool.HutoolUUIDGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

/**
 * @project-name:assemble-platform
 * @package-name:com.hyts.assemble.uuidkey.web
 * @author:LiBo/Alex
 * @create-date:2022-05-25 20:52
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@Slf4j
@RestController
@Api(value="唯一id生成器",tags = {"唯一ID生成器组件"},description = "包含百度、糊涂、雪花三种")
@RequestMapping("/api/uuid")
public class UuidKeyController {


    @Autowired
    Map<String, UUIDGenerator> uuidGeneratorMap;


    private static final UUIDGenerator UUID_GENERATOR = new HutoolUUIDGenerator();

    /**
     * 创建key
     * @param uuidType
     * @return
     */
    @GetMapping("/key")
    @ApiOperation(value="唯一id生成器",notes = "包含百度、糊涂、雪花三种")
    public ResultResponse keyGenerator(@RequestParam("uuidType") String uuidType){
        try {
            UUIDGenerator uuidGenerator = uuidGeneratorMap.getOrDefault(uuidType,UUID_GENERATOR);
            return ResultResponse.success(Optional.ofNullable(uuidGenerator).orElse(UUID_GENERATOR).nextId());
        }catch (Exception e){
            log.error("keyGenerator is failure!",e);
            return ResultResponse.failure("keyGenerator is failure!");
        }
    }


}
