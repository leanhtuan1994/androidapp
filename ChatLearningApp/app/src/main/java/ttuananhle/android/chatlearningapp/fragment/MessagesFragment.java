package ttuananhle.android.chatlearningapp.fragment;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ttuananhle.android.chatlearningapp.R;
import ttuananhle.android.chatlearningapp.activity.ChatActivity;
import ttuananhle.android.chatlearningapp.adapter.RecyclerMessagesAdapter;
import ttuananhle.android.chatlearningapp.model.Message;
import ttuananhle.android.chatlearningapp.model.MessagePerUserTo;
import ttuananhle.android.chatlearningapp.model.User;

/**
 * Created by leanh on 5/4/2017.
 */

public class MessagesFragment extends Fragment {

    private List<MessagePerUserTo> userToList;
    private Map<String, MessagePerUserTo> messageMap = new HashMap<>();

    private FirebaseAuth fireAuth;
    private FirebaseUser fireUser;
    private FirebaseDatabase fireData;
    private DatabaseReference dataRef;

    private RecyclerView recyclerView;
    private RecyclerMessagesAdapter messagesAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.messages_fragment, container, false);

        setHasOptionsMenu(true);
        mappingFirebase();
        mappingView(view);

        initRecyclerView(view);



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initListMessage();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_message_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_create_message:
                Log.i("Messages", "Create messages on click");

                break;

            default:
                break;
        }
        return true;
    }

    private void mappingView(final  View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_messages_view);

        userToList = new ArrayList<>();
    }

    private void initListMessage(){
        DatabaseReference dataUserMessageRef = dataRef.child("User-Messages").child(fireUser.getUid());
        dataUserMessageRef.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(DataSnapshot dataSnapshot, String s) {
               Log.i("Message", dataSnapshot.toString());

               Query dataMessageRef = dataRef.child("Messages").child(dataSnapshot.getKey());
               dataMessageRef.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {
                       Log.i("MessagePer", dataSnapshot.toString());
                       final Message message = dataSnapshot.getValue(Message.class);
                       String idChatUser = message.getToId();

                       if ( message.getToId().equals(fireUser.getUid())) {
                           idChatUser = message.getFromId();
                       }
                       dataRef.child("Users").child(idChatUser).addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(DataSnapshot dataSnapshot) {
                               Log.i("toUser", dataSnapshot.getKey());
                               User chatUser = dataSnapshot.getValue(User.class);

                               MessagePerUserTo messagePer = new
                                       MessagePerUserTo( chatUser.getId(),chatUser.getName(), message.getText(),
                                       chatUser.getPhotoUrl(), message.getTime(), message.isSeen(), message.getFromId());

                               messageMap.put(chatUser.getId(), messagePer);
                               Log.i("Check", "" + messageMap.size());
                               userToList.clear();
                               for( Map.Entry<String, MessagePerUserTo> entry : messageMap.entrySet()){
                                   //  add message to list
                                     userToList.add(entry.getValue());
                               }

                               // Sort list messages by time
                               Collections.sort(userToList, new Comparator<MessagePerUserTo>() {
                                     @Override
                                     public int compare(MessagePerUserTo o1, MessagePerUserTo o2) {
                                         return o1.getTime().compareTo(o2.getTime());
                                     }
                               });

                               // Reverse data
                               Collections.reverse(userToList);

                               // Check seen new message
                               for ( MessagePerUserTo item: userToList) {
                                  if ( !item.getSendId().equals(fireUser.getUid()) && !item.isSeen()){
                                      Log.i("Seen", item.getSendId());

                                      // Send notification
                                     // Intent intent = new Intent(getActivity(), ChatActivity.class);
                                    //  intent.putExtra("toId", item.getToId());

                                   //   PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                   //   NotificationManager manager = (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
                                   //   NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext())
                                   //           .setSmallIcon(R.mipmap.boxchat_logo)
                                   //           .setContentTitle(item.getName())
                                   //           .setAutoCancel(true)
                                    //          .setContentText(item.getMassage())
                                    //          .setContentIntent(pendingIntent);

                                    //  manager.notify(111, builder.build());

                                  }
                               }

                               messagesAdapter.notifyDataSetChanged();
                           }
                           @Override
                           public void onCancelled(DatabaseError databaseError) {}
                       });
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


    private void initRecyclerView(final View view){
        recyclerView.setLayoutManager( new LinearLayoutManager( getContext()));
        messagesAdapter = new RecyclerMessagesAdapter(getContext(), userToList, new RecyclerMessagesAdapter.OnItemClickListener(){
            @Override
           public void onItemClick(MessagePerUserTo item){
                Log.i("ItemOnclick", item.getName() + "is selected");
                
                Intent intent = new Intent( view.getContext(), ChatActivity.class);
                intent.putExtra("toId", item.getToId());
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(messagesAdapter);
    }

    private void mappingFirebase(){
        fireAuth = FirebaseAuth.getInstance();
        fireUser = fireAuth.getCurrentUser();
        fireData = FirebaseDatabase.getInstance();
        dataRef = fireData.getReference();
    }
}
