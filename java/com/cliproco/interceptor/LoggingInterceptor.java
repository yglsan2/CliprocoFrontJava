package com.cliproco.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoggingInterceptor implements HandlerInterceptor {
    
    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        logger.info("Requête reçue - URI: {}, Méthode: {}, IP: {}, User-Agent: {}", 
                   requestURI, method, ipAddress, userAgent);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String requestURI = request.getRequestURI();
        int status = response.getStatus();

        if (ex != null) {
            logger.error("Erreur lors du traitement de la requête - URI: {}, Status: {}, Exception: {}", 
                        requestURI, status, ex.getMessage(), ex);
        } else {
            logger.info("Requête terminée - URI: {}, Status: {}", requestURI, status);
        }
    }
} 