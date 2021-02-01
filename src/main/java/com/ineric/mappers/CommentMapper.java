package com.ineric.mappers;

import com.ineric.repository.entities.CommentEntity;
import com.ineric.repository.entities.CommentNotificationEntity;
import com.ineric.models.Comment;
import com.ineric.models.CommentNotification;
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
