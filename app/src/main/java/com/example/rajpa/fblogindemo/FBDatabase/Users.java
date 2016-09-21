package com.example.rajpa.fblogindemo.FBDatabase;

import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ser.std.StaticListSerializerBase;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rajpa on 03-Sep-16.
 */
public class Users {

    Firebase usersLinkRef = new Firebase("https://facebooklogindemo-8f640.firebaseio.com/user");
    public String id, name, email, profile_picture, username;
    public String password, birthday, doj, gender, city, state, country;

    public static Users users = new Users();

    public static final String TABLE_NAME = "user";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PROFILE_PICTURE = "profile_picture";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_BIRTHDAY = "birthday";
    public static final String COLUMN_DOJ = "doj";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_STATE = "state";
    public static final String COLUMN_COUNTRY = "country";

    public Users() {
    }

    public Users(String name, String id, String username, String profile_picture) {
        this.name = name;
        this.id = id;
        this.username = username;
        this.profile_picture = profile_picture;
    }

    public Users(String id, String name, String email, String profile_picture, String username,
                 String password, String birthday, String doj, String gender, String city, String state, String country) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profile_picture = profile_picture;
        this.username = username;
        this.password = password;
        this.birthday = birthday;
        this.doj = doj;
        this.gender = gender;
        this.city = city;
        this.state = state;
        this.country = country;
    }
    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setDoj(String doj) {
        this.doj = doj;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCountry(String country) {    this.country = country;    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public String getUsername() {
        return username;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getDoj() {
        return doj;
    }

    public String getGender() {
        return gender;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Users getUser(final String uid){
        final Query query = usersLinkRef.child(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("Query is", query.toString());
                Log.e("Uid is", uid);
                if (dataSnapshot.exists()){
                    users = dataSnapshot.getValue(Users.class);
                    Log.e("Inside getUser found", users.getUsername());
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
        return users;
    }
}

