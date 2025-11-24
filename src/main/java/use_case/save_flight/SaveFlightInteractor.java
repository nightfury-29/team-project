package use_case.save_flight;

import entity.FlightDetail;
import data_access.FileUserDataAccessObject; // assume this exists

public class SaveFlightInteractor implements SaveFlightInputBoundary {

    private final SaveFlightDataAccessInterface flightGateway;
    private final FileUserDataAccessObject userGateway;
    private final SaveFlightOutputBoundary presenter;

    public SaveFlightInteractor(
            SaveFlightDataAccessInterface flightGateway,
            FileUserDataAccessObject userGateway,
            SaveFlightOutputBoundary presenter) {
        this.flightGateway = flightGateway;
        this.userGateway = userGateway;
        this.presenter = presenter;
    }

    @Override
    public void execute(SaveFlightInputData inputData) {
        if (inputData == null || inputData.getFlightDetail() == null) {
            presenter.prepareFailView(new SaveFlightOutputData(false, "No flight selected to save."));
            return;
        }

        FlightDetail detail = inputData.getFlightDetail();
        String flightId = inputData.getFlightId();

        if (flightId == null || flightId.isEmpty()) {
            presenter.prepareFailView(new SaveFlightOutputData(false, "Flight has no ID; cannot save."));
            return;
        }

        String username = userGateway.getCurrentUsername();
        if (username == null || username.isEmpty()) {
            presenter.prepareFailView(new SaveFlightOutputData(false, "No logged-in user found."));
            return;
        }

        try {
            if (flightGateway.flightExistsForUser(username, flightId)) {
                presenter.prepareFailView(new SaveFlightOutputData(false, "This flight is already saved."));
                return;
            }

            flightGateway.saveFlightForUser(username, detail);

            presenter.prepareSuccessView(
                    new SaveFlightOutputData(true, "Flight saved successfully!", detail)
            );
        } catch (Exception e) {
            e.printStackTrace();
            presenter.prepareFailView(new SaveFlightOutputData(false, "Failed to save flight: " + e.getMessage()));
        }
    }
}

