package com.alexquasar.threeTasks.thirdTask.repository;

import com.alexquasar.threeTasks.thirdTask.entity.UrlDuplicates;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UrlDuplicatesRepository extends CrudRepository<UrlDuplicates, Long> {

    UrlDuplicates findByLink(String link);
    List<UrlDuplicates> findAll();
}
