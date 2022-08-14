package be.happli.pos.smartcity.services.Mappers;

import be.happli.pos.smartcity.Model.Place;
import be.happli.pos.smartcity.Repositories.DTO.PlaceDTO;

public class PlaceMapper {
    private static PlaceMapper instance = null;

    public static PlaceMapper getInstance() {
        if (instance == null) {
            instance = new PlaceMapper();
        }
        return instance;
    }

    public Place mapToPlace(PlaceDTO placeDTO) {
        if(placeDTO == null) {
            return null;
        }
        return new Place(placeDTO.getId(), placeDTO.getPlace_name(), placeDTO.getActivity_name(), placeDTO.getDescription(), placeDTO.getAddress(), placeDTO.getPostal_code(), placeDTO.getCity());
    }
}
