public class Vehicle implements Comparable<Vehicle> {
    private String vehicleId;
    private String name;
    private int capacity;
    private VehicleType vehicleType;
    private double speed;
    private int ridingPass;
    private Route assignedRoute;
    private Location location;
    private int routePosition;
    private int legProgress;
    private boolean inTransit;
    public boolean goingRight;

    public Vehicle(String vehicleId, String name, VehicleType type, int capacity, double speed) {
        if (vehicleId == null || vehicleId.trim().isEmpty()) {
            throw new IllegalArgumentException("ID can't be null or empty");
        }
        this.vehicleId = vehicleId.trim();
        this.name = name;
        this.vehicleType = type;
        this.capacity = capacity;
        this.speed = speed;
        this.ridingPass = capacity / 4;
        this.routePosition = -1;
        this.legProgress = 0;
        this.assignedRoute = null;
        this.inTransit = false;
        this.goingRight = false;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }
    public double getSpeed() {
        return speed;
    }
    public int getRidingPass() {
        return ridingPass;
    }
    public Route getAssignedRoute() {
        return assignedRoute;
    }
    public Location getLocation() {
        return location;
    }

    public Location getCurrentLocation() {
        return location;
    }

    public int getRoutePosition() {
        return routePosition;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }
    public boolean inTransit() {
        return inTransit;
    }
    public boolean goingRight() {
        return goingRight;
    }
    // sets the number of passengers currently riding on the vehicle
    public void setRidingPass(int passengers) {
        if (passengers < 0 || passengers > capacity) {
            throw new IllegalArgumentException("Invalid passenger count");
        }
        this.ridingPass = passengers;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public void setLocation(Location location) {
        this.location = location; 
    } 

    public void setAssignedRoute(Route route) {
        this.assignedRoute = route;
    }
    
    public void setRoutePosition(int position) {
        this.routePosition = position;
    }

    public void setSpeed(double speed) {
        if (speed <= 0) {
            throw new IllegalArgumentException("Speed must be positive");
        }
        this.speed = speed;
    }

    public void setInTransit(boolean inTransit) {
        this.inTransit = inTransit;
    }

    public void setGoingRight(boolean goingRight) {
        this.goingRight = goingRight;
    }

    public void setCapacity(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity must be non-negative");
        }
        if (capacity < this.ridingPass) {
            throw new IllegalArgumentException("Capacity cannot be less than current riders");
        }
        this.capacity = capacity;
    }

    // removes the vehicle from its assigned route and resets position
    public void removeFromRoute() {
        this.assignedRoute = null;
        this.routePosition = -1;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Vehicle vehicle = (Vehicle) obj;
        return vehicleId.equals(vehicle.vehicleId);
    }

    @Override
    public String toString() {
        String locationInfo = (location != null) ? location.getName() : "No location";
        String routeInfo = (assignedRoute != null) ? assignedRoute.getName() : "No route";
        String transitInfo = inTransit ? " (In Transit)" : "";
        
        String format = "ID: %s | Name: %s | Type: %s | Capacity: %d | Speed: %.2f | Location: %s | Route: %s%s";
        return String.format(format, this.vehicleId, this.name, this.vehicleType, 
                           this.capacity, this.speed, locationInfo, routeInfo, transitInfo);
    }

    @Override
    public int compareTo(Vehicle otherVehicle) {
        return this.vehicleId.compareTo(otherVehicle.getVehicleId());
    }

}
