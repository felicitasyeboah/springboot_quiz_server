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
 * The class checks every request for a valid JW-token.
 * A valid token leads to the login of the user and the execution of the request.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = getJwtFromRequest(request);

        //Checks the token for availability and validity
        if(StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {

            //Extracts the username out of the token
            String userName = jwtTokenProvider.getUserNameFromToken(jwt);

            //Load the user details
            UserDetails userDetails =  customUserDetailsService.loadUserByUsername(userName);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            authentication.setDetails((new WebAuthenticationDetailsSource().buildDetails(request)));

            // geben dem Securitycontext von Spring mit, dass wir einen Authentifizeirten Benutzer haben
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {

        //Searches for the "Autorization"-string in the request header
        String bearerToken = request.getHeader("Authorization");

        //Checks the data of "Authorization" for availability and the beginning string "Bearer"
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {

            //Returns the token without the beginning-string
            return bearerToken.substring(7);
        }

        //No token in the request
        return null;
    }
}