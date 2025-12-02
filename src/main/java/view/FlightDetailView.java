package view;

//import entity.FlightDetail;
//import entity.FlightDetail.SegmentDetail;
//import entity.FlightDetail.Amenity;
//import entity.FlightDetail.Baggage;
import data_transfer_objects.FlightDetailDataTransferObject;
import data_transfer_objects.FlightDetailDataTransferObject.AmenityDTO;
import data_transfer_objects.FlightDetailDataTransferObject.BaggageDTO;
import data_transfer_objects.FlightDetailDataTransferObject.SegmentDTO;
import interface_adapter.flight_detail.FlightDetailController;
import interface_adapter.flight_detail.FlightDetailFacade;
import interface_adapter.flight_detail.FlightDetailState;
import interface_adapter.flight_detail.FlightDetailViewModel;
import interface_adapter.go_back.GoBackController;
import interface_adapter.save_flight.SaveFlightController;
import interface_adapter.save_flight.SaveFlightViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class FlightDetailView extends JPanel implements PropertyChangeListener {

    private final String viewName = "flight detail";
    private final FlightDetailViewModel fdViewModel;
    private FlightDetailController controller;
    private FlightDetailFacade facade;
    private GoBackController goBackController;
    private SaveFlightController sfcontroller;
    private final SaveFlightViewModel saveFlightViewModel;

    private final JLabel titleLabel = new JLabel("Flight Details", SwingConstants.CENTER);
    private final JPanel contentPanel = new JPanel();
    private final JButton saveButton = new JButton("Save Flight");
    private final JButton goback = new JButton("Go Back");

    public FlightDetailView(FlightDetailViewModel fdViewModel
                            ,SaveFlightViewModel saveFlightViewModel) {
        this.fdViewModel = fdViewModel;
        this.saveFlightViewModel = saveFlightViewModel;
        fdViewModel.addPropertyChangeListener(this);
        saveFlightViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());

        // ---------- Title ----------
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0, 102, 204));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(titleLabel, BorderLayout.NORTH);

        // ---------- Content Area ----------
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        add(scrollPane, BorderLayout.CENTER);

        // ---------- Buttons ----------
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        buttonPanel.add(goback);
        goback.addActionListener(e -> {
            if (goBackController != null) {
                FlightDetailState state = fdViewModel.getState();
                String target = state.getPreviousViewName();

                if (target == null || target.isEmpty()) {
                    target = "flight results";
                }

                goBackController.execute(target);
            }
        });

        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);


        saveButton.addActionListener(e -> {
            FlightDetailState state = fdViewModel.getState();
            FlightDetailDataTransferObject detail = state.getFlightDetail();

            if (detail == null) {
                JOptionPane.showMessageDialog(this, "No flight loaded.");
                return;
            }

            sfcontroller.handleSaveButton(detail);
        });


        renderEmpty();
    }

    public void setFacade(FlightDetailFacade facade) {
        this.facade = facade;
    }

    public JButton getSaveButton() { return saveButton; }
    public JButton getGoback() { return goback; }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // --------- SAVE FLIGHT EVENTS ---------
        if ("saveFlightResult".equals(evt.getPropertyName())) {

            // Get message from the ViewModel
            String message = saveFlightViewModel.getMessage();
            JOptionPane.showMessageDialog(this, message);

            return; // Don't fall through to flight rendering
        }


        // --------- FLIGHT DETAIL EVENTS ---------
        if ("state".equals(evt.getPropertyName())) {

            FlightDetailState state = fdViewModel.getState();

            String prev = state.getPreviousViewName();
            if ("saved flights".equals(prev)) {
                saveButton.setVisible(false);
            } else {
                saveButton.setVisible(true);
            }

            if (state.getErrorMessage() != null) {
                JOptionPane.showMessageDialog(this, state.getErrorMessage());
                return;
            }

            FlightDetailDataTransferObject detail = state.getFlightDetail();
            if (detail == null) {
                renderEmpty();
            } else {
                renderDetail(detail);
            }
        }

    }

    private void renderEmpty() {
        contentPanel.removeAll();
        JLabel empty = new JLabel("Select a flight to see details.");
        empty.setFont(new Font("SansSerif", Font.ITALIC, 14));
        contentPanel.add(empty);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void renderDetail(FlightDetailDataTransferObject fd) {
        contentPanel.removeAll();

        // Title section
        addTitle("General Information");
        addLine("Fare Option: " + fd.fareOption);
        addLine("Seats Available: " + fd.numberOfBookableSeats);

        addSeparator();

        // Price Section
        addTitle("Price");
        addLine("Total Price: " + fd.price.total + " " + fd.price.currency);

        addSeparator();

        // Segments Section
        addTitle("Flight Segments");

        for (int i = 0; i < fd.segments.size(); i++) {
            SegmentDTO seg = fd.segments.get(i);

            addSubtitle("Segment " + (i + 1));

            addLine("Route: " + seg.departureAirport + " → " + seg.arrivalAirport);
            addLine("Departure: " + format(seg.departureTime)
                    + (seg.departureTerminal.isEmpty() ? "" : " (Terminal " + seg.departureTerminal + ")"));
            addLine("Arrival: " + format(seg.arrivalTime)
                    + (seg.arrivalTerminal.isEmpty() ? "" : " (Terminal " + seg.arrivalTerminal + ")"));

            addLine("Airline: " + seg.carrierCode);
            addLine("Flight Number: " + seg.flightNumber);
            addLine("Aircraft: " + seg.aircraft);
            addLine("Duration: " + formatDuration(seg.duration));
            addLine("Cabin Class: " + seg.cabinClass);

            // Baggage
            BaggageDTO bag = seg.baggage;
            addLine("Checked Bags: " + bag.checkedBags);
            addLine("Cabin Bags: " + bag.cabinBags);

            // Amenities
            if (seg.amenities != null && !seg.amenities.isEmpty()) {
                addSubtitle("Amenities:");

                for (AmenityDTO am : seg.amenities) {
                    String chargeInfo = am.isChargeable ? "(Extra)" : "(Included)";
                    addSmall("- " + am.description + " / " + am.amenityType + " " + chargeInfo);
                }
            }

            addSeparator();
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // Helpers
    private void addTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 17));
        label.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        contentPanel.add(label);
    }

    private void addSubtitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setBorder(BorderFactory.createEmptyBorder(8, 0, 4, 0));
        contentPanel.add(label);
    }

    private void addLine(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
        contentPanel.add(label);
    }

    private void addSmall(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        contentPanel.add(label);
    }

    private void addSeparator() {
        contentPanel.add(Box.createVerticalStrut(8));
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        contentPanel.add(sep);
        contentPanel.add(Box.createVerticalStrut(8));
    }

    private String format(String iso) {
        if (iso == null) return "-";
        return iso.replace("T", " ");
    }

    private String formatDuration(String iso) {
        if (iso == null) return "-";
        try {
            String clean = iso.replace("PT", "");
            int h = 0, m = 0;
            if (clean.contains("H")) {
                h = Integer.parseInt(clean.substring(0, clean.indexOf("H")));
            }
            if (clean.contains("M")) {
                int start = clean.contains("H") ? clean.indexOf("H") + 1 : 0;
                m = Integer.parseInt(clean.substring(start, clean.indexOf("M")));
            }
            return h + "h " + m + "m";
        } catch (Exception e) {
            return iso;
        }
    }

    public String getViewName() { return viewName; }

    public void setGoBackController(GoBackController goBackController) {
        this.goBackController = goBackController;
    }

    public void setSaveFlightController(SaveFlightController saveFlightController) {
        this.sfcontroller = saveFlightController;
    }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

}
