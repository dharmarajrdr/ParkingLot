package com.dharmaraj.parking_lot.repositories;

import com.dharmaraj.parking_lot.models.ParkingLot;

import java.util.Optional;

public interface ParkingLotRepository {
    // Do not modify the method signatures, feel free to add new methods

    public Optional<ParkingLot> getParkingLotByGateId(long gateId);

    public Optional<ParkingLot> getParkingLotById(long id);

    public ParkingLot save(ParkingLot parkingLot);
}
