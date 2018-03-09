package com.week7challange.addisnews;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TopicRepository extends CrudRepository<Topic,Long>{
    List<Topic> findByUser(User user);
}
