package com.dharmaraj.parking_lot.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenerateTicketRequestDto {
    private int gateId;
    private String registrationNumber;
    private String vehicleType;
}
