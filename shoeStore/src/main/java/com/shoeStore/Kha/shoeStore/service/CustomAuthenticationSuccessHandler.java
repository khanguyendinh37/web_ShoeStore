package com.shoeStore.Kha.shoeStore.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String targetUrl = "/login?success=false"; // URL mặc định khi đăng nhập thất bại

        for (GrantedAuthority authority : authorities) {
            System.out.println(authority.getAuthority());
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                targetUrl = "/admin/viewProduct"; // Phân quyền cho vai trò "ADMIN"
                break;
            } else if (authority.getAuthority().equals("ROLE_CUSTOMER")) {
                targetUrl = "/home"; // Phân quyền cho vai trò "CUSTOMER"
                break;
            }
        }
        response.sendRedirect(targetUrl);
    }
}
