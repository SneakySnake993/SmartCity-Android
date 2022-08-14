package be.happli.pos.smartcity.UI.Place;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import be.happli.pos.smartcity.Model.Place;
import be.happli.pos.smartcity.Model.NetworkError;
import be.happli.pos.smartcity.Repositories.ApiService.PlaceService;
import be.happli.pos.smartcity.Repositories.DTO.PlaceDTO;
import be.happli.pos.smartcity.Repositories.RetrofitConfigurationService;
import be.happli.pos.smartcity.Utils.errors.NoConnectivityException;
import be.happli.pos.smartcity.services.Mappers.PlaceMapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceViewModel extends AndroidViewModel {

    private MutableLiveData<List<Place>> _places = new MutableLiveData<>();
    private LiveData<List<Place>> places = _places;

    private MutableLiveData<NetworkError> _error = new MutableLiveData<>();
    private LiveData<NetworkError> error = _error;

    private PlaceService placeService;
    private PlaceMapper placeMapper;

    public PlaceViewModel(@NonNull Application application) {
        super(application);
        this.placeService = RetrofitConfigurationService.getInstance(getApplication()).placeService();
        this.placeMapper = PlaceMapper.getInstance();
    }

    public void getPlacesFromWeb(Integer location_id, Integer activity_id) {
        placeService.getPlacesByActivityLocation(location_id, activity_id).enqueue(new Callback<List<PlaceDTO>>() {
            @Override
            public void onResponse(Call<List<PlaceDTO>> call, Response<List<PlaceDTO>> response) {
                if(response.isSuccessful()) {
                    List<Place> places = new ArrayList<>();
                    for(PlaceDTO placeDTO : response.body()) {
                        places.add(placeMapper.mapToPlace(placeDTO));
                    }
                    _places.setValue(places);
                    _error.setValue(null);
                } else {
                    _error.setValue(NetworkError.REQUEST_ERROR);
                }
            }

            @Override
            public void onFailure(Call<List<PlaceDTO>> call, Throwable t) {
                if (t instanceof NoConnectivityException) {
                    _error.setValue(NetworkError.NO_CONNECTION);
                } else {
                    _error.setValue(NetworkError.TECHNICAL_ERROR);
                }
            }
        });
    }

    public LiveData<List<Place>> getPlaces() {
        return places;
    }

    public LiveData<NetworkError> getError() {
        return error;
    }
}
