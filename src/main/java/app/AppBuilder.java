package app;

import data_access.*;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.flight_detail.FlightDetailViewModel;
import interface_adapter.logged_in.ChangePasswordController;
import interface_adapter.logged_in.ChangePasswordPresenter;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.view_history.ViewHistoryController;
import interface_adapter.view_history.ViewHistoryPresenter;
import interface_adapter.viewing_history.LoadHistoryController;
import interface_adapter.viewing_history.LoadHistoryPresenter;
import interface_adapter.viewing_history.ViewingHistoryViewModel;
import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordInteractor;
import use_case.change_password.ChangePasswordOutputBoundary;
import use_case.flight_detail.FlightDetailDataAccessInterface;
import use_case.load_history.LoadHistoryInputBoundary;
import use_case.load_history.LoadHistoryInteractor;
import use_case.load_history.LoadHistoryOutputBoundary;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
import use_case.save_flight.SaveFlightDataAccessInterface;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import use_case.view_history.ViewHistoryInputBoundary;
import use_case.view_history.ViewHistoryInteractor;
import use_case.view_history.ViewHistoryOutputBoundary;
import view.*;

import data_access.FileSearchHistoryDAO;
import data_access.InMemoryFlightDataAccessObject;
import use_case.sort_flights.SortFlightsDataAccessInterface;
import view.LoggedInView;
import view.LoginView;
import view.SignupView;
import view.ViewManager;

import helpers.CityCodeConverter;
import helpers.SearchInfoVerifier;
import interface_adapter.flight_results.FlightResultsViewModel;
import interface_adapter.flight_results.FindFlightPresenter;
import interface_adapter.logged_in.FindFlightController;
import use_case.find_flight.*;
import interface_adapter.go_back.GoBackController;

import interface_adapter.sort_flights.SortFlightsController;
import interface_adapter.sort_flights.SortFlightsPresenter;
import use_case.sort_flights.SortFlightsInputBoundary;
import use_case.sort_flights.SortFlightsInteractor;
import use_case.sort_flights.SortFlightsOutputBoundary;

import interface_adapter.flight_detail.FlightDetailFacade;
import interface_adapter.flight_detail.FlightDetailController;
import interface_adapter.flight_detail.FlightDetailPresenter;
import use_case.flight_detail.FlightDetailInputBoundary;
import use_case.flight_detail.FlightDetailInteractor;

import interface_adapter.go_back.GoBackPresenter;
import use_case.go_back.GoBackInputBoundary;
import use_case.go_back.GoBackInteractor;
import use_case.go_back.GoBackOutputBoundary;

import data_access.SaveFlightDataAccessObject;
import interface_adapter.save_flight.SaveFlightController;
import interface_adapter.save_flight.SaveFlightPresenter;
import interface_adapter.save_flight.SaveFlightViewModel;
import use_case.save_flight.SaveFlightInputBoundary;
import use_case.save_flight.SaveFlightInteractor;



import use_case.saved_flights.SavedFlightsInputBoundary;
import use_case.saved_flights.SavedFlightsInteractor;
import use_case.saved_flights.SavedFlightsOutputBoundary;

import interface_adapter.saved_flights.SavedFlightsPresenter;
import interface_adapter.saved_flights.SavedFlightsViewModel;
import interface_adapter.saved_flights.SeeSavedFlightsController;

import view.SavedFlightsView;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final UserFactory userFactory = new UserFactory();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    // set which data access implementation to use, can be any
    // of the classes from the data_access package

    // DAO version using local file storage
    final FileUserDataAccessObject userDataAccessObject = new FileUserDataAccessObject("users.csv", userFactory);
    final FileSearchHistoryDAO searchHistoryDAO = new FileSearchHistoryDAO("search_history.csv");

    // DAO version using a shared external database
    // final DBUserDataAccessObject userDataAccessObject = new DBUserDataAccessObject(userFactory);

    private SignupView signupView;
    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private LoggedInViewModel loggedInViewModel;
    private LoggedInView loggedInView;
    private LoginView loginView;
    private FlightResultsViewModel flightResultsViewModel;
    private FlightResultsView flightResultsView;
    private FlightDetailViewModel flightDetailViewModel;
    private FlightDetailView flightDetailView;
    private SaveFlightViewModel saveFlightViewModel;
    private ViewingHistoryView viewingHistoryView;
    private ViewingHistoryViewModel viewingHistoryViewModel;
    private SavedFlightsViewModel savedFlightsViewModel;
    private SavedFlightsView savedFlightsView;


    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addSignupView() {
        signupViewModel = new SignupViewModel();
        signupView = new SignupView(signupViewModel);
        cardPanel.add(signupView, signupView.getViewName());
        return this;
    }

    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel);
        cardPanel.add(loginView, loginView.getViewName());
        return this;
    }

    public AppBuilder addLoggedInView() {
        loggedInViewModel = new LoggedInViewModel();
        loggedInView = new LoggedInView(loggedInViewModel);
        cardPanel.add(loggedInView, loggedInView.getViewName());
        return this;
    }

    public AppBuilder addSignupUseCase() {
        final SignupOutputBoundary signupOutputBoundary = new SignupPresenter(viewManagerModel,
                signupViewModel, loginViewModel);
        final SignupInputBoundary userSignupInteractor = new SignupInteractor(
                userDataAccessObject, signupOutputBoundary, userFactory);

        SignupController controller = new SignupController(userSignupInteractor);
        signupView.setSignupController(controller);
        return this;
    }

    public AppBuilder addLoginUseCase() {
        final LoginOutputBoundary loginOutputBoundary = new LoginPresenter(viewManagerModel,
                loggedInViewModel, loginViewModel, signupViewModel);
        final LoginInputBoundary loginInteractor = new LoginInteractor(
                userDataAccessObject, loginOutputBoundary);

        LoginController loginController = new LoginController(loginInteractor);
        loginView.setLoginController(loginController);
        return this;
    }

    public AppBuilder addChangePasswordUseCase() {
        final ChangePasswordOutputBoundary changePasswordOutputBoundary = new ChangePasswordPresenter(viewManagerModel,
                loggedInViewModel);

        final ChangePasswordInputBoundary changePasswordInteractor =
                new ChangePasswordInteractor(userDataAccessObject, changePasswordOutputBoundary, userFactory);

        ChangePasswordController changePasswordController = new ChangePasswordController(changePasswordInteractor);
        loggedInView.setChangePasswordController(changePasswordController);
        return this;
    }

    /**
     * Adds the Logout Use Case to the application.
     * @return this builder
     */
    public AppBuilder addLogoutUseCase() {
        final LogoutOutputBoundary logoutOutputBoundary = new LogoutPresenter(viewManagerModel,
                loggedInViewModel, loginViewModel);

        final LogoutInputBoundary logoutInteractor =
                new LogoutInteractor(userDataAccessObject, logoutOutputBoundary);

        final LogoutController logoutController = new LogoutController(logoutInteractor);
        loggedInView.setLogoutController(logoutController);
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("User Login Example");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(signupView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }

    public AppBuilder addFlightResultsView() {
        flightResultsViewModel = new FlightResultsViewModel();
        flightResultsView = new FlightResultsView(flightResultsViewModel);
        cardPanel.add(flightResultsView, flightResultsView.viewName);

        // 1. Create the Presenter (connects to ViewManagerModel)
        GoBackOutputBoundary goBackPresenter = new GoBackPresenter(viewManagerModel);

        // 2. Create the Interactor (connects to Presenter)
        GoBackInputBoundary goBackInteractor = new GoBackInteractor(goBackPresenter);

        // 3. Create the Controller (connects to Interactor)
        GoBackController goBackController = new GoBackController(goBackInteractor);

        // 4. Set the controller in the view
        flightResultsView.setGoBackController(goBackController);


        return this;
    }

    // In app/AppBuilder.java
    public AppBuilder addFindFlightUseCase() {
        // --- Initialize DAOs ---
        // (We use InMemory for this example, but you could swap it)
        FindFlightUserDataAccessInterface flightDataAccessObject = new InMemoryFlightDataAccessObject();


        // --- Initialize Helpers ---
        CityCodeConverter cityCodeConverter = new CityCodeConverter();
        SearchInfoVerifier searchInfoVerifier = new SearchInfoVerifier();

        // --- Initialize Presenter ---
        FindFlightOutputBoundary findFlightPresenter = new FindFlightPresenter(
                this.flightResultsViewModel,
                this.loggedInViewModel,
                this.viewManagerModel
        );

        // --- Initialize Interactor ---
        FindFlightInputBoundary findFlightInteractor = new FindFlightInteractor(
                searchInfoVerifier,
                findFlightPresenter,
                searchHistoryDAO,
                cityCodeConverter,
                flightDataAccessObject
        );

        // --- Initialize Controller ---
        FindFlightController findFlightController = new FindFlightController(findFlightInteractor);

        // --- Set Controller on the View ---
        this.loggedInView.setFindFlightController(findFlightController);

        return this;
    }

    public AppBuilder addSortFlightsUseCase() {
        // --- Initialize Presenter ---
        SortFlightsOutputBoundary sortFlightsPresenter = new SortFlightsPresenter(flightResultsViewModel);


        // 1. Create DAO
        SortFlightsDataAccessInterface sortDAO = new InMemorySortSettingsDAO(); // Or null if you prefer

        // 2. Pass DAO to Interactor
        SortFlightsInputBoundary sortFlightsInteractor = new SortFlightsInteractor(sortFlightsPresenter, sortDAO);

        // --- Initialize Controller ---
        SortFlightsController sortFlightsController = new SortFlightsController(sortFlightsInteractor);

        // --- Set Controller on the View ---
        this.flightResultsView.setSortFlightsController(sortFlightsController);

        return this;
    }

    public AppBuilder addFlightDetailView() {
        this.flightDetailViewModel = new FlightDetailViewModel();
        this.saveFlightViewModel = new SaveFlightViewModel();

        this.flightDetailView = new FlightDetailView(flightDetailViewModel, saveFlightViewModel);

        cardPanel.add(flightDetailView, flightDetailView.getViewName());
        return this;
    }

    public AppBuilder addFlightDetailUseCase() {

        // Flight Detail:
        final FlightDetailDataAccessInterface flightDetailDataAccessObject = new FlightDetailDataAccessObject();

        final FlightDetailPresenter presenter = new FlightDetailPresenter(flightDetailViewModel,
                flightResultsViewModel,
                viewManagerModel);

        final FlightDetailInputBoundary flightDetailInteractor = new FlightDetailInteractor(flightDetailDataAccessObject,presenter);

        final FlightDetailController controller =
                new FlightDetailController(flightDetailInteractor);

        final FlightDetailFacade flightDetailFacade =
                new FlightDetailFacade(
                        controller,
                        presenter,
                        flightDetailViewModel,
                        viewManagerModel
                );

        this.flightDetailView.setFacade(flightDetailFacade);
        if (this.flightResultsView != null) {
            this.flightResultsView.setFlightDetailFacade(flightDetailFacade);
        }

        if (this.flightResultsView != null) {
            this.flightResultsView.setFlightDetailController(controller);
        }

        // Go Back
        final GoBackOutputBoundary goBackPresenter = new GoBackPresenter(viewManagerModel);

        final GoBackInputBoundary goBackInteractor = new GoBackInteractor(goBackPresenter);

        final GoBackController goBackController = new GoBackController(goBackInteractor);

        flightDetailView.setGoBackController(goBackController);

        // Save Flight
        final SaveFlightDataAccessInterface saveFlightDataAccessObject = new SaveFlightDataAccessObject();

        final SaveFlightPresenter saveFlightPresenter = new SaveFlightPresenter(saveFlightViewModel, viewManagerModel);

        final SaveFlightInputBoundary saveFlightInteractor = new SaveFlightInteractor(saveFlightDataAccessObject, this.userDataAccessObject, saveFlightPresenter);

        final SaveFlightController saveFlightController = new SaveFlightController(saveFlightInteractor);

        flightDetailView.setSaveFlightController(saveFlightController);

        return this;
    }
    public AppBuilder addSavedFlightsView() {

        // 1. Create ViewModel
        this.savedFlightsViewModel = new SavedFlightsViewModel();

        // 2. Create Presenter
        SavedFlightsPresenter savedFlightsPresenter =
                new SavedFlightsPresenter(this.savedFlightsViewModel, viewManagerModel);

        // 3. Create Interactor
        SaveFlightDataAccessInterface dao = new SaveFlightDataAccessObject();
        SavedFlightsInteractor savedFlightsInteractor =
                new SavedFlightsInteractor(dao, savedFlightsPresenter);

        // 4. Create Controller
        SeeSavedFlightsController savedFlightsController =
                new SeeSavedFlightsController(savedFlightsInteractor);

        // 5. Register panel to CardLayout
        SavedFlightsView savedFlightsView =
                new SavedFlightsView(this.savedFlightsViewModel, this.viewManagerModel);

        cardPanel.add(savedFlightsView, this.savedFlightsViewModel.getViewName());

        return this;
    }




    public AppBuilder addSavedFlightsUseCase() {
        final SaveFlightDataAccessInterface savedFlightDAO = new SaveFlightDataAccessObject();

        final SavedFlightsOutputBoundary savedFlightsPresenter =
                new SavedFlightsPresenter(savedFlightsViewModel, viewManagerModel);

        final SavedFlightsInputBoundary savedFlightsInteractor =
                new SavedFlightsInteractor(savedFlightDAO, savedFlightsPresenter);

        final SeeSavedFlightsController savedFlightsController =
                new SeeSavedFlightsController(savedFlightsInteractor);

        loggedInView.setSeeSavedFlightsController(savedFlightsController);

        return this;
    }



    public AppBuilder addViewingHistoryView() {
        viewingHistoryViewModel = new ViewingHistoryViewModel();
        viewingHistoryView = new ViewingHistoryView(viewingHistoryViewModel);
        cardPanel.add(viewingHistoryView, viewingHistoryView.getViewName());
        return this;
    }

    public AppBuilder addViewHistoryUseCase() {

        final ViewHistoryOutputBoundary viewHistoryPresenter= new ViewHistoryPresenter(viewingHistoryViewModel, loggedInViewModel,  viewManagerModel);
        final ViewHistoryInputBoundary viewHistoryInteractor = new ViewHistoryInteractor(searchHistoryDAO,  viewHistoryPresenter);
        final ViewHistoryController viewHistoryController = new ViewHistoryController(viewHistoryInteractor);
        loggedInView.setViewHistoryController(viewHistoryController);

        return this;
    }

    public AppBuilder addLoadHistoryUseCase() {

        final LoadHistoryOutputBoundary loadHistoryPresenter = new LoadHistoryPresenter(viewingHistoryViewModel, loggedInViewModel, viewManagerModel);
        final LoadHistoryInputBoundary loadHistoryInteractor = new LoadHistoryInteractor(loadHistoryPresenter);
        final LoadHistoryController loadHistoryController = new LoadHistoryController(loadHistoryInteractor);
        viewingHistoryView.setLoadHistoryController(loadHistoryController);

        return this;
    }

}
