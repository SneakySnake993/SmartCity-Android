package be.happli.pos.smartcity.Repositories;

import android.content.Context;

import com.squareup.moshi.Moshi;
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory;

import be.happli.pos.smartcity.Model.Place;
import be.happli.pos.smartcity.Repositories.ApiService.ActivityService;
import be.happli.pos.smartcity.Repositories.ApiService.BookmarkService;
import be.happli.pos.smartcity.Repositories.ApiService.CategoryService;
import be.happli.pos.smartcity.Repositories.ApiService.LocationService;
import be.happli.pos.smartcity.Repositories.ApiService.LoginService;
import be.happli.pos.smartcity.Repositories.ApiService.PlaceService;
import be.happli.pos.smartcity.Utils.ConnectivityCheckInterceptor;
import be.happli.pos.smartcity.Utils.HeaderInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class RetrofitConfigurationService {
    private static final String BASE_URL = "http://10.0.2.2:3001";

    // Retrofit client creation
    private Retrofit retrofitClient;

    private static LocationService locationService = null;
    private static ActivityService activityService = null;
    private static CategoryService categoryService = null;
    private static PlaceService placeService = null;
    private static LoginService loginService = null;
    private static BookmarkService bookmarkService = null;

    private RetrofitConfigurationService(Context context) {
        initializeRetrofit(context);
    }

    private void initializeRetrofit(Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityCheckInterceptor(context))
                .addInterceptor(new HeaderInterceptor(context))
                .build();

        Moshi moshiConverter = new Moshi.Builder()
                .add(new KotlinJsonAdapterFactory())
                .build();

        this.retrofitClient = new Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshiConverter))
                .build();
    }

    public static RetrofitConfigurationService getInstance(Context context) {
        return new RetrofitConfigurationService(context);
    }

    public LocationService locationService() {
        if (locationService == null) {
            locationService = retrofitClient.create(LocationService.class);
        }
        return locationService;
    }

    public ActivityService activityService() {
        if (activityService == null) {
            activityService = retrofitClient.create(ActivityService.class);
        }
        return activityService;
    }

    public CategoryService categoryService() {
        if (categoryService == null) {
            categoryService = retrofitClient.create(CategoryService.class);
        }
        return categoryService;
    }

    public PlaceService placeService() {
        if (placeService == null) {
            placeService = retrofitClient.create(PlaceService.class);
        }
        return placeService;
    }

    public LoginService loginService() {
        if (loginService == null) {
            loginService = retrofitClient.create(LoginService.class);
        }
        return loginService;
    }

    public BookmarkService bookmarkService() {
        if (bookmarkService == null) {
            bookmarkService = retrofitClient.create(BookmarkService.class);
        }
        return bookmarkService;
    }
}
