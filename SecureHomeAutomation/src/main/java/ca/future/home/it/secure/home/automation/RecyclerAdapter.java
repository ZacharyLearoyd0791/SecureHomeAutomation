package ca.future.home.it.secure.home.automation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private List<RecyclerViewData> mData;
    UserInfo userInfo=new UserInfo();
    String time,alertType;
    String key,localKey,personalKey,windowsKey,sensorKey;
    public RecyclerAdapter(List<RecyclerViewData> data){
        mData = data;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View recyclerView = inflater.inflate(R.layout.windows_break_activity_row,parent,false);

        ViewHolder viewHolder = new ViewHolder(recyclerView);



        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {

        RecyclerViewData recyclerViewData = mData.get(position);

        TextView textView = holder.AlertType;
        textView.setText(recyclerViewData.getwAlertType());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView AlertType;
        public TextView AlertTime;
        public ImageView AlertImageView;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            AlertType = (TextView) itemView.findViewById(R.id.windows_break_alarm_title);
            AlertTime = (TextView) itemView.findViewById(R.id.windows_break_alarm_date);
            AlertImageView = itemView.findViewById(R.id.window_break_alarm_icon);
            getFromDb();
        }

        public void setTextData(String timing, String alarmType){
            AlertType.setText(alarmType);
            AlertTime.setText(timing);


        }


            private String dbID(){
                userInfo.typeAccount();

                localKey=userInfo.userId;
                personalKey=userInfo.idInfo;

                if(localKey!=null){
                    key=localKey;
                }
                if(personalKey!=null) {
                    key= personalKey;
                }
                windowsKey=key+"/Windows Sensor/";
                sensorKey=windowsKey;

                return windowsKey;
            }

            private void getFromDb(){
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        time = snapshot.child(dbID()).child("Time").toString();
                        alertType = snapshot.child(dbID()).child("Device Status").toString();
                        setTextData(time,alertType);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }


    }
}
