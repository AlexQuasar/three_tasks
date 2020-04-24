package com.alexquasar.threeTasks.thirdTask.repository;

import com.alexquasar.threeTasks.thirdTask.entity.UrlDuplicate;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UrlDuplicateRepository extends CrudRepository<UrlDuplicate, Long> {

    UrlDuplicate findByLink(String link);
    List<UrlDuplicate> findAll();
}
