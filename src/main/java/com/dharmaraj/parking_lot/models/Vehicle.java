package com.dharmaraj.parking_lot.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Vehicle extends BaseModel{

    private String registrationNumber;
    private VehicleType vehicleType;
}
