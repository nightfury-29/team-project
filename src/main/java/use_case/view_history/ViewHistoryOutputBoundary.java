package use_case.view_history;

/**
 * The output boundary for the View History Use Case.
 */

public interface ViewHistoryOutputBoundary {

    /**
     * Prepares the success view for the View History Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(ViewHistoryOutputData outputData);

    /**
     * Prepares the failure view for the View History Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
