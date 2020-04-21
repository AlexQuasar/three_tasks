package com.alexquasar.threeTasks.thirdTask.service;

import com.alexquasar.threeTasks.thirdTask.entity.Url;
import com.alexquasar.threeTasks.thirdTask.entity.UrlDuplicates;
import com.alexquasar.threeTasks.thirdTask.repository.UrlDuplicatesRepository;
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
    private UrlDuplicatesRepository urlDuplicatesRepository;

    public UrlService(UrlRepository urlRepository, UrlDuplicatesRepository urlDuplicatesRepository) {
        this.urlRepository = urlRepository;
        this.urlDuplicatesRepository = urlDuplicatesRepository;
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
        UrlDuplicates url = urlDuplicatesRepository.findByLink(link);
        if (url == null) {
            urlDuplicatesRepository.save(new UrlDuplicates(link));
        }
    }

    public void addUrls(List<String> links) {
        List<Url> urls = new ArrayList<>();

        for (String link : links) {
            Url url = new Url(link);
            if (!urls.contains(url) && !checkLinkDuplicate(link)) {
                urls.add(url);
            } else {
                addLinkDuplicate(link);
            }
        }

        urlRepository.saveAll(urls);
    }

    public List<UrlDuplicates> getDuplicatesUrls() {
        return urlDuplicatesRepository.findAll();
    }
}
