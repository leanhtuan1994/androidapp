package ttuananhle.android.chatlearningapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ttuananhle.android.chatlearningapp.R;
import ttuananhle.android.chatlearningapp.model.User;
import ttuananhle.android.chatlearningapp.viewholder.RecyclerContactItemViewHolder;

/**
 * Created by leanh on 5/5/2017.
 */

public class RecyclerContactAdapter extends RecyclerView.Adapter<RecyclerContactItemViewHolder> {

    private List<User> userList;
    private Context context;
    private LayoutInflater layoutInflater;
    private final OnItemClickListener listener;


    public interface OnItemClickListener {
        void onItemClick(User item);

    }


    public RecyclerContactAdapter(Context context, List<User> list, OnItemClickListener listener){
        this.context = context;
        this.userList = list;
        this.layoutInflater = LayoutInflater.from(context);
        this.listener = listener;
    }


    @Override
    public RecyclerContactItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.recycler_item_contacts, parent, false);
        return RecyclerContactItemViewHolder.newInstance(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerContactItemViewHolder holder, int position) {
        User user = userList.get(position);

        // Set text
        holder.bind(user, listener);
    }

    @Override
    public int getItemCount() {
        return userList == null ? 0 : userList.size();
    }
}



