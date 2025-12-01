package use_case.load_history;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The test suite for the Load History Interactor
 */

class LoadHistoryInteractorTest {

    @Test
    void successTest() {
        LoadHistoryInputData inputData = new LoadHistoryInputData("Toronto", "Port Of Spain", "14", "December", "2025");

        // create a success presenter to test if the interactor functioned as expected
        LoadHistoryOutputBoundary successPresenter = new LoadHistoryOutputBoundary() {
            @Override
            public void prepareSuccessView(LoadHistoryOutputData outputData) {
                // we need to verify that the interactor packaged the relevant data correctly
                assertEquals("Toronto", outputData.getFrom());
                assertEquals("Port Of Spain", outputData.getTo());
                assertEquals("14", outputData.getDay());
                assertEquals("December", outputData.getMonth());
                assertEquals("2025", outputData.getYear());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                // this should never be reached since the test case should pass
                fail("Use case failure is unexpected.");
            }

            @Override
            public void switchToLoggedInView() {
                // we can ignore this since this method is explicitly used for switching views
            }
        };

        LoadHistoryInputBoundary interactor = new LoadHistoryInteractor(successPresenter);
        interactor.execute(inputData);
    }

    @Test
    void failureTest() {
        // for the failure test, there is no selected entry
        LoadHistoryInputData inputData = new LoadHistoryInputData(null, null, null, null, null);

        // create a failure presenter to test if the interactor functioned as expected
        LoadHistoryOutputBoundary failurePresenter = new LoadHistoryOutputBoundary() {
            @Override
            public void prepareSuccessView(LoadHistoryOutputData outputData) {
                // this should never be reached since the test case should fail
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                // verify that the correct error message sent to the presenter
                String expectedMessage = "Please select an entry from the table.";
                assertEquals(expectedMessage, errorMessage);
            }

            @Override
            public void switchToLoggedInView() {
                // we can ignore this since this method is explicitly used for switching views
            }
        };
        LoadHistoryInputBoundary interactor = new LoadHistoryInteractor(failurePresenter);
        interactor.execute(inputData);
    }

    @Test
    void switchViewTest() {
        // for this test we simply need to check that the correct method in the presenter gets called

        // create a simple presenter to test if the interactor functioned as expected
        LoadHistoryOutputBoundary simplePresenter = new LoadHistoryOutputBoundary() {
            @Override
            public void prepareSuccessView(LoadHistoryOutputData outputData) {
                // this should never be reached
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                // this should never be reached
                fail("Use case failure is unexpected.");
            }

            @Override
            public void switchToLoggedInView() {
                // this method should be reached
                assertTrue(true);
            }
        };

        LoadHistoryInputBoundary interactor = new LoadHistoryInteractor(simplePresenter);
        interactor.switchToLoggedInView();
    }
}
