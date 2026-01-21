package dev.portfolio.cryptotracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "dev.portfolio.cryptotracker")
@EntityScan("dev.portfolio.cryptotracker.domain.model")
@EnableJpaRepositories("dev.portfolio.cryptotracker.port.out")
public class CryptoTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoTrackerApplication.class, args);
	}

}
