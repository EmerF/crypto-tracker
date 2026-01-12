package dev.portfolio.cryptotracker.adapter.out.binance;

import dev.portfolio.cryptotracker.adapter.common.AbstractCryptoDataFetcher;
import dev.portfolio.cryptotracker.domain.model.Coin;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BinanceFetcher extends AbstractCryptoDataFetcher {
    public BinanceFetcher(RestTemplate rest) {
        super(rest);
    }

    @Override
    public String getExchange() {
        return "";
    }

    @Override
    public List<Coin> fetchData() {
        BinanceTicker[] resp = get("https://api.binance.com/api/v3/ticker/price", BinanceTicker[].class);
        if (resp == null) return List.of();
        return Arrays.stream(resp)
                .map(t -> new Coin(t.symbol, t.price))
                .collect(Collectors.toList());
    }

    static class BinanceTicker {
        public String symbol;
        public String price;
    }
}
