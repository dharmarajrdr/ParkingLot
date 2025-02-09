package com.dharmaraj.parking_lot.models;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingLot extends BaseModel{

    private List<ParkingFloor> parkingFloors;
    private List<Gate> gates;
    private String name;
    private String address;
}
