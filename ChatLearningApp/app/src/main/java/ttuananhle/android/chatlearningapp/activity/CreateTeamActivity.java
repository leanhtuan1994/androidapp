package ttuananhle.android.chatlearningapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.tokenautocomplete.FilteredArrayAdapter;
import com.tokenautocomplete.TokenCompleteTextView;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import ttuananhle.android.chatlearningapp.R;
import ttuananhle.android.chatlearningapp.model.Team;
import ttuananhle.android.chatlearningapp.model.User;
import ttuananhle.android.chatlearningapp.viewholder.UserCompletionView;

public class CreateTeamActivity extends AppCompatActivity {

    private UserCompletionView completionView;
    private ArrayAdapter<User> adapter;
    private List<User> userList = new ArrayList<>();

    private FirebaseDatabase fireData;
    private DatabaseReference dataRef;
    private FirebaseAuth fireAuth;
    private FirebaseUser fireUser;

    private TextInputEditText edtName;
    private Button btnCreateTeam;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);

        mappingView();
        initAutoCompleList();
        initListUser();
        listenerButtonCreatePre();

    }

    private void listenerButtonCreatePre(){
        btnCreateTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               try {
                   final String name = edtName.getText().toString();

                   if (!name.equals("")  && completionView.getObjects().size() != 0){
                       final List<String> listTeamId = new ArrayList<>();
                       for (Object token : completionView.getObjects()){
                           listTeamId.add(token.toString());
                       }

                       dataRef.child("Users").child(fireUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(DataSnapshot dataSnapshot) {
                               User user = dataSnapshot.getValue(User.class);

                               String code = user.getCode();
                               String key =  dataRef.child("Team").child(code).push().getKey();
                               Team team = new Team(key, name, listTeamId);

                               dataRef.child("Team").child(code).child(key).setValue(team);

                               // Save id team for user
                               for (String id : listTeamId){
                                   dataRef.child("Users").child(id).child("team").setValue(key);
                               }

                               Toasty.success(CreateTeamActivity.this, "Create team successful!", Toast.LENGTH_LONG).show();
                               Intent intent = new Intent();
                               CreateTeamActivity.this.setResult(RESULT_OK, intent);
                               finish();
                           }

                           @Override
                           public void onCancelled(DatabaseError databaseError) {}
                       });

                   } else if (completionView.getObjects().size() == 0){
                       Toasty.info(CreateTeamActivity.this, "Input student name in this team!", Toast.LENGTH_LONG).show();
                   } else {
                       Toasty.info(CreateTeamActivity.this, "Input team name!", Toast.LENGTH_LONG).show();
                   }
               } catch (Exception e){
                   Toasty.info(CreateTeamActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
               }
            }
        });
    }

    private void mappingView(){
        this.completionView = (UserCompletionView) this.findViewById(R.id.searchUser);
        this.edtName = (TextInputEditText) this.findViewById(R.id.edtTeamName);
        this.btnCreateTeam = (Button) this.findViewById(R.id.btnCreateteam);

        fireData = FirebaseDatabase.getInstance();
        dataRef  = fireData.getReference();
        fireAuth = FirebaseAuth.getInstance();
        fireUser = fireAuth.getCurrentUser();

    }

    private void initListUser(){
        dataRef.child("Users").child(fireUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User user = dataSnapshot.getValue(User.class);

                dataRef.child("Code").child(user.getCode()).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                       if (!dataSnapshot.getKey().equals(user.getId())){
                           dataRef.child("Users").child(dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                               @Override
                               public void onDataChange(DataSnapshot dataSnapshot) {
                                  try {
                                      User student = dataSnapshot.getValue(User.class);

                                      if(student.getTeam().equals("") && !student.isTeacher()){
                                          Log.i("Team", student.getName());
                                          userList.add(student);
                                          adapter.notifyDataSetChanged();
                                      }

                                  } catch (Exception e){}

                               }

                               @Override
                               public void onCancelled(DatabaseError databaseError) {}
                           });
                       }
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
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void initAutoCompleList(){
        adapter = new FilteredArrayAdapter<User>(this, R.layout.user_autocomplete, userList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {

                    LayoutInflater l = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                    convertView = l.inflate(R.layout.user_autocomplete, parent, false);
                }

                User p = getItem(position);
                ((TextView)convertView.findViewById(R.id.user_name)).setText(p.getName());
                ((TextView)convertView.findViewById(R.id.user_email)).setText(p.getEmail());

                return convertView;
            }

            @Override
            protected boolean keepObject(User person, String mask) {
                mask = mask.toLowerCase();
                return person.getName().toLowerCase().startsWith(mask) || person.getEmail().toLowerCase().startsWith(mask);
            }
        };


        completionView.setAdapter(adapter);
        completionView.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Select);

    }
}
