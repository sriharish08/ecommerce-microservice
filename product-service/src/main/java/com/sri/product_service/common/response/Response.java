package com.sri.product_service.common.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response<T> {

    private HttpStatus status;
    private T payload;
    private Object errors;
    private Object metadata;
    private String uuid;

    public static <T> Response<T> ok() {
        return status(HttpStatus.OK);
    }

    public static <T> Response<T> created() {
        return status(HttpStatus.CREATED);
    }

    public static <T> Response<T> badRequest() {
        return status(HttpStatus.BAD_REQUEST);
    }

    public static <T> Response<T> unauthorized() {
        return status(HttpStatus.UNAUTHORIZED);
    }

    public static <T> Response<T> notFound() {
        return status(HttpStatus.NOT_FOUND);
    }

    public static <T> Response<T> conflict() {
        return status(HttpStatus.CONFLICT);
    }

    public static <T> Response<T> serviceUnavailable() {
        return status(HttpStatus.SERVICE_UNAVAILABLE);
    }

    public static <T> Response<T> internalServerError() {
        return status(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static <T> Response<T> methodNotSupported() {
        return status(HttpStatus.METHOD_NOT_ALLOWED);
    }

    public static <T> Response<T> status(HttpStatus status) {
        Response<T> response = new Response<>();
        response.setStatus(status);
        return response;
    }

    public void addErrorMsgToResponse(String errorMsg, Exception ex) {
        ResponseError error = new ResponseError();
        error.setMessage(errorMsg);
        error.setDetails(ex.getMessage());
        error.setTimestamp(LocalDateTime.now());
        setErrors(error);
    }

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ResponseError {
        private String message;
        private String details;
        private LocalDateTime timestamp;
    }
}
