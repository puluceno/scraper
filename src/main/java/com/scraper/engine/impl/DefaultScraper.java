package com.scraper.engine.impl;

import com.scraper.engine.Scraper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DefaultScraper implements Scraper {
    private static final Logger logger = Logger.getLogger(DefaultScraper.class.getName());
    private Scraper bestbuyScraper;
    private Scraper neweggScraper;
    private Scraper bhphotoScraper;

    public DefaultScraper() {
    }

    public DefaultScraper(Scraper bestbuyScraper, Scraper neweggScraper, Scraper bhphotoScraper) {
        this.bestbuyScraper = bestbuyScraper;
        this.neweggScraper = neweggScraper;
        this.bhphotoScraper = bhphotoScraper;
    }

    @Override
    public List<String> scrape(String uri, boolean inStock) {
        try {
            if (uri.contains("newegg")) {
                return neweggScraper.scrape(uri, inStock);
            } else if (uri.contains("bestbuy")) {
                return bestbuyScraper.scrape(uri, inStock);
            } else if (uri.contains("bhphotovideo")) {
                return bhphotoScraper.scrape(uri, inStock);
            }
        } catch (Exception e) {
            logger.warning("Scrape Error detected: " + e.toString() + "\r\n         Stacktrace: " + Arrays.toString(e.getStackTrace()));
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> scrape(List<String> uris, boolean inStock) {
        return uris.parallelStream().map(uri -> {
            if (uri.contains("newegg")) {
                return neweggScraper.scrape(uri, inStock);
            } else if (uri.contains("bestbuy")) {
                return bestbuyScraper.scrape(uri, inStock);
            } else if (uri.contains("bhphotovideo")) {
                return bhphotoScraper.scrape(uri, inStock);
            }
            return Collections.<String>emptyList();
        }).flatMap(listContainer -> listContainer.stream()).collect(Collectors.toList());
    }
}
