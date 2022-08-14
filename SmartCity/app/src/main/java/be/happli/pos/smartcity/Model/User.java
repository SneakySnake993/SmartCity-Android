package be.happli.pos.smartcity.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

public class User {
    private String username;
    private String password;
    private Boolean isAdmin;

    public User(String username, String password, Boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }
}
