package ttuananhle.android.chatlearningapp.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import es.dmoral.toasty.Toasty;
import ttuananhle.android.chatlearningapp.R;
import ttuananhle.android.chatlearningapp.fragment.ContactsFragment;
import ttuananhle.android.chatlearningapp.fragment.MessagesFragment;
import ttuananhle.android.chatlearningapp.fragment.SettingsFragment;
import ttuananhle.android.chatlearningapp.model.User;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_SIGN_IN_USER = 1000;
    public static final int REQUEST_CODE_SIGN_UP_USER = 2000;
    public static final int REQUEST_LOAD_IMAGE = 2000;

    public static  String CODE;

    // Permission for get data
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private BottomBar       bottomBar;
    private FragmentManager fragmentManager;

    // create firebase Auth, current user
    private FirebaseAuth      fireAuth;
    private FirebaseUser      fireUser;
    private FirebaseDatabase  fireData;
    private DatabaseReference dataRef;

    private User currentUser;


    private Toolbar toolbar;
    private TextView txtTitleToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyStoragePermissions(MainActivity.this);
        // init bottom bar
        initFragment(savedInstanceState);
        initToolbar();
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
                    // Set title toolber
                    txtTitleToolbar.setText("Settings");

                    // Replace Fragment
                    SettingsFragment settingsFragment = new SettingsFragment();
                    fragmentManager.beginTransaction().replace(R.id.frame_container, settingsFragment)
                            .commit();

                } else if ( tabId == R.id.tab_contacts ){
                    // Set title toolber
                    txtTitleToolbar.setText("Contacts");

                    // Replace Fragment
                    ContactsFragment contactsFragment = new ContactsFragment();
                    fragmentManager.beginTransaction().replace(R.id.frame_container, contactsFragment)
                            .commit();
                } else if ( tabId == R.id.tab_messages){
                    // Set title toolber
                    txtTitleToolbar.setText("Messages");

                    // Replace Fragment
                    MessagesFragment messagesFragment = new MessagesFragment();
                    fragmentManager.beginTransaction().replace(R.id.frame_container, messagesFragment)
                            .commit();
                }
            }
        });
    }

    private void initToolbar(){
        toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        txtTitleToolbar = (TextView) toolbar.findViewById(R.id.toolbar_title);
    }

    private void initFragment(Bundle savedInstanceState){
        fragmentManager = getSupportFragmentManager();
        Fragment fragment = new MessagesFragment();

        if(savedInstanceState == null){
            fragmentManager.beginTransaction().add(R.id.frame_container, fragment, "messages_fragment")
                    .commit();
        } else {
            fragment = fragmentManager.findFragmentByTag("messages_fragment");
        }

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
                    Log.i("DATA", dataSnapshot.toString());

                    if (dataSnapshot.getKey().equals(fireUser.getUid()) ){
                        currentUser = dataSnapshot.getValue(User.class);

                        // if user is student -> check code
                        if (!currentUser.isTeacher()){
                            CODE = currentUser.getCode();
                            if (CODE.equals("")){
                                // Not have code
                                startActivity(new Intent(MainActivity.this, CheckCodeActivity.class));
                                finish();
                            } else {
                                Toasty.success(MainActivity.this,"Hi! " + currentUser.getName(), Toast.LENGTH_LONG).show();
                            }
                        }
                        return;
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
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Checks if the app has permission to write to device storage
     * If the app does not has permission then the user will be prompted to grant permissions
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_LOAD_IMAGE
            );
        }
    }

}
