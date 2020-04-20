package com.alexquasar.threeTasks.thirdTask.web.input;

import com.alexquasar.threeTasks.thirdTask.GeneratorUrlUtils;
import com.alexquasar.threeTasks.thirdTask.entity.Url;
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
    public void getDuplicatesTest() throws Exception {
        String getDuplicates = urlController + "/getDuplicates";

        long countUrl = 100_000_000L;
        int countDuplicatesInOneIteration = 10;
        int maxDuplicatesInOneIteration = 10;
        long countIteration = countUrl / maxSizeCollection;

        for (long i = 0; i < countIteration; i++) {
            List<Url> urls = generatorUrlUtils.generateUrls(maxSizeCollection, countDuplicatesInOneIteration, maxDuplicatesInOneIteration);
            urlRepository.saveAll(urls);
        }

        MvcResult result = mockMvc.perform(get(getDuplicates)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

        String content = result.getResponse().getContentAsString();
        List<String> duplicatesUrls = mapper.readValue(content, new TypeReference<List<String>>() {});

        long countDuplicates = countIteration * countDuplicatesInOneIteration;
        assertEquals(countDuplicates, duplicatesUrls.size());
    }
}