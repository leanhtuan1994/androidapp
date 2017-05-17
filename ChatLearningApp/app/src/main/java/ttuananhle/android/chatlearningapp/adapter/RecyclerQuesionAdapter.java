package ttuananhle.android.chatlearningapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ttuananhle.android.chatlearningapp.R;
import ttuananhle.android.chatlearningapp.model.Quesion;
import ttuananhle.android.chatlearningapp.viewholder.RecyclerQuestionDetailViewHolder;

/**
 * Created by leanh on 5/17/2017.
 */

public class RecyclerQuesionAdapter extends RecyclerView.Adapter<RecyclerQuestionDetailViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Quesion item);
    }

    private Context context;
    private List<Quesion> quesionList;
    private LayoutInflater layoutInflater;
    private OnItemClickListener listener;



    public RecyclerQuesionAdapter(Context context, List<Quesion> list, OnItemClickListener listener){
        this.context = context;
        this.quesionList = list;
        this.listener = listener;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerQuestionDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.recycler_item_quesion_detail, parent, false);
        return RecyclerQuestionDetailViewHolder.newInstance(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerQuestionDetailViewHolder holder, int position) {
        holder.bind(quesionList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return quesionList == null ? 0 : quesionList.size();
    }
}
