package my.project.facade.impl;

import my.project.exceptions.ExternalLogicErrorException;
import my.project.facade.ProcessingCommentFacade;
import my.project.rest.mappers.CommentResourceMapper;
import my.project.rest.resources.request.CommentRequest;
import my.project.rest.resources.response.CommentNotificationResponse;
import my.project.rest.resources.response.CommentResponse;
import my.project.services.CommentService;
import my.project.services.models.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProcessingCommentFacadeImpl implements ProcessingCommentFacade {

    @Autowired
    CommentService commentService;

    @Autowired
    CommentResourceMapper commentResourceMapper;

    @Override
    public ResponseEntity<CommentResponse> addComment(CommentRequest commentRequest) {
        CommentResponse commentResponse;
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        Comment comment = commentResourceMapper.mapCommentRequestToComment(commentRequest);
        try {
            comment = commentService.createComment(comment);
            httpStatus = HttpStatus.ACCEPTED;
        } catch (ExternalLogicErrorException exception) {
            if (exception.getObjectProcessed() instanceof Comment) {
                comment = (Comment) exception.getObjectProcessed();
            }
        }
        commentResponse = commentResourceMapper.mapCommentToCommentResponse(comment);

        return new ResponseEntity<>(commentResponse, httpStatus);
    }

    @Override
    public ResponseEntity<List<CommentResponse>> getCommentsByPage(Integer pageNumber) {
        List<CommentResponse> commentResponses = commentService.getCommentsByPage(pageNumber)
                .stream()
                .map(commentResourceMapper::mapCommentToCommentResponse)
                .collect(Collectors.toList());

        return new ResponseEntity<>(commentResponses, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<CommentNotificationResponse>> getCommentNotificationsByPage(Integer pageNumber) {
        List<CommentNotificationResponse> notificationResponses = commentService.getNotificationsByPage(pageNumber)
                .stream()
                .map(commentResourceMapper::mapCommentNotificationToCommentNotificationResponse)
                .collect(Collectors.toList());

        return new ResponseEntity<>(notificationResponses, HttpStatus.OK);
    }
}
