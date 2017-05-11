package ttuananhle.android.chatlearningapp.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import ttuananhle.android.chatlearningapp.R;
import ttuananhle.android.chatlearningapp.adapter.RecyclerMessagesAdapter;
import ttuananhle.android.chatlearningapp.model.MessagePerUserTo;

/**
 * Created by leanh on 5/10/2017.
 */

public class RecyclerMessagesItemViewHolder extends RecyclerView.ViewHolder {

    private CircleImageView imgUserProfile;
    private TextView txtUsername;
    private TextView txtRecentMessage;
    private TextView txtTime;

    private static Context context;
    public RecyclerMessagesItemViewHolder(final View parent,
                                          CircleImageView imgView,
                                          TextView txtName,
                                          TextView txtMessage,
                                          TextView txtTime){
        super(parent);

        this.imgUserProfile = imgView;
        this.txtRecentMessage = txtMessage;
        this.txtTime = txtTime;
        this.txtUsername = txtName;
    }


    public  static RecyclerMessagesItemViewHolder newInstance(final View parent){
        CircleImageView imgView = (CircleImageView) parent.findViewById(R.id.img_item_messages);
        TextView txtName = (TextView) parent.findViewById(R.id.txt_item_message_name);
        TextView txtMessage = (TextView) parent.findViewById(R.id.txt_item_message_message);
        TextView txtTime = (TextView) parent.findViewById(R.id.txt_item_message_time);

        context = parent.getContext();
        return new RecyclerMessagesItemViewHolder(parent, imgView, txtName, txtMessage, txtTime);
    }

    public void setImgUserProfile(String url){
        if ( url != ""){
            try {
                Picasso.with(context).load(url).into(imgUserProfile);
            } catch (Exception e){
            }
        }
    }
    public void setTxtUsername(String name){ this.txtUsername.setText(name);}
    public void setTxtRecentMessage(String message) { this.txtRecentMessage.setText(message);}
    public void setTxtTime(String time) { this.txtTime.setText(time);}

    public void bind(final MessagePerUserTo userTo, final RecyclerMessagesAdapter.OnItemClickListener listener){
        setImgUserProfile(userTo.getPhotoUrl());
        setTxtRecentMessage(userTo.getMassage());
        setTxtUsername(userTo.getName());
        setTxtTime(userTo.getTime());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(userTo);
            }
        });
    }

}
