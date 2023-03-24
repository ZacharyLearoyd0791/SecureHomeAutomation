package ca.future.home.it.secure.home.automation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WindowsFragmentRecyclerViewAdapter extends RecyclerView.Adapter<WindowsFragmentViewHolder> {

    private List<WindowsFragmentData> windowsFragmentActivityData;
    public WindowsFragmentRecyclerViewAdapter(List<WindowsFragmentData> dataset){
        windowsFragmentActivityData = dataset;

    }
    @NonNull
    @Override
    public WindowsFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.windows_break_activity_row,parent,false);
        WindowsFragmentViewHolder viewHolder = new WindowsFragmentViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WindowsFragmentViewHolder holder, int position) {
        WindowsFragmentData items = windowsFragmentActivityData.get(position);
        holder.activityIcon.setImageResource(items.getActivityIcon());
        holder.activityAlarm.setText(items.getActivityAlarmTitle());
        holder.activityDate.setText(items.getActivityDate());
    }

    @Override
    public int getItemCount() {
        return windowsFragmentActivityData.size();
    }
}
