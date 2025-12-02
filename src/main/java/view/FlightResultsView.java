package view;

import entity.Flight;
import interface_adapter.flight_results.FlightResultsState;
import interface_adapter.flight_results.FlightResultsViewModel;
import interface_adapter.go_back.GoBackController;
import interface_adapter.sort_flights.SortFlightsController;
import interface_adapter.flight_detail.FlightDetailController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.ArrayList;

public class FlightResultsView extends JPanel implements ActionListener, PropertyChangeListener {

    public final String viewName = "flight results";
    private final FlightResultsViewModel flightResultsViewModel;

    private final JButton sortByPrice;
    private final JButton sortByDuration;
    private final JButton sortByNonstop;
    private final JButton showAllFlights;

    private List<Flight> allFlightsCache;
    private List<Flight> currentDisplayedFlights = new ArrayList<>();

    private final JButton goBack;
    private GoBackController goBackController;
    private SortFlightsController sortFlightsController;
    private FlightDetailController flightDetailController;

    private JTable flightTable;
    private DefaultTableModel tableModel;

    private boolean suppressCacheUpdate = false;

    public FlightResultsView(FlightResultsViewModel flightResultsViewModel) {
        this.flightResultsViewModel = flightResultsViewModel;
        this.flightResultsViewModel.addPropertyChangeListener(this);

        JLabel title = new JLabel(FlightResultsViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] columnNames = {
                "Airline", "Flight No.", "Aircraft",
                "Departure", "Dep. Time", "Dep. Airport",
                "Arrival", "Arr. Time", "Arr. Airport",
                "Price", "Duration", "Details"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return column == 11; }
        };

        flightTable = new JTable(tableModel);
        flightTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScrollPane = new JScrollPane(flightTable);
        tableScrollPane.setPreferredSize(new Dimension(800, 300));

        TableColumn detailsColumn = flightTable.getColumnModel().getColumn(11);
        detailsColumn.setCellRenderer(new ButtonRenderer());
        ButtonEditor editor = new ButtonEditor(new JCheckBox());
        detailsColumn.setCellEditor(editor);

        JPanel buttons = new JPanel();
        sortByPrice = new JButton(FlightResultsViewModel.SORT_BY_PRICE_BUTTON_LABEL);
        sortByDuration = new JButton(FlightResultsViewModel.SORT_BY_DURATION_BUTTON_LABEL);
        sortByNonstop = new JButton(FlightResultsViewModel.NONSTOP_BUTTON_LABEL);
        showAllFlights = new JButton(FlightResultsViewModel.SHOW_ALL_FLIGHTS_BUTTON_LABEL);
        goBack = new JButton(FlightResultsViewModel.GO_BACK_BUTTON_LABEL);

        buttons.add(sortByDuration);
        buttons.add(sortByPrice);
        buttons.add(sortByNonstop);
        buttons.add(showAllFlights);
        buttons.add(goBack);

        goBack.addActionListener(e -> {
            if (goBackController != null) {
                goBackController.execute("logged in");
            }
        });

        sortByPrice.addActionListener(e -> {
            if (sortFlightsController != null) {
                suppressCacheUpdate = true;

                List<Flight> flightsToUse = (currentDisplayedFlights != null && !currentDisplayedFlights.isEmpty())
                        ? currentDisplayedFlights
                        : flightResultsViewModel.getState().getFlights();
                sortFlightsController.execute(flightsToUse, "PRICE");
            }
        });

        sortByDuration.addActionListener(e -> {
            if (sortFlightsController != null) {
                suppressCacheUpdate = true;

                List<Flight> flightsToUse = (currentDisplayedFlights != null && !currentDisplayedFlights.isEmpty())
                        ? currentDisplayedFlights
                        : flightResultsViewModel.getState().getFlights();
                sortFlightsController.execute(flightsToUse, "DURATION");
            }
        });

        sortByNonstop.addActionListener(e -> {
            if (sortFlightsController != null) {
                suppressCacheUpdate = true;

                List<Flight> flightsToUse = (currentDisplayedFlights != null && !currentDisplayedFlights.isEmpty())
                        ? currentDisplayedFlights
                        : flightResultsViewModel.getState().getFlights();
                sortFlightsController.execute(flightsToUse, "NONSTOP");
            }
        });

        showAllFlights.addActionListener(e -> {
            if (allFlightsCache != null) {
                updateTable(allFlightsCache);
            }
        });

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(title);
        this.add(tableScrollPane);
        this.add(buttons);
    }

    private void updateTable(List<Flight> flights) {
        currentDisplayedFlights = (flights == null) ? new ArrayList<>() : new ArrayList<>(flights);
        tableModel.setRowCount(0);

        for (Flight flight : currentDisplayedFlights) {
            String airlineCode = "N/A";
            String flightNumber = "N/A";

            if (flight.airline != null && !flight.airline.isEmpty()) {
                airlineCode = flight.airline.replaceAll("[^A-Z]", "");
                flightNumber = flight.airline.replaceAll("[^0-9]", "");
            }

            Object[] rowData = {
                    airlineCode,
                    flightNumber,
                    flight.aircraft,
                    flight.depTime.substring(0, 10),
                    flight.depTime.substring(11),
                    flight.depAirport,
                    flight.arrTime.substring(0, 10),
                    flight.arrTime.substring(11),
                    flight.arrAirport,
                    String.format("%.2f %s", flight.priceTotal, flight.currency),
                    flight.duration,
                    "View Details"
            };
            tableModel.addRow(rowData);
        }
        refreshButtonEditor();
    }

    private void refreshButtonEditor() {
        TableColumn detailsColumn = flightTable.getColumnModel().getColumn(11);

        ButtonEditor editor = new ButtonEditor(new JCheckBox());
        editor.setDependencies(
                flightTable,
                currentDisplayedFlights,
                flightDetailController
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
        updateTable(currentDisplayedFlights);
        refreshButtonEditor();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            FlightResultsState state = (FlightResultsState) evt.getNewValue();

            if (state.getError() != null) {
                JOptionPane.showMessageDialog(this, state.getError());
                state.setError(null);
                suppressCacheUpdate = false;
                return;
            }

            List<Flight> flights = state.getFlights();

            if (!suppressCacheUpdate) {
                allFlightsCache = (flights == null) ? new ArrayList<>() : new ArrayList<>(flights);
            }

            suppressCacheUpdate = false;

            updateTable(flights);
            refreshButtonEditor();
        }
    }

    @Override
    public void actionPerformed(ActionEvent evt) {

    }
}
