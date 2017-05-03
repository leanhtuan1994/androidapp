package ttuananhle.android.chatlearningapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignInActivity extends AppCompatActivity {
    private Button btnSignIn;
    private EditText edtSignInEmail;
    private EditText edtSignInPassword;
    private TextView txtToSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mappingView();
        listenerTextViewClick();
        listenerButtonSignInOnClick();


    }

    private void listenerButtonSignInOnClick(){
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Click", "btnSignIn Clicked");
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
