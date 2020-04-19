package com.alexquasar.threeTasks.dto;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class DuplicateParser implements Runnable {

    private Map<String, Long> groupedUrls;
    private List<String> urls;

    @Override
    public void run() {
        Map<String, Long> localGroupedUrls = urls.stream().collect(Collectors.groupingBy(i -> i, Collectors.counting()));
        for (Map.Entry<String, Long> entry : localGroupedUrls.entrySet()) {
            addInMap(entry.getKey(), entry.getValue());
        }
    }

    private void addInMap(String key, Long value) {
        groupedUrls.merge(key, value, Long::sum);
    }
}
