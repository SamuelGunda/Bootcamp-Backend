package com.kasv.gunda.bootcamp.security.jwt;


import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;

import com.kasv.gunda.bootcamp.security.services.UserDetailsImpl;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${gunda.bootcamp.app.jwtSecret}")
    private String jwtSecret;

    @Value("${gunda.bootcamp.app.jwtExpirationMs}")
    private int jwtExpirationMs;

        public String generateJwtToken(UserDetailsImpl userPrincipal) {
            return Jwts.builder().setSubject(userPrincipal.getUsername())
                    .claim("id", userPrincipal.getId())
                    .claim("email", userPrincipal.getEmail())
                    .claim("roles", userPrincipal.getAuthorities())
                    .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)).signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }


    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public Long getUserIdFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().get("id", Long.class);
    }

    private Key key() {
        byte[] decodedSecret = Base64.getUrlDecoder().decode(jwtSecret);
        return Keys.hmacShaKeyFor(decodedSecret);
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
