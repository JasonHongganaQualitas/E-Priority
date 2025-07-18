package id.co.qualitas.epriority.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Trips implements Serializable {
    public int offset;
    public int limit;
    public String tripType;
    public String search;
    @SerializedName("departureTrips")
    List<Booking> departureTrips;
    @SerializedName("arrivalTrips")
    List<Booking> arrivalTrips;

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

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public List<Booking> getDepartureTrips() {
        return departureTrips;
    }

    public void setDepartureTrips(List<Booking> departureTrips) {
        this.departureTrips = departureTrips;
    }

    public List<Booking> getArrivalTrips() {
        return arrivalTrips;
    }

    public void setArrivalTrips(List<Booking> arrivalTrips) {
        this.arrivalTrips = arrivalTrips;
    }
}
