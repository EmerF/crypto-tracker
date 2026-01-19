package dev.portfolio.cryptotracker.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Coin  {

    @Id
    private String symbol;  // e.g., "BTC", "ETH", etc.
    private String name;
    private String currentPrice;  // Store the price as a string initially
    private String marketCap;

    public Coin(String symbol, String price) {
        this.symbol = symbol;
        this.currentPrice = price;
    }
}
