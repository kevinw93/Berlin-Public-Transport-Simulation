public class SimulationService {
    public final SimulationManager simManager = SimulationManager.getInstance();
    public final TransitDataModel model;
    public SimulationService(TransitDataModel model) {
        this.model = model;
    }
    public int getCurrentTime() {
        return simManager.getCurrentTime();
    }
    // advances simulation time and executes all events that occur during the time period
    public boolean advanceTime(int minAdvance) {
        if (minAdvance <= 0) {
            return false;
        }
        int newTime = simManager.getCurrentTime() + minAdvance;
        while (simManager.hasNextEvent() && (simManager.getNextEventTime() <= newTime)) {
            simManager.executeNextEvent();
        }
        if (simManager.getCurrentTime() < newTime) {
            simManager.advanceTime(newTime);
        }
        return true;
    }
    public void displayNextEvents() {
        simManager.displayNextEvents();
    }
    public boolean executeNextEvent() {
        return simManager.executeNextEvent();
    }
    public String getVehicleStatus(Vehicle vehicle) {
        if (vehicle == null) {
            return "Vehicle not found.";
        }
        return simManager.getVehicleStatus(vehicle);
    }
    
    // schedules a vehicle to start moving on its assigned route
    public boolean startVehicleMovement(Vehicle vehicle) {
        if (vehicle == null) {
            return false;
        }
        if (vehicle.getAssignedRoute() == null) {
            return false;
        }
        if (vehicle.getLocation() == null) {
            return false;
        }
        if (vehicle.getAssignedRoute().getLocations().size() <= 1) {
            return false;
        }
        if (vehicle.inTransit()) {
            return false;
        }
        if (simManager.hasScheduledEvent(vehicle)) {
            return false;
        }
        int departureTime = simManager.getCurrentTime() + 1;
        Event event = new Event(departureTime, vehicle, vehicle.getLocation(), Event.EventType.DEPARTURE);
        simManager.addEvent(event);
        return true;
    }
}

