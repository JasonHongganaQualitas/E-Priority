package id.co.qualitas.epriority.model;

public class PassengerDetails {
    public String name;
    public String phone;
    public String email;
    public String dob;
    public String travelClass;

    public String passportNumber;
    public String issuingCountry;
    public String passportExpiry;

    public PassengerDetails(String name, String phone, String email, String dob, String travelClass,
                            String passportNumber, String issuingCountry, String passportExpiry) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.dob = dob;
        this.travelClass = travelClass;
        this.passportNumber = passportNumber;
        this.issuingCountry = issuingCountry;
        this.passportExpiry = passportExpiry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getTravelClass() {
        return travelClass;
    }

    public void setTravelClass(String travelClass) {
        this.travelClass = travelClass;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getIssuingCountry() {
        return issuingCountry;
    }

    public void setIssuingCountry(String issuingCountry) {
        this.issuingCountry = issuingCountry;
    }

    public String getPassportExpiry() {
        return passportExpiry;
    }

    public void setPassportExpiry(String passportExpiry) {
        this.passportExpiry = passportExpiry;
    }
}

