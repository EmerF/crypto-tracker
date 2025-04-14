package dev.portfolio.cryptotracker.service;

import dev.portfolio.cryptotracker.model.Coin;
import dev.portfolio.cryptotracker.repository.CoinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoinService {

    private final CoinRepository coinRepository;

    @Autowired
    public CoinService(CoinRepository coinRepository) {
        this.coinRepository = coinRepository;
    }

    public List<Coin> getAllCoins() {
        return coinRepository.findAll();
    }

    public Coin getCoinBySymbol(String symbol) {
        return coinRepository.findById(symbol).orElse(null);
    }

    public Coin saveCoin(Coin coin) {
        return coinRepository.save(coin);
    }

}
