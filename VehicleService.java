import java.util.List;

public class VehicleService {
    public final TransitDataModel model;

    public VehicleService(TransitDataModel model) {
        this.model = model;
    }

    public boolean addVehicle(String vehicleID, String name, VehicleType type, int capacity, double speed) {
        if (vehicleID.length() > 99) {
            return false;
        }
        if (!vehicleID.matches("[a-zA-Z0-9]+")) {
            return false;
        }
        if (findVehicleById(vehicleID) != null) {
            return false;
        }
        Vehicle v = new Vehicle(vehicleID, name, type, capacity, speed);
        model.vehicles.add(v);
        return true;
    }

    public List<Vehicle> getVehicles() {
        return model.vehicles;
    }

    public Vehicle findVehicleById(String id) {
        for (Vehicle v : model.vehicles) {
            if (v.getVehicleId().equals(id)) {
                return v;
            }
        }
        return null;
    }

    public boolean setVehicleCapacity(String vehicleId, int capacity) {
        Vehicle v = findVehicleById(vehicleId);
        if (v == null || capacity < 0) {
            return false;
        }
        v.setCapacity(capacity);
        return true;
    }

    public boolean setVehicleRiderCount(String vehicleId, int count) {
        Vehicle v = findVehicleById(vehicleId);
        if (v == null) {
            return false;
        }
        v.setRidingPass(count);
        return true;
    }

    public boolean removeVehicle(String vehicleId) {
        Vehicle v = findVehicleById(vehicleId);
        if (v == null) {
            return false;
        }
        model.vehicles.remove(v);
        return true;
    }
}

