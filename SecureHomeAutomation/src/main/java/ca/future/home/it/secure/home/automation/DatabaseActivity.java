package ca.future.home.it.secure.home.automation;

import static android.content.ContentValues.TAG;

import static com.facebook.FacebookSdk.getApplicationContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.FragmentTransaction;
import android.content.Context;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DatabaseActivity extends Fragment {

    //Database
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    UserInfo userInfo=new UserInfo();
    static Context context;

    //Date
    Date date;
    DateFormat dateFormat;

    //String
    String idKey,localKey,key,personalKey,strDate,sensorKey,lightKey,statusKey,SensorKey,doorKey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseDatabase = FirebaseDatabase.getInstance();
        initV();
        dbID();
        time();


    }

    private void initV() {
        time();

        Log.d(TAG,"Key init");
        doorKey=getApplicationContext().getString(R.string.forwardslash)+getApplicationContext().getString(R.string.door_status)+getApplicationContext().getString(R.string.forwardslash)+strDate;
        statusKey=getApplicationContext().getString(R.string.statusKey);
        SensorKey=getApplicationContext().getString(R.string.db_ultrasonic_dist);
        Log.d(TAG,"Key tester\n"+statusKey+"\n"+SensorKey);
    }

    //FOR DOOR LOCK
    public void toDatabase(String status){
        DatabaseActivity.context = getApplicationContext();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child((idKey));

        databaseReference.setValue(status);
        Map<String, Object> updateStatus = new HashMap<>();
        updateStatus.put(getString(R.string.status),status);

        databaseReference.updateChildren(updateStatus);
    }

    public void dbID(){
        userInfo.typeAccount();
        initV();
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

        idKey=key+doorKey;
        lightKey=key+statusKey;

        sensorKey=key+SensorKey;
        Log.d(TAG,"Key grabbed from DatabaseClass: \n"+idKey+"\n"+lightKey+"\n"+sensorKey);
    }

   // @SuppressLint("SimpleDateFormat")
    private void time(){
        Log.d(TAG,"STR TIME METHOD");
        date = Calendar.getInstance().getTime();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            dateFormat = new SimpleDateFormat(getApplicationContext().getString(R.string.formatted));
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            strDate = dateFormat.format(date);
        }
        System.out.println("Converted String: " + strDate);
        Log.d(TAG,"STR Time: "+strDate);
    }
}