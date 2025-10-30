package com.smartcare.cloud.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * JWT工具类
 * 
 * 用于生成和解析JWT token
 *
 * @author SmartCare Team
 * @date 2025-10-29
 */
@Component
public class JwtUtils {

    @Value("${jwt.secret:SmartCare_Cloud_Secret_Key_2025}")
    private String secret;

    @Value("${jwt.expiration:86400000}")
    private Long expiration;

    /**
     * 从token中获取用户ID
     * 
     * @param token JWT token
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Object userIdObj = claims.get("userId");
            
            if (userIdObj instanceof Integer) {
                return ((Integer) userIdObj).longValue();
            } else if (userIdObj instanceof Long) {
                return (Long) userIdObj;
            }
            
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从token中获取角色编码
     * 
     * @param token JWT token
     * @return 角色编码
     */
    public String getRoleCodeFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return (String) claims.get("roleCode");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从token中获取用户名
     * 
     * @param token JWT token
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 生成token
     * 
     * @param userId 用户ID
     * @param username 用户名
     * @param roleCode 角色编码
     * @return JWT token
     */
    public String generateToken(Long userId, String username, String roleCode) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("roleCode", roleCode);
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 验证token是否有效
     * 
     * @param token JWT token
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检查token是否过期
     * 
     * @param token JWT token
     * @return 是否过期
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expirationDate = claims.getExpiration();
            return expirationDate.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 从token中解析Claims
     * 
     * @param token JWT token
     * @return Claims对象
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}
