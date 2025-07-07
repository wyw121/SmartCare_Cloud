package com.smartcare.cloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring Security配置类
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 认证接口允许匿名访问
                .antMatchers("/api/auth/**").permitAll()
                // 静态资源允许匿名访问
                .antMatchers("/", "/static/**", "/*.html", "/*.css", "/*.js", "/*.png", "/*.jpg", "/*.jpeg", "/*.gif", "/*.ico").permitAll()
                // Swagger相关接口允许匿名访问
                .antMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()
                // 健康检查接口允许匿名访问
                .antMatchers("/actuator/**").permitAll()
                // 开发环境：暂时开放主要业务接口以便前端测试
                .antMatchers("/api/elderly/**", "/api/user/**", "/api/health-warning/**", "/api/doctor/**", "/api/reports/**").permitAll()
                .antMatchers("/test/**", "/api/init/**", "/api/admin/**", "/api/temp/**").permitAll()
                // 其他接口需要认证
                .anyRequest().authenticated();
    }

    /**
     * 密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
