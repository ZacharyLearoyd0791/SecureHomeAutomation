package ca.future.home.it.secure.home.automation;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AccountFragmentRecyclerViewAdapter extends RecyclerView.Adapter<AccountFragmentViewHolder> {
    private List<AccountFragmentData> accountFragmentDataset;


    public AccountFragmentRecyclerViewAdapter(List<AccountFragmentData> dataset){
        accountFragmentDataset = dataset;
    }


    @NonNull
    @Override
    public AccountFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_recyclerview_items,parent,false);
        AccountFragmentViewHolder viewHolder = new AccountFragmentViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AccountFragmentViewHolder holder, int position) {
        AccountFragmentData itemText = accountFragmentDataset.get(position);
        holder.accountRVIcon.setImageResource(itemText.getIcon());
        holder.accountRVTitle.setText(itemText.getTitle());
        holder.accountRVDetails.setText(itemText.getDetail());
    }

    @Override
    public int getItemCount() {
        return accountFragmentDataset.size();
    }
}
