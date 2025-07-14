public class SimulationController {
    private final SimulationService simulationService;

    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    public String getCurrentTime() {
        return "Current simulation time: " + simulationService.getCurrentTime();
    }

    public String advanceTime(String minAdvanceStr) {
        int minAdvance;
        try {
            minAdvance = Integer.parseInt(minAdvanceStr);
            if (minAdvance <= 0) {
                return "The number of minutes must be positive.";
            }
        } catch (NumberFormatException ex) {
            return "Please enter a positive integer for minutes.";
        }
        boolean success = simulationService.advanceTime(minAdvance);
        if (success) {
            return "Simulation time advanced. Current time: " + simulationService.getCurrentTime();
        } else {
            return "Failed to advance simulation time.";
        }
    }

    public String displayNextEvents() {
        // Capture the output from the simulation service
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        java.io.PrintStream oldOut = System.out;
        System.setOut(new java.io.PrintStream(baos));
        
        simulationService.displayNextEvents();
        
        System.setOut(oldOut);
        String output = baos.toString();
        
        if (output.trim().isEmpty()) {
            return "No upcoming events scheduled.";
        }
        return output;
    }

    public String executeNextEvent() {
        boolean success = simulationService.executeNextEvent();
        if (success) {
            return "Executed next event.";
        } else {
            return "No events to execute.";
        }
    }

    public String getVehicleStatus(int vehicleIdx) {
        if (vehicleIdx < 0 || vehicleIdx >= simulationService.model.vehicles.size()) {
            return "Please select a vehicle.";
        }
        Vehicle v = simulationService.model.vehicles.get(vehicleIdx);
        return simulationService.getVehicleStatus(v);
    }

    public String startVehicleMovement(int vehicleIdx) {
        if (vehicleIdx < 0 || vehicleIdx >= simulationService.model.vehicles.size()) {
            return "Please select a vehicle.";
        }
        Vehicle v = simulationService.model.vehicles.get(vehicleIdx);
        boolean success = simulationService.startVehicleMovement(v);
        if (success) {
            return "Vehicle " + v.getName() + " scheduled to start route at " + (simulationService.getCurrentTime() + 1);
        } else {
            return "Failed to schedule vehicle movement. Check vehicle assignment, location, or event status.";
        }
    }

    
    public String setupTestScenario() {

        Location stadium = new Location("l1", "stadium", 0, 5, 8);
        stadium.setDebarkRange(2, 6);
        stadium.setEmbarkRange(1, 1);
        stadium.setTransferRange(2, 3);
        simulationService.model.locations.add(stadium);

        Location west = new Location("l2", "west", 1, 3, 2);
        west.setDebarkRange(1, 2);
        west.setEmbarkRange(1, 1);
        west.setTransferRange(1, 1);
        simulationService.model.locations.add(west);

        Location castle = new Location("l3", "castle", 2, 0, 5);
        castle.setDebarkRange(1, 2);
        castle.setEmbarkRange(1, 1);
        castle.setTransferRange(1, 3);
        simulationService.model.locations.add(castle);

        Location port = new Location("l4", "port", 3, 10, 3);
        port.setDebarkRange(1, 1);
        port.setEmbarkRange(1, 1);
        port.setTransferRange(1, 1);
        simulationService.model.locations.add(port);

        Location bahnhof = new Location("l5", "bahnhof", 4, 5, 10);
        bahnhof.setDebarkRange(1, 4);
        bahnhof.setEmbarkRange(3, 6);
        bahnhof.setTransferRange(1, 6);
        simulationService.model.locations.add(bahnhof);

        Location north = new Location("l6", "north", 5, 8, 3);
        north.setDebarkRange(1, 2);
        north.setEmbarkRange(1, 1);
        north.setTransferRange(1, 2);
        simulationService.model.locations.add(north);

        Location museum = new Location("l7", "museum", 5, 3, 8);
        museum.setDebarkRange(2, 4);
        museum.setEmbarkRange(2, 6);
        museum.setTransferRange(1, 4);
        simulationService.model.locations.add(museum);

        Location south = new Location("l8", "south", 6, 1, 3);
        south.setDebarkRange(1, 2);
        south.setEmbarkRange(1, 1);
        south.setTransferRange(1, 1);
        simulationService.model.locations.add(south);

        Location tower = new Location("l9", "tower", 7, 4, 5);
        tower.setDebarkRange(1, 1);
        tower.setEmbarkRange(1, 3);
        tower.setTransferRange(1, 2);
        simulationService.model.locations.add(tower);

        Location east = new Location("l10", "east", 8, 4, 2);
        east.setDebarkRange(1, 2);
        east.setEmbarkRange(1, 2);
        east.setTransferRange(1, 1);
        simulationService.model.locations.add(east);

        Location airport = new Location("l11", "airport", 10, 2, 10);
        airport.setDebarkRange(3, 8);
        airport.setEmbarkRange(1, 2);
        airport.setTransferRange(2, 5);
        simulationService.model.locations.add(airport);

        Location gate = new Location("l12", "gate", 9, 9, 5);
        gate.setDebarkRange(1, 1);
        gate.setEmbarkRange(1, 3);
        gate.setTransferRange(1, 2);
        simulationService.model.locations.add(gate);

        // ROUTES
        Route line1 = new Route("r1", "line_1");
        line1.addLocation(stadium, 0);
        line1.addLocation(west, 1);
        line1.addLocation(bahnhof, 2);
        line1.addLocation(tower, 3);
        line1.addLocation(east, 4);
        line1.addLocation(airport, 5);
        line1.addLocation(east, 6);
        line1.addLocation(tower, 7);
        line1.addLocation(bahnhof, 8);
        line1.addLocation(west, 9);
        simulationService.model.routes.add(line1);

        Route line2 = new Route("r2", "line_2");
        line2.addLocation(port, 0);
        line2.addLocation(north, 1);
        line2.addLocation(bahnhof, 2);
        line2.addLocation(west, 3);
        line2.addLocation(castle, 4);
        line2.addLocation(west, 5);
        line2.addLocation(bahnhof, 6);
        line2.addLocation(north, 7);
        simulationService.model.routes.add(line2);

        Route line3 = new Route("r3", "line_3");
        line3.addLocation(gate, 0);
        line3.addLocation(north, 1);
        line3.addLocation(bahnhof, 2);
        line3.addLocation(tower, 3);
        line3.addLocation(south, 4);
        line3.addLocation(tower, 5);
        line3.addLocation(bahnhof, 6);
        line3.addLocation(north, 7);
        simulationService.model.routes.add(line3);

        Route ring1 = new Route("r4", "ring_1");
        ring1.addLocation(south, 0);
        ring1.addLocation(castle, 1);
        ring1.addLocation(bahnhof, 2);
        ring1.addLocation(tower, 3);
        ring1.addLocation(east, 4);
        ring1.addLocation(tower, 5);
        ring1.addLocation(bahnhof, 6);
        ring1.addLocation(north, 7);
        simulationService.model.routes.add(ring1);

        Route ring2 = new Route("r5", "ring_2");
        ring2.addLocation(bahnhof, 0);
        ring2.addLocation(museum, 1);
        ring2.addLocation(tower, 2);
        ring2.addLocation(bahnhof, 3);
        ring2.addLocation(tower, 4);
        ring2.addLocation(bahnhof, 5);
        ring2.addLocation(north, 6);
        ring2.addLocation(north, 7);
        simulationService.model.routes.add(ring2);

        Route ring3 = new Route("r6", "ring_3");
        ring3.addLocation(north, 0);
        ring3.addLocation(bahnhof, 1);
        ring3.addLocation(north, 2);
        ring3.addLocation(port, 3);
        ring3.addLocation(north, 4);
        ring3.addLocation(gate, 5);
        simulationService.model.routes.add(ring3);

        // VEHICLES
        Vehicle sbahn_A = new Vehicle("sb_A", "sbahn_A", new SbahnType(), 6, 60);
        sbahn_A.setLocation(stadium);
        sbahn_A.setAssignedRoute(line1);
        sbahn_A.setRoutePosition(0);
        sbahn_A.setGoingRight(true);
        line1.addVehicle(sbahn_A);
        simulationService.model.vehicles.add(sbahn_A);

        Vehicle ubahn_A = new Vehicle("ub_A", "ubahn_A", new UbahnType(), 5, 45);
        ubahn_A.setLocation(port);
        ubahn_A.setAssignedRoute(line2);
        ubahn_A.setRoutePosition(0);
        ubahn_A.setGoingRight(true);
        line2.addVehicle(ubahn_A);
        simulationService.model.vehicles.add(ubahn_A);

        Vehicle bus_A = new Vehicle("bus_A", "bus_A", new BusType(), 3, 35);
        bus_A.setLocation(north);
        bus_A.setAssignedRoute(ring2);
        bus_A.setRoutePosition(1);
        bus_A.setGoingRight(true);
        ring2.addVehicle(bus_A);
        simulationService.model.vehicles.add(bus_A);

        Vehicle sbahn_C = new Vehicle("sb_C", "sbahn_C", new SbahnType(), 8, 60);
        sbahn_C.setLocation(bahnhof);
        sbahn_C.setAssignedRoute(ring1);
        sbahn_C.setRoutePosition(2);
        sbahn_C.setGoingRight(true);
        ring1.addVehicle(sbahn_C);
        simulationService.model.vehicles.add(sbahn_C);

        Vehicle tram_A = new Vehicle("tram_5", "tram_A", new TramType(), 4, 40);
        tram_A.setLocation(bahnhof);
        tram_A.setAssignedRoute(ring3);
        tram_A.setRoutePosition(2);
        tram_A.setGoingRight(true);
        ring3.addVehicle(tram_A);
        simulationService.model.vehicles.add(tram_A);

        Vehicle sbahn_B = new Vehicle("sb_B", "sbahn_B", new SbahnType(), 6, 60);
        sbahn_B.setLocation(east);
        sbahn_B.setAssignedRoute(line1);
        sbahn_B.setRoutePosition(6);
        sbahn_B.setGoingRight(true);
        line1.addVehicle(sbahn_B);
        simulationService.model. vehicles.add(sbahn_B);

        Vehicle ubahn_C = new Vehicle("ub_C", "ubahn_C", new UbahnType(), 5, 45);
        ubahn_C.setLocation(east);
        ubahn_C.setAssignedRoute(line2);
        ubahn_C.setRoutePosition(6);
        ubahn_C.setGoingRight(true);
        line2.addVehicle(ubahn_C);
        simulationService.model.vehicles.add(ubahn_C);

        Vehicle ubahn_B = new Vehicle("ub_B", "ubahn_B", new UbahnType(), 5, 45);
        ubahn_B.setLocation(tower);
        ubahn_B.setAssignedRoute(line3);
        ubahn_B.setRoutePosition(7);
        ubahn_B.setGoingRight(true);
        line3.addVehicle(ubahn_B);
        simulationService.model.vehicles.add(ubahn_B);
        

        //Schedule ALL vehicle departures
        Event event = new Event(0, sbahn_A, sbahn_A.getLocation(), Event.EventType.ARRIVAL);
        this.simulationService.simManager.addEvent(event);

        Event event1 = new Event(2, sbahn_B, sbahn_B.getLocation(), Event.EventType.DEPARTURE);
        this.simulationService.simManager.addEvent(event1);

        Event event2 = new Event(4, sbahn_C, sbahn_C.getLocation(), Event.EventType.ARRIVAL);
        this.simulationService.simManager.addEvent(event2);

        Event event3 = new Event(4, ubahn_A, ubahn_A.getLocation(), Event.EventType.DEPARTURE);
        this.simulationService.simManager.addEvent(event3);

        Event event4 = new Event(5, ubahn_B, ubahn_B.getLocation(), Event.EventType.ARRIVAL);
        this.simulationService.simManager.addEvent(event4);

        Event event5 = new Event(8, ubahn_C, ubahn_C.getLocation(), Event.EventType.DEPARTURE);
        this.simulationService.simManager.addEvent(event5);

        Event event6 = new Event(10, tram_A, tram_A.getLocation(), Event.EventType.ARRIVAL);
        this.simulationService.simManager.addEvent(event6);

        Event event7 = new Event(3, bus_A, bus_A.getLocation(), Event.EventType.DEPARTURE);
        this.simulationService.simManager.addEvent(event7);


        return "Test Scenario Set Up Successfully";
    }
}
