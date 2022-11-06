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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;

public class DoorFragment extends Fragment {

    ToggleButton doorLock;
    TextView status;

    ImageView locked;
    ImageView unlocked;

    public DoorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment




        return inflater.inflate(R.layout.fragment_door, container, false);
    }
    @Override
    public void onViewCreated (View view,
                               Bundle savedInstanceState){

        doorLock=view.findViewById(R.id.doorLockBtn);
        status=view.findViewById(R.id.statusofDoor);
        locked=view.findViewById(R.id.iv_locked);
        unlocked=view.findViewById(R.id.iv_unlocked);
        locked.setVisibility(View.INVISIBLE);
        unlocked.setVisibility(View.INVISIBLE);

        doorLock.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(getActivity(), R.string.closedDoor, Toast.LENGTH_SHORT).show();
                status.setText(R.string.lock);
                locked.setVisibility(View.VISIBLE);
                unlocked.setVisibility(View.INVISIBLE);
                doorLock.setBackgroundResource(R.drawable.lock_border_green);
                doorLock.setTextOff(getString(R.string.space));
                doorLock.setTextOn(getString(R.string.space));
            } else {
                Toast.makeText(getActivity(), R.string.openDoor, Toast.LENGTH_SHORT).show();
                status.setText(R.string.unlock);
                unlocked.setVisibility(View.VISIBLE);
                locked.setVisibility(View.INVISIBLE);
                doorLock.setBackgroundResource(R.drawable.lock_border_red);
                doorLock.setTextOff(getString(R.string.space));
                doorLock.setTextOn(getString(R.string.space));
            }
        });
        }
    }
