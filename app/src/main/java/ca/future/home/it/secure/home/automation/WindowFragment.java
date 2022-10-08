package ca.future.home.it.secure.home.automation;

import android.os.Bundle;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class WindowFragment extends Fragment {

    public WindowFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_window, container, false);
        PulsatorLayout pulsator = (PulsatorLayout) view.findViewById(R.id.pulsator);
        pulsator.start();
        pulsator.setCount(4);
        pulsator.setDuration(2000);




        return view;
    }
}