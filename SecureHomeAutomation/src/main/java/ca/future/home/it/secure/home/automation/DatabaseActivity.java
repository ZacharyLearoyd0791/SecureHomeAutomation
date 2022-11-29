package ca.future.home.it.secure.home.automation;

import static android.content.ContentValues.TAG;

import static com.facebook.FacebookSdk.getApplicationContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
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
    String idKey,localKey,key,personalKey,strDate;


    public void toDatabase(String status){
        DatabaseActivity.context = getApplicationContext();

        dbID();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child((idKey));

        databaseReference.setValue(status);
        Map<String, Object> updateStatus = new HashMap<>();
        updateStatus.put(getString(R.string.status),status);

        databaseReference.updateChildren(updateStatus);
    }

    private void dbID(){
        userInfo.typeAccount();
        time();
        //Log.d(TAG,"Time string before key;"+strDate);

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

        idKey=key+getString(R.string.forwardslash)+getString(R.string.door_status)+getString(R.string.forwardslash)+strDate;
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