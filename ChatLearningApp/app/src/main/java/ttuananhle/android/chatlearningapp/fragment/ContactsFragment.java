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
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import ttuananhle.android.chatlearningapp.R;
import ttuananhle.android.chatlearningapp.activity.ChatActivity;
import ttuananhle.android.chatlearningapp.adapter.DividerContactItemDecotation;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contacts_fragment, container, false);

        initRecyclerContactView(view);
        listenerDataUser(view);

        return view;
    }


    private void listenerDataUser(final View view){
        fireData = FirebaseDatabase.getInstance();
        dataRef = fireData.getReference();

        dataRef.child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                userList.add( dataSnapshot.getValue( User.class));

                // notity data changed for recycler view
                recyclerContactAdapter.notifyDataSetChanged();
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
                intent.putExtra("id", item.getId());
                startActivity(intent);
            }
        });

        // Add Divider
        recyclerContactView.addItemDecoration( new DividerContactItemDecotation(view.getContext(), LinearLayoutManager.VERTICAL));
        recyclerContactView.setAdapter(recyclerContactAdapter);

    }


}
