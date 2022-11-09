/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/
package ca.future.home.it.secure.home.automation;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class TempFragment extends Fragment {

    TextView tvCurrentTemperature;
    TextView tvMinimumTemperature;
    TextView tvMaximumTemperature;
    public TempFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_temp, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setListeners();
    }

    private void init(View view) {
        tvCurrentTemperature = view.findViewById(R.id.CurrentTemp);
        tvMinimumTemperature = view.findViewById(R.id.MaximumTemperature);
        tvMaximumTemperature = view.findViewById(R.id.MaximumTemperature);
    }

    private void setListeners() {
        tvMaximumTemperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogForInputTemperature(true);
            }
        });

        tvMaximumTemperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogForInputTemperature(false);
            }
        });
    }

    private void showDialogForInputTemperature(boolean isMinTemperature) {
        String title = isMinTemperature ? "Minimum Temperature" : "Maximum Temperature";
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.temperature_input_dialog_box, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptsView);
        TextView tvTitle = promptsView.findViewById(R.id.Title);
        tvTitle.setText(title);
        EditText userInput = (EditText) promptsView.findViewById(R.id.TemperatureValue);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (isMinTemperature) {
                            //Todo handle the input for min temperature
                        } else {
                            //Todo handle the input for max temperature
                        }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}