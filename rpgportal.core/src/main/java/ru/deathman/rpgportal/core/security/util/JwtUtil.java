package ru.deathman.rpgportal.core.security.util;

import io.jsonwebtoken.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

/**
 * @author Виктор
 */
public class JwtUtil {

    public static String createToken(UserDetails user, long expireTime, String secretKey) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(new Date(new Date().getTime() + expireTime))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public static String verifyToken(String token, String secretKey) {
        String username = null;
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        username = claims.getSubject();
        if (username == null) {
            return null;
        }
        return username;
    }
}
