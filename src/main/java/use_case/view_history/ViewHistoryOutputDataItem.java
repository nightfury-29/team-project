package use_case.view_history;

/**
 * A single item in the Output Data for the View History Use Case.
 */

public class ViewHistoryOutputDataItem {

    private final String date;
    private final String time;
    private final String from;
    private final String to;
    private final String day;
    private final String month;
    private final String year;

    public ViewHistoryOutputDataItem(String date, String time, String from, String to, String day, String month, String year) {
        this.date = date;
        this.time = time;
        this.from = from;
        this.to = to;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
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
