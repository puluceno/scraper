package com.scraper;

import com.scraper.engine.Scraper;
import com.scraper.engine.impl.BestbuyScraper;
import com.scraper.engine.impl.BhPhotoScraper;
import com.scraper.engine.impl.DefaultScraper;
import com.scraper.engine.impl.NeweggScraper;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static java.lang.Thread.sleep;

public class Runner {

    private static BufferedInputStream bufferedInputStream;
    private static AudioInputStream audioInputStream;
    private static FileHandler fh;
    private static final Logger logger = Logger.getLogger("pooplog");

    public static void main(String[] args) {
        List<String> scrape3080;
        List<String> scrape6800xt;
        List<String> scrapeCpu;
        List<String> scrapePs5;

        Scraper scraper = new DefaultScraper(new BestbuyScraper(), new NeweggScraper(), new BhPhotoScraper());

        try {
            fh = new FileHandler("poop.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            View view = new View();

            while (true) {
                logger.info("Scraping... ");
                List<String> urls = view.getUrls();
                if (view.inStock) {
                    scrape3080 = scraper.scrape(Arrays.asList(urls.get(0), urls.get(4)), false);

                    scrape6800xt = scraper.scrape(Arrays.asList(urls.get(1), urls.get(5)), false);

                    scrapeCpu = scraper.scrape(Arrays.asList(urls.get(2), urls.get(6), urls.get(10)), false);

                    scrapePs5 = scraper.scrape(Arrays.asList(urls.get(3), urls.get(7), urls.get(11)), false);

                    startAlert(view, scrape3080, scrape6800xt, scrapeCpu, scrapePs5);
                } else {
                    scrape3080 = scraper.scrape(Arrays.asList(urls.get(0), urls.get(4)), true);

                    scrape6800xt = scraper.scrape(Arrays.asList(urls.get(1), urls.get(5)), true);

                    scrapeCpu = scraper.scrape(Arrays.asList(urls.get(2), urls.get(6), urls.get(10)), true);

                    scrapePs5 = scraper.scrape(Arrays.asList(urls.get(3), urls.get(7), urls.get(11)), true);
                }
                view.set3080Content(scrape3080);
                view.set6800xtContent(scrape6800xt);
                view.setCpuContent(scrapeCpu);
                view.setPs5Content(scrapePs5);
                sleep(5000);
            }
        } catch (Exception e) {
            logger.warning("Main Error detected: " + e.toString() + "\r\n         Stacktrace: " + Arrays.toString(e.getStackTrace()));
        }
    }

    private static void startAlert(View view, List<String>... scrapeResult) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        for (List<String> result : scrapeResult) {
            for (String item : result) {
                logger.info(item.split("‽")[0] + " IN STOCK\r\n      " + item.split("‽")[1]);
            }
            if (!view.muteSound && !result.isEmpty()) {
                bufferedInputStream = new BufferedInputStream(Objects.requireNonNull(Runner.class.getClassLoader().getResourceAsStream("alert.wav")));
                audioInputStream = AudioSystem.getAudioInputStream(bufferedInputStream);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            }
        }
    }

    public static void closer() {
        try {
            fh.close();
            if (bufferedInputStream != null)
                bufferedInputStream.close();
            if (audioInputStream != null)
                audioInputStream.close();
        } catch (IOException e) {
            // Do nothing
        }
    }
}
