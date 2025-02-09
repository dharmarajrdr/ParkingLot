package com.dharmaraj.parking_lot.dtos;

import lombok.Getter;

@Getter
public class Response {
    
    private ResponseStatus responseStatus;
    private String message;

    public Response(ResponseStatus responseStatus, String message) {
        this.responseStatus = responseStatus;
        this.message = message;
    }
}
