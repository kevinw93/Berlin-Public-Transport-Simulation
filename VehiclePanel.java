import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

public class VehiclePanel extends JPanel {
    private final JTextField idField = new JTextField(10);
    private final JTextField nameField = new JTextField(10);
    private final JComboBox<String> typeBox = new JComboBox<>(new String[]{"S_Bahn", "U_Bahn", "Tram", "Bus"});
    private final JTextField capacityField = new JTextField(5);
    private final JTextField speedField = new JTextField(5);
    private final JButton addButton = new JButton("Add Vehicle");
    private final JLabel messageLabel = new JLabel(" ");
    private final DefaultListModel<String> vehicleListModel = new DefaultListModel<>();
    private final JList<String> vehicleList = new JList<>(vehicleListModel);
    private final JButton removeButton = new JButton("Remove Vehicle");

    // In-memory vehicle storage for now
    private final TransitDataModel model;
    private final VehicleController vehicleController;
    private SimulationPanel simulationPanel; // Reference to simulation panel

    public VehiclePanel(TransitDataModel model, VehicleController vehicleController) {
        this.model = model;
        this.vehicleController = vehicleController;
        setLayout(new BorderLayout());

        // Form panel
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; form.add(idField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; form.add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; form.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1; form.add(typeBox, gbc);
        gbc.gridx = 0; gbc.gridy = 3; form.add(new JLabel("Capacity:"), gbc);
        gbc.gridx = 1; form.add(capacityField, gbc);
        gbc.gridx = 0; gbc.gridy = 4; form.add(new JLabel("Speed (km/h):"), gbc);
        gbc.gridx = 1; form.add(speedField, gbc);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; form.add(addButton, gbc);
        gbc.gridy = 6; form.add(messageLabel, gbc);
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; form.add(removeButton, gbc);

        add(form, BorderLayout.NORTH);

        // Vehicle list with remove button below
        JPanel listPanel = new JPanel(new BorderLayout());
        vehicleList.setVisibleRowCount(10);
        listPanel.add(new JScrollPane(vehicleList), BorderLayout.CENTER);
        listPanel.add(removeButton, BorderLayout.SOUTH);
        add(listPanel, BorderLayout.CENTER);

        // Add button logic
        addButton.addActionListener(this::handleAddVehicle);
        removeButton.addActionListener(e -> handleRemoveVehicle());
        
        // Refresh vehicle list when panel is shown
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                refreshVehicleList();
            }
        });
    }
    
    // Method to set simulation panel reference
    public void setSimulationPanel(SimulationPanel simulationPanel) {
        this.simulationPanel = simulationPanel;
    }
    
    // Method to refresh vehicle list
    private void refreshVehicleList() {
        vehicleListModel.clear();
        for (Vehicle v : model.vehicles) {
            vehicleListModel.addElement(v.toString());
        }
    }
    
    // Public method to refresh vehicle list from other panels
    public void refreshVehicleDisplay() {
        refreshVehicleList();
    }

    private void handleAddVehicle(ActionEvent e) {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String typeStr = (String) typeBox.getSelectedItem();
        String capacityStr = capacityField.getText().trim();
        String speedStr = speedField.getText().trim();

        // Validation
        if (id.isEmpty() || name.isEmpty() || capacityStr.isEmpty() || speedStr.isEmpty()) {
            setMessage("All fields are required.", Color.RED); return;
        }
        if (id.length() > 99) {
            setMessage("Vehicle ID must be less than 100 characters.", Color.RED); return;
        }
        if (!id.matches("[a-zA-Z0-9]+")) {
            setMessage("Vehicle ID must be alphanumeric.", Color.RED); return;
        }
        if (model.allIds.contains(id)) {
            setMessage("ID already exists globally.", Color.RED); return;
        }
        int capacity;
        double speed;
        try {
            capacity = Integer.parseInt(capacityStr);
            if (capacity <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            setMessage("Capacity must be a positive integer.", Color.RED); return;
        }
        try {
            speed = Double.parseDouble(speedStr);
            if (speed <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            setMessage("Speed must be a positive number.", Color.RED); return;
        }

        VehicleType type;
        switch (typeStr) {
            case "S_Bahn": type = new SbahnType(); break;
            case "U_Bahn": type = new UbahnType(); break;
            case "Tram": type = new TramType(); break;
            case "Bus": type = new BusType(); break;
            default: setMessage("Invalid vehicle type.", Color.RED); return;
        }

        Vehicle v = new Vehicle(id, name, type, capacity, speed);
        model.vehicles.add(v);
        model.allIds.add(id);
        vehicleListModel.addElement(v.toString());
        setMessage("Vehicle added successfully!", new Color(0, 128, 0));
        idField.setText(""); nameField.setText(""); capacityField.setText(""); speedField.setText("");
        
        // Refresh simulation panel if available
        if (simulationPanel != null) {
            simulationPanel.refreshVehicleComboBox();
        }
    }

    private void handleRemoveVehicle() {
        int idx = vehicleList.getSelectedIndex();
        if (idx < 0) {
            setMessage("Please select a vehicle to remove.", Color.RED); return;
        }
        Vehicle v = model.vehicles.get(idx);
        if (v.getAssignedRoute() != null) {
            setMessage("Cannot remove vehicle: it is assigned to a route.", Color.RED); return;
        }
        model.vehicles.remove(v);
        model.allIds.remove(v.getVehicleId());
        vehicleListModel.remove(idx);
        setMessage("Vehicle removed!", new Color(0, 128, 0));
        
        // Refresh simulation panel if available
        if (simulationPanel != null) {
            simulationPanel.refreshVehicleComboBox();
        }
    }

    private void setMessage(String msg, Color color) {
        messageLabel.setText(msg);
        messageLabel.setForeground(color);
    }
} 