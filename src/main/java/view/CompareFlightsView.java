package view;

import interface_adapter.compare_saved_flights.CompareSavedFlightsState;
import interface_adapter.compare_saved_flights.CompareSavedFlightsViewModel;
import interface_adapter.go_back.GoBackController;
import use_case.compare_saved_flights.CompareSavedFlightsOutputData;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class CompareFlightsView extends JPanel implements PropertyChangeListener {

    private final CompareSavedFlightsViewModel viewModel;
    private GoBackController goBackController;

    private final JLabel titleLabel = new JLabel("Compare Flights", SwingConstants.CENTER);
    private final JPanel leftPanel = new JPanel();
    private final JPanel rightPanel = new JPanel();

    public CompareFlightsView(CompareSavedFlightsViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());

        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0, 102, 204));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(1, 2, 20, 0));
        center.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        center.add(leftPanel);
        center.add(rightPanel);
        add(center, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Saved Flights");
        backButton.addActionListener(e -> {
            if (goBackController != null) {
                goBackController.execute("saved flights");
            }
        });
        JPanel bottom = new JPanel();
        bottom.add(backButton);
        add(bottom, BorderLayout.SOUTH);
    }

    public String getViewName() {
        return viewModel.getViewName(); // "compare flights"
    }

    public void setGoBackController(GoBackController controller) {
        this.goBackController = controller;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!"state".equals(evt.getPropertyName())) {
            return;
        }

        CompareSavedFlightsState state = viewModel.getState();
        renderFlight(leftPanel, state.getFirst(), "Flight A");
        renderFlight(rightPanel, state.getSecond(), "Flight B");
    }

    private void renderFlight(JPanel panel,
                              CompareSavedFlightsOutputData.FlightSummary summary,
                              String label) {
        panel.removeAll();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        if (summary == null) {
            panel.add(new JLabel("No flight selected."));
        } else {
            panel.add(new JLabel(label));
            panel.add(Box.createVerticalStrut(8));
            panel.add(new JLabel("Airline: " + summary.airline));
            panel.add(new JLabel("Flight: " + summary.flightNumber));
            panel.add(new JLabel("Price: " + summary.price + " " + summary.currency));
            panel.add(new JLabel("Duration: " + summary.duration));
            panel.add(new JLabel("Checked bags: " + summary.checkedBags));
            panel.add(new JLabel("Cabin bags: " + summary.cabinBags));
        }

        panel.revalidate();
        panel.repaint();
    }
}
