import java.util.List;

public class HazardService {
    public final TransitDataModel model;
    public HazardService(TransitDataModel model) {
        this.model = model;
    }
    
    public boolean addHazard(Hazard hazard) {
        if (hazard == null) {
            return false;
        }
        
        // Check for existing hazard at the same location(s) and replace it
        Hazard existingHazard = findExistingHazard(hazard);
        if (existingHazard != null) {
            // Remove the existing hazard
            model.hazards.remove(existingHazard);
            model.allIds.remove(existingHazard.getHazardId());
            
            // Clear the hazard from the location(s)
            if (existingHazard.isPair()) {
                // Clear from both locations
                if (existingHazard.getLocationA() != null) {
                    existingHazard.getLocationA().setHazard(null);
                }
                if (existingHazard.getLocationB() != null) {
                    existingHazard.getLocationB().setHazard(null);
                }
            } else {
                // Clear from single location
                if (existingHazard.getLocation() != null) {
                    existingHazard.getLocation().setHazard(null);
                }
            }
        }
        
        model.hazards.add(hazard);
        // Update the Event hazard list
        Event.setHazardList(model.hazards);
        return true;
    }
    
    // finds any existing hazard that overlaps with the new hazard's location(s)
    public Hazard findExistingHazard(Hazard newHazard) {
        for (Hazard existingHazard : model.hazards) {
            if (hazardsOverlap(existingHazard, newHazard)) {
                return existingHazard;
            }
        }
        return null;
    }
    
    // checks if two hazards overlap in their locations
    private boolean hazardsOverlap(Hazard hazard1, Hazard hazard2) {
        // Check if both hazards are at the same location(s)
        if (hazard1.isPair() && hazard2.isPair()) {
            // Both are between locations - check if they cover the same pair
            return (hazard1.getLocationA().equals(hazard2.getLocationA()) && 
                    hazard1.getLocationB().equals(hazard2.getLocationB())) ||
                   (hazard1.getLocationA().equals(hazard2.getLocationB()) && 
                    hazard1.getLocationB().equals(hazard2.getLocationA()));
        } else if (!hazard1.isPair() && !hazard2.isPair()) {
            // Both are at single locations - check if same location
            return hazard1.getLocation().equals(hazard2.getLocation());
        }
        // One is pair, one is single - no overlap
        return false;
    }
    
    public List<Hazard> getHazards() {
        return model.hazards;
    }
    
    public Hazard findHazardById(String id) {
        for (Hazard h : model.hazards) {
            if (h.getHazardId().equals(id)) {
                return h;
            }
        }
        return null;
    }
    
    public boolean removeHazard(String hazardId) {
        Hazard h = findHazardById(hazardId);
        if (h == null) {
            return false;
        }
        model.hazards.remove(h);
        // Update the Event hazard list
        Event.setHazardList(model.hazards);
        return true;
    }
}

