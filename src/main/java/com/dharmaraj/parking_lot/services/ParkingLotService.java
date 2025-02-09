package com.dharmaraj.parking_lot.services;

import java.util.List;
import java.util.Map;

import com.dharmaraj.parking_lot.exceptions.InvalidParkingLotException;
import com.dharmaraj.parking_lot.models.ParkingFloor;
import com.dharmaraj.parking_lot.models.VehicleType;

public interface ParkingLotService {

    // Do not modify the method signature, feel free to add more methods

    public Map<ParkingFloor, Map<String, Integer>> getParkingLotCapacity(long parkingLotId, List<Long> parkingFloors, List<VehicleType> vehicleTypes) throws InvalidParkingLotException, InvalidParkingLotException;

}
