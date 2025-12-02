package use_case.compare_saved_flights;

import entity.FlightDetail;
import java.util.List;

public class CompareSavedFlightsInteractor implements CompareSavedFlightsInputBoundary {

    private final CompareSavedFlightsOutputBoundary presenter;

    public CompareSavedFlightsInteractor(CompareSavedFlightsOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(CompareSavedFlightsInputData inputData) {
        FlightDetail first = inputData.getFirst();
        FlightDetail second = inputData.getSecond();

        CompareSavedFlightsOutputData.FlightSummary s1 = toSummary(first);
        CompareSavedFlightsOutputData.FlightSummary s2 = toSummary(second);

        CompareSavedFlightsOutputData output =
                new CompareSavedFlightsOutputData(s1, s2);

        presenter.present(output);
    }

    private CompareSavedFlightsOutputData.FlightSummary toSummary(FlightDetail fd) {
        List<FlightDetail.SegmentDetail> segs = fd.segments;

        if (segs == null || segs.isEmpty()) {
            return new CompareSavedFlightsOutputData.FlightSummary(
                    "",
                    "",
                    "",
                    fd.price.total,
                    fd.price.currency,
                    0,
                    0
            );
        }

        FlightDetail.SegmentDetail seg = segs.get(0);

        int checked = 0;
        int cabin = 0;
        if (seg.baggage != null) {
            checked = seg.baggage.checkedBags;
            cabin = seg.baggage.cabinBags;
        }

        return new CompareSavedFlightsOutputData.FlightSummary(
                seg.carrierCode,
                seg.flightNumber,
                seg.duration,
                fd.price.total,
                fd.price.currency,
                checked,
                cabin
        );
    }
}
