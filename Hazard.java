public class Hazard implements Comparable<Hazard> {

    //hazard fields
    private String hazardId;
    private String name;
    private HazardType hazardType;
    private double impactValue;
    private Location location;
    private Location locationA;
    private Location locationB;
    private boolean isPair;
    private boolean isActive;

    //hazard constructor
    public Hazard(String hazardId, String name, HazardType hazardType, double impactValue) {
        if (hazardId == null || hazardId.trim().isEmpty()) {
            throw new IllegalArgumentException("Hazard ID cannot be null or empty.");
        }
        if (hazardType == null) {
            throw new IllegalArgumentException("Hazard type cannot be null.");
        }
        if (impactValue <= 0) {
            throw new IllegalArgumentException("Impact value must be positive.");
        }

        this.hazardId = hazardId.trim();
        this.name = name;
        this.hazardType = hazardType;
        this.impactValue = impactValue;
        this.isActive = true;
        this.isPair = false;
        this.location = null;
        this.locationA = null;
        this.locationB = null;

    }

    //getters and setters
    public String getHazardId() {
        return hazardId;
    }

    public String getName() {
        return name;
    }

    public HazardType getHazardType() {
        return hazardType;
    }

    public double getImpactValue() {
        return impactValue;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isPair() {
        return isPair;
    }

    public Location getLocation() {
        return location;
    }

    public Location getLocationA() {
        return locationA;
    }

    public Location getLocationB() {
        return locationB;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHazardType(HazardType hazardType) {
        if (hazardType == null) {
            throw new IllegalArgumentException("Hazard type cannot be null.");
        }

        this.hazardType = hazardType;
    }

    public void setImpactValue(double impactValue) {
        //delays must have a positive value and be at least 1 so delays aren't shortened
        if (impactValue <= 1) {
            throw new IllegalArgumentException("Impact value must be positive.");
        }

        this.impactValue = impactValue;
    }

    // sets the hazard to affect a single location and clears pair locations
    public void setLocation(Location location) {
        this.location = location;
        this.isPair = false;
        this.locationA = null;
        this.locationB = null;
    }

    // sets the hazard to affect travel between two locations and clears single location
    public void setLocations(Location locationA, Location locationB) {
        this.locationA = locationA;
        this.locationB = locationB;
        this.isPair = true;
        this.location = null;
    }

    // deactivates the hazard without removing it from the system
    public void removeHazard() {
        this.isActive = false;

    }
    

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Hazard hazard = (Hazard) obj;
        return hazardId.equals(hazard.hazardId);
    }

    @Override
    public int compareTo(Hazard otherHazard) {
        return this.hazardId.compareTo(otherHazard.getHazardId());
    }

    //used for displaying hazards in main class
    @Override
    public String toString() {
        
        String target;
        if (isPair) {
            target = String.format("Between %s and %s", 
                locationA != null ? locationA.getName() : "Unknown",
                locationB != null ? locationB.getName() : "Unknown");
        } else if (location != null) {
            target = String.format("At %s", location.getName());
        } else {
            target = "No location specified";
        }
        String impact = hazardType.getImpactString(impactValue);
        return String.format(
            "Hazard ID: %s | Name: %s | Type: %s | Impact: %s | Location(s): %s | Active: %b",
            hazardId, name, hazardType.getTypeName(), impact, target, isActive
        );
    }
}
