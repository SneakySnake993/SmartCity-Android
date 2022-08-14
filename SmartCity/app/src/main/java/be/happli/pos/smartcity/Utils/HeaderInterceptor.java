package be.happli.pos.smartcity.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {
    private Context Context;

    public HeaderInterceptor(Context Context) {
        this.Context = Context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        SharedPreferences sharedPreferences = Context.getSharedPreferences(Constants.TOKEN, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(Constants.TOKEN, null);

        Request newRequest = chain.request().newBuilder()
                .addHeader("authorization", "Bearer " + token)
                .build();

        return chain.proceed(newRequest);
    }
}
