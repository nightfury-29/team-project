package use_case.save_flight;

import java.util.List;

import entity.FlightDetail;

/**
 * DAO interface for the Save Flight Use Case.
 */
public interface SaveFlightDataAccessInterface {
    /**
     * Checks if the given flight object already exists in the saved flights database.
     * @param flightId the flight to look for
     * @param username the username to look for
     * @return true if a user with the given flight exists; false otherwise
     */
    boolean flightExistsForUser(String username, String flightId);

    /**
     * Saves the flight.
     * @param detail the flight to save
     * @param username the user to save the flight against
     */
    void saveFlightForUser(String username, FlightDetail detail);

    /**
     * Returns the saved flights for a given user.
     * @param username the user to get the flights for, returns empty list if no flights available
     * @return a list of FlightDetail for a given user
     */
    List<FlightDetail> getSavedFlights(String username);
}

