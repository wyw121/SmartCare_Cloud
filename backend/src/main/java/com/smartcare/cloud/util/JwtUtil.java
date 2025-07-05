package com.smartcare.cloud.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * JWT工具类
 * 
 * 修复了HS512算法的密钥长度安全问题
 * 确保密钥长度至少512位以符合RFC 7518规范
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret:smartcare-cloud-jwt-secret-key-2024-secure-enough-for-hs512-algorithm}")
    private String secret;

    @Value("${jwt.expiration:7200}") // 默认2小时
    private Long expiration;

    @Value("${jwt.issuer:smartcare-cloud}")
    private String issuer;

    // 缓存安全密钥，避免重复生成
    private SecretKey cachedSecretKey;

    /**
     * 生成JWT token
     *
     * @param userId 用户ID
     * @param username 用户名
     * @param roleCode 角色编码
     * @return JWT token
     */
    public String generateToken(Long userId, String username, String roleCode) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("roleCode", roleCode);

        return createToken(claims, username);
    }

    /**
     * 创建token
     *
     * @param claims 载荷
     * @param subject 主题
     * @return token
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuer(issuer)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * 获取签名密钥
     * 
     * 修复：确保HS512算法的密钥长度至少512位
     * 按照RFC 7518规范要求生成安全密钥
     *
     * @return 签名密钥
     */
    private SecretKey getSigningKey() {
        if (cachedSecretKey == null) {
            // 检查配置的secret是否足够长（至少64字节=512位）
            byte[] keyBytes = secret.getBytes();
            
            if (keyBytes.length >= 64) {
                // 如果配置的密钥足够长，直接使用
                cachedSecretKey = Keys.hmacShaKeyFor(keyBytes);
            } else {
                // 如果配置的密钥太短，生成一个安全的密钥
                // 使用推荐的方法生成HS512算法的安全密钥
                cachedSecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
                
                // 记录警告信息（生产环境应该使用日志框架）
                System.out.println("警告: 配置的JWT密钥长度不足(" + keyBytes.length + "字节)，" +
                    "已自动生成安全密钥。建议在配置文件中设置至少64字节的密钥。");
            }
        }
        return cachedSecretKey;
    }

    /**
     * 从token中获取用户名
     *
     * @param token JWT token
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    /**
     * 从token中获取用户ID
     *
     * @param token JWT token
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        Object userId = claims.get("userId");
        if (userId instanceof Integer) {
            return ((Integer) userId).longValue();
        }
        return (Long) userId;
    }

    /**
     * 从token中获取角色编码
     *
     * @param token JWT token
     * @return 角色编码
     */
    public String getRoleCodeFromToken(String token) {
        return (String) getClaimsFromToken(token).get("roleCode");
    }

    /**
     * 从token中获取过期时间
     *
     * @param token JWT token
     * @return 过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    /**
     * 从token中获取载荷
     *
     * @param token JWT token
     * @return 载荷
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 判断token是否过期
     *
     * @param token JWT token
     * @return 是否过期
     */
    public Boolean isTokenExpired(String token) {
        try {
            Date expirationDate = getExpirationDateFromToken(token);
            return expirationDate.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 验证token
     *
     * @param token JWT token
     * @param username 用户名
     * @return 是否有效
     */
    public Boolean validateToken(String token, String username) {
        try {
            String tokenUsername = getUsernameFromToken(token);
            return (username.equals(tokenUsername) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 验证token格式
     *
     * @param token JWT token
     * @return 是否有效
     */
    public Boolean validateTokenFormat(String token) {
        try {
            getClaimsFromToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 刷新token
     *
     * @param token 原token
     * @return 新token
     */
    public String refreshToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Long userId = getUserIdFromToken(token);
            String username = claims.getSubject();
            String roleCode = getRoleCodeFromToken(token);

            return generateToken(userId, username, roleCode);
        } catch (Exception e) {
            throw new RuntimeException("Token刷新失败", e);
        }
    }

    /**
     * 获取token剩余有效时间（秒）
     *
     * @param token JWT token
     * @return 剩余时间
     */
    public Long getRemainTimeFromToken(String token) {
        try {
            Date expirationDate = getExpirationDateFromToken(token);
            Date now = new Date();
            return Math.max(0, (expirationDate.getTime() - now.getTime()) / 1000);
        } catch (Exception e) {
            return 0L;
        }
    }
}
