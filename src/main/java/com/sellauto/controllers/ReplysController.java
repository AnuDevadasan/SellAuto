package com.sellauto.controllers;

import com.sellauto.model.Reply;
import com.sellauto.repositories.PostRepository;
import com.sellauto.repositories.ReplyRepository;
import com.sellauto.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Controller
public class ReplysController {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ReplyRepository replyRepository;
    
    @Autowired
    private JavaMailSender sender;

    @Autowired
    public ReplysController(UserRepository userRepository, PostRepository postRepository, ReplyRepository replyRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.replyRepository = replyRepository;
    }
    
    //display My reply
    @GetMapping("replys/{id}")
    public String displayReplysByUser(@PathVariable String id, Model model) {
        List<Reply> replys = replyRepository.findReplyByUser_IdOrderByCreatedDateDesc(Long.parseLong(id));
        model.addAttribute("replys", replys);
        return "replys";
    }
    
    //send reply
    @PostMapping("reply")
    public String addReply(@ModelAttribute("reply") Reply reply, final RedirectAttributes redirectAttributes,
    		 @RequestParam("id_post") String id_post, @RequestParam("id_user") String id_user) {
    	reply.setCreatedDate(LocalDateTime.now());
        reply.setPost(postRepository.findPostById(Long.valueOf(id_post)));
        reply.setUser(userRepository.getUserById(Long.parseLong(id_user)));
        
        //create reply in db
        replyRepository.save(reply);
        
        //get the author's email
        String email = reply.getPost().getUser().getEmail();
              
        //sending email
          MimeMessage message = sender.createMimeMessage();
          MimeMessageHelper helper = new MimeMessageHelper(message);
            
       try {
            	helper.setTo(email);
				helper.setText(reply.getContent());
				helper.setSubject("Reply for your post id: "+id_post+" on SellAuto Webisite. "
						+ "Please do not reply to this email id");
			} catch (MessagingException e) {
				
				 return "Error in sending email: "+e; //e.printStackTrace();
			}
          sender.send(message);
          return "redirect:/post/"+id_post;
    }
 
}
