package view;

import interface_adapter.viewing_history.LoadHistoryController;
import interface_adapter.viewing_history.SearchHistoryItem;
import interface_adapter.viewing_history.ViewingHistoryState;
import interface_adapter.viewing_history.ViewingHistoryViewModel;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for when the user clicks on the "See History" button from the Logged In view.
 */

public class ViewingHistoryView extends JPanel implements ActionListener, PropertyChangeListener {


    private final String viewName = "viewing history";
    private final ViewingHistoryViewModel viewingHistoryViewModel;
    private LoadHistoryController loadHistoryController;

    private final JLabel titleLabel;
    private final JButton goBack;
    private final JButton loadSelection;
    private final JTable historyTable;
    private final DefaultTableModel tableModel;
    private final JScrollPane tableScrollPane;

    public ViewingHistoryView(ViewingHistoryViewModel viewingHistoryViewModel) {
        this.viewingHistoryViewModel = viewingHistoryViewModel;
        this.viewingHistoryViewModel.addPropertyChangeListener(this);

        // Set up title label
        this.titleLabel = new JLabel("Search History");
        this.titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Set up buttons
        final JPanel buttons = new JPanel();
        this.goBack = new JButton("Go Back");
        this.loadSelection = new JButton("Load Selection");
        buttons.add(goBack);
        buttons.add(loadSelection);

        // Set up table
        final String[] columnTitles = {"Date", "Time", "From", "To", "Day of Arrival", "Month of Arrival", "Year of Arrival"};

        this.tableModel = new DefaultTableModel(columnTitles, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        this.historyTable = new JTable(this.tableModel);

        // Add to scroll pane
        this.tableScrollPane = new JScrollPane(this.historyTable);
        this.tableScrollPane.setPreferredSize(new Dimension(800, 300));

        // Set layout and add components
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(titleLabel);
        this.add(tableScrollPane);
        this.add(buttons);

        // Add selection listener to table
        this.historyTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {

                // Handles updating the selected entry in the state
                int row = historyTable.getSelectedRow();
                final ViewingHistoryState currentState = this.viewingHistoryViewModel.getState();

                if (row != -1) {

                    currentState.setSelectedEntry(currentState.getSearchHistory().get(row));
                    this.viewingHistoryViewModel.setState(currentState);
                }

                else {
                    currentState.setSelectedEntry(null);
                    this.viewingHistoryViewModel.setState(currentState);
                }

            }
        });

        // Add listener to Go Back button
        this.goBack.addActionListener(evt -> {
            if (evt.getSource() == this.goBack) {
                loadHistoryController.switchToLoggedInView();
            }
        });

        // Add listener to Load Selection button
        this.loadSelection.addActionListener(evt -> {
            if (evt.getSource() == this.loadSelection) {
                final ViewingHistoryState currentState = this.viewingHistoryViewModel.getState();

                if (currentState.getSelectedEntry() != null) {
                    final SearchHistoryItem searchInformation = currentState.getSelectedEntry();
                    final String from = searchInformation.getFrom();
                    final String to = searchInformation.getTo();
                    final String day = searchInformation.getDay();
                    final String month = searchInformation.getMonth();
                    final String year = searchInformation.getYear();
                    this.loadHistoryController.execute(from, to, day, month, year);
                }

                else {
                    this.loadHistoryController.execute(null, null, null, null, null);
                }
            }
        });
    }

    private void initializeHistoryTable(List<SearchHistoryItem> searchHistory) {

        // Clear existing rows
        this.tableModel.setRowCount(0);

        // Populate and add rows to the table
        for (SearchHistoryItem search: searchHistory) {
            final Object[] row = {search.getDate(), search.getTime(), search.getFrom(),
                    search.getTo(), search.getDay(), search.getMonth(), search.getYear()};

            this.tableModel.addRow(row);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final ViewingHistoryState state = (ViewingHistoryState) evt.getNewValue();

        if (evt.getPropertyName().equals("initialize success view")) {
            final List<SearchHistoryItem> searchHistory = state.getSearchHistory();
            initializeHistoryTable(searchHistory);
        }

        else if (evt.getPropertyName().equals("error")) {
            JOptionPane.showMessageDialog(this, state.getErrorMessage());
        }
    }

    public void setLoadHistoryController(LoadHistoryController loadHistoryController) {
        this.loadHistoryController = loadHistoryController;
    }

    public String getViewName() {
        return viewName;
    }
}
