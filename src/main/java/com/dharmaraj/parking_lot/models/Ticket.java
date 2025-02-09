package com.dharmaraj.parking_lot.models;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ticket extends BaseModel {
    
    private Vehicle vehicle;
    private Date entryTime;
    private ParkingSpot parkingSpot;
    private Gate gate;
    private ParkingAttendant parkingAttendant;
}
