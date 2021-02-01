package com.ineric.facade.impl;

import com.ineric.exceptions.ExternalLogiсException;
import com.ineric.facade.ProcessingCommentFacade;
import com.ineric.rest.mappers.CommentResourceMapper;
import com.ineric.rest.resources.request.CommentRequest;
import com.ineric.rest.resources.response.CommentNotificationResponse;
import com.ineric.rest.resources.response.CommentResponse;
import com.ineric.services.CommentService;
import com.ineric.services.models.Comment;
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
        } catch (ExternalLogiсException exception) {
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
