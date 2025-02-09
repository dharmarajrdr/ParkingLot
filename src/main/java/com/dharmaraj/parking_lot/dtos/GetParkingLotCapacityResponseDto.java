package com.dharmaraj.parking_lot.dtos;

import com.dharmaraj.parking_lot.models.ParkingFloor;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class GetParkingLotCapacityResponseDto {

    private Response response;
    private Map<ParkingFloor, Map<String, Integer>> capacityMap;
}
