package com.dharmaraj.parking_lot.strategies.assignment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import com.dharmaraj.parking_lot.models.FloorStatus;
import com.dharmaraj.parking_lot.models.ParkingFloor;
import com.dharmaraj.parking_lot.models.ParkingLot;
import com.dharmaraj.parking_lot.models.ParkingSpot;
import com.dharmaraj.parking_lot.models.ParkingSpotStatus;
import com.dharmaraj.parking_lot.models.VehicleType;

public class FloorWithFewSlotsStrategy implements SpotAssignmentStrategy {

    private List<ParkingSpot> getAvailableSpots(ParkingFloor parkingFloor, VehicleType vehicleType) {

        List<ParkingSpot> parkingSpots = parkingFloor.getSpots();
        List<ParkingSpot> availableSpots = new ArrayList<>();
        for (ParkingSpot parkingSpot : parkingSpots) {
            if (parkingSpot.getSupportedVehicleType().equals(vehicleType)
                    && parkingSpot.getStatus().equals(ParkingSpotStatus.AVAILABLE)) {
                availableSpots.add(parkingSpot);
            }
        }
        return availableSpots;
    }

    private Map<ParkingFloor, List<ParkingSpot>> getAvailableSlotsInAllFloor(List<ParkingFloor> floors,
            VehicleType vehicleType) {

        Map<ParkingFloor, List<ParkingSpot>> availableSlots = new HashMap<>();
        for (ParkingFloor parkingFloor : floors) {
            if (parkingFloor.getStatus().equals(FloorStatus.OPERATIONAL)) {
                List<ParkingSpot> slots = getAvailableSpots(parkingFloor, vehicleType);
                if (slots.isEmpty()) {
                    continue;
                }
                availableSlots.put(parkingFloor, slots);
            }
        }
        return availableSlots;
    }

    private List<ParkingFloor> getFloorsWithLeastAvailableSpots(
            Map<ParkingFloor, List<ParkingSpot>> availableSlotsInAllFloor) {

        AtomicInteger leastNumberOfSpots = new AtomicInteger(Integer.MAX_VALUE);
        List<ParkingFloor> floorsWithLeastAvailableSpots = new ArrayList<>();
        availableSlotsInAllFloor.forEach((parkingFloor, parkingSpots) -> {
            int numberOfSpots = parkingSpots.size();
            if (leastNumberOfSpots.get() > numberOfSpots) {
                leastNumberOfSpots.set(numberOfSpots);
                floorsWithLeastAvailableSpots.clear(); // Reset list since a new minimum is found
            }

            if (leastNumberOfSpots.get() == numberOfSpots) {
                floorsWithLeastAvailableSpots.add(parkingFloor);
            }
        });
        return floorsWithLeastAvailableSpots;
    }

    private Optional<ParkingFloor> getLowestFloor(List<ParkingFloor> floors) {

        int lowestFloorNumber = Integer.MAX_VALUE;
        ParkingFloor lowestParkingFloor = null;
        for (ParkingFloor parkingFloor : floors) {
            if (lowestFloorNumber > parkingFloor.getFloorNumber()) {
                lowestFloorNumber = parkingFloor.getFloorNumber();
                lowestParkingFloor = parkingFloor;
            }
        }
        return lowestParkingFloor == null ? Optional.empty() : Optional.of(lowestParkingFloor);
    }

    private Optional<ParkingSpot> getNearestParkingSpot(List<ParkingSpot> parkingSpots) {

        long number = Integer.MAX_VALUE;
        ParkingSpot nearestSpot = null;
        for (ParkingSpot parkingSpot : parkingSpots) {
            if (parkingSpot.getId() < number) {
                number = parkingSpot.getId();
                nearestSpot = parkingSpot;
            }
        }
        return nearestSpot == null ? Optional.empty() : Optional.of(nearestSpot);
    }

    @Override
    public Optional<ParkingSpot> assignSpot(ParkingLot parkingLot, VehicleType vehicleType) {

        Map<ParkingFloor, List<ParkingSpot>> availableSlotsInAllFloor = getAvailableSlotsInAllFloor(
                parkingLot.getParkingFloors(), vehicleType);

        List<ParkingFloor> floors = getFloorsWithLeastAvailableSpots(availableSlotsInAllFloor);

        Optional<ParkingFloor> optionalLowestFloor = getLowestFloor(floors);
        if (optionalLowestFloor.isEmpty()) {
            return Optional.empty();
        }

        ParkingFloor parkingFloor = optionalLowestFloor.get();
        List<ParkingSpot> parkingSpots = availableSlotsInAllFloor.get(parkingFloor);
        if (parkingSpots == null || parkingSpots.isEmpty()) {
            return Optional.empty();
        }

        Optional<ParkingSpot> optionalParkingSpot = getNearestParkingSpot(parkingSpots);
        optionalParkingSpot.get().setStatus(ParkingSpotStatus.OCCUPIED);
        return optionalParkingSpot;
    }

}
