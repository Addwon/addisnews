package com.week7challange.addisnews;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
public class MainController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;


    @RequestMapping("/login")
    public String login(){return "login";}

    @RequestMapping("/")
    public String showIndex(Model model)
    {

        RestTemplate restTemplate=new RestTemplate();
        String url="https://newsapi.org/v2/everything?q=bitcoin&apiKey=cdcff7c00ad446bd9fd970620d96b155";
        //MediaType contentType = responseEntity.getHeaders().getContentType();
        //HttpStatus statusCode = responseEntity.getStatusCode();
       // News news[]=restTemplate.getForObject("https://newsapi.org/v2/everything?q=bitcoin&apiKey=cdcff7c00ad446bd9fd970620d96b155",News[].class);
        //List<News> news=restTemplate.getForObject("https://newsapi.org/v2/everything?q=bitcoin&apiKey=cdcff7c00ad446bd9fd970620d96b155",News.class).getNews();
        //List<Articles> articles=restTemplate.getForObject("https://newsapi.org/v2/everything?q=bitcoin&apiKey=cdcff7c00ad446bd9fd970620d96b155",Articles.class).getArticles();
        //List<Source> sources=restTemplate.getForObject("https://newsapi.org/v2/everything?q=bitcoin&apiKey=cdcff7c00ad446bd9fd970620d96b155",Source.class).getSources();

        /*News news=restTemplate.getForObject("https://newsapi.org/v2/top-headlines -G \\\n" +
                "    -d country=us \\\n" +
                "    -d apiKey=cdcff7c00ad446bd9fd970620d96b155",News.class);*/
        //return news.getArticles().getAuthor();


       News news=restTemplate.getForObject("https://newsapi.org/v2/everything?q=bitcoin&apiKey=cdcff7c00ad446bd9fd970620d96b155",News.class);
      // Articles articles=restTemplate.getForObject(url,Articles.class);
        //ResponseEntity<Articles[]> articles = restTemplate.getForEntity(url, Articles[].class);
       model.addAttribute("author",news.getTotalResults());
        model.addAttribute("articles",news.getArticles());
       return "index";
        //return news.getArticles().;
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
