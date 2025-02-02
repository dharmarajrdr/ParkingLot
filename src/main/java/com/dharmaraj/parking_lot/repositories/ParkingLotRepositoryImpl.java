package com.dharmaraj.parking_lot.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.dharmaraj.parking_lot.models.Gate;
import com.dharmaraj.parking_lot.models.ParkingLot;

public class ParkingLotRepositoryImpl implements ParkingLotRepository {

    private static final Map<Long, ParkingLot> parkingLotsById = new HashMap<>();
    private static final Map<Long, ParkingLot> parkingLotsByGateId = new HashMap<>();

    @Override
    public Optional<ParkingLot> getParkingLotByGateId(long gateId) {
        return parkingLotsByGateId.containsKey(gateId) ? Optional.of(parkingLotsByGateId.get(gateId)) : Optional.empty();
    }

    @Override
    public Optional<ParkingLot> getParkingLotById(long id) {
        return parkingLotsById.containsKey(id) ? Optional.of(parkingLotsById.get(id)) : Optional.empty();
    }

    @Override
    public ParkingLot save(ParkingLot parkingLot) {
        long id = parkingLot.getId();
        parkingLotsById.put(id, parkingLot);
        List<Gate> gates = parkingLot.getGates();
        for(Gate gate: gates) {
            long gateId = gate.getId();
            parkingLotsByGateId.put(gateId, parkingLot);
        }
        return parkingLot;
    }
    
}
