package com.week7challange.addisnews;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    /*@ManyToMany(mappedBy="categories",fetch=FetchType.LAZY)
    private Collection<User> users;*/
    @Column(name="newsCategory")
    private String newsCategory;

    @ManyToMany
    private List<User> users;

    public Category() {
        users=new ArrayList<>();
    }

    public void addUser(User user){

        users.add(user);
    }

    public Category(List<User> users) {
        this.users = users;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNewsCategory() {
        return newsCategory;
    }

    public void setNewsCategory(String newsCategory) {
        this.newsCategory = newsCategory;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

/*public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }*/
}
