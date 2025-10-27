package com.smartcare.cloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring Security配置类
 * 区分开发环境和生产环境的安全策略
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 开发环境安全配置 - 开放API以便测试
     */
    @Profile("dev")
    @Configuration
    public static class DevSecurityConfig extends WebSecurityConfigurerAdapter {
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
                    // 开发环境：开放主要业务接口以便前端测试
                    .antMatchers("/api/elderly/**", "/api/user/**", "/api/health-warning/**", "/api/doctor/**", "/api/reports/**").permitAll()
                    .antMatchers("/api/family/**").permitAll()
                    .antMatchers("/api/system/**").permitAll()
                    .antMatchers("/api/equipment/**").permitAll()
                    // 测试和管理接口（仅开发环境可访问）
                    .antMatchers("/test/**", "/api/init/**", "/admin/**", "/api/temp/**").permitAll()
                    // 其他接口需要认证
                    .anyRequest().authenticated();
        }
    }

    /**
     * 生产环境安全配置 - 严格权限控制
     */
    @Profile("prod")
    @Configuration
    public static class ProdSecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors().and().csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests()
                    // 认证接口允许匿名访问
                    .antMatchers("/api/auth/login", "/api/auth/register", "/api/auth/health").permitAll()
                    // 静态资源允许匿名访问
                    .antMatchers("/", "/static/**", "/*.html", "/*.css", "/*.js", "/*.png", "/*.jpg", "/*.jpeg", "/*.gif", "/*.ico").permitAll()
                    // Swagger文档（生产环境可选择关闭）
                    .antMatchers("/swagger-ui/**", "/v3/api-docs/**").hasRole("ADMIN")
                    // 健康检查接口
                    .antMatchers("/actuator/health").permitAll()
                    .antMatchers("/actuator/**").hasRole("ADMIN")
                    // 业务接口 - 需要认证
                    .antMatchers("/api/elderly/**").authenticated()
                    .antMatchers("/api/doctor/**").authenticated()
                    .antMatchers("/api/health-warning/**").authenticated()
                    .antMatchers("/api/family/**").authenticated()
                    .antMatchers("/api/equipment/**").authenticated()
                    .antMatchers("/api/reports/**").authenticated()
                    // 系统管理接口 - 仅管理员
                    .antMatchers("/api/system/**").hasRole("ADMIN")
                    // 禁止访问测试和管理接口
                    .antMatchers("/test/**", "/api/init/**", "/admin/**", "/api/temp/**").denyAll()
                    // 其他所有接口需要认证
                    .anyRequest().authenticated();
        }
    }
}
