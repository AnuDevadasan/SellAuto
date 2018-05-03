package com.sellauto.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.sellauto.model.Reply;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Transactional
    void deleteReplyById(Long id); //delete the reply 

    Long countReplysByUser_Id(Long id); //count of replys by a user
    Long countReplysByPost_Id(Long post_id); //count of replys for a particular post

    List<Reply> findReplyByUser_IdOrderByCreatedDateDesc(Long id); //find replys for the given userId, order by date desc
    List<Reply> findReplyByPost_Id(Long post_id);//find replys for given postId
}
