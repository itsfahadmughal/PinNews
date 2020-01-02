package com.example.pinnews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baoyz.widget.PullRefreshLayout;
import com.github.ybq.android.spinkit.style.FadingCircle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

class fragment_one_trending extends Fragment {

    // Declaration

    private String[] title, image, content, likeText, shareText, t_temp, l_temp, date_entry, newstag, category, view;
    private RecyclerView recyclerView_JOne_trending;
    private SharedPreferences sharedPreferences;
    private ProgressBar progressBar;

    private customlistView_fragment_one_trending customListView_JTrending;
    private View view_Jfragment_one_trending;

    private PullRefreshLayout pullRefreshLayout_JOne_Trending;


    // Initialization
    private void Init() {
        recyclerView_JOne_trending = view_Jfragment_one_trending.findViewById(R.id.recyclerview_one_trending);
        sharedPreferences = view_Jfragment_one_trending.getContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        // Progress Bar
        progressBar = view_Jfragment_one_trending.findViewById(R.id.spin_kit_fragment_one_trending);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);
        progressBar.setVisibility(View.VISIBLE);

        // Recycler View
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView_JOne_trending.setLayoutManager(layoutManager);


        // Swipe refresh Layout
        pullRefreshLayout_JOne_Trending = view_Jfragment_one_trending.findViewById(R.id.swipeRefreshLayout_one_trending);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view_Jfragment_one_trending = inflater.inflate(R.layout.fragment_one_trending, container, false);

        Init();

        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        collectData();

        // Swipe Refresh Layout

        // listen refresh event
        pullRefreshLayout_JOne_Trending.setOnRefreshListener(() -> {
            // start refresh
            collectData();
            progressBar.setVisibility(View.INVISIBLE);

        });


        return view_Jfragment_one_trending;
    }

    // For Original Data
    private void collectData() {

        final String email_Home = sharedPreferences.getString("runningEmail", "");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://pinnews.000webhostapp.com/retrievingTrendingOne.php", response -> {
            response = response.trim();
            progressBar.setVisibility(View.INVISIBLE);
            switch (response) {
                case "Failed to connecting database!":
                    error_alert("J22P14E1M01");
                    break;
                case "Retrieving Error!":
                    error_alert("J22P14E1M02");
                    break;
                case "false":
                    error_alert("J22P14E1M: " + response);
                    break;
                default:
                    try {

                        JSONArray jsonArray = new JSONArray(response);
                        title = new String[jsonArray.length()];
                        image = new String[jsonArray.length()];
                        content = new String[jsonArray.length()];
                        likeText = new String[jsonArray.length()];
                        shareText = new String[jsonArray.length()];
                        date_entry = new String[jsonArray.length()];
                        newstag = new String[jsonArray.length()];
                        category = new String[jsonArray.length()];
                        view = new String[jsonArray.length()];

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            title[i] = jsonObject.getString("title");
                            image[i] = jsonObject.getString("image");
                            content[i] = jsonObject.getString("content");
                            likeText[i] = jsonObject.getString("likes");
                            shareText[i] = jsonObject.getString("share");
                            date_entry[i] = jsonObject.getString("date_entry");
                            newstag[i] = jsonObject.getString("news_channel");
                            category[i] = jsonObject.getString("category");
                            view[i] = jsonObject.getString("view");
                        }
                        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, "https://pinnews.000webhostapp.com/like_checker.php", response1 -> {
                            response1 = response1.trim();
                            progressBar.setVisibility(View.INVISIBLE);
                            switch (response1) {
                                case "Failed to connecting database!":
                                    error_alert("J22P10E1M01");
                                    break;
                                case "Retrieving Error!":
                                    error_alert("J22P10E1M02");
                                    break;
                                case "Not Found":
                                    t_temp = new String[]{"no"};
                                    l_temp = new String[]{"no"};
                                    customListView_JTrending = new customlistView_fragment_one_trending((AppCompatActivity) view_Jfragment_one_trending.getContext(), title, image, content, likeText, shareText, t_temp, l_temp, date_entry, newstag, category, view);
                                    recyclerView_JOne_trending.setAdapter(customListView_JTrending);
                                    pullRefreshLayout_JOne_Trending.setRefreshing(false);
                                    break;
                                default:
                                    try {
                                        JSONArray jsonArray1 = new JSONArray(response1);
                                        t_temp = new String[jsonArray1.length()];
                                        l_temp = new String[jsonArray1.length()];
                                        for (int i = 0; i < jsonArray1.length(); i++) {
                                            JSONObject jsonObject = jsonArray1.getJSONObject(i);
                                            t_temp[i] = jsonObject.getString("title");
                                            l_temp[i] = jsonObject.getString("likes");
                                        }

                                        customListView_JTrending = new customlistView_fragment_one_trending((AppCompatActivity) view_Jfragment_one_trending.getContext(), title, image, content, likeText, shareText, t_temp, l_temp, date_entry, newstag, category, view);
                                        progressBar.setVisibility(View.INVISIBLE);
                                        recyclerView_JOne_trending.setAdapter(customListView_JTrending);
                                        pullRefreshLayout_JOne_Trending.setRefreshing(false);


                                    } catch (JSONException e) {
                                        error_alert("J22P10E3M: " + e.getMessage());
                                    }
                                    break;
                            }
                        }, error -> {
                            progressBar.setVisibility(View.INVISIBLE);
                            error_alert("J22P10E2M: " + error.getMessage());
                        }) {
                            // Code
                            @Override
                            protected Map<String, String> getParams() {
                                HashMap<String,String> hashMap = new HashMap<>();
                                hashMap.put("email", email_Home);
                                return hashMap;
                            }
                        };  // String Request

                        RequestQueue requestQueue = Volley.newRequestQueue(view_Jfragment_one_trending.getContext());
                        requestQueue.add(stringRequest1);


                    } catch (JSONException e) {
                        error_alert("J22P14E3M: " + e.getMessage());
                    }
                    break;
            }
        }, error -> {
            // Code...
            progressBar.setVisibility(View.INVISIBLE);
            error_alert("J22P14E2M: " + error.getMessage());
        }) {
            // Code
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("ema", email_Home);
                return hashMap;
            }
        };  // String Request

        RequestQueue requestQueue = Volley.newRequestQueue(view_Jfragment_one_trending.getContext());
        requestQueue.add(stringRequest);

    } // Close

    private void error_alert(String code) {
        ConnectivityManager cm = (ConnectivityManager) view_Jfragment_one_trending.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            AlertDialog.Builder alert = new AlertDialog.Builder(view_Jfragment_one_trending.getContext());
            alert.setTitle("Error Found!");
            alert.setMessage("Error code: " + code + ". Report this error?");
            alert.setPositiveButton("Report", (dialog, which) -> {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"pinnews.pk@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Error Found ('" + code + " ')!!!");
                intent.setPackage("com.google.android.gm");
                startActivity(intent);
                collectData();

            });
            alert.setNegativeButton("Cancel", (dialog, which) -> collectData());
            alert.show();
        } else {
            Toast.makeText(getContext(), "Check Internet Connectivity!!!", Toast.LENGTH_SHORT).show();
        }
    }


}
