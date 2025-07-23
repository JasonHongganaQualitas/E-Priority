package id.co.qualitas.epriority.model;

import java.io.Serializable;
import java.util.List;

public class Packages implements Serializable {
    private Dropdown selectedLoungeType;
    private List<Dropdown> loungeTypes;
    private Dropdown selectedVehicleType;
    private List<Dropdown> vehicleTypes;
    private Dropdown selectedFastlaneType;
    private List<Dropdown> fastlaneTypes;
    private Dropdown selectedFlightClasses;
    private List<Dropdown> flightClasses;
    private Dropdown selectedBaggageType;
    private List<Dropdown> baggageTypes;
    private List<Dropdown> countries;
    private List<Dropdown> packages;

    //header packages
    private Packages trip_airporttransfer;
    private Packages trip_loungeaccess;
    private Packages flight_detail;
    private Packages trip_fastlane;
    private Packages trip_baggageservice;

    //detail packages
    private int vehicle_type;
    private String pickup_time;
    private String contact_no;
    private String request_note;
    private int lounge_type;
    private int flight_class;
    private int type_lane;
    private int type_baggage;
    private int id;
    private int trip_id;
    private int lane_type_id;
    private int lounge_type_id;
    private int vehicle_type_id;
    private int baggage_type_id;
    private int flight_class_id;
    private String flight_class_name;
    private String lounge_type_name;
    private String lane_type_name;
    private String vehicle_type_name;
    private String baggage_type_name;

    public Packages() {
    }

    public int getLounge_type_id() {
        return lounge_type_id;
    }

    public void setLounge_type_id(int lounge_type_id) {
        this.lounge_type_id = lounge_type_id;
    }

    public int getLane_type_id() {
        return lane_type_id;
    }

    public void setLane_type_id(int lane_type_id) {
        this.lane_type_id = lane_type_id;
    }

    public int getVehicle_type_id() {
        return vehicle_type_id;
    }

    public void setVehicle_type_id(int vehicle_type_id) {
        this.vehicle_type_id = vehicle_type_id;
    }

    public int getBaggage_type_id() {
        return baggage_type_id;
    }

    public void setBaggage_type_id(int baggage_type_id) {
        this.baggage_type_id = baggage_type_id;
    }

    public String getLounge_type_name() {
        return lounge_type_name;
    }

    public void setLounge_type_name(String lounge_type_name) {
        this.lounge_type_name = lounge_type_name;
    }

    public String getLane_type_name() {
        return lane_type_name;
    }

    public void setLane_type_name(String lane_type_name) {
        this.lane_type_name = lane_type_name;
    }

    public String getVehicle_type_name() {
        return vehicle_type_name;
    }

    public void setVehicle_type_name(String vehicle_type_name) {
        this.vehicle_type_name = vehicle_type_name;
    }

    public String getBaggage_type_name() {
        return baggage_type_name;
    }

    public void setBaggage_type_name(String baggage_type_name) {
        this.baggage_type_name = baggage_type_name;
    }

    public int getFlight_class_id() {
        return flight_class_id;
    }

    public void setFlight_class_id(int flight_class_id) {
        this.flight_class_id = flight_class_id;
    }

    public String getFlight_class_name() {
        return flight_class_name;
    }

    public void setFlight_class_name(String flight_class_name) {
        this.flight_class_name = flight_class_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(int trip_id) {
        this.trip_id = trip_id;
    }

    public Dropdown getSelectedLoungeType() {
        return selectedLoungeType;
    }

    public void setSelectedLoungeType(Dropdown selectedLoungeType) {
        this.selectedLoungeType = selectedLoungeType;
    }

    public Dropdown getSelectedVehicleType() {
        return selectedVehicleType;
    }

    public void setSelectedVehicleType(Dropdown selectedVehicleType) {
        this.selectedVehicleType = selectedVehicleType;
    }

    public Dropdown getSelectedFastlaneType() {
        return selectedFastlaneType;
    }

    public void setSelectedFastlaneType(Dropdown selectedFastlaneType) {
        this.selectedFastlaneType = selectedFastlaneType;
    }

    public Dropdown getSelectedFlightClasses() {
        return selectedFlightClasses;
    }

    public void setSelectedFlightClasses(Dropdown selectedFlightClasses) {
        this.selectedFlightClasses = selectedFlightClasses;
    }

    public Dropdown getSelectedBaggageType() {
        return selectedBaggageType;
    }

    public void setSelectedBaggageType(Dropdown selectedBaggageType) {
        this.selectedBaggageType = selectedBaggageType;
    }

    public Packages getTrip_loungeaccess() {
        return trip_loungeaccess;
    }

    public void setTrip_loungeaccess(Packages trip_loungeaccess) {
        this.trip_loungeaccess = trip_loungeaccess;
    }

    public Packages getFlight_detail() {
        return flight_detail;
    }

    public void setFlight_detail(Packages flight_detail) {
        this.flight_detail = flight_detail;
    }

    public Packages getTrip_fastlane() {
        return trip_fastlane;
    }

    public void setTrip_fastlane(Packages trip_fastlane) {
        this.trip_fastlane = trip_fastlane;
    }

    public Packages getTrip_baggageservice() {
        return trip_baggageservice;
    }

    public void setTrip_baggageservice(Packages trip_baggageservice) {
        this.trip_baggageservice = trip_baggageservice;
    }

    public Packages getTrip_airporttransfer() {
        return trip_airporttransfer;
    }

    public void setTrip_airporttransfer(Packages trip_airporttransfer) {
        this.trip_airporttransfer = trip_airporttransfer;
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
