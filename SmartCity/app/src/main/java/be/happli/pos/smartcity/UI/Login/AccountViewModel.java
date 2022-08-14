package be.happli.pos.smartcity.UI.Login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

import be.happli.pos.smartcity.Model.User;
import be.happli.pos.smartcity.Repositories.DTO.UserDTO;
import be.happli.pos.smartcity.services.Mappers.UserMapper;

public class AccountViewModel extends AndroidViewModel {
    private MutableLiveData<User> _user = new MutableLiveData<>();
    private LiveData<User> user = _user;

    public AccountViewModel(@NonNull Application application) {
        super(application);
    }

    public void getUserFromToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        Date expirationDate = decodedJWT.getExpiresAt();
        Date today = new Date();
        boolean isDateExpired = expirationDate.before(today);
        if(!isDateExpired) {
            Claim userData = decodedJWT.getClaim("username");
            //TODO : only the username is usefull
            UserDTO userDTO = new UserDTO(userData.asString(), null, null);
            User user = UserMapper.getInstance().mapToUser(userDTO);
            _user.setValue(user);
        } else {
            _user.setValue(null);
        }
    }

    public LiveData<User> getUser() {
        return user;
    }

    public void resetUser() {
        _user.setValue(null);
    }
}
