package use_case.flight_detail;

import data_transfer_objects.FlightDetailDataTransferObject;
import data_transfer_objects.FlightDetailToDTOMapper;
import entity.FlightDetail;

/**
 * Interactor for the "view flight detail" use case.
 *
 * This class implements the application business rules for retrieving the
 * full flight detail based on a flight ID. It receives the user input
 * (a flight ID), requests the corresponding data from the data access layer,
 * maps the entity into a Data Transfer Object (DTO), and passes the result to
 * the presenter for preparing the appropriate view.
 *
 * The interactor follows the Clean Architecture rule that all business logic
 * remains independent of UI and frameworks, relying only on input/output
 * boundaries and pure data structures.
 */
public class FlightDetailInteractor implements FlightDetailInputBoundary {

    private final FlightDetailDataAccessInterface flightDetailDataAccessObject;
    private final FlightDetailOutputBoundary flightDetailPresenter;

    public FlightDetailInteractor(FlightDetailDataAccessInterface flightDetailDataAccessObject,
                                  FlightDetailOutputBoundary flightDetailPresenter) {
        this.flightDetailDataAccessObject = flightDetailDataAccessObject;
        this.flightDetailPresenter = flightDetailPresenter;
    }

    /**
     * Executes the "view flight detail" use case.
     *
     * The method follows these steps:
     *     Validate that the input flight ID is not null.
     *     Request the full {@link FlightDetail} from the data access layer.
     *     If the detail cannot be retrieved (null), prepare a failure response.
     *     Map the entity into a {@link FlightDetailDataTransferObject}.
     *     Wrap it into output data and delegate to the presenter to prepare the success view.
     *
     * If any unexpected exception occurs during execution, the method catches it
     * and triggers prepareFailView with an error message.
     *
     * @param inputData the input data containing the flight ID
     */
    @Override
    public void execute(FlightDetailInputData inputData) {
//        System.out.println("[DEBUG] FlightDetailInteractor reached.");

        try {

            final String flightId = inputData.getFlightId();

            if (flightId == null) {
                flightDetailPresenter.prepareFailView("Error: Flight is null.");
                return;
            }

            // --- Call data source to get the full flight detail ---
            final FlightDetail detail = flightDetailDataAccessObject.fetchDetail(flightId);

            if (detail == null) {
                flightDetailPresenter.prepareFailView("Could not retrieve flight details.");
                return;
            }

            final FlightDetailToDTOMapper mapper = new FlightDetailToDTOMapper();
            final FlightDetailDataTransferObject outputDataDetail = mapper.map(detail);

            final FlightDetailOutputData outputData = new FlightDetailOutputData(outputDataDetail);
            flightDetailPresenter.prepareSuccessView(outputData);

        }
        catch (Exception e) {
            flightDetailPresenter.prepareFailView("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
