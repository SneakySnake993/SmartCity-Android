package be.happli.pos.smartcity.Model;

public class Location {
    private int id;
    private int zipCode;
    private String cityName;

    public Location(int id, int zipCode, String cityName) {
        this.id = id;
        this.zipCode = zipCode;
        this.cityName = cityName;
    }

    public int getId() {
        return id;
    }

    public String getCityName() {
        return cityName;
    }
}
