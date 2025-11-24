package interface_adapter.save_flight;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class SaveFlightViewModel {

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    private String message;
    private boolean success;
    private boolean alreadySaved;

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

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void firePropertyChanged() {
        support.firePropertyChange("saveFlightResult", null, null);
    }
}


