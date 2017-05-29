package ttuananhle.android.chatlearningapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import ttuananhle.android.chatlearningapp.R;
import ttuananhle.android.chatlearningapp.adapter.RecyclerQuesionAdapter;
import ttuananhle.android.chatlearningapp.model.Presentation;
import ttuananhle.android.chatlearningapp.model.Quesion;
import ttuananhle.android.chatlearningapp.model.User;

public class QuesionPresentationActivity extends AppCompatActivity {

    public static String PRESENTATION_ID;

    private Toolbar toolbar;
    private TextView txtTitleToolbar;
    private EditText edtQuestion;
    private ImageView btnSend;
    private RecyclerView recyclerView;
    private RecyclerQuesionAdapter quesionAdapter;
    private List<Quesion> quesionList;
    private RatingBar ratingBar;

    private FirebaseAuth fireAuth;
    private FirebaseUser fireUser;
    private FirebaseDatabase fireData;
    private DatabaseReference dataRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quesion_presentation);
        PRESENTATION_ID = getIntent().getStringExtra("PRESENTATION_ID");

        mappingView();
        getFirebase();
        setToolbar();

        initRecyclerView();

        setRatingBar();
        listenerButtonQuestionSend();


    }

    @Override
    protected void onResume() {
        super.onResume();
        initListQuestion();
    }

    private void mappingView(){
        toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        edtQuestion = (EditText) this.findViewById(R.id.edt_quesion_input);
        btnSend = (ImageView) this.findViewById(R.id.btn_quesion_send);
        recyclerView = (RecyclerView) this.findViewById(R.id.recycler_quesion_presentation_view);
        ratingBar = (RatingBar) this.findViewById(R.id.ratingbar_in_question);
    }


    private void setRatingBar(){


        dataRef.child("Users").child(fireUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User user = dataSnapshot.getValue(User.class);

                // set rating saved
                dataRef.child("Presentations").child(user.getCode()).child(PRESENTATION_ID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Presentation pre = dataSnapshot.getValue(Presentation.class);
                        ratingBar.setRating(pre.getRating());
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });


                if(!user.isTeacher()){
                    ratingBar.setIsIndicator(true);
                } else {
                    ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                        @Override
                        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                            Log.i("Rating", rating + "");
                            dataRef.child("Presentations").child(user.getCode()).child(PRESENTATION_ID).child("rating").setValue(rating);
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
    private void getFirebase(){
        fireAuth = FirebaseAuth.getInstance();
        fireUser = fireAuth.getCurrentUser();
        fireData = FirebaseDatabase.getInstance();
        dataRef = fireData.getReference();
    }

    private void initRecyclerView(){
        quesionList = new ArrayList<>();

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(QuesionPresentationActivity.this);

        quesionAdapter = new RecyclerQuesionAdapter(QuesionPresentationActivity.this, quesionList, new RecyclerQuesionAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(Quesion item) {

            }
        });

        quesionAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int questionCount = quesionAdapter.getItemCount();
                int lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (questionCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    recyclerView.scrollToPosition(positionStart);
                }
            }
        });

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(quesionAdapter);
    }

    private void initListQuestion(){
        DatabaseReference dataQuestionRef = dataRef.child("Presentation-Questions").child(PRESENTATION_ID);
        dataQuestionRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String keyQuestion = dataSnapshot.getKey();
                dataRef.child("Questions").child(keyQuestion).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            Quesion quesion = dataSnapshot.getValue(Quesion.class);

                            quesionList.add(quesion);

                            quesionAdapter.notifyDataSetChanged();
                            recyclerView.scrollToPosition(quesionAdapter.getItemCount() - 1);
                        } catch (Exception e){}
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    private void listenerButtonQuestionSend(){
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = edtQuestion.getText().toString();

                if (!question.equals("")){
                    try {
                        DatabaseReference dataQuestionRef = dataRef.child("Questions");
                        Quesion quesion = new Quesion(question, getCurrentTime(), fireUser.getUid(), PRESENTATION_ID);

                        String key = dataQuestionRef.push().getKey();
                        dataQuestionRef.child(key).setValue(quesion);

                        dataRef.child("Presentation-Questions").child(PRESENTATION_ID).child(key).setValue(1);

                        edtQuestion.setText("");
                    } catch (Exception ex){

                    }
                }
            }
        });
    }


    private void setToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        txtTitleToolbar = (TextView) toolbar.findViewById(R.id.toolbar_title);

        try {
            dataRef.child("Users").child(fireUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    dataRef.child("Presentations").child(user.getCode()).child(PRESENTATION_ID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Presentation presentation = dataSnapshot.getValue(Presentation.class);
                            txtTitleToolbar.setText(presentation.getName());
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        } catch (Exception e){

        }
    }

    public String getCurrentTime(){
       Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        return simpleDateFormat.format(calendar.getTime());
    }

}
