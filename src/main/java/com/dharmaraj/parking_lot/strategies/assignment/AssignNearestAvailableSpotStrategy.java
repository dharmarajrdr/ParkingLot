package com.dharmaraj.parking_lot.strategies.assignment;

import java.util.Optional;

import com.dharmaraj.parking_lot.models.FloorStatus;
import com.dharmaraj.parking_lot.models.ParkingFloor;
import com.dharmaraj.parking_lot.models.ParkingLot;
import com.dharmaraj.parking_lot.models.ParkingSpot;
import com.dharmaraj.parking_lot.models.ParkingSpotStatus;
import com.dharmaraj.parking_lot.models.VehicleType;

public class AssignNearestAvailableSpotStrategy implements SpotAssignmentStrategy {

    @Override
    public Optional<ParkingSpot> assignSpot(ParkingLot parkingLot, VehicleType vehicleType) {
        for (ParkingFloor parkingFloor : parkingLot.getParkingFloors()) {
            if (parkingFloor.getStatus().equals(FloorStatus.OPERATIONAL)) {
                for (ParkingSpot spot : parkingFloor.getSpots()) {
                    if (spot.getStatus().equals(ParkingSpotStatus.AVAILABLE) && spot.getSupportedVehicleType().equals(vehicleType)) {
                        spot.setStatus(ParkingSpotStatus.OCCUPIED);
                        return Optional.of(spot);
                    }
                }
            }
        }
        return Optional.empty();
    }
}
