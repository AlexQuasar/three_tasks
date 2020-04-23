package com.alexquasar.threeTasks.thirdTask.web.input;

import com.alexquasar.threeTasks.thirdTask.GeneratorUrlUtils;
import com.alexquasar.threeTasks.thirdTask.entity.UrlDuplicate;
import com.alexquasar.threeTasks.thirdTask.repository.UrlRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UrlRestControllerTest {

    MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    UrlRepository urlRepository;

    @Autowired
    ObjectMapper mapper;

    Logger log = Logger.getLogger(UrlRestControllerTest.class.getName());
    String urlController = "/urlController";

    GeneratorUrlUtils generatorUrlUtils = new GeneratorUrlUtils();
    int maxSizeCollection = 1_000_000;

    @Before
    public void setUp() throws Exception {
        ConfigurableMockMvcBuilder builder =
                MockMvcBuilders.webAppContextSetup(this.webApplicationContext);
        this.mockMvc = builder.build();
    }

    @Test
    @Transactional
    public void addUrlTest() throws Exception {
        String addUrl = urlController + "/addUrl";

        int expectedVisitsSize = urlRepository.findAll().size() + 1;

        mockMvc.perform(post(addUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(addUrl)))
        .andExpect(status().isOk());

        assertEquals(expectedVisitsSize, urlRepository.findAll().size());
    }

    @Test
    @Transactional
    public void addUrlsTest() throws Exception {
        String addUrls = urlController + "/addUrls";

        int countUrl = 10;
        int countDuplicates = 0;
        int maxDuplicates = 0;

        List<String> urls = generatorUrlUtils.generateLinks(countUrl, countDuplicates, maxDuplicates);
        int expectedVisitsSize = urlRepository.findAll().size() + urls.size();

        mockMvc.perform(post(addUrls)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(urls)))
        .andExpect(status().isOk());

        assertEquals(expectedVisitsSize, urlRepository.findAll().size());
    }

    @Test
    @Transactional
    public void getDuplicatesTest() throws Exception {
        String addUrls = urlController + "/addUrls";
        String getDuplicates = urlController + "/getDuplicates";

        // 10_000 - 5 sec // < 1 sec
        // 100_000 - 35 sec // 9 sec
        // 1_000_000 - 13 min // 1 min 37 sec
        // 10_000_000 - ??? > 2 h // 14 min 45 sec
        long countUrl = 10_000_000L;//10_000_000_000L;
        maxSizeCollection = 10_000;
        int countDuplicatesInOneIteration = 2;
        int maxDuplicatesInOneIteration = 2;
        long countIteration = countUrl / maxSizeCollection;

        for (long i = 0; i < countIteration; i++) {
            List<String> urls = generatorUrlUtils.generateLinks(maxSizeCollection, countDuplicatesInOneIteration, maxDuplicatesInOneIteration);
            mockMvc.perform(post(addUrls)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(urls)))
            .andExpect(status().isOk());
        }

        MvcResult result = mockMvc.perform(get(getDuplicates)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

        String content = result.getResponse().getContentAsString();
        List<UrlDuplicate> duplicatesUrls = mapper.readValue(content, new TypeReference<List<UrlDuplicate>>() {});

        long countDuplicates = countIteration * countDuplicatesInOneIteration;
        assertEquals(countDuplicates, duplicatesUrls.size());
    }
}