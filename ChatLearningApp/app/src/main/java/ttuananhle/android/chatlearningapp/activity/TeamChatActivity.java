package ttuananhle.android.chatlearningapp.activity;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.List;

import ttuananhle.android.chatlearningapp.R;
import ttuananhle.android.chatlearningapp.adapter.RecyclerMessageDetailAdapter;
import ttuananhle.android.chatlearningapp.model.Message;
import ttuananhle.android.chatlearningapp.model.Team;
import ttuananhle.android.chatlearningapp.model.User;

public class TeamChatActivity extends AppCompatActivity {

    private String TEAM;
    private String CODE;

    private FirebaseAuth fireAuth;
    private FirebaseUser fireUser;
    private FirebaseDatabase fireData;
    private DatabaseReference dataRef;


    private Toolbar toolbar;
    private TextView txtTitleToobar;

    private ImageView btnSend;
    private EditText editMessage;
    private RecyclerView recyclerView;
    private List<Message> messageList;
    private RecyclerMessageDetailAdapter messageDetailAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_chat);
        initToolbar();
        getTeam();
        mappingView();

        listenerButtonDSendOnClick();
        initRecyclerView();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_team_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_view_member:
                initDialogViewMember();
                break;
            default:
                break;
        }
        return true;
    }


    private void initDialogViewMember(){
        final List<String> membersList = new ArrayList<>();
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        View view = this.getLayoutInflater().inflate(R.layout.custom_dialog_view_member_team, null);

        ListView listView = (ListView) view.findViewById(R.id.list_member_in_team);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, membersList);
        listView.setAdapter(adapter);

        dataRef.child("Team").child(CODE).child(TEAM).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               try {
                   Team team = dataSnapshot.getValue(Team.class);
                   for (String id: team.getIdmember()){
                       dataRef.child("Users").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(DataSnapshot dataSnapshot) {
                               User user = dataSnapshot.getValue(User.class);

                               membersList.add(user.getName());

                               adapter.notifyDataSetChanged();
                           }
                           @Override
                           public void onCancelled(DatabaseError databaseError) {}
                       });
                   }
               } catch (Exception e){}

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });


        alertDialog.setView(view);
        alertDialog.show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        getListMessages();
    }

    private void mappingView(){
        btnSend = (ImageView) this.findViewById(R.id.btn_team_chat_send);
        editMessage = (EditText) this.findViewById(R.id.txt_team_chat_input);
        recyclerView = (RecyclerView) this.findViewById(R.id.recycler_message_team_detail_view);

        messageList = new ArrayList<>();
    }

    private void listenerButtonDSendOnClick(){
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editMessage.getText().toString();

                if (!text.equals("")){
                    Message message = new Message(fireUser.getUid(), TEAM, text, getCurrentTime(), false);
                    String keyMessage = dataRef.child("Messages").push().getKey();
                    dataRef.child("Messages").child(keyMessage).setValue(message);

                    dataRef.child("Team-Messages").child(CODE).child(TEAM).child(keyMessage).setValue(fireUser.getUid());
                    editMessage.setText("");
                }
            }
        });
    }

    private void getListMessages(){
        DatabaseReference dataListMessageRef = dataRef.child("Team-Messages").child(CODE).child(TEAM);
        dataListMessageRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DatabaseReference dataMessageRef = dataRef.child("Messages").child(dataSnapshot.getKey());

                // Get messages detail
                dataMessageRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Message message = dataSnapshot.getValue(Message.class);

                        if( !message.getFromId().equals(fireUser.getUid())){
                            dataRef.child("Messages").child(dataSnapshot.getKey()).child("seen").setValue(true);
                        }
                        messageList.add(message);
                        messageDetailAdapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(messageDetailAdapter.getItemCount() - 1);
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

    private void getTeam(){
        fireAuth = FirebaseAuth.getInstance();
        fireUser = fireAuth.getCurrentUser();
        fireData = FirebaseDatabase.getInstance();
        dataRef = fireData.getReference();
        Intent intent = getIntent();
        try {
            TEAM = intent.getStringExtra("TEAM");
            CODE = intent.getStringExtra("CODE");

            dataRef.child("Team").child(CODE).child(TEAM).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Team team = dataSnapshot.getValue(Team.class);
                    txtTitleToobar.setText(team.getName());
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        } catch (Exception e){

        }
    }

    private void initToolbar(){
        toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        txtTitleToobar = (TextView) toolbar.findViewById(R.id.toolbar_title);
    }

    private void initRecyclerView(){
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TeamChatActivity.this);

        messageDetailAdapter = new RecyclerMessageDetailAdapter(TeamChatActivity.this, messageList,
                new RecyclerMessageDetailAdapter.OnItemClickListener(){
                    @Override
                    public void onItemClick(Message item) {

                    }
                });

        messageDetailAdapter.registerAdapterDataObserver( new RecyclerView.AdapterDataObserver(){
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int messageCount = messageDetailAdapter.getItemCount();
                int lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (messageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    recyclerView.scrollToPosition(positionStart);
                }
            }
        });

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(messageDetailAdapter);
    }


    public String getCurrentTime(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        return simpleDateFormat.format(calendar.getTime());
    }
}
