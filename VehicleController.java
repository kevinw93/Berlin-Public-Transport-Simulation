import java.util.List;

public class VehicleController {
    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    public String addVehicle(String vehicleID, String name, String typeStr, String capacityStr, String speedStr) {
        if (vehicleID == null || vehicleID.trim().isEmpty() || name == null || name.trim().isEmpty() ||
            typeStr == null || capacityStr == null || speedStr == null) {
            return "All fields are required.";
        }
        if (vehicleID.length() > 99) {
            return "Vehicle ID must be less than 100 characters.";
        }
        if (!vehicleID.matches("[a-zA-Z0-9]+")) {
            return "Vehicle ID must be alphanumeric.";
        }
        if (vehicleService.model.allIds.contains(vehicleID)) {
            return "ID already exists globally.";
        }
        VehicleType type;
        switch (typeStr) {
            case "S_Bahn": type = new SbahnType(); break;
            case "U_Bahn": type = new UbahnType(); break;
            case "Tram": type = new TramType(); break;
            case "Bus": type = new BusType(); break;
            default: return "Invalid vehicle type.";
        }
        int capacity;
        double speed;
        try {
            capacity = Integer.parseInt(capacityStr);
            if (capacity <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            return "Capacity must be a positive integer.";
        }
        try {
            speed = Double.parseDouble(speedStr);
            if (speed <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            return "Speed must be a positive number.";
        }
        boolean success = vehicleService.addVehicle(vehicleID, name, type, capacity, speed);
        if (success) {
            vehicleService.model.allIds.add(vehicleID);
            return "Vehicle added: " + vehicleID;
        } else {
            return "Failed to add vehicle. Check for duplicate ID or invalid input.";
        }
    }

    public String showVehicles() {
        List<Vehicle> vehicles = vehicleService.getVehicles();
        if (vehicles.isEmpty()) {
            return "There are no vehicles to display!";
        }
        StringBuilder sb = new StringBuilder();
        for (Vehicle v : vehicles) {
            sb.append(String.format("ID: %s | Name: %s | Type: %s | Capacity: %d | Speed: %.2f\n",
                v.getVehicleId(), v.getName(), v.getVehicleType(), v.getCapacity(), v.getSpeed()));
        }
        return sb.toString();
    }

    public String setVehicleCapacity(String vehicleId, int capacity) {
        if (capacity < 0) {
            return "Capacity must be non-negative.";
        }
        boolean success = vehicleService.setVehicleCapacity(vehicleId, capacity);
        if (success) {
            return "Vehicle capacity updated.";
        } else {
            return "Failed to update vehicle capacity. Vehicle not found or invalid input.";
        }
    }

    public String setVehicleRiderCount(String vehicleId, int count) {
        if (count < 0) {
            return "Rider count must be non-negative.";
        }
        boolean success = vehicleService.setVehicleRiderCount(vehicleId, count);
        if (success) {
            return "Vehicle rider count updated.";
        } else {
            return "Failed to update rider count. Vehicle not found or invalid input.";
        }
    }

    public String removeVehicle(String vehicleId) {
        Vehicle v = vehicleService.findVehicleById(vehicleId);
        if (v == null) {
            return "Vehicle not found.";
        }
        if (v.getAssignedRoute() != null) {
            return "Cannot remove vehicle: it is assigned to a route.";
        }
        boolean success = vehicleService.removeVehicle(vehicleId);
        if (success) {
            vehicleService.model.allIds.remove(vehicleId);
            return "Vehicle removed!";
        } else {
            return "Failed to remove vehicle.";
        }
    }

    public Vehicle findVehicleById(String id) {
        return vehicleService.findVehicleById(id);
    }
}
