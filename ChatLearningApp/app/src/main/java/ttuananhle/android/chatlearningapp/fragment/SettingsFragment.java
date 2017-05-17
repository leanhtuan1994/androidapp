package ttuananhle.android.chatlearningapp.fragment;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import es.dmoral.toasty.Toasty;
import ttuananhle.android.chatlearningapp.R;
import ttuananhle.android.chatlearningapp.activity.CreateCodeActivity;
import ttuananhle.android.chatlearningapp.activity.CreateTeamActivity;
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

    public static final int CREATE_CODE = 1;
    public static final int CREATE_NEWTEAM = 2;
    public static final int SIGN_OUT = 3;

    public static final int REQUEST_CREATE_NEWTEAM = 2323;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container, false);


        getFirebase();
        mappingView(view);
        initRecyclerView(view);
        initListSetting(view);

        return view;
    }

    private void initListSetting(final View view){

        settingList.add( new Setting( SIGN_OUT, "Sign Out", BitmapFactory.decodeResource(getResources(),
                R.drawable.signout)));

        Query query = dataRef.child("Users").child(fireUser.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                Log.i("Query", user.getName());

                // set user name and email
                txtUserEmail.setText( user.getEmail());
                txtUsername.setText( user.getName() );

                // Check user is teacher
                if ( user.isTeacher()){
                    settingList.add( new Setting( CREATE_CODE, "Code", BitmapFactory.decodeResource(getResources(),
                            R.drawable.code)));

                    settingList.add( new Setting(CREATE_NEWTEAM, "Create Team", BitmapFactory.decodeResource(getResources(),
                            R.drawable.presentation)));

                    Collections.sort(settingList, new Comparator<Setting>() {
                        @Override
                        public int compare(Setting o1, Setting o2) {
                            return String.valueOf(o1.getId()).compareTo(String.valueOf(o2.getId()));
                        }
                    });
                    recyclerSetingAdapter.notifyDataSetChanged();
                }

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
    }

    private void initRecyclerView(final View view){
        recyclerView = (RecyclerView)  view.findViewById(R.id.recycler_setting_fragment);
        recyclerView.setLayoutManager( new LinearLayoutManager(view.getContext()));
        recyclerSetingAdapter = new RecyclerSetingAdapter( view.getContext(), settingList, new RecyclerSetingAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(Setting item) {
                Log.i("OnClick", "Setting Item Clicked");
                if( item.getId() == SIGN_OUT){
                    fireAuth.signOut();
                    startActivity( new Intent(view.getContext(), SignInActivity.class));
                    getActivity().finish();

                } else if (item.getId() == CREATE_CODE){
                    dataRef.child("Users").child(fireUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User teacherUser = dataSnapshot.getValue(User.class);
                            if ( teacherUser.getCode().equals("")){
                                startActivity(new Intent(view.getContext(), CreateCodeActivity.class));
                            } else {
                                AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                                alertDialog.setTitle("Code");
                                alertDialog.setMessage("Code is: " + teacherUser.getCode());
                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                alertDialog.show();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });
                } else if (item.getId() == CREATE_NEWTEAM){
                    dataRef.child("Users").child(fireUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            if (!user.getCode().equals("")){
                                Intent intent = new Intent(getActivity(), CreateTeamActivity.class);
                                startActivityForResult(intent, REQUEST_CREATE_NEWTEAM);
                            } else {
                                AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                                alertDialog.setTitle("Create Team");
                                alertDialog.setMessage("Not have Course, please create Code for Course!");
                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                alertDialog.show();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });
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

            } else  if (requestCode == REQUEST_CREATE_NEWTEAM){

            }
        }
    }
}
