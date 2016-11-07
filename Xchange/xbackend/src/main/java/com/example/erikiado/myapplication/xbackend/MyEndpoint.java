/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.example.erikiado.myapplication.xbackend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.inject.Named;

import static com.example.erikiado.myapplication.xbackend.RetroAdapter.createService;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "xbackend.myapplication.erikiado.example.com",
                ownerName = "xbackend.myapplication.erikiado.example.com",
                packagePath = ""
        )
)
public class MyEndpoint {

    /**
     * A simple endpoint method that takes a name and says Hi back
     */
    @ApiMethod(name = "sayHi")
    public MyBean sayHi(@Named("name") String name) {
        MyBean response = new MyBean();
        response.setData("Hi, " + name);

        return response;
    }


    @ApiMethod(name = "getCustomToken")
    public CustomTokenBean getCustomToken(@Named("access_token") String accessToken) throws FileNotFoundException {


        RetroAdapter.GetUserDetailsService getUserDetailsService = createService(RetroAdapter.GetUserDetailsService.class);
        AccountKitUser retrofitResponse = getUserDetailsService.getUserDetails(accessToken);
        String uid = retrofitResponse.getId();
        FirebaseOptions options = null;
        options = new FirebaseOptions.Builder()
                .setServiceAccount(new FileInputStream(
                        new File("WEB-INF/xchange-b0ea5bc4f3af.json")))
                .setDatabaseUrl("https://xchange-543c1.firebaseio.com/")
                .build();
        FirebaseApp.initializeApp(options);
        String customToken = FirebaseAuth.getInstance().createCustomToken(uid);

        CustomTokenBean response = new CustomTokenBean();
        response.setData(customToken);

        return response;
    }
}
