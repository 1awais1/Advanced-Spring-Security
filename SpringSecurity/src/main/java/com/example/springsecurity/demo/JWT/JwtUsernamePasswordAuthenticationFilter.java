package com.example.springsecurity.demo.JWT;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Date;


public class JwtUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    //job of this class is to verify credentials


    private final AuthenticationManager authenticationManager;
    private  final JwtConfig jwtConfig;
    private final SecretKey secretKey;
    public JwtUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager, JwtConfig jwtConfig, SecretKey secretKey) {
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)throws AuthenticationException {

    try {

        InputStream inputStream = request.getInputStream();
        if (inputStream == null) {
            throw new RuntimeException("Request InputStream is null");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        UsernameAndPasswordAuthentication usernameAndPasswordAuthentication = objectMapper.readValue(
                inputStream, UsernameAndPasswordAuthentication.class);
        /*UsernameAndPasswordAuthentication usernameAndPasswordAuthentication= new ObjectMapper().readValue(
               request.getInputStream(),UsernameAndPasswordAuthentication.class);*/


        String password = obtainPassword(request);
        Authentication auth =  new UsernamePasswordAuthenticationToken(
                usernameAndPasswordAuthentication.getUsername(),
                usernameAndPasswordAuthentication.getPassword()
        );
      return  authenticationManager.authenticate(auth);

    } catch (IOException e) {
        System.out.println("Getting exception authenticating");
        throw new RuntimeException(e);
    }
}
    @Override
    protected void successfulAuthentication(jakarta.servlet.http.HttpServletRequest request,
                                            jakarta.servlet.http.HttpServletResponse response,
                                            jakarta.servlet.FilterChain chain,
                                            Authentication authResult)
            throws IOException,
            jakarta.servlet.ServletException {


        // Generate a fixed JWT token


        String JWTtoken = Jwts.builder().setSubject(authResult.getName()).
                claim("authorities", authResult.getAuthorities())
                .issuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationDays())))
                .signWith(secretKey)
                .compact();
        response.addHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix() + JWTtoken);


    }


}
