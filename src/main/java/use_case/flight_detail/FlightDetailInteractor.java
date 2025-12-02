package use_case.flight_detail;

import entity.Flight;
import entity.FlightDetail;
import data_transfer_objects.FlightDetailDataTransferObject;
import data_transfer_objects.DTOToFlightDetailMapper;
import data_transfer_objects.FlightDetailToDTOMapper;

public class FlightDetailInteractor implements FlightDetailInputBoundary {

    private final FlightDetailDataAccessInterface flightDetailDataAccessObject;
    private final FlightDetailOutputBoundary flightDetailPresenter;

    public FlightDetailInteractor(FlightDetailDataAccessInterface flightDetailDataAccessObject,
                                  FlightDetailOutputBoundary flightDetailPresenter) {
        this.flightDetailDataAccessObject = flightDetailDataAccessObject;
        this.flightDetailPresenter = flightDetailPresenter;
    }

    @Override
    public void execute(FlightDetailInputData inputData) {
//        System.out.println("[DEBUG] FlightDetailInteractor reached.");

        try {

            String flightId = inputData.getFlightId();

            if (flightId == null) {
                flightDetailPresenter.prepareFailView("Error: Flight is null.");
                return;
            }

            // --- Call data source to get the full flight detail ---
            FlightDetail detail = flightDetailDataAccessObject.fetchDetail(flightId);

            if (detail == null) {
                flightDetailPresenter.prepareFailView("Could not retrieve flight details.");
                return;
            }

            FlightDetailToDTOMapper mapper = new FlightDetailToDTOMapper();
            FlightDetailDataTransferObject outputDataDetail = mapper.map(detail);

            FlightDetailOutputData outputData = new FlightDetailOutputData(outputDataDetail);
            flightDetailPresenter.prepareSuccessView(outputData);

        } catch (Exception e) {
            flightDetailPresenter.prepareFailView("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
