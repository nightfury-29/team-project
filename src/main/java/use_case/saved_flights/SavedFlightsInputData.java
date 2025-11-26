package use_case.saved_flights;

public class SavedFlightsInputData {
    private final String username;

    public SavedFlightsInputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
