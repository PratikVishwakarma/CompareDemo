package com.example.rajpa.fblogindemo.Utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by prati on 05-Sep-16.
 */
public class Utility {
    public String getCurrentDate(){
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String todayDate = df.format(c.getTime());
        return todayDate;
    }
    public void getLogedInSharedPreferences(String uid, boolean status){

    }
}
