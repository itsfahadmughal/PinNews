package com.example.pinnews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.style.FadingCircle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

class fragment_notification extends Fragment {

    private String[] title, image, content, date_entry, newstag, category;
    private ListView listView_JNotification;
    private customlistview_notification customlistview_JNotification;
    private View view_JFragment_Notification;
    private ImageView imageView_JNotification;
    private ProgressBar progressBar;
    private String email_JNotification;

    // Initialization
    private void Init() {
        listView_JNotification = view_JFragment_Notification.findViewById(R.id.listview_Notification);
        // Declaration
        SharedPreferences sharedPreferences = view_JFragment_Notification.getContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        imageView_JNotification = view_JFragment_Notification.findViewById(R.id.no_notification);

        // Progress Bar
        progressBar = view_JFragment_Notification.findViewById(R.id.spin_kit_Notification);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);
        progressBar.setVisibility(View.VISIBLE);
        //getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        email_JNotification = sharedPreferences.getString("runningEmail", "");

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view_JFragment_Notification = inflater.inflate(R.layout.fragment_notification, container,false);

        // Initialization
        Init();
        getNotifications(email_JNotification);
        return view_JFragment_Notification;
    }

    private void getNotifications(String email_JNotification) {
        progressBar.setVisibility(View.VISIBLE);
        // code...
        StringRequest stringRequest_JNotification = new StringRequest(Request.Method.POST, "https://pinnews.000webhostapp.com/getNotification.php", response -> {
            progressBar.setVisibility(View.INVISIBLE);
            switch (response) {
                case "Failed to connecting database!":
                    error_alert("J21P07E1M01");
                    break;
                case "Retrieving Error!":
                    error_alert("J21P07E1M02");
                    break;
                case "false":
                    imageView_JNotification.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "No Notifications yet!!!", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    imageView_JNotification.setVisibility(View.GONE);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        title = new String[jsonArray.length()];
                        image = new String[jsonArray.length()];
                        content = new String[jsonArray.length()];
                        date_entry = new String[jsonArray.length()];
                        newstag = new String[jsonArray.length()];
                        category = new String[jsonArray.length()];

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            title[i] = jsonObject.getString("title");
                            image[i] = jsonObject.getString("image");
                            content[i] = jsonObject.getString("content");
                            date_entry[i] = jsonObject.getString("date_entry");
                            newstag[i] = jsonObject.getString("news_channel");
                            category[i] = jsonObject.getString("category");
                        }
                        customlistview_JNotification = new customlistview_notification((AppCompatActivity) view_JFragment_Notification.getContext(), title, image, content, date_entry, newstag, category);
                        listView_JNotification.setAdapter(customlistview_JNotification);
                        //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    } catch (JSONException e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        error_alert("J21P07E3M: " + e.getMessage());
                    }
                    break;
            }
        }, error -> {
            progressBar.setVisibility(View.INVISIBLE);
            error_alert("J21P07E2M: " + error.getMessage());
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap_JNotification = new HashMap<>();
                hashMap_JNotification.put("email_notification", email_JNotification);
                return hashMap_JNotification;
            }
        };
        RequestQueue requestQueue_JNotification = Volley.newRequestQueue(view_JFragment_Notification.getContext());
        requestQueue_JNotification.add(stringRequest_JNotification);

    }

    private void error_alert(String code) {
        ConnectivityManager cm = (ConnectivityManager) view_JFragment_Notification.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            AlertDialog.Builder alert = new AlertDialog.Builder(view_JFragment_Notification.getContext());
            alert.setTitle("Error Found!");
            alert.setMessage("Error code: " + code + ". Report this error?");
            alert.setPositiveButton("Report", (dialog, which) -> {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"pinnews.pk@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Error Found ('" + code + " ')!!!");
                intent.setPackage("com.google.android.gm");
                startActivity(intent);
                getNotifications(email_JNotification);
            });
            alert.setNegativeButton("Cancel", (dialog, which) -> getNotifications(email_JNotification));
            alert.show();
        } else {
            Toast.makeText(getContext(), "Check Internet Connectivity!!!", Toast.LENGTH_SHORT).show();
        }
    }

}
