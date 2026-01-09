package dev.portfolio.cryptotracker.adapter.out.coingecko;

import dev.portfolio.cryptotracker.port.out.CryptoDataFetcher;
import dev.portfolio.cryptotracker.domain.model.Coin;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

// base stereotype anotation Use for generecu utility classes
@Component
@Qualifier("geckoDataFetcher")
public class GeckoDataFetcher implements CryptoDataFetcher {
    @Override
    public Coin fetchData() {
        return new Coin();
    }
}
