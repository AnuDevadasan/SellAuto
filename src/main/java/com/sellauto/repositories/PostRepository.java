package com.sellauto.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.sellauto.model.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Long countPostsByUser_Id(Long id); //get count of posts by the user
    
    Post findPostById(Long id); //find post by postId
    
	List<Post> findAllByOrderByStatusAscCreatedDateDesc(); //find all posts, order by status asc,date desc
  
    List<Post> findPostsByStatusOrderByCreatedDateDesc(String status); //find posts for given category, order by date desc
    List<Post> findPostsByCategoryOrderByCreatedDateDesc(String category); //find posts for given category, order by status asc,date desc
    
    List<Post> findPostsByCategoryAndStatusOrderByCreatedDateDesc(String category,String status); //find posts for given category and status,order by date desc
    List<Post> findPostsByCategoryAndStatusAndUser_LocationOrderByCreatedDateDesc(String category,String status,String location);//find posts for given category and location, order by date desc
    
    List<Post> findPostsByUser_IdOrderByCreatedDateDesc(Long id);//find posts for given user, order by date desc
    List<Post> findPostsByUser_LocationOrderByCreatedDateDesc(String location);//find posts for given location,order by date desc
    
    @Transactional
    void deletePostById(Long id); //delete selected postId
   
}
