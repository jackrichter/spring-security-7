package net.javaguides.springsecurity.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

/**
 * Utility class to generate a JWT token, validate it, and extract user details from it, like username which is, in ower case, the subject.
 */
@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-milliseconds}")
    private Long jwtExpirationInMs;

    // Generate JWT token utility method. The authentication object has been stored in the Security Context during login
    public String generateToken(Authentication authentication) {

        String username = authentication.getName();
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + jwtExpirationInMs);

        // Build the JWT token and return it
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(expirationDate)
                .signWith(getKey())
                .compact();
    }

    // Get username from JWT token
    public String getUsernameFromToken(String token) {

        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // Validate the JWT token
    public boolean validateToken(String token) {

        Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parse(token);          // If token is valid, no exception is thrown

        return true;
    }

    /**
     * Utility method to get the Secret key from the JWT secret.
     * @return Key java.security.Key
     */
    private Key getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
}
