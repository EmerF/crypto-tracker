package dev.portfolio.cryptotracker.application.service;

import dev.portfolio.cryptotracker.port.out.CryptoDataFetcher;
import dev.portfolio.cryptotracker.domain.model.Coin;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApiFetcherProcessor {

    private final List<CryptoDataFetcher> fetchers;
    public ApiFetcherProcessor(List<CryptoDataFetcher> fetchers) {
        this.fetchers = fetchers;
    }

    public List<Coin> fetchAll(){
        return fetchers.stream()
                .map(CryptoDataFetcher::fetchData)
                .collect(Collectors.toList());
    }
}
