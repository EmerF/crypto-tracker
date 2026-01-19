package dev.portfolio.cryptotracker.adapter.out.binance;

import dev.portfolio.cryptotracker.adapter.common.AbstractCryptoDataFetcher;
import dev.portfolio.cryptotracker.domain.model.Coin;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BinanceFetcher extends AbstractCryptoDataFetcher {
    private final RestTemplate rest;
    public BinanceFetcher(BinanceAuthProvider authProvider, RestTemplate rest) {
        super(authProvider);
        this.rest = rest;
    }
    @Override
    protected List<Coin> fetchWithAuth(String authToken) {
        // call Binance API and map to Coin
        return null;
    }

    @Override
    public String getExchange() {
        return "";
    }
}
