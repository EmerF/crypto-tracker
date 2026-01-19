package dev.portfolio.cryptotracker.adapter.out.mercadobit;

import dev.portfolio.cryptotracker.adapter.common.AbstractCryptoDataFetcher;
import dev.portfolio.cryptotracker.port.out.AuthProvider;
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
    private final RestTemplate rest;
    private static final String TICKERS_URL = "https://www.mercadobitcoin.net/api/v4/tickers";

    public MercadoDataFetcher(AuthProvider authProvider, RestTemplate rest) {
        super(authProvider);
        this.rest = rest;
    }

    @Override
    public String getExchange() {
        return "MERCADO_BIT";
    }

    @Override
    protected List<Coin> fetchWithAuth(String authToken) {
        // Mercado Bitcoin's public tickers endpoint returns a JSON array of tickers.
        // We only care about symbol and last price for now.
        Ticker[] resp = rest.getForObject(TICKERS_URL, Ticker[].class);
        if (resp == null || resp.length == 0) return List.of();
        return Arrays.stream(resp)
                .map(t -> new Coin(t.symbol, t.last))
                .collect(Collectors.toList());
    }

    static class Ticker {
        public String symbol; // e.g. "BTC"
        public String last;   // last traded price (string in API)
        // other fields omitted
    }
}
