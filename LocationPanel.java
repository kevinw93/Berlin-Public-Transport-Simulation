import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class LocationPanel extends JPanel {
    private final JTextField idField = new JTextField(10);
    private final JTextField nameField = new JTextField(10);
    private final JTextField xField = new JTextField(5);
    private final JTextField yField = new JTextField(5);
    private final JTextField waitingField = new JTextField(5);
    private final JButton addButton = new JButton("Add Location");
    private final JLabel messageLabel = new JLabel(" ");
    private final DefaultListModel<String> locationListModel = new DefaultListModel<>();
    private final JList<String> locationList = new JList<>(locationListModel);
    private final JButton removeButton = new JButton("Remove Location");
    private final JTextField arrLowField = new JTextField("1", 3);
    private final JTextField arrHighField = new JTextField("3", 3);
    private final JTextField transLowField = new JTextField("0", 3);
    private final JTextField transHighField = new JTextField("2", 3);
    private final JTextField depLowField = new JTextField("1", 3);
    private final JTextField depHighField = new JTextField("3", 3);

    // In-memory location storage for now
    private final TransitDataModel model;
    private final LocationController locationController;

    public LocationPanel(TransitDataModel model, LocationController locationController) {
        this.model = model;
        this.locationController = locationController;
        setLayout(new BorderLayout());

        // Form panel
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; form.add(idField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; form.add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; form.add(new JLabel("X Coord:"), gbc);
        gbc.gridx = 1; form.add(xField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; form.add(new JLabel("Y Coord:"), gbc);
        gbc.gridx = 1; form.add(yField, gbc);
        gbc.gridx = 0; gbc.gridy = 4; form.add(new JLabel("Waiting Passengers:"), gbc);
        gbc.gridx = 1; form.add(waitingField, gbc);
        gbc.gridx = 0; gbc.gridy = 5; form.add(new JLabel("Arrive Low:"), gbc);
        gbc.gridx = 1; form.add(arrLowField, gbc);
        gbc.gridx = 0; gbc.gridy = 6; form.add(new JLabel("Arrive High:"), gbc);
        gbc.gridx = 1; form.add(arrHighField, gbc);
        gbc.gridx = 0; gbc.gridy = 7; form.add(new JLabel("Trans Low:"), gbc);
        gbc.gridx = 1; form.add(transLowField, gbc);
        gbc.gridx = 0; gbc.gridy = 8; form.add(new JLabel("Trans High:"), gbc);
        gbc.gridx = 1; form.add(transHighField, gbc);
        gbc.gridx = 0; gbc.gridy = 9; form.add(new JLabel("Dep Low:"), gbc);
        gbc.gridx = 1; form.add(depLowField, gbc);
        gbc.gridx = 0; gbc.gridy = 10; form.add(new JLabel("Dep High:"), gbc);
        gbc.gridx = 1; form.add(depHighField, gbc);
        gbc.gridx = 0; gbc.gridy = 11; gbc.gridwidth = 2; form.add(addButton, gbc);
        gbc.gridy = 12; form.add(messageLabel, gbc);
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; form.add(removeButton, gbc);

        add(form, BorderLayout.NORTH);

        // Location list with remove button below
        JPanel listPanel = new JPanel(new BorderLayout());
        locationList.setVisibleRowCount(10);
        listPanel.add(new JScrollPane(locationList), BorderLayout.CENTER);
        listPanel.add(removeButton, BorderLayout.SOUTH);
        add(listPanel, BorderLayout.CENTER);

        // Add button logic
        addButton.addActionListener(this::handleAddLocation);
        removeButton.addActionListener(e -> handleRemoveLocation());
    }

    private void handleAddLocation(ActionEvent e) {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String xStr = xField.getText().trim();
        String yStr = yField.getText().trim();
        String waitingStr = waitingField.getText().trim();
        String arrLow = arrLowField.getText().trim();
        String arrHigh = arrHighField.getText().trim();
        String transLow = transLowField.getText().trim();
        String transHigh = transHighField.getText().trim();
        String depLow = depLowField.getText().trim();
        String depHigh = depHighField.getText().trim();
        String msg = locationController.addLocation(id, name, xStr, yStr, waitingStr, arrLow, arrHigh, transLow, transHigh, depLow, depHigh);
        if (msg.startsWith("Location added")) {
            locationListModel.addElement(locationController.findLocationById(id).toString());
            idField.setText(""); nameField.setText(""); xField.setText(""); yField.setText(""); waitingField.setText("");
            arrLowField.setText("1"); arrHighField.setText("3"); transLowField.setText("0"); transHighField.setText("2"); depLowField.setText("1"); depHighField.setText("3");
            setMessage(msg, new Color(0, 128, 0));
        } else {
            setMessage(msg, Color.RED);
        }
    }

    private void handleRemoveLocation() {
        int idx = locationList.getSelectedIndex();
        if (idx < 0) {
            setMessage("Please select a location to remove.", Color.RED); return;
        }
        String id = locationListModel.get(idx).split(" \\|")[0].split(": ")[1].trim();
        String msg = locationController.removeLocation(id);
        if (msg.startsWith("Location removed")) {
            locationListModel.remove(idx);
            setMessage(msg, new Color(0, 128, 0));
        } else {
            setMessage(msg, Color.RED);
        }
    }
    
    private void refreshLocationList() {
        locationListModel.clear();
        for (Location l : model.locations) {
            locationListModel.addElement(l.toString());
        }
    }

    public void refreshLocationDisplay() {
        refreshLocationList();
    }


    private void setMessage(String msg, Color color) {
        messageLabel.setText(msg);
        messageLabel.setForeground(color);
    }
} 