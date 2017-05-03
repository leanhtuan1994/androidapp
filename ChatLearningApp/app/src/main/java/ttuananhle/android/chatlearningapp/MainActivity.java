package ttuananhle.android.chatlearningapp;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabReselectListener;

public class MainActivity extends AppCompatActivity {

    private BottomBar bottomBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init bottom bar
        initBottomBar();


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
}
