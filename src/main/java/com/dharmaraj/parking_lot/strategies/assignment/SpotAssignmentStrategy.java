package com.dharmaraj.parking_lot.strategies.assignment;

import com.dharmaraj.parking_lot.models.ParkingLot;
import com.dharmaraj.parking_lot.models.ParkingSpot;
import com.dharmaraj.parking_lot.models.VehicleType;

import java.util.Optional;

public interface SpotAssignmentStrategy {

    Optional<ParkingSpot> assignSpot(ParkingLot parkingLot, VehicleType vehicleType);

}
