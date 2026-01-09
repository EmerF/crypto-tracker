package dev.portfolio.cryptotracker.application.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class CoinGeckoMarketChartResponse {
    private List<List<Double>> prices;
    private List<List<Double>> market_caps;
    private List<List<Double>> total_volumes;
}
