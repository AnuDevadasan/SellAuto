package com.sellauto.controllers;

import com.sellauto.model.Post;
import com.sellauto.model.Reply;
import com.sellauto.model.User;
import com.sellauto.repositories.PostRepository;
import com.sellauto.repositories.ReplyRepository;
import com.sellauto.repositories.UserRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class ProfileController {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ReplyRepository replyRepository;
   

    @Autowired
    public ProfileController(UserRepository userRepository, PostRepository postRepository, ReplyRepository replyRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.replyRepository = replyRepository;
        
    }
    
    //show profile
    @GetMapping("profile")
    public String displayMyProfile(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails)principal).getUsername();
        
        //return user object for the given username
        User user = userRepository.getUserByUsername(username);
      
        Long numberOfPosts = postRepository.countPostsByUser_Id(user.getId()); //get count of posts by the user
        Long numberOfReplys = replyRepository.countReplysByUser_Id(user.getId()); //count of replys by a user

        model.addAttribute("user", user);
        model.addAttribute("numberOfPosts", numberOfPosts);
        model.addAttribute("numberOfReplys", numberOfReplys);
           
        return "profile";
    }

    
   
    //delete profile
    @PostMapping("profile/remove/{id}")
    public String deleteProfileById(@PathVariable String id,final RedirectAttributes redirectAttributes) {
    	
    	//delete all replies posted by the user
    	List<Reply> replys = replyRepository.findReplyByUser_IdOrderByCreatedDateDesc(Long.valueOf(id)); //find replys for the given userId, order by date desc
    	 for (Reply reply: replys) {
    		 replyRepository.deleteReplyById(reply.getId());
    	 }
    	
    	//delete all posts posted by the user 
    	List<Post> posts = postRepository.findPostsByUser_IdOrderByCreatedDateDesc(Long.valueOf(id));//find posts for given user, order by date desc
    	 for (Post post: posts) {
    		 postRepository.deletePostById(post.getId());
    	 }
    	 
    	 //delete the user 
    	userRepository.deleteUserById(Long.valueOf(id));
    	
    	return "redirect:/"; //go to home page
    	 
    }
     
}