package interface_adapter.viewing_history;

import use_case.load_history.LoadHistoryInputBoundary;
import use_case.load_history.LoadHistoryInputData;

/**
 * Controller for the Load History Use Case.
 */

public class LoadHistoryController {

    private final LoadHistoryInputBoundary loadHistoryInteractor;

    public LoadHistoryController(LoadHistoryInputBoundary loadHistoryInteractor) {
        this.loadHistoryInteractor = loadHistoryInteractor;
    }

    /**
     * Executes the Load History Use Case.
     */

    public void execute(String from, String to, String day, String month, String year) {
        final LoadHistoryInputData loadHistoryInputData = new LoadHistoryInputData(from, to, day, month, year);
        this.loadHistoryInteractor.execute(loadHistoryInputData);
    }

    /**
     * Executes the Switch to Logged In View Use Case.
     */

    public void switchToLoggedInView() {
        this.loadHistoryInteractor.switchToLoggedInView();
    }
}
