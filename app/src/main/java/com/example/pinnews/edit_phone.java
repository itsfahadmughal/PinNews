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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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

class edit_phone extends Fragment {


    // Declaration
    private ImageButton back_JEdit_phone;
    private EditText phone_JEdit_phone;
    private Button done_JEdit_phone;
    private SharedPreferences sharedPreferences;
    private ProgressBar progressBar;
    private View view_JEdit_phone;
    
    private void Init()
    {
        back_JEdit_phone = view_JEdit_phone.findViewById(R.id.back_Edit_phone);
        phone_JEdit_phone = view_JEdit_phone.findViewById(R.id.phone_Edit_phone);
        done_JEdit_phone = view_JEdit_phone.findViewById(R.id.done_Edit_phone);
        sharedPreferences = view_JEdit_phone.getContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);

        // Progress Bar
        progressBar = view_JEdit_phone.findViewById(R.id.spin_kit_edit_phone);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);
        progressBar.setVisibility(View.GONE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view_JEdit_phone = inflater.inflate(R.layout.edit_phone, container,false);

        // Initialization
        Init();

        set_phone();

        // Back Button Listener
        back_JEdit_phone.setOnClickListener(view -> {
            fragment_profile_edit frag_JEdit_name = new fragment_profile_edit();
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragmentContainer_BottomNavigation, frag_JEdit_name); // give your fragment container id in first parameter
            transaction.commit();
            fragmentManager.popBackStack();

        }); // Back Listener

        done_JEdit_phone.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            Objects.requireNonNull(getActivity()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            final String phone = phone_JEdit_phone.getText().toString();
            final String email = sharedPreferences.getString("runningEmail", "");
            StringRequest stringRequest_JEdit_phone = new StringRequest(Request.Method.POST, "https://pinnews.000webhostapp.com/updateAccount.php", response -> {
                progressBar.setVisibility(View.INVISIBLE);
                Objects.requireNonNull(getActivity()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                if (response.equals("Failed to connecting database!")) {
                    error_alert("J17P21E1M01");
                }
                else if (response.equals("Updated")) {
                    Toast.makeText(view_JEdit_phone.getContext(), "Updated!", Toast.LENGTH_SHORT).show();
                    fragment_profile_edit frag_JEdit_phone = new fragment_profile_edit();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragmentContainer_BottomNavigation, frag_JEdit_phone);
                    fragmentManager.popBackStack();
                    transaction.commit();
                } else {
                    error_alert("J17P21E1M: " + response);
                }

            }, error -> {
                progressBar.setVisibility(View.INVISIBLE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                error_alert("J17P21E2M: " + error.getMessage());
            }) {
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String, String> hashMap_JEdit_phone = new HashMap<>();
                    hashMap_JEdit_phone.put("editPhone", phone);
                    hashMap_JEdit_phone.put("editEmail", email);
                    hashMap_JEdit_phone.put("account", "phone");
                    return hashMap_JEdit_phone;

                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(view_JEdit_phone.getContext());
            requestQueue.add(stringRequest_JEdit_phone);
        });

        return view_JEdit_phone;
    }

    private void set_phone() {
        Bundle bundle = getArguments();
        assert bundle != null;
        phone_JEdit_phone.setText(bundle.getString("editphone"));
    }

    private void error_alert(String code) {
        ConnectivityManager cm = (ConnectivityManager) view_JEdit_phone.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            AlertDialog.Builder alert = new AlertDialog.Builder(view_JEdit_phone.getContext());
            alert.setTitle("Error Found!");
            alert.setMessage("Error code: " + code + ". Report this error?");
            alert.setPositiveButton("Report", (dialog, which) -> {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"pinnews.pk@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Error Found ('" + code + " ')!!!");
                intent.setPackage("com.google.android.gm");
                startActivity(intent);
                set_phone();

            });
            alert.setNegativeButton("Cancel", (dialog, which) -> set_phone());
            alert.show();
        } else {
            Toast.makeText(getContext(), "Check Internet Connectivity!!!", Toast.LENGTH_SHORT).show();
        }
    }


}
