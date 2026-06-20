/*
package mockLoginedChecker; // Change this to your actual package name!

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service // This tells Spring Boot to create and manage this class
public class JwtService {

    // 🔑 The Secret Key: The jjwt library will automatically generate a secure 256-bit
    // cryptographic key for you right here. (In a real app, you'd store this in application.properties)
    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // ==========================================
    // 1. CREATE A REAL TOKEN
    // ==========================================
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)                 // Who is this token for?
                .setIssuedAt(new Date())              // When was it created?
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // Expires in 24 hours
                .signWith(secretKey)                  // Sign it using the Secret Key!
                .compact();                           // Turn it into a compact String
    }

    // ==========================================
    // 2. VERIFY THE TOKEN MATHEMETICALLY
    // ==========================================
    public boolean validateToken(String token) {
        try {
            // The library attempts to decode the token using your secret key.
            // If the token was tampered with, or if it is expired, this will throw an Error!
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

            // If we reach this line without throwing an error, the token is 100% valid!
            return true;
        } catch (Exception e) {
            // Token is expired, malformed, or fake.
            System.out.println("Token validation failed: " + e.getMessage());
            return false;
        }
    }
}
*/