package be.happli.pos.smartcity.UI.Bookmarks;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import be.happli.pos.smartcity.Model.NetworkError;
import be.happli.pos.smartcity.Model.Place;
import be.happli.pos.smartcity.Repositories.ApiService.BookmarkService;
import be.happli.pos.smartcity.Repositories.ApiService.PlaceService;
import be.happli.pos.smartcity.Repositories.DTO.PlaceDTO;
import be.happli.pos.smartcity.Repositories.RetrofitConfigurationService;
import be.happli.pos.smartcity.Utils.errors.NoConnectivityException;
import be.happli.pos.smartcity.services.Mappers.PlaceMapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookmarksViewModel extends AndroidViewModel {

    private MutableLiveData<List<Place>> _bookmarkedPlaces = new MutableLiveData<>();
    private LiveData<List<Place>> bookmarkedPlaces = _bookmarkedPlaces;

    private MutableLiveData<List<Place>> _bookmarkedPlaceDeleted = new MutableLiveData<>();
    private LiveData<List<Place>> bookmarkedPlaceDeleted = _bookmarkedPlaceDeleted;

    private MutableLiveData<List<Place>> _bookmarkedPlaceAdded = new MutableLiveData<>();
    private LiveData<List<Place>> bookmarkedPlaceAdded = _bookmarkedPlaceAdded;

    private MutableLiveData<NetworkError> _error = new MutableLiveData<>();
    private LiveData<NetworkError> error = _error;

    private BookmarkService bookmarkService;
    private PlaceMapper placeMapper;

    public BookmarksViewModel(@NonNull Application application) {
        super(application);
        this.bookmarkService = RetrofitConfigurationService.getInstance(getApplication()).bookmarkService();
        this.placeMapper = PlaceMapper.getInstance();
    }

    public void getBookmarkedPlacesFromWeb() {
        bookmarkService.getAllUserPlaces().enqueue(new Callback<List<PlaceDTO>>() {
            @Override
            public void onResponse(Call<List<PlaceDTO>> call, Response<List<PlaceDTO>> response) {
                if(response.isSuccessful()) {
                    List<Place> places = new ArrayList<>();
                    for(PlaceDTO placeDTO : response.body()) {
                        places.add(placeMapper.mapToPlace(placeDTO));
                    }
                    _bookmarkedPlaces.setValue(places);
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

    public void deleteBookmarkedPlaceFromWeb(Integer idPlace) {
        bookmarkService.deleteUserPlace(idPlace).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful()) {
                    _bookmarkedPlaceDeleted.setValue(null);
                } else {
                    _error.setValue(NetworkError.REQUEST_ERROR);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (t instanceof NoConnectivityException) {
                    _error.setValue(NetworkError.NO_CONNECTION);
                } else {
                    _error.setValue(NetworkError.TECHNICAL_ERROR);
                }
            }
        });
    }

    public void addBookmarkedPlaceFromWeb(Integer idPlace) {
        bookmarkService.addUserPlace(idPlace).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful()) {
                    _bookmarkedPlaceAdded.setValue(null);
                } else {
                    _error.setValue(NetworkError.REQUEST_ERROR);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (t instanceof NoConnectivityException) {
                    _error.setValue(NetworkError.NO_CONNECTION);
                } else {
                    _error.setValue(NetworkError.TECHNICAL_ERROR);
                }
            }
        });
    }

    public LiveData<List<Place>> getBookmarkedPlaceDeleted() {return bookmarkedPlaceDeleted;}

    public LiveData<List<Place>> getBookmarkedPlaceAddedd() {return bookmarkedPlaceAdded;}

    public LiveData<List<Place>> getBookmarkedPlaces() {
        return bookmarkedPlaces;
    }

    public LiveData<NetworkError> getError() {
        return error;
    }
}