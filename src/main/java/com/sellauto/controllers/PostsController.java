package com.sellauto.controllers;

import com.sellauto.model.Post;
import com.sellauto.repositories.PostRepository;
import com.sellauto.repositories.ReplyRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class PostsController {

    private final PostRepository postRepository;
    private final ReplyRepository replyRepository;

    @Autowired
    public PostsController(PostRepository postRepository, ReplyRepository replyRepository) {
        this.postRepository = postRepository;
        this.replyRepository = replyRepository;
    }

    //show all posts
    @GetMapping("posts")
    public String displayAllPosts(Model model) {
    	//find all posts, order by status asc,date desc
       	List<Post> posts = postRepository.findAllByOrderByStatusAscCreatedDateDesc();
        String header = setHeader("all");
        model.addAttribute("posts", posts);
        model.addAttribute("header", header);
        model.addAttribute("replyRepository", replyRepository);
        return "posts";
    }

    //show all posts under selected category
    @GetMapping("posts/{category}")
    public String displayPostsByCategory(@PathVariable("category") String category, Model model) {
    	//find posts for given category, order by status asc,date desc
       	List<Post> posts = postRepository.findPostsByCategoryOrderByCreatedDateDesc(category);
    	String header = setHeader(category);
        model.addAttribute("posts", posts);
        model.addAttribute("header", header);
        model.addAttribute("replyRepository", replyRepository);
        return "posts";
    }
    
    //show all posts by the status
    @GetMapping("posts/status/{status}")
    public String displayPostsByStatus(@PathVariable String status, Model model) {
    	//find posts for given location, order by date desc
        List<Post> posts = postRepository.findPostsByStatusOrderByCreatedDateDesc(status);
        String header = status;
        model.addAttribute("posts", posts);
        model.addAttribute("header", header);
        model.addAttribute("replyRepository", replyRepository);
        return "posts";
    }
    
   //show all posts under selected status and category 
   @GetMapping("posts/{category}/{status}")
    public String displayPostsByStatus(@PathVariable("category") String category,@PathVariable ("status") String status, Model model) {
	   //find posts for given category and status,order by date desc
        List<Post> posts = postRepository.findPostsByCategoryAndStatusOrderByCreatedDateDesc(category,status);
        String header = setHeader(category);
        header = header +" "+status;
        model.addAttribute("header", header);
        model.addAttribute("posts", posts);
        model.addAttribute("replyRepository", replyRepository);
        return "posts";
    }
   
   //show all posts by the location
   @GetMapping("posts/{category}/{status}/{location}")
   public String displayPostsByLocation(@PathVariable("category") String category,@PathVariable ("status") String status,@PathVariable ("location") String location, Model model) {
   	//find posts for given user, order by date desc
       List<Post> posts = postRepository.findPostsByCategoryAndStatusAndUser_LocationOrderByCreatedDateDesc(category,status,location);
       String header = setHeader(category);
       header = header +" "+status+" Located at "+location;
       model.addAttribute("posts", posts);
       model.addAttribute("header", header);
       model.addAttribute("replyRepository", replyRepository);
       return "posts";
   }
   
   //show all posts by the user
    @GetMapping("posts/user/{id}")
    public String displayPostsByUser(@PathVariable String id, Model model) {
    	//find posts for given user, order by date desc
        List<Post> posts = postRepository.findPostsByUser_IdOrderByCreatedDateDesc(Long.valueOf(id));
        String header = setHeader("user");
        model.addAttribute("posts", posts);
        model.addAttribute("header", header);
        model.addAttribute("replyRepository", replyRepository);
        return "posts";
    }
    
    //show all posts by the location
    @GetMapping("posts/location/{location}")
    public String displayPostsByLocation(@PathVariable String location, Model model) {
    	//find posts for given location, order by date desc
        List<Post> posts = postRepository.findPostsByUser_LocationOrderByCreatedDateDesc(location);
        String header = location;
        model.addAttribute("posts", posts);
        model.addAttribute("header", header);
        model.addAttribute("replyRepository", replyRepository);
        return "posts";
    }

    
    //category name for category number
    private String setHeader(String category) {
        switch (category) {
            case "1":
                return "Automobiles";
            case "2":
                return "Real Estate";
            case "3":
                return "Household Items";
            case "4":
                return "Electronics";
            case "5":
                return "Miscellaneous Items";
            case "all":
                return "All";
            default:
                return "User's posts";
        }
    }
}