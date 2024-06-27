package com.lvh.spring_boot_project.repository;

import com.lvh.spring_boot_project.entity.Comment;
import com.lvh.spring_boot_project.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findByPostId(Long postId);
}
