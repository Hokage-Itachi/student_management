package com.example.student_management.security.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtProvider {
    @Value("${student_management.auth.jwt.JWT_SECRET}")
    private String JWT_SECRET;
    @Value("${student_management.auth.jwt.JWT_EXPIRATION}")
    private Long JWT_EXPIRATION;

    public String generateToken(UserDetails userDetails) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + JWT_EXPIRATION);
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public String getUserNameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token:  {}", e.getMessage());
            return false;
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token: {}", e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty: {}", e.getMessage());
            return false;
        } catch (SignatureException e) {
            log.error("Invalid JWT Signature: {}", e.getMessage());
            return false;
        }

    }


}
