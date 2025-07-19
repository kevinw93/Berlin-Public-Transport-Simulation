import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

public class HazardPanel extends JPanel {
    private final TransitDataModel model;
    private final JTextField idField = new JTextField(10);
    private final JTextField nameField = new JTextField(10);
    private final JComboBox<String> typeBox = new JComboBox<>(new String[]{"Short-term", "Long-term"});
    private final JTextField impactField = new JTextField(5);
    private final JRadioButton singleStopRadio = new JRadioButton("Single Stop");
    private final JRadioButton betweenStopsRadio = new JRadioButton("Between Two Adjacent Stops");
    private final JComboBox<String> stopComboBox = new JComboBox<>();
    private final JComboBox<String> routeComboBox = new JComboBox<>();
    private final JComboBox<String> stopAComboBox = new JComboBox<>();
    private final JComboBox<String> stopBComboBox = new JComboBox<>();
    private final JButton addButton = new JButton("Add Hazard");
    private final JLabel messageLabel = new JLabel(" ");
    private final DefaultListModel<String> hazardListModel = new DefaultListModel<>();
    private final JList<String> hazardList = new JList<>(hazardListModel);
    private final JTextField removeIdField = new JTextField(10);
    private final JButton removeButton = new JButton("Remove Hazard");
    private final HazardController hazardController;

    // private final List<Hazard> hazards = new ArrayList<>();

    public HazardPanel(TransitDataModel model, HazardController hazardController) {
        this.model = model;
        this.hazardController = hazardController;
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
        gbc.gridx = 0; gbc.gridy = 3; form.add(new JLabel("Impact Value:"), gbc);
        gbc.gridx = 1; form.add(impactField, gbc);
        gbc.gridx = 0; gbc.gridy = 4; form.add(new JLabel("Scope:"), gbc);
        gbc.gridx = 1; form.add(singleStopRadio, gbc);
        gbc.gridx = 2; form.add(betweenStopsRadio, gbc);
        ButtonGroup group = new ButtonGroup();
        group.add(singleStopRadio); group.add(betweenStopsRadio);
        singleStopRadio.setSelected(true);
        gbc.gridx = 0; gbc.gridy = 5; form.add(new JLabel("Stop:"), gbc);
        gbc.gridx = 1; form.add(stopComboBox, gbc);
        gbc.gridx = 0; gbc.gridy = 6; form.add(new JLabel("Route:"), gbc);
        gbc.gridx = 1; form.add(routeComboBox, gbc);
        gbc.gridx = 0; gbc.gridy = 7; form.add(new JLabel("Stop A:"), gbc);
        gbc.gridx = 1; form.add(stopAComboBox, gbc);
        gbc.gridx = 2; form.add(new JLabel("Stop B:"), gbc);
        gbc.gridx = 3; form.add(stopBComboBox, gbc);
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 4; form.add(addButton, gbc);
        gbc.gridy = 9; form.add(messageLabel, gbc);

        add(form, BorderLayout.NORTH);

        // Hazard list
        hazardList.setVisibleRowCount(10);
        add(new JScrollPane(hazardList), BorderLayout.CENTER);

        // Remove hazard panel
        JPanel removePanel = new JPanel();
        removePanel.add(new JLabel("Remove by ID:"));
        removePanel.add(removeIdField);
        removePanel.add(removeButton);
        add(removePanel, BorderLayout.SOUTH);

        // Listeners
        addButton.addActionListener(this::handleAddHazard);
        removeButton.addActionListener(this::handleRemoveHazard);
        singleStopRadio.addActionListener(e -> updateScopeUI());
        betweenStopsRadio.addActionListener(e -> updateScopeUI());
        routeComboBox.addActionListener(e -> updateStopAStopBComboBoxes());

        updateScopeUI();
        refreshComboBoxes();
        
        // Refresh combo boxes when panel is shown
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                refreshComboBoxes();
            }
        });
    }

    private void updateScopeUI() {
        boolean single = singleStopRadio.isSelected();
        stopComboBox.setEnabled(single);
        routeComboBox.setEnabled(!single);
        stopAComboBox.setEnabled(!single);
        stopBComboBox.setEnabled(!single);
    }



    private void updateStopAStopBComboBoxes() {
        stopAComboBox.removeAllItems();
        stopBComboBox.removeAllItems();
        int routeIdx = routeComboBox.getSelectedIndex();
        if (routeIdx < 0 || routeIdx >= model.routes.size()) return;
        Route r = model.routes.get(routeIdx);
        List<Location> locs = r.getLocations();
        
        // Add all stops to both dropdowns
        for (Location loc : locs) {
            String stopLabel = loc.getLocationId() + " - " + loc.getName();
            stopAComboBox.addItem(stopLabel);
            stopBComboBox.addItem(stopLabel);
        }
    }

    private void handleAddHazard(ActionEvent e) {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String typeStr = (String) typeBox.getSelectedItem();
        String impactStr = impactField.getText().trim();
        boolean single = singleStopRadio.isSelected();
        int stopIdx = stopComboBox.getSelectedIndex();
        int routeIdx = routeComboBox.getSelectedIndex();
        int stopAIdx = stopAComboBox.getSelectedIndex();
        int stopBIdx = stopBComboBox.getSelectedIndex();
        
        // Validate between-stops selection
        if (!single && (stopAIdx < 0 || stopBIdx < 0)) {
            setMessage("Please select both Stop A and Stop B.", Color.RED);
            return;
        }
        
        if (!single && stopAIdx == stopBIdx) {
            setMessage("Stop A and Stop B cannot be the same location.", Color.RED);
            return;
        }
        
        // Check if stops are adjacent for between-stops hazards
        if (!single) {
            Route selectedRoute = model.routes.get(routeIdx);
            List<Location> routeLocs = selectedRoute.getLocations();
            
            if (stopAIdx >= routeLocs.size() || stopBIdx >= routeLocs.size()) {
                setMessage("Invalid stop selection.", Color.RED);
                return;
            }
            
            Location stopA = routeLocs.get(stopAIdx);
            Location stopB = routeLocs.get(stopBIdx);
            
            // Check if stops are adjacent (consecutive in the route)
            boolean areAdjacent = false;
            for (int i = 0; i < routeLocs.size() - 1; i++) {
                if ((routeLocs.get(i).equals(stopA) && routeLocs.get(i + 1).equals(stopB)) ||
                    (routeLocs.get(i).equals(stopB) && routeLocs.get(i + 1).equals(stopA))) {
                    areAdjacent = true;
                    break;
                }
            }
            
            if (!areAdjacent) {
                setMessage("Selected stops must be adjacent on the route.", Color.RED);
                return;
            }
        }
        
        String msg = hazardController.addHazard(id, name, typeStr, impactStr, single, stopIdx, routeIdx, stopAIdx, stopBIdx);
        if (msg.startsWith("Hazard added")) {
            // Refresh the entire hazard list to show updated information
            refreshHazardList();
            idField.setText(""); nameField.setText(""); impactField.setText("");
            setMessage(msg, new Color(0, 128, 0));
        } else {
            setMessage(msg, Color.RED);
        }
    }
    
    private void refreshHazardList() {
        hazardListModel.clear();
        for (Hazard h : model.hazards) {
            hazardListModel.addElement(h.toString());
        }
    }

    private void handleRemoveHazard(ActionEvent e) {
        String id = removeIdField.getText().trim();
        String msg = hazardController.removeHazard(id);
        if (msg.startsWith("Hazard removed")) {
            for (int i = 0; i < hazardListModel.size(); i++) {
                if (hazardListModel.get(i).contains(id)) {
                    hazardListModel.remove(i);
                    break;
                }
            }
            setMessage(msg, new Color(0, 128, 0));
        } else {
            setMessage(msg, Color.RED);
        }
    }

    private void setMessage(String msg, Color color) {
        messageLabel.setText(msg);
        messageLabel.setForeground(color);
    }
    
    // Public method to refresh combo boxes from other panels
    public void refreshComboBoxes() {
        stopComboBox.removeAllItems();
        for (Location l : model.locations) {
            stopComboBox.addItem(l.getLocationId() + " - " + l.getName());
        }
        routeComboBox.removeAllItems();
        for (Route r : model.routes) {
            routeComboBox.addItem(r.getRouteId() + " - " + r.getName());
        }
        updateStopAStopBComboBoxes();
    }
} 