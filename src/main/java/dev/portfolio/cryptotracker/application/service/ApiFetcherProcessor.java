package dev.portfolio.cryptotracker.application.service;

import dev.portfolio.cryptotracker.port.out.CryptoDataFetcher;
import dev.portfolio.cryptotracker.domain.model.Coin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ApiFetcherProcessor {

    private static final Logger log = LoggerFactory.getLogger(ApiFetcherProcessor.class);
    private final List<CryptoDataFetcher> fetchers;
    public ApiFetcherProcessor(List<CryptoDataFetcher> fetchers) {
        this.fetchers = fetchers;
    }

    public List<Coin> fetchAll() {
        return fetchers.stream()
                .flatMap(fetcher -> {
                    try {
                        return fetcher.fetchData().stream();
                    } catch (Exception e) {
                        log.warn("Fetcher {} failed: {}", fetcher.getExchange(), e.getMessage());
                        return Stream.empty();
                    }
                })
                .distinct() // depends on Coin#equals/hashCode
                .collect(Collectors.toList());
    }
}
