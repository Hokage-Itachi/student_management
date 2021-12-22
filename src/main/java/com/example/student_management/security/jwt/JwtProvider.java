package com.example.student_management.security.jwt;

import com.example.student_management.exception.DataInvalidException;
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
    @Value("${student_management.auth.jwt.JWT_LOGIN_EXPIRATION}")
    private Long JWT_LOGIN_EXPIRATION;
    @Value("${student_management.auth.jwt.JWT_REFRESH_EXPIRATION}")
    private Long JWT_REFRESH_EXPIRATION;

    public String generateLoginToken(UserDetails userDetails) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + JWT_LOGIN_EXPIRATION);
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public String generateRefreshToken(String accessToken) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + JWT_REFRESH_EXPIRATION);
        return Jwts.builder()
                .setSubject(accessToken)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public String getSubjectFromToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
            return claims.getSubject();
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token:  {}", e.getMessage());
            throw new DataInvalidException("Invalid JWT token");
        }
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
