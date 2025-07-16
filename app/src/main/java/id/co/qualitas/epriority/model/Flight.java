package id.co.qualitas.epriority.model;

public class Flight {
    private String number;
    private String iata;
    private String icao;
    private String codeshared;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getIata() {
        return iata;
    }

    public void setIata(String iata) {
        this.iata = iata;
    }

    public String getIcao() {
        return icao;
    }

    public void setIcao(String icao) {
        this.icao = icao;
    }

    public String getCodeshared() {
        return codeshared;
    }

    public void setCodeshared(String codeshared) {
        this.codeshared = codeshared;
    }
}
