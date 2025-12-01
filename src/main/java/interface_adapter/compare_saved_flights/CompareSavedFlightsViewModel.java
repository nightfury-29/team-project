package interface_adapter.compare_saved_flights;

import interface_adapter.ViewModel;

/**
 * ViewModel for comparing two saved flights.
 */
public class CompareSavedFlightsViewModel extends ViewModel<CompareSavedFlightsState> {

    /**
     * Constructs a view model for the compare flights view.
     */
    public CompareSavedFlightsViewModel() {
        super("compare flights");
        setState(new CompareSavedFlightsState());
    }
}

