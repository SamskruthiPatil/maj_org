package com.example.maj_org;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class sendupdates extends AppCompatActivity {


    EditText  msg;
    Button notify;

    String eid, oid;
    DatabaseReference ref, ref1;
    updates update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendupdates);


        Intent intent = getIntent();
        eid = intent.getStringExtra("eid");
        oid = intent.getStringExtra("oid");

        msg = (EditText) findViewById(R.id.msg);
        notify = (Button) findViewById(R.id.notifyp);

        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref = FirebaseDatabase.getInstance().getReference().child("users_org").child(oid.toString().trim()).child("events").child(eid).child("updates");
                ref1 = FirebaseDatabase.getInstance().getReference().child("events").child(eid).child("updates");

                update = new updates();

                update.setMessage(msg.getText().toString().trim());

                ref.child("msg").setValue(update);
                ref1.child("msg").setValue(update);


            }
        });
    }
}