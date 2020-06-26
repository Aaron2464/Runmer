package com.aaron.runmer;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.aaron.runmer.api.RunmerParser;
import com.aaron.runmer.base.BaseActivity;
import com.aaron.runmer.map.MapPage;
import com.aaron.runmer.userdata.UserDataPage;
import com.aaron.runmer.util.Constants;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;

public class LoginActivity extends BaseActivity {
    private FirebaseAuth mAuth;
    CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        AppEventsLogger.activateApp(getApplication());
        LoginManager.getInstance().logInWithReadPermissions((Activity)mContext, Arrays.asList("public_profile", "email"));
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
         if (accessToken == null || accessToken.isExpired() || user == null) {
            // Initialize Facebook Login button
            mCallbackManager = CallbackManager.Factory.create();
            Button loginButton = findViewById(R.id.login_button);
//            mLoginButton.setReadPermissions("email");
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email"));
                    getkeyhash();
                    LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            Log.d(Constants.TAG, "facebook:onSuccess:" + loginResult);
                            handleFacebookAccessToken(loginResult.getAccessToken());
                        }

                        @Override
                        public void onCancel() {
                            Log.d(Constants.TAG, "facebook:onCancel");
                        }

                        @Override
                        public void onError(FacebookException error) {
                            Log.d(Constants.TAG, "facebook:onError", error);
                        }
                    });
                }
            });
        } else {
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, MapPage.class);
            startActivity(intent);
            LoginActivity.this.finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        //每個與 FacebookSDK 登入或分享整合的活動和片段，都應該將 onActivityResult 轉送至 callbackManager
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(Constants.TAG, "handleFacebookAccessToken:" + token.getToken());

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(Constants.TAG, "signInWithCredential:success");
                            RunmerParser.parseFirebaseUploadUserPhoto(Uri.parse(mAuth.getCurrentUser().getPhotoUrl() + "?type=large"));
                            Intent intent = new Intent(LoginActivity.this, UserDataPage.class);
                            startActivity(intent);
                            LoginActivity.this.finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(Constants.TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getkeyhash() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.facebook.samples.hellofacebook", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
