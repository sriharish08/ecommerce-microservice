package com.sri.web_gateway.controller;

import com.sri.web_gateway.common.response.Response;
import com.sri.web_gateway.integration.notification.client.NotificationClient;
import com.sri.web_gateway.integration.notification.dto.NotificationHistoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationGatewayController {

    private final NotificationClient notificationClient;

    @GetMapping("/{id}")
    public ResponseEntity<Response<NotificationHistoryResponse>> getById(@PathVariable("id") Long id) {
        Response<NotificationHistoryResponse> response = Response.ok();
        response.setPayload(notificationClient.getById(id));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Response<List<NotificationHistoryResponse>>> getByUserId(@PathVariable("userId") Long userId) {
        Response<List<NotificationHistoryResponse>> response = Response.ok();
        response.setPayload(notificationClient.getByUserId(userId));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
