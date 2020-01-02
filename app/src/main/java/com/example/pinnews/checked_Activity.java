package com.example.pinnews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.github.ybq.android.spinkit.style.FadingCircle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class checked_Activity extends AppCompatActivity {

    private final ArrayList<String> subcategories = new ArrayList<>();
    private final ArrayList<String> subcategories_date = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checked_);

        // Progress Bar
        ProgressBar progressBar = findViewById(R.id.spin_kit_checked);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);
        progressBar.setVisibility(View.VISIBLE);

        SharedPreferences sharedPreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        String[] sports_Subcategories = new String[]{"Cricket", "Hockey", "Football", "Kabaddi", "Badminton"};
        String[] date_filter = new String[]{"2019-07-18", "2019-07-17", "2019-07-16"};

        for (int i = 0; i < MyCategoriesExpandableListAdapter.parentItems.size(); i++ ) {
            /*String isChecked = MyCategoriesExpandableListAdapter.parentItems.get(i).get(ConstantManager.Parameter.IS_CHECKED);
            if (isChecked.equalsIgnoreCase(ConstantManager.CHECK_BOX_CHECKED_TRUE)) {
                //tvParent.setText(tvParent.getText() + MyCategoriesExpandableListAdapter.parentItems.get(i).get(ConstantManager.Parameter.CATEGORY_NAME));
            }*/
            for (int j = 0; j < MyCategoriesExpandableListAdapter.childItems.get(i).size(); j++) {

                String isChildChecked = MyCategoriesExpandableListAdapter.childItems.get(i).get(j).get(ConstantManager.Parameter.IS_CHECKED);

                assert isChildChecked != null;
                if (isChildChecked.equalsIgnoreCase(ConstantManager.CHECK_BOX_CHECKED_TRUE)) {
                    if (i == 0) {
                        subcategories.add(sports_Subcategories[j]);
                    } else {
                        subcategories_date.add(date_filter[j]);
                    }
                }
            }
        }

        Intent intent = new Intent(checked_Activity.this,BottomNavigation.class);
        //Set the values
        Set<String> set = new HashSet<>(subcategories);
        Set<String> set_date = new HashSet<>(subcategories_date);
        editor.putStringSet("subcategory_list", set);
        editor.putStringSet("subcategory_list_date", set_date);
        editor.putBoolean("is_Subcategory",true);
        editor.apply();
        progressBar.setVisibility(View.INVISIBLE);
        finish();
        startActivity(intent);

    }
}
