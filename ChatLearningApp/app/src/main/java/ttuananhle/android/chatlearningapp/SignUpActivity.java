package ttuananhle.android.chatlearningapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity {

    private TextView txtToSignIn;
    private EditText edtSignUpEmail;
    private EditText edtSignUpPassword;
    private EditText edtSignUpName;
    private Button btnSignUp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mappingView();
        listenerTxtToSignInOnClick();
        listenerButtonSignUpOnClick();
    }


    private void listenerButtonSignUpOnClick(){
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Click", "btnSignUp Clicked");
            }
        });
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
