package ttuananhle.android.chatlearningapp.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import ttuananhle.android.chatlearningapp.R;
import ttuananhle.android.chatlearningapp.adapter.RecyclerContactAdapter;
import ttuananhle.android.chatlearningapp.model.User;

/**
 * Created by leanh on 5/5/2017.
 */

public class RecyclerContactItemViewHolder extends RecyclerView.ViewHolder {

    private final TextView txtItemName;
    private final TextView txtItemEmail;
    private final CircleImageView circleImageView;
    private static Context context;

    private RecyclerContactItemViewHolder(final View parent, TextView txtItemName, TextView txtItemEmail, CircleImageView imgView){
        super(parent);
        this.txtItemEmail = txtItemEmail;
        this.txtItemName = txtItemName;
        this.circleImageView = imgView;
    }

    public static RecyclerContactItemViewHolder newInstance(View parent){
        TextView txtName = (TextView) parent.findViewById(R.id.txt_item_contact_name);
        TextView txtEmail = (TextView) parent.findViewById(R.id.txt_item_contact_email);
        CircleImageView imgView = (CircleImageView) parent.findViewById(R.id.img_item_contact);

        context = parent.getContext();

        return new RecyclerContactItemViewHolder(parent, txtName, txtEmail, imgView);
    }

    public void setItemName(String name){
        this.txtItemName.setText(name);
    }
    public void setItemEmail(String email){
        this.txtItemEmail.setText(email);
    }
    public void setCircleImageView(String url){
        if ( url != ""){
            try {
                Picasso.with(context).load(url).into(circleImageView);
            } catch (Exception ex){}
        }
    }

    public void bind(final User user, final RecyclerContactAdapter.OnItemClickListener listener){
        setItemName(user.getName());
        setItemEmail(user.getEmail());
        setCircleImageView(user.getPhotoUrl());

        // Set onItemClick
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(user);
            }
        });
    }


}
