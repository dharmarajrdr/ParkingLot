# Generate Ticket for Parking Lot System

You are building a Parking Lot Management System. As a part of this system, you need to build a functionality using which customers can generate a ticket while entering the parking lot.

Your task is to implement the following functionality.

#### Requirements

1. A ticket will be generated for a customer when they enter the parking lot.
2. Tickets can be generated only at entry gates. If a customer tries to generate a ticket at any exit gate, then the system should throw an exception.
3. Generate ticket request will contain gate id, vehicle number, and vehicle type.
4. If the vehicle doesn't exist in our database, we need to create a new vehicle entry in the database.
5. As a part of ticket generation we also need to assign a spot to the vehicle. Following are the rules for assigning a spot to a vehicle.
   - When a vehicle arrives, the system should assign it to the floor with the least number
     of available spots for that vehicle type.
   - If there are multiple floors with same number of available spots, the system should
     assign the vehicle to the floor with the lowest floor number.
   - If a floor is operational, then only it should be considered, otherwise the system should ignore that floor.
   - Once a floor has been selected, the system should assign the vehicle to the nearest available spot of that vehicle type on that floor.
   - If there are no available spots on any floor, the system should not issue a ticket.

#### Instructions

- Refer the `generateTicket` method inside `TicketController` class.
- Refer the `GenerateTicketRequestDto` and `GenerateTicketResponseDto` for understanding the expected input and output to the functionality.
- Refer the models package to understand the models.
- Implement the `TicketService`, `VehicleRepository`, `TicketRepository`, `ParkingLotRepository` and `GateRepository` interfaces to achieve the above requirements.
- We need in memory database implementation for this assignment.
- Refer the `TestTicketController` class to understand the test cases that will be used to evaluate your solution.
- Do not modify existing methods and their parameters for interfaces, feel free to add more methods if required.
