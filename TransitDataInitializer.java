//redundant old code don't use
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class TransitDataInitializer {
    private TransitDataModel model;
    private SimulationService simService;
    
    public TransitDataInitializer(TransitDataModel model) {
        this.model = model;
        this.simService = new SimulationService(model);
    }
    
    // initializes all hardcoded data from the spreadsheet
    public void initializeAllData() {
        initializeLocations();
        initializeRoutes();
        initializeVehicles();
        initializeHazards();
        initializeRiderEvents();
    }
    
    // creates locations with their coordinates and passenger ranges
    private void initializeLocations() {
        // Location data from spreadsheet: ID, Name, X, Y, waiting passengers
        // Based on the locations table showing loc_3 through loc_9, port, and gate
        addLocation("loc_3", "tower", 1, 3, 10);
        addLocation("loc_4", "east", 4, 5, 10);
        addLocation("loc_5", "airport", 7, 4, 10);
        addLocation("loc_6", "bahnhof", 8, 4, 10);
        addLocation("loc_7", "tower", 10, 2, 10);
        addLocation("loc_8", "west", 5, 3, 10);
        addLocation("loc_9", "north", 7, 4, 10);
        addLocation("port", "north", 6, 1, 10);
        addLocation("gate", "south", 2, 0, 10);
        
        // Set passenger ranges based on spreadsheet probabilities
        setLocationPassengerRanges();
    }
    
    // sets passenger ranges for each location based on spreadsheet data
    private void setLocationPassengerRanges() {
        // Using the exact probability ranges from the spreadsheet "probabilities - arriving, transferring, departing" table
        // Format: debark (arr_low, arr_high), transfer (trans_low, trans_high), embark (dep_low, dep_high)
        setRangesForLocation("loc_3", 2, 6, 2, 3, 1, 1);
        setRangesForLocation("loc_4", 1, 2, 1, 1, 1, 1);
        setRangesForLocation("loc_5", 1, 2, 1, 3, 1, 1);
        setRangesForLocation("loc_6", 1, 1, 1, 1, 1, 2);
        setRangesForLocation("loc_7", 1, 4, 1, 6, 3, 6);
        setRangesForLocation("loc_8", 1, 2, 1, 2, 1, 1);
        setRangesForLocation("loc_9", 2, 4, 1, 4, 2, 6);
        setRangesForLocation("port", 1, 2, 1, 1, 1, 1);
        setRangesForLocation("gate", 1, 1, 1, 2, 1, 3);
    }
    
    // creates routes connecting the locations based on travel times from spreadsheet
    private void initializeRoutes() {
        RouteService routeService = new RouteService(model);
        
        // Create routes based on the travel times table from spreadsheet
        // The table shows travel times between locations for different vehicle types
        routeService.addRoute("route_1", "Main Transit Line");
        
        // Add locations to route based on travel times from spreadsheet
        // Using the "travel times (minutes)" table data
        routeService.addLocationToRoute("route_1", findLocation("loc_3"), 0);
        routeService.addLocationToRoute("route_1", findLocation("loc_4"), 1);
        routeService.addLocationToRoute("route_1", findLocation("loc_5"), 2);
        routeService.addLocationToRoute("route_1", findLocation("loc_6"), 3);
        routeService.addLocationToRoute("route_1", findLocation("loc_7"), 4);
        
        routeService.addRoute("route_2", "Secondary Line");
        routeService.addLocationToRoute("route_2", findLocation("loc_8"), 0);
        routeService.addLocationToRoute("route_2", findLocation("loc_9"), 1);
        routeService.addLocationToRoute("route_2", findLocation("port"), 2);
        routeService.addLocationToRoute("route_2", findLocation("gate"), 3);
    }
    
    // creates vehicles of different types based on travel times table speeds
    private void initializeVehicles() {
        VehicleService vehicleService = new VehicleService(model);
        
        // Vehicle types and speeds from the "travel times (minutes)" table
        // speeds (km/hr): 60, 45, 40, 35 for s-bahn, u-bahn, tram, bus respectively
        
        // S-Bahn vehicles (60 km/hr) - highest speed
        vehicleService.addVehicle("s1", "S-Bahn 1", new SbahnType(), 200, 60);
        vehicleService.addVehicle("s2", "S-Bahn 2", new SbahnType(), 200, 60);
        
        // U-Bahn vehicles (45 km/hr) - second highest speed
        vehicleService.addVehicle("u1", "U-Bahn 1", new UbahnType(), 150, 45);
        vehicleService.addVehicle("u2", "U-Bahn 2", new UbahnType(), 150, 45);
        
        // Tram vehicles (40 km/hr) - third speed
        vehicleService.addVehicle("t1", "Tram 1", new TramType(), 100, 40);
        vehicleService.addVehicle("t2", "Tram 2", new TramType(), 100, 40);
        
        // Bus vehicles (35 km/hr) - slowest speed
        vehicleService.addVehicle("b1", "Bus 1", new BusType(), 50, 35);
        vehicleService.addVehicle("b2", "Bus 2", new BusType(), 50, 35);
    }
    
    // creates some initial hazards
    private void initializeHazards() {
        HazardService hazardService = new HazardService(model);
        
        // Add a hazard at the airport
        Hazard airportHazard = new Hazard("h1", "Construction at Airport", new ShortTermHazardType(), 1.5);
        airportHazard.setLocation(findLocation("loc_5"));
        hazardService.addHazard(airportHazard);
        
        // Add a hazard between two locations
        Hazard routeHazard = new Hazard("h2", "Traffic Jam", new LongTermHazardType(), 2.0);
        routeHazard.setLocations(findLocation("loc_3"), findLocation("loc_4"));
        hazardService.addHazard(routeHazard);
    }
    
    // initializes rider events based on spreadsheet data
    private void initializeRiderEvents() {
        // Add events from the "riders" table in the spreadsheet
        // Format: riders, status, event_time
        addRiderEvent(2, "arriving_at", 0);
        addRiderEvent(3, "departing_from", 4);
        addRiderEvent(3, "departing_from", 3);
        addRiderEvent(7, "arriving_at", 4);
        addRiderEvent(4, "arriving_at", 10);
        addRiderEvent(3, "departing_from", 2);
        addRiderEvent(5, "departing_from", 8);
        addRiderEvent(2, "arriving_at", 5);
    }
    
    // adds a location to the model
    private void addLocation(String id, String name, double x, double y, int waitingPassengers) {
        LocationService locationService = new LocationService(model);
        // Use default ranges that will be set later
        locationService.addLocation(id, name, x, y, waitingPassengers, 1, 2, 1, 1, 1, 1);
    }
    
    // sets passenger ranges for a specific location
    private void setRangesForLocation(String locationId, int debarkMin, int debarkMax, 
                                    int transferMin, int transferMax, int embarkMin, int embarkMax) {
        Location location = findLocation(locationId);
        if (location != null) {
            location.setDebarkRange(debarkMin, debarkMax);
            location.setTransferRange(transferMin, transferMax);
            location.setEmbarkRange(embarkMin, embarkMax);
        }
    }
    
    // finds a location by ID
    private Location findLocation(String locationId) {
        LocationService locationService = new LocationService(model);
        return locationService.findLocationById(locationId);
    }
    
    // adds a rider event that will occur in x minutes
    public void addRiderEvent(int riders, String status, int minutesFromNow) {
        int eventTime = simService.getCurrentTime() + minutesFromNow;
        
        // Find a random location for the event
        List<Location> locations = new LocationService(model).getLocations();
        if (locations.isEmpty()) {
            return;
        }
        
        Random rand = new Random();
        Location randomLocation = locations.get(rand.nextInt(locations.size()));
        
        if (status.equals("arriving_at")) {
            // Add riders to waiting passengers
            int currentWaiting = randomLocation.getWaitingPassengers();
            randomLocation.setWaitingPassengers(currentWaiting + riders);
            System.out.printf("Event: %d riders arriving at %s in %d minutes\n", 
                            riders, randomLocation.getName(), minutesFromNow);
        } else if (status.equals("departing_from")) {
            // Reduce waiting passengers (but don't go below 0)
            int currentWaiting = randomLocation.getWaitingPassengers();
            int newWaiting = Math.max(0, currentWaiting - riders);
            randomLocation.setWaitingPassengers(newWaiting);
            System.out.printf("Event: %d riders departing from %s in %d minutes\n", 
                            riders, randomLocation.getName(), minutesFromNow);
        }
    }
    
    // adds a custom rider event with specific location
    public void addRiderEventAtLocation(int riders, String status, String locationId, int minutesFromNow) {
        Location location = findLocation(locationId);
        if (location == null) {
            System.out.println("Location not found: " + locationId);
            return;
        }
        
        int eventTime = simService.getCurrentTime() + minutesFromNow;
        
        if (status.equals("arriving_at")) {
            int currentWaiting = location.getWaitingPassengers();
            location.setWaitingPassengers(currentWaiting + riders);
            System.out.printf("Event: %d riders arriving at %s in %d minutes\n", 
                            riders, location.getName(), minutesFromNow);
        } else if (status.equals("departing_from")) {
            int currentWaiting = location.getWaitingPassengers();
            int newWaiting = Math.max(0, currentWaiting - riders);
            location.setWaitingPassengers(newWaiting);
            System.out.printf("Event: %d riders departing from %s in %d minutes\n", 
                            riders, location.getName(), minutesFromNow);
        }
    }
    
    // assigns vehicles to routes
    public void assignVehiclesToRoutes() {
        RouteService routeService = new RouteService(model);
        VehicleService vehicleService = new VehicleService(model);
        
        List<Vehicle> vehicles = vehicleService.getVehicles();
        List<Route> routes = routeService.getRoutes();
        
        if (vehicles.isEmpty() || routes.isEmpty()) {
            return;
        }
        
        // Assign first few vehicles to route 1
        if (vehicles.size() >= 2 && routes.size() >= 1) {
            routeService.assignVehicleToRoute("route_1", vehicles.get(0), 0, true);
            routeService.assignVehicleToRoute("route_1", vehicles.get(1), 2, false);
        }
        
        // Assign next few vehicles to route 2
        if (vehicles.size() >= 4 && routes.size() >= 2) {
            routeService.assignVehicleToRoute("route_2", vehicles.get(2), 0, true);
            routeService.assignVehicleToRoute("route_2", vehicles.get(3), 1, false);
        }
    }
    
    // starts vehicle movement on all assigned routes
    public void startAllVehicles() {
        VehicleService vehicleService = new VehicleService(model);
        List<Vehicle> vehicles = vehicleService.getVehicles();
        
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getAssignedRoute() != null) {
                simService.startVehicleMovement(vehicle);
            }
        }
    }
} 
