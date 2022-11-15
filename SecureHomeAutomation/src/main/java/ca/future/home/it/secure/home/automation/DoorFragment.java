/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/

package ca.future.home.it.secure.home.automation;

import static android.content.ContentValues.TAG;
import static java.lang.Thread.sleep;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DoorFragment extends Fragment {

    //Door status
    ToggleButton doorLock;
    TextView status;
    ImageView locked;
    ImageView unlocked;

    //Manage keys
    CardView cardView;
    ImageButton addKey;
    ImageButton removeKey;
    Handler handler = new Handler();

    //Door history
    ScrollView scrollView;
    LinearLayout linearLayout;

    //Database
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    //String
    String idKey,localKey,key,personalKey,strDate;

    //Date
    Date date;
    DateFormat dateFormat;

    //Classes called
    UserInfo userInfo=new UserInfo();
    public DoorFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_door, container, false);
    }
    @Override
    public void onViewCreated (View view, Bundle savedInstanceState){
        dbID();

        //Door status
        doorLock=view.findViewById(R.id.doorLockBtn);
        status=view.findViewById(R.id.statusofDoor);
        locked=view.findViewById(R.id.iv_locked);
        unlocked=view.findViewById(R.id.iv_unlocked);

        //Manage keys
        cardView=view.findViewById(R.id.cv_keys);
        cardView.setBackgroundResource(R.drawable.cardview_border);
        addKey=view.findViewById(R.id.add_key_btn);
        removeKey=view.findViewById(R.id.remove_key_btn);

        //Door status unlocked by default for testing
        locked.setVisibility(View.INVISIBLE);
        doorLock.setBackgroundResource(R.drawable.lock_border_red);
        status.setText(R.string.unlock);

        //Door history
        linearLayout=view.findViewById(R.id.linear_history);
        addHistory(getString(R.string.sample_4));
        addHistory(getString(R.string.sample_2));
        addHistory(getString(R.string.sample_3));
        addHistory(getString(R.string.sample_1));
        addHistory(getString(R.string.sample_4));
        addHistory(getString(R.string.sample_2));
        addHistory(getString(R.string.sample_1));
        addHistory(getString(R.string.sample_4));
        addHistory(getString(R.string.sample_2));

        doorLock.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(getActivity(), R.string.closedDoor, Toast.LENGTH_SHORT).show();
                status.setText(R.string.lock);
                locked.setVisibility(View.VISIBLE);
                unlocked.setVisibility(View.INVISIBLE);
                doorLock.setBackgroundResource(R.drawable.lock_border_green);
                toDatabase(getString(R.string.lock_status));
            } else {
                Toast.makeText(getActivity(), R.string.openDoor, Toast.LENGTH_SHORT).show();
                status.setText(R.string.unlock);
                unlocked.setVisibility(View.VISIBLE);
                locked.setVisibility(View.INVISIBLE);
                doorLock.setBackgroundResource(R.drawable.lock_border_red);
                toDatabase(getString(R.string.unlocked_status));
            }
        });

        //Add key
        addKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addKey();
            }});

        //Remove key
        removeKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeKey();
            }});
    }

    public void addHistory(String info){
        TextView textView = new TextView(getContext());
        textView.setText(info);
        textView.setBackgroundResource(R.drawable.scroll_view_item_border);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextColor(0xFF000000);
        textView.setTextSize(14);
        textView.setFontFeatureSettings(getString(R.string.font_sans_serif));
        textView.setPadding(10,19,10,19);
        linearLayout.addView(textView);
    }

    public void addKey(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.place_key);
        builder.setTitle(R.string.add_key);

        //Cancel
        builder.setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener)
                (dialog, which) -> {
                    dialog.cancel();
                });

        //On successful add
        AlertDialog.Builder successBuilder = new AlertDialog.Builder(getContext());
        successBuilder.setMessage(R.string.success);
        successBuilder.setNegativeButton(R.string.close, (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog addSuccess = successBuilder.create();
        addSuccess.show();

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void removeKey(){

        AlertDialog.Builder removeBuilder = new AlertDialog.Builder(getContext());
        removeBuilder.setMessage(R.string.place_key_remove);
        removeBuilder.setTitle(R.string.remove_key);

        //Cancel
        removeBuilder.setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });

        //On successful removal
        AlertDialog.Builder successBuilder = new AlertDialog.Builder(getContext());
        successBuilder.setMessage(R.string.success);
        successBuilder.setNegativeButton(R.string.close, (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog removeSuccess = successBuilder.create();
        removeSuccess.show();

        //Remove alert
        AlertDialog removeAlert = removeBuilder.create();
        removeAlert.show();
    }

    private void toDatabase(String status){
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
