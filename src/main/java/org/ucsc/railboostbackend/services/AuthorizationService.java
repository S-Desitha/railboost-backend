package org.ucsc.railboostbackend.services;


import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.*;

public class AuthorizationService {
    private static final byte[] secret = new byte[32];
    private static final SecretKey key;
    private static final List<String> revokedTokenIDs = new ArrayList<>();

    static {
        new SecureRandom().nextBytes(secret);
        key = new SecretKeySpec(secret, 0, secret.length, "HmacSHA256");
    }

    public JwtBuilder getJWTBuilder() {
        return Jwts.builder()
                .issuer("RailBoost")
                .subject("User Details")
                .id(UUID.randomUUID().toString())
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime()+1000*60));
    }
    
    
    public Claims verifyToken(String token) throws JwtException {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }


//    public void revokeToken(DecodedJWT jwt) {
//        revokedTokenIDs.add(jwt.getId());
//    }


    public boolean isRevoked(Claims claims) {
        return revokedTokenIDs.contains(claims.getId());
    }


    public SecretKey getKey() {return key;}
}
