package com.example.rajpa.fblogindemo;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rajpa.fblogindemo.Adapter.UserAdapter;
import com.example.rajpa.fblogindemo.FBDatabase.Users;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {

    RecyclerView search_RecyclerView;
    private List<Users> userList = new ArrayList<>();
    private UserAdapter uAdapter;

    private Users users = new Users();

    public EditText ed_search;
    public ImageView image_search;

    private static SharedPreferences usersharedpref;
    private static String USER_ID = "user_id_key";
    private static String user_id = null;

    public static ProgressDialog progressDialog;

    Firebase rootLinkRef = new Firebase("https://facebooklogindemo-8f640.firebaseio.com");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search_RecyclerView = (RecyclerView)findViewById(R.id.recyclerView_search);

        usersharedpref = PreferenceManager.getDefaultSharedPreferences(this);
        progressDialog = new ProgressDialog(this);

        user_id = usersharedpref.getString(USER_ID,"");

        uAdapter = new UserAdapter(userList, getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        search_RecyclerView.setLayoutManager(layoutManager);
        search_RecyclerView.setItemAnimator(new DefaultItemAnimator());
        search_RecyclerView.setAdapter(uAdapter);

        image_search = (ImageView) findViewById(R.id.image_search);
        image_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_search = (EditText) findViewById(R.id.ed_search);
                featch_selected_users(ed_search.getText().toString());
            }
        });
    }
    public void featch_selected_users(String name){
        final Query query = rootLinkRef.child(Users.TABLE_NAME).orderByChild(Users.COLUMN_USERNAME).startAt(name);
        userList.clear();
        progressDialog.setMessage("Searching...");
        progressDialog.show();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    userList.clear();
                    uAdapter.notifyDataSetChanged();
                    for(DataSnapshot userdatasnapshot: dataSnapshot.getChildren()){
                        Users userValue = userdatasnapshot.getValue(Users.class);
                        if(userValue.getId().equals(user_id)){

                        } else{
                            users.setName(userValue.name);
                            users.setId(userValue.getId());
                            users.setUsername(userValue.getUsername());
                            //users = new Users(userValue.name, userValue.getId(), userValue.getUsername(), userValue.getProfile_picture());
                            userList.add(users);
                        }
                        Log.i("Return value ", userValue.getUsername()+" "+userValue.getName()+" "+userValue.getProfile_picture());
                        progressDialog.dismiss();
                    }
                    uAdapter.notifyDataSetChanged();
                }
                else {
                    progressDialog.dismiss();
                    userList.clear();
                    uAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(),"No User exist.",Toast.LENGTH_SHORT).show();
                    Log.i("Return value ", "No value return.");
                }
                query.removeEventListener(this);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                progressDialog.dismiss();
                query.removeEventListener(this);
            }
        });
    }
}
