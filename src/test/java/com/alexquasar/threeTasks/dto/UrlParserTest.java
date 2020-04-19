package com.alexquasar.threeTasks.dto;

import com.alexquasar.threeTasks.GeneratorUrlUtils;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class UrlParserTest {

    UrlParser urlParser = new UrlParser();

    GeneratorUrlUtils generatorUrlUtils = new GeneratorUrlUtils();

    @Test
    public void groupUrlTest() {
        long countUrl = 10000000L;
        long countDuplicates = 100L;
        long maxDuplicates = 10L;
        long endCountUrl = countDuplicates + countUrl - countDuplicates * maxDuplicates;

        List<String> urls = generatorUrlUtils.generateUrls(countUrl, countDuplicates, maxDuplicates);

        assertEquals(countUrl, urls.size());

        Map<String, Long> groupedUrls = urlParser.groupUrl(urls);

        assertEquals(endCountUrl, groupedUrls.size());

        long countDuplicatesInMap = groupedUrls.values().stream().filter(i -> i == maxDuplicates).count();
        assertEquals(countDuplicates, countDuplicatesInMap);
    }

    @Test
    public void benchmark() {
        long countUrl = 100000000L;
        long countDuplicates = 100L;
        long maxDuplicates = 10L;

        LocalDateTime start = LocalDateTime.now();
        List<String> urls = generatorUrlUtils.generateUrls(countUrl, countDuplicates, maxDuplicates);
        System.out.println(Duration.between(start, LocalDateTime.now()).getSeconds());
    }
}