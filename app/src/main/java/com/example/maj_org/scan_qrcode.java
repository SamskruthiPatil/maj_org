package com.example.maj_org;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class scan_qrcode extends AppCompatActivity implements View.OnClickListener {

    Button scanBtn, verifyBtn;
    TextView messageText, messageFormat;

    String eid, oid, pid, temp, sam="registered_participants";
    DatabaseReference ref, ref1;
    CountDownTimer countDownTimer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode);


        Intent intent = getIntent();
        eid = intent.getStringExtra("eid");
        oid = intent.getStringExtra("oid");

        scanBtn = findViewById(R.id.scanBtn);
        messageText = findViewById(R.id.textContent);
        messageFormat = findViewById(R.id.textFormat);
        verifyBtn = (Button) findViewById(R.id.verifyBtn);


        scanBtn.setOnClickListener(this);

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp = messageText.getText().toString().trim();
                ref=FirebaseDatabase.getInstance().getReference().child("users_org").child(oid).child("events").child(eid);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.child("registered_participants").hasChild(temp)){
                            Toast.makeText(scan_qrcode.this, "Valid Participant", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(scan_qrcode.this, "InValid", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }

    @Override
    public void onClick(View v) {

        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Scan QR Code");
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.initiateScan();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                // if the intentResult is not null we'll set
                // the content and format of scan message
                messageText.setText(intentResult.getContents());
                pid = intentResult.getContents().toString().trim();
                messageFormat.setText(intentResult.getFormatName());

                sam = messageText.getText().toString().trim();
                Toast.makeText(scan_qrcode.this, "Text Obtained", Toast.LENGTH_LONG).show();
            }
        }
    }
}