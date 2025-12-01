package interface_adapter.saved_flights;

import entity.FlightDetail;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

/**
 * ViewModel for the Saved Flights view.
 * <p>
 * This ViewModel stores the list of flights that a user has previously saved.
 * It provides property-change notification support so that the corresponding
 * view can update its UI whenever the list of saved flights is modified.
 * </p>
 */
public class SavedFlightsViewModel {

    /**
     * The list of saved flights retrieved for the current user.
     */
    private List<FlightDetail> flights;

    /**
     * The name of the view managed by the ViewManagerModel.
     */
    private final String viewName = "saved flights";

    /**
     * Support object for firing property change events to registered listeners.
     */
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    /**
     * Returns the name of the view that this ViewModel corresponds to.
     *
     * @return the view name as a string
     */
    public String getViewName() {
        return viewName;
    }

    /**
     * Sets the list of saved flights.
     *
     * @param flights the list of saved {@link FlightDetail} objects
     */
    public void setFlights(List<FlightDetail> flights) {
        this.flights = flights;
    }

    /**
     * Returns the current list of saved flights.
     *
     * @return the list of {@link FlightDetail} objects
     */
    public List<FlightDetail> getFlights() {
        return flights;
    }

    /**
     * Registers a listener to observe changes in the saved flights state.
     *
     * @param listener the {@link PropertyChangeListener} to add
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Notifies all registered listeners that the list of saved flights has changed.
     * <p>
     * This event uses the property name {@code "savedFlights"}.
     * </p>
     */
    public void firePropertyChanged() {
        support.firePropertyChange("savedFlights", null, this.flights);
    }
}
