public class Vehicle implements Comparable<Vehicle> {
    private String vehicleId;
    private String name;
    private int capacity;
    private VehicleType vehicleType;
    private Route assignedRoute;
    private Location location;
    private int routePosition;

    public Vehicle(String vehicleId, String name, VehicleType type, int capacity) {
        if (vehicleId == null || vehicleId.trim().isEmpty()) {
            throw new IllegalArgumentException("ID can't be null or empty");
        }
        this.vehicleId = vehicleId.trim();
        this.name = name;
        this.vehicleType = type;
        this.capacity = capacity;
        this.routePosition = -1;
        this.assignedRoute = null;
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
        
        String format = "ID: %s | Name: %s | Type: %s | Capacity: %d | Location: %s | Route: %s";
        return String.format(format, this.vehicleId, this.name, this.vehicleType, 
                           this.capacity, locationInfo, routeInfo);
    }

    @Override
    public int compareTo(Vehicle otherVehicle) {
        return this.vehicleId.compareTo(otherVehicle.getVehicleId());
    }

}
