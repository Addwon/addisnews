package com.week7challange.addisnews;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    User findById(long id);

    Long countByEmail(String email);
    Long countByUsername(String username);
    User findDistinctByRoles(String role);

}
