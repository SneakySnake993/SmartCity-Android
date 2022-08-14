package be.happli.pos.smartcity.Repositories.ApiService;

import com.google.gson.JsonObject;

import be.happli.pos.smartcity.Repositories.DTO.LoginDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {
    @POST("user/login")
    Call<String> login(@Body LoginDTO loginDto);
}
