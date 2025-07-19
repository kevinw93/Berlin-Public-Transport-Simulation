import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import javax.swing.*;

public class SimulationPanel extends JPanel {
    private final TransitDataModel model;
    private final SimulationManager simManager = SimulationManager.getInstance();
    private final JLabel timeLabel = new JLabel("Current Time: 0");
    private final JTextField advanceField = new JTextField(5);
    private final JButton advanceButton = new JButton("Advance Time");
    private final JButton showEventButton = new JButton("Show Next Event(s)");
    private final JButton executeEventButton = new JButton("Execute Next Event");
    private final JComboBox<String> vehicleComboBox = new JComboBox<>();
    private final JButton showStatusButton = new JButton("Show Vehicle Status");
    private final JButton startVehicleButton = new JButton("Start Vehicle on Route");
    private final JButton setupTestScenarioButton = new JButton("Set Up Test Scenario");
    private final JTextArea outputArea = new JTextArea(8, 40);
    private final JLabel messageLabel = new JLabel(" ");
    private final SimulationController simulationController;

    public SimulationPanel(TransitDataModel model, SimulationController simulationController) {
        this.model = model;
        this.simulationController = simulationController;
        setLayout(new BorderLayout());

        // Top panel with time and controls
        JPanel topPanel = new JPanel();
        topPanel.add(timeLabel);
        topPanel.add(new JLabel("Advance by (min):"));
        topPanel.add(advanceField);
        topPanel.add(advanceButton);
        topPanel.add(showEventButton);
        topPanel.add(executeEventButton);
        topPanel.add(setupTestScenarioButton);
        add(topPanel, BorderLayout.NORTH);

        // Center panel with vehicle controls
        JPanel centerPanel = new JPanel(new BorderLayout());
        
        // Vehicle control panel
        JPanel vehiclePanel = new JPanel();
        vehiclePanel.add(new JLabel("Vehicle:"));
        vehiclePanel.add(vehicleComboBox);
        vehiclePanel.add(showStatusButton);
        vehiclePanel.add(startVehicleButton);
        centerPanel.add(vehiclePanel, BorderLayout.NORTH);
        
        // Output area in center
        outputArea.setEditable(false);
        outputArea.setRows(10);
        outputArea.setColumns(50);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);

        // Bottom panel with message
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(messageLabel);
        add(bottomPanel, BorderLayout.SOUTH);

        advanceButton.addActionListener(this::handleAdvanceTime);
        showEventButton.addActionListener(this::handleShowEvent);
        executeEventButton.addActionListener(this::handleExecuteEvent);
        showStatusButton.addActionListener(this::handleShowVehicleStatus);
        setupTestScenarioButton.addActionListener(this::setupTestScenario);
        startVehicleButton.addActionListener(this::handleStartVehicleOnRoute);
        

        refreshVehicleComboBox();
        updateTimeLabel();
        
        // Refresh combo box when panel is shown
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                refreshVehicleComboBox();
            }
        });
    }

    // Public method to refresh vehicle combo box from other panels
    public void refreshVehicleComboBox() {
        vehicleComboBox.removeAllItems();
        for (Vehicle v : model.vehicles) {
            vehicleComboBox.addItem(v.getVehicleId() + " - " + v.getName());
        }
    }

    private void updateTimeLabel() {
        timeLabel.setText("Current Time: " + simManager.getCurrentTime());
    }

    private void handleAdvanceTime(ActionEvent e) {
        String val = advanceField.getText().trim();
        String msg = simulationController.advanceTime(val);
        setMessage(msg, msg.startsWith("Simulation time advanced") ? new Color(0, 128, 0) : Color.RED);
        updateTimeLabel();
    }

    private void handleShowEvent(ActionEvent e) {
        String output = simulationController.displayNextEvents();
        outputArea.setText(output);
    }

    private void handleExecuteEvent(ActionEvent e) {
        // Capture console output
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream oldOut = System.out;
        System.setOut(new PrintStream(baos));
        
        String msg = simulationController.executeNextEvent();
        
        System.setOut(oldOut);
        String output = baos.toString();
        
        // Combine the message and console output
        String fullOutput = msg + "\n\n" + output;
        outputArea.setText(fullOutput);
        
        setMessage(msg, msg.startsWith("Executed next event") ? new Color(0, 128, 0) : Color.RED);
        updateTimeLabel();
    }

    private void handleShowVehicleStatus(ActionEvent e) {
        int idx = vehicleComboBox.getSelectedIndex();
        if (idx < 0) {
            setMessage("Please select a vehicle.", Color.RED);
            return;
        }
        String msg = simulationController.getVehicleStatus(idx);
        outputArea.setText(msg);
    }

    private void handleStartVehicleOnRoute(ActionEvent e) {
        int idx = vehicleComboBox.getSelectedIndex();
        if (idx < 0) {
            setMessage("Please select a vehicle.", Color.RED);
            return;
        }
        
        Vehicle v = model.vehicles.get(idx);
        
        // Check if vehicle is already started
        if (v.inTransit()) {
            setMessage("Vehicle is already in transit and cannot be started again.", Color.RED);
            return;
        }
        
        // Check if vehicle already has a scheduled event
        if (simManager.hasScheduledEvent(v)) {
            setMessage("Vehicle already has a scheduled event and cannot be started again.", Color.RED);
            return;
        }
        
        String msg = simulationController.startVehicleMovement(idx);
        setMessage(msg, msg.startsWith("Vehicle ") ? new Color(0, 128, 0) : Color.RED);
    }

    public void setupTestScenario(ActionEvent e) {
        String msg = simulationController.setupTestScenario();
        setMessage(msg, new Color(0, 128, 0));
        refreshVehicleComboBox();
        setupTestScenarioButton.setEnabled(false);
    }

    private void setMessage(String msg, Color color) {
        messageLabel.setText(msg);
        messageLabel.setForeground(color);
    }
} 