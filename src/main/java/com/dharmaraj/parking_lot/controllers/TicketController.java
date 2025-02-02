package com.dharmaraj.parking_lot.controllers;

import com.dharmaraj.parking_lot.dtos.GenerateTicketRequestDto;
import com.dharmaraj.parking_lot.dtos.GenerateTicketResponseDto;
import com.dharmaraj.parking_lot.dtos.ResponseStatus;
import com.dharmaraj.parking_lot.exceptions.InvalidGateException;
import com.dharmaraj.parking_lot.exceptions.InvalidParkingLotException;
import com.dharmaraj.parking_lot.exceptions.ParkingSpotNotAvailableException;
import com.dharmaraj.parking_lot.models.Ticket;
import com.dharmaraj.parking_lot.services.TicketService;

public class TicketController {

    private TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    public GenerateTicketResponseDto generateTicket(GenerateTicketRequestDto requestDto){
        int gateId = requestDto.getGateId();
        String registrationNumber = requestDto.getRegistrationNumber();
        String VehicleType = requestDto.getVehicleType();
        GenerateTicketResponseDto generateTicketResponseDto = new GenerateTicketResponseDto();
        try {
            Ticket ticket = this.ticketService.generateTicket(gateId, registrationNumber, VehicleType);
            generateTicketResponseDto.setTicket(ticket);
            generateTicketResponseDto.setResponseStatus(ResponseStatus.SUCCESS);
        } catch (InvalidGateException | InvalidParkingLotException | ParkingSpotNotAvailableException e) {
            generateTicketResponseDto.setResponseStatus(ResponseStatus.FAILURE);
        }
        return generateTicketResponseDto;
    }
}
