package com.example.pinnews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

class fragment_trending extends Fragment {

    private String[] title, image, content, likeText, shareText, date_entry, newstag, category, view;
    private RecyclerView recyclerView_JTrending;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private androidx.appcompat.widget.SearchView searchView_JTrending;
    private customlistView_fragment_one_trending customListView_JTrending;
    private String[] t_temp, l_temp;
    private Boolean b;

    private View view_JTrending;
    private RelativeLayout r1;
    private RelativeLayout r2;


    @SuppressLint("CommitPrefEdits")
    private void Init(View view_JTrending) {
        searchView_JTrending = view_JTrending.findViewById(R.id.searchView_fragment_trending);

        recyclerView_JTrending = view_JTrending.findViewById(R.id.recyclerview_trending);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView_JTrending.setLayoutManager(layoutManager);

        sharedPreferences = view_JTrending.getContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        b = false;

        r1 = view_JTrending.findViewById(R.id.layout_tab1_Trending);
        r2 = view_JTrending.findViewById(R.id.layout_tab2_Trending);

    }

    private void setDesignSearchView() {
        androidx.appcompat.widget.SearchView.SearchAutoComplete searchAutoComplete = searchView_JTrending.findViewById(R.id.search_src_text);
        searchAutoComplete.setHintTextColor(getResources().getColor(android.R.color.holo_red_dark));
        searchAutoComplete.setTextColor(getResources().getColor(android.R.color.holo_red_dark));

        // change close icon color
        ImageView iconClose = searchView_JTrending.findViewById(R.id.search_close_btn);
        iconClose.setColorFilter(getResources().getColor(R.color.red));
        //change search icon color
        ImageView iconSearch = searchView_JTrending.findViewById(R.id.search_button);
        iconSearch.setColorFilter(getResources().getColor(R.color.red));
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view_JTrending = inflater.inflate(R.layout.fragment_trending, container,false);
        // Declaration

        // Initialization
        Init(view_JTrending);
        setDesignSearchView();

        // For Full Screen (maybe)
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));

        if (!b) {
            setTablayout(view_JTrending);
        }

        // For Search

        searchView_JTrending.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            public boolean onQueryTextSubmit(String query) {
                recyclerView_JTrending.setVisibility(View.VISIBLE);
                b = true;
                r1.setVisibility(View.GONE);
                r2.setVisibility(View.GONE);
                // Code...
                searchView_JTrending.clearFocus();
                final String email_Home = sharedPreferences.getString("runningEmail", "");
                StringRequest stringRequest_Search = new StringRequest(Request.Method.POST, "https://pinnews.000webhostapp.com/home_Search.php", response -> {
                    response = response.trim();
                    // Code...
                    switch (response) {
                        case "Failed to connecting database!":
                            error_alert("J25P09E1M01");
                            break;
                        case "Retrieving Error!":
                            error_alert("J25P09E1M02");
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
                                    response1 = response1.trim();
                                    switch (response1) {
                                        case "Failed to connecting database!":
                                            error_alert("J25P10E1M01");
                                            break;
                                        case "Retrieving Error!":
                                            error_alert("J25P10E1M02");
                                            break;
                                        case "Not Found":
                                            t_temp = new String[]{"no"};
                                            l_temp = new String[]{"no"};
                                            customListView_JTrending = new customlistView_fragment_one_trending((AppCompatActivity) view_JTrending.getContext(), title, image, content, likeText, shareText, date_entry, newstag, t_temp, l_temp, category, view);
                                            recyclerView_JTrending.setAdapter(customListView_JTrending);
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
                                                customListView_JTrending = new customlistView_fragment_one_trending((AppCompatActivity) view_JTrending.getContext(), title, image, content, likeText, shareText, date_entry, newstag, t_temp, l_temp, category, view);
                                                recyclerView_JTrending.setAdapter(customListView_JTrending);

                                            } catch (JSONException e) {
                                                error_alert("J25P10E3M: " + e.getMessage());
                                            }
                                            break;
                                    }
                                }, error -> {
                                    error_alert("J25P10E2M: " + error.getMessage());
                                }) {
                                    // Code
                                    @Override
                                    protected Map<String, String> getParams() {
                                        HashMap<String,String> hashMap = new HashMap<>();
                                        hashMap.put("email", email_Home);
                                        return hashMap;
                                    }
                                };  // String Request

                                RequestQueue requestQueue = Volley.newRequestQueue(view_JTrending.getContext());
                                requestQueue.add(stringRequest);


                            } catch (JSONException e) {
                                error_alert("J25P09E3M: " + e.getMessage());
                            }

                            break;
                    }
                }, error -> {
                    error_alert("J25P09E2M: " + error.getMessage());
                }) {
                    // Code
                    @Override
                    protected Map<String, String> getParams() {
                        HashMap<String,String> hashMap = new HashMap<>();
                        hashMap.put("email_Search", email_Home);
                        hashMap.put("title_Search", query);
                        return hashMap;
                    }
                };  // String Request

                RequestQueue requestQueue = Volley.newRequestQueue(view_JTrending.getContext());
                requestQueue.add(stringRequest_Search);

                return true;
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Searchview (X) button

        searchView_JTrending.setOnCloseListener(() -> {
            if (b) {
                // getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                // WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                setTablayout(view_JTrending);
                r1.setVisibility(View.VISIBLE);
                r2.setVisibility(View.VISIBLE);
                recyclerView_JTrending.setVisibility(View.GONE);
            }
            b = false;
            return false;
        });


        return view_JTrending;
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
                if (!searchView_JTrending.isIconified()) {
                    searchView_JTrending.setIconified(true);
                    searchView_JTrending.onActionViewCollapsed();
                    if (b) {
                        setTablayout(view_JTrending);
                    }

                }
                return true;
            }
            return false;
        });
    }

    private void setTablayout(View view_JTrending) {

        // Adapter for the viewpager
        SectionsPagerAdapter adapter_JTrending = new SectionsPagerAdapter(getChildFragmentManager());
        // Adding fragment to adapter
        adapter_JTrending.addFragment(new fragment_one_trending());
        adapter_JTrending.addFragment(new fragment_two_trending());
        //Initializing viewpager
        ViewPager viewpager_JTrending = view_JTrending.findViewById(R.id.viewPager_Trending);
        // Set Adapter
        viewpager_JTrending.setAdapter(adapter_JTrending);

        // Initializing TabLabout
        TabLayout tabLayout_JTrending = view_JTrending.findViewById(R.id.tabLayout_Trending);
        // Pass the ViewPager to the TabLayout
        tabLayout_JTrending.setupWithViewPager(viewpager_JTrending);
        //tabLayout_JTrending.setBackgroundColor(getResources().getColor(R.color.grey));
        tabLayout_JTrending.setTabTextColors(getResources().getColor(R.color.black), getResources().getColor(R.color.black));

        //tabLayout_JTrending.setSelectedTabIndicatorHeight((int) (2 * getResources().getDisplayMetrics().density));
        tabLayout_JTrending.setTabTextColors(Color.parseColor("#000000"), Color.parseColor("#BD081C"));

        // Set the tabs index and set text, icon etc for your tabs.
        Objects.requireNonNull(tabLayout_JTrending.getTabAt(0)).setText("Latest News");
        Objects.requireNonNull(tabLayout_JTrending.getTabAt(1)).setText("Top rated");


        // Solution for tab layout
        viewpager_JTrending.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(tabLayout_JTrending));
        tabLayout_JTrending.addOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewpager_JTrending));
    }


    class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragmentList_JTrending = new ArrayList<>();

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList_JTrending.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList_JTrending.size();
        }

        void addFragment(Fragment fragment) {
            fragmentList_JTrending.add(fragment);
        }
    }


    // Exceptions

    private void error_alert(String code) {
        ConnectivityManager cm = (ConnectivityManager) view_JTrending.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            AlertDialog.Builder alert = new AlertDialog.Builder(view_JTrending.getContext());
            alert.setTitle("Error Found!");
            alert.setMessage("Error code: " + code + ". Report this error?");
            alert.setPositiveButton("Report", (dialog, which) -> {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"pinnews.pk@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Error Found ('" + code + " ')!!!");
                intent.setPackage("com.google.android.gm");
                startActivity(intent);
                setTablayout(view_JTrending);

            });
            alert.setNegativeButton("Cancel", (dialog, which) -> setTablayout(view_JTrending));
            alert.show();
        } else {
            Toast.makeText(getContext(), "Check Internet Connectivity!!!", Toast.LENGTH_SHORT).show();
        }
    }

}
