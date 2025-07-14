import java.util.List;

public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    public String addLocation(String locationID, String name, String xCoordStr, String yCoordStr, String waitingStr, String arrLow, String arrHigh, String transLow, String transHigh, String depLow, String depHigh) {
        if (locationID == null || locationID.trim().isEmpty() || name == null || name.trim().isEmpty() || xCoordStr == null || yCoordStr == null || waitingStr == null || arrLow == null || arrHigh == null || transLow == null || transHigh == null || depLow == null || depHigh == null) {
            return "All fields are required.";
        }
        if (locationID.length() > 99) {
            return "Location ID must be less than 100 characters.";
        }
        if (!locationID.matches("[a-zA-Z0-9]+")) {
            return "Location ID must be alphanumeric.";
        }
        if (locationService.model.allIds.contains(locationID)) {
            return "ID already exists globally.";
        }
        double x, y;
        int waiting, aLow, aHigh, tLow, tHigh, dLow, dHigh;
        try {
            x = Double.parseDouble(xCoordStr); 
        } catch (NumberFormatException ex) {
            return "X coordinate must be a number.";
        }
        try {
            y = Double.parseDouble(yCoordStr);
        } catch (NumberFormatException ex) {
            return "Y coordinate must be a number.";
        }
        try {
            waiting = Integer.parseInt(waitingStr);
            if (waiting < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) { 
            return "Waiting passengers must be a non-negative integer."; 
        }
        try {
            aLow = Integer.parseInt(arrLow);
        } catch (NumberFormatException ex) {
            return "Arrive Low must be an integer.";
        }
        try {
            aHigh = Integer.parseInt(arrHigh);
        } catch (NumberFormatException ex) {
            return "Arrive High must be an integer.";
        }
        try {
            tLow = Integer.parseInt(transLow);
        } catch (NumberFormatException ex) { 
            return "Trans Low must be an integer.";
        }
        try { 
            tHigh = Integer.parseInt(transHigh); 
        } catch (NumberFormatException ex) {
             return "Trans High must be an integer."; 
        }
        try {
            dLow = Integer.parseInt(depLow);
        } catch (NumberFormatException ex) {
            return "Dep Low must be an integer.";
        }
        try {
            dHigh = Integer.parseInt(depHigh);
        } catch (NumberFormatException ex) {
             return "Dep High must be an integer."; 
            }
        boolean success = locationService.addLocation(locationID, name, x, y, waiting, aLow, aHigh, tLow, tHigh, dLow, dHigh);
        if (success) {
            locationService.model.allIds.add(locationID);
            return "Location added: " + locationID;
        } else {
            return "Failed to add location. Check for duplicate ID or invalid input.";
        }
    }

    public String showLocations() {
        List<Location> locations = locationService.getLocations();
        if (locations.isEmpty()) {
            return "There are no locations to display!";
        }
        StringBuilder sb = new StringBuilder();
        for (Location l : locations) {
            sb.append(String.format("ID: %s | Name: %s | Coordinate: (%.2f, %.2f)| Waiting Passengers: %d\n",
                l.getLocationId(), l.getName(), l.getXCoord(), l.getYCoord(), l.getWaitingPassengers()));
        }
        return sb.toString();
    }

    public String setWaitingPassengers(String locationId, int count) {
        if (count < 0) {
            return "Waiting passengers must be non-negative.";
        }
        boolean success = locationService.setWaitingPassengers(locationId, count);
        if (success) {
            return "Waiting passengers updated.";
        } else {
            return "Failed to update waiting passengers. Location not found or invalid input.";
        }
    }

    public String setPassengerExchangeRanges(String locationId, int debarkMin, int debarkMax, int transferMin, int transferMax, int embarkMin, int embarkMax) {
        if (debarkMin < 0 || debarkMax < 0 || transferMin < 0 || transferMax < 0 || embarkMin < 0 || embarkMax < 0) {
            return "All exchange ranges must be non-negative.";
        }
        boolean success = locationService.setPassengerExchangeRanges(locationId, debarkMin, debarkMax, transferMin, transferMax, embarkMin, embarkMax);
        if (success) {
            return "Passenger exchange ranges updated.";
        } else {
            return "Failed to update exchange ranges. Location not found or invalid input.";
        }
    }

    public Location findLocationById(String id) {
        return locationService.findLocationById(id);
    }

    public String removeLocation(String locationID) {
        Location l = locationService.findLocationById(locationID);
        if (l == null) {
            return "Location not found.";
        }
        // Check if part of any route
        for (Route r : locationService.model.routes) {
            if (r.getLocations().contains(l)) {
                return "Cannot remove location: it is part of a route.";
            }
        }
        boolean success = locationService.removeLocation(locationID);
        if (success) {
            locationService.model.allIds.remove(locationID);
            return "Location removed!";
        } else {
            return "Failed to remove location.";
        }
    }
}
