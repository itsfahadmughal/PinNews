package com.example.pinnews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class home_Detailed extends AppCompatActivity {

    // Declaration
    private ImageButton back_JHome_Detailed;
    private TextView title_JHome_Detailed;
    private TextView content_JHome_Detailed;
    private TextView date_entry_JHome_Detailed;
    private TextView newstag_JHome_Detailed;
    private String date_entry;
    private String category;
    private String newstag;
    private String title_temp;
    private String[] title2;
    private String[] image2;
    private String[] content2;
    private String[] likeText2;
    private String[] shareText2;
    private String[] date_entry2;
    private String[] newstag2;
    private String[] category2;
    private ImageView image_JHome_Detailed;
    private ImageButton pinButton_JHome_Detailed;
    private SQLite_Helper_Pinned_News sqLite_helper_pinned_news;
    private Cursor cursor;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;

    //Initialization
    private void Init() {
        back_JHome_Detailed = findViewById(R.id.back_Home_Detailed);
        title_JHome_Detailed = findViewById(R.id.title_Home_Detailed);
        image_JHome_Detailed = findViewById(R.id.image_Home_Detailed);
        content_JHome_Detailed = findViewById(R.id.content_Home_Detailed);
        pinButton_JHome_Detailed = findViewById(R.id.pinButton_Home_Detailed);
        date_entry_JHome_Detailed = findViewById(R.id.date_entry_Home_Detailed);
        newstag_JHome_Detailed = findViewById(R.id.newstag_Home_Detailed);
        sqLite_helper_pinned_news = new SQLite_Helper_Pinned_News(home_Detailed.this);

        // Progress Bar
        progressBar = findViewById(R.id.spin_kit_home_detailed);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);
        progressBar.setVisibility(View.GONE);

        // Shared preferences
        sharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE);
    }


    @SuppressLint({"WrongConstant", "SetTextI18n"})
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__detailed);

        // Initialization
        Init();

        // Back Button Listener
        back_JHome_Detailed.setOnClickListener(view -> finish()); // Back Listener

        //content_JHome_Detailed.setJustificationMode(1);
        //Setting Values

        Intent home_detailed = getIntent();
        String title = home_detailed.getStringExtra("title");
        String image = home_detailed.getStringExtra("image");
        String content = home_detailed.getStringExtra("content");
        String likeText = home_detailed.getStringExtra("like_Text");
        String shareText = home_detailed.getStringExtra("shareText");
        date_entry = home_detailed.getStringExtra("date_entry");
        newstag = home_detailed.getStringExtra("newstag");
        category = home_detailed.getStringExtra("category");



        title_JHome_Detailed.setText(title);
        date_entry_JHome_Detailed.setText("Date: " + date_entry);
        newstag_JHome_Detailed.setText("Source: " + newstag);
        Glide.with(home_Detailed.this).load(image).into(image_JHome_Detailed);

        String[] temp_array = content.split("\\s{2,7}");
        StringBuilder cont = new StringBuilder();
        for (String s : temp_array) {
            cont.append(s).append(System.getProperty("line.separator"));
        }
        content_JHome_Detailed.setText(cont.toString());


        // Pinned News (LOGO SETTING)
        cursor = sqLite_helper_pinned_news.getData(title);
        if (cursor.getCount() == 0) {
            pinButton_JHome_Detailed.setBackgroundResource(R.drawable.unpin);
        } else {
            pinButton_JHome_Detailed.setBackgroundResource(R.drawable.pin);
        }


        //Boolean b = home_detailed.getBooleanExtra("pin_Status", false);

        pinButton_JHome_Detailed.setOnClickListener(v -> {
            SQLite_Helper_Pinned_News sqLite_helper_pinned_news = new SQLite_Helper_Pinned_News(getApplicationContext());
            if (cursor.getCount() == 0) {
                // Code...
                //pin
                boolean b = sqLite_helper_pinned_news.insertData(title, image, content, likeText, shareText, date_entry, newstag, category);
                if (b) {
                    cursor = sqLite_helper_pinned_news.getData(title);
                    pinButton_JHome_Detailed.setBackgroundResource(R.drawable.pin);
                    Snackbar snackbar;
                    snackbar = Snackbar.make(v, Html.fromHtml("<b>Pinned</b>"), Snackbar.LENGTH_SHORT);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.red));
                    TextView textView = snackBarView.findViewById(R.id.snackbar_text);
                    textView.setTextSize(18);
                    textView.setTextColor(ContextCompat.getColor(v.getContext(), R.color.white));
                    snackbar.show();
                } else {
                    pinButton_JHome_Detailed.setBackgroundResource(R.drawable.unpin);
                    Snackbar snackbar;
                    snackbar = Snackbar.make(v, Html.fromHtml("<b>Error Found! Try Again.</b>"), Snackbar.LENGTH_SHORT);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.red));
                    TextView textView = snackBarView.findViewById(R.id.snackbar_text);
                    textView.setTextSize(18);
                    textView.setTextColor(ContextCompat.getColor(v.getContext(), R.color.white));
                    snackbar.show();
                }
            } else {
                //Unpin
                int i = sqLite_helper_pinned_news.deleteResult(title);
                if (i >= 1) {
                    cursor = sqLite_helper_pinned_news.getData(title);
                    pinButton_JHome_Detailed.setBackgroundResource(R.drawable.unpin);
                    Snackbar snackbar;
                    snackbar = Snackbar.make(v, Html.fromHtml("<b>Un-Pinned</b>"), Snackbar.LENGTH_SHORT);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.red));
                    TextView textView = snackBarView.findViewById(R.id.snackbar_text);
                    textView.setTextSize(18);
                    textView.setTextColor(ContextCompat.getColor(v.getContext(), R.color.white));
                    snackbar.show();
                } else {
                    pinButton_JHome_Detailed.setBackgroundResource(R.drawable.pin);
                    Snackbar snackbar;
                    snackbar = Snackbar.make(v, Html.fromHtml("<b>No News has been affected!</b>"), Snackbar.LENGTH_SHORT);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.red));
                    TextView textView = snackBarView.findViewById(R.id.snackbar_text);
                    textView.setTextSize(18);
                    textView.setTextColor(ContextCompat.getColor(v.getContext(), R.color.white));
                    snackbar.show();
                }
            }
        });
    }


    public void swapFunction(View view) {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        title_temp = title_JHome_Detailed.getText().toString();
        String email = sharedPreferences.getString("runningEmail", "");

        StringRequest stringRequest_JSignup_check = new StringRequest(Request.Method.POST, "https://pinnews.000webhostapp.com/swap_home.php", response -> {
            response = response.trim();
            progressBar.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            switch (response) {
                case "Failed to connecting database!":
                    error_alert("J27P19E1M01");
                    break;
                case "Retrieving Error!":
                    error_alert("J27P19E1M02");
                    break;
                case "no":
                    Snackbar snackbar;
                    snackbar = Snackbar.make(view, Html.fromHtml("<b>No News Exist!</b>"), Snackbar.LENGTH_SHORT);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.red));
                    snackbar.show();

                    break;
                default:
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        title2 = new String[jsonArray.length()];
                        image2 = new String[jsonArray.length()];
                        content2 = new String[jsonArray.length()];
                        likeText2 = new String[jsonArray.length()];
                        shareText2 = new String[jsonArray.length()];
                        date_entry2 = new String[jsonArray.length()];
                        newstag2 = new String[jsonArray.length()];
                        category2 = new String[jsonArray.length()];

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            title2[i] = jsonObject.getString("title");
                            image2[i] = jsonObject.getString("image");
                            content2[i] = jsonObject.getString("content");
                            likeText2[i] = jsonObject.getString("likes");
                            shareText2[i] = jsonObject.getString("share");
                            date_entry2[i] = jsonObject.getString("date_entry");
                            newstag2[i] = jsonObject.getString("news_channel");
                            category2[i] = jsonObject.getString("category");
                        }
                        title_JHome_Detailed.setText(title2[0]);
                        date_entry_JHome_Detailed.setText("Date: " + date_entry2[0]);
                        newstag_JHome_Detailed.setText("Source: " + newstag2[0]);
                        Glide.with(home_Detailed.this).load(image2[0]).into(image_JHome_Detailed);

                        String[] temp_array = content2[0].split("\\s{2,6}");
                        StringBuilder cont = new StringBuilder();
                        for (String s : temp_array) {
                            cont.append(s).append(System.getProperty("line.separator"));
                        }
                        content_JHome_Detailed.setText(cont.toString());
                        // Pinned News (LOGO SETTING)
                        cursor = sqLite_helper_pinned_news.getData(title2[0]);
                        if (cursor.getCount() == 0) {
                            pinButton_JHome_Detailed.setBackgroundResource(R.drawable.unpin);
                        } else {
                            pinButton_JHome_Detailed.setBackgroundResource(R.drawable.pin);
                        }

                        // For Re-Swap
                        title_temp = title2[0];
                        newstag = newstag2[0];

                    } catch (JSONException e) {
                        error_alert("J27P19E3M: " + e.getMessage());

                    }

                    break;
            }
        }, error -> {
            progressBar.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            error_alert("J27P19E2M: " + error.getMessage());

        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap_JHome_Detailed = new HashMap<>();
                hashMap_JHome_Detailed.put("email", email);
                hashMap_JHome_Detailed.put("title", title_temp);
                hashMap_JHome_Detailed.put("date", date_entry);
                hashMap_JHome_Detailed.put("category", category);
                hashMap_JHome_Detailed.put("news_channel", newstag);
                return hashMap_JHome_Detailed;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(home_Detailed.this);
        requestQueue.add(stringRequest_JSignup_check);

    }

    private void error_alert(String code) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            AlertDialog.Builder alert = new AlertDialog.Builder(home_Detailed.this);
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
            Toast.makeText(home_Detailed.this, "Check Internet Connectivity!!!", Toast.LENGTH_SHORT).show();
        }
    }
}
