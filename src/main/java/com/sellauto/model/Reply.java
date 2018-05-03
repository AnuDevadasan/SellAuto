package com.sellauto.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "reply")
public class Reply {

    @Id //key
    @GeneratedValue //auto-incremented
    private Long id;

    @Column(nullable = false, length = 1024)
    private String content;

    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;   //reply is linked to User

    @ManyToOne
    @JoinColumn(name = "id_post") //Many reply for one user
    private Post post;	// reply is linked to post
    
    //getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String displayParsedCreatedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy  hh:mm a");
        		
        return this.createdDate.format(formatter);
    }
    
    //to display the replys listed , content is shown less
    public String displayBeginning() {
        return (this.content.length() < 32) ? this.content.concat("...") : this.content.substring(0, 20).concat("...");
    }
}
