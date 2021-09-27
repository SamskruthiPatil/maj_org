package com.example.maj_org;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;

import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;


public class myadapter extends FirebaseRecyclerAdapter<events, myadapter.myviewholder>
{

    String temp1, temp2;
    Bitmap bitmap;
    public myadapter(@NonNull FirebaseRecyclerOptions<events> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull events events) {

        //temp2=events.getEid().toString();



            holder.eid.setText(events.getKey());
            holder.ename.setText(events.getName());
            holder.orgname.setText(events.getOrg());
            holder.datetext.setText(events.getDate());
            Glide.with(holder.img.getContext()).load(events.getImgurl()).into(holder.img);
            //holder.img1.setImageBitmap(bitmap);
            temp1 = events.getKey().toString().trim();


    }


    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        CircleImageView img;
        ImageView img1;
        TextView eid, ename, orgname, datetext;
        Button vierpar, scan, notify;
        DatabaseReference ref;
        registered_participants rp;
        String eid1, pid;
        String key;

        public myviewholder(@NonNull View itemView)
        {

            super(itemView);

            Intent intent = ((Activity)itemView.getContext()).getIntent();
            key = intent.getStringExtra("key");
            System.out.println(key+"*****************************");
            //temp1=key;


                img = (CircleImageView) itemView.findViewById(R.id.img1);
                eid = (TextView) itemView.findViewById(R.id.eid);
                ename = (TextView) itemView.findViewById(R.id.ename);
                orgname = (TextView) itemView.findViewById(R.id.orgname);
                datetext = (TextView) itemView.findViewById(R.id.datetext);
                vierpar = (Button) itemView.findViewById(R.id.viewpar);

                notify = (Button) itemView.findViewById(R.id.edit);
                scan = (Button) itemView.findViewById(R.id.delete_event);
            System.out.println(key+"*****************************"+eid.getText().toString());


            scan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent((Activity)itemView.getContext(),scan_qrcode.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("eid",eid.getText().toString().trim());
                    bundle.putString("oid", key);
                    intent.putExtras(bundle);
                    ((Activity)itemView.getContext()).startActivity(intent);
                }
            });


            notify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent((Activity)itemView.getContext(),sendupdates.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("eid",eid.getText().toString().trim());
                    bundle.putString("oid", key);
                    intent.putExtras(bundle);
                    ((Activity)itemView.getContext()).startActivity(intent);
                }
            });


            vierpar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent((Activity)itemView.getContext(),view_participants.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("eid",eid.getText().toString().trim());
                    bundle.putString("oid", key);
                    intent.putExtras(bundle);
                    ((Activity)itemView.getContext()).startActivity(intent);

                }
            });


        }
    }
}
