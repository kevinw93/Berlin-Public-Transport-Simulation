import java.util.List;

public class HazardController {
    private final HazardService hazardService;

    public HazardController(HazardService hazardService) {
        this.hazardService = hazardService;
    }

    public String addHazard(String hazardId, String name, String typeStr, String impactStr, boolean single, int stopIdx, int routeIdx, int stopAIdx, int stopBIdx) {
        if (hazardId == null || hazardId.trim().isEmpty() || name == null || name.trim().isEmpty() || typeStr == null || impactStr == null) {
            return "All fields are required.";
        }
        if (hazardId.length() > 99) {
            return "Hazard ID must be less than 100 characters.";
        }
        if (!hazardId.matches("[a-zA-Z0-9]+")) {
            return "Hazard ID must be alphanumeric.";
        }
        if (hazardService.model.allIds.contains(hazardId)) {
            return "ID already exists globally.";
        }
        double impact;
        try {
            impact = Double.parseDouble(impactStr);
            if (typeStr.equals("Long-term") && impact < 1) {
                return "Long-term hazard multiplier must be >= 1.";
            }
            if (impact <= 0) {
                return "Impact value must be positive.";
            }
        } catch (NumberFormatException ex) {
            return "Impact value must be a number.";
        }
        HazardType type = typeStr.equals("Short-term") ? new ShortTermHazardType() : new LongTermHazardType();
        Hazard hazard = new Hazard(hazardId, name, type, impact);
        
        // Check if there's an existing hazard at the same location(s)
        Hazard existingHazard = hazardService.findExistingHazard(hazard);
        String replacementMessage = "";
        if (existingHazard != null) {
            replacementMessage = " (replaced existing hazard: " + existingHazard.getHazardId() + ")";
        }
        
        if (single) {
            if (stopIdx < 0 || stopIdx >= hazardService.model.locations.size()) {
                return "Invalid stop selection.";
            }
            Location l = hazardService.model.locations.get(stopIdx);
            hazard.setLocation(l);
            l.setHazard(hazard);
        } else {
            if (routeIdx < 0 || routeIdx >= hazardService.model.routes.size()) {
                return "Invalid route selection.";
            }
            Route r = hazardService.model.routes.get(routeIdx);
            if (stopAIdx < 0 || stopBIdx < 0 || stopAIdx >= r.getLocations().size() || stopBIdx >= r.getLocations().size()) {
                return "Invalid stop selection.";
            }
            if (Math.abs(stopAIdx - stopBIdx) != 1) {
                return "Stops must be adjacent on the route.";
            }
            Location a = r.getLocations().get(stopAIdx);
            Location b = r.getLocations().get(stopBIdx);
            hazard.setLocations(a, b);
        }
        
        boolean success = hazardService.addHazard(hazard);
        if (success) {
            hazardService.model.allIds.add(hazardId);
            return "Hazard added: " + hazardId + replacementMessage;
        } else {
            return "Failed to add hazard. Check for duplicate or invalid input.";
        }
    }

    public String showHazards() {
        List<Hazard> hazards = hazardService.getHazards();
        if (hazards.isEmpty()) {
            return "There are no hazards to display!";
        }
        StringBuilder sb = new StringBuilder();
        for (Hazard h : hazards) {
            sb.append(String.format("ID: %s | Name: %s | Type: %s | Impact: %.2f\n",
                h.getHazardId(), h.getName(), h.getHazardType(), h.getImpactValue()));
        }
        return sb.toString();
    }

    public String removeHazard(String hazardId) {
        Hazard h = hazardService.findHazardById(hazardId);
        if (h == null) {
            return "Hazard not found.";
        }
        boolean success = hazardService.removeHazard(hazardId);
        if (success) {
            hazardService.model.allIds.remove(hazardId);
            return "Hazard removed!";
        } else {
            return "Failed to remove hazard.";
        }
    }

    public Hazard findHazardById(String id) {
        return hazardService.findHazardById(id);
    }
}
