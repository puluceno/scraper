package com.scraper.engine;

import java.io.IOException;
import java.util.List;

public interface Scraper {

    List<String> scrape(String uri, boolean inStock);

    List<String> scrape(List<String> uris, boolean inStock);
}
