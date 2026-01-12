package dev.portfolio.cryptotracker.port.out;

import dev.portfolio.cryptotracker.domain.model.Coin;

import java.util.List;

public interface CryptoDataFetcher {
    String getExchange();                // exchange identifier
    List<Coin> fetchData() throws Exception;
}
