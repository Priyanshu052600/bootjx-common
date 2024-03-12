package com.amx.jax.limitter.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;

import com.boot.jx.exception.AmxApiError;
import com.boot.utils.JsonUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Utility class for servlet filters
 * 
 * @author prashant
 *
 */
public class FilterUtil {

    public static final AntPathMatcher ANT_MATCHER = new AntPathMatcher();

    public static boolean isExclude(String path, List<String> excludUrls) {
        boolean isExclude = false;
        if (CollectionUtils.isNotEmpty(excludUrls)) {
            isExclude = excludUrls.stream().anyMatch(i -> {
                return ANT_MATCHER.match(i, path);
            });
        }
        return isExclude;
    }

    public static void setErrorResponse(HttpServletRequest request, HttpServletResponse response, String messageKey,
            String message) throws IOException {
        String acceptHeader = request.getHeader("Accept");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        AmxApiError amxApiError = new AmxApiError(messageKey, message);
        amxApiError.setMessageKey(messageKey);
        PrintWriter out = response.getWriter();
        if (MediaType.APPLICATION_JSON_VALUE.equals(acceptHeader)) {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            out.print(JsonUtil.toJson(amxApiError));
        } else {
            response.sendError(HttpStatus.FORBIDDEN.value(), message);
        }
        request.setAttribute("amxApiError", amxApiError);
        
    }
}
