package de.bentrm.datacat.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import de.bentrm.datacat.properties.AppProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfiguration {

    private final AppProperties properties;

    public JwtConfiguration(AppProperties properties) {
        this.properties = properties;
    }

    @Bean
    public JWTVerifier jwtVerifier(Algorithm algorithm) {
        final var auth = properties.getAuth();
        return JWT
                .require(algorithm)
                .withIssuer(auth.getIssuer())
                .build();
    }

    @Bean
    public Algorithm jwtAlgorithm() {
        final var auth = properties.getAuth();
        return Algorithm.HMAC256(auth.getSecret());
    }

}
