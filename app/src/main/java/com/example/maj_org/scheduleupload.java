package com.example.maj_org;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Random;

public class scheduleupload extends AppCompatActivity {

    Button host, schedule;
    DatabaseReference ref, ref1;
    ImageView img1;
    String domain, duration, key, imagurl, name, org, date, eid, imagurl1, key1;
    Uri filepath;
    Bitmap bitmap;
    events event1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduleupload);


        event1 = new events();


        Intent intent = getIntent();
        eid = intent.getStringExtra("key");
        domain=intent.getStringExtra("domain");
        duration=intent.getStringExtra("duration");
        key=intent.getStringExtra("key");
        imagurl=intent.getStringExtra("imgurl");
        name=intent.getStringExtra("name");
        org=intent.getStringExtra("org");
        date=intent.getStringExtra("date");
        key1=intent.getStringExtra("key1");

        host = (Button) findViewById(R.id.host);
        img1 = (ImageView) findViewById(R.id.sch_img);
        schedule=(Button) findViewById(R.id.sch);


        ref = FirebaseDatabase.getInstance().getReference().child("users_org").child(key1.toString().trim()).child("events");
        ref1 = FirebaseDatabase.getInstance().getReference().child("events");



        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(scheduleupload.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                Intent i = new Intent(Intent.ACTION_PICK);
                                i.setType("image/");
                                startActivityForResult(Intent.createChooser(i, "Please select Schedule"), 1);

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                                token.continuePermissionRequest();

                            }
                        }).check();
            }
        });



        host.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadfirebase();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {



        if(requestCode==1 && resultCode==RESULT_OK){
            filepath=data.getData();

            try{
              InputStream is = getContentResolver().openInputStream(filepath);

            bitmap = BitmapFactory.decodeStream(is);
            img1.setImageBitmap(bitmap);
            }
            catch (Exception ex){

            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }



    private void uploadfirebase() {

        ProgressDialog d = new ProgressDialog(this);
        d.setTitle("Image Uploader");
        d.show();
        FirebaseStorage fs=FirebaseStorage.getInstance();
        StorageReference uploader =fs.getReference("Image1"+new Random().nextInt(50));

        uploader.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        d.dismiss();
                        uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imagurl1=uri.toString();
                                Toast.makeText(scheduleupload.this, "Url received ", Toast.LENGTH_LONG).show();


                                event1.setName(name);
                                event1.setOrg(org);
                                event1.setDate(date);
                                event1.setDomain(domain);
                                event1.setDuration(duration);
                                event1.setKey(key.toString().trim());
                                event1.setEid(key1.toString().trim());
                                event1.setImgurl(imagurl);
                                event1.setSchurl(imagurl1);
                                ref.child(key.toString().trim()).setValue(event1);
                                ref1.child(key.toString().trim()).setValue(event1);

                            }
                        });
                        Toast.makeText(scheduleupload.this, "File uploaded ", Toast.LENGTH_LONG).show();

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        float percent = (100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                        d.setMessage("Uploaded : "+(int)percent +"%");
                    }
                });

    }
}