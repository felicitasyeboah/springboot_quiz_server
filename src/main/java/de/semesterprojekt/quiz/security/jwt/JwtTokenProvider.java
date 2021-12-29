package de.semesterprojekt.quiz.security.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    /**
     * Generates a JW-token with generation and expiration date based on the username.
     *
     * @param userName username
     * @return JW-token
     */
    public String generateToken(String userName) {

        //Generates a generation date
        Instant now = Instant.now();

        //Generates an expiration date (7 days in the future)
        Instant expiration = now.plus(2, ChronoUnit.DAYS);

        //Returns the JW-Token (algorithm: HS512)
        return Jwts.builder()
                .setSubject(userName)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    /**
     * Generates a JW-token with generation and expiration date based on the authentication.
     * @param authentication authentication
     * @return JW-token
     */
    public String generateToken(Authentication authentication) {

        //Extracts user from authentication
        User user = (User) authentication.getPrincipal();

        //Returns the JW-Token
        return generateToken(user.getUsername());
    }

    /**
     * Returns the username based on the JW-token.
     * @param token JW-token
     * @return username
     */
    public String getUserNameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    /**
     * Validates the JW-token.
     *
     * @param token JW-token
     * @return boolean
     */
    public boolean validateToken(String token) {

        //Validates the JW-token and catches all exceptions
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }
}