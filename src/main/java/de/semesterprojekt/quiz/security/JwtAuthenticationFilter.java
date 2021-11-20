package de.semesterprojekt.quiz.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Uebreprueft jeden Request auf enthaltenden JWT, ob dieser gueltig ist, wenn er gueltig ist,
 * dann wird User angemeledet und sein Request ausgefuehrt
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = getJwtFromRequest(request);

        // pruefen ob jwt vorhanden und gueltig
        if(StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {

            String userName = jwtTokenProvider.getUserNameFromToken(jwt);
            UserDetails userDetails =  customUserDetailsService.loadUserByUsername(userName);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            authentication.setDetails((new WebAuthenticationDetailsSource().buildDetails(request)));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
