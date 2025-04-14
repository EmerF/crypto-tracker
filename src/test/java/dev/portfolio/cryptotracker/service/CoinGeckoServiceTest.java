package dev.portfolio.cryptotracker.service;

import dev.portfolio.cryptotracker.controller.GlobalExceptionHandler;
import dev.portfolio.cryptotracker.dto.CoinGeckoMarketChartResponse;
import dev.portfolio.cryptotracker.exception.NoDataFoundException;
import dev.portfolio.cryptotracker.model.Coin;
import dev.portfolio.cryptotracker.repository.CoinRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.function.Function;

@ExtendWith(MockitoExtension.class)
class CoinGeckoServiceTest {

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private CoinRepository coinRepository;

    private CoinGeckoService coinGeckoService;

    @BeforeEach
    void setup() {
        Mockito.when(webClientBuilder.baseUrl(Mockito.anyString())).thenReturn(webClientBuilder);
        Mockito.when(webClientBuilder.build()).thenReturn(webClient);

        coinGeckoService = new CoinGeckoService(webClientBuilder, coinRepository);
    }

    @Test
    void fetchCoinData_shouldReturnMockedCoin() {
        String symbol = "btc";

        CoinGeckoMarketChartResponse mockResponse = new CoinGeckoMarketChartResponse();
        mockResponse.setPrices(List.of(
                List.of(1712707200000.0, 50000.0),
                List.of(1712793600000.0, 51000.0)
        ));
        mockResponse.setMarket_caps(List.of(
                List.of(1712707200000.0, 1000000000.0),
                List.of(1712793600000.0, 1050000000.0)
        ));

        Mockito.when(webClient.get()).thenReturn(requestHeadersUriSpec);
        Mockito.when(requestHeadersUriSpec.uri(Mockito.any(Function.class)))
                .thenReturn(requestHeadersSpec);
        Mockito.when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        Mockito.when(responseSpec.bodyToMono(CoinGeckoMarketChartResponse.class))
                .thenReturn(Mono.just(mockResponse));
        Mockito.when(coinRepository.save(Mockito.any(Coin.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        Mono<Coin> result = coinGeckoService.fetchCoinData(symbol);

        StepVerifier.create(result)
                .expectNextMatches(coin ->
                        coin.getSymbol().equalsIgnoreCase("btc") &&
                                Double.parseDouble(coin.getCurrentPrice()) == 51000.0 &&
                                Double.parseDouble(coin.getMarketCap()) == 1050000000.0
                ).verifyComplete();
    }

    @Test
    void fetchCoinData_shouldThrowExceptionOnApiError() {
        String symbol = "btc";

        // Simulate an API error (e.g., 4xx or 5xx error response)
        Mockito.when(webClient.get()).thenReturn(requestHeadersUriSpec);
        Mockito.when(requestHeadersUriSpec.uri(Mockito.any(Function.class)))
                .thenReturn(requestHeadersSpec);
        Mockito.when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        Mockito.when(responseSpec.bodyToMono(CoinGeckoMarketChartResponse.class))
                .thenReturn(Mono.error(new NoDataFoundException("API error")));

        Mono<Coin> result = coinGeckoService.fetchCoinData(symbol);

        StepVerifier.create(result)
                .expectError(NoDataFoundException.class) // Expecting the custom exception
                .verify();
    }


}
