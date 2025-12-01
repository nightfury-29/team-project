package use_case.find_flight;

import entity.Flight;
import entity.FlightSearchInformation;
import helpers.CityCodeConverter;
import helpers.SearchInfoVerifier;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class FindFlightInteractorTest {

    static final class RecordingFindFlightDaoStub implements FindFlightApiAccessInterface {
        private final List<Flight> flightsToReturn;
        private final RuntimeException toThrow;

        boolean called = false;
        String origin;
        String destination;
        String departureDate;
        int adults;
        boolean nonstop;

        RecordingFindFlightDaoStub(List<Flight> flightsToReturn) {
            this.flightsToReturn = flightsToReturn;
            this.toThrow = null;
        }

        RecordingFindFlightDaoStub(RuntimeException toThrow) {
            this.flightsToReturn = null;
            this.toThrow = toThrow;
        }

        @Override
        public List<Flight> search(String originLocationCode,
                                   String destinationLocationCode,
                                   String departureDate,
                                   int adults,
                                   boolean nonstop) {
            this.called = true;
            this.origin = originLocationCode;
            this.destination = destinationLocationCode;
            this.departureDate = departureDate;
            this.adults = adults;
            this.nonstop = nonstop;

            if (toThrow != null) throw toThrow;
            return flightsToReturn;
        }
    }

    private FindFlightInputData input(String from, String to, int day, String month, int year, String username) {
        return new FindFlightInputData(username, from, to, String.valueOf(day), month, String.valueOf(year));
    }

    private Flight flight(String id, double price) {
        return new Flight(
                id,
                "SYD", "2025-12-02T10:00:00",
                "YYZ", "2025-12-02T18:00:00",
                price, "EUR",
                "8h0m", "AC123", "A320"
        );
    }

    @Test
    void invalidFrom_callsFailView_andDoesNotCallDao() {
        SearchInfoVerifier verifier = mock(SearchInfoVerifier.class);
        FindFlightOutputBoundary presenter = mock(FindFlightOutputBoundary.class);
        LogSearchInfoDataAccessInterface logger = mock(LogSearchInfoDataAccessInterface.class);
        CityCodeConverter converter = mock(CityCodeConverter.class);

        RecordingFindFlightDaoStub daoStub = new RecordingFindFlightDaoStub(List.of(flight("70", 1000.0)));
        FindFlightInteractor interactor = new FindFlightInteractor(verifier, presenter, logger, converter, daoStub);

        FindFlightInputData in = input("City", "Toronto", 2, "December", 2025, "sun");
        when(verifier.isCityValid("City")).thenReturn(false);

        interactor.execute(in);

        verify(presenter).prepareFailView("Please enter a valid city to depart from");
        verify(presenter, never()).prepareSuccessView(any());
        assertFalse(daoStub.called);
        verifyNoInteractions(logger);
        verifyNoInteractions(converter);
    }

    @Test
    void invalidTo_callsFailView_andDoesNotCallDao() {
        SearchInfoVerifier verifier = mock(SearchInfoVerifier.class);
        FindFlightOutputBoundary presenter = mock(FindFlightOutputBoundary.class);
        LogSearchInfoDataAccessInterface logger = mock(LogSearchInfoDataAccessInterface.class);
        CityCodeConverter converter = mock(CityCodeConverter.class);

        RecordingFindFlightDaoStub daoStub = new RecordingFindFlightDaoStub(List.of(flight("70", 1000.0)));
        FindFlightInteractor interactor = new FindFlightInteractor(verifier, presenter, logger, converter, daoStub);

        FindFlightInputData in = input("Sydney", "City", 2, "December", 2025, "sun");
        when(verifier.isCityValid("Sydney")).thenReturn(true);
        when(verifier.isCityValid("City")).thenReturn(false);

        interactor.execute(in);

        verify(presenter).prepareFailView("Please enter a valid city to travel to");
        verify(presenter, never()).prepareSuccessView(any());
        assertFalse(daoStub.called);
        verifyNoInteractions(logger);
        verifyNoInteractions(converter);
    }

    @Test
    void invalidDay_callsFailView_andDoesNotCallDao() {
        SearchInfoVerifier verifier = mock(SearchInfoVerifier.class);
        FindFlightOutputBoundary presenter = mock(FindFlightOutputBoundary.class);
        LogSearchInfoDataAccessInterface logger = mock(LogSearchInfoDataAccessInterface.class);
        CityCodeConverter converter = mock(CityCodeConverter.class);

        RecordingFindFlightDaoStub daoStub = new RecordingFindFlightDaoStub(List.of(flight("70", 1000.0)));
        FindFlightInteractor interactor = new FindFlightInteractor(verifier, presenter, logger, converter, daoStub);

        FindFlightInputData in = input("Sydney", "Toronto", 99, "December", 2025, "sun");
        when(verifier.isCityValid(anyString())).thenReturn(true);
        when(verifier.isDayValid(99, "December", 2025)).thenReturn(false);

        interactor.execute(in);

        verify(presenter).prepareFailView("The day you entered is not valid");
        verify(presenter, never()).prepareSuccessView(any());
        assertFalse(daoStub.called);
        verifyNoInteractions(logger);
        verifyNoInteractions(converter);
    }

    @Test
    void invalidMonth_callsFailView_andDoesNotCallDao() {
        SearchInfoVerifier verifier = mock(SearchInfoVerifier.class);
        FindFlightOutputBoundary presenter = mock(FindFlightOutputBoundary.class);
        LogSearchInfoDataAccessInterface logger = mock(LogSearchInfoDataAccessInterface.class);
        CityCodeConverter converter = mock(CityCodeConverter.class);

        RecordingFindFlightDaoStub daoStub = new RecordingFindFlightDaoStub(List.of(flight("70", 1000.0)));
        FindFlightInteractor interactor = new FindFlightInteractor(verifier, presenter, logger, converter, daoStub);

        FindFlightInputData in = input("Sydney", "Toronto", 2, "Month", 2025, "sun");
        when(verifier.isCityValid(anyString())).thenReturn(true);
        when(verifier.isDayValid(anyInt(), anyString(), anyInt())).thenReturn(true);
        when(verifier.isMonthValid("Month", 2025)).thenReturn(false);

        interactor.execute(in);

        verify(presenter).prepareFailView("The month you entered is not valid");
        verify(presenter, never()).prepareSuccessView(any());
        assertFalse(daoStub.called);
        verifyNoInteractions(logger);
        verifyNoInteractions(converter);
    }

    @Test
    void success_callsDaoWithConvertedCodes_andPrepareSuccessView() {
        SearchInfoVerifier verifier = mock(SearchInfoVerifier.class);
        FindFlightOutputBoundary presenter = mock(FindFlightOutputBoundary.class);
        LogSearchInfoDataAccessInterface logger = mock(LogSearchInfoDataAccessInterface.class);
        CityCodeConverter converter = mock(CityCodeConverter.class);

        List<Flight> flights = List.of(
                flight("10", 500.0),
                flight("20", 300.0)
        );
        RecordingFindFlightDaoStub daoStub = new RecordingFindFlightDaoStub(flights);

        FindFlightInteractor interactor = new FindFlightInteractor(verifier, presenter, logger, converter, daoStub);

        FindFlightInputData in = input("Sydney", "Toronto", 2, "December", 2025, "henry");

        when(verifier.isCityValid(anyString())).thenReturn(true);
        when(verifier.isDayValid(2, "December", 2025)).thenReturn(true);
        when(verifier.isMonthValid("December", 2025)).thenReturn(true);
        when(verifier.getMonthAsInt("December")).thenReturn(12);

        when(converter.getCode("Sydney")).thenReturn("SYD");
        when(converter.getCode("Toronto")).thenReturn("YYZ");

        interactor.execute(in);

        assertTrue(daoStub.called);
        assertEquals("SYD", daoStub.origin);
        assertEquals("YYZ", daoStub.destination);
        assertEquals("2025-12-02", daoStub.departureDate);
        assertEquals(1, daoStub.adults);
        assertFalse(daoStub.nonstop);

        verify(logger).log(eq("henry"), any(FlightSearchInformation.class));

        ArgumentCaptor<FindFlightOutputData> captor = ArgumentCaptor.forClass(FindFlightOutputData.class);
        verify(presenter).prepareSuccessView(captor.capture());

        FindFlightOutputData out =  captor.getValue();
        assertNotNull(out);
        assertEquals(flights, out.getFlights());
        verify(presenter, never()).prepareFailView(anyString());
    }

    @Test
    void exceptionInDao_callsFailView() {
        SearchInfoVerifier verifier = mock(SearchInfoVerifier.class);
        FindFlightOutputBoundary presenter = mock(FindFlightOutputBoundary.class);
        LogSearchInfoDataAccessInterface logger = mock(LogSearchInfoDataAccessInterface.class);
        CityCodeConverter converter = mock(CityCodeConverter.class);

        RecordingFindFlightDaoStub daoStub = new RecordingFindFlightDaoStub(new RuntimeException("dao boom"));
        FindFlightInteractor interactor = new FindFlightInteractor(verifier, presenter, logger, converter, daoStub);

        FindFlightInputData in = input("Sydney", "Toronto", 2, "December", 2025, "henry");

        when(verifier.isCityValid(anyString())).thenReturn(true);
        when(verifier.isDayValid(anyInt(), anyString(), anyInt())).thenReturn(true);
        when(verifier.isMonthValid(anyString(), anyInt())).thenReturn(true);
        when(verifier.getMonthAsInt(anyString())).thenReturn(12);

        when(converter.getCode(anyString())).thenReturn("XXX");

        interactor.execute(in);

        verify(presenter).prepareFailView(contains("An error occurred: dao boom"));
        verify(presenter, never()).prepareSuccessView(any());
    }
}
