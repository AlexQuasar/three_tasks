package com.alexquasar.threeTasks.dto;

import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class UrlParser {

    @SneakyThrows
    public Map<String, Long> groupUrl(List<String> urls) {
        Map<String, Long> groupedUrls = new ConcurrentHashMap<>();

        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(cores);
        List<Callable<Object>> tasks = new ArrayList<>();

        int splitStep = urls.size() / cores;

        int min = 0;
        for (int i = 1; i <= cores; i++) {
            if (i == cores) {
                splitStep = urls.size() - min;
            }
            List<String> urlsPart = urls.stream()
                    .skip(min)
                    .limit(splitStep)
                    .collect(Collectors.toCollection(LinkedList::new));
            min = splitStep * i;

            DuplicateParser duplicateParser = new DuplicateParser(groupedUrls, urlsPart);
            Callable<Object> callable = Executors.callable(duplicateParser);
            tasks.add(callable);
        }
        executorService.invokeAll(tasks);
        executorService.shutdown();

        return groupedUrls;
    }
}
