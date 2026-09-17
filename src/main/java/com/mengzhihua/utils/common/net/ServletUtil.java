package com.mengzhihua.utils.common.net;


import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Current-request helpers based on {@link RequestContextHolder}.
 */
public final class ServletUtil {

    private ServletUtil() {
    }

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes == null ? null : attributes.getRequest();
    }

    public static String getHeader(String name) {
        HttpServletRequest request = getRequest();
        return request == null ? null : request.getHeader(name);
    }

    public static String getParameter(String name) {
        HttpServletRequest request = getRequest();
        return request == null ? null : request.getParameter(name);
    }

    public static boolean isAjax(HttpServletRequest request) {
        if (request == null) {
            return false;
        }
        String requestedWith = request.getHeader("X-Requested-With");
        String accept = request.getHeader("Accept");
        return "XMLHttpRequest".equalsIgnoreCase(requestedWith)
                || (accept != null && accept.contains(MediaTypes.JSON));
    }

    private static final class MediaTypes {
        private static final String JSON = "application/json";
    }
}
