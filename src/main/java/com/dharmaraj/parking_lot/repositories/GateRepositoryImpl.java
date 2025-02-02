package com.dharmaraj.parking_lot.repositories;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.dharmaraj.parking_lot.models.Gate;

public class GateRepositoryImpl implements GateRepository {

    private static final Map<Long, Gate> allGates = new HashMap<>();
    
    @Override
    public Optional<Gate> findById(long gateId) {
        if(allGates.containsKey(gateId)) {
            return Optional.of(allGates.get(gateId));
        }
        return Optional.empty();
    }

    @Override
    public Gate save(Gate gate) {
        long gateId = gate.getId();
        allGates.put(gateId, gate);
        return gate;
    }
    
}
