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
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class DoorFragment extends Fragment {

    ToggleButton doorLock;
    View view;

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
        doorLock.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                doorLock.setBackgroundColor(android.graphics.Color.parseColor("#00FF00"));

            } else {
                doorLock.setBackgroundColor(android.graphics.Color.parseColor("#FF0000"));

            }
        });
          //  doorLock.setBackgroundColor(android.graphics.Color.parseColor("#738b28"));
        }
    }
