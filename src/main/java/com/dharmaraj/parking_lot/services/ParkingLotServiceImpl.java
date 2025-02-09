package com.dharmaraj.parking_lot.services;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.dharmaraj.parking_lot.exceptions.InvalidParkingLotException;
import com.dharmaraj.parking_lot.models.FloorStatus;
import com.dharmaraj.parking_lot.models.ParkingFloor;
import com.dharmaraj.parking_lot.models.ParkingLot;
import com.dharmaraj.parking_lot.models.ParkingSpot;
import com.dharmaraj.parking_lot.models.ParkingSpotStatus;
import com.dharmaraj.parking_lot.models.VehicleType;
import com.dharmaraj.parking_lot.repositories.ParkingLotRepository;

public class ParkingLotServiceImpl implements ParkingLotService {

    private ParkingLotRepository parkingLotRepository;

    public ParkingLotServiceImpl(ParkingLotRepository parkingLotRepository) {
        this.parkingLotRepository = parkingLotRepository;
    }

    private static int calculateAvailableSpotsByVehicleType(ParkingFloor parkingFloor, VehicleType vehicleType) {
        int count = 0;
        if (parkingFloor == null || vehicleType == null || !parkingFloor.getStatus().equals(FloorStatus.OPERATIONAL)) {
            return count;
        }

        for (ParkingSpot spot : parkingFloor.getSpots()) {
            if (spot.getSupportedVehicleType().equals(vehicleType) && spot.getStatus().equals(ParkingSpotStatus.AVAILABLE)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public Map<ParkingFloor, Map<String, Integer>> getParkingLotCapacity(long parkingLotId, List<Long> parkingFloorIds, List<VehicleType> inputVehicleTypes) throws InvalidParkingLotException {
        Optional<ParkingLot> parkingLotOptional = parkingLotRepository.getParkingLotById(parkingLotId);
        if (parkingLotOptional.isEmpty()) {
            throw new InvalidParkingLotException("Invalid Parking Lot Id , Please try again...");
        }

        ParkingLot parkingLot = parkingLotOptional.get();
        Map<ParkingFloor, Map<String, Integer>> map = new HashMap<>();
        Set<Long> parkingFloorIdSet = (parkingFloorIds == null) ? new HashSet<>() : new HashSet<>(parkingFloorIds);

        if (inputVehicleTypes == null || inputVehicleTypes.isEmpty()) {
            inputVehicleTypes = Arrays.asList(VehicleType.values());
        }

        for (ParkingFloor parkingFloor : parkingLot.getParkingFloors()) {
            if (parkingFloorIdSet.isEmpty() && !parkingFloorIdSet.contains(parkingFloor.getId())) {
                continue;
            }
            Map<String, Integer> vehicleTypeMap = new HashMap<>();
            for (VehicleType vehicleType : inputVehicleTypes) {
                vehicleTypeMap.put(vehicleType.name(), calculateAvailableSpotsByVehicleType(parkingFloor, vehicleType));
            }
            map.put(parkingFloor, vehicleTypeMap);
        }
        return map;
    }
}
