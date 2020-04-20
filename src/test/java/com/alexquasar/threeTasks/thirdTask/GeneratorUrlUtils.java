package com.alexquasar.threeTasks.thirdTask;

import com.alexquasar.threeTasks.thirdTask.entity.Url;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GeneratorUrlUtils {

    private List<String> sites = Arrays.asList(
            "google.com/home", "wiki.org/home" , "youtube.com/home", "yandex.ru/home", "habr.com/home",
            "stackoverflow.com/home", "spring.io/home", "github.com/home", "codeflow.site/home", "codegym.cc/home");

    private Random random = new Random();

    public List<Url> generateUrls(int countUrl, int countDuplicates, int maxDuplicates) {
        List<Url> urls = new ArrayList<>();

        int countUrlWithoutDuplicates = countUrl - countDuplicates * maxDuplicates;

        int sizeSites = sites.size();
        for (int i = 0; i < countUrlWithoutDuplicates; i++) {
            int index = random.nextInt(sizeSites);
            urls.add(new Url(sites.get(index) + i));
        }

        int urlsSize = urls.size();
        for (int i = 0; i < countDuplicates; i++) {
            int index = random.nextInt(urlsSize);
            String site = urls.get(index).getLink();
            for (int j = 0; j < maxDuplicates; j++) {
                urls.add(new Url(site + "_" + site + i));
            }
        }

        return urls;
    }
}
