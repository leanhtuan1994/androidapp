package ttuananhle.android.chatlearningapp.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Range;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ttuananhle.android.chatlearningapp.R;

/**
 * Created by leanh on 5/4/2017.
 */

public class MessagesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.messages_fragment, container, false);


        return view;
    }
}