package id.co.qualitas.epriority.model;

import java.io.Serializable;
import java.util.List;

public class Packages implements Serializable {
    private List<Dropdown> loungeTypes;
    private List<Dropdown> vehicleTypes;
    private List<Dropdown> fastlaneTypes;
    private List<Dropdown> flightClasses;
    private List<Dropdown> baggageTypes;
    private List<Dropdown> countries;
    private List<Dropdown> packages;

    //header packages
    private Packages airport_transfer;
    private Packages lounge_access;
    private Packages flight_detail;
    private Packages fast_lane;
    private Packages baggage_service;

    //detail packages
    private int vehicle_type;
    private String pickup_time;
    private String contact_no;
    private String request_note;
    private int lounge_type;
    private int flight_class;
    private int type_lane;
    private int type_baggage;

    public Packages() {
    }

    public Packages getLounge_access() {
        return lounge_access;
    }

    public void setLounge_access(Packages lounge_access) {
        this.lounge_access = lounge_access;
    }

    public Packages getFlight_detail() {
        return flight_detail;
    }

    public void setFlight_detail(Packages flight_detail) {
        this.flight_detail = flight_detail;
    }

    public Packages getFast_lane() {
        return fast_lane;
    }

    public void setFast_lane(Packages fast_lane) {
        this.fast_lane = fast_lane;
    }

    public Packages getBaggage_service() {
        return baggage_service;
    }

    public void setBaggage_service(Packages baggage_service) {
        this.baggage_service = baggage_service;
    }

    public Packages getAirport_transfer() {
        return airport_transfer;
    }

    public void setAirport_transfer(Packages airport_transfer) {
        this.airport_transfer = airport_transfer;
    }

    public int getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(int vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getPickup_time() {
        return pickup_time;
    }

    public void setPickup_time(String pickup_time) {
        this.pickup_time = pickup_time;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getRequest_note() {
        return request_note;
    }

    public void setRequest_note(String request_note) {
        this.request_note = request_note;
    }

    public int getLounge_type() {
        return lounge_type;
    }

    public void setLounge_type(int lounge_type) {
        this.lounge_type = lounge_type;
    }

    public int getFlight_class() {
        return flight_class;
    }

    public void setFlight_class(int flight_class) {
        this.flight_class = flight_class;
    }

    public int getType_lane() {
        return type_lane;
    }

    public void setType_lane(int type_lane) {
        this.type_lane = type_lane;
    }

    public int getType_baggage() {
        return type_baggage;
    }

    public void setType_baggage(int type_baggage) {
        this.type_baggage = type_baggage;
    }

    public List<Dropdown> getLoungeTypes() {
        return loungeTypes;
    }

    public void setLoungeTypes(List<Dropdown> loungeTypes) {
        this.loungeTypes = loungeTypes;
    }

    public List<Dropdown> getVehicleTypes() {
        return vehicleTypes;
    }

    public void setVehicleTypes(List<Dropdown> vehicleTypes) {
        this.vehicleTypes = vehicleTypes;
    }

    public List<Dropdown> getFastlaneTypes() {
        return fastlaneTypes;
    }

    public void setFastlaneTypes(List<Dropdown> fastlaneTypes) {
        this.fastlaneTypes = fastlaneTypes;
    }

    public List<Dropdown> getFlightClasses() {
        return flightClasses;
    }

    public void setFlightClasses(List<Dropdown> flightClasses) {
        this.flightClasses = flightClasses;
    }

    public List<Dropdown> getBaggageTypes() {
        return baggageTypes;
    }

    public void setBaggageTypes(List<Dropdown> baggageTypes) {
        this.baggageTypes = baggageTypes;
    }

    public List<Dropdown> getCountries() {
        return countries;
    }

    public void setCountries(List<Dropdown> countries) {
        this.countries = countries;
    }

    public List<Dropdown> getPackages() {
        return packages;
    }

    public void setPackages(List<Dropdown> packages) {
        this.packages = packages;
    }
}
