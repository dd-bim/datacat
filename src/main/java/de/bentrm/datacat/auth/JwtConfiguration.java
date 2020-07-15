package de.bentrm.datacat.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import de.bentrm.datacat.properties.ApplicationProperties;
import de.bentrm.datacat.properties.AuthProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;

@Configuration
public class JwtConfiguration {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Bean
    public JWTVerifier jwtVerifier(Algorithm algorithm) {
        @NotNull final AuthProperties auth = applicationProperties.getAuth();
        return JWT
                .require(algorithm)
                .withIssuer(auth.getIssuer())
                .build();
    }

    @Bean
    public Algorithm jwtAlgorithm() {
        @NotNull final AuthProperties auth = applicationProperties.getAuth();
        return Algorithm.HMAC256(auth.getSecret());
    }

}
