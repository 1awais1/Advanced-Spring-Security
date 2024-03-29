package com.example.springsecurity.demo.JWT;

import com.google.common.net.HttpHeaders;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.crypto.SecretKey;

@ConfigurationProperties(prefix="application.jwt")
public class JwtConfig {


    private String secretKey;

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    public Integer getTokenExpirationDays() {
        return tokenExpirationDays;
    }

    public void setTokenExpirationDays(Integer tokenExpirationDays) {
        this.tokenExpirationDays = tokenExpirationDays;
    }

    private String tokenPrefix;
    private Integer tokenExpirationDays;



    public JwtConfig(){

    }
    public JwtConfig(String secretKey, String tokenPrefix, Integer tokenExpirationDays) {
        this.secretKey = secretKey;
        this.tokenPrefix = tokenPrefix;
        this.tokenExpirationDays = tokenExpirationDays;
    }


    public String getAuthorizationHeader(){
        return HttpHeaders.AUTHORIZATION;
    }
}
