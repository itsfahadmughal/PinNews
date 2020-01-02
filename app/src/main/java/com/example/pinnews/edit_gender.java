package com.example.pinnews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.github.ybq.android.spinkit.style.FadingCircle;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class edit_gender extends Fragment {

    // Declaration
    private ImageButton back_JEdit_gender;
    private RadioGroup gender_JEdit_gender;
    private RadioButton radioButton_JEdit_gender;
    private Button done_JEdit_gender;
    private SharedPreferences sharedPreferences;
    private ProgressBar progressBar;
    private View view_JEdit_gender;

    private void Init()
    {
        back_JEdit_gender = view_JEdit_gender.findViewById(R.id.back_Edit_gender);
        gender_JEdit_gender = view_JEdit_gender.findViewById(R.id.gender_Edit_gender);
        done_JEdit_gender = view_JEdit_gender.findViewById(R.id.done_Edit_gender);
        sharedPreferences = view_JEdit_gender.getContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        // Progress Bar
        progressBar = view_JEdit_gender.findViewById(R.id.spin_kit_edit_gender);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);
        progressBar.setVisibility(View.GONE);
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view_JEdit_gender = inflater.inflate(R.layout.edit_gender, container,false);

        // Initialization
        Init();

        set_gender();


        // Back Button Listener
        back_JEdit_gender.setOnClickListener(view -> {
            fragment_profile_edit frag_JEdit_gender = new fragment_profile_edit();
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragmentContainer_BottomNavigation, frag_JEdit_gender); // give your fragment container id in first parameter
            transaction.commit();
            fragmentManager.popBackStack();

        }); // Back Listener


        done_JEdit_gender.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            Objects.requireNonNull(getActivity()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            // Code...

            final String gender;
            int radioId = gender_JEdit_gender.getCheckedRadioButtonId();
            radioButton_JEdit_gender = view_JEdit_gender.findViewById(radioId);
            if(radioButton_JEdit_gender.getText().toString().equals("Male")||radioButton_JEdit_gender.getText().toString().equals("male"))
            {
                gender = "Male";
            }
            else
            {
                gender = "Female";
            }
            final String email = sharedPreferences.getString("runningEmail", "");
            StringRequest stringRequest_JEdit_gender = new StringRequest(Request.Method.POST, "https://pinnews.000webhostapp.com/updateAccount.php", response -> {
                response = response.trim();
                progressBar.setVisibility(View.INVISIBLE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.equals("Failed to connecting database!")) {
                    error_alert("J13P21E1M01");
                }
                if (response.equals("Updated")) {
                    Toast.makeText(view_JEdit_gender.getContext(), "Updated!", Toast.LENGTH_SHORT).show();
                    fragment_profile_edit frag_JEdit_gender = new fragment_profile_edit();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragmentContainer_BottomNavigation, frag_JEdit_gender);
                    fragmentManager.popBackStack();
                    transaction.commit();
                } else {
                    error_alert("J13P21E1M: " + response);
                }

            }, error -> {

                progressBar.setVisibility(View.INVISIBLE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                error_alert("J13P21E2M: " + error.getMessage());
            }) {
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String, String> hashMap_JEdit_gender = new HashMap<>();
                    hashMap_JEdit_gender.put("editGender", gender);
                    hashMap_JEdit_gender.put("editEmail", email);
                    hashMap_JEdit_gender.put("account", "gender");
                    return hashMap_JEdit_gender;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(view_JEdit_gender.getContext());
            requestQueue.add(stringRequest_JEdit_gender);
        });

        return view_JEdit_gender;
    }

    private void set_gender() {
        Bundle bundle = getArguments();
        assert bundle != null;
        bundle.getString("editgender");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if(Objects.equals(bundle.getString("editgender"), "Male") || Objects.equals(bundle.getString("editgender"), "male"))
            {
                ((RadioButton)gender_JEdit_gender.getChildAt(0)).setChecked(true);
            }
            else
            {
                ((RadioButton)gender_JEdit_gender.getChildAt(1)).setChecked(true);
            }
        }
    }

    private void error_alert(String code) {
        ConnectivityManager cm = (ConnectivityManager) view_JEdit_gender.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            AlertDialog.Builder alert = new AlertDialog.Builder(view_JEdit_gender.getContext());
            alert.setTitle("Error Found!");
            alert.setMessage("Error code: " + code + ". Report this error?");
            alert.setPositiveButton("Report", (dialog, which) -> {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"pinnews.pk@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Error Found ('" + code + " ')!!!");
                intent.setPackage("com.google.android.gm");
                startActivity(intent);
                set_gender();

            });
            alert.setNegativeButton("Cancel", (dialog, which) -> set_gender());
            alert.show();
        } else {
            Toast.makeText(getContext(), "Check Internet Connectivity!!!", Toast.LENGTH_SHORT).show();
        }
    }
}
