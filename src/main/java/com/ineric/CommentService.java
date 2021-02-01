package com.ineric;

import com.ineric.models.Comment;
import com.ineric.models.CommentNotification;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface CommentService {
    Comment createComment(@NotNull Comment comment);

    CommentNotification createNotificationByCommentId(Long commentId);

    List<Comment> getCommentsByPage(Integer pageNumber);

    List<CommentNotification> getNotificationsByPage(Integer pageNumber);
}
