package dev.portfolio.cryptotracker.controller;

import dev.portfolio.cryptotracker.model.Coin;
import dev.portfolio.cryptotracker.service.CoinGeckoService;
import dev.portfolio.cryptotracker.service.CoinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/coins")
public class CoinController {

    private final CoinService coinService;
    private final CoinGeckoService coinGeckoService;
    private static final Logger logger = LoggerFactory.getLogger(CoinController.class);

    @Autowired
    public CoinController(CoinService coinService, CoinGeckoService coinGeckoService) {
        this.coinService = coinService;
        this.coinGeckoService = coinGeckoService;
    }

    @GetMapping
    public List<Coin> getAllCoins() {
        logger.info("Fetching all coins");
        return coinService.getAllCoins();
    }

    @GetMapping("/{symbol}")
    public Coin getCoinBySymbol(@PathVariable String symbol) {
        logger.info("Get data from database for coin: {}", symbol);
        return coinService.getCoinBySymbol(symbol);
    }

    @PostMapping
    public Coin addCoin(@RequestBody Coin coin) {
        logger.info("Saving data to database for coin: {}", coin.getSymbol());
        return coinService.saveCoin(coin);
    }

    // Fetch coin from external API (CoinGecko) and return it asynchronously
    @GetMapping("/fetch/{symbol}")
    public Mono<Coin> fetchCoinFromExternalApi(@PathVariable String symbol) {
        System.out.println("Logger started controller fetch..");
        logger.info("Get data from api for coin: {}", symbol);
        return coinGeckoService.fetchCoinData(symbol);
    }
}
