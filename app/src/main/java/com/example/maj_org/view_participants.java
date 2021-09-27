package com.example.maj_org;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class view_participants extends AppCompatActivity {

    private ListView participants;

    String eid, oid;

    // creating a new array list.
    ArrayList<String> parArrayList;

    // creating a variable for database reference.
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_participants);

        Intent intent = getIntent();
        eid = intent.getStringExtra("eid");
        oid = intent.getStringExtra("oid");

        participants = (ListView) findViewById(R.id.view);

        parArrayList = new ArrayList<String>();
        initializeListView();
    }

    private void initializeListView() {

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, parArrayList);


        reference = FirebaseDatabase.getInstance().getReference().child("users_org").child(oid).child("events").child(eid);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("registered_participants")) {
                    for(DataSnapshot ds : snapshot.child("registered_participants").getChildren()){
                        //parArrayList.add(snapshot.child("registered_participants").getKey());
                        //adapter.notifyDataSetChanged();
                        System.out.println(ds.getKey());
                        String temp = ds.getKey().toString();
                        parArrayList.add(temp);
                        adapter.notifyDataSetChanged();
                    }

                    Toast.makeText(getApplicationContext(), "Inside If", Toast.LENGTH_LONG).show();
                    //parArrayList.add(snapshot.child("registered_participants").getKey());
                    //
                }
                else{
                    parArrayList.add("No participants have registered yet");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        participants.setAdapter(adapter);
    }
}