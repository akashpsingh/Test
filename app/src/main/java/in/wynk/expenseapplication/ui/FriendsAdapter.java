package in.wynk.expenseapplication.ui;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.wynk.expenseapplication.ExpenseStore;
import in.wynk.expenseapplication.R;
import in.wynk.expenseapplication.User;

/**
 * Created by Akash on 10/03/18.
 */

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    private List<User> users;

    public FriendsAdapter(List<User> users) {
        this.users = users;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = users.get(position);
        holder.tvName.setText(user.getName());
        double amount = user.getCurrentBalance();
        holder.tvAmount.setText(String.valueOf(amount));
        if (user.equals(ExpenseStore.getInstance().getMe())) {
            holder.tvAmount.setTextColor(amount >= 0 ? Color.GREEN : Color.RED);
        } else {
            holder.tvAmount.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return users != null ? users.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvAmount;

        public ViewHolder(View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.tv_famount);
            tvName = itemView.findViewById(R.id.tv_fname);
        }
    }
}
