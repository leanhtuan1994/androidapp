package ttuananhle.android.chatlearningapp.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ttuananhle.android.chatlearningapp.R;
import ttuananhle.android.chatlearningapp.activity.SignInActivity;
import ttuananhle.android.chatlearningapp.adapter.DividerContactItemDecotation;
import ttuananhle.android.chatlearningapp.adapter.RecyclerSetingAdapter;
import ttuananhle.android.chatlearningapp.model.Setting;
import ttuananhle.android.chatlearningapp.model.User;

/**
 * Created by leanh on 5/5/2017.
 */

public class SettingsFragment extends Fragment {
    FirebaseAuth fireAuth;
    FirebaseUser fireUser;
    FirebaseDatabase fireData;
    DatabaseReference dataRef;
    private User user = new User();
    private RecyclerView recyclerView;
    private RecyclerSetingAdapter recyclerSetingAdapter;
    private List<Setting> settingList = new ArrayList<>();

    private TextView txtUsername;
    private TextView txtUserEmail;
    private ImageView btnImageProfile;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container, false);


        getFirebase();
        mappingView(view);
        initListSetting();
        initRecyclerView(view);

        return view;
    }

    private void initListSetting(){

        settingList.add( new Setting( 1, "Sign Out", BitmapFactory.decodeResource(getResources(),
                R.drawable.signout)));
    }

    private void initRecyclerView(final View view){
        recyclerView = (RecyclerView)  view.findViewById(R.id.recycler_setting_fragment);
        recyclerView.setLayoutManager( new LinearLayoutManager(view.getContext()));
        recyclerSetingAdapter = new RecyclerSetingAdapter( view.getContext(), settingList, new RecyclerSetingAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(Setting item) {
                Log.i("OnClick", "Setting Item Clicked");
                if( item.getId() == 1){
                    fireAuth.signOut();
                    startActivity( new Intent(view.getContext(), SignInActivity.class));
                    getActivity().finish();
                }
            }
        });

        recyclerView.addItemDecoration(new DividerContactItemDecotation(view.getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(recyclerSetingAdapter);
    }

    private void mappingView(final View view){
        txtUserEmail = (TextView) view.findViewById(R.id.txt_setting_user_email);
        txtUsername = (TextView) view.findViewById(R.id.txt_setting_user_name);
        btnImageProfile = (ImageView) view.findViewById(R.id.img_setting_user_profile);

        Query query = dataRef.child("Users").child(fireUser.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                Log.i("Query", user.getName());

                // set user name and email
                txtUserEmail.setText( user.getEmail());
                txtUsername.setText( user.getName() );
                Picasso.with(view.getContext())
                        .load( user.getPhotoUrl())
                        .into(btnImageProfile);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        // listener change image profile
        btnImageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("ImageProfile", "Clicked");

                
            }
        });


    }

    private void getFirebase(){
        fireAuth = FirebaseAuth.getInstance();
        fireUser = fireAuth.getCurrentUser();
        fireData = FirebaseDatabase.getInstance();
        dataRef = fireData.getReference();
    }
}
