package com.ineric.services.mappers;

import com.ineric.repository.entities.CommentEntity;
import com.ineric.repository.entities.CommentNotificationEntity;
import com.ineric.services.models.Comment;
import com.ineric.services.models.CommentNotification;
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
