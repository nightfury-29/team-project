package interface_adapter.compare_saved_flights;

import interface_adapter.ViewModel;

public class CompareSavedFlightsViewModel extends ViewModel<CompareSavedFlightsState> {

    public CompareSavedFlightsViewModel() {
        super("compare flights");
        setState(new CompareSavedFlightsState());
    }
}
