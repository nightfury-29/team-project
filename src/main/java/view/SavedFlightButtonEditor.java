package view;

import entity.FlightDetail;
import entity.FlightDetail.SegmentDetail;
import interface_adapter.saved_flights.SavedFlightDetailController;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.List;

public class SavedFlightButtonEditor extends AbstractCellEditor implements TableCellEditor {

    private final JButton button = new JButton();
    private JTable table;
    private List<FlightDetail> flights;
    private SavedFlightDetailController controller;

    public void setDependencies(JTable table,
                                List<FlightDetail> flights,
                                SavedFlightDetailController controller) {
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

            // 现在不再把它压缩成 Flight，而是直接把整个 FlightDetail 交给控制器
            controller.execute(f);
        });

        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }
}
