package com.dharmaraj.parking_lot.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.dharmaraj.parking_lot.models.Gate;
import com.dharmaraj.parking_lot.models.ParkingLot;

public class ParkingLotRepositoryImpl implements ParkingLotRepository {

    private final List<ParkingLot> allParkingLots = new ArrayList<>();

    @Override
    public Optional<ParkingLot> getParkingLotByGateId(long gateId) {
        for (ParkingLot parkingLot : allParkingLots) {
            List<Gate> gates = parkingLot.getGates();
            for (Gate gate : gates) {
                if (gate.getId() == gateId) {
                    return Optional.of(parkingLot);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<ParkingLot> getParkingLotById(long id) {
        for (ParkingLot parkingLot : allParkingLots) {
            if (parkingLot.getId() == id) {
                return Optional.of(parkingLot);
            }
        }
        return Optional.empty();
    }

    @Override
    public ParkingLot save(ParkingLot parkingLot) {
        this.allParkingLots.add(parkingLot);
        return parkingLot;
    }
}
