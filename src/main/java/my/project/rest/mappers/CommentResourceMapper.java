package my.project.rest.mappers;

import my.project.rest.resources.request.CommentRequest;
import my.project.rest.resources.response.CommentNotificationResponse;
import my.project.rest.resources.response.CommentResponse;
import my.project.services.models.Comment;
import my.project.services.models.CommentNotification;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CommentResourceMapper {
    CommentResourceMapper instance = Mappers.getMapper(CommentResourceMapper.class);

    Comment mapCommentRequestToComment(CommentRequest commentRequest);

    CommentResponse mapCommentToCommentResponse(Comment comment);

    CommentNotificationResponse mapCommentNotificationToCommentNotificationResponse(CommentNotification commentNotification);
}
