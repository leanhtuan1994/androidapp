package ttuananhle.android.chatlearningapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import ttuananhle.android.chatlearningapp.R;
import ttuananhle.android.chatlearningapp.activity.ChatActivity;
import ttuananhle.android.chatlearningapp.adapter.DividerItemDecotation;
import ttuananhle.android.chatlearningapp.adapter.RecyclerContactAdapter;
import ttuananhle.android.chatlearningapp.model.User;

/**
 * Created by leanh on 5/4/2017.
 */

public class ContactsFragment extends Fragment {

    private RecyclerView           recyclerContactView;
    private RecyclerContactAdapter recyclerContactAdapter;
    private List<User>             userList = new ArrayList<>();
    private FirebaseDatabase       fireData;
    private DatabaseReference      dataRef;
    private FirebaseAuth           fireAuth;
    private FirebaseUser           fireUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contacts_fragment, container, false);

        initRecyclerContactView(view);
        listenerDataUser();

        return view;
    }


    private void listenerDataUser(){
        fireData = FirebaseDatabase.getInstance();
        dataRef = fireData.getReference();
        fireAuth = FirebaseAuth.getInstance();
        fireUser = fireAuth.getCurrentUser();

        Query query = dataRef.child("Users").orderByChild("name");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                // Check current user not add list
                if ( !dataSnapshot.getKey().equals(fireUser.getUid())){
                    userList.add( dataSnapshot.getValue( User.class));

                    // notify data changed for recycler view
                    recyclerContactAdapter.notifyDataSetChanged();
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
    }

    private void initRecyclerContactView(final View view){
        recyclerContactView = (RecyclerView) view.findViewById(R.id.recycler_contacts_view);
        recyclerContactView.setLayoutManager( new LinearLayoutManager(view.getContext()));
        recyclerContactAdapter = new RecyclerContactAdapter(view.getContext(), userList, new RecyclerContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(User item) {
                Log.i("ItemOnclick", item.getName() + "is selected");

                Intent intent = new Intent( view.getContext(), ChatActivity.class);
                intent.putExtra("toId", item.getId());
                startActivity(intent);
            }
        });

        // Add Divider
        recyclerContactView.addItemDecoration( new DividerItemDecotation(view.getContext(), LinearLayoutManager.VERTICAL, 300));
        recyclerContactView.setAdapter(recyclerContactAdapter);

    }


}
