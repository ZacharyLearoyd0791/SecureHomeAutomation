/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC*/
package ca.future.home.it.secure.home.automation;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class AddDeviceFragment extends Fragment {

    private static final int MY_PERMISSIONS_REQUEST_BLUETOOTH = 5;

    public AddDeviceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_device, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bluetoothPermissionChecker();

    }

    private  boolean bluetoothPermissionChecker() {
        int bluetoothScan = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH_SCAN);
        int bluetoothConnect = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH_CONNECT);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (bluetoothScan != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.BLUETOOTH_SCAN);
        }
        if (bluetoothConnect != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.BLUETOOTH_CONNECT);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            requestPermissions(listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), MY_PERMISSIONS_REQUEST_BLUETOOTH);
            return false;
        }
        return true;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_BLUETOOTH: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // granted
                    Snackbar.make(getView(),"Permission is granted",Snackbar.LENGTH_LONG).show();

                } else {
                    // denied
                    Snackbar.make(getView(),"Permission is denied",Snackbar.LENGTH_LONG).show();
                }
                return;
            }

        }
    }
}