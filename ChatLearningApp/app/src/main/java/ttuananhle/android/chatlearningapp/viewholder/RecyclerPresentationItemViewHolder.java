package ttuananhle.android.chatlearningapp.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ttuananhle.android.chatlearningapp.R;
import ttuananhle.android.chatlearningapp.adapter.RecyclerPresentationAdapter;
import ttuananhle.android.chatlearningapp.model.Presentation;
import ttuananhle.android.chatlearningapp.model.Team;
import ttuananhle.android.chatlearningapp.model.User;

/**
 * Created by leanh on 5/17/2017.
 */

public class RecyclerPresentationItemViewHolder extends RecyclerView.ViewHolder {

    private TextView txtTopic;
    private TextView txtTeamName;
    private TextView txtTime;
    private RatingBar ratingBar;


    private FirebaseDatabase fireData;
    private DatabaseReference dataRef;
    private FirebaseAuth fireAuth;
    private FirebaseUser fireUser;


    public RecyclerPresentationItemViewHolder(View parent, TextView txtTopic,
                                              TextView txtTeamName, TextView txtTime, RatingBar ratingBar ){
        super(parent);

        this.txtTopic = txtTopic;
        this.txtTeamName = txtTeamName;
        this.txtTime = txtTime;
        this.ratingBar = ratingBar;

        fireData = FirebaseDatabase.getInstance();
        dataRef = fireData.getReference();
        fireAuth = FirebaseAuth.getInstance();
        fireUser = fireAuth.getCurrentUser();

    }

    public static RecyclerPresentationItemViewHolder newInstance(View parent){
        TextView txtTopic = (TextView) parent.findViewById(R.id.txt_item_presentation_topic);
        TextView txtTeamName = (TextView) parent.findViewById(R.id.txt_item_presentation_team_name);
        TextView txtTime = (TextView) parent.findViewById(R.id.txt_item_presentation_time);
        RatingBar ratingBar = (RatingBar) parent.findViewById(R.id.ratingbar_item_presentation);

        return new RecyclerPresentationItemViewHolder(parent, txtTopic, txtTeamName, txtTime, ratingBar);
    }

    public void setTxtTopic(String topic){
        this.txtTopic.setText(topic);
    }

    public void setTxtTeamName(final String idTeam){
        dataRef.child("Users").child(fireUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               try {
                   User user = dataSnapshot.getValue(User.class);
                   dataRef.child("Team").child(user.getCode()).child(idTeam).addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                           Team team = dataSnapshot.getValue(Team.class);
                           txtTeamName.setText(team.getName());
                       }
                       @Override
                       public void onCancelled(DatabaseError databaseError) {}
                   });
               } catch (Exception e){}
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void setTxtTime(String time){
        this.txtTime.setText(time);
    }

    public void setRatingBar(float rating){
        this.ratingBar.setRating(rating);
    }


    public void bind(final Presentation presentation, final RecyclerPresentationAdapter.OnItemClickListener listener){
        setTxtTime(presentation.getTime());
        setRatingBar(presentation.getRating());
        setTxtTopic(presentation.getName());
        setTxtTeamName(presentation.getTeamId());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(presentation);
            }
        });
    }

}
