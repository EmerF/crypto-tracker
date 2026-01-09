package dev.portfolio.cryptotracker.port.out;

import dev.portfolio.cryptotracker.domain.model.Coin;

public interface CryptoDataFetcher {

    Coin fetchData();
}
