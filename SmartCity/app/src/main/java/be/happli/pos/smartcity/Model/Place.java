package be.happli.pos.smartcity.Model;

public class Place {
    private int id;
    private String placeName;
    private String activityName;
    private String description;
    private String address;
    private int postalCode;
    private String cityName;

    private Boolean isBookmarked;

    public Place(int id, String placeName, String activityName, String description, String address, int postalCode, String cityName) {
        this.id = id;
        this.placeName = placeName;
        this.activityName = activityName;
        this.description = description;
        this.address = address;
        this.postalCode = postalCode;
        this.cityName = cityName;
        this.isBookmarked = false;
    }

    public int getId() {
        return id;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getActivityName() {
        return activityName;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public String getCityName() {
        return cityName;
    }

    public Boolean getBookmarked() {
        return isBookmarked;
    }

    public void setBookmarked(Boolean bookmarked) {
        isBookmarked = bookmarked;
    }
}
