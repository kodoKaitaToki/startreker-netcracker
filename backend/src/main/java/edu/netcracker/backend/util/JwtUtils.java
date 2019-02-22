package edu.netcracker.backend.util;

import javax.servlet.http.HttpServletRequest;

public class JwtUtils {

    public static String getAccessToken(HttpServletRequest request) {
        return getToken(request, "Authorization");
    }

    public static String getRefreshToken(HttpServletRequest request) {
        return getToken(request, "Authorization-Refresh");
    }

    private static String getToken(HttpServletRequest request, String header) {
        String authHeader = request.getHeader(header);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.replace("Bearer ", "");
        }

        return null;
    }
}
