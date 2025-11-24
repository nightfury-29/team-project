package use_case.save_flight;

/**
 * The input boundary for the Save Flight Use Case.
 */
public interface SaveFlightInputBoundary {
    /**
     * Executes the login use case.
     *
     * @param saveFlightInputData the input data
     */
    void execute(SaveFlightInputData saveFlightInputData);
}
