package com.example.matcha.security;

import com.example.matcha.entity.User;
import com.example.matcha.services.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static org.apache.commons.lang3.stream.Streams.stream;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

    public static final Logger LOG = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    @Transactional
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            if (request.getServletPath().equals("/api/auth/signup")  || request.getServletPath().equals("/api/auth/signin") || request.getServletPath().equals("/api/auth/token/refresh") || request.getServletPath().equals("/api/auth/logout")) {
                filterChain.doFilter(request, response);
            } else {
                String jwt = getJWTFromRequest(request);
                if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt, "ACCESS")) {
                    Long userId = jwtTokenProvider.getUserIdFromToken(jwt);
                    User userDetails = customUserDetailsService.loadUserById(userId);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    stream(userDetails.getRoles()).forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, authorities
                    );

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    filterChain.doFilter(request, response);
                } else {
                    HttpServletResponse httpResponse = (HttpServletResponse) response;
                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");

                }

            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            LOG.error("Could not set user authentication");
            LOG.error(e.getMessage());
        }
    }

    public String getJWTFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(SecurityConstants.HEADER_STRING);
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            return authorizationHeader.split(" ")[1];
        }
        return null;
    }
}
