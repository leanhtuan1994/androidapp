package ttuananhle.android.chatlearningapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import ttuananhle.android.chatlearningapp.R;
import ttuananhle.android.chatlearningapp.activity.SignInActivity;

/**
 * Created by leanh on 5/5/2017.
 */

public class SettingsFragment extends Fragment {
    FirebaseAuth fireAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container, false);


        return view;
    }
}
