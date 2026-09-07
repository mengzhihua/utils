package com.mengzhihua.utils.config;

import com.mengzhihua.utils.util.TraceIdUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Propagates {@code X-Trace-Id} on every HTTP request.
 */
@Component
public class TraceIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        TraceIdUtil.set(request.getHeader(TraceIdUtil.HEADER));
        response.setHeader(TraceIdUtil.HEADER, TraceIdUtil.get());
        try {
            filterChain.doFilter(request, response);
        } finally {
            TraceIdUtil.clear();
        }
    }
}
