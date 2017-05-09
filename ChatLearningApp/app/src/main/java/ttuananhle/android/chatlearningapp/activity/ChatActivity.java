package ttuananhle.android.chatlearningapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import ttuananhle.android.chatlearningapp.R;
import ttuananhle.android.chatlearningapp.model.Message;
import ttuananhle.android.chatlearningapp.model.User;

public class ChatActivity extends AppCompatActivity {

    private ImageView btnSend;
    private EditText edtText;

    private String toId;
    private FirebaseAuth fireAuth;
    private FirebaseUser fireUser;
    private FirebaseDatabase fireData;
    private DatabaseReference dataRef;

    private Calendar calendar;

    private Toolbar toolbar;
    private TextView txtTitleToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        mappingView();
        getUser();
        initToolbar();
        listenerBtnSend();


    }

    private void getUser(){
        fireAuth = FirebaseAuth.getInstance();
        fireUser = fireAuth.getCurrentUser();
        fireData = FirebaseDatabase.getInstance();
        dataRef = fireData.getReference();
        toId = getIntent().getStringExtra("toId");

    }
    private void mappingView(){
        btnSend = (ImageView) this.findViewById(R.id.btn_chat_send);
        edtText = (EditText) this.findViewById(R.id.txt_chat_input);

    }

    private void listenerBtnSend(){
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = edtText.getText().toString();
                if (text != ""){

                    DatabaseReference dataMessageRef = dataRef.child("Messages");
                    Message message = new Message(fireUser.getUid(), toId, text, getCurrentTime());
                    // Push message to firebase - Messages
                    String key = dataMessageRef.push().getKey();
                    dataMessageRef.child(key).setValue(message);

                    // Save data user and key message in database to support multi user
                    DatabaseReference dataUserMessageRef = dataRef.child("User-Messages").child(fireUser.getUid());
                    dataUserMessageRef.child(key).setValue(toId);


                    // Set edit text;
                    edtText.setText("");
                }
            }
        });
    }

    private void initToolbar(){
        toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        txtTitleToolbar = (TextView) toolbar.findViewById(R.id.toolbar_title);

        // Get to user name
        dataRef.child("Users").child(toId).addListenerForSingleValueEvent(new ValueEventListener() {
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
}
