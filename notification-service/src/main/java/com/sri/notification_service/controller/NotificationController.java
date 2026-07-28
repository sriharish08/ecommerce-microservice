package com.sri.notification_service.controller;

import com.sri.notification_service.common.response.Response;
import com.sri.notification_service.dto.response.NotificationHistoryResponse;
import com.sri.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    @GetMapping("/{id}")
    public ResponseEntity<Response<NotificationHistoryResponse>> getById(
            @PathVariable("id") Long id,
            @RequestHeader("X-User-Id") Long requesterId,
            @RequestHeader("X-User-Role") String requesterRole) {

        Response<NotificationHistoryResponse> response = Response.ok();
        response.setPayload(service.getById(id, requesterId, requesterRole));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Response<List<NotificationHistoryResponse>>> getByUserId(
            @PathVariable("userId") Long userId,
            @RequestHeader("X-User-Id") Long requesterId,
            @RequestHeader("X-User-Role") String requesterRole) {

        Response<List<NotificationHistoryResponse>> response = Response.ok();
        response.setPayload(service.getByUserId(userId, requesterId, requesterRole));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
