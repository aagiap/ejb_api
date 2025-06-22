package com.example.ws_cert.security.service;


import com.example.ws_cert.exception.AppException;
import com.example.ws_cert.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;


import org.springframework.core.io.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTService {

//    @Value("${jwt.secret-key}")
//    private String secretKey;

    @Value("${jwt.private-key-url}")
    private Resource privateKeyResource;

    @Value("${jwt.public-key-url}")
    private Resource publicKeyResource;

    private PrivateKey privateKey;
    private PublicKey publicKey;

    @Value("${jwt.expiration-time}")
    private long expirationTime;


    public String generateToken(String username) { // Use email as username
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

//    private String createToken(Map<String, Object> claims, String username) {
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(username)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * expirationTime))
//                .signWith(getSignKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }

    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * expirationTime))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

//    private Key getSignKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

//    private Claims extractAllClaims(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(getSignKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    @PostConstruct
    public void loadKeys() {
        // Load private key
        try (InputStream inputStream = privateKeyResource.getInputStream()) {
            String privateKeyPEM = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            privateKey = loadPrivateKey(privateKeyPEM);
        } catch (Exception e) {
            throw new AppException(ErrorCode.FAILED_TO_LOAD_KEYSTORE);
        }

        // Load public key
        try (InputStream inputStream = publicKeyResource.getInputStream()) {
            String publicKeyPEM = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            publicKey = loadPublicKey(publicKeyPEM);
        } catch (Exception e) {
            throw new AppException(ErrorCode.FAILED_TO_LOAD_KEYSTORE);
        }
    }

    // Parse PEM-formatted RSA keys
    private PrivateKey loadPrivateKey(String key) throws Exception {
        String privateKeyPEM = key.replaceAll("-----BEGIN (.*)-----", "")
                .replaceAll("-----END (.*)-----", "")
                .replaceAll("\\s", "");
        byte[] decoded = Base64.getDecoder().decode(privateKeyPEM);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    private PublicKey loadPublicKey(String key) throws Exception {
        String publicKeyPEM = key.replaceAll("-----BEGIN (.*)-----", "")
                .replaceAll("-----END (.*)-----", "")
                .replaceAll("\\s", "");
        byte[] decoded = Base64.getDecoder().decode(publicKeyPEM);

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }
}
