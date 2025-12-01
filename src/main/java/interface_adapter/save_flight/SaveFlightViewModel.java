package interface_adapter.save_flight;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * ViewModel for the Save Flight feature.
 * <p>
 * This ViewModel stores the state related to the result of saving a flight,
 * including the status message, success indicator, and whether the flight
 * has already been saved. It also manages property change notifications
 * so that the UI can react to updates.
 * </p>
 */
public class SaveFlightViewModel {

    /**
     * The name used by the ViewManagerModel to identify this view.
     */
    private static final String VIEW_NAME = "save flight";

    /**
     * Support object for managing and notifying {@link PropertyChangeListener}s.
     */
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    /**
     * The message to be displayed after attempting to save a flight.
     */
    private String message;

    /**
     * Indicates whether the save operation was successful.
     */
    private boolean success;

    /**
     * Indicates whether the selected flight was already saved.
     */
    private boolean alreadySaved;

    /**
     * Returns the name of the save flight view.
     *
     * @return the view name as a string
     */
    public static String getViewName() {
        return VIEW_NAME;
    }

    /**
     * Sets the result message for the save flight operation.
     *
     * @param message the message to store
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Returns the message associated with the save flight result.
     *
     * @return the message text
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets whether the save operation succeeded.
     *
     * @param success {@code true} if the save was successful; {@code false} otherwise
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Sets whether the flight has already been saved.
     *
     * @param alreadySaved {@code true} if the flight was previously saved
     */
    public void setAlreadySaved(boolean alreadySaved) {
        this.alreadySaved = alreadySaved;
    }

    /**
     * Adds a {@link PropertyChangeListener} to observe changes in this ViewModel.
     *
     * @param listener the listener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Fires a property change event indicating that save-flight-related state
     * has been updated. UI components listening for the {@code "saveFlightResult"}
     * event will be notified.
     */
    public void firePropertyChanged() {
        support.firePropertyChange("saveFlightResult", null, null);
    }
}
