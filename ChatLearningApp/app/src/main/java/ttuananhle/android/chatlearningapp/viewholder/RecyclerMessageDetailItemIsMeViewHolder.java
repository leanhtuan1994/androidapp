package ttuananhle.android.chatlearningapp.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ttuananhle.android.chatlearningapp.R;
import ttuananhle.android.chatlearningapp.adapter.RecyclerMessageDetailAdapter;
import ttuananhle.android.chatlearningapp.model.Message;

/**
 * Created by leanh on 5/12/2017.
 */

public class RecyclerMessageDetailItemIsMeViewHolder extends RecyclerView.ViewHolder {
    private TextView txtMessage;
    private Context context;

    public RecyclerMessageDetailItemIsMeViewHolder(final View parent, TextView txtMessage){
        super(parent);

        this.txtMessage = txtMessage;
        this.context = parent.getContext();
    }


    public static RecyclerMessageDetailItemIsMeViewHolder newInstance(View parent){
        TextView textView = (TextView) parent.findViewById(R.id.txt_item_message_detail_isme);

        return new RecyclerMessageDetailItemIsMeViewHolder(parent, textView);
    }

    public void setTxtMessage(String message){
        this.txtMessage.setText(message);
    }


    public void bind(final Message message, final RecyclerMessageDetailAdapter.OnItemClickListener listener){
        setTxtMessage(message.getText());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(message);
            }
        });
    }

}
