package de.uko.takeaway.persistence.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "de.uko.takeaway.persistence.repository")
@EntityScan(basePackages = "de.uko.takeaway.persistence.entity")
public class JpaConfig {
}
