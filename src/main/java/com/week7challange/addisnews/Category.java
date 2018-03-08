package com.week7challange.addisnews;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

//    private List<String> newsCategories;

    @ManyToMany(mappedBy="categories",fetch=FetchType.LAZY)
    private Collection<User> users;

    public Category() {
    }

//    public Category(List<String> category) {
//        this.category = category;
//    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
//
//    public List<String> getCategory() {
//        return category;
//    }
//
//    public void setCategory(List<String> category) {
//        this.category = category;
//    }

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }
}
