package com.example.rajpa.fblogindemo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.rajpa.fblogindemo.FBDatabase.Users;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreateUsername extends AppCompatActivity{

    public EditText edusername;
    public TextView ed_dob;
    public DatePicker date_dob;
    public Button save;
    private RadioButton rdFemale, rdMale;
    private ProgressDialog progressDialog;
    public static boolean status;

    private static String user_id;
    private static SharedPreferences usersharedpref;
    SharedPreferences.Editor editor;
    private static String USER_ID = "user_id_key";
    private static Boolean LOGIN_STATUS = Boolean.FALSE;
    private static Boolean USERNAME_STATUS = Boolean.FALSE;

    public static String newusername = null, dob = null, gender = null;

    Firebase rootref = new Firebase("https://facebooklogindemo-8f640.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_username);

        progressDialog = new ProgressDialog(this);
        usersharedpref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = usersharedpref.edit();
        user_id = usersharedpref.getString(user_id,"");

        date_dob = (DatePicker)findViewById(R.id.date_dob);
        date_dob.setVisibility(View.GONE);
        ed_dob = (TextView)findViewById(R.id.ed_dob);

        rdFemale = (RadioButton)findViewById(R.id.radioButton_gender_male);
        rdMale = (RadioButton)findViewById(R.id.radioButton_gender_female);
        rdFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "female";
                rdMale.setChecked(false);
            }
        });
        rdMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "male";
                rdFemale.setChecked(false);
            }
        });

        ed_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date_dob.setVisibility(View.VISIBLE);
            }
        });

        save = (Button)findViewById(R.id.button_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = date_dob.getDayOfMonth()+"";
                String month = date_dob.getMonth()+"";
                String year = date_dob.getYear()+"";
                dob = date+"-"+month+"-"+year;
                ed_dob.setText(dob);
                date_dob.setVisibility(View.GONE);
                edusername = (EditText)findViewById(R.id.et_username);
                newusername = edusername.getText().toString();
                if (newusername.length()>3 && newusername.length()<19){
                    //Toast.makeText(getApplicationContext(), "Username "+ newusername +"/n DOB "+ dob +"/n gender "+gender,Toast.LENGTH_SHORT).show();
                    progressDialog.setMessage("Checking Username");
                    progressDialog.show();
                    final Query usernameCheck = rootref.child("user").orderByChild("username").equalTo(newusername);
                    usernameCheck.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),newusername+" Username already exist...",Toast.LENGTH_SHORT).show();
                            }else{
                                progressDialog.dismiss();
                                Firebase userRef = rootref.child("user").child(user_id);
                                Map<String, Object> userInfo = new HashMap<String, Object>();
                                userInfo.put(Users.COLUMN_BIRTHDAY, dob);
                                userInfo.put(Users.COLUMN_GENDER, gender);
                                userInfo.put(Users.COLUMN_USERNAME, newusername);
                                userRef.updateChildren(userInfo);
                                editor.putBoolean(String.valueOf(USERNAME_STATUS),Boolean.TRUE);
                                editor.apply();
                                Intent intent = new Intent(getApplicationContext(), AddPost.class);
                                startActivity(intent);
                                //Intent intent = new Intent(getApplicationContext(), UserProfile.class);
                                //Toast.makeText(getApplicationContext(),newusername+" Username not exist "+status,Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(FirebaseError firebaseError) { }
                    });
                }else {}
            }
        });
    }

    boolean checkUsername(final String username){
        progressDialog.setMessage("Checking Username");
        progressDialog.show();
        final Query usernameCheck = rootref.child("user").orderByChild("username").equalTo(newusername);
        usernameCheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),username+" Username exist",Toast.LENGTH_SHORT).show();
                    status = false;
                }else{
                    progressDialog.dismiss();
                    status = true;
                    Toast.makeText(getApplicationContext(),username+" Username not exist "+status,Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) { }
        });
        Toast.makeText(getApplicationContext(),username+" U "+status,Toast.LENGTH_SHORT).show();
        return status;
    }
}