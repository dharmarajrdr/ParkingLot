package com.dharmaraj.parking_lot.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingSpot extends BaseModel{

    private int number;
    private VehicleType supportedVehicleType;
    private ParkingSpotStatus status;
}
