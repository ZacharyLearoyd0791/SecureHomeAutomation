/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/

package ca.future.home.it.secure.home.automation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;

public class DoorFragment extends Fragment {

    ToggleButton doorLock;



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


        doorLock.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                doorLock.setBackgroundColor(android.graphics.Color.parseColor("#00FF00"));
                Toast.makeText(getActivity(), R.string.openDoor, Toast.LENGTH_SHORT).show();
                doorLock.setBackgroundResource(R.drawable.ic_lock);

            } else {
                doorLock.setBackgroundColor(android.graphics.Color.parseColor("#FF0000"));
                Toast.makeText(getActivity(), R.string.closedDoor, Toast.LENGTH_SHORT).show();
                doorLock.setBackgroundResource(R.drawable.ic_lock_closed);
            }
        });
        }
    }
