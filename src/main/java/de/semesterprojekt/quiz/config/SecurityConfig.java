package de.semesterprojekt.quiz.config;

import de.semesterprojekt.quiz.security.JwtAuthenticationEntryPoint;
import de.semesterprojekt.quiz.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

/* Resolves CORS-Problem in Chrome
    Source: https://stackoverflow.com/questions/44697883/can-you-completely-disable-cors-support-in-spring*/

    @Component
    public class CorsFilter extends OncePerRequestFilter {

        @Override
        protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                        final FilterChain filterChain) throws ServletException, IOException {
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, PATCH, HEAD");
            response.addHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
            response.addHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin, Access-Control-Allow-Credentials");
            response.addHeader("Access-Control-Allow-Credentials", "true");
            response.addIntHeader("Access-Control-Max-Age", 10);
            filterChain.doFilter(request, response);
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement()

                //makes every session unique
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                //TODO: allows a user to view the api documentation without login
                .and()
                .authorizeRequests()
                .antMatchers("/swagger-ui/**") // /swagger-ui
                .permitAll()

                //TODO: Allow use of websockets without authentication
                .and()
                .authorizeRequests()
                .antMatchers("/app","/ws","/hello","/topic","/websocket","/resources/**","/topic/user","/app/user","/websocket/**","/topic/**","/app/**","/webjars/**","/app.js") // /Websockets
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .requestMatchers(PathRequest
                        .toStaticResources()
                        .atCommonLocations())
                .permitAll()


                //allows a user to register and login
                .and()
                .authorizeRequests()
                .antMatchers("/auth/**") // /auth/register, /auth/login
                .permitAll()
                .anyRequest()
                .authenticated();

        http.addFilterBefore(
                jwtAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class
        );

    }
}
