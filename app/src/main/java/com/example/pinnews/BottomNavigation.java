package com.example.pinnews;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigation extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    // Declaration
    private BottomNavigationView nav_JBottomNavigation;


    // Initialization
    private void Init()
    {
        nav_JBottomNavigation = findViewById(R.id.nav_BottomNavigation);
    }
    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);


        SharedPreferences sharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE);
        // Initialization
        Init();
        nav_JBottomNavigation.setOnNavigationItemSelectedListener(BottomNavigation.this);

        Intent intent = getIntent();
        String s = intent.getStringExtra("key_noti");
        final boolean b = sharedPreferences.getBoolean("is_Login", false);
        final boolean b2 = sharedPreferences.getBoolean("is_Category", false);


        if (s != null && s.equals("true") && b && b2) {
            loadFragment(new fragment_notification());
            nav_JBottomNavigation.setSelectedItemId(R.id.navigation_notification);
        }
        else if(!b && !b2)
        {
            Intent intent2 = new Intent(BottomNavigation.this, SignUp.class);
            startActivity(intent2);
            finish();
        }
        else if(b && !b2)
        {
            Intent intent3 = new Intent(BottomNavigation.this, Category.class);
            startActivity(intent3);
            finish();
        }
        else if(!b && b2)
        {
            Intent intent4 = new Intent(BottomNavigation.this, SignUp.class);
            startActivity(intent4);
            finish();
        }
        else
        {
            intent.putExtra("key_noti","false");
            loadFragment(new fragment_home());
        }
    }



    private boolean loadFragment(Fragment fragment)
    {
        if(fragment != null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer_BottomNavigation, fragment).commit();
            return true;
        }
        return false;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId())
        {
            case R.id.navigation_home:
                fragment = new fragment_home();
                break;
            case R.id.navigation_trending:
                fragment = new fragment_trending();
                break;
            case R.id.navigation_notification:
                fragment = new fragment_notification();
                break;
            case R.id.navigation_profile:
                fragment = new fragment_profile();
                break;
        }
        return loadFragment(fragment);
    }

   @Override
   public boolean onKeyDown(int keyCode, KeyEvent event) {
       if (keyCode == KeyEvent.KEYCODE_BACK) {
           Menu menu = nav_JBottomNavigation.getMenu();

           for (int i = 0; i < nav_JBottomNavigation.getMenu().size(); i++) {
               MenuItem menuItem = menu.getItem(i);

               if (menuItem.isChecked()) {
                   if (menuItem.getItemId()==2131296565)
                   {
                       finish();
                   }
               }
           }
       }
       return super.onKeyDown(keyCode, event);
   }
}
