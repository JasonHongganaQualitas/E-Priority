package id.co.qualitas.epriority.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TripRequest implements Serializable {
    public int offset;
    public int limit;
    public String trip_type;
    public String search;
    @SerializedName("departureTrips")
    List<TripsResponse> departureTrips;
    @SerializedName("arrivalTrips")
    List<TripsResponse> arrivalTrips;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getTrip_type() {
        return trip_type;
    }

    public void setTrip_type(String trip_type) {
        this.trip_type = trip_type;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public List<TripsResponse> getDepartureTrips() {
        return departureTrips;
    }

    public void setDepartureTrips(List<TripsResponse> departureTrips) {
        this.departureTrips = departureTrips;
    }

    public List<TripsResponse> getArrivalTrips() {
        return arrivalTrips;
    }

    public void setArrivalTrips(List<TripsResponse> arrivalTrips) {
        this.arrivalTrips = arrivalTrips;
    }
}
