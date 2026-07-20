package com.sri.auth_service.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public final class ResponseBuilder {

    private ResponseBuilder() {
    }

    public static <T> ResponseEntity<Response<T>> success(T payload) {
        return success(payload, HttpStatus.OK);
    }

    public static <T> ResponseEntity<Response<T>> success(T payload, HttpStatus status) {
        return build(status, payload, null, null);
    }

    public static <T> ResponseEntity<Response<T>> success(T payload, Object metadata) {
        return build(HttpStatus.OK, payload, null, metadata);
    }

    public static <T> ResponseEntity<Response<T>> success(T payload, HttpStatus status, Object metadata) {
        return build(status, payload, null, metadata);
    }

    public static <T> ResponseEntity<Response<T>> error(Object errors, HttpStatus status) {
        return build(status, null, errors, null);
    }

    public static <T> ResponseEntity<Response<T>> error(Object errors, Object metadata, HttpStatus status) {
        return build(status, null, errors, metadata);
    }

    private static <T> ResponseEntity<Response<T>> build(HttpStatus status, T payload, Object errors, Object metadata) {

        Response<T> response = new Response<>();
        response.setStatus(status);
        response.setPayload(payload);
        response.setErrors(errors);
        response.setMetadata(metadata);
        response.setUuid(UUID.randomUUID().toString());

        return new ResponseEntity<>(response, status);
    }
}
