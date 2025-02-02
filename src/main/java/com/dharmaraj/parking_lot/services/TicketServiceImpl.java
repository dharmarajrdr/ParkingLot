package com.dharmaraj.parking_lot.services;

import java.util.Optional;

import com.dharmaraj.parking_lot.exceptions.InvalidGateException;
import com.dharmaraj.parking_lot.exceptions.InvalidParkingLotException;
import com.dharmaraj.parking_lot.exceptions.ParkingSpotNotAvailableException;
import com.dharmaraj.parking_lot.models.Gate;
import com.dharmaraj.parking_lot.models.GateType;
import com.dharmaraj.parking_lot.models.ParkingLot;
import com.dharmaraj.parking_lot.models.ParkingSpot;
import com.dharmaraj.parking_lot.models.Ticket;
import com.dharmaraj.parking_lot.models.Vehicle;
import com.dharmaraj.parking_lot.models.VehicleType;
import com.dharmaraj.parking_lot.repositories.GateRepository;
import com.dharmaraj.parking_lot.repositories.ParkingLotRepository;
import com.dharmaraj.parking_lot.repositories.TicketRepository;
import com.dharmaraj.parking_lot.repositories.VehicleRepository;
import com.dharmaraj.parking_lot.strategies.assignment.SpotAssignmentStrategy;

public class TicketServiceImpl implements TicketService {

    private final GateRepository gateRepository;
    private final VehicleRepository vehicleRepository;
    private final ParkingLotRepository parkingLotRepository;
    private final SpotAssignmentStrategy spotAssignmentStrategy;
    private final TicketRepository ticketRepository;

    public TicketServiceImpl(GateRepository gateRepository, VehicleRepository vehicleRepository, SpotAssignmentStrategy spotAssignmentStrategy, ParkingLotRepository parkingLotRepository, TicketRepository ticketRepository) {
        this.gateRepository = gateRepository;
        this.vehicleRepository = vehicleRepository;
        this.parkingLotRepository = parkingLotRepository;
        this.spotAssignmentStrategy = spotAssignmentStrategy;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Ticket generateTicket(int gateId, String registrationNumber, String vehicleType)
            throws InvalidGateException, InvalidParkingLotException, ParkingSpotNotAvailableException {

        Optional<Gate> optionalGate = this.gateRepository.findById(gateId);
        if (optionalGate.isEmpty()) {
            throw new InvalidGateException("Gate not found");
        }

        Gate gate = optionalGate.get();
        if(gate.getType().equals(GateType.EXIT)) {
            throw new InvalidGateException("");
        }

        Optional<Vehicle> optionalVehicle = this.vehicleRepository.getVehicleByRegistrationNumber(registrationNumber);
        Vehicle vehicle;
        if(optionalVehicle.isEmpty()) {
            vehicle = new Vehicle();
            vehicle.setRegistrationNumber(registrationNumber);
            vehicle.setVehicleType(VehicleType.valueOf(vehicleType));
            this.vehicleRepository.save(vehicle);
        } else {
            vehicle = optionalVehicle.get();
        }

        Optional<ParkingLot> optionalParkingLot = this.parkingLotRepository.getParkingLotByGateId(gateId);
        if(optionalParkingLot.isEmpty()) {
            throw new InvalidParkingLotException("Invalid parking lot");
        }

        ParkingLot parkingLot = optionalParkingLot.get();

        Optional<ParkingSpot> optionalParkingSpot = this.spotAssignmentStrategy.assignSpot(parkingLot, vehicle.getVehicleType());
        if(optionalParkingSpot.isEmpty()) {
            throw new ParkingSpotNotAvailableException("No spot is available.");
        }

        ParkingSpot parkingSpot = optionalParkingSpot.get();

        Ticket ticket = new Ticket();
        ticket.setGate(gate);
        ticket.setVehicle(vehicle);
        ticket.setParkingSpot(parkingSpot);
        ticket.setParkingAttendant(gate.getParkingAttendant());

        this.ticketRepository.save(ticket);
        
        return ticket;
    }

}
