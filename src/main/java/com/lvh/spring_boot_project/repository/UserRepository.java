package com.lvh.spring_boot_project.repository;

import com.lvh.spring_boot_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    @Query("""
         select u from User u
         where u.enabled = true 
        """)
    List<User> findAllUser();

}
