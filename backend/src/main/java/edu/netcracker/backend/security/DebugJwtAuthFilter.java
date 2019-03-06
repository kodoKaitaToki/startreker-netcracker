package edu.netcracker.backend.security;

import edu.netcracker.backend.service.UserService;
import edu.netcracker.backend.utils.AuthorityUtils;
import edu.netcracker.backend.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@Qualifier("DebugAuthFilter")
@Profile("debug_su")
@Primary // override default production auth filter
public class DebugJwtAuthFilter extends AuthFilter {

    private final JwtAuthFilter authFilter;
    private final UserService userService;

    @Autowired
    public DebugJwtAuthFilter(@Qualifier("ProductionAuthFilter") JwtAuthFilter authFilter, UserService userService) {
        this.authFilter = authFilter;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if(JwtUtils.getAccessToken(request) != null){
            authFilter.doFilter(request, response, filterChain);
            return;
        }

        String debugLogin = request.getHeader("Authorization");

        if (debugLogin != null && debugLogin.startsWith("debug_login ")) {
            long userId = Long.parseLong(debugLogin.substring(12));
            UserDetails userDetails = userService.find(userId);
            authFilter.createAuthentication(userDetails, request);
            logger.info("DEBUG_LOGIN: " + userId);
            filterChain.doFilter(request, response);
        } else {
            authFilter.createAuthentication(AuthorityUtils.DEBUG_SUPERUSER, request);
            logger.info("DEBUG_LOGIN: SU");
            filterChain.doFilter(request, response);
        }

    }
}
