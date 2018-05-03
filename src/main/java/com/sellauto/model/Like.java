package com.sellauto.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "likedPost")
public class Like {
	
	@Id //key
	@GeneratedValue //auto-incremented
	private Long id;

	@Column(nullable = false, length = 60)
	private String likedUser;
	
    @ManyToOne
    @JoinColumn(name = "id_post") 
    private Post post;	 

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLikedUser() {
		return likedUser;
	}

	public void setLikedUser(String likedUser) {
		this.likedUser = likedUser;
	}

	public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
	
}
