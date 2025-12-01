package use_case.save_flight;

import data_transfer_objects.DTOToFlightDetailMapper;
import data_transfer_objects.FlightDetailDataTransferObject;
import data_transfer_objects.FlightDetailToDTOMapper;
import entity.FlightDetail;
import use_case.logout.LogoutUserDataAccessInterface;

public class SaveFlightInteractor implements SaveFlightInputBoundary {

    private final SaveFlightDataAccessInterface flightGateway;
    private final LogoutUserDataAccessInterface userGateway;
    private final SaveFlightOutputBoundary presenter;

    public SaveFlightInteractor(
            SaveFlightDataAccessInterface flightGateway,
            LogoutUserDataAccessInterface userGateway,
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

        final FlightDetailDataTransferObject detaildto = inputData.getFlightDetail();
        final DTOToFlightDetailMapper mapper = new DTOToFlightDetailMapper();

        final FlightDetail detail = mapper.map(detaildto);

        final String flightId = inputData.getFlightId();

        if (flightId == null || flightId.isEmpty()) {
            presenter.prepareFailView(new SaveFlightOutputData(false, "Flight has no ID; cannot save."));
            return;
        }

        final String username = userGateway.getCurrentUsername();
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

            final FlightDetailToDTOMapper mapperdtotemp = new FlightDetailToDTOMapper();
            final FlightDetailDataTransferObject detailDto = mapperdtotemp.map(detail);

            presenter.prepareSuccessView(
                    new SaveFlightOutputData(true, "Flight saved successfully!", detailDto)
            );
        }
        
        catch (Exception ex) {
            ex.printStackTrace();
            presenter.prepareFailView(new SaveFlightOutputData(false, "Failed to save flight: " + ex.getMessage()));
        }
    }
}

