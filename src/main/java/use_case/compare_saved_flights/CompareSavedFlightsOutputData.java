package use_case.compare_saved_flights;

public class CompareSavedFlightsOutputData {

    public static class FlightSummary {
        public final String airline;
        public final String flightNumber;
        public final String duration;
        public final double price;
        public final String currency;
        public final int checkedBags;
        public final int cabinBags;

        public FlightSummary(String airline,
                             String flightNumber,
                             String duration,
                             double price,
                             String currency,
                             int checkedBags,
                             int cabinBags) {
            this.airline = airline;
            this.flightNumber = flightNumber;
            this.duration = duration;
            this.price = price;
            this.currency = currency;
            this.checkedBags = checkedBags;
            this.cabinBags = cabinBags;
        }
    }

    private final FlightSummary first;
    private final FlightSummary second;

    public CompareSavedFlightsOutputData(FlightSummary first, FlightSummary second) {
        this.first = first;
        this.second = second;
    }

    public FlightSummary getFirst() { return first; }
    public FlightSummary getSecond() { return second; }
}
