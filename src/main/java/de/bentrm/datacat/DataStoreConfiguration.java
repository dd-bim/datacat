package de.bentrm.datacat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Optional;

@Configuration
@EnableNeo4jRepositories(
        basePackages = {
                "de.bentrm.datacat.base.repository",
                "de.bentrm.datacat.auth.repository",
                "de.bentrm.datacat.catalog.repository"
        }
)
@EnableTransactionManagement
public class DataStoreConfiguration {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.ofNullable(SecurityContextHolder.getContext()
                .getAuthentication())
                .map(auth -> (String) auth.getPrincipal())
                .or(() -> Optional.of("SYSTEM"));
    }

}
