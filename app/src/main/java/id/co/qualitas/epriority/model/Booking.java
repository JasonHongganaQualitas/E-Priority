package id.co.qualitas.epriority.model;

public class Booking {
    public int total_count;//status today
    public int pending_count;//status today
    public String name;
    public String booking_id;
    public String dateTime;
    public String locationAndFlight;
    public int peopleCount;
    public String status;
    //on going trip customer
    public int id;
    public int passenger_count;
    public String customerId;
    public String customer_name;
    public String tripId;
    public String tripDate;
    public String flight_date;
    public String flight_time;
    public String flight_no;
    public String flightInfo;
    public String route_to;
    public String route_from;
    public String date_from;
    public String date_to;
    public String trip_type;
    public String city;

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

//     "id": 1,
//             "trip_type": "ARRIVAL",
//             "booking_id": "TIKET0001",
//             "customer_name": "Billy",
//             "route_from": "JAKARTA",
//             "route_to": "SINGAPORE",
//             "city": "SINGAPORE",
//             "flight_no": "SQ01",
//             "passenger_count": 1,
//             "status": "Active",
//             "flight_date": "2025-07-17",
//             "flight_time": "14:20:00",
//             "date_from": "2025-07-17 13:00:00",
//             "date_to": "2025-07-17 14:20:00"

    public Booking(String name, String booking_id, String dateTime,
                   String locationAndFlight, int peopleCount, String status) {
        this.name = name;
        this.booking_id = booking_id;
        this.dateTime = dateTime;
        this.locationAndFlight = locationAndFlight;
        this.peopleCount = peopleCount;
        this.status = status;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public int getPending_count() {
        return pending_count;
    }

    public void setPending_count(int pending_count) {
        this.pending_count = pending_count;
    }

    public String getFlight_date() {
        return flight_date;
    }

    public void setFlight_date(String flight_date) {
        this.flight_date = flight_date;
    }

    public String getFlight_time() {
        return flight_time;
    }

    public void setFlight_time(String flight_time) {
        this.flight_time = flight_time;
    }

    public String getDate_to() {
        return date_to;
    }

    public void setDate_to(String date_to) {
        this.date_to = date_to;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPassenger_count() {
        return passenger_count;
    }

    public void setPassenger_count(int passenger_count) {
        this.passenger_count = passenger_count;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getFlightInfo() {
        return flightInfo;
    }

    public void setFlightInfo(String flightInfo) {
        this.flightInfo = flightInfo;
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

    public String getFlight_no() {
        return flight_no;
    }

    public void setFlight_no(String flight_no) {
        this.flight_no = flight_no;
    }

    public String getRoute_to() {
        return route_to;
    }

    public void setRoute_to(String route_to) {
        this.route_to = route_to;
    }

    public String getRoute_from() {
        return route_from;
    }

    public void setRoute_from(String route_from) {
        this.route_from = route_from;
    }

    public String getDate_from() {
        return date_from;
    }

    public void setDate_from(String date_from) {
        this.date_from = date_from;
    }

    public String getTrip_type() {
        return trip_type;
    }

    public void setTrip_type(String trip_type) {
        this.trip_type = trip_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
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
