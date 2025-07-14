import java.util.List;

public class LocationService {
    public final TransitDataModel model;
    public LocationService(TransitDataModel model) {
        this.model = model;
    }
    public boolean addLocation(String locationID, String name, double xCoord, double yCoord, int waitingPassengers, int arrLow, int arrHigh, int transLow, int transHigh, int depLow, int depHigh) {
        if (locationID.length() > 99) {
            return false;
        }
        if (!locationID.matches("[a-zA-Z0-9]+")) {
            return false;
        }
        if (findLocationById(locationID) != null) {
            return false;
        }
        Location l = new Location(locationID, name, xCoord, yCoord, waitingPassengers);
        l.setDebarkRange(arrLow, arrHigh);
        l.setTransferRange(transLow, transHigh);
        l.setEmbarkRange(depLow, depHigh);
        model.locations.add(l);
        return true;
    }
    public List<Location> getLocations() {
        return model.locations;
    }
    public Location findLocationById(String id) {
        for (Location l : model.locations) {
            if (l.getLocationId().equals(id)) {
                return l;
            }
        }
        return null;
    }
    public boolean removeLocation(String locationID) {
        Location l = findLocationById(locationID);
        if (l == null) {
            return false;
        }
        model.locations.remove(l);
        return true;
    }

    public boolean setWaitingPassengers(String locationId, int count) {
        Location l = findLocationById(locationId);
        if (l == null) {
            return false;
        }
        l.setWaitingPassengers(count);
        return true;
    }

    public boolean setPassengerExchangeRanges(String locationId, int debarkMin, int debarkMax, int transferMin, int transferMax, int embarkMin, int embarkMax) {
        Location l = findLocationById(locationId);
        if (l == null) {
            return false;
        }
        l.setDebarkRange(debarkMin, debarkMax);
        l.setTransferRange(transferMin, transferMax);
        l.setEmbarkRange(embarkMin, embarkMax);
        return true;
    }

    public void clear() {
        model.locations.clear();
    }
}

