package edu.netcracker.backend.security.jwt;

import edu.netcracker.backend.service.impl.UserInformationHolderServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@PropertySource("classpath:jwt.properties")
public class JwtProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Autowired
    private UserInformationHolderServiceImpl userInformationHolderService;

    @Value("${jwt.jwtSecret}")
    private String jwtSecret;

    @Value("${jwt.jwtAuthenticationExpiration}")
    private int jwtAuthenticationExpiration;

    @Value("${jwt.jwtMailRegistrationExpiration}")
    private int jwtMailRegistrationExpiration;

    @Value("${jwt.jwtRefreshExpiration}")
    private int jwtRefreshExpiration;

    public String generateAuthenticationToken(Authentication authentication) {
        return generateTokenFromAuthentication(authentication, jwtAuthenticationExpiration);
    }

    public String generateRefreshToken(Authentication authentication) {
        return generateTokenFromAuthentication(authentication, jwtRefreshExpiration);
    }

    public String generateMailRegistrationToken(String username) {
        return generateToken(username, jwtMailRegistrationExpiration);
    }

    public String retrieveSubject(String jwt) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(jwt)
                .getBody().getSubject();
    }

    public boolean validateToken(String jwt) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt);
            return true;
        } catch (Exception e) {
            logger.error("Invalid JWT -> Message: {} " + e);
        }

        return false;
    }

    private String generateTokenFromAuthentication(Authentication authentication, int expiration) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        String userInformationHolder = userInformationHolderService.convertAsString(userPrincipal);

        if (userInformationHolder.equals("")) {
            throw new RuntimeException("Something went wrong");
        }

        return generateToken(userInformationHolder, expiration);
    }

    private String generateToken(String subject, int expiration) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + expiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
}
