package id.co.qualitas.epriority.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TripsResponse {
    public int total_count;//status today
    public int pending_count;//status today
    public String name;
    public String booking_id;
    public String dateTime;
    public String locationAndFlight;
    public int peopleCount;
    public String status;
    public int id;
    public int passenger_count;
    public int agent_id;
    public int customer_id;
    public String customer_name;
    public String tripId;
    public String trip_date;
    public String trip_type;
    public String flight_date;
    public String flight_time;
    public String flight_no;
    public String flightInfo;
    public String route_to;
    public String route_from;
    public String date_from;
    public String date_to;
    public String airline;
    public String aircraft;
    public List<Passenger> passengers;
    @SerializedName("package")
    public Packages packages;
    public String city;
    private List<Agent> agent_list;

    //detail
    private int trip_id;
    private String qr_code_data;

    public TripsResponse() {
    }

    public TripsResponse(String name, String booking_id, String dateTime, String locationAndFlight, int peopleCount, String status) {
        this.name = name;
        this.booking_id = booking_id;
        this.dateTime = dateTime;
        this.locationAndFlight = locationAndFlight;
        this.peopleCount = peopleCount;
        this.status = status;
    }

    public int getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(int trip_id) {
        this.trip_id = trip_id;
    }

    public String getQr_code_data() {
        return qr_code_data;
    }

    public void setQr_code_data(String qr_code_data) {
        this.qr_code_data = qr_code_data;
    }

    public int getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(int agent_id) {
        this.agent_id = agent_id;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getAircraft() {
        return aircraft;
    }

    public void setAircraft(String aircraft) {
        this.aircraft = aircraft;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    public Packages getPackages() {
        return packages;
    }

    public void setPackages(Packages packages) {
        this.packages = packages;
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

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getTrip_date() {
        return trip_date;
    }

    public void setTrip_date(String trip_date) {
        this.trip_date = trip_date;
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

    public void setAgent_list(List<Agent> agent_list) {
        this.agent_list = agent_list;
    }

    public List<Agent> getAgent_list() {
        return agent_list;
    }
}
