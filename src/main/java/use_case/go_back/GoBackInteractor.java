package use_case.go_back;

public class GoBackInteractor implements GoBackInputBoundary {
    private final GoBackOutputBoundary presenter;

    public GoBackInteractor(GoBackOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(String viewName) {
        // In a complex app, you might check if navigation is allowed here.
        // For now, we just pass the request to the presenter.
        presenter.prepareSuccessView(viewName);
    }
}
