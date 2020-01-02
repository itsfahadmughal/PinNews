package com.example.pinnews;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baoyz.widget.PullRefreshLayout;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static androidx.appcompat.widget.SearchView.OnQueryTextListener;


class fragment_home extends Fragment {

    // Declaration

    private String[] title, image, content, likeText, shareText, date_entry, newstag, category, view;
    private String[] noti_title;
    private ImageButton filter_JHome;
    private RecyclerView recyclerView_JHome;
    private SharedPreferences sharedPreferences;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private SharedPreferences.Editor e;
    private ProgressBar progressBar;
    private SearchView searchView_JHome;
    private customlistView_Home customListView;
    private View view_Search;
    private String[] t_temp, l_temp;
    private final ArrayList<String> image_Slider_Array = new ArrayList<>();
    private Boolean b;
    private Drawer result;
    private PullRefreshLayout pullRefreshLayout_JHome;

    private GoogleSignInClient gsc;

    private View view_home;

    private Toolbar toolbar;


    // Initialization
    private void Init() {
        filter_JHome = view_home.findViewById(R.id.filter_Home);

        // Recycler View + LinearLayout

        recyclerView_JHome = view_home.findViewById(R.id.recyclerview_home);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView_JHome.setLayoutManager(layoutManager);

        sharedPreferences = view_home.getContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        sp = view_home.getContext().getSharedPreferences("Signup", Context.MODE_PRIVATE);
        e = sp.edit();
        // Progress Bar
        progressBar = view_home.findViewById(R.id.spin_kit_Home);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);
        progressBar.setVisibility(View.VISIBLE);
        //getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        //WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        searchView_JHome = view_home.findViewById(R.id.searchView_Home);
        b = false;

        toolbar = view_home.findViewById(R.id.toolbar_Home);

        // Google logout
        // Google Logout
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(view_home.getContext(), gso);

        // Swipe refresh Layout
        pullRefreshLayout_JHome = view_home.findViewById(R.id.swipeRefreshLayout_Home);


    }

    private void setDesignSearchView() {
        androidx.appcompat.widget.SearchView.SearchAutoComplete searchAutoComplete = searchView_JHome.findViewById(R.id.search_src_text);
        searchAutoComplete.setHintTextColor(getResources().getColor(android.R.color.holo_red_dark));
        searchAutoComplete.setTextColor(getResources().getColor(android.R.color.holo_red_dark));

        // change close icon color
        ImageView iconClose = searchView_JHome.findViewById(R.id.search_close_btn);
        iconClose.setColorFilter(getResources().getColor(R.color.red));
        //change search icon color
        ImageView iconSearch = searchView_JHome.findViewById(R.id.search_button);
        iconSearch.setColorFilter(getResources().getColor(R.color.red));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view_home = inflater.inflate(R.layout.fragment_home, container, false);
        view_Search = inflater.inflate(R.layout.fragment_home, container, false);
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));

        // Methods (Functions)

        Init();
        setDesignSearchView();
        // For offline

/*
        Cursor cursor_home;
        SQLite_Helper_Fragment_Home sqLite_helper_fragment_home = new SQLite_Helper_Fragment_Home(view_home.getContext());
        cursor_home = sqLite_helper_fragment_home.getAllData();
        if(cursor_home.getCount()==0)
        {
            Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
        }
        else
        {
            int i = 0;
            title = new String[cursor_home.getCount()];
            image = new String[cursor_home.getCount()];
            content = new String[cursor_home.getCount()];
            likeText = new String[cursor_home.getCount()];
            shareText = new String[cursor_home.getCount()];
            date_entry = new String[cursor_home.getCount()];
            newstag = new String[cursor_home.getCount()];
            l_temp = new String[cursor_home.getCount()];
            category = new String[cursor_home.getCount()];
            view = new String[cursor_home.getCount()];

            while (cursor_home.moveToNext())
            {
                title[i] = cursor_home.getString(0);
                image[i] = cursor_home.getString(1);
                content[i] = cursor_home.getString(2);
                likeText[i] = cursor_home.getString(3);
                shareText[i] = cursor_home.getString(4);
                date_entry[i] = cursor_home.getString(5);
                newstag[i] = cursor_home.getString(6);
                l_temp[i] = cursor_home.getString(7);
                image_Slider_Array.add(cursor_home.getString(8));
                category[i] = cursor_home.getString(9);
                view[i] = cursor_home.getString(10);
                i++;
            }
            customListView = new customlistView_Home((AppCompatActivity) view_home.getContext(), title, image, content, likeText, shareText, date_entry, newstag,
                    l_temp, image_Slider_Array, category, view);
            progressBar.setVisibility(View.INVISIBLE);
            recyclerView_JHome.setAdapter(customListView);
        }
*/
        notification(view_home);
        collectDataImage(view_home);

        String name = sharedPreferences.getString("runningName", "");
        String email = sharedPreferences.getString("runningEmail", "");
        String picture = sharedPreferences.getString("runningPicture", "");

        String temp = sp.getString("Signup_with", "create");
        Bitmap decodedByte = null;
        assert temp != null;
        if (temp.equals("create")) {
            byte[] decodedString = Base64.decode(picture, Base64.DEFAULT);
            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } else {
            try {
                URL url = new URL(picture);
                decodedByte = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }


        // Drawer

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity((Activity) view_home.getContext())
                .withHeaderBackground(R.drawable.header)
                .addProfiles(new ProfileDrawerItem().withName(name).withEmail(email).withIcon(decodedByte))
                .withOnAccountHeaderListener((view, profile, currentProfile) -> false)
                .build();

        /*
        //if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("Sports");
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName("      Cricket");
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withName("      Hockey");
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(4).withName("      Football");
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier(5).withName("      Kabaddi");
        PrimaryDrawerItem item6 = new PrimaryDrawerItem().withIdentifier(6).withName("      Badminton");
*/

        SecondaryDrawerItem item = (SecondaryDrawerItem) new SecondaryDrawerItem().withIdentifier(0).withName("Logout");

        //create the drawer and remember the `Drawer` result object
        result = new DrawerBuilder().withActivity((Activity) view_home.getContext())
                .withAccountHeader(headerResult)
                .withToolbar(toolbar)
                .addDrawerItems(
                        //item1, item2, item3, item4, item5, item6,
                        new DividerDrawerItem(),
                        item
                )
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    result.closeDrawer();
                    /*
                    if (position==0)
                    {
                        Toast.makeText(view_home.getContext(), "closed", Toast.LENGTH_SHORT).show();
                    }
                    if (position == 2) {
                        drawer_click_check = "cricket";
                    } else if (position == 3) {
                        drawer_click_check = "hockey";
                    } else if (position == 4) {
                        drawer_click_check = "football";
                    } else if (position == 5) {
                        drawer_click_check = "kabaddi";
                    } else if (position == 6) {
                        drawer_click_check = "badminton";
                    }
                    if (position == 1) {
                        collectData(view_home);
                    } else if (position > 1 && position < 7) {
                        progressBar.setVisibility(View.VISIBLE);
                        // Send request for sub-category
                        //check_subcategory(drawer_click_check);
                    }*/
                    if (drawerItem instanceof SecondaryDrawerItem) {
                        // Logout
                        if (checkConnectivity(view_home)) {
                            String temp1 = sp.getString("Signup_with", "");

                            assert temp1 != null;
                            switch (temp1) {
                                case "create":
                                    Intent logout_Intent = new Intent(getContext(), SignUp.class);
                                    editor.putString("runningName", "");
                                    editor.putString("runningEmail", "");
                                    editor.putString("runningPicture", "");
                                    editor.putBoolean("is_Login", false);
                                    editor.apply();
                                    startActivity(logout_Intent);
                                    ((Activity) view_home.getContext()).finish();
                                    break;
                                case "facebook":
                                    editor.putString("runningName", "");
                                    editor.putString("runningEmail", "");
                                    editor.putString("runningPicture", "");
                                    editor.apply();
                                    fbsignout(view_home);
                                    break;
                                case "google":
                                    editor.putString("runningName", "");
                                    editor.putString("runningEmail", "");
                                    editor.putString("runningPicture", "");
                                    editor.apply();
                                    gsignout(view_home);
                                    break;
                                default:
                                    Toast.makeText(view_home.getContext(), "Something went wrong, Try again!!!", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } else {
                            Toast.makeText(view_home.getContext(), "Check Internet Connectivity!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    return true;
                })
                .build();

        result.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        searchView_JHome.setOnQueryTextListener(new OnQueryTextListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            public boolean onQueryTextSubmit(String query) {
                b = true;
                // Code...
                searchView_JHome.clearFocus();
                progressBar.setVisibility(View.VISIBLE);
                //getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                //WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                final String email_Home = sharedPreferences.getString("runningEmail", "");
                StringRequest stringRequest_Search = new StringRequest(Request.Method.POST, "https://pinnews.000webhostapp.com/home_Search.php", response -> {
                    progressBar.setVisibility(View.INVISIBLE);
                    switch (response) {
                        case "Failed to connecting database!":
                            error_alert("J20P09E1M01");
                            break;
                        case "Retrieving Error!":
                            error_alert("J20P09E1M02");
                            break;
                        case "false":
                            Toast.makeText(getContext(), "No News Found!. Try another keyword", Toast.LENGTH_SHORT).show();
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
                                category = new String[jsonArray.length()];
                                newstag = new String[jsonArray.length()];
                                view = new String[jsonArray.length()];

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    title[i] = jsonObject.getString("title");
                                    image[i] = jsonObject.getString("image");
                                    content[i] = jsonObject.getString("content");
                                    likeText[i] = jsonObject.getString("likes");
                                    shareText[i] = jsonObject.getString("share");
                                    date_entry[i] = jsonObject.getString("date_entry");
                                    category[i] = jsonObject.getString("category");
                                    newstag[i] = jsonObject.getString("news_channel");
                                    view[i] = jsonObject.getString("view");

                                }

                                StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://pinnews.000webhostapp.com/like_checker.php", response1 -> {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    switch (response1) {
                                        case "Failed to connecting database!":
                                            error_alert("J20P10E1M01");
                                            break;
                                        case "Retrieving Error!":
                                            error_alert("J20P10E1M02");
                                            break;
                                        case "Not Found":
                                            t_temp = new String[]{"no"};
                                            l_temp = new String[]{"no"};
                                            customListView = new customlistView_Home((AppCompatActivity) view_home.getContext(), title, image, content, likeText, shareText, date_entry, newstag, t_temp, l_temp, image_Slider_Array, category, view);
                                            progressBar.setVisibility(View.INVISIBLE);
                                            recyclerView_JHome.setAdapter(customListView);
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
                                                editor.putBoolean("is_Search", true);
                                                editor.apply();
                                                customListView = new customlistView_Home((AppCompatActivity) view_home.getContext(), title, image, content, likeText, shareText, date_entry, newstag, t_temp, l_temp, image_Slider_Array, category, view);
                                                recyclerView_JHome.setAdapter(customListView);

                                            } catch (JSONException e) {
                                                error_alert("J20P10E3M: " + e.getMessage());
                                            }
                                            break;
                                    }
                                }, error -> {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    error_alert("J20P10E2M: " + error.getMessage());
                                }) {
                                    // Code
                                    @Override
                                    protected Map<String, String> getParams() {
                                        HashMap<String, String> hashMap = new HashMap<>();
                                        hashMap.put("email", email_Home);
                                        return hashMap;
                                    }
                                };  // String Request

                                RequestQueue requestQueue = Volley.newRequestQueue(view_home.getContext());
                                requestQueue.add(stringRequest);


                            } catch (JSONException e) {
                                error_alert("J20P09E3M: " + e.getMessage());
                            }
                            break;
                    }
                }, error -> {
                    progressBar.setVisibility(View.INVISIBLE);
                    error_alert("J20P09E2M: " + error.getMessage());
                }) {
                    // Code
                    @Override
                    protected Map<String, String> getParams() {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("email_Search", email_Home);
                        hashMap.put("title_Search", query);
                        return hashMap;
                    }
                };  // String Request

                RequestQueue requestQueue = Volley.newRequestQueue(view_home.getContext());
                requestQueue.add(stringRequest_Search);

                return true;
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Searchview (X) button

        searchView_JHome.setOnCloseListener(() -> {
            if (b) {
                progressBar.setVisibility(View.VISIBLE);
                // getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                // WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                collectData(view_Search);
            }
            return false;
        });


        // Filter
        filter_JHome.setOnClickListener(v -> {
            // Code...
            Intent intent_filter = new Intent(view_home.getContext(), Filtering_Category.class);
            startActivity(intent_filter);
            Objects.requireNonNull(getActivity()).finish();
        });

        // Swipe Refresh Layout

        // listen refresh event
        pullRefreshLayout_JHome.setOnRefreshListener(() -> {
            // start refresh
            collectDataImage(view_home);
            progressBar.setVisibility(View.INVISIBLE);

        });


        return view_home;
    } // on create

    private void notification(View view_home) {
        final String email_notification = sharedPreferences.getString("runningEmail", "");
        StringRequest stringRequest_JNotification = new StringRequest(Request.Method.POST, "https://pinnews.000webhostapp.com/getNotification.php", response -> {
            progressBar.setVisibility(View.INVISIBLE);
            switch (response) {
                case "Failed to connecting database!":
                    error_alert("J20P07E1M01");
                    break;
                case "Retrieving Error!":
                    error_alert("J20P07E1M02");
                    break;
                case "false":
                    error_alert("J20P07E1M: " + response);
                    break;
                default:
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        noti_title = new String[jsonArray.length()];

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            noti_title[i] = jsonObject.getString("title");
                        }

                        setNotificationTitle(noti_title[0], view_home);


                    } catch (JSONException e) {
                        Objects.requireNonNull(getActivity()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        error_alert("J20P07E3M: " + e.getMessage());
                    }
                    break;
            }
        }, error -> error_alert("J20P07E2M: " + error.getMessage())) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap_JNotification = new HashMap<>();
                hashMap_JNotification.put("email_notification", email_notification);
                return hashMap_JNotification;
            }
        };
        RequestQueue requestQueue_JNotification = Volley.newRequestQueue(view_home.getContext());
        requestQueue_JNotification.add(stringRequest_JNotification);

    }

    private void setNotificationTitle(String s, View view) {

        //NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(view.getContext());
        NotificationManager notificationManager = (NotificationManager) view.getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("com.example.pinnews", "Pinnews", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        // On Click Notification

        Intent noti_intent = new Intent(getContext(), BottomNavigation.class);
        noti_intent.putExtra("key_noti", "true");

        noti_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivities(view.getContext(), 1, new Intent[]{noti_intent}, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(view.getContext(), "com.example.pinnews")
                .setContentTitle(s)
                .setContentText("and 5+ Notifications Available")
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.logo)
                .setContentIntent(pendingIntent);

        Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(notificationUri);
        notificationManager.notify(999, builder.build());
    }


    // For Image Slider
    private void collectDataImage(View view_home) {
        progressBar.setVisibility(View.VISIBLE);
        try {
            final String email_Home = sharedPreferences.getString("runningEmail", "");
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://pinnews.000webhostapp.com/retrievingHomeSlider.php", response -> {
                progressBar.setVisibility(View.INVISIBLE);
                switch (response) {
                    case "Failed to connecting database!":
                        error_alert("J20P11E1M01");
                        break;
                    case "Retrieving Error!":
                        error_alert("J20P11E1M02");
                        break;
                    case "false":
                        Toast.makeText(getContext(), "Image not found!!!", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                image_Slider_Array.add(jsonObject.getString("image"));
                            }

                            Boolean is_Subcategory = sharedPreferences.getBoolean("is_Subcategory", false);
                            if (is_Subcategory.equals(true)) {
                                Set<String> set = sharedPreferences.getStringSet("subcategory_list", null);
                                Set<String> set_date = sharedPreferences.getStringSet("subcategory_list_date", null);

                                assert set != null;
                                ArrayList<String> category_list = new ArrayList<>(set);
                                assert set_date != null;
                                ArrayList<String> category_list_date = new ArrayList<>(set_date);

                                filter_JHome.setBackgroundResource(R.drawable.filled_filter);
                                check_subcategory(category_list, category_list_date);
                            } else {
                                filter_JHome.setBackgroundResource(R.drawable.empty_filter);
                                collectData(view_home);
                            }
                        } catch (JSONException e) {
                            error_alert("J20P11E3M: " + e.getMessage());
                        }
                        break;
                }
            }, error -> {
                progressBar.setVisibility(View.INVISIBLE);
                error_alert("J20P11E3M: " + error.getMessage());
            }) {
                // Code
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("email", email_Home);
                    return hashMap;
                }
            };  // String Request

            RequestQueue requestQueue = Volley.newRequestQueue(view_home.getContext());
            requestQueue.add(stringRequest);


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    // For Original Data
    private void collectData(final View view_home) {
        editor.putBoolean("is_Search", false);
        editor.apply();

        try {
            final String email_Home = sharedPreferences.getString("runningEmail", "");
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://pinnews.000webhostapp.com/retrievingNews.php", response -> {
                progressBar.setVisibility(View.INVISIBLE);
                switch (response) {
                    case "Failed to connecting database!":
                        error_alert("J20P12E1M01");
                        break;
                    case "Retrieving Error!":
                        error_alert("J20P12E1M02");
                        break;
                    case "false":
                        error_alert("J20P12E1M: " + response);
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
                            category = new String[jsonArray.length()];
                            newstag = new String[jsonArray.length()];
                            view = new String[jsonArray.length()];

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                title[i] = jsonObject.getString("title");
                                image[i] = jsonObject.getString("image");
                                content[i] = jsonObject.getString("content");
                                likeText[i] = jsonObject.getString("likes");
                                shareText[i] = jsonObject.getString("share");
                                date_entry[i] = jsonObject.getString("date_entry");
                                category[i] = jsonObject.getString("category");
                                newstag[i] = jsonObject.getString("news_channel");
                                view[i] = jsonObject.getString("view");

                            }
                            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, "https://pinnews.000webhostapp.com/like_checker.php", response1 -> {
                                progressBar.setVisibility(View.INVISIBLE);
                                b = false;
                                progressBar.setVisibility(View.INVISIBLE);
                                switch (response1) {
                                    case "Failed to connecting database!":
                                        error_alert("J20P10E1M01");
                                        break;
                                    case "Retrieving Error!":
                                        error_alert("J20P10E1M02");
                                        break;
                                    case "Not Found":
                                        t_temp = new String[]{"no"};
                                        l_temp = new String[]{"no"};
                                        customListView = new customlistView_Home((AppCompatActivity) view_home.getContext(), title, image, content, likeText, shareText, date_entry, newstag, t_temp, l_temp, image_Slider_Array, category, view);
                                        recyclerView_JHome.setAdapter(customListView);
                                        pullRefreshLayout_JHome.setRefreshing(false);

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

                                            customListView = new customlistView_Home((AppCompatActivity) view_home.getContext(), title, image, content, likeText, shareText, date_entry, newstag, t_temp, l_temp, image_Slider_Array, category, view);
                                            recyclerView_JHome.setAdapter(customListView);
                                            progressBar.setVisibility(View.INVISIBLE);
                                            //new SQLite_Helper_Fragment_Home(view_home.getContext()).deleteResult();
                                            //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                            pullRefreshLayout_JHome.setRefreshing(false);

                                        } catch (JSONException e) {
                                            error_alert("J20P10E3M: " + e.getMessage());
                                        }

                                        break;
                                }
                            }, error -> {
                                progressBar.setVisibility(View.INVISIBLE);
                                error_alert("J20P10E2M: " + error.getMessage());
                            }) {
                                // Code
                                @Override
                                protected Map<String, String> getParams() {
                                    HashMap<String, String> hashMap = new HashMap<>();
                                    hashMap.put("email", email_Home);
                                    return hashMap;
                                }

                            };  // String Request

                            RequestQueue requestQueue = Volley.newRequestQueue(view_home.getContext());
                            requestQueue.add(stringRequest1);


                        } catch (JSONException e) {
                            error_alert("J20P12E3M: " + e.getMessage());
                        }
                        break;
                }

            }, error -> {
                // Code...
                progressBar.setVisibility(View.INVISIBLE);
                error_alert("J20P12E2M: " + error.getMessage());
            }) {
                // Code
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("ema", email_Home);
                    return hashMap;
                }
            };  // String Request

            RequestQueue requestQueue = Volley.newRequestQueue(view_home.getContext());
            requestQueue.add(stringRequest);


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    // For Search
    @Override
    public void onResume() {
        super.onResume();
        if (getView() == null) {
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {

            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                if (!searchView_JHome.isIconified()) {
                    searchView_JHome.setIconified(true);
                    searchView_JHome.onActionViewCollapsed();
                    if (b) {
                        progressBar.setVisibility(View.VISIBLE);
                        //getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        //WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        collectData(view_home);
                    }

                }
                return true;
            }
            return false;
        });
    }


    // For internet connectivity
    private boolean checkConnectivity(View view_JHome) {
        ConnectivityManager connectivity = (ConnectivityManager) view_JHome.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (NetworkInfo networkInfo : info)
                    if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }


    // Signout
    // Sign out
    private void gsignout(final View view_JProfile) {
        gsc.signOut().addOnCompleteListener(Objects.requireNonNull(getActivity()), task -> {
            e.putString("Signup_with", "");
            e.apply();
            Intent logout_Intent = new Intent(view_JProfile.getContext(), SignUp.class);
            startActivity(logout_Intent);
        });
    }

    private void fbsignout(View view_JProfile) {
        Toast.makeText(view_JProfile.getContext(), "Fb logout", Toast.LENGTH_SHORT).show();
        e.putString("Signup_with", "");
        e.apply();

        // Logout from Facebook
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            LoginManager.getInstance().logOut();
            Intent logout_Intent = new Intent(view_JProfile.getContext(), SignUp.class);
            startActivity(logout_Intent);
        }
    }

    private void check_subcategory(ArrayList<String> drawer_click_check, ArrayList<String> drawer_click_date) {
        String number_Sub = String.valueOf(drawer_click_check.size());
        String number_Sub_date = String.valueOf(drawer_click_date.size());


        editor.putBoolean("is_Subcategory", false);
        editor.apply();
        final String email_Home = sharedPreferences.getString("runningEmail", "");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://pinnews.000webhostapp.com/retrievingNews_Subcategory.php", response -> {
            progressBar.setVisibility(View.INVISIBLE);
            switch (response) {
                case "Failed to connecting database!":
                    error_alert("J20P13E1M01");
                    break;
                case "Retrieving Error!":
                    error_alert("J20P13E1M02");
                    break;
                case "false":
                    Toast.makeText(getContext(), "Value not found!!!", Toast.LENGTH_SHORT).show();
                    filter_JHome.setBackgroundResource(R.drawable.empty_filter);
                    collectDataImage(view_home);
                    break;
                default:
                    progressBar.setVisibility(View.INVISIBLE);
                    // Code...
                    try {

                        JSONArray jsonArray = new JSONArray(response);
                        title = new String[jsonArray.length()];
                        image = new String[jsonArray.length()];
                        content = new String[jsonArray.length()];
                        likeText = new String[jsonArray.length()];
                        shareText = new String[jsonArray.length()];
                        date_entry = new String[jsonArray.length()];
                        category = new String[jsonArray.length()];
                        newstag = new String[jsonArray.length()];
                        view = new String[jsonArray.length()];

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            title[i] = jsonObject.getString("title");
                            image[i] = jsonObject.getString("image");
                            content[i] = jsonObject.getString("content");
                            likeText[i] = jsonObject.getString("likes");
                            shareText[i] = jsonObject.getString("share");
                            date_entry[i] = jsonObject.getString("date_entry");
                            category[i] = jsonObject.getString("category");
                            newstag[i] = jsonObject.getString("news_channel");
                            view[i] = jsonObject.getString("view");

                        }
                        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, "https://pinnews.000webhostapp.com/like_checker.php", response1 -> {
                            progressBar.setVisibility(View.INVISIBLE);
                            b = false;
                            progressBar.setVisibility(View.INVISIBLE);
                            switch (response1) {
                                case "Failed to connecting database!":
                                    error_alert("J20P10E1M01");
                                    break;
                                case "Retrieving Error!":
                                    error_alert("J20P10E1M02");
                                    break;
                                case "Not Found":
                                    t_temp = new String[]{"no"};
                                    l_temp = new String[]{"no"};
                                    customListView = new customlistView_Home((AppCompatActivity) view_home.getContext(), title, image, content, likeText, shareText, date_entry, newstag, t_temp, l_temp, image_Slider_Array, category, view);
                                    recyclerView_JHome.setAdapter(customListView);
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
                                        editor.putBoolean("is_Search", true);
                                        editor.apply();
                                        customListView = new customlistView_Home((AppCompatActivity) view_home.getContext(), title, image, content, likeText, shareText, date_entry, newstag, t_temp, l_temp, image_Slider_Array, category, view);
                                        recyclerView_JHome.setAdapter(customListView);

                                        progressBar.setVisibility(View.INVISIBLE);
                                        //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                                    } catch (JSONException e) {
                                        error_alert("J20P10E3M: " + e.getMessage());
                                    }
                                    break;
                            }
                        }, error -> {
                            progressBar.setVisibility(View.INVISIBLE);
                            error_alert("J20P10E2M: " + error.getMessage());
                        }) {
                            // Code
                            @Override
                            protected Map<String, String> getParams() {
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("email", email_Home);
                                return hashMap;
                            }
                        };  // String Request

                        RequestQueue requestQueue = Volley.newRequestQueue(view_home.getContext());
                        requestQueue.add(stringRequest1);


                    } catch (JSONException e) {
                        error_alert("J20P13E3M: " + e.getMessage());
                    }
                    break;
            }
        }, error -> {
            // Code...
            progressBar.setVisibility(View.INVISIBLE);
            error_alert("J20P13E2M: " + error.getMessage());
        }) {
            // Code
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("number", number_Sub);
                hashMap.put("number_date",number_Sub_date);
                for (int i = 0; i < drawer_click_check.size(); i++) {
                    hashMap.put("sub_category_name" + i, drawer_click_check.get(i));
                }

                for (int i = 0; i < drawer_click_date.size(); i++) {
                    hashMap.put("sub_category_name_date" + i, drawer_click_date.get(i));
                }

                return hashMap;
            }
        };  // String Request

        RequestQueue requestQueue = Volley.newRequestQueue(view_home.getContext());
        requestQueue.add(stringRequest);
    }

    private void error_alert(String code) {
        ConnectivityManager cm = (ConnectivityManager) view_home.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            AlertDialog.Builder alert = new AlertDialog.Builder(view_home.getContext());
            alert.setTitle("Error Found!");
            alert.setMessage("Error code: " + code + ". Report this error?");
            alert.setPositiveButton("Report", (dialog, which) -> {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"pinnews.pk@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Error Found ('" + code + " ')!!!");
                intent.setPackage("com.google.android.gm");
                startActivity(intent);
            });
            alert.setNegativeButton("Cancel", (dialog, which) -> {

            });
            alert.show();
        } else {
            Toast.makeText(getContext(), "Check Internet Connectivity!!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        result.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

    }
}
