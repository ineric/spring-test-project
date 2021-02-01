package my.project.rest.controllers;

import my.project.facade.ProcessingCommentFacade;
import my.project.rest.resources.request.CommentRequest;
import my.project.rest.resources.response.CommentNotificationResponse;
import my.project.rest.resources.response.CommentResponse;
import my.project.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    ProcessingCommentFacade processingCommentFacade;

    @Autowired
    CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> saveComment(@RequestBody CommentRequest commentRequest) throws InterruptedException {

        return processingCommentFacade.addComment(commentRequest);
    }

    @GetMapping("/{pageNumber}")
    public ResponseEntity<List<CommentResponse>> getCommentsByPage(@PathVariable Integer pageNumber) {

        return processingCommentFacade.getCommentsByPage(pageNumber);
    }

    @GetMapping("/notifications/{pageNumber}")
    public ResponseEntity<List<CommentNotificationResponse>> getNotificationsByPage(@PathVariable Integer pageNumber) {

        return processingCommentFacade.getCommentNotificationsByPage(pageNumber);
    }

}
