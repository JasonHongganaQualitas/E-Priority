package id.co.qualitas.epriority.model;

public class Booking {
    public String name;
    public String bookingId;
    public String dateTime;
    public String locationAndFlight;
    public int peopleCount;
    public String status;

    public Booking(String name, String bookingId, String dateTime,
                   String locationAndFlight, int peopleCount, String status) {
        this.name = name;
        this.bookingId = bookingId;
        this.dateTime = dateTime;
        this.locationAndFlight = locationAndFlight;
        this.peopleCount = peopleCount;
        this.status = status;
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
