package view;

import entity.FlightDetail;
import entity.FlightDetail.SegmentDetail;
import interface_adapter.saved_flights.SavedFlightsViewModel;
import interface_adapter.saved_flights.SavedFlightDetailController;
import interface_adapter.go_back.GoBackController;
import interface_adapter.compare_saved_flights.CompareSavedFlightsController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import interface_adapter.ViewManagerModel;
import interface_adapter.go_back.GoBackController;
import interface_adapter.ViewManagerModel;


/**
 * Displays user's saved flights (FlightDetail objects)
 */
public class SavedFlightsView extends JPanel implements PropertyChangeListener {

    public final String viewName = "saved flights";

    private final SavedFlightsViewModel viewModel;

    private JTable table;
    private DefaultTableModel model;

    private SavedFlightDetailController detailController;
    private GoBackController goBackController;
    private CompareSavedFlightsController compareController;
    private JButton compareButton;

    public SavedFlightsView(SavedFlightsViewModel viewModel, ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        // Listen to ViewManagerModel to show this screen when activated
        viewManagerModel.addPropertyChangeListener(evt -> {

            System.out.println("[SavedFlightsView] ViewManagerModel event: "
                    + evt.getPropertyName() + " new=" + evt.getNewValue());

            if ("state".equals(evt.getPropertyName())) {
                boolean active = viewName.equals(evt.getNewValue());
                this.setVisible(active);

                if (active) {
                    System.out.println("[SavedFlightsView] ACTIVATED!");
                }
            }
        });

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Saved Flights");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);

        String[] columns = {
                "Select",
                "Airline", "Flight No",
                "Aircraft",
                "Dep Airport", "Dep Time",
                "Arr Airport", "Arr Time",
                "Duration",
                "Price",
                "Fare",
                "Seats",
                "Details"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return c == 0 || c == 12;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Boolean.class;
                }
                return String.class;
            }
        };


        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(900, 350));
        add(scroll);

        // add renderer/editor for Details btn
        TableColumn col = table.getColumnModel().getColumn(12);
        col.setCellRenderer(new ButtonRenderer());
        col.setCellEditor(new ButtonEditor(new JCheckBox()));

        JButton back = new JButton("Go Back");
        back.addActionListener(e -> {
            if (goBackController != null) goBackController.execute("logged in");
        });

        compareButton = new JButton("Compare Flights");
        compareButton.addActionListener(e -> handleCompareButtonClicked());

        JPanel p = new JPanel();
        p.add(back);
        p.add(compareButton);
        add(p);

    }

    private void handleCompareButtonClicked() {
        if (compareController == null) {
            JOptionPane.showMessageDialog(this, "Compare controller not set.");
            return;
        }

        List<FlightDetail> flights = viewModel.getFlights();
        if (flights == null || flights.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No saved flights to compare.");
            return;
        }

        java.util.List<Integer> selectedRows = new java.util.ArrayList<>();

        for (int i = 0; i < model.getRowCount(); i++) {
            Object value = model.getValueAt(i, 0);
            if (Boolean.TRUE.equals(value)) {
                selectedRows.add(i);
            }
        }

        if (selectedRows.size() != 2) {
            JOptionPane.showMessageDialog(this, "Please select exactly two flights to compare.");
            return;
        }

        int idx1 = selectedRows.get(0);
        int idx2 = selectedRows.get(1);

        FlightDetail f1 = flights.get(idx1);
        FlightDetail f2 = flights.get(idx2);

        compareController.execute(f1, f2);
    }

    public void setCompareSavedFlightsController(CompareSavedFlightsController c) {
        this.compareController = c;
    }

    private void refreshEditor(List<FlightDetail> flights) {
        SavedFlightButtonEditor editor = new SavedFlightButtonEditor();
        editor.setDependencies(table, flights, detailController);
        table.getColumnModel().getColumn(12).setCellEditor(editor);

    }

    private void updateTable(List<FlightDetail> flights) {
        model.setRowCount(0);

        for (FlightDetail f : flights) {
            if (f.segments == null || f.segments.isEmpty()) {
                continue;
            }

            SegmentDetail firstSeg = f.segments.get(0);
            SegmentDetail lastSeg = f.segments.get(f.segments.size() - 1);

            String airline = firstSeg.carrierCode;
            String flightNo = firstSeg.flightNumber;
            String aircraft = firstSeg.aircraft;

            Object[] row = {
                    Boolean.FALSE,
                    airline,
                    flightNo,
                    aircraft,
                    firstSeg.departureAirport,
                    firstSeg.departureTime,
                    lastSeg.arrivalAirport,
                    lastSeg.arrivalTime,
                    firstSeg.duration,
                    f.price.total + " " + f.price.currency,
                    f.fareOption,
                    f.numberOfBookableSeats,
                    "View"
            };
            model.addRow(row);
        }

        refreshEditor(flights);
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!evt.getPropertyName().equals("savedFlights")) return;
        updateTable(viewModel.getFlights());
    }

    public void setFlightDetailController(SavedFlightDetailController c) {
        this.detailController = c;
    }

    public void setGoBackController(GoBackController c) {
        this.goBackController = c;
    }
}
