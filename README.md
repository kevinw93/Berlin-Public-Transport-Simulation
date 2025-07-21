# Berlin Public Transport Simulation

A discrete-event simulation of a mass transit system, inspired by Berlin's U-Bahn, S-Bahn, tram and bus network. Built in Java with a Swing GUI for CS 2340 (Objects and Design), Berlin Summer Program 2025.

Team: Sasanka Polisetti, Jeffrey Xu, Rohan Dalal, Kevin Wang

## What it does

Vehicles travel along routes of locations, exchanging passengers at each stop. The simulation moves from event to event (arrivals and departures) instead of ticking in real time.

- **Vehicles, locations and routes.** Create and remove them with globally unique IDs. Vehicles can be U-Bahn, S-Bahn, tram or bus, each with a capacity, rider count and speed in kph. Routes are ordered lists of locations, and a location can appear more than once. A vehicle at the end of its route continues from the start.
- **Travel times.** Each location has (X, Y) coordinates in kilometers. Travel time is the straight-line distance divided by the vehicle's speed, truncated to whole minutes, with a minimum of one minute.
- **Events.** An arrival at time t schedules a departure at t + 1 for the passenger exchange. A departure schedules an arrival at the next location after the travel time.
- **Simulation controls.** Advance the logical time, show the next event time and every event due then, execute the next event, and check whether a vehicle is at a location or in transit. A vehicle about to depart at the current time still counts as at its location.
- **Passenger exchange.** Each location has waiting passengers and configurable low/high ranges for debarking, transferring and embarking. On arrival, the system draws uniform random values in those ranges, applies the vehicle's capacity, and updates the riders and waiting passengers. It also reports how many passengers were delayed because the vehicle was full.
- **Hazards.** Attach a hazard (for example weather, traffic, construction or a power outage) to a location or to the connection between two locations. A short-term hazard adds minutes to the normal duration. A long-term hazard multiplies it by a factor. Hazards can be removed.

## Design changes (final phase)

1. **Graphical user interface.** A tabbed Swing GUI (Vehicles, Locations, Routes, Hazards, Simulation) replaced the original text menu.
2. **Paying back technical debt.** The single command-line class was split into a model, services, controllers and panels for each entity.
   - Services own the business rules and controllers translate user input. Panels only handle display, which follows the Single Responsibility and Controller principles.
   - `VehicleType` and `HazardType` are interfaces with one class per type, so a new vehicle or hazard type is a new class instead of another branch in a switch (Open/Closed, Polymorphism).
   - `SimulationManager` is a singleton that owns the event queue and the logical clock.

The original text menu is kept in `deprecated` for reference.

## Running it

Requires JDK 17 or newer.

```sh
javac -d out *.java
java -cp out TransitSimulatorGUI
```

On the Simulation tab, **Set Up Test Scenario** loads the client's starting state: 12 locations with their coordinates and passenger ranges, 6 routes, and the vehicles assigned to them.

## Project layout

| Files | Role |
|---|---|
| `Vehicle`, `Location`, `Route`, `Hazard`, `Event` | Domain model |
| `*Type.java` | Vehicle and hazard types |
| `TransitDataModel` | Shared lists of vehicles, locations, routes and hazards, plus the global ID set |
| `*Service.java` | Business rules and validation |
| `*Controller.java` | Input parsing and user-facing messages |
| `*Panel.java`, `TransitSimulatorGUI` | Swing interface |
| `SimulationManager` | Event queue and logical time |
