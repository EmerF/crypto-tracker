package dev.portfolio.cryptotracker.adapter.out.mercadobit;

import dev.portfolio.cryptotracker.port.out.CryptoDataFetcher;
import dev.portfolio.cryptotracker.domain.model.Coin;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("mercadoDataFetcher")
public class MercadoDataFetcher implements CryptoDataFetcher {
    @Override
    public Coin fetchData() {
        return new Coin();
    }
}
