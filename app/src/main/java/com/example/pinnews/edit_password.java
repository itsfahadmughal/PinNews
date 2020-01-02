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
import java.util.regex.Pattern;

class edit_password extends Fragment {


    // Declaration
    private ImageButton back_JEdit_password;
    private EditText pass1_JEdit_password;
    private EditText pass2_JEdit_password;
    private Button done_JEdit_password;
    private SharedPreferences sharedPreferences;

    private ProgressBar progressBar;
    private View view_JEdit_password;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[a-zA-Z0-9])" +      //any letter
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$");


    private void Init()
    {
        back_JEdit_password = view_JEdit_password.findViewById(R.id.back_Edit_password);
        pass1_JEdit_password = view_JEdit_password.findViewById(R.id.pass1_Edit_password);
        pass2_JEdit_password = view_JEdit_password.findViewById(R.id.pass2_Edit_password);
        done_JEdit_password = view_JEdit_password.findViewById(R.id.done_Edit_password);
        sharedPreferences = view_JEdit_password.getContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);

        // Progress Bar
        progressBar = view_JEdit_password.findViewById(R.id.spin_kit_edit_password);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);
        progressBar.setVisibility(View.GONE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view_JEdit_password = inflater.inflate(R.layout.edit_password, container,false);

        // Initialization
        Init();

        // Back Button Listener
        back_JEdit_password.setOnClickListener(view -> {
            fragment_profile_edit frag_JEdit_password = new fragment_profile_edit();
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragmentContainer_BottomNavigation, frag_JEdit_password); // give your fragment container id in first parameter
            transaction.commit();
            fragmentManager.popBackStack();

        }); // Back Listener

        done_JEdit_password.setOnClickListener(v -> confirmInput4(view_JEdit_password));

        return view_JEdit_password;
    }


    private void confirmInput4(View v) {
        if (!validatePassword()) {
            return;
        }
        createAccount(v);
    }

    private void createAccount(View view_Edit_password) {
        progressBar.setVisibility(View.VISIBLE);
        Objects.requireNonNull(getActivity()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        // Code...
        if (pass1_JEdit_password.getText().toString().equals(pass2_JEdit_password.getText().toString()))
        {
            final String password = pass1_JEdit_password.getText().toString();
            final String email = sharedPreferences.getString("runningEmail", "");
            StringRequest stringRequest_JEdit_password = new StringRequest(Request.Method.POST, "https://pinnews.000webhostapp.com/updateAccount.php", response -> {
                response = response.trim();
                progressBar.setVisibility(View.INVISIBLE);
                Objects.requireNonNull(getActivity()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.equals("Failed to connecting database!")) {
                    error_alert("J16P21E1M01");
                }
                else if (response.equals("Updated")) {
                    Toast.makeText(view_Edit_password.getContext(), "Updated!", Toast.LENGTH_SHORT).show();
                    fragment_profile_edit frag_JEdit_password = new fragment_profile_edit();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragmentContainer_BottomNavigation, frag_JEdit_password);
                    fragmentManager.popBackStack();
                    transaction.commit();
                } else {
                    error_alert("J16P21E1M: " + response);
                }

            }, error -> {

                progressBar.setVisibility(View.INVISIBLE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                error_alert("J16P21E2M: " + error.getMessage());
            }) {
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String, String> hashMap_JEdit_password = new HashMap<>();
                    hashMap_JEdit_password.put("editPassword", password);
                    hashMap_JEdit_password.put("editEmail", email);
                    hashMap_JEdit_password.put("account", "password");
                    return hashMap_JEdit_password;

                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(view_Edit_password.getContext());
            requestQueue.add(stringRequest_JEdit_password);
        }
        else
        {
            Toast.makeText(view_Edit_password.getContext(), "Password not Matched!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validatePassword() {
        String passwordInput = pass1_JEdit_password.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            pass1_JEdit_password.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            pass1_JEdit_password.setError("Password is Weak!");
            return false;
        } else {
            pass1_JEdit_password.setError(null);
            return true;
        }
    }

    private void error_alert(String code) {
        ConnectivityManager cm = (ConnectivityManager) view_JEdit_password.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            AlertDialog.Builder alert = new AlertDialog.Builder(view_JEdit_password.getContext());
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


}
