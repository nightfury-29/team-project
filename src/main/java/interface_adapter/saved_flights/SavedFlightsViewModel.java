package interface_adapter.saved_flights;

import entity.FlightDetail;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

public class SavedFlightsViewModel {

    private List<FlightDetail> flights;

    private final String viewName = "saved flights";

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public String getViewName() {
        return viewName;
    }

    public void setFlights(List<FlightDetail> flights) {
        this.flights = flights;
    }

    public List<FlightDetail> getFlights() {
        return flights;
    }

    // observer attach
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    // notify view
    public void firePropertyChanged() {
        support.firePropertyChange("savedFlights", null, this.flights);
    }
}
