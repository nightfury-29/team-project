package use_case.load_history;

/**
 * Input Boundary for the Load History use case.
 */


public interface LoadHistoryInputBoundary {

    /**
     * Executes the Load History use case. After this executes,
     * the user will be taken back to the Logged In View with their
     * past search loaded into the input fields.
     */

    void execute(LoadHistoryInputData loadHistoryInputData);

    /**
     *  Adds functionality to the Go Back button. Takes the user
     *  back to the Logged In View.
     */

    void switchToLoggedInView();
}
