package ttuananhle.android.chatlearningapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ttuananhle.android.chatlearningapp.R;
import ttuananhle.android.chatlearningapp.model.MessagePerUserTo;
import ttuananhle.android.chatlearningapp.model.Presentation;
import ttuananhle.android.chatlearningapp.viewholder.RecyclerPresentationItemViewHolder;

/**
 * Created by leanh on 5/17/2017.
 */

public class RecyclerPresentationAdapter extends RecyclerView.Adapter<RecyclerPresentationItemViewHolder> {

    private List<Presentation> presentationList;
    private Context context;
    private LayoutInflater layoutInflater;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Presentation item);

    }

    public RecyclerPresentationAdapter(Context context, List<Presentation> list, OnItemClickListener listener){
        this.context = context;
        this.presentationList = list;
        this.layoutInflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @Override
    public RecyclerPresentationItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.recycler_item_presentations, parent, false);
        return RecyclerPresentationItemViewHolder.newInstance(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerPresentationItemViewHolder holder, int position) {
        holder.bind(presentationList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return presentationList == null ? 0 : presentationList.size();
    }
}
