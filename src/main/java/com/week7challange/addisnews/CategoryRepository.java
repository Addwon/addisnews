package com.week7challange.addisnews;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category,Long>{
    //Category findByTopicofInterest(String topicofInterest);
    List<Category> findByUsers(User user);

}
