package com.alexquasar.threeTasks.thirdTask.service;

import com.alexquasar.threeTasks.thirdTask.entity.Url;
import com.alexquasar.threeTasks.thirdTask.entity.UrlDuplicate;
import com.alexquasar.threeTasks.thirdTask.exception.ServiceException;
import com.alexquasar.threeTasks.thirdTask.repository.UrlDuplicateRepository;
import com.alexquasar.threeTasks.thirdTask.repository.UrlRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.orm.hibernate5.HibernateOperations;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        if (links.isEmpty()) {
            throw new ServiceException("list is empty", HttpStatus.NO_CONTENT);
        }

        try {
            saveAll(links);
        } catch (SQLException ex) {
            throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private void saveAll(List<String> links) throws SQLException {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/postgres",
                    "postgres", "toor");

            if (connection == null) {
                System.out.println("Нет соединения с БД!");
                System.exit(0);
            }

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getQuery_AddWithDuplicates(links, "url"));

            List<String> savedLinks = new ArrayList<>();
            while (resultSet.next()) {
                savedLinks.add(resultSet.getString("link"));
            }
            List<String> duplicateLinks = links.stream().filter(i -> !savedLinks.contains(i)).collect(Collectors.toList());
            links.stream()
                    .collect(Collectors.groupingBy(i -> i, Collectors.counting()))
                        .entrySet().stream().filter(i -> i.getValue() > 1)
                        .forEach(i -> duplicateLinks.add(i.getKey()));

            if (!duplicateLinks.isEmpty()) {
                statement.executeQuery(getQuery_AddWithDuplicates(duplicateLinks, "url_duplicates"));
            }

            statement.close();
        }
        catch (SQLException ex) {
            throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } finally{
            if (connection != null){
                connection.close();
            }
        }
    }

    private String getQuery_AddWithDuplicates(List<String> links, String tableName) {
        StringBuilder query = new StringBuilder("insert into " + tableName + " (link) values ");
        int collectionIteration = links.size();
        for (int i = 0; i < collectionIteration; i++) {
            query.append("('").append(links.get(i)).append("')").append(i == collectionIteration - 1 ? " " : ", ");
        }
        query.append("on conflict (link) do nothing returning link");

        return query.toString();
    }

    public List<UrlDuplicate> getDuplicatesUrls() {
        return urlDuplicateRepository.findAll();
    }
}
