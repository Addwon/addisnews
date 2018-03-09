package com.week7challange.addisnews;

import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category,Long>{
    //Category findByTopicofInterest(String topicofInterest);
}
