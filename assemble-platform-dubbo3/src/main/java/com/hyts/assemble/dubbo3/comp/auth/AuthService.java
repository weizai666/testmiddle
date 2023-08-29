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
package com.hyts.assemble.dubbo3.comp.auth;

import cn.hutool.crypto.SecureUtil;
import com.hyts.assemble.common.util.StringUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @project-name:middleware
 * @package-name:com.hyts.assemble.dubbo3.comp.auth
 * @author:LiBo/Alex
 * @create-date:2022-11-06 19:01
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@Data
@Slf4j
@NoArgsConstructor
public class AuthService {


    public  final String AUTH_SECRET_KEY = "dubbo.secret.key";

    public  final String AUTH_APP_KEY = "dubbo.app.key";

    private String appKey;

    private String secretKey;

    /**
     * 匹配ak和sk的值
     * @param appKey
     * @param secretKey
     * @return
     */
    public boolean matchSecretKey(String appKey,String secretKey){
        log.info("local-appkey:{} - local-secretKey:{}",this.appKey,this.secretKey);
        log.info("remote-appkey:{} - remote-secretKey:{}",appKey,secretKey);
        if(StringUtils.isEmpty(appKey)){
            return Boolean.FALSE;
        }
        if(this.appKey.equals(appKey)){
            if(StringUtils.isEmpty(secretKey)){
                return Boolean.FALSE;
            }
            else if(!SecureUtil.md5(this.secretKey).equals(secretKey)){
                return Boolean.FALSE;
            }
        }else{
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

}
