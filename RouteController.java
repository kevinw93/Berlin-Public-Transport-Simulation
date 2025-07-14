import java.util.List;

public class RouteController {
    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    public String addRoute(String routeID, String name) {
        if (routeID == null || routeID.trim().isEmpty() || name == null || name.trim().isEmpty()) {
            return "All fields are required.";
        }
        if (routeID.length() > 99) {
            return "Route ID must be less than 100 characters.";
        }
        if (!routeID.matches("[a-zA-Z0-9]+")) {
            return "Route ID must be alphanumeric.";
        }
        if (routeService.model.allIds.contains(routeID)) {
            return "ID already exists globally.";
        }
        boolean success = routeService.addRoute(routeID, name);
        if (success) {
            routeService.model.allIds.add(routeID);
            return "Route added: " + routeID;
        } else {
            return "Failed to add route. Check for duplicate ID or invalid input.";
        }
    }

    public String showRoutes() {
        List<Route> routes = routeService.getRoutes();
        if (routes.isEmpty()) {
            return "There are no routes to display!";
        }
        StringBuilder sb = new StringBuilder();
        for (Route r : routes) {
            sb.append(String.format("ID: %s | Name: %s | Locations: %d\n",
                r.getRouteId(), r.getName(), r.getLocationCount()));
        }
        return sb.toString();
    }

    public String addLocationToRoute(String routeId, Location location, int position) {
        if (location == null) {
            return "Location cannot be null.";
        }
        boolean success = routeService.addLocationToRoute(routeId, location, position);
        if (success) {
            return "Location added to route.";
        } else {
            return "Failed to add location to route. Check route ID, position, or location.";
        }
    }

    public String removeLocationFromRoute(String routeId, int position) {
        boolean success = routeService.removeLocationFromRoute(routeId, position);
        if (success) {
            return "Location removed from route.";
        } else {
            return "Failed to remove location from route. Check route ID and position.";
        }
    }

    public String showRouteContent(String routeId) {
        List<Location> locations = routeService.getLocationsOnRoute(routeId);
        if (locations == null || locations.isEmpty()) {
            return "No locations found for this route.";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < locations.size(); i++) {
            Location l = locations.get(i);
            sb.append(String.format("%d: %s (ID: %s)\n", i, l.getName(), l.getLocationId()));
        }
        return sb.toString();
    }

    public String assignVehicleToRoute(String routeId, Vehicle vehicle, int position, boolean goingRight) {
        if (vehicle == null) {
            return "Vehicle cannot be null.";
        }
        boolean success = routeService.assignVehicleToRoute(routeId, vehicle, position, goingRight);
        if (success) {
            return "Vehicle assigned to route.";
        } else {
            return "Failed to assign vehicle to route. Check route, vehicle, or position.";
        }
    }

    public String removeVehicleFromRoute(String routeId, Vehicle vehicle) {
        if (vehicle == null) {
            return "Vehicle cannot be null.";
        }
        boolean success = routeService.removeVehicleFromRoute(routeId, vehicle);
        if (success) {
            return "Vehicle removed from route.";
        } else {
            return "Failed to remove vehicle from route. Check route and vehicle.";
        }
    }

    public String showVehiclesOnRoute(String routeId) {
        List<Vehicle> vehicles = routeService.getVehiclesOnRoute(routeId);
        if (vehicles == null || vehicles.isEmpty()) {
            return "No vehicles found on this route.";
        }
        StringBuilder sb = new StringBuilder();
        for (Vehicle v : vehicles) {
            sb.append(String.format("ID: %s | Name: %s | Type: %s\n",
                v.getVehicleId(), v.getName(), v.getVehicleType()));
        }
        return sb.toString();
    }

    public String removeRoute(String routeID) {
        Route r = routeService.findRouteById(routeID);
        if (r == null) {
            return "Route not found.";
        }
        for (Vehicle v : routeService.model.vehicles) {
            if (v.getAssignedRoute() == r) {
                return "Cannot remove route: a vehicle is assigned to it.";
            }
        }
        boolean success = routeService.removeRoute(routeID);
        if (success) {
            routeService.model.allIds.remove(routeID);
            return "Route removed!";
        } else {
            return "Failed to remove route.";
        }
    }

    public Route findRouteById(String id) {
        return routeService.findRouteById(id);
    }
}
