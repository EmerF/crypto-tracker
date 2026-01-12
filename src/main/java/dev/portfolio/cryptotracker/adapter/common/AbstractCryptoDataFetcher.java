package dev.portfolio.cryptotracker.adapter.common;

import dev.portfolio.cryptotracker.domain.model.Coin;
import dev.portfolio.cryptotracker.port.out.CryptoDataFetcher;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public abstract class AbstractCryptoDataFetcher implements CryptoDataFetcher {
    protected final RestTemplate rest;

    protected AbstractCryptoDataFetcher(RestTemplate rest) {
        this.rest = rest;
    }

    protected <T> T get(String url, Class<T> responseType) {
        return rest.getForObject(url, responseType);
    }
}
