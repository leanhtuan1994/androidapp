package ttuananhle.android.chatlearningapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import ttuananhle.android.chatlearningapp.R;

public class CreateCodeActivity extends AppCompatActivity {

    private EditText edtCode;
    private Button btnCreateCode;

    private FirebaseAuth fireAuth;
    private FirebaseUser fireUser;
    private FirebaseDatabase fireData;
    private DatabaseReference dataRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_code);

        mappingView();
        initFirebase();
        listenerCreateCodeOnClick();

    }

    private void mappingView(){
        edtCode = (EditText) this.findViewById(R.id.edtCode);
        btnCreateCode = (Button) this.findViewById(R.id.btnCreateCode);
    }

    private void initFirebase(){
        fireAuth = FirebaseAuth.getInstance();
        fireUser = fireAuth.getCurrentUser();
        fireData = FirebaseDatabase.getInstance();
        dataRef = fireData.getReference();
    }

    private void listenerCreateCodeOnClick(){
        btnCreateCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String code = edtCode.getText().toString();
                    if (code.equals("")) return;
                    dataRef.child("Users").child(fireUser.getUid()).child("code").setValue(code);

                    // Add Code in firedata
                    dataRef.child("Code").child(code).setValue(fireUser.getUid());

                    // Add user in code data
                    dataRef.child("Code").child(code).child(fireUser.getUid()).setValue(1);

                    AlertDialog alertDialog = new AlertDialog.Builder(CreateCodeActivity.this).create();
                    alertDialog.setTitle("Code");
                    alertDialog.setMessage("Create code successful!");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startActivity( new Intent(CreateCodeActivity.this, MainActivity.class));
                        }
                    });
                    alertDialog.show();

                } catch (NumberFormatException e){
                    Toasty.info(CreateCodeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    edtCode.setText("");
                }
            }
        });
    }
}
