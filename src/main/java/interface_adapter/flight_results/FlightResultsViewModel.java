package interface_adapter.flight_results;

import interface_adapter.ViewModel;

public class FlightResultsViewModel extends ViewModel<FlightResultsState> {

    public static final String TITLE_LABEL = "Flight Results";
    public static final String SORT_BY_PRICE_BUTTON_LABEL = "Sort by Price";
    public static final String SORT_BY_DURATION_BUTTON_LABEL = "Sort by Duration";
    public static final String NONSTOP_BUTTON_LABEL = "Non-stop";
    public static final String SHOW_ALL_FLIGHTS_BUTTON_LABEL = "Show All Flights";
    public static final String GO_BACK_BUTTON_LABEL = "Go Back";

    public FlightResultsViewModel() {
        super("flight results");
        setState(new FlightResultsState());
    }
}
