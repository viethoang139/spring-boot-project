package com.lvh.spring_boot_project.repository;

import com.lvh.spring_boot_project.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {
}
