package com.dharmaraj.parking_lot.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.dharmaraj.parking_lot.dtos.GetParkingLotCapacityRequestDto;
import com.dharmaraj.parking_lot.dtos.GetParkingLotCapacityResponseDto;
import com.dharmaraj.parking_lot.dtos.Response;
import com.dharmaraj.parking_lot.dtos.ResponseStatus;
import com.dharmaraj.parking_lot.exceptions.InvalidParkingLotException;
import com.dharmaraj.parking_lot.models.ParkingFloor;
import com.dharmaraj.parking_lot.models.VehicleType;
import com.dharmaraj.parking_lot.services.ParkingLotService;


public class ParkingLotController {

    private final ParkingLotService parkingLotService;

    public ParkingLotController(ParkingLotService parkingLotService) {
        this.parkingLotService = parkingLotService;
    }

    public GetParkingLotCapacityResponseDto getParkingLotCapacity(GetParkingLotCapacityRequestDto getParkingLotCapacityRequestDto) {

        long parkingLotId = getParkingLotCapacityRequestDto.getParkingLotId();
        List<Long> parkingFloors = getParkingLotCapacityRequestDto.getParkingFloorIds();
        List<VehicleType> vehicleTypes = Optional.ofNullable(getParkingLotCapacityRequestDto.getVehicleTypes()).orElse(List.of()).stream().map((str) -> VehicleType.valueOf(str)).toList();
        GetParkingLotCapacityResponseDto getParkingLotCapacityResponseDto = new GetParkingLotCapacityResponseDto();
        Response response;
        try {
            Map<ParkingFloor, Map<String, Integer>> capacity = this.parkingLotService.getParkingLotCapacity(parkingLotId, parkingFloors, vehicleTypes);
            getParkingLotCapacityResponseDto.setCapacityMap(capacity);
            response = new Response(ResponseStatus.SUCCESS, "Successfully fetched the capacity");
            getParkingLotCapacityResponseDto.setResponse(response);
        } catch (InvalidParkingLotException e) {
            response = new Response(ResponseStatus.FAILURE, e.getMessage());
        }
        getParkingLotCapacityResponseDto.setResponse(response);
        return getParkingLotCapacityResponseDto;
    }

}
