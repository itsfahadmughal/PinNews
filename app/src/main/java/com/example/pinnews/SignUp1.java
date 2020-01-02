package com.example.pinnews;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class SignUp1 extends AppCompatActivity {

    // Declaration
    private ImageButton back_JSignup1;
    private Button upload_JSignup1;
    private Button next_JSignup1;
    private TextView tv2_JSignup1;
    private final int request_codeGallery = 999;
    private Bitmap bitmap;
    private ImageView dp_JSignup1;
    private Intent next_Intent;

    // Initialization
    private void Init() {
        back_JSignup1 = findViewById(R.id.back_Signup1);
        upload_JSignup1 = findViewById(R.id.upload_Signup1);
        tv2_JSignup1 = findViewById(R.id.tv2_Signup1);
        dp_JSignup1 = findViewById(R.id.dp_Signup1);
        next_JSignup1 = findViewById(R.id.next_Signup1);
        bitmap = null;
        next_Intent = new Intent(SignUp1.this, SignUp2.class);

        // other values,
        Intent create_Intent = getIntent();
        next_Intent.putExtra("name", create_Intent.getStringExtra("name"));
        next_Intent.putExtra("newem", create_Intent.getStringExtra("newem"));
        next_Intent.putExtra("password", create_Intent.getStringExtra("password"));
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up1);

        // Initialization
        Init();

        // Skip Button > underline
        tv2_JSignup1.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        // Back Button Listener
        back_JSignup1.setOnClickListener(view -> {
            Intent back_Intent = new Intent(SignUp1.this, SignUp.class);
            startActivity(back_Intent);
            finish();
        }); // Back Listener

        upload_JSignup1.setOnClickListener(view -> {
            ActivityCompat.requestPermissions(SignUp1.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},request_codeGallery);
            // # functions (imageViewToByte) + (onRequestPermissionsResult) + (onActivityResult).
        }); // Upload Listener

        next_JSignup1.setOnClickListener(view -> {
            if(bitmap == null)
            {
                Toast.makeText(SignUp1.this, "No Image", Toast.LENGTH_SHORT).show();
            }
            else
            {
                dp_JSignup1.buildDrawingCache();
                Bitmap bitmap_Scaled = scaleDownBitmap(bitmap, SignUp1.this);
                String imageData = imageViewToByte(bitmap_Scaled);
                next_Intent.putExtra("dp_image", imageData);
                startActivity(next_Intent);
                finish();
            }
        }); // Next Listener



    }// On Create

    // Skip Button

    public void skip(View view) {
        Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(),  R.drawable.profile);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 100, bao);
        byte [] ba = bao.toByteArray();
        String dp_encoded = Base64.encodeToString(ba,Base64.DEFAULT);


        next_Intent.putExtra("dp_image", dp_encoded);
        startActivity(next_Intent);
        finish();
    }

    // Scaling
    private static Bitmap scaleDownBitmap(Bitmap photo, Context context) {
        final float densityMultiplier = context.getResources().getDisplayMetrics().density;
        int h= (int) (150 *densityMultiplier);
        int w= (int) (h * photo.getWidth()/((double) photo.getHeight()));
        photo=Bitmap.createScaledBitmap(photo, w, h, true);
        return photo;
    }

    // Upload Image # 1
    private String imageViewToByte(Bitmap imageBitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    // Upload Image # 2
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == request_codeGallery)
        {
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,request_codeGallery);
            }
            else
            {
                Toast.makeText(SignUp1.this, "You don't have permission to excess file location", Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // Upload Image # 3
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == request_codeGallery && resultCode == RESULT_OK && data != null)
        {
            Uri uri = data.getData();
            try {
                assert uri != null;
                InputStream inputStream = getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(inputStream);
                dp_JSignup1.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return;
        }
        else
        {
            Toast.makeText(SignUp1.this, "Couldn't Upload Image!", Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent back_JSignup1 = new Intent(SignUp1.this, SignUp.class);
        startActivity(back_JSignup1);
        finish();
    }
} // Class Closing
