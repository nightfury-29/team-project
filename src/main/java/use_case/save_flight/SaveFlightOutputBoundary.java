package use_case.save_flight;

/**
 * The output boundary for the Save Flight Use Case.
 */
public interface SaveFlightOutputBoundary {
    /**
     * Prepares the success view for the Signup Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(SaveFlightOutputData outputData);

    /**
     * Prepares the failure view for the Signup Use Case.
     * @param outputData the output data we get back
     */
    void prepareFailView(SaveFlightOutputData outputData);
}
