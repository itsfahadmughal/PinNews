package com.example.pinnews;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.style.FadingCircle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

class fragment_profile_edit extends Fragment {

    // Declaration
    private ImageButton back_JProfile_edit;
    private ImageButton dp_JBProfile_edit;
    private ImageButton name_JBProfile_edit;
    private ImageButton location_JBProfile_edit;
    private ImageButton dob_JBProfile_edit;
    private ImageButton gender_JBProfile_edit;
    private ImageButton phone_JBProfile_edit;
    private ImageButton password_JBProfile_edit;
    private TextView name_JProfile_edit;
    private TextView email_JProfile_edit;
    private TextView location_JProfile_edit;
    private TextView dob_JProfile_edit;
    private TextView gender_JProfile_edit;
    private TextView phone_JProfile_edit;
    private String country_JProfile_edit;
    private String address_JProfile_edit;
    private String dp_String;
    private CircleImageView dp_JProfile_edit;
    private SharedPreferences sharedPreferences;
    private SharedPreferences sp;
    private ProgressBar progressBar;
    private View view_JProfile_edit;

    private void Init()
    {
        back_JProfile_edit = view_JProfile_edit.findViewById(R.id.back_Profile_edit);
        dp_JBProfile_edit = view_JProfile_edit.findViewById(R.id.dp_BProfile_edit);
        name_JProfile_edit = view_JProfile_edit.findViewById(R.id.name_Profile_edit);
        name_JBProfile_edit = view_JProfile_edit.findViewById(R.id.name_BProfile_edit);
        email_JProfile_edit = view_JProfile_edit.findViewById(R.id.email_Profile_edit);
        location_JProfile_edit = view_JProfile_edit.findViewById(R.id.location_Profile_edit);
        location_JBProfile_edit = view_JProfile_edit.findViewById(R.id.location_BProfile_edit);
        dob_JProfile_edit = view_JProfile_edit.findViewById(R.id.dob_Profile_edit);
        dob_JBProfile_edit = view_JProfile_edit.findViewById(R.id.dob_BProfile_edit);
        gender_JProfile_edit = view_JProfile_edit.findViewById(R.id.gender_Profile_edit);
        gender_JBProfile_edit = view_JProfile_edit.findViewById(R.id.gender_BProfile_edit);
        phone_JProfile_edit = view_JProfile_edit.findViewById(R.id.phone_Profile_edit);
        phone_JBProfile_edit = view_JProfile_edit.findViewById(R.id.phone_BProfile_edit);
        password_JBProfile_edit = view_JProfile_edit.findViewById(R.id.password_BProfile_edit);
        dp_JProfile_edit = view_JProfile_edit.findViewById(R.id.dp_Profile_edit);

        sharedPreferences = view_JProfile_edit.getContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        sp = view_JProfile_edit.getContext().getSharedPreferences("Signup", Context.MODE_PRIVATE);

        // Progress Bar
        progressBar = view_JProfile_edit.findViewById(R.id.spin_kit_Profile_Edit);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);
        progressBar.setVisibility(View.VISIBLE);
        Objects.requireNonNull(getActivity()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view_JProfile_edit = inflater.inflate(R.layout.fragment_profile_edit, container, false);

        // Initialization
        Init();

        // Retrieving Values
        getValues();

        // Back Button Listener
        back_JProfile_edit.setOnClickListener(view -> {
            Fragment frag_JProfile = new fragment_profile();
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragmentContainer_BottomNavigation, frag_JProfile); // give your fragment container id in first parameter
            transaction.commit();
            fragmentManager.popBackStack();

        }); // Back Listener

        name_JBProfile_edit.setOnClickListener(view -> {
            Fragment frag_JProfile_edit = new edit_name();
            Bundle bundle = new Bundle();
            bundle.putString("editname", name_JProfile_edit.getText().toString());
            frag_JProfile_edit.setArguments(bundle);
            assert getFragmentManager() != null;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer_BottomNavigation, frag_JProfile_edit, null).addToBackStack(null); // give your fragment container id in first parameter
            transaction.commit();
        });

        location_JBProfile_edit.setOnClickListener(view -> {
            Fragment frag_JProfile_edit = new edit_location();
            Bundle bundle = new Bundle();
            bundle.putString("editaddress", address_JProfile_edit);
            bundle.putString("editcountry", country_JProfile_edit);
            frag_JProfile_edit.setArguments(bundle);
            assert getFragmentManager() != null;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer_BottomNavigation, frag_JProfile_edit, null).addToBackStack(null); // give your fragment container id in first parameter
            transaction.commit();
        });

        phone_JBProfile_edit.setOnClickListener(view -> {
            Fragment frag_JProfile_edit = new edit_phone();
            Bundle bundle = new Bundle();
            bundle.putString("editphone", phone_JProfile_edit.getText().toString());
            frag_JProfile_edit.setArguments(bundle);
            assert getFragmentManager() != null;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer_BottomNavigation, frag_JProfile_edit, null).addToBackStack(null); // give your fragment container id in first parameter
            //transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
            transaction.commit();
        });

        gender_JBProfile_edit.setOnClickListener(view -> {
            Fragment frag_JProfile_gender = new edit_gender();
            Bundle bundle = new Bundle();
            bundle.putString("editgender", gender_JProfile_edit.getText().toString());
            frag_JProfile_gender.setArguments(bundle);
            assert getFragmentManager() != null;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer_BottomNavigation, frag_JProfile_gender, null).addToBackStack(null); // give your fragment container id in first parameter
            transaction.commit();
        });

        dob_JBProfile_edit.setOnClickListener(view -> {
            Fragment frag_JProfile_dob = new edit_dob();
            Bundle bundle = new Bundle();
            bundle.putString("editdob", dob_JProfile_edit.getText().toString());
            frag_JProfile_dob.setArguments(bundle);
            assert getFragmentManager() != null;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer_BottomNavigation, frag_JProfile_dob, null).addToBackStack(null); // give your fragment container id in first parameter
            transaction.commit();
        });

        password_JBProfile_edit.setOnClickListener(view -> {
            Fragment frag_JProfile_password = new edit_password();
            assert getFragmentManager() != null;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer_BottomNavigation, frag_JProfile_password, null).addToBackStack(null); // give your fragment container id in first parameter
            transaction.commit();
        });

        dp_JBProfile_edit.setOnClickListener(view -> {
            Fragment frag_JProfile_dp = new edit_dp();
            Bundle bundle = new Bundle();
            bundle.putString("editdp", dp_String);
            frag_JProfile_dp.setArguments(bundle);
            assert getFragmentManager() != null;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer_BottomNavigation, frag_JProfile_dp, null).addToBackStack(null); // give your fragment container id in first parameter
            transaction.commit();
        });


        return view_JProfile_edit;
    }

    private void getValues() {
        final String em_Profile_edit = sharedPreferences.getString("runningEmail","");

        StringRequest stringRequest_JSignup2 = new StringRequest(Request.Method.POST, "https://pinnews.000webhostapp.com/getUserProfile.php", response -> {
            response = response.trim();
            progressBar.setVisibility(View.INVISIBLE);
            Objects.requireNonNull(getActivity()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            switch (response) {
                case "Failed to connecting database!":
                    error_alert("J24P08E1M01");
                    break;
                case "Retrieving Error!":
                    error_alert("J24P08E1M02");
                    break;
                case "false":
                    error_alert("J24P08E1M03");
                    break;
                default:
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            name_JProfile_edit.setText(jsonObject.getString("name"));
                            email_JProfile_edit.setText(em_Profile_edit);
                            gender_JProfile_edit.setText(jsonObject.getString("gender"));
                            phone_JProfile_edit.setText(jsonObject.getString("phone"));
                            dob_JProfile_edit.setText(jsonObject.getString("dob"));
                            address_JProfile_edit = jsonObject.getString("address");
                            country_JProfile_edit = jsonObject.getString("country");
                            String location = address_JProfile_edit + ", " + country_JProfile_edit;
                            if (location.length() > 25) {
                                String location1 = location.substring(0, 25) + "...";
                                location_JProfile_edit.setText(location1);
                            } else {
                                location_JProfile_edit.setText(location);
                            }
                            dp_String = jsonObject.getString("dp");
                            String temp = sp.getString("Signup_with", "create");
                            assert temp != null;
                            if (temp.equals("create")) {
                                Glide.with(view_JProfile_edit.getContext()).load(Base64.decode(dp_String, Base64.DEFAULT)).into(dp_JProfile_edit);
                            } else {
                                Glide.with(view_JProfile_edit.getContext())
                                        .load(dp_String)
                                        .into(dp_JProfile_edit);
                            }
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                    } catch (Exception e) {
                        error_alert("J24P08E3M: " + e.getMessage());
                    }
                    break;
            }
        }, error -> {
            progressBar.setVisibility(View.INVISIBLE);
            Objects.requireNonNull(getActivity()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            error_alert("J24P08E2M: " + error.getMessage());
        })
        {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap_JSignup2 = new HashMap<>();
                hashMap_JSignup2.put("edit_email", em_Profile_edit);
                return hashMap_JSignup2;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(view_JProfile_edit.getContext());
        requestQueue.add(stringRequest_JSignup2);

    }

    private void error_alert(String code) {
        ConnectivityManager cm = (ConnectivityManager) view_JProfile_edit.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            AlertDialog.Builder alert = new AlertDialog.Builder(view_JProfile_edit.getContext());
            alert.setTitle("Error Found!");
            alert.setMessage("Error code: " + code + ". Report this error?");
            alert.setPositiveButton("Report", (dialog, which) -> {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"pinnews.pk@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Error Found ('" + code + " ')!!!");
                intent.setPackage("com.google.android.gm");
                startActivity(intent);
                getValues();
            });
            alert.setNegativeButton("Cancel", (dialog, which) -> getValues());
            alert.show();
        } else {
            Toast.makeText(getContext(), "Check Internet Connectivity!!!", Toast.LENGTH_SHORT).show();
        }
    }

}
