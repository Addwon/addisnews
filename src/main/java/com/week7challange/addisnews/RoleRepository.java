package com.week7challange.addisnews;

import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role,Long> {
    Role findByRole(String role);
    Role findByUsers(User user);
}
