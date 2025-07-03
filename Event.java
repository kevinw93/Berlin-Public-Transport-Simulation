import java.util.List;

public class Event implements Comparable<Event> {
    // two event types either arrival or departure represented with an enum
    public enum EventType {
        ARRIVAL,
        DEPARTURE
    }

    // fields
    private int eventTime;
    private Vehicle vehicle;
    private Location location;
    private EventType eventType;

        
    public Event(int eventTime, Vehicle vehicle, Location location, EventType eventType) {
        this.eventTime = eventTime;
        this.vehicle = vehicle;
        this.location = location;
        this.eventType = eventType;
    }

    public int getEventTime() {
        return eventTime; 
    }

    public Vehicle getVehicle() {
        return vehicle; 
    }

    public Location getLocation() {
        return location; 
    }

    public EventType getEventType() {
        return eventType;
    }

    // executes the event based on its type (arrival or departure)
    public void execute() {
        switch (eventType) {
            case ARRIVAL:
                executeArrival();
                break;
            case DEPARTURE:
                executeDeparture();
                break;
        }
    }

    // handles vehicle arrival at a location including passenger exchange and departure scheduling
    private void executeArrival() {
        System.out.println("Vehicle " + vehicle.getVehicleId() + " has arrived at " + location.getLocationId());
        vehicle.setLocation(location);
        vehicle.setInTransit(false);

        //  Initiate passenger exchange logic
        int ridePassCur = vehicle.getRidingPass();
        int arrPassLow = location.getDebarkMin();
        int arrPassHigh = location.getDebarkMax();
        int xferPassLow = location.getTransferMin();
        int xferPassHigh = location.getTransferMax();
        int boardPassLow = location.getEmbarkMin();
        int boardPassHigh = location.getEmbarkMax();
        int waitPassCur = location.getWaitingPassengers();
        int capacity = vehicle.getCapacity();

        // randomly generating a number to calculate how many passengers will debark
        java.util.Random rand = new java.util.Random();
        int arrPass = arrPassLow + rand.nextInt(arrPassHigh - arrPassLow + 1);
        if (arrPass > ridePassCur) arrPass = ridePassCur;

        // calculating transferring passengers with a new random number.
        int xferPass = 0;
        if (arrPass > 0) {
            xferPass = xferPassLow + rand.nextInt(xferPassHigh - xferPassLow + 1);
            if (xferPass > arrPass) xferPass = arrPass;
        }
        int homePass = arrPass - xferPass;

        // calculating boarding passengers 
        int boardPass = boardPassLow + rand.nextInt(boardPassHigh - boardPassLow + 1);
        int availableSeats = capacity - (ridePassCur - arrPass);
        int actualBoard = Math.min(boardPass, Math.max(0, availableSeats));
        int delayPass = Math.max(0, boardPass - actualBoard);

        int waitPassNext = waitPassCur - actualBoard + xferPass;
        if (waitPassNext < 0) {
            waitPassNext = 0;
        }
        int ridePassNext = ridePassCur - arrPass + actualBoard;

        // Update state
        vehicle.setRidingPass(ridePassNext);
        location.setWaitingPassengers(waitPassNext);

    
        System.out.printf("Passenger exchange at %s:\n", location.getLocationId());
        System.out.printf("  Debarked: %d (Home: %d, Transfers: %d)\n", arrPass, homePass, xferPass);
        System.out.printf("  Boarded: %d\n", actualBoard);
        System.out.printf("  Delayed: %d\n", delayPass);
        System.out.printf("  Riders now: %d\n", ridePassNext);
        System.out.printf("  Waiting now: %d\n", waitPassNext);

        // passenger exchange takes 1 min
        int departureTime = eventTime + 1;
        Event departureEvent = new Event(departureTime, vehicle, location, EventType.DEPARTURE);
        SimulationManager.getInstance().addEvent(departureEvent);
    }

    // handles vehicle departure and schedules arrival at the next location
    private void executeDeparture() {
        System.out.println("Vehicle " + vehicle.getVehicleId() + " is departing from " + location.getLocationId());

        Route route = vehicle.getAssignedRoute();
        if (route != null && !route.isEmpty()) {
            int currentPos = vehicle.getRoutePosition();
            int nextPos;
            List<Location> lList = route.getLocations();

            if (vehicle.goingRight()) {
                nextPos = (currentPos + 1) % lList.size();
            } else {
                nextPos = (currentPos - 1 + lList.size()) % lList.size();
            }

            Location nextLocation = route.getLocationAt(nextPos);

            double distance = location.distanceTo(nextLocation);
            double speed = vehicle.getSpeed();
            int travelTimeMinutes = Math.max(1, (int)(distance / speed * 60));

            vehicle.setRoutePosition(nextPos);
            vehicle.setInTransit(true);
            

            int arrivalTime = eventTime + travelTimeMinutes;
            Event arrivalEvent = new Event(arrivalTime, vehicle, nextLocation, EventType.ARRIVAL);
            SimulationManager.getInstance().addEvent(arrivalEvent);
        }
    }

    public String getDescription() {
        String action = (eventType == EventType.ARRIVAL) ? "Arrival" : "Departure";
        String preposition = (eventType == EventType.ARRIVAL) ? "at" : "from";
        return action + ": Vehicle " + vehicle.getVehicleId() + " " + preposition + " " + location.getLocationId();
    }

    @Override
    public int compareTo(Event other) {
        return Integer.compare(this.eventTime, other.eventTime);
    }
}
