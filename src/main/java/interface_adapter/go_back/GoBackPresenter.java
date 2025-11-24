package interface_adapter.go_back;

import interface_adapter.ViewManagerModel;
import use_case.go_back.GoBackOutputBoundary;

public class GoBackPresenter implements GoBackOutputBoundary {
    private final ViewManagerModel viewManagerModel;

    public GoBackPresenter(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(String viewName) {
        // This is where the UI state actually updates
        viewManagerModel.setState(viewName);
        viewManagerModel.firePropertyChange();
    }
}
