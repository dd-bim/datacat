package de.bentrm.datacat;

import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.boot.actuate.audit.InMemoryAuditEventRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuditConfiguration {

    @Bean
    public AuditEventRepository auditEventRepository() {
        return new InMemoryAuditEventRepository(1000);
    }
}
