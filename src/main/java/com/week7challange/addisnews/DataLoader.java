package com.week7challange.addisnews;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    TopicRepository topicRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public void run(String... strings) throws Exception{

       System.out.println("Loading data . . .");
       roleRepository.save(new Role("USER"));
       roleRepository.save(new Role("ADMIN"));

        Role adminRole=roleRepository.findByRole("ADMIN");
        Role userRole=roleRepository.findByRole("USER");

        User user1=new User("admin@admin.com","password","Dave","Wolf",true,"Dave");
        user1.setRoles(Arrays.asList(adminRole));
        userRepository.save(user1);

        User user2=new User("user@user.com","password","Addis","Wondie",true,"Addis");
        user2.setRoles(Arrays.asList(userRole));
        userRepository.save(user2);

        User user3 = new User("bob@bob.com", "password", "Bob", "Marley", true, "Bob");
        user3.setRoles(Arrays.asList(userRole));
        userRepository.save(user3);

        Topic topic=new Topic();
        topic.setTopictext("Apple");
        topic.setUser(user2);
        topicRepository.save(topic);

        Category category=new Category();
        category.setNewsCategory("technology");
        category.addUser(user2);
        categoryRepository.save(category);

        topic=new Topic();
        topic.setTopictext("Car");
        topic.setUser(user3);
        topicRepository.save(topic);

        category=new Category();
        category.setNewsCategory("sports");
        category.addUser(user3);
        categoryRepository.save(category);
    }
}
