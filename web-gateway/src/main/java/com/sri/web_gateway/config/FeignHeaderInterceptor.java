package com.sri.web_gateway.config;

import com.sri.web_gateway.filter.LoggingFilter;
import com.sri.web_gateway.security.JwtAuthenticationToken;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class FeignHeaderInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {

        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            Object requestId = request.getAttribute(LoggingFilter.REQUEST_ID_ATTRIBUTE);
            if (requestId != null) {
                template.header(LoggingFilter.REQUEST_ID_HEADER, requestId.toString());
            }
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwt && jwt.isAuthenticated()) {
            template.header("X-User-Email", jwt.getEmail());
            template.header("X-User-Id", jwt.getUserId().toString());
            template.header("X-User-Role", jwt.getRole());
        }
    }
}
