/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/

package ca.future.home.it.secure.home.automation;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class DoorFragment extends Fragment {

    //Door status
    ToggleButton doorLock;
    TextView status;
    ImageView locked;
    ImageView unlocked;

    //Manage keys
    CardView cardView;

    //Door history
    ScrollView scrollView;
    LinearLayout linearLayout;

    public DoorFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_door, container, false);
    }
    @Override
    public void onViewCreated (View view, Bundle savedInstanceState){

        //Door status
        doorLock=view.findViewById(R.id.doorLockBtn);
        status=view.findViewById(R.id.statusofDoor);
        locked=view.findViewById(R.id.iv_locked);
        unlocked=view.findViewById(R.id.iv_unlocked);

        //Manage keys
        cardView=view.findViewById(R.id.cv_keys);
        cardView.setBackgroundResource(R.drawable.cardview_border);

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
            } else {
                Toast.makeText(getActivity(), R.string.openDoor, Toast.LENGTH_SHORT).show();
                status.setText(R.string.unlock);
                unlocked.setVisibility(View.VISIBLE);
                locked.setVisibility(View.INVISIBLE);
                doorLock.setBackgroundResource(R.drawable.lock_border_red);
            }
        });
    }

    public void addHistory(String info){
        TextView textView = new TextView(getContext());
        textView.setText(info);
        textView.setBackgroundResource(R.drawable.scroll_view_item_border);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextColor(0xFF000000);
        textView.setTextSize(14);
        textView.setFontFeatureSettings("sans-serif");
        textView.setPadding(10,19,10,19);
        linearLayout.addView(textView);
    }
}
