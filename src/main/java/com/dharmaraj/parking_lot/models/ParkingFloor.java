package com.dharmaraj.parking_lot.models;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingFloor extends BaseModel{

    private List<ParkingSpot> spots;
    private int floorNumber;
    private FloorStatus status;
}
