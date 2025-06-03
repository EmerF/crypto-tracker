package dev.portfolio.cryptotracker.impl;

import dev.portfolio.cryptotracker.interf.CryptoDataFetcher;
import dev.portfolio.cryptotracker.model.Coin;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Component
@Qualifier("geckoDataFetcher")
public class GeckoDataFetcher implements CryptoDataFetcher {
    @Override
    public Coin fetchData() {
        return new Coin();
    }
}
