package dev.portfolio.cryptotracker.service;

import dev.portfolio.cryptotracker.dto.CoinGeckoMarketChartResponse;
import dev.portfolio.cryptotracker.exception.NoDataFoundException;
import dev.portfolio.cryptotracker.model.Coin;
import dev.portfolio.cryptotracker.repository.CoinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class CoinGeckoService {

    private final WebClient webClient;
    private final CoinRepository coinRepository;

    // Simple static symbol → ID mapping
    private static final Map<String, String> SYMBOL_TO_ID = Map.of(
            "btc", "bitcoin",
            "eth", "ethereum",
            "ada", "cardano",
            "bnb", "binancecoin",
            "xrp", "ripple",
            "doge", "dogecoin",
            "sol", "solana",
            "dot", "polkadot"
            // Add more as needed
    );

    @Autowired
    public CoinGeckoService(WebClient.Builder webClientBuilder, CoinRepository coinRepository) {
        this.webClient = webClientBuilder.baseUrl("https://api.coingecko.com/api/v3").build();
        this.coinRepository = coinRepository;
    }
    @Cacheable(value = "coinData", key = "#symbol")
    public Mono<Coin> fetchCoinData(String symbol) {
        String coinId = SYMBOL_TO_ID.getOrDefault(symbol.toLowerCase(), symbol.toLowerCase());

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
                        // Lança exceção se não houver dados
                        return Mono.error(new NoDataFoundException("No data found for coin: " + symbol));
                    }

                    Coin coin = new Coin();
                    coin.setSymbol(symbol.toLowerCase());

                    // Get latest price
                    double price = response.getPrices().get(response.getPrices().size() - 1).get(1);
                    coin.setCurrentPrice(String.valueOf(price));

                    // Get latest market cap
                    double marketCap = response.getMarket_caps().get(response.getMarket_caps().size() - 1).get(1);
                    coin.setMarketCap(String.valueOf(marketCap));

                    coin.setName(coinId); // Using CoinGecko ID as name for clarity

                    // Save to DB if not already in DB
                    return Mono.fromCallable(() -> coinRepository.save(coin)).thenReturn(coin);
                });
    }
}
