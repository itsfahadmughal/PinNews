package com.example.pinnews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class Category extends AppCompatActivity {

    // Declaration
    private GridLayout grid_JCategory;
    private Button next_JCategory;
    private final ArrayList<String> arr_Category = new ArrayList<>();
    private ImageView image1_JCategory;
    private ImageView image2_JCategory;
    private ImageView image3_JCategory;
    private ImageView image4_JCategory;
    private ImageView image5_JCategory;
    private ImageView image6_JCategory;
    private ImageView image7_JCategory;
    private ImageView image8_JCategory;
    private ImageView image9_JCategory;
    private ImageView image10_JCategory;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ProgressBar progressBar;

    private void Init() {
        grid_JCategory = findViewById(R.id.grid_Category);
        next_JCategory = findViewById(R.id.next_Category);
        image1_JCategory = findViewById(R.id.image1_Category);
        image2_JCategory = findViewById(R.id.image2_Category);
        image3_JCategory = findViewById(R.id.image3_Category);
        image4_JCategory = findViewById(R.id.image4_Category);
        image5_JCategory = findViewById(R.id.image5_Category);
        image6_JCategory = findViewById(R.id.image6_Category);
        image7_JCategory = findViewById(R.id.image7_Category);
        image8_JCategory = findViewById(R.id.image8_Category);
        image9_JCategory = findViewById(R.id.image9_Category);
        image10_JCategory = findViewById(R.id.image10_Category);

        // Progress Bar
        progressBar = findViewById(R.id.spin_kit_Category);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);
        progressBar.setVisibility(View.GONE);


        sharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // Initialization
        Init();

        // Set Event
        setMultipleEvent();

        // Next Button
        next_JCategory.setOnClickListener(view -> {
            if(arr_Category.size()>=2)
            {
                progressBar.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                // Extracting array.
                String[] arr_temp = new String[arr_Category.size()];
                arr_temp = arr_Category.toArray(arr_temp);

                // Storing value in variable
                String category_nationalNation = "no",
                        category_opinionNation = "no",
                        category_businessNation = "no",
                        category_internationalNation = "no",
                        category_metropolitan_lahoreNation = "no",
                        category_metropolitan_islamabadNation = "no",
                        category_metropolitan_karachiNation = "no",
                        category_sportsNation = "no",
                        category_lifestyleNation = "no",
                        category_snippetNation = "no";

                for (String s : arr_temp) {
                    switch (s) {
                        case "category_nationalNation":
                            category_nationalNation = "category_nationalNation";
                            break;
                        case "category_opinionNation":
                            category_opinionNation = "category_opinionNation";
                            break;
                        case "category_businessNation":
                            category_businessNation = "category_businessNation";
                            break;
                        case "category_internationalNation":
                            category_internationalNation = "category_internationalNation";
                            break;
                        case "category_metropolitan_lahoreNation":
                            category_metropolitan_lahoreNation = "category_metropolitan_lahoreNation";
                            break;
                        case "category_metropolitan_islamabadNation":
                            category_metropolitan_islamabadNation = "category_metropolitan_islamabadNation";
                            break;
                        case "category_metropolitan_karachiNation":
                            category_metropolitan_karachiNation = "category_metropolitan_karachiNation";
                            break;
                        case "category_sportsNation":
                            category_sportsNation = "category_sportsNation";
                            break;
                        case "category_lifestyleNation":
                            category_lifestyleNation = "category_lifestyleNation";
                            break;
                        case "category_snippetNation":
                            category_snippetNation = "category_snippetNation";
                            break;
                    }
                } // Loop

                final Intent next_Intent = new Intent(Category.this, BottomNavigation.class);
                editor.putBoolean("is_Category", true);
                final String email_JCategory = sharedPreferences.getString("runningEmail", "");
                editor.apply();

                // Uploading
                final String finalCategory_nationalNation = category_nationalNation;
                final String finalCategory_opinionNation = category_opinionNation;
                final String finalCategory_businessNation = category_businessNation;
                final String finalCategory_internationalNation = category_internationalNation;
                final String finalCategory_metropolitan_lahoreNation = category_metropolitan_lahoreNation;
                final String finalCategory_metropolitan_islamabadNation = category_metropolitan_islamabadNation;
                final String finalCategory_metropolitan_karachiNation = category_metropolitan_karachiNation;
                final String finalCategory_sportsNation = category_sportsNation;
                final String finalCategory_lifestyleNation = category_lifestyleNation;
                final String finalCategory_snippetNation = category_snippetNation;
                StringRequest stringRequest_JCategory = new StringRequest(Request.Method.POST, "https://pinnews.000webhostapp.com/category.php", response -> {
                    response = response.trim();
                    progressBar.setVisibility(View.INVISIBLE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    response = response.trim();
                    switch (response)
                    {
                        case "Failed to connecting database!":
                            error_alert("J03P01E1M01");
                            break;
                        case "inserted":
                            //Set the values
                            Set<String> set = new HashSet<>(arr_Category);
                            editor.putStringSet("category_list", set);
                            editor.apply();
                            // Moving
                            startActivity(next_Intent);
                            finish();
                            break;
                        default:
                            error_alert("J03P01E1M: " + response);
                            break;
                    }
                }, error -> {
                    progressBar.setVisibility(View.INVISIBLE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    error_alert("J03P01E2M: " + error.getMessage());

                })
                {
                    @Override
                    protected Map<String, String> getParams() {
                        HashMap<String, String> hashMap_JCategory = new HashMap<>();
                        hashMap_JCategory.put("email", email_JCategory);
                        hashMap_JCategory.put("national", finalCategory_nationalNation);
                        hashMap_JCategory.put("opinion", finalCategory_opinionNation);
                        hashMap_JCategory.put("business", finalCategory_businessNation);
                        hashMap_JCategory.put("international", finalCategory_internationalNation);
                        hashMap_JCategory.put("lahore", finalCategory_metropolitan_lahoreNation);
                        hashMap_JCategory.put("islamabad", finalCategory_metropolitan_islamabadNation);
                        hashMap_JCategory.put("karachi", finalCategory_metropolitan_karachiNation);
                        hashMap_JCategory.put("sports", finalCategory_sportsNation);
                        hashMap_JCategory.put("lifestyle", finalCategory_lifestyleNation);
                        hashMap_JCategory.put("snippet", finalCategory_snippetNation);
                        return hashMap_JCategory;

                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(Category.this);
                requestQueue.add(stringRequest_JCategory);

            }
            else
            {
                Snackbar.make(view,"Minimum 2 Categories required", Snackbar.LENGTH_LONG).setAction("Action",null).show();

            }
        });

    } // On Create

    private void setMultipleEvent() {
        // Loop all child item of the grid
        for(int i = 0; i < grid_JCategory.getChildCount(); i++)
        {
            final CardView cardView = (CardView) grid_JCategory.getChildAt(i);
            final boolean[] b = {false};
            final int finalI = i;
            cardView.setOnClickListener(view -> {
                if (cardView.getCardBackgroundColor().getDefaultColor() == -1 && !b[0])
                {
                    switch (finalI)
                    {
                        case 0:
                            image1_JCategory.setColorFilter(Color.rgb(169, 169, 169), android.graphics.PorterDuff.Mode.MULTIPLY);
                            b[0] = true;
                            arr_Category.add("category_nationalNation");
                            break;
                        case 1:
                            image2_JCategory.setColorFilter(Color.rgb(169, 169, 169), android.graphics.PorterDuff.Mode.MULTIPLY);
                            b[0] = true;
                            arr_Category.add("category_opinionNation");
                            break;
                        case 2:
                            image3_JCategory.setColorFilter(Color.rgb(169, 169, 169), android.graphics.PorterDuff.Mode.MULTIPLY);
                            b[0] = true;
                            arr_Category.add("category_businessNation");
                            break;
                        case 3:
                            image4_JCategory.setColorFilter(Color.rgb(169, 169, 169), android.graphics.PorterDuff.Mode.MULTIPLY);
                            b[0] = true;
                            arr_Category.add("category_internationalNation");
                            break;
                        case 4:
                            image5_JCategory.setColorFilter(Color.rgb(169, 169, 169), android.graphics.PorterDuff.Mode.MULTIPLY);
                            b[0] = true;
                            arr_Category.add("category_metropolitan_lahoreNation");
                            break;
                        case 5:
                            image6_JCategory.setColorFilter(Color.rgb(169, 169, 169), android.graphics.PorterDuff.Mode.MULTIPLY);
                            b[0] = true;
                            arr_Category.add("category_metropolitan_islamabadNation");
                            break;
                        case 6:
                            image7_JCategory.setColorFilter(Color.rgb(169, 169, 169), android.graphics.PorterDuff.Mode.MULTIPLY);
                            b[0] = true;
                            arr_Category.add("category_metropolitan_karachiNation");
                            break;
                        case 7:
                            image8_JCategory.setColorFilter(Color.rgb(169, 169, 169), android.graphics.PorterDuff.Mode.MULTIPLY);
                            b[0] = true;
                            arr_Category.add("category_sportsNation");
                            break;
                        case 8:
                            image9_JCategory.setColorFilter(Color.rgb(169, 169, 169), android.graphics.PorterDuff.Mode.MULTIPLY);
                            b[0] = true;
                            arr_Category.add("category_lifestyleNation");
                            break;
                        case 9:
                            image10_JCategory.setColorFilter(Color.rgb(169, 169, 169), android.graphics.PorterDuff.Mode.MULTIPLY);
                            b[0] = true;
                            arr_Category.add("category_snippetNation");
                            break;
                        /*case 10:
                            image11_JCategory.setColorFilter(Color.rgb(169, 169, 169), android.graphics.PorterDuff.Mode.MULTIPLY);
                            b[0] = true;
                            arr_Category.add("entertainment");
                            break;
                        case 11:
                            image12_JCategory.setColorFilter(Color.rgb(169, 169, 169), android.graphics.PorterDuff.Mode.MULTIPLY);
                            b[0] = true;
                            arr_Category.add("snippet");
                            break;*/
                        default:
                            Toast.makeText(Category.this, "Click Any Category", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
                else
                {
                    switch (finalI)
                    {
                        case 0:
                            b[0] = false;
                            image1_JCategory.clearColorFilter();
                            arr_Category.remove("category_nationalNation");
                            break;
                        case 1:
                            b[0] = false;
                            image2_JCategory.clearColorFilter();
                            arr_Category.remove("category_opinionNation");
                            break;
                        case 2:
                            b[0] = false;
                            image3_JCategory.clearColorFilter();
                            arr_Category.remove("category_businessNation");
                            break;
                        case 3:
                            b[0] = false;
                            image4_JCategory.clearColorFilter();
                            arr_Category.remove("category_internationalNation");
                            break;
                        case 4:
                            b[0] = false;
                            image5_JCategory.clearColorFilter();
                            arr_Category.remove("category_metropolitan_lahoreNation");
                            break;
                        case 5:
                            b[0] = false;
                            image6_JCategory.clearColorFilter();
                            arr_Category.remove("category_metropolitan_islamabadNation");
                            break;
                        case 6:
                            b[0] = false;
                            image7_JCategory.clearColorFilter();
                            arr_Category.remove("category_metropolitan_karachiNation");
                            break;
                        case 7:
                            b[0] = false;
                            image8_JCategory.clearColorFilter();
                            arr_Category.remove("category_sportsNation");
                            break;
                        case 8:
                            b[0] = false;
                            image9_JCategory.clearColorFilter();
                            arr_Category.remove("category_lifestyleNation");
                            break;
                        case 9:
                            b[0] = false;
                            image10_JCategory.clearColorFilter();
                            arr_Category.remove("category_snippetNation");
                            break;
                        /*case 10:
                            b[0] = false;
                            image11_JCategory.clearColorFilter();
                            arr_Category.remove("entertainment");
                            break;
                        case 11:
                            b[0] = false;
                            image12_JCategory.clearColorFilter();
                            arr_Category.remove("snippet");
                            break;*/
                        default:
                            Toast.makeText(Category.this, "Click Any Category", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
        }
    }


    private void error_alert(String code) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            AlertDialog.Builder alert = new AlertDialog.Builder(Category.this);
            alert.setTitle("Error Found!");
            alert.setMessage("Error code: " + code + ". Report this error?");
            alert.setPositiveButton("Report", (dialog, which) -> {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"pinnews.pk@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Error Found ('" + code + " ')!!!");
                intent.setPackage("com.google.android.gm");
                startActivity(intent);
                startActivity(getIntent());
                finish();
            });
            alert.setNegativeButton("Cancel", (dialog, which) -> {
                startActivity(getIntent());
                finish();
            });
            alert.show();
        } else {
            Toast.makeText(Category.this, "Check Internet Connectivity!!!", Toast.LENGTH_SHORT).show();
        }
    }

}// Class
