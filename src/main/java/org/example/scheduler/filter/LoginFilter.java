package org.example.scheduler.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

/**
 * 로그인 여부에 따라 접근을 제어하는 필터
 * - 화이트리스트 및 세션에 userId가 존재하면 통과
 * - 실패 시 401 반환
 */
public class LoginFilter implements Filter {
    private static final String[] WHITE_LIST = {"/", "/signup", "/login", "/logout"};

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        String requestURI = httpRequest.getRequestURI();

        if(isWhiteList(requestURI)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        HttpSession httpSession = httpRequest.getSession(false);
        Long userId = (httpSession == null) ? null : (Long) httpSession.getAttribute("userId");

        if(userId == null){
            if(httpSession != null){
                httpSession.invalidate();
            }
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.setContentType("application/json;charset=UTF-8");
            httpResponse.getWriter().write("{\"status\":401,\"message\":\"로그인 후 이용 가능합니다.\"}");
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean isWhiteList(String requestURI) {
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }
}
