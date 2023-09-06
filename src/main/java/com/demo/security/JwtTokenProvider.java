package com.demo.security;

import com.demo.entity.RoleEntity;
import com.demo.entity.UserEntity;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String JWT_SECRET;
    private final long JWT_TIME_EXPIRATION= 15 * 24 * 60 * 60 * 1000L;
    public String createToken(UserEntity userEntity, Date currentDate){
        Date dateExpire =new Date(currentDate.getTime() + JWT_TIME_EXPIRATION);
        Map<String,Object> mapDataPayload = new HashMap<>();
        mapDataPayload.put("username",userEntity.getUsername());
        mapDataPayload.put("idUser",userEntity.getId());
        mapDataPayload.put("roles",userEntity.getRoleEntities()
                .stream().map(RoleEntity::getName).collect(Collectors.joining(",")));
        // Tạo chuỗi json web token từ id của user.
        return Jwts.builder()
                .setSubject(Long.toString(userEntity.getId()))
                .setAudience(userEntity.getUsername())
                .setIssuedAt(currentDate)
                .setClaims(mapDataPayload)
                .setExpiration(dateExpire)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public String getTokenFromRequest(HttpServletRequest request){
        String bearToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearToken) &&  bearToken.startsWith("Bearer ")){
            return bearToken.substring(7);
        }
        return null;
    }

    public Claims getPayload(String tokenJwt){
        return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(tokenJwt).getBody();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            System.out.println("Invalid JWT token: {}" +  ex.getMessage());
        } catch (ExpiredJwtException ex) {
            System.out.println("Expired JWT token: {}" + ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            System.out.println("Unsupported JWT token: {}" + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.out.println("JWT claims string is empty: {}" + ex.getMessage());
        }
        return false;
    }
}
