package com.dharmaraj.parking_lot.respositories;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.dharmaraj.parking_lot.models.Vehicle;

public class VehicleRepositoryImpl implements VehicleRepository {

    private static final Map<String, Vehicle> allVehicles = new HashMap<>();

    @Override
    public Optional<Vehicle> getVehicleByRegistrationNumber(String registrationNumber) {
        return allVehicles.containsKey(registrationNumber) ? Optional.of(allVehicles.get(registrationNumber)) : Optional.empty();
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        allVehicles.put(vehicle.getRegistrationNumber(), vehicle);
        return vehicle;
    }
    
}
