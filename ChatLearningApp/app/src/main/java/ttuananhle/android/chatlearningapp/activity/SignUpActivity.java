package ttuananhle.android.chatlearningapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.dmoral.toasty.Toasty;
import ttuananhle.android.chatlearningapp.R;
import ttuananhle.android.chatlearningapp.model.User;

public class SignUpActivity extends AppCompatActivity {

    private TextView txtToSignIn;
    private EditText edtSignUpEmail;
    private EditText edtSignUpPassword;
    private EditText edtSignUpName;
    private Button   btnSignUp;

    private FirebaseAuth fireAuth;
    private FirebaseDatabase fireData;
    private DatabaseReference dataRef;
    private FirebaseUser fireUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mappingView();
        initFirebase();
        listenerTxtToSignInOnClick();
        listenerButtonSignUpOnClick();
    }


    private void listenerButtonSignUpOnClick(){
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Click", "btnSignUp Clicked");

                try {
                    final String email    = edtSignUpEmail.getText().toString();
                    final String password = edtSignUpPassword.getText().toString();
                    final String name     = edtSignUpName.getText().toString();

                    fireAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if ( task.isSuccessful()){
                                        Log.i("SignUp", "createUserWithEmail:success");
                                        fireUser = fireAuth.getCurrentUser();
                                        saveFireUserOnData(fireUser.getUid(), name, email, password);

                                        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                        finish();
                                    } else {
                                        Toasty.error(SignUpActivity.this,
                                                task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                } catch (Exception ex){
                    Toasty.error(SignUpActivity.this,
                            ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void saveFireUserOnData(final String id, final String name, String email, String password){

        String photoUrl = "https://firebasestorage.googleapis.com/v0/b/chatlearningapp-338d2.appspot." +
                "com/o/photo_profile.png?alt=media&token=9df30e16-083c-494e-9bb7-9baa56e5c741";

        User userSignUp = new User(id, name, email, password, photoUrl);

        dataRef.child("Users").child(id).setValue(userSignUp);

        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(photoUrl))
                .setDisplayName(name)
                .build();

        fireUser.updateProfile(request)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.i("Users", "User profile updated.");
                        } else {
                            Toasty.error(SignUpActivity.this,
                                    task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

        dataRef.child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i("Users", "Added: " + dataSnapshot);

                if( dataSnapshot.getValue(User.class).getId() == id) {

                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.i("Users", "Changed: " + dataSnapshot.getKey());
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

    private void initFirebase(){
        fireAuth = FirebaseAuth.getInstance();
        fireData = FirebaseDatabase.getInstance();
        dataRef = fireData.getReference();
    }

    private void listenerTxtToSignInOnClick(){
        txtToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Click", "txtToSignIn Clicked");
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                finish();
            }
        });
    }
    private void mappingView(){
        txtToSignIn = (TextView) this.findViewById(R.id.txtToSignIn);
        edtSignUpEmail = (EditText) this.findViewById(R.id.edtSignUpEmail);
        edtSignUpName = (EditText) this.findViewById(R.id.edtSignUpName);
        edtSignUpPassword = (EditText) this.findViewById(R.id.edtSignUpPassword);
        btnSignUp = (Button) this.findViewById(R.id.btnSignUp);
    }
}
