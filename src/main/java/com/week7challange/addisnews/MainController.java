package com.week7challange.addisnews;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@Controller
public class MainController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    CategoryRepository categoryRepository;

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
        return "redirect:/selectednews";
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
