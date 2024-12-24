package com.travelagency.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String path = request.getRequestURI();

        // Пропускаем публичные URL
        if (isPublicUrl(path)) {
            return true;
        }

        HttpSession session = request.getSession();
        String userRole = (String) session.getAttribute("userRole");

        System.out.println("Current path: " + path); // Для отладки
        System.out.println("User role from session: " + userRole); // Для отладки

        // Проверяем доступ к админке
        if (path.startsWith("/admin")) {
            if (!"ADMIN".equals(userRole)) {
                System.out.println("Access denied to admin area"); // Для отладки
                response.sendRedirect("/login");
                return false;
            }
        }

        // Проверяем аутентификацию для остальных URL
        if (session.getAttribute("userId") == null) {
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }

    private boolean isPublicUrl(String path) {
        return path.equals("/") ||
                path.equals("/login") ||
                path.equals("/register") ||
                path.startsWith("/css/") ||
                path.startsWith("/js/") ||
                path.startsWith("/images/");
    }
}