package com.sellauto.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sellauto.model.Like;

public interface LikeRepository extends JpaRepository<Like, Long> {
	
	List<Like> findLikeByPost_Id(Long post_id);
	Long countLikesByPost_Id(Long post_id);
	Like findLikeBylikedUserAndPost_Id(String liked_user,Long post_id); //return like object for the given username and post_id

}
