package ttuananhle.android.chatlearningapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;
import ttuananhle.android.chatlearningapp.R;

public class ChatActivity extends AppCompatActivity {

    ImageView btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        btnSend = (ImageView) this.findViewById(R.id.btn_chat_send);


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toasty.success(ChatActivity.this, "Send", Toast.LENGTH_LONG).show();
            }
        });


    }
}
