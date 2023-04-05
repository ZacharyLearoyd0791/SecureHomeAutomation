package ca.future.home.it.secure.home.automation;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AccountFragmentViewHolder extends RecyclerView.ViewHolder {
    public ImageView accountRVIcon;
    public TextView accountRVTitle;
    public TextView accountRVDetails;

    public AccountFragmentViewHolder(@NonNull View itemView) {
        super(itemView);
        accountRVIcon = itemView.findViewById(R.id.account_recyclerView_icon);
        accountRVTitle = itemView.findViewById(R.id.account_recyclerview_title);
        accountRVDetails = itemView.findViewById(R.id.account_recyclerview_details);

    }
}
