package com.alexquasar.threeTasks.service;

import com.alexquasar.threeTasks.GeneratorUrlUtils;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

public class ParserServiceTest {

    ParserService parserService = new ParserService();
    GeneratorUrlUtils generatorUrlUtils = new GeneratorUrlUtils();

    @Test
    public void getDuplicatesUrlsTest() {
        long countUrl = 1000000L;
        long countDuplicates = 100L;
        long maxDuplicates = 10L;

        List<String> urls = generatorUrlUtils.generateUrls(countUrl, countDuplicates, maxDuplicates);

        assertEquals(countUrl, urls.size());

        Map<String, Long> groupedUrls = parserService.getDuplicatesUrls(urls);

        Optional<Long> max = groupedUrls.values().stream().max(Long::compareTo);
        assertEquals(maxDuplicates, max.get().longValue());
        Optional<Long> min = groupedUrls.values().stream().min(Long::compareTo);
        assertEquals(maxDuplicates, min.get().longValue());

        assertEquals(countDuplicates, groupedUrls.size());
    }
}