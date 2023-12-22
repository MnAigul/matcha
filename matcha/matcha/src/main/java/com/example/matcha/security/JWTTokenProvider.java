package com.example.matcha.security;


import com.example.matcha.entity.Role;
import com.example.matcha.entity.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class JWTTokenProvider {

    public static final Logger LOG = LoggerFactory.getLogger(JWTTokenProvider.class);

    public String generateToken(User user, long expiresTime, String tokenType) {  //User
        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(now.getTime() + expiresTime);
        String userId = Long.toString(user.getId());

        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("id", userId);
        claimsMap.put("username", user.getEmail());
        claimsMap.put("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
        claimsMap.put("token_type", tokenType);

        String token = Jwts.builder()
                .setSubject(userId)
                .addClaims(claimsMap)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, SecurityConstants.SECRET)
                .compact();
        return token;
    }

    public boolean validateToken(String token, String tokenType) {
        try {
            Jwts.parser()
                    .setSigningKey(SecurityConstants.SECRET)
                    .parseClaimsJws(token);
            if (tokenType.equals(getTokenTypeFromToken(token)))
                return true;
            else
                return false;
        } catch (SignatureException | MalformedJwtException
            | ExpiredJwtException | UnsupportedJwtException
            | IllegalArgumentException ex) {
            LOG.error(ex.getMessage());
            return false;
        }
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.SECRET)
                .parseClaimsJws(token)
                .getBody();
        String id = (String) claims.get("id");
        return Long.parseLong(id);
    }

    public String getTokenTypeFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.SECRET)
                .parseClaimsJws(token)
                .getBody();
        String tokenType = (String) claims.get("token_type");
        return tokenType;
    }
}
