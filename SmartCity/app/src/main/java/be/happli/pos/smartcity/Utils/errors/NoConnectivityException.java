package be.happli.pos.smartcity.Utils.errors;

import java.io.IOException;

public class NoConnectivityException extends IOException {

    @Override
    public String getMessage() {
        return "No Internet Connection Exception";
    }
}

