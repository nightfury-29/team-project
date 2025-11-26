package use_case.load_history;

import use_case.view_history.ViewHistoryOutputData;

/**
 * The output boundary for the Load History Use Case.
 */

public interface LoadHistoryOutputBoundary {

    /**
     * Prepares the success view for the Load History Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(LoadHistoryOutputData outputData);

    /**
     * Prepares the failure view for the Load History Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);

    /**
     * Switches to the Logged In View
     */

    void switchToLoggedInView();
}
