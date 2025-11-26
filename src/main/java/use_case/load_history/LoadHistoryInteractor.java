package use_case.load_history;

/**
 * The Load History Interactor.
 */


public class LoadHistoryInteractor implements LoadHistoryInputBoundary{

    private final LoadHistoryOutputBoundary loadHistoryPresenter;

    public  LoadHistoryInteractor(LoadHistoryOutputBoundary loadHistoryPresenter) {
        this.loadHistoryPresenter = loadHistoryPresenter;
    }

    @Override
    public void execute(LoadHistoryInputData loadHistoryInputData) {
        final String from =  loadHistoryInputData.getFrom();
        final String to = loadHistoryInputData.getTo();
        final String day =  loadHistoryInputData.getDay();
        final String month =  loadHistoryInputData.getMonth();
        final String year =  loadHistoryInputData.getYear();


        if (from == null) {
            loadHistoryPresenter.prepareFailView("Please select an entry from the table.");
        }

        else {
            final LoadHistoryOutputData loadHistoryOutputData = new LoadHistoryOutputData(from, to, day, month, year);
            loadHistoryPresenter.prepareSuccessView(loadHistoryOutputData);
        }

    }

    @Override
    public void switchToLoggedInView() {
        loadHistoryPresenter.switchToLoggedInView();
    }
}
