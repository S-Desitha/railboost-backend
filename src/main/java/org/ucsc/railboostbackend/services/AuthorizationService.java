package org.ucsc.railboostbackend.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.*;

public class AuthorizationService {
    private static byte[] secret = new byte[32];
    private static Algorithm algorithm;
    private static final List<String> revokedTokenIDs = new ArrayList<>();

    static {
        new Random().nextBytes(secret);
        algorithm = Algorithm.HMAC256(secret);
    }

    public JWTCreator.Builder getJWTBuilder() {
        return JWT.create()
                .withIssuer("RailBoost")
                .withSubject("User Details")
                .withJWTId(UUID.randomUUID().toString())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(new Date().getTime()+1000*60*60*24));
    }
    
    
    public DecodedJWT verifyToken(String token) {
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("RailBoost")
                .build();
        
        return verifier.verify(token);
    }


    public void revokeToken(DecodedJWT jwt) {
        revokedTokenIDs.add(jwt.getId());
    }


    public boolean isRevoked(DecodedJWT jwt) {
        return revokedTokenIDs.contains(jwt.getId());
    }


    public Algorithm getAlgorithm() {return algorithm;}
}
