package com.lvh.spring_boot_project.entity;

import com.lvh.spring_boot_project.service.CommentRedisService;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RequiredArgsConstructor
public class CommentListener {
    private final CommentRedisService commentRedisService;

    private static final Logger logger = LoggerFactory.getLogger(CommentListener.class);

    @PrePersist
    public void prePersist(Comment comment){
        logger.info("prePersist");
    }

    @PostPersist
    public void postPersist(Comment comment){
        logger.info("postPersist");
        commentRedisService.clear();
    }

    @PreUpdate
    public void preUpdate(Comment comment){
        logger.info("preUpdate");
    }

    @PostUpdate
    public void postUpdate(Comment comment){
        logger.info("postUpdate");
        commentRedisService.clear();
    }

    @PreRemove
    public void preRemove(Comment comment){
        logger.info("preRemove");
    }

    @PostRemove
    public void postRemove(Comment comment){
        logger.info("postRemove");
        commentRedisService.clear();
    }



}
