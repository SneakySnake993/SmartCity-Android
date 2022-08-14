package be.happli.pos.smartcity.services.Mappers;

import be.happli.pos.smartcity.Model.Location;
import be.happli.pos.smartcity.Repositories.DTO.LocationDTO;

public class LocationMapper {
    private static LocationMapper instance = null;

    public static LocationMapper getInstance() {
        if (instance == null) {
            instance = new LocationMapper();
        }
        return instance;
    }

    public Location mapToLocation(LocationDTO dto) {
        if(dto == null) {
            return null;
        }

        return new Location(dto.getLocationId(), dto.getPostalCode(), dto.getCityName());
    }
}
