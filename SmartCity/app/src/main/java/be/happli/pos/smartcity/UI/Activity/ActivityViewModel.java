package be.happli.pos.smartcity.UI.Activity;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import be.happli.pos.smartcity.Model.Activity;
import be.happli.pos.smartcity.Model.NetworkError;
import be.happli.pos.smartcity.Repositories.ApiService.ActivityService;
import be.happli.pos.smartcity.Repositories.DTO.ActivityDTO;
import be.happli.pos.smartcity.Repositories.RetrofitConfigurationService;
import be.happli.pos.smartcity.Utils.errors.NoConnectivityException;
import be.happli.pos.smartcity.services.Mappers.ActivityMapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;

public class ActivityViewModel extends AndroidViewModel {
    private MutableLiveData<List<Activity>> _activities = new MutableLiveData<>();
    private LiveData<List<Activity>> activities = _activities;

    private MutableLiveData<NetworkError> _error = new MutableLiveData<>();
    private LiveData<NetworkError> error = _error;

    private ActivityService activityService;
    private ActivityMapper activityMapper;


    public ActivityViewModel(@NonNull Application application) {
        super(application);
        this.activityService = RetrofitConfigurationService.getInstance(getApplication()).activityService();
        this.activityMapper = ActivityMapper.getInstance();
    }

    public void getAllActivitiesFromWeb(Integer location_id, Integer[] categories_id) {

        activityService.getAllActivities(location_id, categories_id).enqueue(new Callback<List<ActivityDTO>>() {
            @Override
            public void onResponse(Call<List<ActivityDTO>> call, Response<List<ActivityDTO>> response) {
                if(response.isSuccessful()) {
                    List<Activity> activities = new ArrayList<>();
                    for(ActivityDTO activityDTO : response.body()) {
                        activities.add(activityMapper.mapToActivity(activityDTO));
                    }
                    _activities.setValue(activities);
                    _error.setValue(null);
                } else {
                    _error.setValue(NetworkError.REQUEST_ERROR);
                }
            }

            @Override
            public void onFailure(Call<List<ActivityDTO>> call, Throwable t) {
                if (t instanceof NoConnectivityException) {
                    _error.setValue(NetworkError.NO_CONNECTION);
                } else {
                    _error.setValue(NetworkError.TECHNICAL_ERROR);
                }
            }
        });
    }

    public LiveData<List<Activity>> getAllActivities() {
        return activities;
    }
    public LiveData<NetworkError> getError() {
        return error;
    }
}