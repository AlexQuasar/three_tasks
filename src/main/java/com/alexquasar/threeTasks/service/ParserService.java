package com.alexquasar.threeTasks.service;

import com.alexquasar.threeTasks.dto.UrlParser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// TODO: Необходимо обработать большое количество URL(10_000_000_000), указать для каждого url, есть ли у него дубликаты.
//  а) Описать решение данной задачи.
//  б) Если получится, реализовать сервис для решения этой задачи.

@Service
public class ParserService {

    public Map<String, Long> getDuplicatesUrls(List<String> urls) {
        UrlParser urlParser = new UrlParser();
        Map<String, Long> groupedUrl = urlParser.groupUrl(urls);

        return groupedUrl.entrySet().stream()
                .filter(i -> i.getValue() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
