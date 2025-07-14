//redundant old code don't use
public class TestDataInitializer {
    public static void main(String[] args) {
        // Create the data model
        TransitDataModel model = new TransitDataModel();
        
        // Create the initializer
        TransitDataInitializer initializer = new TransitDataInitializer(model);
        
        System.out.println("=== Initializing Transit Data ===");
        
        // Initialize all hardcoded data
        initializer.initializeAllData();
        
        System.out.println("\n=== Assigning Vehicles to Routes ===");
        initializer.assignVehiclesToRoutes();
        
        System.out.println("\n=== Adding Custom Rider Events ===");
        
        // Example of adding custom rider events that will occur in the future
        // These are additional events beyond the ones from the spreadsheet
        initializer.addRiderEventAtLocation(5, "arriving_at", "loc_5", 15);  // 5 riders arriving at airport in 15 minutes
        initializer.addRiderEventAtLocation(3, "departing_from", "loc_3", 20);  // 3 riders departing from tower in 20 minutes
        initializer.addRiderEventAtLocation(8, "arriving_at", "loc_7", 25);  // 8 riders arriving at tower in 25 minutes
        
        // Example of adding random events at random locations
        initializer.addRiderEvent(4, "arriving_at", 30);  // 4 riders arriving at random location in 30 minutes
        initializer.addRiderEvent(2, "departing_from", 35);  // 2 riders departing from random location in 35 minutes
        
        System.out.println("\n=== Starting Vehicle Movement ===");
        initializer.startAllVehicles();
        
        System.out.println("\n=== Current Simulation State ===");
        System.out.println("Current time: " + new SimulationService(model).getCurrentTime());
        System.out.println("Locations: " + new LocationService(model).getLocations().size());
        System.out.println("Routes: " + new RouteService(model).getRoutes().size());
        System.out.println("Vehicles: " + new VehicleService(model).getVehicles().size());
        System.out.println("Hazards: " + new HazardService(model).getHazards().size());
        
        System.out.println("\n=== Sample Location Details ===");
        Location airport = new LocationService(model).findLocationById("loc_5");
        if (airport != null) {
            System.out.println("Airport waiting passengers: " + airport.getWaitingPassengers());
            System.out.println("Airport debark range: " + airport.getDebarkMin() + "-" + airport.getDebarkMax());
            System.out.println("Airport embark range: " + airport.getEmbarkMin() + "-" + airport.getEmbarkMax());
        }
        
        System.out.println("\n=== Ready for Simulation ===");
        System.out.println("You can now run the simulation with: java Main");
        System.out.println("Or use the GUI with: java TransitSimulatorGUI");
    }
} 
