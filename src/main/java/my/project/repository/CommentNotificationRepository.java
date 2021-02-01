package my.project.repository;

import my.project.repository.entities.CommentNotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentNotificationRepository extends JpaRepository<CommentNotificationEntity, Long> {

}
