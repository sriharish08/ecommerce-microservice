package com.sri.web_gateway.config;


import com.sri.web_gateway.security.JwtAuthenticationToken;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
public class FeignHeaderInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwt && jwt.isAuthenticated()) {
            template.header("X-User-Email", jwt.getEmail());
            template.header("X-User-Id", jwt.getUserId().toString());
            template.header("X-User-Role", jwt.getRole());
        }
    }
}
