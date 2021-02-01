package my.project.facade;

import my.project.rest.resources.request.CommentRequest;
import my.project.rest.resources.response.CommentNotificationResponse;
import my.project.rest.resources.response.CommentResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProcessingCommentFacade {
    ResponseEntity<CommentResponse> addComment(CommentRequest commentRequest);

    ResponseEntity<List<CommentResponse>> getCommentsByPage(Integer pageNumber);

    ResponseEntity<List<CommentNotificationResponse>> getCommentNotificationsByPage(Integer pageNumber);
}
