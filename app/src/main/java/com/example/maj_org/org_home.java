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
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

import org.joda.time.Days;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class org_home extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    events event1;
    //DatabaseReference ref, ref1;
    EditText name, date, org;
    Button host, upload, schedule;
    String domain, duration, key, imagurl, imagurl1, key1;
    ImageView img, img1;
    Uri filepath, filepath1, filepath2;
    Bitmap bitmap, bitmap1;
    CountDownTimer countDownTimer2;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date1;
    Date date11;
    Date date2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_home);

        Intent intent = getIntent();
        key1 = intent.getStringExtra("key");


        calendar = Calendar.getInstance();

        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        date1 = dateFormat.format(calendar.getTime());

        System.out.println(date1+"&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");

        //event1 = new events();
        //ref = FirebaseDatabase.getInstance().getReference().child("users_org").child(key1.toString().trim()).child("events");
        //ref1 = FirebaseDatabase.getInstance().getReference().child("events");

        name = (EditText) findViewById(R.id.eventname);
        date = (EditText) findViewById(R.id.eventdate);
        org = (EditText) findViewById(R.id.org);
        host = (Button) findViewById(R.id.host);
        img = (ImageView) findViewById(R.id.bro_img);
        upload = (Button) findViewById(R.id.uploadimg);
        img1 = (ImageView) findViewById(R.id.sch_img);
        schedule=(Button) findViewById(R.id.sch);

        //name.setText(key1.toString().trim());



        Spinner spinner = (Spinner) findViewById(R.id.domain_spinner);
        spinner.setOnItemSelectedListener(this);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.domain_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        Spinner spinner1 = (Spinner) findViewById(R.id.duration_spinner);
        spinner1.setOnItemSelectedListener(this);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.duration_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner1.setAdapter(adapter1);

        domain = spinner.getSelectedItem().toString();
        duration=spinner1.getSelectedItem().toString();

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dexter.withActivity(org_home.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                Intent i = new Intent(Intent.ACTION_PICK);
                                i.setType("image/");
                                startActivityForResult(Intent.createChooser(i, "Please select Brochure"), 1);

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
                key=generateRandKey();
                uploadfirebase();





                countDownTimer2 = new CountDownTimer(10000, 10000) {
                    @Override

                    public void onTick(long millisUntilFinished)
                    {

                    }

                    @Override
                    public void onFinish() {

                        try {
                            date11 = dateFormat.parse(date.getText().toString());
                            date2 = dateFormat.parse(date1.toString());
                            System.out.println(date2);
                            long difference = Math.abs(date11.getTime() - date2.getTime());
                            long differenceDates = difference / (24 * 60 * 60 * 1000);
                            String dayDifference = Long.toString(differenceDates);
                            //textView.setText("The difference between two dates is " + dayDifference + " days");
                            System.out.println(dayDifference);
                            int value = Integer.parseInt(dayDifference);
                            if (value > 14) {
                                date.setError("You can host before 2 weeks of your event");
                                return;
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        countDownTimer2.cancel();




                        Intent intent = new Intent(getApplicationContext(), scheduleupload.class);
                        intent.putExtra("key", key.toString().trim());
                        intent.putExtra("name", name.getText().toString().trim());
                        intent.putExtra("org", org.getText().toString().trim());
                        intent.putExtra("date", date.getText().toString().trim());
                        intent.putExtra("domain", domain);
                        intent.putExtra("duration", duration);
                        intent.putExtra("key1", key1.toString().trim());
                        intent.putExtra("imgurl", imagurl);



                        Toast.makeText(org_home.this, "Data sent to next activity ", Toast.LENGTH_LONG).show();

                        startActivity(intent);


                    }
                }.start();




            }
        });

    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

        String selectedItemText = (String) adapterView.getItemAtPosition(position);
        Toast.makeText(org_home.this, "Select is " +selectedItemText, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private String generateRandKey() {

        String SALTCHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 16) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        Toast.makeText(org_home.this, "key generated is "+saltStr, Toast.LENGTH_LONG).show();
        return saltStr;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {



        if(requestCode==1 && resultCode==RESULT_OK){
            filepath=data.getData();
            try {
                imgupload(filepath, img);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //try{
            //  InputStream is = getContentResolver().openInputStream(filepath);

            //bitmap = BitmapFactory.decodeStream(is);
            //img.setImageBitmap(bitmap);
            //}
            //catch (Exception ex){

            //}
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void imgupload(Uri path, ImageView imgage) throws FileNotFoundException {
        //path = data.getData()

        InputStream is = getContentResolver().openInputStream(path);

        bitmap = BitmapFactory.decodeStream(is);
        imgage.setImageBitmap(bitmap);
    }


    private void insert(Uri path){

        //FirebaseStorage fs=FirebaseStorage.getInstance();
        //StorageReference uploader =fs.getReference("Image1"+new Random().nextInt(50));


    }

    private String imageupload( Uri filepath){
        //String imgurl;
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
                                imagurl = uri.toString();

                            }
                        });
                        Toast.makeText(org_home.this, "File uploaded ", Toast.LENGTH_LONG).show();

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        float percent = (100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                        d.setMessage("Uploaded : "+(int)percent +"%");
                    }
                });
        return imagurl;

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
                                imagurl=uri.toString();
                                Toast.makeText(org_home.this, "Url received ", Toast.LENGTH_LONG).show();
                            }
                        });
                        Toast.makeText(org_home.this, "File uploaded ", Toast.LENGTH_LONG).show();

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