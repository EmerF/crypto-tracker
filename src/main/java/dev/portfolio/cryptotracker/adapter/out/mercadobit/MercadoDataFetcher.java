package dev.portfolio.cryptotracker.adapter.out.mercadobit;

import dev.portfolio.cryptotracker.adapter.common.AbstractCryptoDataFetcher;
import dev.portfolio.cryptotracker.port.out.CryptoDataFetcher;
import dev.portfolio.cryptotracker.domain.model.Coin;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Qualifier("mercadoDataFetcher")
public class MercadoDataFetcher extends AbstractCryptoDataFetcher {
    public MercadoDataFetcher(RestTemplate rest) {
        super(rest);
    }

    @Override
    public String getExchange() {
        return "MERCADO_BIT";
    }

    @Override
    public List<Coin> fetchData() {
        // Example Coinbase endpoint (adjust to actual API shape)
        MarcadoBasePrice[] resp = get("https://api.coinbase.com/v2/prices/spot?currency=USD", CoinbasePrice[].class);
        if (resp == null) return List.of();
        return Arrays.stream(resp)
                .map(p -> new Coin(p.base, p.amount))
                .collect(Collectors.toList());
    }

    static class MarcadoBasePrice {
        public String base;
        public String currency;
        public String amount;
    }
}
