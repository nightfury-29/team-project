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
        FlightDetail f1 = inputData.getFirst();
        FlightDetail f2 = inputData.getSecond();

        CompareSavedFlightsOutputData.FlightSummary s1 = toSummary(f1);
        CompareSavedFlightsOutputData.FlightSummary s2 = toSummary(f2);

        CompareSavedFlightsOutputData output = new CompareSavedFlightsOutputData(s1, s2);
        presenter.present(output);
    }

    private CompareSavedFlightsOutputData.FlightSummary toSummary(FlightDetail fd) {
        // 使用第一个 segment 的数据
        List<FlightDetail.SegmentDetail> segs = fd.segments;
        FlightDetail.SegmentDetail seg = segs.get(0);

        int checked = 0;
        int cabin = 0;
        if (seg.baggage != null) {
            checked = seg.baggage.checkedBags;
            cabin = seg.baggage.cabinBags;
        }

        return new CompareSavedFlightsOutputData.FlightSummary(
                seg.carrierCode,                  // airline / carrier
                seg.flightNumber,
                seg.duration,
                fd.price.total,
                fd.price.currency,
                checked,
                cabin
        );
    }
}
