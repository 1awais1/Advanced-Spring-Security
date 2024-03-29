package com.example.springsecurity.demo.Security;
import com.example.springsecurity.demo.JWT.JwtConfig;
import com.example.springsecurity.demo.JWT.JwtTokenVerifier;
import com.example.springsecurity.demo.JWT.JwtUsernamePasswordAuthenticationFilter;
import com.example.springsecurity.demo.auth.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.crypto.SecretKey;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig  {
private final PasswordEncoder passwordEncoder;
private final ApplicationUserService applicationUserService;

private final JwtConfig jwtConfig;
private final SecretKey secretKey;

@Autowired
public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, ApplicationUserService applicationUserService, JwtConfig jwtConfig, SecretKey secretKey){
    this.passwordEncoder=passwordEncoder;

    this.applicationUserService = applicationUserService;
    this.jwtConfig = jwtConfig;
    this.secretKey = secretKey;
}



    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return new ProviderManager(Collections.singletonList(daoAuthenticationProvider()));
    }

    public JwtUsernamePasswordAuthenticationFilter jwtFilter() throws Exception {
        JwtUsernamePasswordAuthenticationFilter filter = new JwtUsernamePasswordAuthenticationFilter(authenticationManager(), jwtConfig, secretKey);
        // Configure other properties of your filter if needed
        return filter;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                /*.csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))*/
                .csrf(x -> x.disable())//to disable it*/
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new JwtTokenVerifier(secretKey,jwtConfig), JwtUsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(x ->
                        x.requestMatchers(
                                        ("/"),
                                        ("/index"),
                                        ("/public/**"),
                                        ("/css/**"),
                                        ("/js/**")

                                )

                                .permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/**"))
                                .hasRole(ApplicationUserRole.STUDENT.name())
                                .requestMatchers(HttpMethod.GET, "/management/api/**").hasAnyAuthority(ApplicationUserPermission.COURSE_READ.getPermission())
                                .requestMatchers(HttpMethod.DELETE, "/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())
                                .requestMatchers(HttpMethod.POST, "/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())
                                .requestMatchers(HttpMethod.PUT, "/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())  //these user permissions are order based
                                .requestMatchers(HttpMethod.GET, ("/management/api/**")).hasAnyRole(ApplicationUserRole.ADMIN.name(), ApplicationUserRole.ADMIN_TRAINEE.name())


                                .anyRequest()
                                .authenticated()

                )//.httpBasic(Customizer.withDefaults())// for http basic auth

            /*          THIS IS CODE FOR FORM BASED LOGIN                 .formLogin
                        (login->login.loginPage("/login") //form based authentication
                        .permitAll()
                                .defaultSuccessUrl("/courses",true)
                                //default to 2 weeks (somethingverysecure replace default provided by spring security )
                        ).rememberMe(remember -> remember.tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21)).key("somethingverysecured"))
                .logout(logout->logout.logoutUrl("/logout")
                        .logoutRequestMatcher( new AntPathRequestMatcher("/logout","GET")) //if csrf is enabled then logout should be a POST method
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID","remember-me")
                        .logoutSuccessUrl("/login"))
                        .authenticationProvider(daoAuthenticationProvider())*/
        ;


        return httpSecurity.build();


    }



//  @Bean
//    protected InMemoryUserDetailsManager inMemoryUserDetailsManager(){
//      UserDetails myUser = User.builder()
//              .username("mushii")
//              .password(passwordEncoder.encode("password"))
//              //.roles(ApplicationUserRole.STUDENT.name())
//              .authorities(ApplicationUserRole.STUDENT.getGrantedAuthorities())
//              .build();
//
//
//      UserDetails adminUser = User.builder().
//              username("MianSaab")
//              .password(passwordEncoder.encode("password"))
//              .authorities(ApplicationUserRole.ADMIN.getGrantedAuthorities())
//             // .roles(ApplicationUserRole.ADMIN.name())
//              .build();
//
//      UserDetails adminUserTrainee = User.builder().
//              username("Zain")
//              .password(passwordEncoder.encode("password"))
//              //.roles(ApplicationUserRole.ADMIN_TRAINEE.name())// role admin trainee
//              .authorities(ApplicationUserRole.ADMIN_TRAINEE.getGrantedAuthorities())
//              .build();
//
//      return new InMemoryUserDetailsManager(
//              myUser,
//              adminUser,
//              adminUserTrainee);
//    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(passwordEncoder);
    provider.setUserDetailsService(applicationUserService);
    return  provider;
    }
}
