package dev.portfolio.cryptotracker.controller;

import dev.portfolio.cryptotracker.model.Coin;
import dev.portfolio.cryptotracker.service.CoinGeckoService;
import dev.portfolio.cryptotracker.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/coins")
public class CoinController {

    private final CoinService coinService;
    private final CoinGeckoService coinGeckoService;

    @Autowired
    public CoinController(CoinService coinService, CoinGeckoService coinGeckoService) {
        this.coinService = coinService;
        this.coinGeckoService = coinGeckoService;
    }

    @GetMapping
    public List<Coin> getAllCoins() {
        return coinService.getAllCoins();
    }

    @GetMapping("/{symbol}")
    public Coin getCoinBySymbol(@PathVariable String symbol) {
        return coinService.getCoinBySymbol(symbol);
    }

    @PostMapping
    public Coin addCoin(@RequestBody Coin coin) {
        return coinService.saveCoin(coin);
    }

    // Fetch coin from external API (CoinGecko) and return it asynchronously
    @GetMapping("/fetch/{symbol}")
    public Mono<Coin> fetchCoinFromExternalApi(@PathVariable String symbol) {
        return coinGeckoService.fetchCoinData(symbol);
    }
}
