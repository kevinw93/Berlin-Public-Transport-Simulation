import java.util.PriorityQueue;

public class SimulationManager {
    private static SimulationManager simulation;
    private PriorityQueue<Event> eventQueue;
    private int currentTime;

    private SimulationManager() {
        this.eventQueue = new PriorityQueue<>();
        this.currentTime = 0; 
    }

    // method to return the only instance of the class (singleton)
    public static SimulationManager getInstance() {
        if (simulation == null) {
            simulation = new SimulationManager();
        }
        return simulation;
    }

    // adds an event to the simulation queue for future execution
    public void addEvent(Event event) {
        eventQueue.offer(event);
    }

    
    public int getCurrentTime() {
        return currentTime;
    }

    // advances the simulation time and executes any events that occur during the time period
    public void advanceTime(int newTime) {
        if (newTime > currentTime) {
            currentTime = newTime;
            System.out.println("Simulation time advanced to: " + currentTime);
            
        }
    }

    
    public Event getNextEvent() {
        if (eventQueue.isEmpty()) {
            return null;
        }
        return eventQueue.poll();
    }

    
    public int getNextEventTime() {
        if (eventQueue.isEmpty()) {
            return Integer.MAX_VALUE;
        }
        return eventQueue.peek().getEventTime();
    }

    // look through the priority queue to print events
    public void displayNextEvents() {
        if (eventQueue.isEmpty()) {
            System.out.println("No upcoming events scheduled.");
            return;
        }

        int nextTime = eventQueue.peek().getEventTime();
        System.out.println("Next simulation state change will occur at: " + nextTime + " minutes");

        for (Event event : eventQueue) {
            if (event.getEventTime() == nextTime) {
                System.out.println("  - " + event.getDescription());
            }
        }
    }

    // calls the execute method of the top event in the priority queue
    public boolean executeNextEvent() {
        if (eventQueue.isEmpty()) {
            System.out.println("No events to execute.");
            return false;
        }

        Event nextEvent = eventQueue.poll();
        currentTime = nextEvent.getEventTime();
        System.out.println("Executing event at " + currentTime + " minutes: "); // +  + nextEvent.getDescription
        nextEvent.execute();
        return true;
    }


    public boolean hasNextEvent() {
        return !eventQueue.isEmpty();
    }

    // determine if a vehicle has an event scheduled in the priority queue
    public boolean hasScheduledEvent(Vehicle vehicle) {
        for (Event event : eventQueue) {
            if (event.getVehicle().equals(vehicle)) {
                return true;
            }
            
        }
        return false;
    }

    // removes all scheduled events for a specific vehicle from the simulation queue
    public void removeVehicleEvent(Vehicle vehicle) {
        eventQueue.removeIf(e -> e.getVehicle().equals(vehicle));
    }

    public void removeLocationEvent(Route route, int index) {
        eventQueue.removeIf(e -> e.getVehicle().getAssignedRoute() == route && e.getVehicle().getRoutePosition() == index);
    }   
    
    // return the status of the vehicle (in transit or at a station)
    public String getVehicleStatus(Vehicle vehicle) {
        Event vehicleNextEvent = null;
        for(Event event : this.eventQueue) {
            if (event.getVehicle().equals(vehicle)){
                vehicleNextEvent = event;
                break;
            }
        }
        if (vehicleNextEvent == null) {
            return "Could not find vehicle next event.";
        }
        if (vehicle.inTransit()) {
            return String.format("Vehicle %s will arrive at location %s at time %d. (in transit)", vehicle.getVehicleId(), 
                vehicleNextEvent.getLocation().getLocationId(), vehicleNextEvent.getEventTime());
        } else {
            return String.format("Vehicle %s will depart from location %s at time %d.", 
            vehicle.getVehicleId(), vehicleNextEvent.getLocation().getLocationId(), vehicleNextEvent.getEventTime());
        }
    }
}
