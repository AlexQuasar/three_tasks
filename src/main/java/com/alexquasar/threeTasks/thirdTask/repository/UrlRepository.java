package com.alexquasar.threeTasks.thirdTask.repository;

import com.alexquasar.threeTasks.thirdTask.entity.Url;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UrlRepository extends CrudRepository<Url, Long> {

    @Query(value = "select u.link from url u group by u.link having count(u.link) > 1", nativeQuery = true)
    List<String> findAllDuplicates();
}
