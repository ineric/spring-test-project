package my.project.services.mappers;

import my.project.repository.entities.CommentEntity;
import my.project.repository.entities.CommentNotificationEntity;
import my.project.services.models.Comment;
import my.project.services.models.CommentNotification;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentMapper instance = Mappers.getMapper(CommentMapper.class);

    CommentEntity mapCommentToCommentEntity(Comment comment);

    Comment mapCommentEntityToComment(CommentEntity commentEntity);

    CommentNotificationEntity mapNotificationToNotificationEntity(CommentNotification commentNotification);

    CommentNotification mapNotificationEntityToNotification(CommentNotificationEntity commentNotificationEntity);
}
