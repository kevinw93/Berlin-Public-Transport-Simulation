public class Location implements Comparable<Location> {
    private String locationId;  
    private String name;
    private double xCoord;
    private double yCoord;

    public Location(String locationId, String name, double x, double y) {
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
        String format = "ID: %s | Name: %s | Coordinate: (%.2f, %.2f)";
        return String.format(format, this.locationId, this.name, this.xCoord, this.yCoord);
    }

    @Override
    public int compareTo(Location otherLocation) {
        return this.locationId.compareTo(otherLocation.getLocationId());
    }

}
