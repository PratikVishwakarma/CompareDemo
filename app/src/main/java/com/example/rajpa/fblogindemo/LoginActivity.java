package com.example.rajpa.fblogindemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rajpa.fblogindemo.DatabaseHandler.FB_User;
import com.example.rajpa.fblogindemo.FBDatabase.Users;
import com.example.rajpa.fblogindemo.Utility.Utility;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "AndroidBash";
    private static String LOGEDIDSTATUS = "false";
    public Users user;
    private EditText email;
    private EditText password;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog mProgressDialog;

    private static SharedPreferences usersharedpref;
    SharedPreferences.Editor editor;
    private static String USER_ID = "user_id_key";
    private static Boolean LOGIN_STATUS = Boolean.FALSE;
    private static Boolean USERNAME_STATUS = Boolean.FALSE;

    private static String _uid = null,_name = null, _email=null, _image=null;

    //Add YOUR Firebase Reference URL instead of the following URL
    public static Firebase mRef;

    //FaceBook callbackManager
    private CallbackManager callbackManager;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        usersharedpref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = usersharedpref.edit();

        mRef = new Firebase("https://facebooklogindemo-8f640.firebaseio.com/");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();

        if(usersharedpref.getBoolean(String.valueOf(LOGIN_STATUS),Boolean.FALSE) == Boolean.TRUE && usersharedpref.getBoolean(String.valueOf(USERNAME_STATUS),Boolean.FALSE)== Boolean.TRUE){
            Intent intent = new Intent(getApplicationContext(), AddPost.class);
            startActivity(intent);
        } else if(usersharedpref.getBoolean(String.valueOf(LOGIN_STATUS),Boolean.FALSE) == Boolean.TRUE && usersharedpref.getBoolean(String.valueOf(USERNAME_STATUS),Boolean.FALSE)== Boolean.FALSE){
            Intent intent = new Intent(getApplicationContext(), CreateUsername.class);
            startActivity(intent);
        }else {
            //FaceBook
            FacebookSdk.sdkInitialize(getApplicationContext());
            callbackManager = CallbackManager.Factory.create();
            LoginButton loginButton = (LoginButton) findViewById(R.id.button_facebook_login);
            loginButton.setReadPermissions("email", "public_profile");
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.d(TAG, "facebook:onSuccess:" + loginResult);
                    signInWithFacebook(loginResult.getAccessToken());
                }

                @Override
                public void onCancel() {
                    Log.d(TAG, "facebook:onCancel");
                }

                @Override
                public void onError(FacebookException error) {
                    Log.d(TAG, "facebook:onError", error);
                }
            });
            Toast.makeText(getApplicationContext(),"LogedIn status :- False", Toast.LENGTH_SHORT).show();
        }

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mUser = firebaseAuth.getCurrentUser();

                if (mUser != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + mUser.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    public void afterSignIn(final String user_id){
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        final Query usernameCheck = mRef.child("user").child(user_id);
        usernameCheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Users value = dataSnapshot.getValue(Users.class);
                    Toast.makeText(getApplicationContext(),"Username Equality :- "+value.getUsername(), Toast.LENGTH_SHORT).show();
                    if(value.getUsername().equals(user_id)){
                        mProgressDialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), CreateUsername.class);
                        startActivity(intent);
                        finish();
                    }else {
                        mProgressDialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), AddPost.class);
                        String image=mAuth.getCurrentUser().getPhotoUrl().toString();
                        intent.putExtra("user_id", user_id);
                        if(image!=null || image!=""){
                            intent.putExtra("profile_picture",image);
                        }
                        editor.putBoolean(String.valueOf(USERNAME_STATUS), Boolean.TRUE);
                        mRef.child("user").removeEventListener(this);
                        startActivity(intent);
                        finish();
                    }
                }
                mProgressDialog.dismiss();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        Toast.makeText(getApplicationContext(),"LogedIn status :- True", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        email = (EditText) findViewById(R.id.edit_text_email_id);
        password = (EditText) findViewById(R.id.edit_text_password);
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    //FaceBook
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    //

    protected void setUpUser() {
        user = new Users();
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());
    }

    public void onSignUpClicked(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void onLoginClicked(View view) {
        setUpUser();
        signIn(email.getText().toString(), password.getText().toString());
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            String user_id = mAuth.getCurrentUser().getUid();
                            SharedPreferences.Editor editor = usersharedpref.edit();
                            editor.putString(USER_ID,user_id);
                            editor.putBoolean(String.valueOf(LOGIN_STATUS),Boolean.TRUE);
                            editor.apply();
                            afterSignIn(user_id);
                        }
                    hideProgressDialog();
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String userEmail = email.getText().toString();
        if (TextUtils.isEmpty(userEmail)) {
            email.setError("Required.");
            valid = false;
        } else {
            email.setError(null);
        }

        String userPassword = password.getText().toString();
        if (TextUtils.isEmpty(userPassword)) {
            password.setError("Required.");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }


    private void signInWithFacebook(AccessToken token) {
        Log.d(TAG, "signInWithFacebook:" + token);

        showProgressDialog();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            final FirebaseUser currentUser = mAuth.getCurrentUser();
                            //currentUser.getUid();
                            _uid = task.getResult().getUser().getUid();
                            _name = task.getResult().getUser().getDisplayName();
                            _email = task.getResult().getUser().getEmail();
                            _image = task.getResult().getUser().getPhotoUrl().toString();
                            Query checkUserexistance =mRef.child("user").child(currentUser.getUid());
                            Toast.makeText(LoginActivity.this, "Authentication sucess. "+ currentUser.getUid(),
                                    Toast.LENGTH_SHORT).show();
                            checkUserexistance.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        Users value = dataSnapshot.getValue(Users.class);
                                        Log.e("Value of username", value.getUsername());
                                        if(value.getUsername().equals(currentUser.getUid())){
                                            Intent intent = new Intent(getApplicationContext(), CreateUsername.class);
                                            intent.putExtra("user_id",_uid);
                                            mRef.child("user").removeEventListener(this);
                                            startActivity(intent);
                                            finish();
                                        }else {
                                            Intent intent = new Intent(getApplicationContext(), AddPost.class);
                                            String image=mAuth.getCurrentUser().getPhotoUrl().toString();
                                            mRef.child("user").removeEventListener(this);
                                            editor.putBoolean(String.valueOf(USERNAME_STATUS), Boolean.TRUE);
                                            editor.apply();
                                            startActivity(intent);
                                            finish();
                                        }
                                    }else{
                                        //Create a new User and Save it in Firebase database
                                        Users user = new Users(_uid,_name,_email,_image,_uid,_uid, "dummy",new Utility().getCurrentDate(), "dummy", _uid, _uid, _uid);
                                        Firebase tableuserRef = mRef.child("user");
                                        tableuserRef.child(_uid).setValue(user);
                                        Intent intent = new Intent(getApplicationContext(), CreateUsername.class);
                                        intent.putExtra("user_id",_uid);
                                        mRef.child("user").removeEventListener(this);
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });
                            editor.putString(USER_ID,_uid);
                            editor.putBoolean(String.valueOf(LOGIN_STATUS),Boolean.TRUE);
                            editor.apply();
                        }
                        hideProgressDialog();
                    }
                });
    }


    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
