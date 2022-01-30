package de.semesterprojekt.quiz.config;

import de.semesterprojekt.quiz.security.jwt.JwtAuthenticationEntryPoint;
import de.semesterprojekt.quiz.security.jwt.JwtAuthenticationFilter;
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

                //allows users to upload images
                /*
                .and()
                .authorizeRequests()
                .antMatchers("/files/**") // /upload
                .permitAll()
                */

                //allows users to view the profile images without token
                //TODO: KANN WEG
                .and()
                .authorizeRequests()
                .antMatchers("/profileImage/**") // /get profile image by username
                .permitAll()

                //TODO: Allow use of websockets without authentication
                .and()
                .authorizeRequests()
                .antMatchers("/app", "/topic","/websocket","/topic/user","/app/user","/websocket/**","/topic/**","/app/**") // /Websockets
                .permitAll()

                //TODO: KANN WEG
                /*
                .and()
                .authorizeRequests()
                .antMatchers("/app","/ws","/hello","/topic","/websocket","/resources/**","/topic/user","/app/user","/websocket/**","/topic/**","/app/**","/webjars/**","/app.js") // /Websockets
                .permitAll()
                */


                //TODO: KANN WEG
                /*
                .and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .requestMatchers(PathRequest
                        .toStaticResources()
                        .atCommonLocations())
                .permitAll()
                */



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