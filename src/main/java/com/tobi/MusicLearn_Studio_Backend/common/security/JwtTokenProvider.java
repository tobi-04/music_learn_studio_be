package com.tobi.MusicLearn_Studio_Backend.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    /**
     * Tạo JWT token từ userId
     */
    public String generateToken(String userId) {
        return generateToken(userId, new HashMap<>());
    }

    /**
     * Tạo JWT token với custom claims
     */
    public String generateToken(String userId, Map<String, Object> claims) {
        try {
            // Ensure secret is at least 512 bits (64 bytes)
            byte[] secretBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
            if (secretBytes.length < 64) {
                log.warn("JWT Secret is less than 512 bits. Current size: {} bits", secretBytes.length * 8);
            }

            SecretKey key = Keys.hmacShaKeyFor(secretBytes);

            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(userId)
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(key, SignatureAlgorithm.HS512)
                    .compact();
        } catch (Exception e) {
            log.error("Error creating JWT token", e);
            throw new RuntimeException("Error creating JWT token", e);
        }
    }

    /**
     * Lấy userId từ JWT token
     */
    public String getUserIdFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            if (claims != null) {
                return claims.getSubject();
            }
            return null;
        } catch (Exception e) {
            log.error("Error extracting userId from JWT token", e);
            return null;
        }
    }

    /**
     * Lấy tất cả claims từ JWT token
     */
    public Claims getClaimsFromToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("Error extracting claims from JWT token", e);
            return null;
        }
    }

    /**
     * Kiểm tra JWT token có hợp lệ không
     */
    public boolean validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (Exception e) {
            log.error("JWT token validation failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Kiểm tra token hết hạn
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            if (claims != null) {
                return claims.getExpiration().before(new Date());
            }
            return true;
        } catch (Exception e) {
            log.error("Error checking token expiration", e);
            return true;
        }
    }

    /**
     * Lấy ngày hết hạn từ token
     */
    public Date getExpirationDateFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            if (claims != null) {
                return claims.getExpiration();
            }
            return null;
        } catch (Exception e) {
            log.error("Error extracting expiration date from JWT token", e);
            return null;
        }
    }
}
