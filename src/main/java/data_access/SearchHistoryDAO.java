package data_access;

import use_case.find_flight.LogSearchInfoDataAccessInterface;
import entity.FlightSearchInformation;
import use_case.view_history.ViewHistoryDataAccessInterface;

import java.io.*;
import java.util.*;

/**
 * Data Access Object for flight search information using a CSV file to persist data
 */

public class SearchHistoryDAO implements LogSearchInfoDataAccessInterface, ViewHistoryDataAccessInterface {

    private static final String HEADER = "username,date,time,from,to,day,month,year";

    private final File csvFile;
    private final Map<String, Integer> headers = new LinkedHashMap<>();
    private final Map<String, List<FlightSearchInformation>> usersFlightSearchInfo = new HashMap<>();


    /**
     * Construct this DAO for saving to and reading from a local file.
     * @param csvPath the path of the file to save to
     * @throws RuntimeException if there is an IOException when accessing the file
     */

    public SearchHistoryDAO(String csvPath) {

        csvFile = new File(csvPath);
        headers.put("username", 0);
        headers.put("date", 1);
        headers.put("time", 2);
        headers.put("from", 3);
        headers.put("to", 4);
        headers.put("day", 5);
        headers.put("month", 6);
        headers.put("year", 7);

        if (csvFile.length() == 0) {
            save();
        }
        else {

            try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
                final String header = reader.readLine();

                if (!header.equals(HEADER)) {
                    throw new RuntimeException(String.format("header should be%n: %s%n but was:%n%s", HEADER, header));
                }

                String row;
                while ((row = reader.readLine()) != null) {
                    final String[] col = row.split(",");
                    final String username = String.valueOf(col[headers.get("username")]);
                    final String date = String.valueOf(col[headers.get("date")]);
                    final String time = String.valueOf(col[headers.get("time")]);
                    final String from = String.valueOf(col[headers.get("from")]);
                    final String to = String.valueOf(col[headers.get("to")]);
                    final String day = String.valueOf(col[headers.get("day")]);
                    final String month = String.valueOf(col[headers.get("month")]);
                    final String year = String.valueOf(col[headers.get("year")]);

                    final FlightSearchInformation flightSearchInformation = new FlightSearchInformation(from, to,
                            Integer.parseInt(day), month, Integer.parseInt(year), date, time);

                    if (usersFlightSearchInfo.containsKey(username)) {
                        usersFlightSearchInfo.get(username).add(flightSearchInformation);
                    }

                    else {
                        final List<FlightSearchInformation> list = new ArrayList<>();
                        list.add(flightSearchInformation);
                        usersFlightSearchInfo.put(username, list);
                    }
                }
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

    }

    private void save() {
        final BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(csvFile));
            writer.write(String.join(",", headers.keySet()));
            writer.newLine();

            for (String username : usersFlightSearchInfo.keySet()) {
                final List<FlightSearchInformation> flightSearchInformation = usersFlightSearchInfo.get(username);

                for (FlightSearchInformation searchInformation : flightSearchInformation) {
                    final String line = String.format("%s,%s,%s,%s,%s,%s,%s,%s",
                            username,
                            searchInformation.getDateOfSearch(),
                            searchInformation.getTimeOfSearch(),
                            searchInformation.getFrom(),
                            searchInformation.getTo(),
                            searchInformation.getDay(),
                            searchInformation.getMonth(),
                            searchInformation.getYear());

                    writer.write(line);
                    writer.newLine();
                }
            }

            writer.close();

        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void log(String username, FlightSearchInformation flightSearchInformation) {

        if (usersFlightSearchInfo.containsKey(username)) {
            usersFlightSearchInfo.get(username).add(flightSearchInformation);
        }

        else {
            final List<FlightSearchInformation> list = new ArrayList<>();
            list.add(flightSearchInformation);
            usersFlightSearchInfo.put(username, list);
        }

        this.save();
    }

    @Override
    public List<FlightSearchInformation> getSearchHistory(String username) {
        return usersFlightSearchInfo.get(username);
    }

}
