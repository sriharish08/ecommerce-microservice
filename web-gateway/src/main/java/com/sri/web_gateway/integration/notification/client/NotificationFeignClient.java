package com.sri.web_gateway.integration.notification.client;

import com.sri.web_gateway.common.response.Response;
import com.sri.web_gateway.integration.notification.dto.NotificationHistoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "notification-service",
        url = "${feign.client.notification-service}"
)
public interface NotificationFeignClient {

    @GetMapping("/{id}")
    Response<NotificationHistoryResponse> getById(@PathVariable("id") Long id);

    @GetMapping("/user/{userId}")
    Response<List<NotificationHistoryResponse>> getByUserId(@PathVariable("userId") Long userId);
}
