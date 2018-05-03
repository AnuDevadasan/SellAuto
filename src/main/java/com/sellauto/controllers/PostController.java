package com.sellauto.controllers;

import com.sellauto.exceptions.AllException;
import com.sellauto.exceptions.StorageFileNotFoundException;
import com.sellauto.model.Like;
import com.sellauto.model.Post;
import com.sellauto.model.Reply;
import com.sellauto.repositories.LikeRepository;
import com.sellauto.repositories.PostRepository;
import com.sellauto.repositories.ReplyRepository;
import com.sellauto.repositories.UserRepository;
import com.sellauto.storage.StorageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PostController {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ReplyRepository replyRepository;
    private final LikeRepository likeRepository; 
    private final StorageRepository storageRepository; //image file storage
     
    @Autowired
    public PostController(UserRepository userRepository, PostRepository postRepository, ReplyRepository replyRepository,StorageRepository storageRepository,LikeRepository likeRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.replyRepository = replyRepository;
        this.storageRepository = storageRepository;
        this.likeRepository = likeRepository;
    }
    
    //get post by postId
    @GetMapping("post/{id}")
    public String displayPost(@PathVariable String id,  Model model  ) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails)principal).getUsername(); //retrieve username 
        Long idUser = userRepository.getUserByUsername(username).getId(); //get userId 

        Post post = postRepository.findPostById(Long.valueOf(id)); //retrieve the object post
        List<Reply> replys = replyRepository.findReplyByPost_Id(Long.valueOf(id)); //retrieve the list of replys for the postId
        List<Like> likes = likeRepository.findLikeByPost_Id(Long.valueOf(id));
       
        model.addAttribute("post", post);
        model.addAttribute("replys", replys);
        model.addAttribute("likes", likes);
        model.addAttribute("idUser", idUser);
        model.addAttribute("files", storageRepository.loadAll()  //image file
        	.filter(path -> path.getFileName().toString().contains("_"+id)) //fetch file related to post id
       		.map(path -> MvcUriComponentsBuilder.fromMethodName ( PostController.class, //Create a UriComponentsBuilder from the mapping of a controller method and an array of method argument values.
                        "serverFile", path.getFileName().toString()).build().toString() )
       		.collect(Collectors.toList()));
       return "post";
    }
    
    //like a post
    @PostMapping("like")
    public String addlikes(@ModelAttribute("like") Like like, final RedirectAttributes redirectAttributes,
   		 @RequestParam("id_post") String id_post, @RequestParam("id_user") String id_user , Model model ) {
    	
    	String liked_user = userRepository.getUserById(Long.parseLong(id_user)).getUsername();
    	//check whether the user has already liked the post. 
    	if (likeRepository.findLikeBylikedUserAndPost_Id(liked_user,Long.valueOf(id_post)) == null) {
	    	like.setLikedUser(liked_user);
	    	like.setPost(postRepository.findPostById(Long.valueOf(id_post)));
	    	
	        //create like in db
	        likeRepository.save(like);
	        
	        Post post = postRepository.findPostById(Long.valueOf(id_post));
	        return "redirect:/post/"+post.getId().toString();
    	}
    	else
    	{	redirectAttributes.addFlashAttribute("message", "You have already liked the post");
    	  	Post post = postRepository.findPostById(Long.valueOf(id_post));
    	  	return "redirect:/post/"+post.getId().toString();
    	}
    }
    
    //search for postId
    @GetMapping("post/search/{id}")
    public String SearchPost(@RequestParam("id") String id , Model model, RedirectAttributes redirectAttributes) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails)principal).getUsername(); //retrieve username 
        Long idUser = userRepository.getUserByUsername(username).getId();
        
        Post post = postRepository.findPostById(Long.valueOf(id));
        List<Reply> replys = replyRepository.findReplyByPost_Id(Long.valueOf(id));
        List<Like> likes = likeRepository.findLikeByPost_Id(Long.valueOf(id));
        
        try {
        	 //if no such post id is available, the object post will be NULL.
        	 if(post.equals(null)) {
               	throw new AllException("Post Id not found ");
             	 }
        else { //if postId exists
        model.addAttribute("post", post);
        model.addAttribute("replys", replys);
        model.addAttribute("idUser", idUser);
        model.addAttribute("likes", likes);
        model.addAttribute("files", storageRepository.loadAll() //image file
        	.filter(path -> path.getFileName().toString().contains("_"+id)) //fetch file related to post id
       		.map(path -> MvcUriComponentsBuilder.fromMethodName ( PostController.class, //Create a UriComponentsBuilder from the mapping of a controller method and an array of method argument values.
                        "serverFile", path.getFileName().toString()).build().toString() )
       		.collect(Collectors.toList()));
      
        return "post";
        
        }
             }  catch (Exception e) {
             	throw new AllException("Post Id not found ");
             }
    }
    
    //create post
    @PostMapping("post")
    public String addPost(@ModelAttribute("post") Post post, final RedirectAttributes redirectAttributes,
   		 @RequestParam("id_user") String id_user,@RequestParam("file") MultipartFile file) {
	 
    	post.setCreatedDate(LocalDateTime.now());
    	post.setUser(userRepository.getUserById(Long.parseLong(id_user)));
    	//create post in database
    	postRepository.save(post);
    	
    	//retrieve PostId
    	String post_id = post.getId().toString();
    	//save the file to the location, by appending the postId to the filename
    	storageRepository.store(file,post_id);
    	
    	return "redirect:/post/"+post.getId().toString();
    }
       
    //delete post
    @GetMapping("post/remove/{id}")
    public String deletePostById(@PathVariable String id,final RedirectAttributes redirectAttributes) {
    	
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails)principal).getUsername();
        Long idUser = userRepository.getUserByUsername(username).getId();
        
    	postRepository.deletePostById(Long.valueOf(id));
    	storageRepository.deleteFile(id); //delete the existing image.
    	 
        return "redirect:/posts/user/"+idUser;
    	 
    }
    
    //edit post
    @GetMapping("post/edit/{id}")
    public String editPostById(@PathVariable String id,final RedirectAttributes redirectAttributes, Model model) {
        
        Post editPost = postRepository.findPostById(Long.valueOf(id));

        model.addAttribute("editPost", editPost);
        model.addAttribute("files", storageRepository.loadAll()
            	.filter(path -> path.getFileName().toString().contains("_"+id)) //fetch file related to post id
           		.map(path -> MvcUriComponentsBuilder.fromMethodName ( PostController.class,
                            "serverFile", path.getFileName().toString()).build().toString() )
           		.collect(Collectors.toList()));
        //display editPage
	    return "editPage";

    }
    
    //editPage
    @PostMapping("post/update")
    public String updatePost(@ModelAttribute("editPost") Post editPost,final RedirectAttributes redirectAttributes
    		,@RequestParam("image") MultipartFile file) {
    	
    	 Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         String username = ((UserDetails)principal).getUsername();
         Long idUser = userRepository.getUserByUsername(username).getId();
         
         editPost.setCreatedDate(LocalDateTime.now());
         editPost.setUser(userRepository.getUserById(idUser));
         postRepository.save(editPost);
        
         if (!file.isEmpty()){  // store file only if new image is uploaded.
        	 String post_id = editPost.getId().toString();
        	 storageRepository.deleteFile(post_id); //delete the existing image.
             storageRepository.store(file,post_id); //store the new image uploaded
         }
                  
         return "redirect:/post/"+editPost.getId().toString();
    }
    
    //retrieve file
    @GetMapping("post/files/{filename}")
    @ResponseBody
    public ResponseEntity<Resource> serverFile(@PathVariable String filename) {
    	
        Resource file = storageRepository.loadAsResource(filename);
      
        //http://localhost/post/file/filename
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
    
    //exception handling
    @ExceptionHandler(StorageFileNotFoundException.class)
    public String handleStorageFileNotFound(Model model,StorageFileNotFoundException exc) {
    	
    	model.addAttribute("message", exc.getMessage());
    	return "error";
    }
    
    @ExceptionHandler(AllException.class)
    public String handleException(Model model,AllException exc) {
    	
		model.addAttribute("message", exc.getMessage());

		return "error";
    }
   
}