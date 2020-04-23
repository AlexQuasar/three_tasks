package com.alexquasar.threeTasks.thirdTask.service;

import com.alexquasar.threeTasks.thirdTask.entity.Url;
import com.alexquasar.threeTasks.thirdTask.entity.UrlDuplicate;
import com.alexquasar.threeTasks.thirdTask.repository.UrlDuplicateRepository;
import com.alexquasar.threeTasks.thirdTask.repository.UrlRepository;
import org.springframework.dao.DataIntegrityViolationException;
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
    private EntityManagerFactory entityManagerFactory;

    public UrlService(UrlRepository urlRepository, UrlDuplicateRepository urlDuplicateRepository, EntityManagerFactory entityManagerFactory) {
        this.urlRepository = urlRepository;
        this.urlDuplicateRepository = urlDuplicateRepository;
        this.entityManagerFactory = entityManagerFactory;
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
//        EntityManagerFactory entityManagerFactory =
//                Persistence.createEntityManagerFactory(Url.class.getName());
        EntityManager entityManager = entityManagerFactory.createEntityManager();

//        List<Url> urls = new ArrayList<>();
//        List<String> duplicateUrl = urlRepository.findAllDuplicates(links);
        StringBuilder query = new StringBuilder("insert into url (link) values ");
        int collectionIteration = links.size();
        for (int i = 0; i < collectionIteration; i++) {
//            Url url = new Url(link);
//            if (!urls.contains(url)) {
//                if (!duplicateUrl.contains(link)) {
//                    duplicateUrl.add(link);
//                }
//            } else if (!duplicateUrl.contains(link)) {
//                urls.add(url);
//            }
            query.append("('").append(links.get(i)).append("')").append(i == collectionIteration - 1 ? " " : ", ");
        }
        query.append("on conflict (link) do nothing returning link");


//        entityManager.getTransaction().begin();
//        urls.forEach(i -> entityManager.persist(i));
//        List<Url> resultList = entityManager.createQuery(query.toString()).getResultList();
//        entityManager.getTransaction().commit();
        try {
            addDupl(query.toString());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

//        String savedUrls = urlRepository.saveAllWithDuplicates(links);
//        List<String> savedLinks = Arrays.asList(savedUrls.split(","));
//        List<String> duplicateLinks = links.stream().filter(i -> !savedLinks.contains(i)).collect(Collectors.toList());
//        urlDuplicateRepository.saveAllWithDuplicates(duplicateLinks);

//        List<UrlDuplicate> duplicateUrls = new ArrayList<>();
//        List<String> doubleDuplicatesUrl = urlDuplicateRepository.findAllDuplicates(duplicateUrl);
//        for (String link : duplicateUrl) {
//            UrlDuplicate urlDuplicate = new UrlDuplicate(link);
//            if (!duplicateUrls.contains(urlDuplicate) && !doubleDuplicatesUrl.contains(link)) {
//                duplicateUrls.add(urlDuplicate);
//            }
//        }
//        urlDuplicateRepository.saveAll(duplicateUrls);
    }

//    private String getLinkFromMessage(String messageException) {
//        String beginSubString = "Key (link)=(";
//        String endSubString = ") already exists";
//        int beginIndex = messageException.indexOf(beginSubString) + beginSubString.length();
//        int endIndex = messageException.indexOf(endSubString);
//
//        return messageException.substring(beginIndex, endIndex);
//    }

    public List<UrlDuplicate> getDuplicatesUrls() {
        return urlDuplicateRepository.findAll();
    }

    private void addDupl(String query) throws SQLException {
        /**
         * эта строка загружает драйвер DB.
         * раскомментируйте если прописываете драйвер вручную
         */
        //Class.forName("com.mysql.jdbc.Driver");
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/postgres",
                    "postgres", "toor");

            if (conn == null) {
                System.out.println("Нет соединения с БД!");
                System.exit(0);
            }

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                System.out.println(rs.getRow() + ". " + rs.getString("link")
                        + "\t" + rs.getString("link"));
            }

            /**
             * stmt.close();
             * При закрытии Statement автоматически закрываются
             * все связанные с ним открытые объекты ResultSet
             */
            stmt.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally{
            if (conn != null){
                conn.close();
            }
        }
    }
}
