package com.ineric.repository;

import com.ineric.repository.entities.CommentNotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentNotificationRepository extends JpaRepository<CommentNotificationEntity, Long> {

}
