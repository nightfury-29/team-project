package interface_adapter.go_back;

import use_case.go_back.GoBackInputBoundary;

public class GoBackController {

    private final GoBackInputBoundary interactor;

    // The constructor must accept the InputBoundary, NOT the ViewManagerModel
    public GoBackController(GoBackInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String viewName) {
        System.out.println("[GoBackController] execute called destination = " + viewName);
        interactor.execute(viewName);
    }
}

