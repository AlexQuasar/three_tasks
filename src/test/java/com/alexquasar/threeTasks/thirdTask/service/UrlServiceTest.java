package com.alexquasar.threeTasks.thirdTask.service;

import com.alexquasar.threeTasks.thirdTask.GeneratorUrlUtils;
import com.alexquasar.threeTasks.thirdTask.entity.Url;
import com.alexquasar.threeTasks.thirdTask.entity.UrlDuplicate;
import com.alexquasar.threeTasks.thirdTask.repository.UrlDuplicateRepository;
import com.alexquasar.threeTasks.thirdTask.repository.UrlRepository;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UrlServiceTest {

    @Mock
    UrlRepository urlRepository;

    @Mock
    UrlDuplicateRepository urlDuplicateRepository;

    @InjectMocks
    UrlService urlService;

    GeneratorUrlUtils generatorUrlUtils = new GeneratorUrlUtils();

    @Test
    public void addUrlTest() {
        String url = "google.com";

        when(urlRepository.findByLink(anyString())).thenReturn(null);
        when(urlDuplicateRepository.findByLink(anyString())).thenReturn(null);

        urlService.addUrl(url);

        verify(urlRepository).save(any(Url.class));
    }

    @Test
    public void getDuplicatesUrlsTest() {
        int countUrl = 100;
        int countDuplicates = 2;
        int maxDuplicates = 2;

        List<UrlDuplicate> urls =  new ArrayList<>();
        List<String> links = generatorUrlUtils.generateLinks(countUrl, countDuplicates, maxDuplicates);
        for (String link : links) {
            urls.add(new UrlDuplicate(link));
        }

        assertEquals(countUrl, urls.size());

        when(urlDuplicateRepository.findAll()).thenReturn(urls);

        List<UrlDuplicate> duplicatesUrls = urlService.getDuplicatesUrls();

        assertNotNull(duplicatesUrls);
        assertNotEquals(0, duplicatesUrls.size());
        assertEquals(urls.size(), duplicatesUrls.size());
    }

    @Test
    @SneakyThrows
    public void getDuplicatesLinksTest() {
        int countUrl = 100_000;
        int countDuplicates = 10;
        int maxDuplicates = 10;

        List<String> links = generatorUrlUtils.generateLinks(countUrl, countDuplicates, maxDuplicates);

        Set<String> duplicateLinks = urlService.getDuplicatesLinks(links);

        assertEquals(countDuplicates, duplicateLinks.size());
    }
}