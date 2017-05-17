package ttuananhle.android.chatlearningapp.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import ttuananhle.android.chatlearningapp.R;
import ttuananhle.android.chatlearningapp.adapter.RecyclerQuesionAdapter;
import ttuananhle.android.chatlearningapp.model.Quesion;
import ttuananhle.android.chatlearningapp.model.User;

/**
 * Created by leanh on 5/17/2017.
 */

public class RecyclerQuestionDetailViewHolder extends RecyclerView.ViewHolder {

    private CircleImageView imageView;
    private TextView txtQuestion;
    private TextView txtTime;
    private Context context;

    private FirebaseAuth fireAuth;
    private FirebaseUser fireUser;
    private FirebaseDatabase fireData;
    private DatabaseReference dataRef;

    public RecyclerQuestionDetailViewHolder(View parent, CircleImageView img, TextView txtQuestion, TextView txtTime){
        super(parent);

        this.txtQuestion = txtQuestion;
        this.txtTime = txtTime;

        context = parent.getContext();
        fireAuth = FirebaseAuth.getInstance();
        fireUser = fireAuth.getCurrentUser();
        fireData = FirebaseDatabase.getInstance();
        dataRef = fireData.getReference();
    }

    public static RecyclerQuestionDetailViewHolder newInstance(View parent){
        CircleImageView circleImageView = (CircleImageView) parent.findViewById(R.id.img_item_question);
        TextView txtQuestion = (TextView)parent.findViewById(R.id.txt_item_question);
        TextView txtTime = (TextView) parent.findViewById(R.id.txt_item_question_time);

        return new RecyclerQuestionDetailViewHolder(parent, circleImageView, txtQuestion, txtTime);
    }

    private void setImageView(String userId){
        try {
            dataRef.child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                   try {
                       Picasso.with(context).load(user.getPhotoUrl()).into(imageView);
                   } catch (Exception e){}
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        } catch ( Exception e){

        }
    }

    private void setTxtQuestion(String question){
        txtQuestion.setText(question);
    }

    private void setTxtTime(String time){
        txtTime.setText(time);
    }

    public void bind(final Quesion quesion, final RecyclerQuesionAdapter.OnItemClickListener listener){
        setImageView(quesion.getFromUserId());
        setTxtTime(quesion.getTime());
        setTxtQuestion(quesion.getText());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(quesion);
            }
        });

    }
}
