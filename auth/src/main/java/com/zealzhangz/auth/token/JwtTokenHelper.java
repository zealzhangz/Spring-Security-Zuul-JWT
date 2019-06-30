package com.zealzhangz.auth.token;

import com.zealzhangz.auth.exception.InvalidJwtToken;
import com.zealzhangz.auth.exception.JwtExpiredTokenException;
import com.zealzhangz.auth.model.TokenMetadata;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import static com.zealzhangz.common.constant.Constant.HEADER_PREFIX;

/**
 * @author Created by https://zhangaoo.com.<br/>
 * @version Version: 0.0.1
 * @date DateTime: 2019/06/24 19:46:00<br/>
 */
@Component
@Slf4j
public class JwtTokenHelper {
    @Autowired
    private TokenMetadata metadata;

    public String createAccessToken(String username, String userId) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(userId)) {
            throw new IllegalArgumentException("Cannot create JWT Token without username or userId");
        }
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("scopes", Collections.emptyList());
        claims.put("userId", userId);
        claims.put("type", "access_token");

        LocalDateTime currentTime = LocalDateTime.now();

        String token = Jwts.builder()
                //Note that first set
                .setClaims(claims)
                .setIssuer(metadata.getIssuer())
                .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(currentTime
                        .plusMinutes(metadata.getExpiredMinutes())
                        .atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, metadata.getSigningKey())
                .compact();
        return token;
    }

    public String createRefreshToken(String username, String userId) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(userId)) {
            throw new IllegalArgumentException("Cannot create JWT Token without username or userId");
        }
        LocalDateTime currentTime = LocalDateTime.now();

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("userId", userId);
        claims.put("type", "refresh_token");

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer(metadata.getIssuer())
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(currentTime
                        .plusMinutes(metadata.getRefreshExpiredMinutes())
                        .atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, metadata.getSigningKey())
                .compact();
        return token;
    }

    public Jws<Claims> parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(metadata.getSigningKey()).parseClaimsJws(token);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
            log.error("Invalid JWT Token", ex);
            throw new InvalidJwtToken("Invalid JWT token: ");
        } catch (ExpiredJwtException expiredEx) {
            log.info("JWT Token is expired", expiredEx);
            throw new JwtExpiredTokenException("JWT Token expired", expiredEx);
        }
    }

    public String extract(String header) {
        if (StringUtils.isBlank(header)) {
            throw new InvalidJwtToken("Authorization header cannot be blank!");
        }

        if (header.length() < HEADER_PREFIX.length()) {
            throw new InvalidJwtToken("Invalid authorization header size.");
        }
        if(!header.startsWith(HEADER_PREFIX)){
            throw new InvalidJwtToken("Invalid authorization,Token type valid");
        }

        return header.substring(HEADER_PREFIX.length());
    }
}
