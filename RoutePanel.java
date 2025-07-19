import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

public class RoutePanel extends JPanel {
    private final JTextField idField = new JTextField(10);
    private final JTextField nameField = new JTextField(10);
    private final JButton addButton = new JButton("Add Route");
    private final JLabel messageLabel = new JLabel(" ");
    private final DefaultListModel<String> routeListModel = new DefaultListModel<>();
    private final JList<String> routeList = new JList<>(routeListModel);

    private JComboBox<String> routeComboBox;
    private JComboBox<String> locationComboBox;
    private JButton addLocationToRouteButton;
    private JComboBox<String> assignRouteComboBox;
    private JComboBox<String> assignVehicleComboBox;
    private JComboBox<String> assignPositionComboBox;
    private JComboBox<String> assignDirectionComboBox;
    private JButton assignVehicleButton;
    private JComboBox<String> removeRouteComboBox;
    private JButton removeRouteButton;
    private JComboBox<String> removeLocationRouteComboBox;
    private JComboBox<String> removeLocationComboBox;
    private JButton removeLocationButton;
    


    private final TransitDataModel model;
    private final RouteController routeController;
    private VehiclePanel vehiclePanel; // Reference to vehicle panel

    public RoutePanel(TransitDataModel model, RouteController routeController) {
        this.model = model;
        this.routeController = routeController;
        setLayout(new BorderLayout());

        // --- Add Route Form ---
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; form.add(idField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; form.add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; form.add(addButton, gbc);
        gbc.gridy = 3; form.add(messageLabel, gbc);
        add(form, BorderLayout.NORTH);

        // --- Route List ---
        routeList.setVisibleRowCount(10);
        add(new JScrollPane(routeList), BorderLayout.CENTER);
        addButton.addActionListener(this::handleAddRoute);

        // --- Add Location to Route ---
        JPanel addLocPanel = new JPanel();
        addLocPanel.setBorder(BorderFactory.createTitledBorder("Add Location to Route"));
        routeComboBox = new JComboBox<>();
        locationComboBox = new JComboBox<>();
        addLocationToRouteButton = new JButton("Add Location");
        addLocPanel.add(new JLabel("Route:"));
        addLocPanel.add(routeComboBox);
        addLocPanel.add(new JLabel("Location:"));
        addLocPanel.add(locationComboBox);
        addLocPanel.add(addLocationToRouteButton);
        addLocationToRouteButton.addActionListener(this::handleAddLocationToRoute);
        routeComboBox.addActionListener(e -> updatePositionComboBox());

        // --- Assign Vehicle to Route ---
        JPanel assignPanel = new JPanel(new GridBagLayout());
        assignPanel.setBorder(BorderFactory.createTitledBorder("Assign Vehicle to Route"));

        GridBagConstraints gbcAssign = new GridBagConstraints();
        gbcAssign.insets = new Insets(2, 4, 2, 4); // Padding around components
        gbcAssign.gridy = 0;

        // Add Route label + combo
        gbcAssign.gridx = 0; assignPanel.add(new JLabel("Route:"), gbcAssign);
        gbcAssign.gridx = 1; assignRouteComboBox = new JComboBox<>(); assignPanel.add(assignRouteComboBox, gbcAssign);

        // Add Vehicle label + combo
        gbcAssign.gridx = 2; assignPanel.add(new JLabel("Vehicle:"), gbcAssign);
        gbcAssign.gridx = 3; assignVehicleComboBox = new JComboBox<>(); assignPanel.add(assignVehicleComboBox, gbcAssign);

        // Add Position label + combo
        gbcAssign.gridx = 4; assignPanel.add(new JLabel("Position:"), gbcAssign);
        gbcAssign.gridx = 5; assignPositionComboBox = new JComboBox<>(); assignPanel.add(assignPositionComboBox, gbcAssign);

        // Add Direction label + combo
        gbcAssign.gridx = 6; assignPanel.add(new JLabel("Direction:"), gbcAssign);
        gbcAssign.gridx = 7; assignDirectionComboBox = new JComboBox<>(new String[]{"Right", "Left"}); 
        assignPanel.add(assignDirectionComboBox, gbcAssign);

        // Add Button
        gbcAssign.gridx = 8; assignVehicleButton = new JButton("Assign Vehicle"); assignPanel.add(assignVehicleButton, gbcAssign);

        // Button listener
        assignVehicleButton.addActionListener(this::handleAssignVehicleToRoute);
        assignRouteComboBox.addActionListener(e -> updatePositionComboBox());

        // --- Remove Location from Route ---
        JPanel removeLocPanel = new JPanel();
        removeLocPanel.setBorder(BorderFactory.createTitledBorder("Remove Location from Route"));
        removeLocationRouteComboBox = new JComboBox<>();
        removeLocationComboBox = new JComboBox<>();
        removeLocationButton = new JButton("Remove Location");
        removeLocPanel.add(new JLabel("Route:"));
        removeLocPanel.add(removeLocationRouteComboBox);
        removeLocPanel.add(new JLabel("Location:"));
        removeLocPanel.add(removeLocationComboBox);
        removeLocPanel.add(removeLocationButton);
        removeLocationButton.addActionListener(this::handleRemoveLocationFromRoute);
        removeLocationRouteComboBox.addActionListener(e -> updateRemoveLocationComboBox());

        // --- Remove Route ---
        JPanel removeRoutePanel = new JPanel();
        removeRoutePanel.setBorder(BorderFactory.createTitledBorder("Remove Route"));
        removeRouteComboBox = new JComboBox<>();
        removeRouteButton = new JButton("Remove Route");
        removeRoutePanel.add(new JLabel("Route:"));
        removeRoutePanel.add(removeRouteComboBox);
        removeRoutePanel.add(removeRouteButton);
        removeRouteButton.addActionListener(this::handleRemoveRoute);



        // --- Bottom Panel Layout ---
        JPanel actionPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        actionPanel.add(addLocPanel);
        actionPanel.add(assignPanel);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(actionPanel, BorderLayout.NORTH);

        JPanel removePanelWrapper = new JPanel(new GridLayout(2, 1, 5, 5));
        removePanelWrapper.add(removeLocPanel);
        removePanelWrapper.add(removeRoutePanel);
        bottomPanel.add(removePanelWrapper, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        // Refresh on show
        refreshComboBoxes();
        // Refresh vehicle list when panel is shown
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                refreshComboBoxes();
            }
        });
    }
    
    // Method to set vehicle panel reference
    public void setVehiclePanel(VehiclePanel vehiclePanel) {
        this.vehiclePanel = vehiclePanel;
    }

    // --- Combo Box Refresh Logic ---
    private void refreshComboBoxes() {
        if (routeComboBox != null) routeComboBox.removeAllItems();
        if (assignRouteComboBox != null) assignRouteComboBox.removeAllItems();
        if (removeRouteComboBox != null) removeRouteComboBox.removeAllItems();
        if (removeLocationRouteComboBox != null) removeLocationRouteComboBox.removeAllItems();


        for (Route r : model.routes) {
            String label = r.getRouteId() + " - " + r.getName();
            if (routeComboBox != null) routeComboBox.addItem(label);
            if (assignRouteComboBox != null) assignRouteComboBox.addItem(label);
            if (removeRouteComboBox != null) removeRouteComboBox.addItem(label);
            if (removeLocationRouteComboBox != null) removeLocationRouteComboBox.addItem(label);

        }

        if (locationComboBox != null) {
            locationComboBox.removeAllItems();
            for (Location l : model.locations) {
                locationComboBox.addItem(l.getLocationId() + " - " + l.getName());
            }
        }

        if (assignVehicleComboBox != null) {
            assignVehicleComboBox.removeAllItems();
            for (Vehicle v : model.vehicles) {
                if (v.getAssignedRoute() == null) {
                    assignVehicleComboBox.addItem(v.getVehicleId() + " - " + v.getName());
                }
            }
        }

        updatePositionComboBox();
        updateRemoveLocationComboBox();
    }
    


    private void updatePositionComboBox() {
        if (assignPositionComboBox != null) {
            assignPositionComboBox.removeAllItems();
            if (assignRouteComboBox != null && assignRouteComboBox.getItemCount() > 0) {
                int idx = assignRouteComboBox.getSelectedIndex();
                if (idx >= 0 && idx < model.routes.size()) {
                    Route r = model.routes.get(idx);
                    for (int i = 0; i < r.getLocationCount(); i++) {
                        assignPositionComboBox.addItem(String.valueOf(i));
                    }
                }
            }
        }
    }

    private void updateRemoveLocationComboBox() {
        if (removeLocationComboBox != null) {
            removeLocationComboBox.removeAllItems();
            if (removeLocationRouteComboBox != null && removeLocationRouteComboBox.getItemCount() > 0) {
                int idx = removeLocationRouteComboBox.getSelectedIndex();
                if (idx >= 0 && idx < model.routes.size()) {
                    Route r = model.routes.get(idx);
                    for (Location l : r.getLocations()) {
                        removeLocationComboBox.addItem(l.getLocationId() + " - " + l.getName());
                    }
                }
            }
        }
    }



    // --- Button Handlers ---
    private void handleAddRoute(ActionEvent e) {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String msg = routeController.addRoute(id, name);
        if (msg.startsWith("Route added")) {
            Route r = routeController.findRouteById(id);
            routeListModel.addElement(formatRouteDisplay(r));
            idField.setText("");
            nameField.setText("");
            setMessage(msg, new Color(0, 128, 0));
            refreshComboBoxes();
        } else {
            setMessage(msg, Color.RED);
        }
    }

    private void handleAddLocationToRoute(ActionEvent e) {
        int routeIdx = routeComboBox.getSelectedIndex();
        int locIdx = locationComboBox.getSelectedIndex();
        if (routeIdx < 0 || locIdx < 0) {
            setMessage("Please select both a route and a location.", Color.RED);
            return;
        }
        Route r = model.routes.get(routeIdx);
        Location l = model.locations.get(locIdx);
        if (r.getLocations().contains(l)) {
            setMessage("Location already on route.", Color.RED);
            return;
        }
        r.addLocation(l, r.getLocationCount());
        setMessage("Location added to route!", new Color(0, 128, 0));
        refreshComboBoxes();
        updateRouteListDisplay();
    }

    private void handleAssignVehicleToRoute(ActionEvent e) {
        int routeIdx = assignRouteComboBox.getSelectedIndex();
        int vehicleIdx = assignVehicleComboBox.getSelectedIndex();
        int positionIdx = assignPositionComboBox.getSelectedIndex();
        String direction = (String) assignDirectionComboBox.getSelectedItem();
        
        if (routeIdx < 0 || vehicleIdx < 0 || positionIdx < 0) {
            setMessage("Please select a route, vehicle, and position.", Color.RED);
            return;
        }
        
        Route r = model.routes.get(routeIdx);
        
        // Get the selected vehicle string from the combo box
        String selectedVehicle = (String) assignVehicleComboBox.getSelectedItem();
        String vehicleId = selectedVehicle.split(" - ")[0].trim();
        
        // Find the actual vehicle object by ID
        Vehicle v = null;
        for (Vehicle vehicle : model.vehicles) {
            if (vehicle.getVehicleId().equals(vehicleId)) {
                v = vehicle;
                break;
            }
        }
        
        if (v == null) {
            setMessage("Vehicle not found.", Color.RED);
            return;
        }
        
        if (v.getAssignedRoute() != null) {
            setMessage("Vehicle is already assigned to a route.", Color.RED);
            return;
        }
        
        Location l = r.getLocations().get(positionIdx);
        v.setLocation(l);
        v.setAssignedRoute(r);
        v.setRoutePosition(positionIdx);
        v.setGoingRight(direction.equals("Right"));
        r.addVehicle(v);
        
        setMessage("Vehicle assigned to route!", new Color(0, 128, 0));
        refreshComboBoxes();
        updateRouteListDisplay(); // Refresh the route list to show updated vehicle information
        
        // Refresh vehicle display if vehicle panel is available
        if (vehiclePanel != null) {
            vehiclePanel.refreshVehicleDisplay();
        }
    }

    private void handleRemoveLocationFromRoute(ActionEvent e) {
        int routeIdx = removeLocationRouteComboBox.getSelectedIndex();
        int locIdx = removeLocationComboBox.getSelectedIndex();
        if (routeIdx < 0 || locIdx < 0) {
            setMessage("Please select a route and a location.", Color.RED);
            return;
        }
        Route r = model.routes.get(routeIdx);
        Location l = r.getLocations().get(locIdx);
        for (Vehicle v : model.vehicles) {
            if (v.getAssignedRoute() == r && v.getLocation() == l) {
                v.removeFromRoute();
                r.removeVehicle(v);
            }
        }
        r.removeLocationAt(locIdx);
        setMessage("Location removed from route!", new Color(0, 128, 0));
        refreshComboBoxes();
        updateRouteListDisplay();
    }

    private void handleRemoveRoute(ActionEvent e) {
        int routeIdx = removeRouteComboBox.getSelectedIndex();
        if (routeIdx < 0) {
            setMessage("Please select a route to remove.", Color.RED);
            return;
        }
        String selectedRoute = (String) removeRouteComboBox.getSelectedItem();
        String id = selectedRoute.split(" - ")[0].trim();
        String msg = routeController.removeRoute(id);
        if (msg.startsWith("Route removed")) {
            routeListModel.remove(routeIdx);
            setMessage(msg, new Color(0, 128, 0));
            refreshComboBoxes();
        } else {
            setMessage(msg, Color.RED);
        }
    }





    private void setMessage(String msg, Color color) {
        messageLabel.setText(msg);
        messageLabel.setForeground(color);
    }

    private String formatRouteDisplay(Route r) {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(r.getRouteId()).append(" | Name: ").append(r.getName()).append(" | Locations: ");
        for (Location l : r.getLocations()) {
            sb.append(l.getName()).append(", ");
        }
        if (sb.length() > 0 && r.getLocations().size() > 0) {
            sb.delete(sb.length() - 2, sb.length()); // Remove the last comma and space
        }
        
        // Add vehicle information
        List<Vehicle> routeVehicles = r.getVehicles();
        if (!routeVehicles.isEmpty()) {
            sb.append(" | Vehicles: ").append(routeVehicles.size());
            if (routeVehicles.size() <= 3) {
                // Show vehicle names if 3 or fewer
                String[] vehicleNames = routeVehicles.stream()
                    .map(Vehicle::getName)
                    .toArray(String[]::new);
                sb.append(" (").append(String.join(", ", vehicleNames)).append(")");
            }
        } else {
            sb.append(" | Vehicles: None");
        }
        
        return sb.toString();
    }

    private void updateRouteListDisplay() {
        routeListModel.clear();
        for (Route r : model.routes) {
            routeListModel.addElement(formatRouteDisplay(r));
        }
    }

    
    public void refreshRouteDisplay() {
        updateRouteListDisplay();
    }
    
} 