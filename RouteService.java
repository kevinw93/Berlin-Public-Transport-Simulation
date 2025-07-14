import java.util.List;

public class RouteService {
    public final TransitDataModel model;
    public RouteService(TransitDataModel model) {
        this.model = model;
    }
    public boolean addRoute(String routeID, String name) {
        if (routeID.length() > 99) {
            return false;
        }
        if (!routeID.matches("[a-zA-Z0-9]+")) {
            return false;
        }
        if (findRouteById(routeID) != null) {
            return false;
        }
        Route r = new Route(routeID, name);
        model.routes.add(r);
        return true;
    }
    public List<Route> getRoutes() {
        return model.routes;
    }
    public Route findRouteById(String id) {
        for (Route r : model.routes) {
            if (r.getRouteId().equals(id)) {
                return r;
            }
        }
        return null;
    }
    public boolean removeRoute(String routeID) {
        Route r = findRouteById(routeID);
        if (r == null) {
            return false;
        }
        model.routes.remove(r);
        return true;
    }

    public boolean addLocationToRoute(String routeId, Location location, int position) {
        Route r = findRouteById(routeId);
        if (r == null) {
            return false;
        }
        try {
            r.addLocation(location, position);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean removeLocationFromRoute(String routeId, int position) {
        Route r = findRouteById(routeId);
        if (r == null) {
            return false;
        }
        try {
            r.removeLocationAt(position);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<Location> getLocationsOnRoute(String routeId) {
        Route r = findRouteById(routeId);
        if (r == null) {
            return null;
        }
        return r.getLocations();
    }

    // assigns a vehicle to a route at a specific position with a direction
    public boolean assignVehicleToRoute(String routeId, Vehicle vehicle, int position, boolean goingRight) {
        Route r = findRouteById(routeId);
        if (r == null || vehicle == null) {
            return false;
        }
        if (vehicle.getAssignedRoute() != null) {
            return false;
        }
        if (r.getLocations().size() <= position) {
            return false;
        }
        vehicle.setGoingRight(goingRight);
        vehicle.setLocation(r.getLocations().get(position));
        vehicle.setRoutePosition(position);
        vehicle.setAssignedRoute(r);
        r.addVehicle(vehicle);
        return true;
    }

    // removes a vehicle from its assigned route
    public boolean removeVehicleFromRoute(String routeId, Vehicle vehicle) {
        Route r = findRouteById(routeId);
        if (r == null || vehicle == null) {
            return false;
        }
        r.removeVehicle(vehicle);
        vehicle.setAssignedRoute(null);
        return true;
    }

    public List<Vehicle> getVehiclesOnRoute(String routeId) {
        Route r = findRouteById(routeId);
        if (r == null) {
            return null;
        }
        return r.getVehicles();
    }

    public void clear() {
        model.routes.clear();
    }
}

