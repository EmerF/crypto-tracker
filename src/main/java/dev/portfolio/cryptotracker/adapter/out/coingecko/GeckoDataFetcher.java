package dev.portfolio.cryptotracker.adapter.out.coingecko;

import dev.portfolio.cryptotracker.port.out.CryptoDataFetcher;
import dev.portfolio.cryptotracker.domain.model.Coin;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

// base stereotype anotation Use for generic utility classes
@Component
@Qualifier("geckoDataFetcher")
public class GeckoDataFetcher implements CryptoDataFetcher {
    @Override
    public String getExchange() {
        return "";
    }

    @Override
    public List<Coin> fetchData() {
        return new ArrayList<Coin>();
    }
}
