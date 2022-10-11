/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (???) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/

package ca.future.home.it.secure.home.automation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class DoorFragment extends Fragment {

    ToggleButton doorLock;
    private ImageView unlocked;
    private ImageView locked;


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

        doorLock=view.findViewById(R.id.DoorToggle);
        unlocked=view.findViewById(R.id.iv_unlocked);
        locked=view.findViewById(R.id.iv_locked);

        doorLock.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                doorLock.setBackgroundColor(android.graphics.Color.parseColor("#00FF00"));
                Toast.makeText(getActivity(), R.string.openDoor, Toast.LENGTH_SHORT).show();
                unlocked.setVisibility(View.VISIBLE);
                locked.setVisibility(View.INVISIBLE);

            } else {
                doorLock.setBackgroundColor(android.graphics.Color.parseColor("#FF0000"));
                Toast.makeText(getActivity(), R.string.closedDoor, Toast.LENGTH_SHORT).show();
                unlocked.setVisibility(View.INVISIBLE);
                locked.setVisibility(View.VISIBLE);
            }
        });
        }
    }
