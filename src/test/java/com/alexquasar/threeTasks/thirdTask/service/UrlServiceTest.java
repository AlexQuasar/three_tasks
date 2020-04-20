package com.alexquasar.threeTasks.thirdTask.service;

import com.alexquasar.threeTasks.thirdTask.GeneratorUrlUtils;
import com.alexquasar.threeTasks.thirdTask.entity.Url;
import com.alexquasar.threeTasks.thirdTask.repository.UrlRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UrlServiceTest {

    @Mock
    UrlRepository urlRepository;

    @InjectMocks
    UrlService urlService;

    GeneratorUrlUtils generatorUrlUtils = new GeneratorUrlUtils();

    @Test
    public void getDuplicatesUrlsTest() {
        int countUrl = 100;
        int countDuplicates = 2;
        int maxDuplicates = 2;

        List<Url> urls = generatorUrlUtils.generateUrls(countUrl, countDuplicates, maxDuplicates);
        assertEquals(countUrl, urls.size());

        List<String> generatedDuplicatesUrls = new ArrayList<>();
        for (Url url : urls) {
            generatedDuplicatesUrls.add(url.getLink());
        }

        when(urlRepository.findAllDuplicates()).thenReturn(generatedDuplicatesUrls);

        List<String> duplicatesUrls = urlService.getDuplicatesUrls();

        assertNotNull(duplicatesUrls);
        assertNotEquals(0, duplicatesUrls.size());
        assertEquals(generatedDuplicatesUrls.size(), duplicatesUrls.size());
    }
}