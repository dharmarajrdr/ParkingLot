package com.dharmaraj.parking_lot.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Gate extends BaseModel{

    private String name;
    private GateType type;
    private ParkingAttendant parkingAttendant;
}
