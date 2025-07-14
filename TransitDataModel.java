import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TransitDataModel {
    public final List<Vehicle> vehicles = new ArrayList<>();
    public final List<Location> locations = new ArrayList<>();
    public final List<Route> routes = new ArrayList<>();
    public final List<Hazard> hazards = new ArrayList<>();
    // Tracks all IDs globally to enforce uniqueness across all types
    public final Set<String> allIds = new HashSet<>();
    
    public TransitDataModel() {
        // Initialize the Event hazard list
        Event.setHazardList(hazards);
    }
} 