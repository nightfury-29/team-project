package view;

import interface_adapter.logged_in.ChangePasswordController;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.logged_in.FindFlightController;
import interface_adapter.view_history.ViewHistoryController;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import interface_adapter.saved_flights.SeeSavedFlightsController;

/**
 * The View for when the user is logged into the program.
 */
public class LoggedInView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "logged in";
    private final LoggedInViewModel loggedInViewModel;
    private ChangePasswordController changePasswordController = null;
    private LogoutController logoutController;
    private FindFlightController findFlightController;
    private ViewHistoryController viewHistoryController;

    //  TODO: IMPLEMENT the controllers, then uncomment this code
//  private FindFlightController findFlightController;
//  private SeeHistoryController seeHistoryController;
    private SeeSavedFlightsController seeSavedFlightsController;
//  private LogHistoryController logHistoryController;
    private final JButton logOut;

    private JLabel usernameInfo = new JLabel();

    private final JTextField fromInputField = new JTextField(15);
    private final JTextField toInputField = new JTextField(15);

    private final JTextField dayInputField = new JTextField(15);
    String[] month_options = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private final JComboBox<String> monthInputField = new JComboBox<>(month_options);
    String[] year_options = {"2025", "2026"};
    private final JComboBox<String> yearInputField = new JComboBox<>(year_options);

    private final JButton findFlight;
    private final JButton seeHistory;
    private final JButton seeSavedFlights;

    public LoggedInView(LoggedInViewModel loggedInViewModel) {
        this.loggedInViewModel = loggedInViewModel;
        this.loggedInViewModel.addPropertyChangeListener(this);

        this.usernameInfo.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JLabel title = new JLabel("Where are we going?");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final LabelTextPanel fromInfo = new LabelTextPanel(
                new JLabel("From:"), fromInputField);

        final LabelTextPanel toInfo = new LabelTextPanel(
                new JLabel("To:"), toInputField);

        final LabelTextPanel dayInfo = new LabelTextPanel(
                new JLabel("Day of Arrival:"), dayInputField);

        final JPanel monthInfo = new JPanel();
        monthInfo.add(new JLabel("Month of Arrival:"));
        monthInfo.add(monthInputField);

        final JPanel yearInfo = new JPanel();
        yearInfo.add(new JLabel("Year of Arrival:"));
        yearInfo.add(yearInputField);

        final JPanel buttons = new JPanel();

        findFlight = new JButton("Find my Flight!");
        buttons.add(findFlight);

        seeHistory = new JButton("See History");
        buttons.add(seeHistory);

        seeSavedFlights = new JButton("Saved Flights");
        buttons.add(seeSavedFlights);

        logOut = new JButton("Log Out");
        buttons.add(logOut);

        logOut.addActionListener(// This creates an anonymous subclass of ActionListener and instantiates it.
                evt -> {
                    if (evt.getSource().equals(logOut)) {
                        this.logoutController.execute();
                    }
                });

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        fromInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final LoggedInState currentState = loggedInViewModel.getState();
                currentState.setFrom(fromInputField.getText());
                loggedInViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });

        toInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final LoggedInState currentState = loggedInViewModel.getState();
                currentState.setTo(toInputField.getText());
                loggedInViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });

        dayInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final LoggedInState currentState = loggedInViewModel.getState();
                currentState.setDay(dayInputField.getText());
                loggedInViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });

        monthInputField.addItemListener(e -> {

            if (e.getStateChange() == ItemEvent.SELECTED) {
                String month = monthInputField.getSelectedItem().toString();
                final LoggedInState currentState = loggedInViewModel.getState();
                currentState.setMonth(month);
                loggedInViewModel.setState(currentState);
            }
        });

        yearInputField.addItemListener(e -> {

            if (e.getStateChange() == ItemEvent.SELECTED) {
                String year = yearInputField.getSelectedItem().toString();
                final LoggedInState currentState = loggedInViewModel.getState();
                currentState.setYear(year);
                loggedInViewModel.setState(currentState);
            }
        });

        findFlight.addActionListener(
                evt -> {
                    System.out.println("Find my Flight button clicked!"); // <-- 1. Did the click register?

                    if (evt.getSource().equals(findFlight)) {
                        final LoggedInState currentState = loggedInViewModel.getState();

                        if (this.findFlightController != null) {
                            System.out.println("Controller is NOT null. Executing use case..."); // <-- 2. Is controller valid?

                            this.findFlightController.execute(
                                    currentState.getUsername(),
                                    currentState.getFrom(),
                                    currentState.getTo(),
                                    currentState.getDay(),
                                    currentState.getMonth(),
                                    currentState.getYear()
                            );
                        }
                        else {
                            // THIS IS THE MOST LIKELY PROBLEM
                            System.err.println("FindFlightController is NULL. Check AppBuilder wiring."); // <-- 3. Is it null?
                            JOptionPane.showMessageDialog(this, "Error: FindFlightController is not initialized.", "Wiring Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
        );

        seeHistory.addActionListener(
                // This creates an anonymous subclass of ActionListener and instantiates it.
                evt -> {
                    if (evt.getSource().equals(seeHistory)) {
                        final LoggedInState currentState = loggedInViewModel.getState();
                        this.viewHistoryController.execute(currentState.getUsername());
                    }
                }
        );
        seeSavedFlights.addActionListener(evt -> {
            System.out.println("[UI] Saved Flights button clicked!");
            LoggedInState currentState = loggedInViewModel.getState();
            System.out.println("[UI] current user = " + currentState.getUsername());

            if (seeSavedFlightsController != null) {
                System.out.println("[UI] calling controller...");
                seeSavedFlightsController.execute(currentState.getUsername());
            }
            else {
                System.err.println("[UI] Controller is NULL!!! Wiring error.");
            }
        });

        this.add(title);
        this.add(this.usernameInfo);
        this.add(fromInfo);
        this.add(toInfo);
        this.add(dayInfo);
        this.add(monthInfo);
        this.add(yearInfo);
        this.add(buttons);
    }

    /**
     * React to a button click that results in evt.
     *
     * @param evt the ActionEvent to react to
     */
    public void actionPerformed(ActionEvent evt) {
        System.out.println("Click " + evt.getActionCommand());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final LoggedInState state = (LoggedInState) evt.getNewValue();

        if (evt.getPropertyName().equals("state")) {
            this.usernameInfo.setText("Currently logged in as: " + state.getUsername());
        }
        else if (evt.getPropertyName().equals("error")) {
            JOptionPane.showMessageDialog(this, state.getError());
        }

        else if (evt.getPropertyName().equals("update fields")) {
            fromInputField.setText(state.getFrom());
            toInputField.setText(state.getTo());
            dayInputField.setText(state.getDay());
            monthInputField.setSelectedItem(state.getMonth());
            yearInputField.setSelectedItem(state.getYear());
        }
    }

    public String getViewName() {
        return viewName;
    }

    public void setChangePasswordController(ChangePasswordController changePasswordController) {
        this.changePasswordController = changePasswordController;
    }

    public void setLogoutController(LogoutController logoutController) {
        this.logoutController = logoutController;
    }

    public void setViewHistoryController(ViewHistoryController viewHistoryController) {
        this.viewHistoryController = viewHistoryController;
    }

    public void setFindFlightController(FindFlightController findFlightController) {
        this.findFlightController = findFlightController;
    }

    public void setSeeSavedFlightsController(SeeSavedFlightsController controller) {
        this.seeSavedFlightsController = controller;
    }

}

