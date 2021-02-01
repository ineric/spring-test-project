package com.ineric.facade;

import com.ineric.rest.resources.request.CommentRequest;
import com.ineric.rest.resources.response.CommentNotificationResponse;
import com.ineric.rest.resources.response.CommentResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProcessingCommentFacade {
    ResponseEntity<CommentResponse> addComment(CommentRequest commentRequest);

    ResponseEntity<List<CommentResponse>> getCommentsByPage(Integer pageNumber);

    ResponseEntity<List<CommentNotificationResponse>> getCommentNotificationsByPage(Integer pageNumber);
}
