package com.ineric;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ineric.configuration.Constants;
import com.ineric.MainApplication;
import com.ineric.rest.resources.response.CommentNotificationResponse;
import com.ineric.rest.resources.response.CommentResponse;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = MOCK, classes = MainApplication.class)
@AutoConfigureMockMvc
class CommentIntegrationTest {

    private static final Integer MAX_COMMENT_COUNT = 1000;
    private static final Integer PAGE_COUNT = Math.round(MAX_COMMENT_COUNT / Constants.DEFAULT_PAGE_SIZE);

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void sendCommentsAndCheckResult() throws Exception {

        List<CommentResponse> badCommentsResponses = new ArrayList<>();
        List<CommentResponse> successCommentsResponses = new ArrayList<>();

        sendCommentsAndAddResponseToCorrespondingList(badCommentsResponses, successCommentsResponses);

        List<CommentResponse> availableCommentsResponses = getAvailableCommentResponses();
        List<CommentNotificationResponse> availableNotificationsResponses = getAvailableNotificationResponses();

        List<CommentResponse> commentsFromNotifications = getCommentFromNotifications(availableNotificationsResponses);
        List<CommentNotificationResponse> deliveredNotifications = getDeliveredNotifications(availableNotificationsResponses);

        Integer successCommentsCount = successCommentsResponses.size();
        Integer commentsFromApiCount = availableCommentsResponses.size();
        Integer notificationsCount = availableNotificationsResponses.size();
        Integer commentsFromNotificationsCount = commentsFromNotifications.size();
        Integer deliveredNotificationsCount = deliveredNotifications.size();

        assertThat(successCommentsCount.equals(commentsFromApiCount));
        assertThat(commentsFromApiCount.equals(notificationsCount));
        assertThat(successCommentsCount.equals(commentsFromNotificationsCount));

        float successCommentsPercent = (successCommentsCount * 100) / MAX_COMMENT_COUNT;
        float deliveredNotificationsPercent = (deliveredNotificationsCount * 100) / notificationsCount;

        System.out.println("Success Comments Percent: " + successCommentsPercent);
        System.out.println("Delivered Notifications Percent: " + deliveredNotificationsPercent);

    }

    private void sendCommentsAndAddResponseToCorrespondingList(List<CommentResponse> badCommentsResponses, List<CommentResponse> successCommentsResponses) throws Exception {
        for (int i = 0; i < MAX_COMMENT_COUNT; i++) {
            String requestBody = "{\"comment\":\"comment" + i + "\"}";

            MvcResult mvcResult = mockMvc.perform(post("/api/comments")
                    .content(requestBody)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andReturn();

            CommentResponse commentResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CommentResponse.class);

            if (mvcResult.getResponse().getStatus() == 202) {
                successCommentsResponses.add(commentResponse);
            } else if (mvcResult.getResponse().getStatus() == 400) {
                badCommentsResponses.add(commentResponse);
            }
        }
    }

    private List<CommentNotificationResponse> getDeliveredNotifications(List<CommentNotificationResponse> availableNotificationsResponses) {
        return availableNotificationsResponses
                .stream()
                .filter(item -> item.getDelivered() == Boolean.TRUE)
                .collect(Collectors.toList());
    }

    private List<CommentResponse> getCommentFromNotifications(List<CommentNotificationResponse> availableNotificationsResponses) {
        return availableNotificationsResponses
                .stream()
                .map(CommentNotificationResponse::getComment)
                .collect(Collectors.toList());
    }

    private List<CommentNotificationResponse> getAvailableNotificationResponses() throws Exception {
        List<CommentNotificationResponse> availableNotificationsResponses = new ArrayList<>();
        for (int pageNumber = 0; pageNumber < PAGE_COUNT; pageNumber++) {
            MvcResult mvcResult = mockMvc.perform(get("/api/comments/notifications/" + pageNumber)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().is2xxSuccessful())
                    .andReturn();

            availableNotificationsResponses = objectMapper.
                    readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<CommentNotificationResponse>>() {
                    });
        }
        return availableNotificationsResponses;
    }

    private List<CommentResponse> getAvailableCommentResponses() throws Exception {
        List<CommentResponse> availableCommentsResponses = new ArrayList<>();
        for (int pageNumber = 0; pageNumber < PAGE_COUNT; pageNumber++) {
            MvcResult mvcResult = mockMvc.perform(get("/api/comments/" + pageNumber)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().is2xxSuccessful())
                    .andReturn();

            availableCommentsResponses = objectMapper.
                    readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<CommentResponse>>() {
                    });
        }
        return availableCommentsResponses;
    }

}