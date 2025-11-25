package use_case.saved_flights;

import entity.FlightDetail;
import use_case.save_flight.SaveFlightDataAccessInterface;
import java.util.List;

public class SavedFlightsInteractor implements SavedFlightsInputBoundary {

    private final SaveFlightDataAccessInterface flightGateway;
    private final SavedFlightsOutputBoundary presenter;

    public SavedFlightsInteractor(SaveFlightDataAccessInterface flightGateway,
                                  SavedFlightsOutputBoundary presenter) {
        this.flightGateway = flightGateway;
        this.presenter = presenter;
    }

    @Override
    public void execute(SavedFlightsInputData data) {
        System.out.println("[Interactor] execute called, user = " + data.getUsername());

        List<FlightDetail> flights = flightGateway.getSavedFlights(data.getUsername());
        System.out.println("[Interactor] flights loaded size = " + flights.size());

        presenter.present(new SavedFlightsOutputData(flights));
        System.out.println("[Interactor] presenter.present() called");
    }

}
