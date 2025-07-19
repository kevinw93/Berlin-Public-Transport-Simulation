import java.awt.*;
import javax.swing.*;

public class TransitSimulatorGUI extends JFrame {
    public TransitSimulatorGUI() {
        setTitle("Transit Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        TransitDataModel model = new TransitDataModel();
        VehicleService vehicleService = new VehicleService(model);
        VehicleController vehicleController = new VehicleController(vehicleService);
        VehiclePanel vehiclePanel = new VehiclePanel(model, vehicleController);
        vehiclePanel.setBackground(new Color(220, 235, 255)); // Light blue

        LocationService locationService = new LocationService(model);
        LocationController locationController = new LocationController(locationService);
        LocationPanel locationPanel = new LocationPanel(model, locationController);
        locationPanel.setBackground(new Color(220, 255, 220)); // Light green

        RouteService routeService = new RouteService(model);
        RouteController routeController = new RouteController(routeService);
        RoutePanel routePanel = new RoutePanel(model, routeController);
        routePanel.setBackground(new Color(255, 245, 220)); // Light orange

        HazardService hazardService = new HazardService(model);
        HazardController hazardController = new HazardController(hazardService);
        HazardPanel hazardPanel = new HazardPanel(model, hazardController);
        hazardPanel.setBackground(new Color(255, 220, 235)); // Light pink

        SimulationService simulationService = new SimulationService(model);
        SimulationController simulationController = new SimulationController(simulationService);
        SimulationPanel simulationPanel = new SimulationPanel(model, simulationController);
        simulationPanel.setBackground(new Color(240, 240, 255)); // Light purple

        // Connect vehicle panel with simulation panel for refresh
        vehiclePanel.setSimulationPanel(simulationPanel);
        
        // Connect route panel with vehicle panel for cross-refresh
        routePanel.setVehiclePanel(vehiclePanel);
        
        // Connect panels for cross-panel communication
        // Note: In a more sophisticated implementation, you might use an observer pattern
        // For now, we'll rely on the component listeners to refresh when panels are shown

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Vehicles", vehiclePanel);
        tabbedPane.addTab("Locations", locationPanel);
        tabbedPane.addTab("Routes", routePanel);
        tabbedPane.addTab("Hazards", hazardPanel);
        tabbedPane.addTab("Simulation", simulationPanel);


        tabbedPane.addChangeListener(e -> {
            Component selected = tabbedPane.getSelectedComponent();
            if (selected instanceof VehiclePanel) {
                vehiclePanel.refreshVehicleDisplay();
            }
            if (selected instanceof RoutePanel) {
                routePanel.refreshRouteDisplay();
            }
            if (selected instanceof LocationPanel) {
                locationPanel.refreshLocationDisplay();
            }
        });
        
        add(tabbedPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TransitSimulatorGUI().setVisible(true);
        });
    }
} 