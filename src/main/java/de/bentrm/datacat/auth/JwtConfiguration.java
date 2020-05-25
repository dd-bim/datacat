package de.bentrm.datacat.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfiguration {

    @Autowired
    private AuthProperties authProperties;

    @Bean
    public JWTVerifier jwtVerifier(Algorithm algorithm) {
        return JWT
                .require(algorithm)
                .withIssuer(authProperties.getIssuer())
                .build();
    }

    @Bean
    public Algorithm jwtAlgorithm() {
        return Algorithm.HMAC256(authProperties.getSecret());
    }

}
