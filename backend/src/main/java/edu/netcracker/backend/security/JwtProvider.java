package edu.netcracker.backend.security;


import edu.netcracker.backend.service.impl.UserInformationHolderService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
@PropertySource("classpath:jwt.properties")
public class JwtProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    public enum TokenType {
        ACCESS_TOKEN,
        REGISTRATION_TOKEN,
        REFRESH_TOKEN
    }

    @Autowired
    private UserInformationHolderService userInformationHolderService;

    @Value("${jwt.jwtSecret}")
    private String jwtSecret;

    @Value("${jwt.jwtAuthenticationExpiration}")
    private int jwtAuthenticationExpiration;

    @Value("${jwt.jwtMailRegistrationExpiration}")
    private int jwtMailRegistrationExpiration;

    @Value("${jwt.jwtRefreshExpiration}")
    private int jwtRefreshExpiration;

    public String generateAccessToken(UserDetails userPrincipal) {
        String userInformationHolder = userInformationHolderService.convertAsString(userPrincipal);

        if (userInformationHolder.equals("")) {
            throw new RuntimeException("Something went wrong");
        }

        return generateToken(userInformationHolder, jwtAuthenticationExpiration, TokenType.ACCESS_TOKEN);
    }

    public String generateRefreshToken(String username) {
        return generateToken(username, jwtMailRegistrationExpiration, TokenType.REFRESH_TOKEN);
    }

    public String generateMailRegistrationToken(String username) {
        return generateToken(username, jwtMailRegistrationExpiration, TokenType.REGISTRATION_TOKEN);
    }

    public String retrieveSubject(String jwt) {
        return retrieveClaimsFromToken(jwt).getSubject();
    }

    public String retrieveTokenType(String jwt) {
        return retrieveClaimsFromToken(jwt).getIssuer();
    }

    public boolean validateToken(String jwt) {
        return validateInputToken(jwt, null);
    }

    public boolean validateToken(String jwt, HttpServletRequest request) {
        return validateInputToken(jwt, request);
    }

    private boolean validateInputToken(String jwt, HttpServletRequest request) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt);
            return true;
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token -> Message: {}", e.getMessage());
            if (request != null) request.setAttribute("isExpired", true);
        } catch (Exception e) {
            logger.error("Invalid JWT -> Message: {} " + e.getMessage());
        }

        return false;
    }

    private String generateToken(String subject, int expiration, TokenType tokenType) {
        return Jwts.builder().
                setSubject(subject).
                setIssuedAt(new Date()).
                setIssuer(tokenType.toString()).
                setExpiration(new Date((new Date()).getTime() + expiration)).
                signWith(SignatureAlgorithm.HS512, jwtSecret).
                compact();
    }

    private Claims retrieveClaimsFromToken(String jwt) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(jwt)
                .getBody();
    }

    public boolean isAccessToken(String token) {
        return ifTokenMatchType(token, TokenType.ACCESS_TOKEN);
    }

    public boolean isRefreshToken(String token) {
        return ifTokenMatchType(token, TokenType.REFRESH_TOKEN);
    }

    public boolean isRegistrationToken(String token) {
        return ifTokenMatchType(token, TokenType.REGISTRATION_TOKEN);
    }

    private boolean ifTokenMatchType(String token, TokenType tokenType) {
        return retrieveTokenType(token).equals(tokenType.name());
    }
}
