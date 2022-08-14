package be.happli.pos.smartcity.Repositories.ApiService;

import java.util.List;

import be.happli.pos.smartcity.Repositories.DTO.CategoryDTO;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryService {
    @GET("category/")
    Call<List<CategoryDTO>> getAllCategories();
}
