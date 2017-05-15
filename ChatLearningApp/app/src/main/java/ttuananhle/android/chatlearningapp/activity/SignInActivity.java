package ttuananhle.android.chatlearningapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;
import ttuananhle.android.chatlearningapp.R;

public class SignInActivity extends AppCompatActivity {
    private Button btnSignIn;
    private EditText edtSignInEmail;
    private EditText edtSignInPassword;
    private TextView txtToSignUp;
    private CheckBox cbRemember;
    public static final String PREF_MEM_NAME = "REMEMBER_USER_DATA";

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        firebaseAuth = FirebaseAuth.getInstance();

        mappingView();
        listenerTextViewClick();
        listenerButtonSignInOnClick();


    }

    /**
     * Save data sign in
     */
    @Override
    protected void onPause() {
        super.onPause();
        savePrefSignInData();
    }

    /**
     * Restore data sign in
     */
    @Override
    protected void onResume() {
        super.onResume();
        restoringPrefSignInData();
    }


    private void listenerButtonSignInOnClick(){
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Click", "btnSignIn Clicked");
                try {
                    String email = edtSignInEmail.getText().toString();
                    String password = edtSignInPassword.getText().toString();

                    firebaseAuth.signInWithEmailAndPassword( email, password)
                            .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        startActivity(new Intent(SignInActivity.this, MainActivity.class));
                                        finish();
                                    } else {
                                        Toasty.error(SignInActivity.this,
                                                task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                } catch (Exception ex){
                    Toasty.error(SignInActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * set click for txtToSignIn
     */
    private void listenerTextViewClick(){
        txtToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Click", "txtToSignUp Clicked");
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });
    }

    /***
     * mapping view from xml
     */
    private void mappingView(){
        btnSignIn = (Button) this.findViewById(R.id.btnSignIn);
        txtToSignUp = (TextView )this.findViewById(R.id.txtToSignUp);
        edtSignInPassword = (EditText) this.findViewById(R.id.edtSignInPassword);
        edtSignInEmail = (EditText) this.findViewById(R.id.edtSignInEmail);
        cbRemember = (CheckBox) this.findViewById(R.id.cbSignIn_remember);
    }

    /**
     * Use Preference to save data signin
     */
    private void savePrefSignInData(){
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_MEM_NAME, MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        String email = edtSignInEmail.getText().toString();
        String password = edtSignInPassword.getText().toString();
        boolean isRemember = cbRemember.isChecked();

        if (isRemember){
            editor.putString("email", email);
            editor.putString("password", password);
            editor.putBoolean("isremember", isRemember);
        } else {
            editor.clear();
        }

        editor.commit();
    }

    /**
     * Restore data in Preference
     */
    private void restoringPrefSignInData(){
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_MEM_NAME, MODE_PRIVATE);
        boolean isRemember = sharedPreferences.getBoolean("isremember", false);

        if (isRemember){
            edtSignInEmail.setText( sharedPreferences.getString("email", ""));
            edtSignInPassword.setText( sharedPreferences.getString("password", ""));
        }

        cbRemember.setChecked(isRemember);

    }
}
