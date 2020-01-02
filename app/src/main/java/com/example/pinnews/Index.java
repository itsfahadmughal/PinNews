package com.example.pinnews;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

@SuppressWarnings("ConstantConditions")
public class Index extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);


        SharedPreferences sharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE);

        final boolean b = sharedPreferences.getBoolean("is_Login", false);
        final boolean b2 = sharedPreferences.getBoolean("is_Category", false);


        // Time Thread
        // Declaration
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if(!b && !b2)
            {
                Intent intent = new Intent(Index.this, SignUp.class);
                startActivity(intent);
                finish();
            }
            else if(b && !b2)
            {
                Intent intent = new Intent(Index.this, Category.class);
                startActivity(intent);
                finish();
            }
            else if(b && b2)
            {
                Intent intent = new Intent(Index.this, BottomNavigation.class);
                startActivity(intent);
                finish();
            }else if(!b)
            {
                Intent intent = new Intent(Index.this, SignUp.class);
                startActivity(intent);
                finish();
            }
        }, 500);
    }
}
