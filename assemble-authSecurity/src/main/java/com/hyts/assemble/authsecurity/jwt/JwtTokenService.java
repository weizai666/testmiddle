package com.hyts.assemble.authsecurity.jwt;

import com.alibaba.fastjson.JSON;
import com.hyts.assemble.authsecurity.consts.JwtKeyProperties;
import com.hyts.assemble.authsecurity.domain.SystemUserSubject;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * JWT工具类
 * @Author Sans
 * @CreateTime 2019/10/2 7:42
 */
@Slf4j
@Component
public class JwtTokenService {


    private JwtConfigProperties jwtConfigProperties;



    public JwtTokenService(JwtConfigProperties jwtConfigProperties){
           this.jwtConfigProperties = jwtConfigProperties;
    }

    /**
     * 生成Token
     * @Author Sans
     * @CreateTime 2019/10/2 12:16
     * @Param  selfUserEntity 用户安全实体
     * @Return Token
     */
    public String createAccessToken(SystemUserSubject selfUserEntity){
        // 登陆成功生成JWT
        String token = Jwts.builder()
                // 放入用户名和用户ID
                .setId(String.valueOf(selfUserEntity.getUserId()))
                // 主题
                .setSubject(selfUserEntity.getUsername())
                // 签发时间
                .setIssuedAt(new Date())
                // 签发者
                .setIssuer(JwtKeyProperties.DEFAULT_ISSUE_NAME)
                // 自定义属性 放入用户拥有权限
                .claim(JwtKeyProperties.DEFAULT_PERMISSION_RESOURCE,
                        JSON.toJSONString(selfUserEntity.getAuthorities()))
                // 失效时间
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfigProperties.getExpiration()))
                // 签名算法和密钥
                .signWith(SignatureAlgorithm.HS512, jwtConfigProperties.getSecret())
                .compact();
        return token;
    }

    /**
     * 解析token
     * @param jwtStr
     * @return
     */
    public Jwt parseAccessToken(String jwtStr){
        // 登陆成功生成JWT
       return Jwts.parser().setSigningKey(jwtConfigProperties.getSecret()).parse(jwtStr);
    }
}