package use_case.find_flight;

import data_access.InMemoryFlightDataAccessObject;
import helpers.SearchInfoVerifier;
import helpers.CityCodeConverter;
import entity.FlightSearchInformation;
import entity.Flight;

import java.util.List;

public class FindFlightInteractor implements FindFlightInputBoundary{

    private final SearchInfoVerifier searchInfoVerifier;
    private final FindFlightOutputBoundary flightPresenter;
    private final LogSearchInfoDataAccessInterface logSearchInfoDataObject;
    private final CityCodeConverter cityCodeConverter;
    private final FindFlightUserDataAccessInterface flightUserDataAccessObject;

    public FindFlightInteractor(SearchInfoVerifier searchInfoVerifier, FindFlightOutputBoundary findFlightOutputBoundary,
                                LogSearchInfoDataAccessInterface logSearchInfoDataAccessInterface, CityCodeConverter cityCodeConverter, FindFlightUserDataAccessInterface flightUserDataAccessObject) {
        this.searchInfoVerifier = searchInfoVerifier;
        this.flightPresenter = findFlightOutputBoundary;
        this.logSearchInfoDataObject = logSearchInfoDataAccessInterface;
        this.cityCodeConverter = cityCodeConverter;
        this.flightUserDataAccessObject = flightUserDataAccessObject;
    }

    @Override
    public void execute(FindFlightInputData findFlightInputData) {

        try { // <-- ADD THIS

            if (!searchInfoVerifier.isCityValid(findFlightInputData.getFrom())) {
                flightPresenter.prepareFailView("Please enter a valid city to depart from");
            }

            else if (!searchInfoVerifier.isCityValid(findFlightInputData.getTo())) {
                flightPresenter.prepareFailView("Please enter a valid city to travel to");
            }

            else if (!searchInfoVerifier.isDayValid(findFlightInputData.getDay(), findFlightInputData.getMonth(),  findFlightInputData.getYear())) {
                flightPresenter.prepareFailView("The day you entered is not valid");
            }

            else if (!searchInfoVerifier.isMonthValid(findFlightInputData.getMonth(), findFlightInputData.getYear())) {
                flightPresenter.prepareFailView("The month you entered is not valid");
            }

            else {
                // The search info the user entered passed all the checks...
                // ... (rest of your API call logic)

                final FlightSearchInformation flightSearchInformation = new FlightSearchInformation(findFlightInputData.getFrom(), findFlightInputData.getTo(),
                        findFlightInputData.getDay(), findFlightInputData.getMonth(), findFlightInputData.getYear());
                logSearchInfoDataObject.log(flightSearchInformation);

                int day = findFlightInputData.getDay();
                String month = findFlightInputData.getMonth();
                int year = findFlightInputData.getYear();

                int monthAsInt = searchInfoVerifier.getMonthAsInt(month);
                String departureData = String.format("%04d-%02d-%02d", year, monthAsInt, day);

                String originCityName = findFlightInputData.getFrom();
                String destCityName = findFlightInputData.getTo();

                String originLocationCode = cityCodeConverter.getCode(originCityName);
                String destinationLocationCode = cityCodeConverter.getCode(destCityName);
                int adults = 1;
                boolean nonstop = false
                        ;

                List<Flight> flights = flightUserDataAccessObject.search(
                        originLocationCode, destinationLocationCode,
                        departureData, adults, nonstop
                );

                FindFlightOutputData outputData = new FindFlightOutputData(flights, false);
                flightPresenter.prepareSuccessView(outputData);
            }

        } catch (Exception e) { // <-- ADD THIS CATCH BLOCK
            flightPresenter.prepareFailView("An error occurred: " + e.getMessage());
            e.printStackTrace(); // This will print the full crash log to your console
        }
    }

}
