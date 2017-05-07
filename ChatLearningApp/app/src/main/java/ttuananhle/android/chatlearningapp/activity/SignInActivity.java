package ttuananhle.android.chatlearningapp.activity;

import android.content.Intent;
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

import es.dmoral.toasty.Toasty;
import ttuananhle.android.chatlearningapp.R;

public class SignInActivity extends AppCompatActivity {
    private Button btnSignIn;
    private EditText edtSignInEmail;
    private EditText edtSignInPassword;
    private TextView txtToSignUp;


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
    }
}
