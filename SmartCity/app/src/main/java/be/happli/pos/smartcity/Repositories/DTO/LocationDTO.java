package be.happli.pos.smartcity.Repositories.DTO;

import com.squareup.moshi.Json;

public class LocationDTO {
    @Json(name = "id") // Corresponds to JSON key
    private Integer locationId;

    @Json(name = "postal_code")
    private Integer postalCode;

    @Json(name = "city")
    private String cityName;

    // Getters & Setters
    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
