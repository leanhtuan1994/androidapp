package ttuananhle.android.chatlearningapp.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import ttuananhle.android.chatlearningapp.R;
import ttuananhle.android.chatlearningapp.adapter.RecyclerMessageDetailAdapter;
import ttuananhle.android.chatlearningapp.model.Message;
import ttuananhle.android.chatlearningapp.model.User;

/**
 * Created by leanh on 5/12/2017.
 */

public class RecyclerMessageDetailNotMeViewHolder extends RecyclerView.ViewHolder {

    private TextView txtMessage;
    private CircleImageView imgProfile;
    private Context context;

    public RecyclerMessageDetailNotMeViewHolder(final View parent, TextView txtMessage, CircleImageView imgProfile){
        super(parent);

        this.txtMessage = txtMessage;
        this.imgProfile = imgProfile;
        this.context = parent.getContext();
    }


    public static RecyclerMessageDetailNotMeViewHolder newInstance(View parent){
        TextView textView = (TextView) parent.findViewById(R.id.txt_item_message_detail_notme);
        CircleImageView img = (CircleImageView) parent.findViewById(R.id.img_item_message_detail_notme);

        return new RecyclerMessageDetailNotMeViewHolder(parent, textView, img);
    }

    public void setTxtMessage(String message){
        this.txtMessage.setText(message);
    }

    public void setImgProfile(String idUser){
        try{
            FirebaseDatabase.getInstance().getReference().child("Users").child(idUser)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Picasso.with(context).load(dataSnapshot.getValue(User.class).getPhotoUrl()).into(imgProfile);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });

        } catch (Exception e){}

    }

    public void bind(final Message message, final RecyclerMessageDetailAdapter.OnItemClickListener listener){
        setTxtMessage(message.getText());
        setImgProfile(message.getFromId());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(message);
            }
        });
    }
}
