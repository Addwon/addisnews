package com.week7challange.addisnews;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

@Controller
public class MainController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    TopicRepository topicRepository;

    /*@Autowired
    SourceRepository sourceRepository;
*/
    @RequestMapping("/login")
    public String login(){return "login";}

    @RequestMapping("/")
    public String showIndex(Model model)
    {
        RestTemplate restTemplate=new RestTemplate();
        String url="https://newsapi.org/v2/everything?q=bitcoin&apiKey=cdcff7c00ad446bd9fd970620d96b155";
        String testUrl="https://newsapi.org/v2/top-headlines?sources=abc-news&apiKey=cdcff7c00ad446bd9fd970620d96b155";
        News news=restTemplate.getForObject(testUrl,News.class);

        //model.addAttribute("author",news.getTotalResults());
        model.addAttribute("articles",news.getArticles());
       return "index";
    }

    //User registration
    @RequestMapping(value="/registration",method= RequestMethod.GET)
    public String showRegistrationPage(Model model){
        model.addAttribute("user",new User());
        return "registration";
    }
    @RequestMapping(value="/registration",method= RequestMethod.POST)
    public String processRegistrationPage(@Valid @ModelAttribute("user") User user, BindingResult result, Model model){

        if(result.hasErrors()){
            return "registration";
        }else{
            user.setEnabled(true);
            userRepository.save(user);
            return "redirect:/";
        }

    }

    @GetMapping("/addcategory")
    public String addCategory(Model model) {
        String url2="https://newsapi.org/v2/sources?apiKey=cdcff7c00ad446bd9fd970620d96b155";
        RestTemplate restTemplate = new RestTemplate();
        NewsAgencies newsAgencies = restTemplate.getForObject(url2, NewsAgencies.class);
        List<Source> sources =  newsAgencies.getSources();
        Set<String> categories = new HashSet<>();
        for (Source source :
                sources) {
            categories.add(source.getCategory());
            System.out.println("GettMapping, source.getName():"+source.getName());
        }

        model.addAttribute("categories", categories);
        model.addAttribute("category", new Category());
        
        return "addcategory";
    }


    @PostMapping("/addcategory")
    public String addCategory(Category category, BindingResult result, Model model,Source source, NewsAgencies newsAgencies,Authentication auth, HttpServletRequest request ){

        if (result.hasErrors()) {
            return "addcategory";
        }
        User user = userRepository.findByUsername(auth.getName());
        category.addUser(user);
        System.out.println("PostMapping, source.getName():"+source.getName());
        //category.setNewsCategory(source.getName());
        categoryRepository.save(category);
        //sourceRepository.save(newsAgencies.getSources());
        return "redirect:/categorylist";
    }
    //Show selected news
    @RequestMapping("/selectednews")
    public String showSelectedNews(Model model,Category category,Source source)
    {
        //source.setId(category.);
        RestTemplate restTemplate=new RestTemplate();
        String url3="https://newsapi.org/v2/sources?apiKey=cdcff7c00ad446bd9fd970620d96b155";
        String url4="https://newsapi.org/v2/top-headlines?sources=bbc-news&apiKey=cdcff7c00ad446bd9fd970620d96b155";
        News news=restTemplate.getForObject(url4,News.class);

        NewsAgencies newsAgencies=restTemplate.getForObject(url3,NewsAgencies.class);

       model.addAttribute("source",newsAgencies.getSources());
        model.addAttribute("articles",news.getArticles());
        return "selectednews";
    }

    //Post jobs
    @GetMapping("/addtopic")
    public String addTopic(Model model){
        model.addAttribute("topic",new Topic());
        return "addtopic";
    }
    @PostMapping("/addtopic")
    public String addTopicForm(@RequestParam String newsCategory,@Valid @ModelAttribute("topic") Topic topic, BindingResult result,Model model,Principal principal,
                              RedirectAttributes redirectAttributes){
        if(result.hasErrors()){
            return "addtopic";
        }
        else{
            User user=userRepository.findByUsername(principal.getName());
            topic.setUser(user);
            topicRepository.save(topic);
            System.out.println("from postmaping "+topic.getTopictext());
            System.out.println("from postmaping requestparam string "+newsCategory);
            return "redirect:/";
        }
    }
    //list topic
    @RequestMapping("/topiclist")
    public String ShowTopic(Model model){
        model.addAttribute("topic",topicRepository.findAll());
        return"topiclist";
    }

    @RequestMapping("/showtopic/{id}")
    public String showTopicDetail(@PathVariable("id") long id, Model model){
        Topic topic=topicRepository.findOne(id);
        System.out.println("newsTopic"+topic.getTopictext());
        String newsURL="https://newsapi.org/v2/everything?q="+topic.getTopictext()+"&sortBy=publishedAt&apiKey=cdcff7c00ad446bd9fd970620d96b155";
        RestTemplate restTemplate=new RestTemplate();
        News news=restTemplate.getForObject(newsURL,News.class);
        model.addAttribute("articles",news.getArticles());
        return "showtopic";
    }

    //show category
    @RequestMapping("/categorylist")
    public String ShowCategory(Model model){
        model.addAttribute("category",categoryRepository.findAll());
        return"categorylist";
    }
    @RequestMapping("/showcategory/{id}")
    public String showCategoryDetail(@PathVariable("id") long id, Model model){
        Category category=categoryRepository.findOne(id);
        System.out.println("newsCategory"+category.getNewsCategory());
        //String newsURL="https://newsapi.org/v2/everything?q="+category.getNewsCategory()+"&sortBy=publishedAt&apiKey=cdcff7c00ad446bd9fd970620d96b155";
        //String newsURL="https://newsapi.org/v2/top-headlines?country=us&category="+category.getNewsCategory()+"&apiKey=cdcff7c00ad446bd9fd970620d96b155";
        String newsURL="https://newsapi.org/v2/top-headlines?country=us&category="+category.getNewsCategory()+"&apiKey=cdcff7c00ad446bd9fd970620d96b155";
        RestTemplate restTemplate=new RestTemplate();
        News news=restTemplate.getForObject(newsURL,News.class);
        model.addAttribute("articles",news.getArticles());
        return "selectednews";
    }
/*
    //Show topic
    @RequestMapping("/showtopic")
    public String showTopicNews(Model model,Category category,Source source,Topic topic)
    {
        //source.setId(category.);
        RestTemplate restTemplate=new RestTemplate();
        String url3="https://newsapi.org/v2/sources?apiKey=cdcff7c00ad446bd9fd970620d96b155";
        String url4="https://newsapi.org/v2/top-headlines?sources=bbc-news&apiKey=cdcff7c00ad446bd9fd970620d96b155";
        //String url5="https://newsapi.org/v2/everything?q=apple&from=2018-03-08&to=2018-03-08&sortBy=popularity&apiKey=cdcff7c00ad446bd9fd970620d96b155";
        String url5="https://newsapi.org/v2/top-headlines?q=trump&apiKey=cdcff7c00ad446bd9fd970620d96b155";
        String newsTopic=topic.getTopictext();
        System.out.println("newsTopic"+newsTopic);
        //url5="https://newsapi.org/v2/top-headlines?q="+newsTopic+"&&apiKey=cdcff7c00ad446bd9fd970620d96b155";
        url5="https://newsapi.org/v2/everything?q="+newsTopic+"&sortBy=publishedAt&apiKey=cdcff7c00ad446bd9fd970620d96b155";
        News news=restTemplate.getForObject(url5,News.class);



        NewsAgencies newsAgencies=restTemplate.getForObject(url3,NewsAgencies.class);

        //model.addAttribute("source",newsAgencies.getSources());
        model.addAttribute("articles",news.getArticles());
        return "selectednews";
    }*/
    //Search items

   /* @GetMapping("/search")
    public String getSearch()
    {
        return "index";
    }

    @PostMapping("/search")
    public String showSearchResults(HttpServletRequest request, Model model)
    {
        String searchString = request.getParameter("search");
       model.addAttribute("item",itemRepository.findByItemTitleContainsOrItemCatgoryContains(searchString,searchString));
        return "index";
    }*/
}
