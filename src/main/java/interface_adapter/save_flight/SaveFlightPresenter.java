package interface_adapter.save_flight;

import javax.swing.JOptionPane;

import interface_adapter.ViewManagerModel;
import use_case.save_flight.SaveFlightOutputBoundary;
import use_case.save_flight.SaveFlightOutputData;

public class SaveFlightPresenter implements SaveFlightOutputBoundary {

    private final SaveFlightViewModel saveFlightViewModel;
    private final ViewManagerModel viewManagerModel;

    public SaveFlightPresenter(SaveFlightViewModel saveFlightViewModel, ViewManagerModel viewManagerModel) {
        this.saveFlightViewModel = saveFlightViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(SaveFlightOutputData outputData) {
        if (saveFlightViewModel != null) {
            saveFlightViewModel.setMessage(outputData.getMessage());
            saveFlightViewModel.firePropertyChanged();

            // Update the ViewManagerModel state
            viewManagerModel.setState(SaveFlightViewModel.getViewName());

            viewManagerModel.firePropertyChange("state");
        }
    }

    @Override
    public void prepareFailView(SaveFlightOutputData outputData) {
        JOptionPane.showMessageDialog(null, outputData.getMessage(),
                "ERROR: Unable to Save flight", JOptionPane.ERROR_MESSAGE);
    }
}
