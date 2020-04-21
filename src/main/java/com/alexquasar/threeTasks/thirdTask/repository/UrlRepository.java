package com.alexquasar.threeTasks.thirdTask.repository;

import com.alexquasar.threeTasks.thirdTask.entity.Url;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UrlRepository extends CrudRepository<Url, Long> {

    Url findByLink(String link);
    List<Url> findAll();

    @Query(value = "select link from url where link in :links", nativeQuery = true)
    List<String> findAllDuplicates(List<String> links);
}
