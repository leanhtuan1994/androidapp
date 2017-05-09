package ttuananhle.android.chatlearningapp.fragment;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import ttuananhle.android.chatlearningapp.R;
import ttuananhle.android.chatlearningapp.activity.SignInActivity;
import ttuananhle.android.chatlearningapp.adapter.DividerItemDecotation;
import ttuananhle.android.chatlearningapp.adapter.RecyclerSetingAdapter;
import ttuananhle.android.chatlearningapp.model.Setting;
import ttuananhle.android.chatlearningapp.model.User;

/**
 * Created by leanh on 5/5/2017.
 */

public class SettingsFragment extends Fragment {
    private FirebaseAuth fireAuth;
    private FirebaseUser fireUser;
    private FirebaseDatabase fireData;
    private DatabaseReference dataRef;
    private FirebaseStorage fireStorage;
    private StorageReference storageRef;


    private User user = new User();
    private RecyclerView recyclerView;
    private RecyclerSetingAdapter recyclerSetingAdapter;
    private List<Setting> settingList = new ArrayList<>();

    private TextView txtUsername;
    private TextView txtUserEmail;
    private ImageView btnImageProfile;

    private int REQUEST_LOAD_IMAGE = 1100;


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

        recyclerView.addItemDecoration(new DividerItemDecotation(view.getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(recyclerSetingAdapter);
    }

    private void mappingView(final View view){
        txtUserEmail = (TextView) view.findViewById(R.id.txt_setting_user_email);
        txtUsername = (TextView) view.findViewById(R.id.txt_setting_user_name);
        btnImageProfile = (ImageView) view.findViewById(R.id.img_setting_user_profile);

        Query query = dataRef.child("Users").child(fireUser.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                Log.i("Query", user.getName());

                // set user name and email
                txtUserEmail.setText( user.getEmail());
                txtUsername.setText( user.getName() );
                try {
                    Log.i("Photo", user.getPhotoUrl());
                    Picasso.with(view.getContext())
                            .load( user.getPhotoUrl())
                            .into(btnImageProfile);
                } catch (Exception e){

                }
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
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, REQUEST_LOAD_IMAGE);
            }
        });
    }



    private void getFirebase(){
        fireAuth = FirebaseAuth.getInstance();
        fireUser = fireAuth.getCurrentUser();
        fireData = FirebaseDatabase.getInstance();
        dataRef = fireData.getReference();
        fireStorage = FirebaseStorage.getInstance();
        storageRef = fireStorage.getReference();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( resultCode == Activity.RESULT_OK){
            if(requestCode == REQUEST_LOAD_IMAGE){
                try {
                    final Uri imageUri = data.getData();
                    Picasso.with(getContext()).load(imageUri).into(btnImageProfile);
                    Log.i("Image", "Set image succesfull");

                    final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] dataImage = baos.toByteArray();
                    StorageReference storageUserRef = storageRef.child("PhotoProfile").child(fireUser.getUid());

                    UploadTask uploadTask = storageUserRef.putBytes(dataImage);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toasty.info(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get Uri in storage
                            @SuppressWarnings("VisibleForTests") Uri downloadUri = Uri.parse(taskSnapshot.getDownloadUrl().toString());

                            /* Save image profile */
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(downloadUri)
                                    .build();

                            fireUser.updateProfile(profileChangeRequest)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if ( task.isSuccessful()){
                                                Log.i("UpdateProfileUser", "Oke");
                                            }
                                        }
                                    });

                            // Save image in data
                            dataRef.child("Users").child(fireUser.getUid()).child("photoUrl").setValue(downloadUri.toString());

                        }
                    });

                }catch (Exception ex){
                    Toasty.info(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
