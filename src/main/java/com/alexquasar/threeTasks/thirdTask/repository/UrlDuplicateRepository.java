package com.alexquasar.threeTasks.thirdTask.repository;

import com.alexquasar.threeTasks.thirdTask.entity.UrlDuplicate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UrlDuplicateRepository extends CrudRepository<UrlDuplicate, Long> {

    UrlDuplicate findByLink(String link);
    List<UrlDuplicate> findAll();

    @Query(value = "select link from url_duplicates where link in :links", nativeQuery = true)
    List<String> findAllDuplicates(List<String> links);
}
