package com.mds.auth.config.security;


import com.mds.auth.config.security.handler.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author sopp
 * @version 1.0
 * @date 2021/9/7 15:59
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    LoginFailureHandler loginFailureHandler;

    @Autowired
    LoginSuccessHandler loginSuccessHandler;

    @Autowired
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Autowired
    UserDetailServiceImpl userDetailService;

    @Autowired
    private MyPasswordEncoder myPasswordEncoder;

    @Value("${security-url}")
    private String[] securityUrl;

    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
        return jwtAuthenticationFilter;
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * ??????:
     * http????????? Spring Security ?????????????????????????????????????????????????????????web??????????????? Spring Security ???????????????
     * ??????http???????????????????????????????????????web??????????????????????????????
     **/
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()

                // ????????????
                .httpBasic()
//                .successHandler(loginSuccessHandler)
//                .failureHandler(loginFailureHandler)

                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/logout")
//                .logoutSuccessHandler(jwtLogoutSuccessHandler)

                // ??????session
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // ??????????????????
                .and()
                .authorizeRequests()
                .antMatchers(securityUrl).permitAll()
                .anyRequest().authenticated()

                // ???????????????
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // ???????????????????????????
                .and()
                .addFilter(jwtAuthenticationFilter())
//                .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class)

        ;


    }


    /**
     * ???????????????????????????????????????????????????????????????
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // ??????????????? ?????????????????????
        provider.setHideUserNotFoundExceptions(true);
        // ????????????service - ????????????????????????
        provider.setUserDetailsService(userDetailsService());
        // ????????????????????????
        provider.setPasswordEncoder(myPasswordEncoder);
        auth.authenticationProvider(provider);
        auth.userDetailsService(userDetailService);
    }


    /**
     * ??????: ?????????????????? BCrypt ????????????
     **/
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new MyPasswordEncoder();
//    }
}
