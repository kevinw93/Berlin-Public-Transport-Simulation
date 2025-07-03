public class Location implements Comparable<Location> {
    private String locationId;  
    private String name;
    private double xCoord;
    private double yCoord;
    private int waitingPassengers = 10;
    private int debarkMin = 1;
    private int debarkMax = 3;
    private int transferMin = 0;
    private int transferMax = 2;
    private int embarkMin = 1;
    private int embarkMax = 3;

    public Location(String locationId, String name, double x, double y, int pass) {
        if (locationId == null || locationId.trim().isEmpty()) {
            throw new IllegalArgumentException("id can't be null or empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("name can't be null or empty");
        }
        this.locationId = locationId.trim();
        this.name = name.trim();
        this.xCoord = x;
        this.yCoord = y;
        this.waitingPassengers = pass;
    }

    public String getLocationId() {
        return locationId;
    }
    public String getName() {
        return name;
    }
    public double getXCoord() {
        return xCoord;
    }
    public double getYCoord() {
        return yCoord;
    }
    public int getWaitingPassengers() {
        return waitingPassengers;
    }

    public void setWaitingPassengers(int passengers) {
        if (passengers < 0) {
            throw new IllegalArgumentException("Waiting passenger count can't be negative");
        }   
        this.waitingPassengers = passengers;
    }

    // calculates the Euclidean distance between this location and another location
    public double distanceTo(Location other) {
        if (other == null) {
            throw new IllegalArgumentException("Input location is null");
        }
        double dx = this.xCoord - other.xCoord;
        double dy = this.yCoord - other.yCoord;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Location location = (Location) obj;
        return locationId.equals(location.locationId);
    }

    @Override
    public String toString() {
        String format = "ID: %s | Name: %s | Coordinate: (%.2f, %.2f)| Waiting Passengers: %d";
        return String.format(format, this.locationId, this.name, this.xCoord, this.yCoord, this.waitingPassengers);
    }

    @Override
    public int compareTo(Location otherLocation) {
        return this.locationId.compareTo(otherLocation.getLocationId());
    }

    public int getDebarkMin() {
        return debarkMin;
    }
    
    public int getDebarkMax() {
        return debarkMax;
    }
    
    public int getTransferMin() {
        return transferMin;
    }
    
    public int getTransferMax() {
        return transferMax;
    }
    
    public int getEmbarkMin() {
        return embarkMin;
    }
    
    public int getEmbarkMax() {
        return embarkMax;
    }

    // sets the range for how many passengers will debark at this location
    public void setDebarkRange(int min, int max) {
        if (min < 0 || max < min) {
            throw new IllegalArgumentException("Invalid debark range");
        }
        this.debarkMin = min;
        this.debarkMax = max;
    }
    
    // sets the range for how many passengers will transfer at this location
    public void setTransferRange(int min, int max) {
        if (min < 0 || max < min) {
            throw new IllegalArgumentException("Invalid transfer range");
        }
        this.transferMin = min;
        this.transferMax = max;
    }
    
    // sets the range for how many passengers will embark at this location
    public void setEmbarkRange(int min, int max) {
        if (min < 0 || max < min) {
            throw new IllegalArgumentException("Invalid embark range");
        }
        this.embarkMin = min;
        this.embarkMax = max;
    }
}
