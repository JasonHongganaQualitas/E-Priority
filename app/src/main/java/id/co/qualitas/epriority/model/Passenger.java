package id.co.qualitas.epriority.model;

import java.io.Serializable;

public class Passenger implements Serializable {
    private int item_no;
    private String first_name;
    private String last_name;
    private String email;
    private String phone_no;
    private String birth_date;
    private String passport_no;
    private String passport_expdate;
    private int inflight_meal;
    private int nationality;
    private int flight_class;
    private int passport_country;
    private int cabin;
    private int baggage;

    public Passenger() {
    }

    public int getInflight_meal() {
        return inflight_meal;
    }

    public void setInflight_meal(int inflight_meal) {
        this.inflight_meal = inflight_meal;
    }

    public int getCabin() {
        return cabin;
    }

    public void setCabin(int cabin) {
        this.cabin = cabin;
    }

    public int getBaggage() {
        return baggage;
    }

    public void setBaggage(int baggage) {
        this.baggage = baggage;
    }

    public int getItem_no() {
        return item_no;
    }

    public void setItem_no(int item_no) {
        this.item_no = item_no;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getPassport_no() {
        return passport_no;
    }

    public void setPassport_no(String passport_no) {
        this.passport_no = passport_no;
    }

    public String getPassport_expdate() {
        return passport_expdate;
    }

    public void setPassport_expdate(String passport_expdate) {
        this.passport_expdate = passport_expdate;
    }

    public int getNationality() {
        return nationality;
    }

    public void setNationality(int nationality) {
        this.nationality = nationality;
    }

    public int getFlight_class() {
        return flight_class;
    }

    public void setFlight_class(int flight_class) {
        this.flight_class = flight_class;
    }

    public int getPassport_country() {
        return passport_country;
    }

    public void setPassport_country(int passport_country) {
        this.passport_country = passport_country;
    }
}
