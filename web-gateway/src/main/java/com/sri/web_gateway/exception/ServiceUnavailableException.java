package com.sri.web_gateway.exception;

public class ServiceUnavailableException extends RuntimeException {

    private final String serviceId;

    public ServiceUnavailableException(String serviceId, String message) {
        super(message);
        this.serviceId = serviceId;
    }

    public String getServiceId() {
        return serviceId;
    }
}
