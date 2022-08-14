package be.happli.pos.smartcity.Repositories.ApiService;

import java.util.List;

import be.happli.pos.smartcity.Repositories.DTO.PlaceDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlaceService {
    @GET("place")
    Call<List<PlaceDTO>> getPlacesByActivityLocation(@Query("location_id") Integer location_id,
                                                     @Query("activity_id") Integer activity_id);
}
