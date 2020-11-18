package com.scraper.engine.impl;

import com.scraper.engine.Scraper;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class DefaultScraper implements Scraper {

    private final Scraper bestbuyScraper;
    private final Scraper neweggScraper;

    public DefaultScraper(Scraper bestbuyScraper, Scraper neweggScraper) {
        this.bestbuyScraper = bestbuyScraper;
        this.neweggScraper = neweggScraper;
    }

    @Override
    public List<String> scrape(String uri, boolean inStock) throws IOException {
        if (uri.contains("newegg")) {
            return neweggScraper.scrape(uri, inStock);
        } else if (uri.contains("bestbuy")) {
            return bestbuyScraper.scrape(uri, inStock);
        }
        return Collections.emptyList();
    }
}
