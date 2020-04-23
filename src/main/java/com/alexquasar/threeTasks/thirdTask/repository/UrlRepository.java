package com.alexquasar.threeTasks.thirdTask.repository;

import com.alexquasar.threeTasks.thirdTask.entity.Url;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UrlRepository extends CrudRepository<Url, Long> {

    Url findByLink(String link);
    List<Url> findAll();

//    @Query(value = "select link from url where link = any(select link::text from $1)", nativeQuery = true)
    @Query(value = "with all_links (link) as (values (:links)) " +
                   "select u.link from url u inner join all_links l on u.link = l.link",
                   nativeQuery = true)
    List<String> findAllDuplicates(List<String> links);

    @Query(value = "with all_links (new_link) as (values (:links)) " +
                   "insert into url (link) select l.new_link from all_links l " +
                   "on conflict (link) do nothing returning link",
                   nativeQuery = true)
    String saveAllWithDuplicates(List<String> links);

    @Query(value = //"with all_links (new_link) as (values (:links)) " +
                   "insert into url (link) select unnest(:links)" +
                   "on conflict (link) do nothing returning link",
                   nativeQuery = true)
    String saveAllWithDuplicate(List<String> links);
}
