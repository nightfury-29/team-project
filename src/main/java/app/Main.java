package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addLoginView()
                .addSignupView()
                .addLoggedInView()
                .addFlightResultsView()
                .addFlightDetailView()
                .addViewingHistoryView()
                .addSavedFlightsView()
                .addCompareSavedFlightsView()
                .addSignupUseCase()
                .addLoginUseCase()
                .addChangePasswordUseCase()
                .addLogoutUseCase()
                .addFindFlightUseCase()
                .addSortFlightsUseCase()
                .addFlightDetailUseCase()
                .addSaveFlightUseCase()
                .addViewHistoryUseCase()
                .addLoadHistoryUseCase()
                .addSavedFlightsUseCase()
                .addCompareSavedFlightsUseCase()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
