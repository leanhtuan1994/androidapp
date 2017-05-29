package ttuananhle.android.chatlearningapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import ttuananhle.android.chatlearningapp.R;

public class CheckCodeActivity extends AppCompatActivity {

    private EditText edtCheckCode;
    private Button btnCheckCode;

    private FirebaseAuth fireAuth;
    private FirebaseUser fireUser;
    private FirebaseDatabase fireData;
    private DatabaseReference dataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_code);

        mappingView();
        initFirebase();
        listenerCkeckCodeOnClick();


    }

    private void mappingView(){
        edtCheckCode = (EditText) this.findViewById(R.id.edtCheckCode);
        btnCheckCode = (Button) this.findViewById(R.id.btnCheckCode);
    }

    private void initFirebase(){
        fireAuth = FirebaseAuth.getInstance();
        fireUser = fireAuth.getCurrentUser();
        fireData = FirebaseDatabase.getInstance();
        dataRef = fireData.getReference();
    }

    private void listenerCkeckCodeOnClick(){
        btnCheckCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String code = edtCheckCode.getText().toString();
                if (code.equals("")) return;
                try {
                    dataRef.child("Code").child(code).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if ( dataSnapshot.getValue() == null){
                                Log.i("CheckCode", "error");
                                Toasty.info(CheckCodeActivity.this, "Code Error", Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });


                    //////////////////////////////////////////////////////////////////////
                    dataRef.child("Code").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            if (dataSnapshot.getKey().equals(code)){
                                // Save user id in code data

                                dataRef.child("Code").child(code).child(fireUser.getUid()).setValue(2);

                                // Save code in user data
                                dataRef.child("Users").child(fireUser.getUid()).child("code").setValue(code);

                                startActivity( new Intent(CheckCodeActivity.this, MainActivity.class));
                                finish();
                            }
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
                }catch (Exception e){
                    Toasty.info(CheckCodeActivity.this, e.getMessage() , Toast.LENGTH_LONG).show();
                }


            }
        });
    }
}
