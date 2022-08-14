package be.happli.pos.smartcity.Repositories.ApiService;

import java.util.List;

import be.happli.pos.smartcity.Repositories.DTO.ActivityDTO;
import be.happli.pos.smartcity.Repositories.DTO.PlaceDTO;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BookmarkService {
    @GET("bookmark")
    Call<List<PlaceDTO>> getAllUserPlaces();

    @DELETE("bookmark")
    Call<Object> deleteUserPlace(@Query("idPlace") Integer idPlace);

    @POST("bookmark")
    Call<Object> addUserPlace(@Query("idPlace") Integer idPlace);
}
