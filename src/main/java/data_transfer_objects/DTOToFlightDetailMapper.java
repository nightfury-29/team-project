package data_transfer_objects;

import java.util.stream.Collectors;

import entity.FlightDetail;

public class DTOToFlightDetailMapper {

    public FlightDetail map(FlightDetailDataTransferObject dto) {
        return new FlightDetail(
                dto.id,
                dto.numberOfBookableSeats,
                mapPrice(dto.price),
                dto.fareOption,
                dto.segments.stream()
                        .map(this::mapSegment)
                        .collect(Collectors.toList())
        );
    }

    private FlightDetail.Price mapPrice(FlightDetailDataTransferObject.PriceDTO priceDTO) {
        return new FlightDetail.Price(
                priceDTO.total,
                priceDTO.currency
        );
    }

    private FlightDetail.SegmentDetail mapSegment(FlightDetailDataTransferObject.SegmentDTO segDTO) {
        return new FlightDetail.SegmentDetail(
                segDTO.departureAirport,
                segDTO.departureTime,
                segDTO.departureTerminal,
                segDTO.arrivalAirport,
                segDTO.arrivalTime,
                segDTO.arrivalTerminal,
                segDTO.carrierCode,
                segDTO.flightNumber,
                segDTO.aircraft,
                segDTO.duration,
                segDTO.cabinClass,
                mapBaggage(segDTO.baggage),
                segDTO.amenities.stream()
                        .map(this::mapAmenity)
                        .collect(Collectors.toList())
        );
    }

    private FlightDetail.Baggage mapBaggage(FlightDetailDataTransferObject.BaggageDTO bDTO) {
        return new FlightDetail.Baggage(
                bDTO.checkedBags,
                bDTO.cabinBags
        );
    }

    private FlightDetail.Amenity mapAmenity(FlightDetailDataTransferObject.AmenityDTO aDTO) {
        return new FlightDetail.Amenity(
                aDTO.description,
                aDTO.amenityType,
                aDTO.isChargeable
        );
    }
}

