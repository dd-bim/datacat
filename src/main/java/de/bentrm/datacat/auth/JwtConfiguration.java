package de.bentrm.datacat.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfiguration {

    @Value("${de.bentrm.datacat.auth.secret}")
    private String secret;

    @Value("${de.bentrm.datacat.auth.issuer}")
    private String issuer;

    @Bean
    public JWTVerifier jwtVerifier(Algorithm algorithm) {
        return JWT
                .require(algorithm)
                .withIssuer(issuer)
                .build();
    }

    @Bean
    public Algorithm jwtAlgorithm() {
        return Algorithm.HMAC256(secret);
    }

}
