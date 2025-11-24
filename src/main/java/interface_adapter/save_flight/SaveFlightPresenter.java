package interface_adapter.save_flight;

import use_case.save_flight.SaveFlightOutputBoundary;
import use_case.save_flight.SaveFlightOutputData;
import view.FlightDetailView;

public class SaveFlightPresenter implements SaveFlightOutputBoundary {

    private final FlightDetailView view;

    public SaveFlightPresenter(FlightDetailView view) {
        this.view = view;
    }

    @Override
    public void prepareSuccessView(SaveFlightOutputData outputData) {
        if (view != null) {
            view.showMessage(outputData.getMessage());
        }
    }

    @Override
    public void prepareFailView(SaveFlightOutputData outputData) {
        if (view != null) {
            view.showError(outputData.getMessage());
        }
    }
}
