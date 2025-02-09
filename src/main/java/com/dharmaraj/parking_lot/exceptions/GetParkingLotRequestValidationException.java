package com.dharmaraj.parking_lot.exceptions;

public class GetParkingLotRequestValidationException extends Exception {
    public GetParkingLotRequestValidationException() {
    }

    public GetParkingLotRequestValidationException(String message) {
        super(message);
    }
}
