package id.co.qualitas.epriority.model;

public class Booking {
    public String name;
    public String bookingId;
    public String dateTime;
    public String locationAndFlight;
    public int peopleCount;
    public String status;
    //on going trip customer
    public int id;
    public String customerId;
    public String tripId;
    public String tripDate;
    public String flightNo;
    public String routeTo;
    public String routeFrom;
    public String dateFrom;
    public String tripType;

//     "id": 1001,
//             "customerId": "1",
//             "tripId": "2001",
//             "tripDate": "2025-07-20",
//             "bookingId": "3001",
//             "flightNo": "GA123",
//             "status": "ASSIGNED",
//             "routeTo": "DPS",
//             "routeFrom": "CGK",
//             "dateFrom": "2025-07-20",
//             "dateTo": "2025-07-20",
//             "tripType": "DEPARTURE"

    public Booking(String name, String bookingId, String dateTime,
                   String locationAndFlight, int peopleCount, String status) {
        this.name = name;
        this.bookingId = bookingId;
        this.dateTime = dateTime;
        this.locationAndFlight = locationAndFlight;
        this.peopleCount = peopleCount;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public String getRouteTo() {
        return routeTo;
    }

    public void setRouteTo(String routeTo) {
        this.routeTo = routeTo;
    }

    public String getRouteFrom() {
        return routeFrom;
    }

    public void setRouteFrom(String routeFrom) {
        this.routeFrom = routeFrom;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLocationAndFlight() {
        return locationAndFlight;
    }

    public void setLocationAndFlight(String locationAndFlight) {
        this.locationAndFlight = locationAndFlight;
    }

    public int getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(int peopleCount) {
        this.peopleCount = peopleCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
