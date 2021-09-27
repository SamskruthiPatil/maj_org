package com.example.maj_org;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class view_myevents extends AppCompatActivity {

    RecyclerView recview;
    myadapter adapter;
    String key1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_myevents);


        Intent intent = getIntent();
        key1 = intent.getStringExtra("key");


        recview=(RecyclerView)findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<events> options =
                new FirebaseRecyclerOptions.Builder<events>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("users_org").child(key1.toString().trim()).child("events"), events.class)
                        .build();

        adapter= new myadapter(options);

        recview.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}