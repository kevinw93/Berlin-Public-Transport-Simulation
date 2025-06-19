import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    // ansii color codes for text menu and error messages
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    private static Scanner scanner = new Scanner(System.in);
    private static List<Vehicle> vList = new ArrayList<>();
    private static List<Location> lList = new ArrayList<>();
    private static List<Route> rList = new ArrayList<>();
    private static SimulationManager simManager = SimulationManager.getInstance();

    
    public static void main(String[] args) {
        boolean exit = false;
        String menuState = "MAIN";

        // program loop
        while (!exit) {
            switch (menuState) {
                
                case "MAIN":
                    System.out.println(ANSI_PURPLE + "\nMain Menu" + ANSI_RESET);
                    System.out.println("1. Vehicle");
                    System.out.println("2. Location");
                    System.out.println("3. Route");
                    System.out.println("4. Simulation");
                    System.out.println("5. Exit");
                    System.out.print("Choose an option: ");
                    String mainSwitch = scanner.nextLine().trim();

                    switch (mainSwitch) {
                        case "1":
                            menuState = "VEHICLE";
                            break;
                        case "2":
                            menuState = "LOCATION";
                            break;
                        case "3":
                            menuState = "ROUTE";
                            break;
                        case "4":
                            menuState = "SIMULATION";
                            break;
                        case "5":
                            exit = true;
                            break;
                        default:
                            System.out.println(ANSI_RED + "Must enter 1-5, try again." + ANSI_RESET);
                    }
                    break;

                
                case "VEHICLE":
                    System.out.println(ANSI_BLUE + "\nVehicle Menu" + ANSI_RESET);
                    System.out.println("1. Add a vehicle");
                    System.out.println("2. Display all vehicles");
                    System.out.println("3. Back to main menu");
                    System.out.print("Choose an option: ");
                    String vehicleSwitch = scanner.nextLine().trim();

                    switch (vehicleSwitch) {
                        case "1":
                            addVehicle();
                            break;
                        case "2":
                            showVehicle();
                            break;
                        case "3":
                            menuState = "MAIN";
                            break;
                        default:
                            System.out.println(ANSI_RED + "Must enter 1-3, try again." + ANSI_RESET);
                    }
                    break;

                
                case "LOCATION":
                    System.out.println(ANSI_BLUE + "\nLocation Menu" + ANSI_RESET);
                    System.out.println("1. Add a location");
                    System.out.println("2. Display all locations");
                    System.out.println("3. Show vehicles at location");
                    System.out.println("4. Back to main menu");
                    System.out.print("Choose an option: ");
                    String locationSwitch = scanner.nextLine().trim();

                    switch (locationSwitch) {
                        case "1":
                            addLocation();
                            break;
                        case "2":
                            showLocation();
                            break;
                        case "3":
                            showVehiclesAtLocation();
                            break;
                        case "4":
                            menuState = "MAIN";
                            break;
                        default:
                            System.out.println(ANSI_RED + "Must enter 1-4, try again." + ANSI_RESET);
                    }
                    break;

                
                case "ROUTE":
                    System.out.println(ANSI_BLUE + "\nRoute Menu" + ANSI_RESET);
                    System.out.println("1. Add a route");
                    System.out.println("2. Display all routes");
                    System.out.println("3. Add location to route");
                    System.out.println("4. Remove location from route");
                    System.out.println("5. Show locations on route");
                    System.out.println("6. Assign vehicle to route");
                    System.out.println("7. Remove route from vehicle");
                    System.out.println("8. Show vehicles on route");
                    System.out.println("9. Back to main menu");
                    System.out.print("Choose an option: ");
                    String routeSwitch = scanner.nextLine().trim();
                    
                    switch (routeSwitch) {
                        case "1":
                            addRoute();
                            break;
                        case "2":
                            showRoute();
                            break;
                        case "3":
                            locationRoute();
                            break;
                        case "4":
                            locationRouteRemove();
                            break;
                        case "5":
                            showRouteContent();
                            break;
                        case "6": 
                            addVehicleToRoute(); 
                            break; 
                        case "7":
                            removeVehicleFromRoute(); 
                            break; 
                        case "8": 
                            showVehiclesOnRoute();
                            break;
                        case "9":
                            menuState = "MAIN";
                            break;
                        default:
                            System.out.println(ANSI_RED + "Must enter 1-9, try again." + ANSI_RESET);
                    }
                    break;
                case "SIMULATION":
                    System.out.println(ANSI_CYAN + "\nSimulation Menu" + ANSI_RESET);
                    System.out.println("1. Display current time");
                    System.out.println("2. Advance simulation time");
                    System.out.println("3. Show next event");
                    System.out.println("4. Execute next event");
                    System.out.println("5. Show vehicle status");
                    System.out.println("6. Start vehicle on route");
                    System.out.println("7. Back to main menu");
                    System.out.print("Choose an option: ");
                    String simSwitch = scanner.nextLine().trim();
                    
                    switch (simSwitch) {
                        case "1":
                            showTime();
                            break;
                        case "2":
                            advanceTime();
                            break;
                        case "3":
                            showEvent();
                            break;
                        case "4":
                            executeEvent();
                            break;
                        case "5":
                            vehicleStatus();
                            break;
                        case "6":
                            startVehicleMovement();
                            break;
                        case "7":
                            menuState = "MAIN";
                            break;
                        default:
                            System.out.println(ANSI_RED + "Must enter 1-7, try again!!" + ANSI_RESET);
                    }
                    break;
            }
        }

        System.out.println(ANSI_CYAN + "Exiting program!" + ANSI_RESET);
    }

    
    private static void addVehicle() {
        System.out.println(ANSI_GREEN + "\nAdding Vehicle" + ANSI_RESET);
        System.out.println("Enter vehicle ID: ");
        String vehicleID = scanner.nextLine().trim();
        
        if (vehicleID.length() > 99) {
            System.out.println(ANSI_RED + "The vehicle ID cannot be 100 characters or more!. Try again." + ANSI_RESET);
            return;

        }

        if (!vehicleID.matches("[a-zA-Z0-9]+")) {
            System.out.println(ANSI_RED + "The vehicle ID must be alphanumeric! Try again." + ANSI_RESET);
            return;

        }
        
        if (findVehicleId(vehicleID) != null) {
            System.out.println(ANSI_RED + "There is already a vehicle with that ID! Try again" + ANSI_RESET);
            return;

        }
        if (findLocationId(vehicleID) != null) {
            System.out.println(ANSI_RED + "There is already a location with that ID! Try again" + ANSI_RESET);
            return;

        }
        if (findRouteId(vehicleID) != null) {
            System.out.println(ANSI_RED + "There is already a route with that ID! Try again" + ANSI_RESET);
            return;

        }


        System.out.println("Enter vehicle name: ");
        String name = scanner.nextLine().trim();

        try {
            System.out.println(ANSI_YELLOW + "\nChoose Vehicle Type" + ANSI_RESET);
            System.out.println("1. S_Bahn");
            System.out.println("2. U_Bahn");
            System.out.println("3. Tram");
            System.out.println("4. Bus");
            System.out.print("Choose an option: ");
            String typeSwitch = scanner.nextLine().trim();
            VehicleType type = VehicleType.S_Bahn;
            
                    
                switch (typeSwitch) {
                    case "1":
                        type = VehicleType.S_Bahn;
                        break;
                    case "2":
                        type = VehicleType.U_Bahn;
                        break;
                    case "3":
                        type = VehicleType.Tram;
                        break;
                    case "4":
                        type = VehicleType.Bus;
                        break;
                    default:
                        throw new IllegalArgumentException(ANSI_RED + "Must enter 1-4, try again." + ANSI_RESET);
                }
            
            System.out.print("Enter vehicle capacity: ");
            int capacity = Integer.parseInt(scanner.nextLine().trim());
            if (capacity <= 0) {
                throw new NumberFormatException();
            }
            System.out.print("Enter vehicle speed: ");
            double speed = Double.parseDouble(scanner.nextLine().trim());
            if (speed <= 0) {
                throw new NumberFormatException();
            }

            Vehicle v = new Vehicle(vehicleID, name, type, capacity, speed);
            vList.add(v);
            System.out.println("Vehicle added: " + vehicleID);

        } catch (NumberFormatException e) {
            System.out.println(ANSI_RED + "Capacity must be a postive integer and speed must be a positive number." + ANSI_RESET);
            return;
        } catch (IllegalArgumentException e) {
            System.out.println(ANSI_RED + "Error: " + e.getMessage() + ANSI_RESET);
            return;
        }
    }

    private static void showVehicle() {
        System.out.println(ANSI_GREEN + "\nDisplaying Vehicles" + ANSI_RESET);
        if (vList.isEmpty()) {
            System.out.println("There are no vehicles to display!");
            return;

        }
        List<Vehicle> sorted = new ArrayList<>(vList);
        sorted.sort(null);
        for (Vehicle v : sorted) {
            System.out.printf("ID: %s | Name: %s | Type: %s | Capacity: %d | Speed: %.2f\n",
                v.getVehicleId(), v.getName(), v.getVehicleType(), v.getCapacity(), v.getSpeed());
        }
        
    }

    private static Vehicle findVehicleId(String id) {
        for (Vehicle v : vList) {
            if (v.getVehicleId().equals(id)) {
                return v;
            }
        }
        return null;
    }

    private static void addLocation() {
        System.out.println(ANSI_GREEN + "\nAdding Location" + ANSI_RESET);
        System.out.println("Enter location ID: ");
        String locationID = scanner.nextLine().trim();
        
        if (locationID.length() > 99) {
            System.out.println(ANSI_RED + "The location ID cannot be 100 characters or more!. Try again." + ANSI_RESET);
            return;

        }
        if (!locationID.matches("[a-zA-Z0-9]+")) {
            System.out.println(ANSI_RED + "The location ID must be alphanumeric! Try again." + ANSI_RESET);
            return;
            
        }
        if (findVehicleId(locationID) != null) {
            System.out.println(ANSI_RED + "There is already a vehicle with that ID! Try again" + ANSI_RESET);
            return;

        }
        if (findLocationId(locationID) != null) {
            System.out.println(ANSI_RED + "There is already a location with that ID! Try again" + ANSI_RESET);
            return;

        }
        if (findRouteId(locationID) != null) {
            System.out.println(ANSI_RED + "There is already a route with that ID! Try again" + ANSI_RESET);
            return;

        }
        
        
        System.out.println("Enter location name: ");
        String name = scanner.nextLine().trim();

        
        try {
            System.out.println("Enter x-coordinate of location: ");
            double xCoord = Double.parseDouble(scanner.nextLine().trim());
    
            System.out.println("Enter y-coordinate of location: ");
            double yCoord = Double.parseDouble(scanner.nextLine().trim());

            Location l = new Location(locationID, name, xCoord, yCoord);
            lList.add(l);
            System.out.println("Location added: " + locationID);

        } catch (NumberFormatException e) {
            System.out.println(ANSI_RED + "x-coordinate and y-coordinate must be an decimal number." + ANSI_RESET);
        } catch (IllegalArgumentException e) {
            System.out.println(ANSI_RED + "Error: " + e.getMessage() + ANSI_RESET);
        }

    }
    
    private static void showLocation() {
        System.out.println(ANSI_GREEN + "\nDisplaying Locations" + ANSI_RESET);
        if (lList.isEmpty()) {
            System.out.println("There are no locations to display!");
            return;

        }
        List<Location> sorted = new ArrayList<>(lList);
        sorted.sort(null);
        for (Location l : sorted) {
            System.out.printf("ID: %s | Name: %s | Coordinate: (%.2f, %.2f)\n",
                l.getLocationId(), l.getName(), l.getXCoord(), l.getYCoord());
        }
    }
    
    private static Location findLocationId(String id) {
        for (Location l : lList) {
            if (l.getLocationId().equals(id)) {
                return l;
            }
        }
        return null;
    }

    // display what vehicles are currently at a location
    private static void showVehiclesAtLocation() {
        System.out.println(ANSI_YELLOW + "\nDisplaying Vehicles at Location" + ANSI_RESET);
        System.out.println("Enter location ID: ");
        String locationID = scanner.nextLine().trim();
        
        Location location = findLocationId(locationID);
        if (location == null) { 
            System.out.println("No location was found with that ID: " + locationID);
            return; 
        } 
        
        List<Vehicle> vehiclesAtLocation = new ArrayList<>();
        
        for (Vehicle v : vList) { 
            if (location.equals(v.getLocation()) && !(v.inTransit())) {
                vehiclesAtLocation.add(v);
            }
        }
        
        if (vehiclesAtLocation.isEmpty()) { 
            System.out.println("No vehicles are currently at the location " + location.getName()
                + " with ID " + location.getLocationId() + ".");
            return; 
        }
        
        System.out.println("Vehicles at location " + location.getName() + ":");
        List<Vehicle> sorted = new ArrayList<>(vehiclesAtLocation);
        sorted.sort(null);
        for (Vehicle v : sorted) { 
            System.out.printf("ID: %s | Name: %s | Type: %s\n",
                v.getVehicleId(), v.getName(), v.getVehicleType());
        } 
    } 

    // create a route
    private static void addRoute() {
        System.out.println(ANSI_GREEN + "\nAdding Route" + ANSI_RESET);
        System.out.println("Enter route ID: ");
        String routeID = scanner.nextLine().trim();
        if (routeID.length() > 99) {
            System.out.println(ANSI_RED + "The route ID cannot be 100 characters or more!. Try again." + ANSI_RESET);
            return;

        }
        if (!routeID.matches("[a-zA-Z0-9]+")) {
            System.out.println(ANSI_RED + "The route ID must be alphanumeric! Try again." + ANSI_RESET);
            return;
            
        }
        if (findVehicleId(routeID) != null) {
            System.out.println(ANSI_RED + "There is already a vehicle with that ID! Try again." + ANSI_RESET);
            return;

        }
        if (findLocationId(routeID) != null) {
            System.out.println(ANSI_RED + "There is already a location with that ID! Try again." + ANSI_RESET);
            return;

        }
        if (findRouteId(routeID) != null) {
            System.out.println(ANSI_RED + "There is already a route with that ID! Try again." + ANSI_RESET);
            return;

        }
        System.out.println("Enter route name: ");
        String rName = scanner.nextLine().trim();
        try {
            Route r = new Route(routeID, rName);
            rList.add(r);
            System.out.println("Route added: " + routeID);

        } catch (IllegalArgumentException e) {
            System.out.println(ANSI_RED + "Error: " + e.getMessage() + ANSI_RESET);
        }
    }

    private static void showRoute() {
        System.out.println(ANSI_GREEN + "\nDisplaying Routes" + ANSI_RESET);
        if (rList.isEmpty()) {
            System.out.println("There are no routes to display!");
            return;

        }

        List<Route> sorted = new ArrayList<>(rList);
        sorted.sort(null);
        for (Route r : sorted) {
            System.out.printf("ID: %s | Name: %s\n",
                r.getRouteId(), r.getName());
        }
    }

    private static Route findRouteId(String id) {
        for (Route r : rList) {
            if (r.getRouteId().equals(id)) {
                return r;
            }
        }
        return null;
    }

    // method to add a location to a route
    private static void locationRoute() {
        System.out.println(ANSI_YELLOW + "\nAdding Location to Route" + ANSI_RESET);
        System.out.println("Enter route ID: ");
        String routeID = scanner.nextLine().trim();
        
        
        if (findRouteId(routeID) == null) {
            System.out.println(ANSI_RED + "No route was found with the inputted ID: " + routeID + ANSI_RESET);
            return;

        }
        System.out.println("Enter location ID: ");
        String locationID = scanner.nextLine().trim();
        
        if (findLocationId(locationID) == null) {
            System.out.println(ANSI_RED + "No location was found with the inputted ID: " + locationID + ANSI_RESET);
            return;

        }
        int indexAdd = 0;
        Route route = findRouteId(routeID);
        Location found = findLocationId(locationID);
        try {
            System.out.print("Enter index to add new location to: ");
            indexAdd = Integer.parseInt(scanner.nextLine().trim());
            if (indexAdd < 0) {
                System.out.println(ANSI_RED + "Index cannot be negative. Try again." + ANSI_RESET);
                return;


            } else if (indexAdd > route.getLocationCount()) {
                System.out.println(ANSI_RED + "Index is larger than the size of the list. Try again." + ANSI_RESET);
                return;

            }

        } catch (NumberFormatException e) {
            System.out.println(ANSI_RED + "Index must be an integer. Try again." + ANSI_RESET);
            return;
        } catch (IllegalArgumentException e) {
            System.out.println(ANSI_RED + "Error: " + e.getMessage() + ANSI_RESET);
            return;
        }
        
        route.addLocation(found, indexAdd);
        System.out.println("The location " + found.getName() + " was added to the route "
            + route.getName() + " at index " + indexAdd + ".");

    }

    // remove a location from a route
    private static void locationRouteRemove() {
        System.out.println(ANSI_YELLOW + "\nRemoving Location from Route" + ANSI_RESET);
        System.out.println("Enter route ID: ");
        String routeID = scanner.nextLine().trim();
        
        
        if (findRouteId(routeID) == null) {
            System.out.println(ANSI_RED + "No route was found with the inputted ID: " + routeID + ANSI_RESET);
            return;

        }
        Route route = findRouteId(routeID);
        int indexRemove = 0;

        try {
            System.out.print("Enter index to remove location from: ");
            indexRemove = Integer.parseInt(scanner.nextLine().trim());
            if (indexRemove < 0) {
                System.out.println(ANSI_RED + "Index cannot be negative. Try again." + ANSI_RESET);
                return;


            } else if (indexRemove >= route.getLocationCount()) {
                System.out.println(ANSI_RED + "Index is larger than the size of the list. Try again." + ANSI_RESET);
                return;

            }

        } catch (NumberFormatException e) {
            System.out.println(ANSI_RED + "Index must be an integer. Try again." + ANSI_RESET);
            return;
        } catch (IllegalArgumentException e) {
            System.out.println(ANSI_RED + "Error: " + e.getMessage() + ANSI_RESET);
            return;
        }

        
        for (Vehicle v : route.getVehicles()) {
            if (v.inTransit()) {
                int currentPos = v.getRoutePosition();
                int nextPos = (currentPos + 1) % route.getLocationCount();

                if (nextPos == indexRemove) {
                    System.out.println(ANSI_RED + "Cannot remove location at index " + indexRemove
                            + " because vehicle " + v.getVehicleId() + " is in transit to it!" + ANSI_RESET);

                    return;
                }
            }
        }
        for (Vehicle v : route.getVehicles()) {
            if (v.getRoutePosition() == indexRemove) {
                v.setAssignedRoute(null);
                v.setLocation(null);
                v.setRoutePosition(-1);
                v.setInTransit(false);
                route.removeVehicle(v); // make sure we remove vehicle from route when its location is removed

            } else if (v.getRoutePosition() > indexRemove) {
                v.setRoutePosition(v.getRoutePosition() - 1);

            }
        }
        route.removeLocationAt(indexRemove);
        simManager.removeLocationEvent(route, indexRemove);
        System.out.println("The location at index " + indexRemove + " was remove from the route "
            + route.getName() + ".");
        
    }

    // show all of the locations on a route
    private static void showRouteContent() {
        System.out.println(ANSI_YELLOW + "\nDisplaying Locations on Route" + ANSI_RESET);
        System.out.println("Enter route ID: ");
        String routeID = scanner.nextLine().trim();
        
        
        if (findRouteId(routeID) == null) {
            System.out.println(ANSI_RED + "No route was found with the inputted ID: " + routeID + ANSI_RESET);
            return;

        }
        Route route = findRouteId(routeID);
        List<Location> routeLocations = route.getLocations();
        
        System.out.print("[");
        int count = 0;
        for (Location l : routeLocations) {
            System.out.printf("%s",l.getName());
            if (count < routeLocations.size() - 1) {
                System.out.print(", ");
            }
            count++;
        }
        System.out.print("]\n");
    }

    private static void addVehicleToRoute() {
        System.out.println(ANSI_YELLOW + "\nAdding Vehicle to Route" + ANSI_RESET); 
        System.out.println("Enter route ID: "); 
        String routeID = scanner.nextLine().trim();
        
        Route route = findRouteId(routeID); 
        if (route == null) {
            System.out.println(ANSI_RED + "No route was found with that ID: " + routeID + ANSI_RESET); 
            return; 
        } else if (route.getLocations().isEmpty()) {
            System.out.println(ANSI_RED + "Cannot add a vehicle to an empty route" + ANSI_RESET);
            return;
        }
        
        List<Location> locs = route.getLocations();
        int locsLen = locs.size();
        System.out.println("Route Locations:");
        for (int i = 0; i < locsLen; i++) {
            System.out.printf("%d. ID: %s | Name: %s\n", 
                                (i + 1), locs.get(i).getLocationId(), locs.get(i).getName());
        }
        int chosenPosition = -1;
        System.out.println("Enter a route position:");
        try {
            chosenPosition = Integer.parseInt(scanner.nextLine().trim());
            if (chosenPosition > locsLen || chosenPosition < 1) {
                throw new IllegalArgumentException();
            }
            chosenPosition -= 1;
        } catch (NumberFormatException e) {
            System.out.println(ANSI_RED + "Please enter an integer." + ANSI_RESET);
            return;
        } catch (IllegalArgumentException e) {
            System.out.println(ANSI_RED + "Please enter a valid position." + ANSI_RESET);
            return;
        }
        
        System.out.println("Enter vehicle ID: ");
        String vehicleID = scanner.nextLine().trim();
        
        Vehicle vehicle = findVehicleId(vehicleID);
        if (vehicle == null) { 
            System.out.println(ANSI_RED + "No vehicle was found with that ID: " + vehicleID + ANSI_RESET);
            return; 
        } 

        if (vehicle.getAssignedRoute() != null) {
            System.out.println(ANSI_RED + "Vehicle " + vehicleID + " is already assigned to route " + vehicle.getAssignedRoute().getRouteId() + ANSI_RESET);
            return;
        }

        System.out.print("Vehicle direction (enter left/right): ");
        String direction = scanner.nextLine().trim().toLowerCase();
        if (direction.equals("right") || direction.equals("r")) {
            vehicle.setGoingRight(true);
        } else if (direction.equals("left") || direction.equals("l")) {
            vehicle.setGoingRight(false);
        } else {
            System.out.println(ANSI_RED + "Invalid input. Enter either 'left' or 'right'!" + ANSI_RESET);
            return;
        }
        vehicle.setLocation(route.getLocations().get(chosenPosition));
        vehicle.setRoutePosition(chosenPosition);
        vehicle.setAssignedRoute(route);

        route.addVehicle(vehicle);
        Location assignedLocation = route.getLocations().get(chosenPosition);
        System.out.println("Vehicle " + vehicle.getName() + " was added to route " + route.getName() + " at location " +
            assignedLocation.getName() + " (ID: " + assignedLocation.getLocationId() + ")");
    } 

    private static void removeVehicleFromRoute() {
        System.out.println(ANSI_YELLOW + "\nRemoving Vehicle from Route" + ANSI_RESET);
        System.out.println("Enter route ID: "); 
        String routeID = scanner.nextLine().trim();
        
        Route route = findRouteId(routeID);
        if (route == null) { 
            System.out.println(ANSI_RED + "No route was found with that ID: " + routeID + ANSI_RESET); 
            return; 
        } 
        
        System.out.println("Enter vehicle ID: "); 
        String vehicleID = scanner.nextLine().trim(); 
        
        Vehicle vehicle = findVehicleId(vehicleID); 
        if (vehicle == null) { 
            System.out.println(ANSI_RED + "No vehicle was found with that ID: " + vehicleID + ANSI_RESET); 
            return; 
        } 
        
        if (vehicle.getAssignedRoute() != route) {
            System.out.println(ANSI_RED + "Vehicle " + vehicleID + " is not on route " + routeID + ANSI_RESET);
            return;

        }
        route.removeVehicle(vehicle); 
        vehicle.setAssignedRoute(null);
        vehicle.setRoutePosition(-1);
        vehicle.setLocation(null);
        vehicle.setInTransit(false);
        System.out.println("Vehicle " + vehicle.getName() + " was removed from route " + route.getName()); 

        simManager.removeVehicleEvent(vehicle);
    } 

    private static void showVehiclesOnRoute() { 
        System.out.println(ANSI_YELLOW + "\nDisplaying Vehicles on Route" + ANSI_RESET); 
        System.out.println("Enter route ID: "); 
        String routeID = scanner.nextLine().trim(); 
        
        Route route = findRouteId(routeID); 
        if (route == null) { 
            System.out.println(ANSI_RED + "No ro`ute was found with that ID: " + routeID + ANSI_RESET); 
            return; 
        } 
        
        List<Vehicle> vehicles = route.getVehicles(); 
        if (vehicles.isEmpty()) { 
            System.out.println("No vehicles are assigned to route " + route.getName()); 
            return; 
        } 
        
        System.out.println("Vehicles on route " + route.getName() + ":"); 
        List<Vehicle> sorted = new ArrayList<>(vehicles);
        sorted.sort(null);
        for (Vehicle v : sorted) { 
            System.out.printf("ID: %s | Name: %s | Type: %s\n", 
                v.getVehicleId(), v.getName(), v.getVehicleType()); 
        } 
    } 

    private static void showTime() {
        System.out.println("Current time is: " + simManager.getCurrentTime());
    }

    private static void advanceTime() {
        System.out.println("Current time: " + simManager.getCurrentTime());
        System.out.print("Enter minutes to advance: ");
        try {
            int minAdvance = Integer.parseInt(scanner.nextLine().trim());
            if (minAdvance <= 0) {
                System.out.println(ANSI_RED + "The number of minutes must be positive. Try again!" + ANSI_RESET);
                return;
            }
            int newTime = simManager.getCurrentTime() + minAdvance;

            while (simManager.hasNextEvent() && (simManager.getNextEventTime() <= newTime)) {
               
                simManager.executeNextEvent();
            }

            if (simManager.getCurrentTime() < newTime) {
                simManager.advanceTime(newTime);
            }
            
            System.out.println("The simulation time is now: " + simManager.getCurrentTime());

            
        } catch (NumberFormatException e) {
            System.out.println(ANSI_RED + "Please enter an integer." + ANSI_RESET);
        }
        
    }

    private static void showEvent() {
        simManager.displayNextEvents();

    }

    private static void executeEvent() {
        simManager.executeNextEvent();

    }

    private static void vehicleStatus() {
        System.out.println(ANSI_CYAN + "\nVehicle Status" + ANSI_RESET);

        if (vList.isEmpty()) {
            System.out.println(ANSI_RED + "There are no vehicles to display the status of." + ANSI_RESET);
            return;

        }
        System.out.print("Enter vehicle ID: ");
        String vehicleId = scanner.nextLine().trim();
        Vehicle vehicle = findVehicleId(vehicleId);
        if (vehicle == null) {
            System.out.println(ANSI_RED + "Vehicle not found: " + vehicleId + ANSI_RESET);
            return;

        }
        System.out.println(simManager.getVehicleStatus(vehicle));
    }

    private static void startVehicleMovement() {
        System.out.print("Enter vehicle ID: ");
        String vehicleId = scanner.nextLine().trim();
        Vehicle vehicle = findVehicleId(vehicleId);
        if (vehicle == null) {
            System.out.println(ANSI_RED + "Vehicle not found: " + vehicleId + ANSI_RESET);
            return;
        }

        if (vehicle.getAssignedRoute() == null) {
            System.out.println(ANSI_RED + "Vehicle is not assigned to a route." + ANSI_RESET);
            return;
        }

        if (vehicle.getLocation() == null) {
            System.out.println(ANSI_RED + "Vehicle location is not set." + ANSI_RESET);
            return;
        }

        
        if (vehicle.getAssignedRoute().getLocations().size() <= 1) {
            System.out.println(ANSI_RED + "Route must have at least 2 locations to schedule movement!" + ANSI_RESET);
            return;

        }

        if (vehicle.inTransit()) {
            System.out.println(ANSI_RED + "The vehicle is already in transit!" + ANSI_RESET);
            return;

        }

        if (simManager.hasScheduledEvent(vehicle)) {
            System.out.println(ANSI_RED + "The vehicle already has a scheduled event!" + ANSI_RESET);
            return;

        }

        int departureTime = simManager.getCurrentTime() + 1;
        Event event = new Event(departureTime, vehicle, vehicle.getLocation(), Event.EventType.DEPARTURE);
        simManager.addEvent(event);
        
        System.out.println("Vehicle " + vehicle.getName() + " scheduled to start route at " + departureTime);

    }

}

