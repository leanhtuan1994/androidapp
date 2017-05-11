package ttuananhle.android.chatlearningapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ttuananhle.android.chatlearningapp.R;
import ttuananhle.android.chatlearningapp.model.MessagePerUserTo;
import ttuananhle.android.chatlearningapp.viewholder.RecyclerMessagesItemViewHolder;

/**
 * Created by leanh on 5/10/2017.
 */

public class RecyclerMessagesAdapter extends RecyclerView.Adapter<RecyclerMessagesItemViewHolder> {





    private List<MessagePerUserTo> userToList;
    private Context context;
    private LayoutInflater layoutInflater;
    private final OnItemClickListener listener;


    public interface OnItemClickListener {
        void onItemClick(MessagePerUserTo item);

    }

    public RecyclerMessagesAdapter(Context context, List<MessagePerUserTo> list, OnItemClickListener listener){
        this.context = context;
        this.userToList = list;
        this.listener = listener;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerMessagesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.recycler_item_messages, parent, false);
        return RecyclerMessagesItemViewHolder.newInstance(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerMessagesItemViewHolder holder, int position) {
        MessagePerUserTo messagePerUserTo = userToList.get(position);

        holder.bind(messagePerUserTo, listener);
    }

    @Override
    public int getItemCount() {
        return userToList == null ? 0 : userToList.size();
    }
}
