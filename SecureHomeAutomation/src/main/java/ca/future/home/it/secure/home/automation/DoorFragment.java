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



import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DoorFragment extends Fragment{

    View view;
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


    //String
    String idKey,localKey,key,personalKey,strDate,statusDoor,lockedStr,unlockedStr;
    public static String statusofDoor;

    //Date
    Date date;
    DateFormat dateFormat;

    private Handler handlerRun;
    private Runnable handlerTask;

    public DoorFragment() {
    }
    void StartTimer(){
        handlerRun = new Handler();
        handlerTask = new Runnable()
        {
  /*          public void run() {
                // do something
                statusDoor=statusofDoor;
                status.setText(statusDoor);

                handler.postDelayed(handlerTask, 1000);
            }
        };
        handlerTask.run();
    }*/

    @Override

            public void run() {
                // do something
        statusDoor=statusofDoor;
                if(statusDoor!=null) {
                    if (statusDoor.equals(lockedStr)){
                        doorLock.setChecked(true);
                    }
                    else{
                        status.setText(statusDoor);
                        doorLock.setChecked(false);

                    }

                }
                status.setText(statusDoor);
                handler.postDelayed(handlerTask, 1000);
            }
        };
        handlerTask.run();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_door, container, false);
        init();
        StartTimer();
        action();
        return view;
    }
    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        //dbID();

    }

    private void action() {
        if(Objects.equals(statusDoor, getString(R.string.lock))){
            Toast.makeText(getActivity(), R.string.closedDoor, Toast.LENGTH_SHORT).show();
            status.setText(R.string.lock);
            locked.setVisibility(View.VISIBLE);
            unlocked.setVisibility(View.INVISIBLE);
            doorLock.setBackgroundResource(R.drawable.status_border_green);
            statusofDoor=getString(R.string.lock);
        }
        else if(Objects.equals(statusDoor, getString(R.string.unlock))){
            Toast.makeText(getActivity(), R.string.openDoor, Toast.LENGTH_SHORT).show();
            status.setText(R.string.unlock);
            unlocked.setVisibility(View.VISIBLE);
            locked.setVisibility(View.INVISIBLE);
            doorLock.setBackgroundResource(R.drawable.status_border_red);

        }
        doorLock.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(getActivity(), R.string.closedDoor, Toast.LENGTH_SHORT).show();
                status.setText(R.string.lock);
                locked.setVisibility(View.VISIBLE);
                unlocked.setVisibility(View.INVISIBLE);
                doorLock.setBackgroundResource(R.drawable.status_border_green);
                statusofDoor=getString(R.string.lock);

            } else {
                Toast.makeText(getActivity(), R.string.openDoor, Toast.LENGTH_SHORT).show();
                status.setText(R.string.unlock);
                unlocked.setVisibility(View.VISIBLE);
                locked.setVisibility(View.INVISIBLE);
                doorLock.setBackgroundResource(R.drawable.status_border_red);
                statusofDoor=getString(R.string.unlock);


            }
        });

        //Door status unlocked by default for testing
        locked.setVisibility(View.INVISIBLE);
        doorLock.setBackgroundResource(R.drawable.status_border_red);
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


    private void init() {
        doorLock=view.findViewById(R.id.doorLockBtn);
        status=view.findViewById(R.id.statusofDoor);
        locked=view.findViewById(R.id.iv_locked);
        unlocked=view.findViewById(R.id.iv_unlocked);

        //Manage keys
        cardView=view.findViewById(R.id.cv_keys);
        cardView.setBackgroundResource(R.drawable.cardview_border);
        addKey=view.findViewById(R.id.add_key_btn);
        removeKey=view.findViewById(R.id.remove_key_btn);
        lockedStr=getString(R.string.lock);
        unlockedStr=getString(R.string.unlock);
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

}
