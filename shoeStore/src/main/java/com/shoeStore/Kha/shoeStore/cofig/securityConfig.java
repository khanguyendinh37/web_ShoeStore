package com.shoeStore.Kha.shoeStore.cofig;

import com.shoeStore.Kha.shoeStore.service.AccountDetailService;
import com.shoeStore.Kha.shoeStore.service.CustomAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class securityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AccountDetailService accountDetailService;
    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountDetailService).passwordEncoder(passwordEncoder());
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        AccessDeniedHandler CustomExceptionHandler;
        http.authorizeRequests()
                .antMatchers("/login/**","/register/**","/loginStyle/**",
                        "/home/**",
                        "/product/**",
                        "/productCategory/**",
                        "/deleteCart/**",
                        "/User/**",
                        "/shoppingCart/**",
                        "/getImg/**",
                        "/add/**",
                        "/Sub/**","/cart/**","/News/**","/ViewNewsPage/**","/oderCart/**","/showSuccessPage/**",
                        "/billDetail/**","/About/**","/Contact/**").permitAll() // Cho phép truy cập "/shop" mà không cần xác thực
                .antMatchers("/admin/**","/adminStyle/**").hasRole("ADMIN")
                .antMatchers("/customer/**").hasRole("CUSTOMER")
                .anyRequest().authenticated() // Tất cả các URL khác yêu cầu xác thực
                .and()
                .formLogin()
                .loginPage("/login").permitAll() // Trang đăng nhập tùy chỉnh, không cần xác thực
                .successHandler(customAuthenticationSuccessHandler) // Sử dụng trình xử lý thành công tùy chỉnh
                .failureUrl("/login?success=false") // Điều hướng khi đăng nhập thất bại
                .loginProcessingUrl("/j_spring_security_check")
                .and()
                .logout().permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/login");
    }

}
