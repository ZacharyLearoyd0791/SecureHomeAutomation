package ca.future.home.it.secure.home.automation;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WindowsFragmentViewHolder extends RecyclerView.ViewHolder {

    //Declaration
    public ImageView activityIcon;
    public TextView activityDate;
    public TextView activityAlarm;
    public WindowsFragmentViewHolder(@NonNull View itemView) {
        super(itemView);
        activityIcon = itemView.findViewById(R.id.window_break_alarm_icon);
        activityAlarm = itemView.findViewById(R.id.windows_break_alarm_title);
        activityDate =itemView.findViewById(R.id.windows_break_alarm_date);
    }
}
