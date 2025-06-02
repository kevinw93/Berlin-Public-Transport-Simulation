import java.util.ArrayList;
import java.util.List;

public class Route implements Comparable<Route> {
    private String routeId;
    private String name;
    private List<Location> locations;
    private List<Vehicle> vehicles;

    public Route(String routeId, String name) {
        if (routeId == null || routeId.isEmpty()) {
            throw new IllegalArgumentException("ID can't be null or empty");
        }
        this.routeId = routeId;
        this.name = name;
        this.locations = new ArrayList<>();
        this.vehicles = new ArrayList<>();
    }

    public String getRouteId() {
        return routeId;
    }
    public String getName() {
        return name;
    }

    public int getLocationCount() {
        return locations.size();
    }
    public boolean isEmpty() {
        return locations.isEmpty();
    }

    // adds a location to the route at the specified position
    public void addLocation(Location location, int position) {
        if (location == null) {
            throw new IllegalArgumentException("Location can't be null");
        }
        if (position < 0 || position > locations.size()) {
            throw new IllegalArgumentException("Invalid position");
        }
        locations.add(position, location);
    }

    public void removeLocationAt(int position) {
        if (position < 0 || position >= locations.size()) {
            throw new IllegalArgumentException("Invalid position");
        }
        locations.remove(position);
    }

    public Location getLocationAt(int position) {
        if (position < 0 || position >= locations.size()) {
            throw new IllegalArgumentException("Invalid position");
        }
        return locations.get(position);
    }

    public List<Location> getLocations() {
        return new ArrayList<>(locations);
    }

    // calculates the next position in the route, wrapping around to the beginning
    public int getNextPosition(int currentPosition) {
        if (locations.isEmpty()) {
            throw new IllegalStateException("Route is empty");
        }
        return (currentPosition + 1) % locations.size();
    }

    public void addVehicle(Vehicle vehicle) {
        if (vehicle != null && !vehicles.contains(vehicle)) {
            vehicles.add(vehicle);
        }
    }

    public void removeVehicle(Vehicle vehicle) {
        vehicles.remove(vehicle);
    } 

    public List<Vehicle> getVehicles() {
        return new ArrayList<>(vehicles);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Route route = (Route) obj;
        return routeId.equals(route.routeId);
    }


    @Override
    public String toString() {
        String vehicleInfo = "";
        if (!vehicles.isEmpty()) {
            vehicleInfo = " | Vehicles: " + vehicles.size();
            if (vehicles.size() <= 3) {
                // Show vehicle names if 3 or fewer
                String[] vehicleNames = vehicles.stream()
                    .map(Vehicle::getName)
                    .toArray(String[]::new);
                vehicleInfo += " (" + String.join(", ", vehicleNames) + ")";
            }
        } else {
            vehicleInfo = " | Vehicles: None";
        }
        
        return String.format("ID: %s | Name: %s | Locations: %d%s", 
                           this.routeId, this.name, this.locations.size(), vehicleInfo);
    }

    @Override
    public int compareTo(Route otherRoute) {
        return this.routeId.compareTo(otherRoute.getRouteId());
    }
}
