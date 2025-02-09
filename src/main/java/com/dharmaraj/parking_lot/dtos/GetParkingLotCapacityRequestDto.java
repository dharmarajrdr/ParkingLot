package com.dharmaraj.parking_lot.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetParkingLotCapacityRequestDto {

    private long parkingLotId;
    private List<Long> parkingFloorIds;
    private List<String> vehicleTypes;
}
