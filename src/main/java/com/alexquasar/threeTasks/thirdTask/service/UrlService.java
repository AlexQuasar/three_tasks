package com.alexquasar.threeTasks.thirdTask.service;

import com.alexquasar.threeTasks.thirdTask.entity.Url;
import com.alexquasar.threeTasks.thirdTask.entity.UrlDuplicate;
import com.alexquasar.threeTasks.thirdTask.repository.UrlDuplicateRepository;
import com.alexquasar.threeTasks.thirdTask.repository.UrlRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// TODO: Необходимо обработать большое количество URL(10_000_000_000), указать для каждого url, есть ли у него дубликаты.
//  а) Описать решение данной задачи.
//  б) Если получится, реализовать сервис для решения этой задачи.

// пробовал разные подходы для решения данной задачи:
//  1) коллекцию ArrayList для хранения URL не стал использовать, так как она может хранить всего в себе количество элементов максимум от Integer.
//      получаем OutOfMemoryError;
//  2) коллекции HashSet, TreeSet и HashMap не выдают OutOfMemoryError, но, начиная с 25_000_000 записей,
//      сильно увеличивается время добавление элементов.
//      при чем время добавления увелечивается не у каждого элемента, а у одного, примерно, на 100_000 записей,
//      и чем дальше, тем чаще встречаются такие элементы.
//      думал что причина в hashCode, но с TreeSet такая же проблема. найти причину такого поведения не удалось.
//      в итоге до 30_000_000 не доходило;
//  3) решил реализовать данную задачу через базу данных, где в базу, небольшими порциями, отдельно в разных таблицах,
//      записываются Url - в таблицу "url" только оригинальные, а в таблицу "url_duplicates" только дубликаты,
//      чтобы потом одним запросом выбрать все дубликаты.
//      тут столкнулся с проблемой, что происходит очень много примитивных запросов по поиску существующей записи в таблице,
//      что очень сильно нагружает систему - не разумное решение.
//      в итоге, даже с учетом большого количества простых запросов, результат не был получен;

@Service
public class UrlService {

    private UrlRepository urlRepository;
    private UrlDuplicateRepository urlDuplicateRepository;

    public UrlService(UrlRepository urlRepository, UrlDuplicateRepository urlDuplicateRepository) {
        this.urlRepository = urlRepository;
        this.urlDuplicateRepository = urlDuplicateRepository;
    }

    public void addUrl(String link) {
        if (!checkLinkDuplicate(link)) {
            urlRepository.save(new Url(link));
        } else {
            addLinkDuplicate(link);
        }
    }

    private Boolean checkLinkDuplicate(String link) {
        Url url = urlRepository.findByLink(link);
        return url != null;
    }

    private void addLinkDuplicate(String link) {
        UrlDuplicate url = urlDuplicateRepository.findByLink(link);
        if (url == null) {
            urlDuplicateRepository.save(new UrlDuplicate(link));
        }
    }

    public void addUrls(List<String> links) {
        List<Url> urls = new ArrayList<>();
        List<String> duplicateUrl = urlRepository.findAllDuplicates(links);
        for (String link : links) {
            Url url = new Url(link);
            if (urls.contains(url)) {
                if (!duplicateUrl.contains(link)) {
                    duplicateUrl.add(link);
                }
            } else if (!duplicateUrl.contains(link)) {
                urls.add(url);
            }
        }
        urlRepository.saveAll(urls);

        List<UrlDuplicate> duplicateUrls = new ArrayList<>();
        List<String> doubleDuplicatesUrl = urlDuplicateRepository.findAllDuplicates(duplicateUrl);
        for (String link : duplicateUrl) {
            UrlDuplicate urlDuplicate = new UrlDuplicate(link);
            if (!duplicateUrls.contains(urlDuplicate) && !doubleDuplicatesUrl.contains(link)) {
                duplicateUrls.add(urlDuplicate);
            }
        }
        urlDuplicateRepository.saveAll(duplicateUrls);
    }

    public List<UrlDuplicate> getDuplicatesUrls() {
        return urlDuplicateRepository.findAll();
    }
}
