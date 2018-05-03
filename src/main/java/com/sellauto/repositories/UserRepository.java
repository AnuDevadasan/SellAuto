package com.sellauto.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.sellauto.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	
    User getUserByUsername(String username); //return user object for the given username
    User getUserById(long Id); //return user for the given userId
   
    @Transactional
    void deleteUserById(Long id); //delete the user

}
