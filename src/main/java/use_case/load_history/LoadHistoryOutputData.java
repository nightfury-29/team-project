package use_case.load_history;

/**
 * Output Data for the Load History Use Case.
 */

public class LoadHistoryOutputData {

    private final String from;
    private final String to;
    private final String day;
    private final String month;
    private final String year;

    public LoadHistoryOutputData(String from, String to, String day, String month, String year) {
        this.from = from;
        this.to = to;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getDay() {
        return day;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }
}
