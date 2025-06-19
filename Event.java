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
