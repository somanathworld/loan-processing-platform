package com.gs.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class FeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();

            // ✅ Forward Authorization header (JWT)
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null) {
                template.header("Authorization", authHeader);
            }

            // ✅ Forward custom headers if needed
            String user = request.getHeader("X-User");
            String roles = request.getHeader("X-Roles");

            if (user != null) {
                template.header("X-User", user);
            }

            if (roles != null) {
                template.header("X-Roles", roles);
            }
        }
    }
}
