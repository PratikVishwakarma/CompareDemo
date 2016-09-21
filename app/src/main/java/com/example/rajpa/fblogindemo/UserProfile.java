package com.example.rajpa.fblogindemo;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rajpa.fblogindemo.DatabaseHandler.Single_post;
import com.example.rajpa.fblogindemo.FBDatabase.Post;
import com.example.rajpa.fblogindemo.FBDatabase.Users;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UserProfile extends AppCompatActivity {

    private static SharedPreferences usersharedpref;
    SharedPreferences.Editor editor;
    private static String USER_ID = "user_id_key";
    private static Boolean LOGIN_STATUS = Boolean.FALSE;
    private static Boolean USERNAME_STATUS = Boolean.FALSE;

    Firebase rootLinkRef = new Firebase("https://facebooklogindemo-8f640.firebaseio.com");
    private StorageReference mStorage;
    private FirebaseStorage storage;

    public static Users users = new Users();
    public static Post P_post = new Post();
    public static Single_post P_single_post = new Single_post();

    private static String user_id = null, Uusername = null;;

    private ImageView profile_picture;
    private TextView username, name;

    //Add YOUR Firebase Reference URL instead of the following URL
    public static Firebase mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_user_profile);

        usersharedpref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = usersharedpref.edit();

        username = (TextView) findViewById(R.id.text_username);
        name = (TextView) findViewById(R.id.text_name);

        user_id = usersharedpref.getString(USER_ID,"");
        getUser(user_id);

    }

    public void getUser(String user_id){
        final Query query = rootLinkRef.child(Users.TABLE_NAME).child(user_id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    long time= System.currentTimeMillis();
                    users = dataSnapshot.getValue(Users.class);
                    username.setText(users.getUsername());
                    name.setText(users.getName());
                    Log.e("Inside getUser found", users.getUsername()+time);
                }else{
                    users = null;
                    Log.e("not found", users.getId());
                }
                query.removeEventListener(this);
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                query.removeEventListener(this);
            }
        });
    }



}
