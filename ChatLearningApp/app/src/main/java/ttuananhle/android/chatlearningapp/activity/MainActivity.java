package ttuananhle.android.chatlearningapp.activity;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import es.dmoral.toasty.Toasty;
import ttuananhle.android.chatlearningapp.R;
import ttuananhle.android.chatlearningapp.fragment.ContactsFragment;
import ttuananhle.android.chatlearningapp.fragment.MessagesFragment;
import ttuananhle.android.chatlearningapp.model.User;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_SIGN_IN_USER = 1000;
    public static final int REQUEST_CODE_SIGN_UP_USER = 2000;

    private BottomBar       bottomBar;
    private FragmentManager fragmentManager;

    // create firebase Auth, current user
    private FirebaseAuth      fireAuth;
    private FirebaseUser      fireUser;
    private FirebaseDatabase  fireData;
    private DatabaseReference dataRef;


    private User currentUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init bottom bar
        initFragment();
        initBottomBar();


        // init firebase
        initFirebase();
        checkCurrentUserFirebase();


    }

    /***
     * init Bottom Bar
     */
    private void initBottomBar(){
        bottomBar = (BottomBar) this.findViewById(R.id.bottomBar);
        bottomBar.setDefaultTab(R.id.tab_messages);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if(tabId == R.id.tab_settings){
                    Log.i("Tab", "tab settings selected!");
                    fireAuth.signOut();
                    Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                    startActivity(intent);
                    finish();
                } else if ( tabId == R.id.tab_contacts ){
                    ContactsFragment contactsFragment = new ContactsFragment();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, contactsFragment);
                    fragmentTransaction.commit();
                } else if ( tabId == R.id.tab_messages){
                    MessagesFragment messagesFragment = new MessagesFragment();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, messagesFragment);
                    fragmentTransaction.commit();
                }
            }
        });
    }

    private void initFragment(){
        fragmentManager = getSupportFragmentManager();

        //Set default fragment
        MessagesFragment messagesFragment = new MessagesFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, messagesFragment);
        fragmentTransaction.commit();
    }
    /**
     * init Firebase
     */
    private void initFirebase(){
        fireAuth = FirebaseAuth.getInstance();
        fireUser = fireAuth.getCurrentUser();
        fireData = FirebaseDatabase.getInstance();
        dataRef = fireData.getReference();
    }

    /**
     *
     */
    private void checkCurrentUserFirebase(){
        if ( fireUser == null){
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
        } else {


            dataRef.child("Users").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    Log.i("USER", "fireUSER = " + fireUser.getUid());
                    Log.i("USER", "dataUSER = " + dataSnapshot.getKey());

                    if (dataSnapshot.getKey().equals(fireUser.getUid()) ){
                        currentUser = dataSnapshot.getValue(User.class);

                        Toasty.success(MainActivity.this,"Hi! " + currentUser.getName(), Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
