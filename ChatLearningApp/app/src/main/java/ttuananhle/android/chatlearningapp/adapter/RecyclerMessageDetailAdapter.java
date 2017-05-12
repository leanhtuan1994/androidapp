package ttuananhle.android.chatlearningapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import ttuananhle.android.chatlearningapp.R;
import ttuananhle.android.chatlearningapp.model.Message;
import ttuananhle.android.chatlearningapp.viewholder.RecyclerMessageDetailItemIsMeViewHolder;
import ttuananhle.android.chatlearningapp.viewholder.RecyclerMessageDetailNotMeViewHolder;

/**
 * Created by leanh on 5/12/2017.
 */

public class RecyclerMessageDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_IS_ME = 1;
    public static final int TYPE_NOT_ME = 2;

    private Context context;
    private List<Message> messageList;
    private LayoutInflater layoutInflater;
    private final OnItemClickListener listener;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    public interface OnItemClickListener {
        void onItemClick(Message item);
    }

    public RecyclerMessageDetailAdapter(Context context, List<Message> list,OnItemClickListener listener ){
        this.context = context;
        this.listener = listener;
        this.messageList = list;
        this.layoutInflater = LayoutInflater.from(context);
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.firebaseUser = firebaseAuth.getCurrentUser();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_IS_ME:
                View itemViewIsMe = layoutInflater.inflate(R.layout.recycler_item_detail_message_isme, parent, false);
                return RecyclerMessageDetailItemIsMeViewHolder.newInstance(itemViewIsMe);
            case TYPE_NOT_ME:
                View itemViewNotMe = layoutInflater.inflate(R.layout.recycler_item_detail_message_notme, parent, false);
                return RecyclerMessageDetailNotMeViewHolder.newInstance(itemViewNotMe);
            default:
                break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);
        switch (holder.getItemViewType()){
            case TYPE_IS_ME:
                RecyclerMessageDetailItemIsMeViewHolder viewHolderIsMe = (RecyclerMessageDetailItemIsMeViewHolder) holder;
                viewHolderIsMe.bind(message, listener);
                break;

            case TYPE_NOT_ME:
                RecyclerMessageDetailNotMeViewHolder viewHolderNotMe = (RecyclerMessageDetailNotMeViewHolder) holder;
                viewHolderNotMe.bind(message, listener);
                break;

            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return messageList == null ? 0 : messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return firebaseUser.getUid().equals(messageList.get(position).getFromId()) ? TYPE_IS_ME : TYPE_NOT_ME;
    }
}
