package com.ineric.rest.mappers;

import com.ineric.models.Comment;
import com.ineric.models.CommentNotification;
import com.ineric.rest.resources.request.CommentRequest;
import com.ineric.rest.resources.response.CommentNotificationResponse;
import com.ineric.rest.resources.response.CommentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CommentResourceMapper {
    CommentResourceMapper instance = Mappers.getMapper(CommentResourceMapper.class);

    Comment mapCommentRequestToComment(CommentRequest commentRequest);

    CommentResponse mapCommentToCommentResponse(Comment comment);

    CommentNotificationResponse mapCommentNotificationToCommentNotificationResponse(CommentNotification commentNotification);
}
