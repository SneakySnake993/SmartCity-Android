package be.happli.pos.smartcity.UI.Login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import be.happli.pos.smartcity.Model.NetworkError;
import be.happli.pos.smartcity.Repositories.ApiService.LoginService;
import be.happli.pos.smartcity.Repositories.DTO.LoginDTO;
import be.happli.pos.smartcity.Repositories.RetrofitConfigurationService;
import be.happli.pos.smartcity.Utils.errors.NoConnectivityException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {
    private MutableLiveData<String> _token = new MutableLiveData<>();
    private LiveData<String> token = _token;

    private MutableLiveData<Integer> _statusCode = new MutableLiveData<>();
    private LiveData<Integer> statusCode = _statusCode;

    private MutableLiveData<NetworkError> _error = new MutableLiveData<>();
    private LiveData<NetworkError> error = _error;

    private LoginService loginService;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        this.loginService = RetrofitConfigurationService.getInstance(application).loginService();
    }

    public void login(String username, String password) {
        loginService.login(new LoginDTO(username, password)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {
                    _token.setValue(response.body());
                }
                _statusCode.setValue(response.code());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                _error.setValue(t instanceof NoConnectivityException ? NetworkError.NO_CONNECTION : NetworkError.TECHNICAL_ERROR);
            }
        });
    }

    public LiveData<String> getToken() {
        return token;
    }
    public LiveData<NetworkError> getError() {
        return error;
    }
    public LiveData<Integer> getStatusCode() {
        return statusCode;
    }
}