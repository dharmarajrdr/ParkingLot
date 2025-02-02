package com.dharmaraj.parking_lot.controllers;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import com.dharmaraj.parking_lot.dtos.GenerateTicketRequestDto;
import com.dharmaraj.parking_lot.dtos.GenerateTicketResponseDto;
import com.dharmaraj.parking_lot.dtos.ResponseStatus;
import com.dharmaraj.parking_lot.models.FloorStatus;
import com.dharmaraj.parking_lot.models.Gate;
import com.dharmaraj.parking_lot.models.GateType;
import com.dharmaraj.parking_lot.models.ParkingAttendant;
import com.dharmaraj.parking_lot.models.ParkingFloor;
import com.dharmaraj.parking_lot.models.ParkingLot;
import com.dharmaraj.parking_lot.models.ParkingSpot;
import com.dharmaraj.parking_lot.models.ParkingSpotStatus;
import com.dharmaraj.parking_lot.models.VehicleType;
import com.dharmaraj.parking_lot.respositories.GateRepository;
import com.dharmaraj.parking_lot.respositories.ParkingLotRepository;
import com.dharmaraj.parking_lot.respositories.TicketRepository;
import com.dharmaraj.parking_lot.respositories.VehicleRepository;
import com.dharmaraj.parking_lot.services.TicketService;
import com.dharmaraj.parking_lot.strategies.assignment.SpotAssignmentStrategy;

public class TestTicketController {

    private GateRepository gateRepository;
    private VehicleRepository vehicleRepository;
    private SpotAssignmentStrategy spotAssignmentStrategy;
    private ParkingLotRepository parkingLotRepository;
    private TicketRepository ticketRepository;
    private TicketService ticketService;
    private TicketController ticketController;

    @BeforeEach
    public void setupTest() throws Exception {
        initializeComponents();
    }

    private void initializeComponents() throws Exception {
        initializeRepositories();
        initializeSpotAssignmentStrategy();
        initializeTicketService();
        initializeTicketController();
    }

    private <T> T createInstance(Class<T> interfaceClass, Reflections reflections) throws Exception {
        Set<Class<? extends T>> implementations = reflections.getSubTypesOf(interfaceClass);
        if (implementations.isEmpty()) {
            throw new Exception("No implementation for " + interfaceClass.getSimpleName() + " found");
        }

        Class<? extends T> implementationClass = implementations.iterator().next();
        Constructor<? extends T> constructor = implementationClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

    @SuppressWarnings("unchecked")
    private <T> T createInstanceWithArgs(Class<T> interfaceClass, Reflections reflections, List<Object> dependencies) throws Exception {
        Set<Class<? extends T>> implementations = reflections.getSubTypesOf(interfaceClass);
        if (implementations.isEmpty()) {
            throw new Exception("No implementation for " + interfaceClass.getSimpleName() + " found");
        }
        Class<? extends T> implementationClass = implementations.iterator().next();
        Constructor<?>[] constructors = implementationClass.getConstructors();
        Constructor<?> constructor = Arrays.stream(constructors)
                .filter(constructor1 -> constructor1.getParameterCount() == dependencies.size())
                .findFirst().orElseThrow(() -> new Exception("No constructor with " + dependencies.size() + " arguments found"));
        constructor.setAccessible(true);
        Object[] args = new Object[constructor.getParameterCount()];
        for (int i = 0; i < constructor.getParameterCount(); i++) {
            for (Object dependency : dependencies) {
                if (constructor.getParameterTypes()[i].isInstance(dependency)) {
                    args[i] = dependency;
                    break;
                }
            }
        }
        return (T) constructor.newInstance(args);
    }

    private void initializeRepositories() throws Exception {
        Reflections repositoryReflections = new Reflections(TicketRepository.class.getPackageName(), new SubTypesScanner(false));
        this.gateRepository = createInstance(GateRepository.class, repositoryReflections);
        this.vehicleRepository = createInstance(VehicleRepository.class, repositoryReflections);
        this.parkingLotRepository = createInstance(ParkingLotRepository.class, repositoryReflections);
        this.ticketRepository = createInstance(TicketRepository.class, repositoryReflections);
    }

    private void initializeSpotAssignmentStrategy() throws Exception {
        Reflections strategyReflections = new Reflections(SpotAssignmentStrategy.class.getPackageName(), new SubTypesScanner(false));
        this.spotAssignmentStrategy = createInstance(SpotAssignmentStrategy.class, strategyReflections);
    }

    private void initializeTicketService() throws Exception {
        Reflections serviceReflections = new Reflections(TicketService.class.getPackageName(), new SubTypesScanner(false));
        this.ticketService = createInstanceWithArgs(TicketService.class, serviceReflections, Arrays.asList(this.gateRepository, this.vehicleRepository, this.spotAssignmentStrategy, this.parkingLotRepository, this.ticketRepository));
    }

    private void initializeTicketController() {
        this.ticketController = new TicketController(this.ticketService);
    }
    @Test
    public void testIssueTicketWith1AvailableParkingSpot() {
        insertDummyData(1, Map.of(VehicleType.CAR, 1), 1, 1, "Bangalore");
        GenerateTicketRequestDto requestDto = new GenerateTicketRequestDto();
        requestDto.setGateId(1);
        requestDto.setRegistrationNumber("KA-01-HH-1234");
        requestDto.setVehicleType("CAR");
        GenerateTicketResponseDto responseDto = ticketController.generateTicket(requestDto);
        assertEquals(responseDto.getResponseStatus(), ResponseStatus.SUCCESS, "Response status should be success");
        assertNotNull(responseDto.getTicket(), "Ticket should not be null");
    }

    @Test
    public void testIssueTicketWith2AvailableParkingSpots() {
        insertDummyData(1, Map.of(VehicleType.CAR, 2), 1, 1, "Bangalore");
        GenerateTicketRequestDto requestDto = new GenerateTicketRequestDto();
        requestDto.setGateId(1);
        requestDto.setRegistrationNumber("KA-01-HH-1234");
        requestDto.setVehicleType("CAR");
        GenerateTicketResponseDto responseDto = ticketController.generateTicket(requestDto);
        assertEquals(responseDto.getResponseStatus(), ResponseStatus.SUCCESS, "Response status should be success");
        assertNotNull(responseDto.getTicket(), "Ticket should not be null and assigned to the 1st car");

        requestDto = new GenerateTicketRequestDto();
        requestDto.setGateId(1);
        requestDto.setRegistrationNumber("KA-01-HH-1235");
        requestDto.setVehicleType("CAR");
        responseDto = ticketController.generateTicket(requestDto);
        assertEquals(responseDto.getResponseStatus(), ResponseStatus.SUCCESS, "Response status should be success");
        assertNotNull(responseDto.getTicket(), "Ticket should not be null and assigned to the 2nd car");
    }

    @Test
    public void testIssueTicketWithNoAvailableSpots(){
        insertDummyData(1, Map.of(VehicleType.CAR, 1), 1, 1, "Bangalore");
        GenerateTicketRequestDto requestDto = new GenerateTicketRequestDto();
        requestDto.setGateId(1);
        requestDto.setRegistrationNumber("KA-01-HH-1234");
        requestDto.setVehicleType("CAR");
        GenerateTicketResponseDto responseDto = ticketController.generateTicket(requestDto);
        assertEquals(responseDto.getResponseStatus(), ResponseStatus.SUCCESS, "Response status should be success");
        assertNotNull(responseDto.getTicket(), "Ticket should not be null");

        requestDto = new GenerateTicketRequestDto();
        requestDto.setGateId(1);
        requestDto.setRegistrationNumber("KA-01-HH-1235");
        requestDto.setVehicleType("CAR");
        responseDto = ticketController.generateTicket(requestDto);
        assertEquals(responseDto.getResponseStatus(), ResponseStatus.FAILURE, "Response status should be failure, as no parking spot is available");
        assertNull(responseDto.getTicket(), "Ticket should be null");
    }

    @Test
    public void testIssueTicketWithFromExitGate(){
        insertDummyData(1, Map.of(VehicleType.CAR, 1), 1, 1, "Bangalore");
        GenerateTicketRequestDto requestDto = new GenerateTicketRequestDto();
        requestDto.setGateId(2);
        requestDto.setRegistrationNumber("KA-01-HH-1234");
        requestDto.setVehicleType("CAR");
        GenerateTicketResponseDto responseDto = ticketController.generateTicket(requestDto);
        assertEquals(responseDto.getResponseStatus(), ResponseStatus.FAILURE, "Response status should be failure, as ticket cannot be issued from exit gate");
        assertNull(responseDto.getTicket(), "Ticket should be null");
    }

    @Test
    public void testIssueTicketFromNonExistingGate(){
        insertDummyData(1, Map.of(VehicleType.CAR, 1), 1, 1, "Bangalore");
        GenerateTicketRequestDto requestDto = new GenerateTicketRequestDto();
        requestDto.setGateId(3);
        requestDto.setRegistrationNumber("KA-01-HH-1234");
        requestDto.setVehicleType("CAR");
        GenerateTicketResponseDto responseDto = ticketController.generateTicket(requestDto);
        assertEquals(responseDto.getResponseStatus(), ResponseStatus.FAILURE, "Response status should be failure, as ticket cannot be issued from a non-existing gate");
        assertNull(responseDto.getTicket(), "Ticket should be null");
    }

    @Test
    public void testIssueTicketWith3Floors2CarsOn1stAnd2ndFloorAnd1On3rdFloor(){
        insertDummyData(3, Map.of(VehicleType.CAR, 2), 1, 1, "Bangalore");
        GenerateTicketRequestDto requestDto = new GenerateTicketRequestDto();
        requestDto.setGateId(1);
        requestDto.setRegistrationNumber("KA-01-HH-1234");
        requestDto.setVehicleType("CAR");
        GenerateTicketResponseDto responseDto = ticketController.generateTicket(requestDto);
        assertEquals(responseDto.getResponseStatus(), ResponseStatus.SUCCESS, "Response status should be success");
        assertNotNull(responseDto.getTicket(), "Ticket should not be null"); // Lead to 1st floor

        requestDto = new GenerateTicketRequestDto();
        requestDto.setGateId(1);
        requestDto.setRegistrationNumber("KA-01-HH-1235");
        requestDto.setVehicleType("CAR");
        responseDto = ticketController.generateTicket(requestDto);
        assertEquals(responseDto.getResponseStatus(), ResponseStatus.SUCCESS, "Response status should be success");
        assertNotNull(responseDto.getTicket(), "Ticket should not be null"); //Lead to 2nd floor

        requestDto = new GenerateTicketRequestDto();
        requestDto.setGateId(1);
        requestDto.setRegistrationNumber("KA-01-HH-1236");
        requestDto.setVehicleType("CAR");
        responseDto = ticketController.generateTicket(requestDto);
        assertEquals(responseDto.getResponseStatus(), ResponseStatus.SUCCESS, "Response status should be success");
        assertNotNull(responseDto.getTicket(), "Ticket should not be null"); //Lead to 3rd floor

        requestDto = new GenerateTicketRequestDto();
        requestDto.setGateId(1);
        requestDto.setRegistrationNumber("KA-01-HH-1237");
        requestDto.setVehicleType("CAR");
        responseDto = ticketController.generateTicket(requestDto);
        assertEquals(responseDto.getResponseStatus(), ResponseStatus.SUCCESS, "Response status should be success");
        assertNotNull(responseDto.getTicket(), "Ticket should not be null"); //Lead to 1st floor

        requestDto = new GenerateTicketRequestDto();
        requestDto.setGateId(1);
        requestDto.setRegistrationNumber("KA-01-HH-1238");
        requestDto.setVehicleType("CAR");
        responseDto = ticketController.generateTicket(requestDto);
        assertEquals(responseDto.getResponseStatus(), ResponseStatus.SUCCESS, "Response status should be success");
        assertNotNull(responseDto.getTicket(), "Ticket should not be null"); //Lead to 2nd floor

        Optional<ParkingLot> parkingLotOptional = parkingLotRepository.getParkingLotById(1);
        ParkingLot parkingLot = parkingLotOptional.get();
        ParkingFloor parkingFloor = parkingLot.getParkingFloors().stream().filter(floor -> floor.getId() == 3).findFirst().get();
        int count = 0;
        for (ParkingSpot spot : parkingFloor.getSpots()) {
            if(spot.getStatus().equals(ParkingSpotStatus.AVAILABLE) && spot.getSupportedVehicleType().equals(VehicleType.CAR)){
                count++;
                break;
            }
        }
        assertEquals(count, 1, "There should be 1 available spot on 3rd floor as per the strategy");

        requestDto = new GenerateTicketRequestDto();
        requestDto.setGateId(1);
        requestDto.setRegistrationNumber("KA-01-HH-1239");
        requestDto.setVehicleType("CAR");
        responseDto = ticketController.generateTicket(requestDto);
        assertEquals(responseDto.getResponseStatus(), ResponseStatus.SUCCESS, "Response status should be success");
        assertNotNull(responseDto.getTicket(), "Ticket should not be null"); //Lead to 3rd floor


        parkingLotOptional = parkingLotRepository.getParkingLotById(1);
        parkingLot = parkingLotOptional.get();
        parkingFloor = parkingLot.getParkingFloors().stream().filter(floor -> floor.getId() == 3).findFirst().get();
        count = 0;
        for (ParkingSpot spot : parkingFloor.getSpots()) {
            if(spot.getStatus().equals(ParkingSpotStatus.AVAILABLE) && spot.getSupportedVehicleType().equals(VehicleType.CAR)){
                count++;
                break;
            }
        }
        assertEquals(count, 0, "There should be no available spots on 3rd floor as per the strategy");
    }

    @Test
    public void testIssueWithWith1FloorUnderMaintenance(){
        insertDummyData(3, Map.of(VehicleType.CAR, 1), 1, 1, "Bangalore");
        Optional<ParkingLot> parkingLotOptional = parkingLotRepository.getParkingLotById(1);
        ParkingLot parkingLot = parkingLotOptional.get();
        ParkingFloor parkingFloor = parkingLot.getParkingFloors().stream().filter(floor -> floor.getId() == 2).findFirst().get();
        parkingFloor.setStatus(FloorStatus.UNDER_MAINTENANCE);
        GenerateTicketRequestDto requestDto = new GenerateTicketRequestDto();
        requestDto.setGateId(1);
        requestDto.setRegistrationNumber("KA-01-HH-1234");
        requestDto.setVehicleType("CAR");
        GenerateTicketResponseDto responseDto = ticketController.generateTicket(requestDto);
        assertEquals(responseDto.getResponseStatus(), ResponseStatus.SUCCESS, "Response status should be success");
        assertNotNull(responseDto.getTicket(), "Ticket should not be null"); // Lead to 1st floor

        requestDto = new GenerateTicketRequestDto();
        requestDto.setGateId(1);
        requestDto.setRegistrationNumber("KA-01-HH-1235");
        requestDto.setVehicleType("CAR");
        responseDto = ticketController.generateTicket(requestDto);
        assertEquals(responseDto.getResponseStatus(), ResponseStatus.SUCCESS, "Response status should be success");
        assertNotNull(responseDto.getTicket(), "Ticket should not be null"); //Lead to 3rd floor

        requestDto = new GenerateTicketRequestDto();
        requestDto.setGateId(1);
        requestDto.setRegistrationNumber("KA-01-HH-1235");
        requestDto.setVehicleType("CAR");
        responseDto = ticketController.generateTicket(requestDto);
        assertEquals(responseDto.getResponseStatus(), ResponseStatus.FAILURE, "Response status should be failure, as no parking spot is available");
        assertNull(responseDto.getTicket(), "Ticket should be null");
    }

    public void insertDummyData(int numOfFloors, Map<VehicleType, Integer> numOfSpotsPerVehicleTypePerFloor, int numOfEntryGates, int numOfExitGates, String address) {
        ParkingLot parkingLot = setupParkingLot(numOfFloors, numOfSpotsPerVehicleTypePerFloor, numOfEntryGates, numOfExitGates, address);
        parkingLotRepository.save(parkingLot);
        parkingLot.getGates().forEach(gate -> gateRepository.save(gate));
    }
    public ParkingLot setupParkingLot(int numOfFloors, Map<VehicleType, Integer> numOfSpotsPerVehicleTypePerFloor, int numOfEntryGates, int numOfExitGates, String address){
        int parkingSpotId = 1;
        int parkingFloorId = 1;
        List<ParkingFloor> parkingFloors = new ArrayList<>();
        for(int i=0; i<numOfFloors; i++){
            List<ParkingSpot> spots = new ArrayList<>();
            for(Map.Entry<VehicleType, Integer> entry: numOfSpotsPerVehicleTypePerFloor.entrySet()){
                for(int j=0; j<entry.getValue(); j++){
                    parkingSpotId++;
                    ParkingSpot parkingSpot = new ParkingSpot();
                    parkingSpot.setId(parkingSpotId);
                    parkingSpot.setSupportedVehicleType(entry.getKey());
                    parkingSpot.setStatus(ParkingSpotStatus.AVAILABLE);
                    parkingSpot.setNumber(parkingSpotId);
                    spots.add(parkingSpot);
                }
            }
            ParkingFloor parkingFloor = new ParkingFloor();
            parkingFloor.setId(parkingFloorId++);
            parkingFloor.setSpots(spots);
            parkingFloor.setFloorNumber(parkingFloorId-1);
            parkingFloor.setStatus(FloorStatus.OPERATIONAL);
            parkingFloors.add(parkingFloor);
        }
        List<Gate> gates = new ArrayList<>();
        int parkingAttendantId = 1;
        for(int i=0; i<numOfEntryGates; i++){
            ParkingAttendant parkingAttendant = new ParkingAttendant();
            parkingAttendant.setId(parkingAttendantId);
            parkingAttendant.setName(String.valueOf(parkingAttendantId));
            parkingAttendant.setEmail(parkingAttendantId+"@gmail.com");
            Gate gate = new Gate();
            gate.setId(parkingAttendantId);
            gate.setName(String.valueOf(parkingAttendantId));
            gate.setType(GateType.ENTRY);
            gate.setParkingAttendant(parkingAttendant);
            gates.add(gate);
            parkingAttendantId++;
        }
        for(int i=0; i<numOfExitGates; i++){
            ParkingAttendant parkingAttendant = new ParkingAttendant();
            parkingAttendant.setId(parkingAttendantId);
            parkingAttendant.setName(String.valueOf(parkingAttendantId));
            parkingAttendant.setEmail(parkingAttendantId+"@gmail.com");
            Gate gate = new Gate();
            gate.setId(parkingAttendantId);
            gate.setName(String.valueOf(parkingAttendantId));
            gate.setType(GateType.EXIT);
            gate.setParkingAttendant(parkingAttendant);
            gates.add(gate);
            parkingAttendantId++;
        }
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setId(1);
        parkingLot.setParkingFloors(parkingFloors);
        parkingLot.setGates(gates);
        parkingLot.setName("Test Parking Lot");
        parkingLot.setAddress(address);
        return parkingLot;
    }
}
