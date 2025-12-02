package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import entity.Flight;
import interface_adapter.flight_detail.FlightDetailController;
import interface_adapter.flight_detail.FlightDetailFacade;

public class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private String label;
    private boolean isPushed;
    private int row; //
    private java.util.List<Flight> flights;
    private FlightDetailController flightDetailController;
    private FlightDetailFacade flightDetailFacade;
    private JTable table;
// <-- 1. ADD THIS FIELD TO STORE THE ROW

    public ButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (flights != null && row >= 0 && row < flights.size()) {
                    Flight selectedFlight = flights.get(row);
                    if (flightDetailController != null) {
                        System.out.println("Button clicked, executing controller: " + selectedFlight);
                        flightDetailController.execute(selectedFlight.Id);
                    } else {
                        System.out.println("Controller is null!");
                    }
                } else {
                    System.out.println("Row invalid or flights null!");
                }

                fireEditingStopped();
            }
        });
        ;
    }

    public void setDependencies(JTable table,
                                java.util.List<Flight> flights,
                                FlightDetailFacade facade) {
        this.table = table;
        this.flights = flights;
        this.flightDetailFacade = facade;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        this.row = row; // <-- 2. STORE THE ROW WHEN THE EDITOR IS CREATED
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            int selectedRow = this.row;

            if (flights != null && selectedRow >= 0 && selectedRow < flights.size()) {
                Flight selectedFlight = flights.get(selectedRow);

                if (flightDetailFacade != null) {
                    flightDetailFacade.showFlightDetail(selectedFlight.Id);
                }
            }
        }
        isPushed = false;
        return label;
    }


    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }
}
