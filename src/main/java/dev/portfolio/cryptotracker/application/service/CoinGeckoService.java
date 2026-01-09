package dev.portfolio.cryptotracker.application.service;

import dev.portfolio.cryptotracker.application.service.dto.CoinGeckoMarketChartResponse;
import dev.portfolio.cryptotracker.domain.model.exception.NoDataFoundException;
import dev.portfolio.cryptotracker.domain.model.Coin;
import dev.portfolio.cryptotracker.port.out.CoinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CoinGeckoService {

    
    private static final Logger logger = LoggerFactory.getLogger(CoinGeckoService.class);
    private static final String apiUrl= "https://api.coingecko.com/api/v3";

    private final WebClient webClient;
    private final CoinRepository coinRepository;

    private static final Map<String, String> SYMBOL_TO_ID = Map.of(
            "btc", "bitcoin",
            "eth", "ethereum",
            "ada", "cardano",
            "bnb", "binancecoin",
            "xrp", "ripple",
            "doge", "dogecoin",
            "sol", "solana",
            "dot", "polkadot"
    );

    @Autowired
    public CoinGeckoService(WebClient.Builder webClientBuilder, CoinRepository coinRepository) {
        this.webClient = webClientBuilder.baseUrl(apiUrl).build();
        this.coinRepository = coinRepository;
    }

    @Cacheable(value = "coinData", key = "#symbol")
    public Mono<Coin> fetchCoinData(String symbol) {
        String coinId = SYMBOL_TO_ID.getOrDefault(symbol.toLowerCase(), symbol.toLowerCase());
        logger.info("Fetching data for symbol: {} resolved to ID: {}", symbol, coinId);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/coins/{id}/market_chart")
                        .queryParam("vs_currency", "usd")
                        .queryParam("days", "3")
                        .build(coinId))
                .retrieve()
                .bodyToMono(CoinGeckoMarketChartResponse.class)
                .flatMap(response -> {
                    if (response.getPrices().isEmpty() || response.getMarket_caps().isEmpty()) {
                        logger.warn("No data found for coin: {}", symbol);
                        return Mono.error(new NoDataFoundException("No data found for coin: " + symbol));
                    }

                    double price = response.getPrices().get(response.getPrices().size() - 1).get(1);
                    double marketCap = response.getMarket_caps().get(response.getMarket_caps().size() - 1).get(1);

                    Coin coin = new Coin();
                    coin.setSymbol(symbol.toLowerCase());
                    coin.setCurrentPrice(String.valueOf(price));
                    coin.setMarketCap(String.valueOf(marketCap));
                    coin.setName(coinId);

                    logger.info("Saving coin: {} with price: {} and market cap: {}", symbol, price, marketCap);

                    return Mono.fromCallable(() -> coinRepository.save(coin)).thenReturn(coin);
                })
                .doOnError(ex -> logger.error("Error while fetching coin data: {}", ex.getMessage()));
    }
}
