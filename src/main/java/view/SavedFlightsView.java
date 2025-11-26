package view;

import entity.FlightDetail;
import entity.FlightDetail.SegmentDetail;
import interface_adapter.saved_flights.SavedFlightsViewModel;
import interface_adapter.flight_detail.FlightDetailController;
import interface_adapter.go_back.GoBackController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import interface_adapter.ViewManagerModel;


/**
 * Displays user's saved flights (FlightDetail objects)
 */
public class SavedFlightsView extends JPanel implements PropertyChangeListener {

    public final String viewName = "saved flights";

    private final SavedFlightsViewModel viewModel;

    private JTable table;
    private DefaultTableModel model;

    private FlightDetailController detailController;
    private GoBackController goBackController;

    public SavedFlightsView(SavedFlightsViewModel viewModel, ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        // Listen to ViewManagerModel to show this screen when activated
        viewManagerModel.addPropertyChangeListener(evt -> {

            System.out.println("[SavedFlightsView] ViewManagerModel event: "
                    + evt.getPropertyName() + " new=" + evt.getNewValue());

            if ("state".equals(evt.getPropertyName()) &&
                    viewName.equals(evt.getNewValue())) {
                System.out.println("[SavedFlightsView] ACTIVATED!");
                this.setVisible(true);
            }
        });

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Saved Flights");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);

        String[] columns = {
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
                return c == 11; // only Details column
            }
        };

        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(900, 350));
        add(scroll);

        // add renderer/editor for Details btn
        TableColumn col = table.getColumnModel().getColumn(11);
        col.setCellRenderer(new ButtonRenderer());
        col.setCellEditor(new ButtonEditor(new JCheckBox()));

        JButton back = new JButton("Go Back");
        back.addActionListener(e -> {
            if (goBackController != null) goBackController.execute("logged in");
        });
        JPanel p = new JPanel();
        p.add(back);
        add(p);
    }

    private void refreshEditor(List<FlightDetail> flights) {
        SavedFlightButtonEditor editor = new SavedFlightButtonEditor();
        editor.setDependencies(table, flights, detailController);
        table.getColumnModel().getColumn(11).setCellEditor(editor);

    }

    private void updateTable(List<FlightDetail> flights) {
        model.setRowCount(0);

        for (FlightDetail f : flights) {
            if (f.segments == null || f.segments.isEmpty()) continue;
            SegmentDetail seg = f.segments.get(0);

            String airline = seg.carrierCode;
            String flightNo = seg.flightNumber;
            String aircraft = seg.aircraft;

            Object[] row = {
                    airline,
                    flightNo,
                    aircraft,
                    seg.departureAirport,
                    seg.departureTime,
                    seg.arrivalAirport,
                    seg.arrivalTime,
                    seg.duration,
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

    public void setFlightDetailController(FlightDetailController c) {
        this.detailController = c;
    }

    public void setGoBackController(GoBackController c) {
        this.goBackController = c;
    }
}
