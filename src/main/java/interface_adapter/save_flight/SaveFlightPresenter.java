package interface_adapter.save_flight;

import javax.swing.JOptionPane;

import interface_adapter.ViewManagerModel;
import use_case.save_flight.SaveFlightOutputBoundary;
import use_case.save_flight.SaveFlightOutputData;

/**
 * Presenter for the Save Flight use case.
 * <p>
 * This presenter receives output data from the interactor and updates the
 * {@link SaveFlightViewModel}. It also interacts with the {@link ViewManagerModel}
 * to trigger view changes when necessary. In case of success, it updates the UI
 * through ViewModel notifications. In case of failure, it displays an error dialog.
 * </p>
 */
public class SaveFlightPresenter implements SaveFlightOutputBoundary {

    /**
     * The ViewModel associated with saving a flight.
     */
    private final SaveFlightViewModel saveFlightViewModel;

    /**
     * The global view manager responsible for switching between screens.
     */
    private final ViewManagerModel viewManagerModel;

    /**
     * Constructs a {@code SaveFlightPresenter} with the specified ViewModel and view manager.
     *
     * @param saveFlightViewModel the ViewModel storing the save-flight state
     * @param viewManagerModel    the ViewManagerModel used to manage screen transitions
     */
    public SaveFlightPresenter(SaveFlightViewModel saveFlightViewModel, ViewManagerModel viewManagerModel) {
        this.saveFlightViewModel = saveFlightViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    /**
     * Prepares the success view when a flight has been successfully saved.
     * <p>
     * This method updates the ViewModel with the success message, triggers a property
     * change event, and instructs the view manager to switch to the save-flight view.
     * </p>
     *
     * @param outputData the output data containing the success message
     */
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

    /**
     * Prepares the failure view when saving a flight fails.
     * <p>
     * This method displays an error dialog with the message contained in the output data.
     * </p>
     *
     * @param outputData the output data containing the error message
     */
    @Override
    public void prepareFailView(SaveFlightOutputData outputData) {
        JOptionPane.showMessageDialog(null, outputData.getMessage(),
                "ERROR: Unable to Save flight", JOptionPane.ERROR_MESSAGE);
    }
}
