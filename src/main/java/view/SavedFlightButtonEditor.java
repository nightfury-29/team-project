package view;

import entity.Flight;
import entity.FlightDetail;
import entity.FlightDetail.SegmentDetail;
import interface_adapter.flight_detail.FlightDetailController;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.List;

public class SavedFlightButtonEditor extends AbstractCellEditor implements TableCellEditor {

    private final JButton button = new JButton();
    private JTable table;
    private List<FlightDetail> flights;
    private FlightDetailController controller;

    public void setDependencies(JTable table, List<FlightDetail> flights, FlightDetailController controller) {
        this.table = table;
        this.flights = flights;
        this.controller = controller;
    }

    @Override
    public Component getTableCellEditorComponent(JTable tbl, Object value,
                                                 boolean isSelected, int row, int column) {
        button.setText(value == null ? "View" : value.toString());

        button.addActionListener(e -> {
            if (controller == null || flights == null) return;
            if (row < 0 || row >= flights.size()) return;

            FlightDetail f = flights.get(row);
            if (f.segments == null || f.segments.isEmpty()) return;

            SegmentDetail seg = f.segments.get(0);

            Flight flight = new Flight(
                    f.id,
                    seg.departureAirport,
                    seg.departureTime,
                    seg.arrivalAirport,
                    seg.arrivalTime,
                    f.price.total,
                    f.price.currency,
                    seg.duration,
                    seg.carrierCode,
                    seg.aircraft
            );

            controller.execute(flight.Id);
        });


        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }
}
