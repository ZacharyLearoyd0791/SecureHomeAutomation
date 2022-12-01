package ca.future.home.it.secure.home.automation;

import static android.content.ContentValues.TAG;

import static com.facebook.FacebookSdk.getApplicationContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.Context;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DatabaseActivity extends AppCompatActivity {

    //Database
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    UserInfo userInfo=new UserInfo();
    static Context context;

    //Date
    Date date;
    DateFormat dateFormat;

    //String
    String idKey,localKey,key,personalKey,strDate, lightKey, sensorKey,
            minKey,maxKey;

    int keyType; //1 = door, 2 = light, 3 = temperature, 4 = window

    public void toDatabase(String status) {
        DatabaseActivity.context = getApplicationContext();

        //Door idKey
        if (status.equals(getString(R.string.lock_status))) {
            dbID(1);
        }

        //Light idKey
        if (status.equals(getString(R.string.light_status))) {
            dbID(2);
        }

        //Temperature idKey
        if (status.equals(getString(R.string.temperature_status))) {
            dbID(3);
        }

        //Window idKey
        if (status.equals(getString(R.string.window_status))) {
            dbID(4);
        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child((idKey));

        //Door status
        if(status.equals(getString(R.string.lock_status))) {
            databaseReference.setValue(status);
            Map<String, Object> updateStatus = new HashMap<>();
            updateStatus.put(getString(R.string.status), status);
            databaseReference.updateChildren(updateStatus);
        }

        //Light status

        //Temperature status

        //Window status
    }

    private void dbID(int keyType){
        userInfo.typeAccount();
        time();

        localKey=userInfo.userId;
        personalKey=userInfo.idInfo;

        if(localKey!=null){
            key=localKey;
            Log.d(TAG,key);

        }
        if(personalKey!=null) {
            key= personalKey;
            Log.d(TAG, key);
        }

        //Door idKey
        if(keyType==1){
            idKey=key+getString(R.string.forwardslash)+getString(R.string.door_status)+getString(R.string.forwardslash)+strDate;
        }

        //Light idKey
        if(keyType==2){
            lightKey=key+getString(R.string.statusKey);
            sensorKey=key+getString(R.string.db_ultrasonic_dist);
        }

        //Temperature idKey
        if(keyType==3){
            minKey=key+getString(R.string.tempmin);
            maxKey=key+getString(R.string.tempmax);
        }

        //Window idKey
        if(keyType==4){}
    }

    @SuppressLint("SimpleDateFormat")
    private void time(){
        date = Calendar.getInstance().getTime();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            dateFormat = new SimpleDateFormat(getString(R.string.formatted));
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            strDate = dateFormat.format(date);
        }
        System.out.println("Converted String: " + strDate);
    }
}