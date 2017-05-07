package ttuananhle.android.chatlearningapp.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ttuananhle.android.chatlearningapp.R;
import ttuananhle.android.chatlearningapp.interfacehelper.OnItemClickListener;
import ttuananhle.android.chatlearningapp.model.User;

/**
 * Created by leanh on 5/5/2017.
 */

public class RecyclerContactItemViewHolder extends RecyclerView.ViewHolder {

    private final TextView txtItemName;
    private final TextView txtItemEmail;

    private RecyclerContactItemViewHolder(final View parent, TextView txtItemName, TextView txtItemEmail){
        super(parent);
        this.txtItemEmail = txtItemEmail;
        this.txtItemName = txtItemName;
    }

    public static RecyclerContactItemViewHolder newInstance(View parent){
        TextView txtName = (TextView) parent.findViewById(R.id.txt_item_contact_name);
        TextView txtEmail = (TextView) parent.findViewById(R.id.txt_item_contact_email);
        return new RecyclerContactItemViewHolder(parent, txtName, txtEmail);
    }

    public void setItemName(String name){
        this.txtItemName.setText(name);
    }

    public void setItemEmail(String email){
        this.txtItemEmail.setText(email);
    }


    public void bind(final User user, final OnItemClickListener listener){
        setItemName(user.getName());
        setItemEmail(user.getEmail());

        // Set onItemClick
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(user);
            }
        });
    }


}
