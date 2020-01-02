package com.example.pinnews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import java.util.Objects;
import java.util.Set;

class edit_category extends Fragment{



    // Declaration
    private ImageButton back_JEdit_Category;
    private Button done_JEdit_Category;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean[] b = {false,false,false,false,false,false,false,false,false,false};
    private GridLayout grid_JEdit_Category;
    private ArrayList<String> arr_Category = new ArrayList<>();
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
    private ProgressBar progressBar;
    private View view_JEdit_Category;

    private void Init()
    {
        back_JEdit_Category = view_JEdit_Category.findViewById(R.id.back_Edit_Category);
        done_JEdit_Category = view_JEdit_Category.findViewById(R.id.done_Edit_Category);

        sharedPreferences = view_JEdit_Category.getContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        grid_JEdit_Category = view_JEdit_Category.findViewById(R.id.grid_Edit_Category);
        image1_JCategory = view_JEdit_Category.findViewById(R.id.image1_Category);
        image2_JCategory = view_JEdit_Category.findViewById(R.id.image2_Category);
        image3_JCategory = view_JEdit_Category.findViewById(R.id.image3_Category);
        image4_JCategory = view_JEdit_Category.findViewById(R.id.image4_Category);
        image5_JCategory = view_JEdit_Category.findViewById(R.id.image5_Category);
        image6_JCategory = view_JEdit_Category.findViewById(R.id.image6_Category);
        image7_JCategory = view_JEdit_Category.findViewById(R.id.image7_Category);
        image8_JCategory = view_JEdit_Category.findViewById(R.id.image8_Category);
        image9_JCategory = view_JEdit_Category.findViewById(R.id.image9_Category);
        image10_JCategory = view_JEdit_Category.findViewById(R.id.image10_Category);

        progressBar = view_JEdit_Category.findViewById(R.id.spin_kit_edit_category);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);
        progressBar.setVisibility(View.GONE);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view_JEdit_Category = inflater.inflate(R.layout.edit_category, container, false);

        // Initialization
        Init();

        Set<String> set = sharedPreferences.getStringSet("category_list", null);

        // Set Event
        setMultipleEvent(view_JEdit_Category);
        assert set != null;
        ArrayList<String> category_arr = new ArrayList<>(set);
        for (int i=0;i<category_arr.size();i++)
        {
            if (category_arr.get(i).equals(""))
            {
                Toast.makeText(getContext(), "No Category Selected", Toast.LENGTH_SHORT).show();
            }
            else
            {
                if (category_arr.get(i).equals("category_nationalNation"))
                {
                    image1_JCategory.setColorFilter(Color.rgb(169, 169, 169), android.graphics.PorterDuff.Mode.MULTIPLY);
                    arr_Category.add("category_nationalNation");
                    b[0]=true;
                }
                if (category_arr.get(i).equals("category_opinionNation"))
                {
                    image2_JCategory.setColorFilter(Color.rgb(169, 169, 169), android.graphics.PorterDuff.Mode.MULTIPLY);
                    arr_Category.add("category_opinionNation");
                    b[1]=true;
                }
                if (category_arr.get(i).equals("category_businessNation"))
                {
                    image3_JCategory.setColorFilter(Color.rgb(169, 169, 169), android.graphics.PorterDuff.Mode.MULTIPLY);
                    arr_Category.add("category_businessNation");
                    b[2]=true;
                }
                if (category_arr.get(i).equals("category_internationalNation"))
                {
                    image4_JCategory.setColorFilter(Color.rgb(169, 169, 169), android.graphics.PorterDuff.Mode.MULTIPLY);
                    arr_Category.add("category_internationalNation");
                    b[3]=true;
                }
                if (category_arr.get(i).equals("category_metropolitan_lahoreNation"))
                {
                    image5_JCategory.setColorFilter(Color.rgb(169, 169, 169), android.graphics.PorterDuff.Mode.MULTIPLY);
                    arr_Category.add("category_metropolitan_lahoreNation");
                    b[4]=true;
                }
                if (category_arr.get(i).equals("category_metropolitan_islamabadNation"))
                {
                    image6_JCategory.setColorFilter(Color.rgb(169, 169, 169), android.graphics.PorterDuff.Mode.MULTIPLY);
                    arr_Category.add("category_metropolitan_islamabadNation");
                    b[5]=true;
                }
                if (category_arr.get(i).equals("category_metropolitan_karachiNation"))
                {
                    image7_JCategory.setColorFilter(Color.rgb(169, 169, 169), android.graphics.PorterDuff.Mode.MULTIPLY);
                    arr_Category.add("category_metropolitan_karachiNation");
                    b[6]=true;
                }
                if (category_arr.get(i).equals("category_sportsNation")) {
                    image8_JCategory.setColorFilter(Color.rgb(169, 169, 169), android.graphics.PorterDuff.Mode.MULTIPLY);
                    arr_Category.add("category_sportsNation");
                    b[7]=true;
                }
                if (category_arr.get(i).equals("category_lifestyleNation"))
                {
                    image9_JCategory.setColorFilter(Color.rgb(169, 169, 169), android.graphics.PorterDuff.Mode.MULTIPLY);
                    arr_Category.add("category_lifestyleNation");
                    b[8]=true;
                }
                if (category_arr.get(i).equals("category_snippetNation"))
                {
                    image10_JCategory.setColorFilter(Color.rgb(169, 169, 169), android.graphics.PorterDuff.Mode.MULTIPLY);
                    arr_Category.add("category_snippetNation");
                    b[9]=true;
                }
            }
        }

        // Back Button Listener
        back_JEdit_Category.setOnClickListener(view -> {
            fragment_profile frag_JEdit_Category = new fragment_profile();
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragmentContainer_BottomNavigation, frag_JEdit_Category); // give your fragment container id in first parameter
            transaction.commit();
            fragmentManager.popBackStack();

        }); // Back Listener

        // Done Button Listener

        done_JEdit_Category.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);

        if (arr_Category.size()>=2)
        {
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

            final String email_JCategory = sharedPreferences.getString("runningEmail", "");


            StringRequest stringRequest_JEdit_name = new StringRequest(Request.Method.POST, "https://pinnews.000webhostapp.com/updateCategory.php", response -> {
                // Code...
                progressBar.setVisibility(View.INVISIBLE);
                switch (response) {
                    case "Failed to connecting database!":
                        error_alert("J10P22E1M01");
                        break;
                    case "Retrieving Error!":
                        error_alert("J10P22E1M02");
                        break;
                    case "Updated":
                        Set<String> set1 = new HashSet<>(arr_Category);
                        editor.putStringSet("category_list", set1);
                        editor.apply();
                        Toast.makeText(view_JEdit_Category.getContext(), "Updated!", Toast.LENGTH_SHORT).show();
                        fragment_profile frag_JEdit_name = new fragment_profile();
                        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.fragmentContainer_BottomNavigation, frag_JEdit_name);
                        fragmentManager.popBackStack();
                        transaction.commit();
                        break;
                    case "Failed! Try Again.":
                        error_alert("J10P22E1M04");
                        break;
                    default:
                        error_alert("J10P22E1M: " + response);
                        break;
                }

            }, error -> {
                error_alert("J10P22E3M: " + error.getMessage());
            }) {
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String, String> hashMap_JEdit_Category = new HashMap<>();
                    hashMap_JEdit_Category.put("email", email_JCategory);
                    hashMap_JEdit_Category.put("national", finalCategory_nationalNation);
                    hashMap_JEdit_Category.put("opinion", finalCategory_opinionNation);
                    hashMap_JEdit_Category.put("business", finalCategory_businessNation);
                    hashMap_JEdit_Category.put("international", finalCategory_internationalNation);
                    hashMap_JEdit_Category.put("lahore", finalCategory_metropolitan_lahoreNation);
                    hashMap_JEdit_Category.put("islamabad", finalCategory_metropolitan_islamabadNation);
                    hashMap_JEdit_Category.put("karachi", finalCategory_metropolitan_karachiNation);
                    hashMap_JEdit_Category.put("sports", finalCategory_sportsNation);
                    hashMap_JEdit_Category.put("lifestyle", finalCategory_lifestyleNation);
                    hashMap_JEdit_Category.put("snippet", finalCategory_snippetNation);
                    return hashMap_JEdit_Category;

                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(view_JEdit_Category.getContext());
            requestQueue.add(stringRequest_JEdit_name);

        }
        else
        {
            Snackbar.make(view,"Minimum 2 Categories required", Snackbar.LENGTH_LONG).setAction("Action",null).show();
        }
        });


        return view_JEdit_Category;
    }       // On Create

    private void setMultipleEvent(final View v) {
        // Loop all child item of the grid
        for(int i = 0; i < grid_JEdit_Category.getChildCount(); i++)
        {
            final CardView cardView = (CardView) grid_JEdit_Category.getChildAt(i);

            final int finalI = i;
            cardView.setOnClickListener(view -> {
                if (cardView.getCardBackgroundColor().getDefaultColor() == -1 && !b[finalI])
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
                            b[1] = true;
                            arr_Category.add("category_opinionNation");
                            break;
                        case 2:
                            image3_JCategory.setColorFilter(Color.rgb(169, 169, 169), android.graphics.PorterDuff.Mode.MULTIPLY);
                            b[2] = true;
                            arr_Category.add("category_businessNation");
                            break;
                        case 3:
                            image4_JCategory.setColorFilter(Color.rgb(169, 169, 169), android.graphics.PorterDuff.Mode.MULTIPLY);
                            b[3] = true;
                            arr_Category.add("category_internationalNation");
                            break;
                        case 4:
                            image5_JCategory.setColorFilter(Color.rgb(169, 169, 169), android.graphics.PorterDuff.Mode.MULTIPLY);
                            b[4] = true;
                            arr_Category.add("category_metropolitan_lahoreNation");
                            break;
                        case 5:
                            image6_JCategory.setColorFilter(Color.rgb(169, 169, 169), android.graphics.PorterDuff.Mode.MULTIPLY);
                            b[5] = true;
                            arr_Category.add("category_metropolitan_islamabadNation");
                            break;
                        case 6:
                            image7_JCategory.setColorFilter(Color.rgb(169, 169, 169), android.graphics.PorterDuff.Mode.MULTIPLY);
                            b[6] = true;
                            arr_Category.add("category_metropolitan_karachiNation");
                            break;
                        case 7:
                            image8_JCategory.setColorFilter(Color.rgb(169, 169, 169), android.graphics.PorterDuff.Mode.MULTIPLY);
                            b[7] = true;
                            arr_Category.add("category_sportsNation");
                            break;
                        case 8:
                            image9_JCategory.setColorFilter(Color.rgb(169, 169, 169), android.graphics.PorterDuff.Mode.MULTIPLY);
                            b[8] = true;
                            arr_Category.add("category_lifestyleNation");
                            break;
                        case 9:
                            image10_JCategory.setColorFilter(Color.rgb(169, 169, 169), android.graphics.PorterDuff.Mode.MULTIPLY);
                            b[9] = true;
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
                            Toast.makeText(v.getContext(), "Click Any Category", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

                else
                {
                    switch (finalI)             // first time, vp=0
                    {
                        case 0:
                            b[0] = false;
                            image1_JCategory.clearColorFilter();
                            arr_Category.remove("category_nationalNation");
                            break;
                        case 1:
                            b[1] = false;
                            image2_JCategory.clearColorFilter();
                            arr_Category.remove("category_opinionNation");
                            break;
                        case 2:
                            b[2] = false;
                            image3_JCategory.clearColorFilter();
                            arr_Category.remove("category_businessNation");
                            break;
                        case 3:
                            b[3] = false;
                            image4_JCategory.clearColorFilter();
                            arr_Category.remove("category_internationalNation");
                            break;
                        case 4:
                            b[4] = false;
                            image5_JCategory.clearColorFilter();
                            arr_Category.remove("category_metropolitan_lahoreNation");
                            break;
                        case 5:
                            b[5] = false;
                            image6_JCategory.clearColorFilter();
                            arr_Category.remove("category_metropolitan_islamabadNation");
                            break;
                        case 6:
                            b[6] = false;
                            image7_JCategory.clearColorFilter();
                            arr_Category.remove("category_metropolitan_karachiNation");
                            break;
                        case 7:
                            b[7] = false;
                            image8_JCategory.clearColorFilter();
                            arr_Category.remove("category_sportsNation");
                            break;
                        case 8:
                            b[8] = false;
                            image9_JCategory.clearColorFilter();
                            arr_Category.remove("category_lifestyleNation");
                            break;
                        case 9:
                            b[9] = false;
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
                            Toast.makeText(v.getContext(), "Click Any Category", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }


            });
        }

    }

    public void error_alert(String code) {
        ConnectivityManager cm = (ConnectivityManager) view_JEdit_Category.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            AlertDialog.Builder alert = new AlertDialog.Builder(view_JEdit_Category.getContext());
            alert.setTitle("Error Found!");
            alert.setMessage("Error code: " + code + ". Report this error?");
            alert.setPositiveButton("Report", (dialog, which) -> {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"pinnews.pk@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Error Found ('" + code + " ')!!!");
                intent.setPackage("com.google.android.gm");
                startActivity(intent);
                setMultipleEvent(view_JEdit_Category);
            });
            alert.setNegativeButton("Cancel", (dialog, which) -> setMultipleEvent(view_JEdit_Category));
            alert.show();
        } else {
            Toast.makeText(getContext(), "Check Internet Connectivity!!!", Toast.LENGTH_SHORT).show();
        }
    }

} // Class
