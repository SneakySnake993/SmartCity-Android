package be.happli.pos.smartcity.UI.Home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import be.happli.pos.smartcity.Model.Location;
import be.happli.pos.smartcity.Model.NetworkError;
import be.happli.pos.smartcity.Repositories.ApiService.LocationService;
import be.happli.pos.smartcity.Repositories.DTO.LocationDTO;
import be.happli.pos.smartcity.Repositories.RetrofitConfigurationService;
import be.happli.pos.smartcity.Utils.errors.NoConnectivityException;
import be.happli.pos.smartcity.services.Mappers.LocationMapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends AndroidViewModel {

    private MutableLiveData<List<Location>> _locations = new MutableLiveData<>();
    private LiveData<List<Location>> locations = _locations;

    private MutableLiveData<Integer> _statusCode = new MutableLiveData<>();
    private LiveData<Integer> statusCode = _statusCode;

    private MutableLiveData<NetworkError> _error = new MutableLiveData<>();
    private LiveData<NetworkError> error = _error;

    private LocationService locationService;
    private LocationMapper locationMapper;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        this.locationService = RetrofitConfigurationService.getInstance(getApplication()).locationService();
        this.locationMapper = LocationMapper.getInstance();
    }

    public void getAllLocationsFromWeb() {
        locationService.getAllLocations().enqueue(new Callback<List<LocationDTO>>() {
            @Override
            public void onResponse(Call<List<LocationDTO>> call, Response<List<LocationDTO>> response) {
                if(response.isSuccessful()) {
                    List<Location> locations = new ArrayList<>();
                    for(LocationDTO locationDTO : response.body()) {
                        locations.add(locationMapper.mapToLocation(locationDTO));
                    }
                    _locations.setValue(locations);
                    _error.setValue(null);
                } else {
                    _error.setValue(NetworkError.REQUEST_ERROR);
                }
            }

            @Override
            public void onFailure(Call<List<LocationDTO>> call, Throwable t) {
                if (t instanceof NoConnectivityException) {
                    _error.setValue(NetworkError.NO_CONNECTION);
                } else {
                    _error.setValue(NetworkError.TECHNICAL_ERROR);
                }
            }
        });
    }

    public LiveData<List<Location>> getAllLocations() {
        return locations;
    }

    public LiveData<NetworkError> getError() {
        return error;
    }

    public LiveData<Integer> getStatusCode() {
        return statusCode;
    }
}