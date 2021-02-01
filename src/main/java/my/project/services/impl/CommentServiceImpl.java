package my.project.services.impl;

import my.project.configuration.GlobalConstant;
import my.project.exceptions.ExternalLogicErrorException;
import my.project.exceptions.NotFoundException;
import my.project.external_logic.BusinessLogic;
import my.project.repository.CommentNotificationRepository;
import my.project.repository.CommentRepository;
import my.project.repository.entities.CommentEntity;
import my.project.repository.entities.CommentNotificationEntity;
import my.project.services.CommentService;
import my.project.services.mappers.CommentMapper;
import my.project.services.models.Comment;
import my.project.services.models.CommentNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private static final Boolean DEFAULT_COMMENT_DELIVERED = true;
    private static final String COMMENT_NOT_FOUND_MESSAGE = "Comment by id %s not found";
    private static final String NOTIFICATION_NOT_FOUND_MESSAGE = "Notification by id %s not found";
    private static final Boolean FAIL_DELIVERED_NOTIFICATION = Boolean.FALSE;

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    CommentNotificationRepository notificationRepository;

    @Autowired
    CommentMapper commentMapper;

    @Override
    public Comment createComment(@NotNull Comment comment) {
        CommentEntity commentEntity = commentMapper.mapCommentToCommentEntity(comment);
        commentEntity.setTime(LocalDateTime.now());
        Comment commentModel = commentMapper.mapCommentEntityToComment(commentRepository.save(commentEntity));
        try {
            BusinessLogic.doSomeWorkOnCommentCreation();
            createNotificationByCommentIdAsync(commentModel.getId());
        } catch (RuntimeException exception) {
            commentRepository.deleteById(commentModel.getId());
            throw new ExternalLogicErrorException(commentModel);
        }

        return commentModel;
    }

    @Override
    public CommentNotification createNotificationByCommentId(Long commentId) {
        CommentEntity comment = getCommentById(commentId);
        CommentNotificationEntity notification = notificationRepository.save(buildNotificationEntity(comment));
        try {
            BusinessLogic.doSomeWorkOnNotification();
        } catch (RuntimeException exception) {
            notification.setDelivered(FAIL_DELIVERED_NOTIFICATION);
            notificationRepository.save(notification);
        }

        return commentMapper.mapNotificationEntityToNotification(notification);
    }

    private void createNotificationByCommentIdAsync(Long commentId) {
        new Thread(() -> {
            createNotificationByCommentId(commentId);
        }).start();
    }


    @Override
    public List<Comment> getCommentsByPage(Integer pageNumber) {
        Pageable pageRequest = buildPageableForComments(pageNumber);

        return commentRepository.findAll(pageRequest)
                .stream()
                .map(commentMapper::mapCommentEntityToComment)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentNotification> getNotificationsByPage(Integer page) {
        Pageable pageRequest = buildPageableForNotifications(page);

        return notificationRepository.findAll(pageRequest)
                .stream()
                .map(commentMapper::mapNotificationEntityToNotification)
                .collect(Collectors.toList());
    }

    private CommentNotificationEntity buildNotificationEntity(@NotNull CommentEntity comment) {
        CommentNotificationEntity notification = new CommentNotificationEntity();

        notification.setComment(comment);
        notification.setTime(LocalDateTime.now());
        notification.setDelivered(DEFAULT_COMMENT_DELIVERED);

        return notification;
    }


    private CommentEntity getCommentById(Long commentId) {
        Optional<CommentEntity> commentEntity = commentRepository.findById(commentId);

        return commentEntity.
                orElseThrow(() -> new NotFoundException(String.format(COMMENT_NOT_FOUND_MESSAGE, commentId)));
    }

    private Pageable buildPageableForComments(Integer page) {
        return PageRequest.of(page, GlobalConstant.DEFAULT_PAGE_SIZE);
    }

    private Pageable buildPageableForNotifications(Integer page) {
        return PageRequest.of(page, GlobalConstant.DEFAULT_PAGE_SIZE);
    }
}
