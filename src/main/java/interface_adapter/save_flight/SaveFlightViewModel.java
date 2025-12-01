package interface_adapter.save_flight;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class SaveFlightViewModel {

    private static final String VIEW_NAME = "save flight";
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    private String message;
    private boolean success;
    private boolean alreadySaved;

    public static String getViewName() {
        return VIEW_NAME;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setAlreadySaved(boolean alreadySaved) {
        this.alreadySaved = alreadySaved;
    }

    /**
     * Adds a listener to be notified when properties change.
     *
     * @param listener the PropertyChangeListener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Fires a property change event to notify listeners
     * that the save flight result has changed.
     */
    public void firePropertyChanged() {
        support.firePropertyChange("saveFlightResult", null, null);
    }
}

