package com.erikiado.xchange.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.erikiado.xchange.R;
import com.example.erikiado.myapplication.xbackend.myApi.MyApi;
import com.example.erikiado.myapplication.xbackend.myApi.model.CustomTokenBean;
import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

public class ActivityLogin extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public static int APP_REQUEST_CODE = 99;
    private Button butt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        final AccessToken accessToken = AccountKit.getCurrentAccessToken();

        butt = (Button)findViewById(R.id.button);
        Button logout = (Button)findViewById(R.id.logout);
        TextView bt = (TextView) findViewById(R.id.text);


        if (accessToken != null) {
            //Handle Returning User
            butt.setText("Loggeado, Entrar");
            Snackbar.make(butt,"Si",Snackbar.LENGTH_LONG).show();
        }

        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view,"click",Snackbar.LENGTH_LONG).show();
                if(accessToken != null){
                    Intent intent = new Intent(ActivityLogin.this,ActivityMain.class);
                    startActivity(intent);
                    finish();
                }else{
                    onLoginPhone(view);
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view,"click",Snackbar.LENGTH_LONG).show();
                AccountKit.logOut();
                butt.setText("Nel");
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed into Firebase
                    startActivity(new Intent(ActivityLogin.this, ActivityMain.class));

                } else {
                    // User is signed out of Firebase
                    // Check if user is still signed into FB AccountKit
                    AccessToken accountKitAccessToken = AccountKit.getCurrentAccessToken();

                    if (accountKitAccessToken != null) {
                        //user is already logged into FB AccountKit, skip login, and try to authenticate Firebase
                        new LoginUserAsyncTask().execute(accountKitAccessToken.getToken());
                        startActivity(new Intent(ActivityLogin.this, ActivityMain.class));


                    } else {
                        //Handle new or logged out user
                    }

                }
                // ...
            }
        };
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        // ...
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        // ...
    }

    public void onLoginPhone(final View view) {
        final Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN); // you must use TOKEN response type
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage = null;
            if (loginResult.getError() != null) {
                toastMessage = loginResult.getError().getErrorType().getMessage();
                //showErrorActivity(loginResult.getError());
            } else if (loginResult.wasCancelled()) {
                toastMessage = "Login Cancelled";
            } else {
                if (loginResult.getAccessToken() != null) {

                    String accessToken = loginResult.getAccessToken().getToken();

//                    AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
//                        @Override
//                        public void onSuccess(Account account) {
//
//                        }
//
//                        @Override
//                        public void onError(AccountKitError accountKitError) {
//
//                        }
//                    });
                    new LoginUserAsyncTask().execute(accessToken);

                    toastMessage = "AccountId:" + loginResult.getAccessToken().getAccountId();

                } else {
                    toastMessage = "Login failed";
                }

            }
            Toast.makeText(
                    this,
                    toastMessage,
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    class LoginUserAsyncTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPostExecute(String token) {
            super.onPostExecute(token);
            //uses returned custom token to login to Firebase
            mAuth.signInWithCustomToken(token).addOnCompleteListener(ActivityLogin.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful()) {
                        Toast.makeText(ActivityLogin.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }else{
//                        Toast.makeText(ActivityLogin.this, "Authentication Success.",
//                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ActivityLogin.this,ActivityMain.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }


        @Override
        protected String doInBackground(String... strings) {

            //sends AccountKit token to authentication server
            String accountsKitAccessToken = strings[0];
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null);
            builder.setRootUrl("https://xchange-543c1.appspot.com/_ah/api/");
            builder.setApplicationName("xchange-543c1");
            MyApi service = builder.build();
            try {
                CustomTokenBean customTokenBean = service.getCustomToken(accountsKitAccessToken).execute();
                return customTokenBean.getData();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
