package com.example.maj_org;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class notify extends AppCompatActivity {

    EditText title, msg;
    Button notify;
    String[] tokens;
    String eid, oid;
    DatabaseReference ref, ref1;
   ArrayList arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        Intent intent = getIntent();
        eid = intent.getStringExtra("eid");
        oid = intent.getStringExtra("oid");


        title=(EditText) findViewById(R.id.titlemsg);
        msg = (EditText) findViewById(R.id.msg);
        notify = (Button) findViewById(R.id.notifyp);
        arrayList = new ArrayList<String>();
        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //send();
                gettokens();
                for (int i = 0; i < arrayList.size(); i++) {
                    send((String) arrayList.get(i));

                }
            }

            //

        });

    }
    public void send(String value){
        System.out.println("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPp");

        if(!title.getText().toString().isEmpty() && !msg.getText().toString().isEmpty()){
            FcmNotificationsSender notificationsSender = new FcmNotificationsSender(value, title.getText().toString(), msg.getText().toString(),
                    getApplicationContext(), notify.this);
            notificationsSender.SendNotifications();
            System.out.println("Sent Notification+"+"*************************************");
        }
    }

    public void gettokens(){

        ref= FirebaseDatabase.getInstance().getReference().child("users_org").child(oid).child("events").child(eid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("registered_participants").getChildrenCount()>0){
                    System.out.println(snapshot.child("registered_participants").getChildrenCount()+"Participants exist-------------------------------------------------");
                    snapshot=snapshot.child("registered_participants");
                    Toast.makeText(notify.this, "Sending notification", Toast.LENGTH_LONG).show();
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        for (DataSnapshot ds1 : ds.getChildren()) {
                            System.out.println(ds1);
                            String temp = ds1.getValue().toString();
                            Toast.makeText(notify.this, "Got key", Toast.LENGTH_LONG).show();
                            System.out.println(temp + "************************************************************");
                            System.out.println(temp.length());
                            if(temp.length()>16) {
                                //System.out.println(temp1);
                                arrayList.add(temp);
                            }
                        }
                    }
                    System.out.println(arrayList);
                    System.out.println(title+"OOOOOOOOOOOOOOOOOOOOO"+msg);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}