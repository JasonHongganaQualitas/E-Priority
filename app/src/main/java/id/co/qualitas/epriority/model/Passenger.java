package id.co.qualitas.epriority.model;

import java.io.Serializable;

public class Passenger implements Serializable {
    private int item_no;
    private String first_name;
    private String last_name;
    private String email;
    private String phone_no;
    private String birth_date;
    private String seat_layout;
    private String passport_no;
    private String passport_expdate;
    private String nationality_name;
    private String passport_country_name;
    private String flight_class_name;
    private int pos_passenger;
    private int flight_class_id;
    private int nationality_id;
    private int passport_country_id;
    private int inflight_meal;
    private int cabin;
    private int baggage;
    private Dropdown selectedNationality;
    private Dropdown selectedFlightClass;
    private Dropdown selectedNationalityPassport;

    public Passenger() {
    }

    public int getPos_passenger() {
        return pos_passenger;
    }

    public void setPos_passenger(int pos_passenger) {
        this.pos_passenger = pos_passenger;
    }

    public String getNationality_name() {
        return nationality_name;
    }

    public void setNationality_name(String nationality_name) {
        this.nationality_name = nationality_name;
    }

    public String getPassport_country_name() {
        return passport_country_name;
    }

    public void setPassport_country_name(String passport_country_name) {
        this.passport_country_name = passport_country_name;
    }

    public String getFlight_class_name() {
        return flight_class_name;
    }

    public void setFlight_class_name(String flight_class_name) {
        this.flight_class_name = flight_class_name;
    }

    public int getFlight_class_id() {
        return flight_class_id;
    }

    public void setFlight_class_id(int flight_class_id) {
        this.flight_class_id = flight_class_id;
    }

    public int getNationality_id() {
        return nationality_id;
    }

    public void setNationality_id(int nationality_id) {
        this.nationality_id = nationality_id;
    }

    public int getPassport_country_id() {
        return passport_country_id;
    }

    public void setPassport_country_id(int passport_country_id) {
        this.passport_country_id = passport_country_id;
    }

    public Dropdown getSelectedFlightClass() {
        return selectedFlightClass;
    }

    public void setSelectedFlightClass(Dropdown selectedFlightClass) {
        this.selectedFlightClass = selectedFlightClass;
    }

    public Dropdown getSelectedNationalityPassport() {
        return selectedNationalityPassport;
    }

    public void setSelectedNationalityPassport(Dropdown selectedNationalityPassport) {
        this.selectedNationalityPassport = selectedNationalityPassport;
    }

    public Dropdown getSelectedNationality() {
        return selectedNationality;
    }

    public void setSelectedNationality(Dropdown selectedNationality) {
        this.selectedNationality = selectedNationality;
    }

    public String getSeat_layout() {
        return seat_layout;
    }

    public void setSeat_layout(String seat_layout) {
        this.seat_layout = seat_layout;
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
}
