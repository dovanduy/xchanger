package com.example.erikiado.myapplication.backend;

import retrofit.RestAdapter;
import retrofit.appengine.UrlFetchClient;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by erikiado on 9/21/16.
 */

public class RetroAdapter {
    public static final String API_BASE_URL = "https://graph.accountkit.com";

    private static RestAdapter.Builder builder = new RestAdapter.Builder()
            .setEndpoint(API_BASE_URL)
            .setClient(new UrlFetchClient());

    public static <S> S createService(Class<S> serviceClass) {
        RestAdapter adapter = builder.build();
        return adapter.create(serviceClass);
    }


    public  interface GetUserDetailsService{
        @GET("/v1.0/me")
        AccountKitUser getUserDetails(@Query("access_token") String accessToken);

    }
}
