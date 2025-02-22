package com.dharmaraj.parking_lot.services;

import com.dharmaraj.parking_lot.exceptions.InvalidGateException;
import com.dharmaraj.parking_lot.exceptions.InvalidParkingLotException;
import com.dharmaraj.parking_lot.exceptions.ParkingSpotNotAvailableException;
import com.dharmaraj.parking_lot.models.Ticket;

public interface TicketService {
    // Do not modify the method signatures, feel free to add new methods
    public Ticket generateTicket(int gateId, String registrationNumber, String vehicleType) throws InvalidGateException, InvalidParkingLotException, ParkingSpotNotAvailableException;
}
