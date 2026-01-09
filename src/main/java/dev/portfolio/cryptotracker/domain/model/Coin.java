package dev.portfolio.cryptotracker.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coin {

    @Id
    private String symbol;  // e.g., "BTC", "ETH", etc.
    private String name;
    private String currentPrice;  // Store the price as a string initially
    private String marketCap;

}
