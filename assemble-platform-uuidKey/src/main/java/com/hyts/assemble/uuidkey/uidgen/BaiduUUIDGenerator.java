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
package com.hyts.assemble.uuidkey.uidgen;

import com.baidu.fsg.uid.UidGenerator;
import com.hyts.assemble.uuidkey.base.UUIDGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @project-name:callcenter
 * @package-name:com.hyts.callcenter.extense.idgen.uidgen
 * @author:LiBo/Alex
 * @create-date:2021-12-10 10:33 下午
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */

@Slf4j
//@ConditionalOnExpression("'${uuidKey.type}'.equalsIgnoreCase('uidgen')")
@Service("baidu")
public class BaiduUUIDGenerator implements UUIDGenerator<Long> {


    @Resource
    private UidGenerator uidGenerator;


    @Override
    public Long nextId() {
        Long id = uidGenerator.getUID();
        log.info("生成：{}",id);
        return id;
    }


}
