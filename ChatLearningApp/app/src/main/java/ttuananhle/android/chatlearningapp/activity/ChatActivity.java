package ttuananhle.android.chatlearningapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import ttuananhle.android.chatlearningapp.R;
import ttuananhle.android.chatlearningapp.adapter.RecyclerMessageDetailAdapter;
import ttuananhle.android.chatlearningapp.model.Message;
import ttuananhle.android.chatlearningapp.model.User;

public class ChatActivity extends AppCompatActivity {

    private ImageView btnSend;
    private EditText edtText;
    private RecyclerView recyclerView;
    private RecyclerMessageDetailAdapter recyclerMessageDetailAdapter;

    private String toIdUser;
    private FirebaseAuth fireAuth;
    private FirebaseUser fireUser;
    private FirebaseDatabase fireData;
    private DatabaseReference dataRef;

    private Calendar calendar;

    private Toolbar toolbar;
    private TextView txtTitleToolbar;

    private List<Message> messageList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        mappingView();
        getUser();
        initToolbar();
        initRecyclerView();
        initListMessage();
        listenerBtnSend();
    }



    private void getUser(){
        fireAuth = FirebaseAuth.getInstance();
        fireUser = fireAuth.getCurrentUser();
        fireData = FirebaseDatabase.getInstance();
        dataRef = fireData.getReference();
        toIdUser = getIntent().getStringExtra("toId");

    }
    private void mappingView(){
        btnSend = (ImageView) this.findViewById(R.id.btn_chat_send);
        edtText = (EditText) this.findViewById(R.id.txt_chat_input);
        recyclerView = (RecyclerView) this.findViewById(R.id.recycler_message_detail_view);

        messageList = new ArrayList<>();
    }

    private void listenerBtnSend(){
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = edtText.getText().toString();
                if (!text.equals("")){

                    DatabaseReference dataMessageRef = dataRef.child("Messages");
                    Message message = new Message(fireUser.getUid(), toIdUser, text, getCurrentTime(), false);
                    // Push message to firebase - Messages
                    String key = dataMessageRef.push().getKey();
                    dataMessageRef.child(key).setValue(message);

                    // Save data user and key message in database to support multi user
                    DatabaseReference dataFromUserRef = dataRef.child("User-Messages").child(fireUser.getUid());
                    dataFromUserRef.child(key).setValue(1);

                    DatabaseReference dataToUserRef = dataRef.child("User-Messages").child(toIdUser);
                    dataToUserRef.child(key).setValue(2);

                    // Set edit text;
                    edtText.setText("");
                }
            }
        });
    }

    private void initRecyclerView(){
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);

        recyclerMessageDetailAdapter = new RecyclerMessageDetailAdapter(ChatActivity.this, messageList,
                new RecyclerMessageDetailAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(Message item) {

            }
        });

        recyclerMessageDetailAdapter.registerAdapterDataObserver( new RecyclerView.AdapterDataObserver(){
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int messageCount = recyclerMessageDetailAdapter.getItemCount();
                int lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (messageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    recyclerView.scrollToPosition(positionStart);
                }
            }
        });

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerMessageDetailAdapter);

    }

    private void initToolbar(){
        toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        txtTitleToolbar = (TextView) toolbar.findViewById(R.id.toolbar_title);

        // Get to user name
        dataRef.child("Users").child(toIdUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User toUser = dataSnapshot.getValue(User.class);
                txtTitleToolbar.setText(toUser.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public String getCurrentTime(){
        calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        return simpleDateFormat.format(calendar.getTime());
    }

    private void initListMessage(){

        DatabaseReference dataListMessageRef = dataRef.child("User-Messages").child(fireUser.getUid());
        dataListMessageRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DatabaseReference dataMessageRef = dataRef.child("Messages").child(dataSnapshot.getKey());

                final String keyMessage = dataSnapshot.getKey();
                // Get messages detail
                dataMessageRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Message message = dataSnapshot.getValue(Message.class);

                        if (message.getFromId().equals(toIdUser) ||
                                message.getToId().equals(toIdUser)) {

                            if (message.getToId().equals(fireUser.getUid())){
                                dataRef.child("Messages").child(keyMessage).child("seen").setValue(true);
                            }

                            messageList.add(message);
                        }
                        Log.i("List", messageList.size() + "");
                        recyclerMessageDetailAdapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(recyclerMessageDetailAdapter.getItemCount() - 1);
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
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
