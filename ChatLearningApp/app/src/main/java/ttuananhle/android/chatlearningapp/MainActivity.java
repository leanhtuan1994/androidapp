package ttuananhle.android.chatlearningapp;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabReselectListener;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_SIGN_IN_USER = 1000;
    public static final int REQUEST_CODE_SIGN_UP_USER = 2000;

    private BottomBar bottomBar;

    // create firebase Auth, current user
    private FirebaseAuth fireAuth;
    private FirebaseUser fireUser;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init bottom bar
        initBottomBar();

        // init firebase
        initFirebase();

    }

    /***
     * init Bottom Bar
     */
    private void initBottomBar(){
        bottomBar = (BottomBar) this.findViewById(R.id.bottomBar);

        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {

            }
        });

    }


    private void initFirebase(){
        fireAuth = FirebaseAuth.getInstance();
        fireUser = fireAuth.getCurrentUser();

        if ( fireUser == null){
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SIGN_IN_USER);
            finish();
        } else {

        }
    }
}
