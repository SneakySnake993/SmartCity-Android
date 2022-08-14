package be.happli.pos.smartcity.Repositories.ApiService;

import java.util.List;

import be.happli.pos.smartcity.Repositories.DTO.LocationDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface LocationService {
    @GET("location/{locationId}")
    Call<LocationDTO> getLocation(@Path("locationId") Integer locationId);

    @GET("location/")
    Call<List<LocationDTO>> getAllLocations();
}
