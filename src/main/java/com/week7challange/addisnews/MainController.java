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
    public String login(Model model){
        model.addAttribute("classActiveSettings7","nav-item active");
        return "login";
    }

    @RequestMapping("/")
    public String showIndex(Model model)
    {
        RestTemplate restTemplate=new RestTemplate();
        String newsurl="https://newsapi.org/v2/top-headlines?country=us&category=general&apiKey=cdcff7c00ad446bd9fd970620d96b155";
        News news=restTemplate.getForObject(newsurl,News.class);

        //model.addAttribute("author",news.getTotalResults());
        model.addAttribute("classActiveSettings","nav-item active");
        model.addAttribute("category","General");
        model.addAttribute("articles",news.getArticles());
       return "index";
    }

    //User registration
    @RequestMapping(value="/registration",method= RequestMethod.GET)
    public String showRegistrationPage(Model model){
        model.addAttribute("classActiveSettings6","nav-item active");
        model.addAttribute("user",new User());
        return "registration";
    }
    @RequestMapping(value="/registration",method= RequestMethod.POST)
    public String processRegistrationPage(@Valid @ModelAttribute("user") User user, BindingResult result, Model model){

        if(result.hasErrors()){
            return "registration";
        }else{
            user.setEnabled(true);
            //Role role =roleRepository.findByRole("USER");
            user.setRoles(Arrays.asList(roleRepository.findByRole("USER")));

            System.out.println("user reg.post[role.getRole(): "+user.getRoles());
            //user.setRoles(role.getRole());

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
        model.addAttribute("classActiveSettings2","nav-item active");
        model.addAttribute("categories", categories);
        model.addAttribute("category", new Category());
        
        return "addcategory";
    }


    @PostMapping("/addcategory")
    public String addCategory(@RequestParam("newsCategory") String newsCategory,Category category, BindingResult result, Model model,Source source, NewsAgencies newsAgencies,Authentication auth, HttpServletRequest request ){

        if (result.hasErrors()) {
            return "addcategory";
        }
        User user = userRepository.findByUsername(auth.getName());
        category.addUser(user);
        System.out.println("PostMapping, source.getName():"+source.getName());
        //category.setNewsCategory(source.getName());
        category.setNewsCategory(newsCategory);
        categoryRepository.save(category);
        //sourceRepository.save(newsAgencies.getSources());
        System.out.println("from postmaping requestparam string "+newsCategory);
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

    @RequestMapping("/sessionpage")
    public String showFilteredNews(Model model,Category category,Authentication auth,User user)
    {

            user = userRepository.findByUsername(auth.getName());
            List<Category> cat = categoryRepository.findByUsers(user);

            for (Category ca :
                    cat) {
                System.out.println(ca.getNewsCategory());
                String newsURL = "https://newsapi.org/v2/top-headlines?country=us&category=" + ca.getNewsCategory() + "&apiKey=cdcff7c00ad446bd9fd970620d96b155";
                RestTemplate restTemplate = new RestTemplate();
                News news = restTemplate.getForObject(newsURL, News.class);
                model.addAttribute("category",ca.getNewsCategory());
                model.addAttribute("articles", news.getArticles());
            }


        return "index";
    }

    //Post jobs
    @GetMapping("/addtopic")
    public String addTopic(Model model){
        model.addAttribute("classActiveSettings4","nav-item active");
        model.addAttribute("topic",new Topic());
        return "addtopic";
    }
    @PostMapping("/addtopic")
    public String addTopicForm(@Valid @ModelAttribute("topic") Topic topic, BindingResult result,Model model,Principal principal,
                              RedirectAttributes redirectAttributes){
        if(result.hasErrors()){
            return "addtopic";
        }
        else{
            User user=userRepository.findByUsername(principal.getName());
            topic.setUser(user);
            topicRepository.save(topic);
            System.out.println("from postmaping "+topic.getTopictext());

            return "redirect:/";
        }
    }
    //list topic
    @RequestMapping("/topiclist")
    public String ShowTopic(Model model, Principal principal,User user,Topic topic){
        user=userRepository.findByUsername(principal.getName());
        model.addAttribute("classActiveSettings5","nav-item active");
        model.addAttribute("topic",topicRepository.findByUser(user));
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
    public String ShowCategory(Model model, Principal principal,User user,Category category){
        user=userRepository.findByUsername(principal.getName());
        model.addAttribute("classActiveSettings3","nav-item active");
        model.addAttribute("category",categoryRepository.findByUsers(user));
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


    @RequestMapping("/removecategory/{id}")
    public String removeCategory(@PathVariable("id") long id, Model model){
        //Category category=categoryRepository.findOne(id);
        categoryRepository.delete(id);
        return "redirect:/categorylist";
    }

}
