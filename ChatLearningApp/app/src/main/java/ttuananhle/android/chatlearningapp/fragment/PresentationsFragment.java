package ttuananhle.android.chatlearningapp.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import java.util.Collections;
import java.util.List;

import es.dmoral.toasty.Toasty;
import ttuananhle.android.chatlearningapp.R;
import ttuananhle.android.chatlearningapp.activity.QuesionPresentationActivity;
import ttuananhle.android.chatlearningapp.adapter.RecyclerPresentationAdapter;
import ttuananhle.android.chatlearningapp.model.Presentation;
import ttuananhle.android.chatlearningapp.model.Team;
import ttuananhle.android.chatlearningapp.model.User;

/**
 * Created by leanh on 5/17/2017.
 */

public class PresentationsFragment extends Fragment {

    private FirebaseAuth fireAuth;
    private FirebaseUser fireUser;
    private FirebaseDatabase fireData;
    private DatabaseReference dataRef;

    private RecyclerView recyclerView;
    private RecyclerPresentationAdapter presentationAdapter;
    private List<Presentation> presentationList;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.presentations_fragment, container, false);
        setHasOptionsMenu(true);

        mappingView(view);
        mappingFirebase();
        initRecyclerView();



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getListPresentation();

    }

    private void initRecyclerView(){
        recyclerView.setLayoutManager( new LinearLayoutManager(getContext()));
        presentationAdapter = new RecyclerPresentationAdapter(getContext(), presentationList, new RecyclerPresentationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Presentation item) {
                Log.i("Presentation", "OnClick " + item.getId());
                Intent intent = new Intent(getContext(), QuesionPresentationActivity.class);
                intent.putExtra("PRESENTATION_ID", item.getId());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(presentationAdapter);
    }

    private void getListPresentation(){
        presentationList.clear();
        dataRef.child("Users").child(fireUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    User user = dataSnapshot.getValue(User.class);
                    dataRef.child("Presentations").child(user.getCode()).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            try {
                                Presentation presentation = dataSnapshot.getValue(Presentation.class);

                                presentationList.add(presentation);


                                Collections.reverse(presentationList);
                                presentationAdapter.notifyDataSetChanged();
                            } catch (Exception e){}
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
                } catch (Exception e){}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_presentation_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_create_presentation:
                dataRef.child("Users").child(fireUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            User user = dataSnapshot.getValue(User.class);
                            if (!user.getCode().equals("") && user.isTeacher()) {
                                final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                                View view = getActivity().getLayoutInflater().inflate(R.layout.custom_dialog_create_presentation, null);
                                final TextInputEditText edtTopic = (TextInputEditText) view.findViewById(R.id.edtTopic);
                                final Spinner spinnerTeam = (Spinner) view.findViewById(R.id.spinner_team);
                                Button btnCreatePre = (Button) view.findViewById(R.id.btn_create_presentation);

                                // Set data for spinner
                                final List<Team> listTeam = new ArrayList<>();
                                final ArrayAdapter<Team> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, listTeam);
                                adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                                spinnerTeam.setAdapter(adapter);

                                btnCreatePre.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            final Team selectedTeam = (Team) spinnerTeam.getSelectedItem();

                                            final String topicName = edtTopic.getText().toString();
                                            if ( selectedTeam == null){
                                                Toasty.info(getContext(), "Please Create team in your class!", Toast.LENGTH_LONG).show();
                                            } else if (!topicName.equals("")){
                                                dataRef.child("Users").child(fireUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        try {
                                                            User user = dataSnapshot.getValue(User.class);
                                                            String key =  dataRef.child("Presentations").child(user.getCode()).push().getKey();
                                                            Presentation presentation = new Presentation( key, selectedTeam.getId(), topicName, getCurrentTime());
                                                            dataRef.child("Presentations").child(user.getCode()).child(key).setValue(presentation);

                                                            Toasty.success(getContext(), "Create Presentation Successful!", Toast.LENGTH_LONG).show();
                                                            alertDialog.dismiss();

                                                        }catch (Exception e){}
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {}
                                                });
                                            } else {
                                                Toasty.info(getContext(), " Please input Presentation name", Toast.LENGTH_LONG).show();
                                            }
                                        } catch (Exception e){
                                            Toasty.info(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                                dataRef.child("Users").child(fireUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        try {
                                            User user = dataSnapshot.getValue(User.class);

                                            if (!user.getCode().equals("")){
                                                dataRef.child("Team").child(user.getCode()).addChildEventListener(new ChildEventListener() {
                                                    @Override
                                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                        Team team = dataSnapshot.getValue(Team.class);
                                                        listTeam.add(team);

                                                        adapter.notifyDataSetChanged();
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
                                        }catch ( Exception e){

                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {}
                                });


                                alertDialog.setView(view);
                                alertDialog.show();

                            } else {
                                Toasty.info(getContext(), "Can't create presentation!", Toast.LENGTH_LONG).show();
                            }
                        }catch (Exception e){
                            Toasty.info(getContext(), "Can't create presentation!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
                break;
            default:
                break;
        }
        return true;
    }

    private void mappingView(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_presentation_view);

        presentationList = new ArrayList<>();
    }

    private void mappingFirebase(){
        fireAuth = FirebaseAuth.getInstance();
        fireUser = fireAuth.getCurrentUser();
        fireData = FirebaseDatabase.getInstance();
        dataRef = fireData.getReference();
    }

    public String getCurrentTime(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        return simpleDateFormat.format(calendar.getTime());
    }

}
