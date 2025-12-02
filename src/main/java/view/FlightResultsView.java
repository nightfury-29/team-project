package view;

import entity.Flight;
import interface_adapter.flight_results.FlightResultsState;
import interface_adapter.flight_results.FlightResultsViewModel;
import interface_adapter.go_back.GoBackController;
import interface_adapter.sort_flights.SortFlightsController;
import interface_adapter.flight_detail.FlightDetailController;
import interface_adapter.flight_detail.FlightDetailFacade;
import javax.swing.table.TableColumn;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class FlightResultsView extends JPanel implements ActionListener, PropertyChangeListener {

    public final String viewName = "flight results";
    private final FlightResultsViewModel flightResultsViewModel;

    // Controllers will be set by AppBuilder
    // private SortFlightsController sortFlightsController;
    // private SwitchViewController switchViewController;

    private final JButton sortByPrice;
    private final JButton sortByDuration;
    private final JButton sortByNonstop;
    private final JButton goBack;
    private GoBackController goBackController;
    private SortFlightsController sortFlightsController;
    private FlightDetailController flightDetailController;

    private JTable flightTable;
    private DefaultTableModel tableModel;

    private FlightDetailFacade flightDetailFacade;

    public FlightResultsView(FlightResultsViewModel flightResultsViewModel) {
        this.flightResultsViewModel = flightResultsViewModel;
        this.flightResultsViewModel.addPropertyChangeListener(this);

        JLabel title = new JLabel(FlightResultsViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Table Setup ---
        // Define column names
        String[] columnNames = {
                "Airline", "Flight No.", "Aircraft",
                "Departure", "Dep. Time", "Dep. Airport",
                "Arrival", "Arr. Time", "Arr. Airport",
                "Price", "Duration", "Details" //you can remove some info
        };

        // Create a non-editable table model
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return column == 11;}
        };
        flightTable = new JTable(tableModel);
        flightTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScrollPane = new JScrollPane(flightTable);
        tableScrollPane.setPreferredSize(new Dimension(800, 300));

        TableColumn detailsColumn = flightTable.getColumnModel().getColumn(11); // 11 is the index of the "Details" column
        detailsColumn.setCellRenderer(new ButtonRenderer());
        ButtonEditor editor = new ButtonEditor(new JCheckBox());
        detailsColumn.setCellEditor(editor);


        // --- Button Panel ---
        JPanel buttons = new JPanel();
        sortByPrice = new JButton(FlightResultsViewModel.SORT_BY_PRICE_BUTTON_LABEL);
        sortByDuration = new JButton(FlightResultsViewModel.SORT_BY_DURATION_BUTTON_LABEL);
        sortByNonstop = new JButton(FlightResultsViewModel.NONSTOP_BUTTON_LABEL);
        goBack = new JButton(FlightResultsViewModel.GO_BACK_BUTTON_LABEL);

        buttons.add(sortByDuration);
        buttons.add(sortByPrice);
        buttons.add(sortByNonstop);
        buttons.add(goBack);

        // --- Button Listeners ---
        goBack.addActionListener(e -> {
            if (goBackController != null) {
                goBackController.execute("logged in"); // Tell it to go back to the logged in view
            }
        });

        sortByPrice.addActionListener(e -> {
            if (sortFlightsController != null) {
                FlightResultsState currentState = flightResultsViewModel.getState();
                sortFlightsController.execute(currentState.getFlights(), "PRICE");
            }
        });

        sortByDuration.addActionListener(e -> {
            if (sortFlightsController != null) {
                FlightResultsState currentState = flightResultsViewModel.getState();
                sortFlightsController.execute(currentState.getFlights(), "DURATION");
            }
        });

        sortByNonstop.addActionListener(e -> {
            if (sortFlightsController != null) {
                FlightResultsState currentState = flightResultsViewModel.getState();
                sortFlightsController.execute(currentState.getFlights(), "NONSTOP");
            }
        });
        // Listeners will be added once controllers are made

        // Example for "Go Back"
        // goBack.addActionListener(e -> {
        //     if (switchViewController != null) {
        //         switchViewController.execute("logged in");
        //     }
        // });

        // Example for "Sort"
        // sortByPrice.addActionListener(e -> {
        //     if (sortFlightsController != null) {
        //         FlightResultsState currentState = flightResultsViewModel.getState();
        //         sortFlightsController.execute(currentState.getFlights(), "PRICE");
        //     }
        // });

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(title);
        this.add(tableScrollPane);
        this.add(buttons);
    }

    // This method updates the table when the ViewModel's state changes
    private void updateTable(List<Flight> flights) {
        // Clear existing rows
        tableModel.setRowCount(0);

        for (Flight flight : flights) {

            // --- FIX IS HERE ---
            String airlineCode = "N/A";
            String flightNumber = "N/A";

            if (flight.airline != null && !flight.airline.isEmpty()) {
                // Safely parse codes like "EK5053"
                airlineCode = flight.airline.replaceAll("[^A-Z]", ""); // Gets "EK"
                flightNumber = flight.airline.replaceAll("[^0-9]", ""); // Gets "5053"
            }
            // --- END OF FIX ---

            Object[] rowData = {
                    airlineCode,    // Airline Code
                    flightNumber,   // Flight Number
                    flight.aircraft,
                    flight.depTime.substring(0, 10), // Dep. Date
                    flight.depTime.substring(11),    // Dep. Time
                    flight.depAirport,
                    flight.arrTime.substring(0, 10), // Arr. Date
                    flight.arrTime.substring(11),    // Arr. Time
                    flight.arrAirport,
                    String.format("%.2f %s", flight.priceTotal, flight.currency),
                    flight.duration,
                    "View Details"
            };
            tableModel.addRow(rowData);
        }
        refreshButtonEditor();
    }

    public void setFlightDetailFacade(FlightDetailFacade facade) {
        this.flightDetailFacade = facade;
        refreshButtonEditor();
    }

    private void refreshButtonEditor() {
        TableColumn detailsColumn = flightTable.getColumnModel().getColumn(11);

        ButtonEditor editor = new ButtonEditor(new JCheckBox());
        editor.setDependencies(
                flightTable,
                flightResultsViewModel.getState().getFlights(),
                flightDetailFacade
        );

        detailsColumn.setCellEditor(editor);
    }


    public void setGoBackController(GoBackController goBackController) {
        this.goBackController = goBackController;
    }

    public void setSortFlightsController(SortFlightsController sortFlightsController) {
        this.sortFlightsController = sortFlightsController;
    }

    public void setFlightDetailController(FlightDetailController controller) {
        this.flightDetailController = controller;
        refreshButtonEditor();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            FlightResultsState state = (FlightResultsState) evt.getNewValue();
            if (state.getError() != null) {
                JOptionPane.showMessageDialog(this, state.getError());
                state.setError(null); // Clear error after showing
            } else {
                updateTable(state.getFlights());
                refreshButtonEditor();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        // Handled by lambda listeners
    }

    // Setters for controllers
    // public void setSortFlightsController(SortFlightsController controller) {
    //     this.sortFlightsController = controller;
    // }
    // public void setSwitchViewController(SwitchViewController controller) {
    //     this.switchViewController = controller;
    // }
}
