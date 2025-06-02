public class Location implements Comparable<Location> {
    private String locationId;  
    private String name;

    public Location(String locationId, String name) {
        if (locationId == null || locationId.trim().isEmpty()) {
            throw new IllegalArgumentException("id can't be null or empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("name can't be null or empty");
        }
        this.locationId = locationId.trim();
        this.name = name.trim();
    }

    public String getLocationId() {
        return locationId;
    }
    public String getName() {
        return name;
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
        return String.format("ID: %s | Name: %s", this.locationId, this.name);
    }

    @Override
    public int compareTo(Location otherLocation) {
        return this.locationId.compareTo(otherLocation.getLocationId());
    }

}
