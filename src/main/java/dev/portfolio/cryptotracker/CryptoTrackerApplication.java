package dev.portfolio.cryptotracker;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "dev.portfolio.cryptotracker")
@EntityScan(basePackages = "dev.portfolio.cryptotracker.model")  // Ensure entities are scanned
public class CryptoTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoTrackerApplication.class, args);
	}

}
