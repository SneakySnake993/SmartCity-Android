package be.happli.pos.smartcity.Repositories.ApiService;

import java.util.List;

import be.happli.pos.smartcity.Repositories.DTO.ActivityDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ActivityService {
    @GET("activity/{activityId}")
    Call<ActivityDTO> getActivity(@Path("activityId") Integer activityId);

    @GET("activity")
    Call<List<ActivityDTO>> getAllActivities(@Query("location_id") Integer location_id,
                                             @Query("categories_id") Integer[] categories_id);
}
