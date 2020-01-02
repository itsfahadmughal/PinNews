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

class edit_name extends Fragment {


    // Declaration
    private ImageButton back_JEdit_name;
    private EditText name_JEdit_name;
    private Button done_JEdit_name;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ProgressBar progressBar;
    private View view_JEdit_name;

    private void Init()
    {
        back_JEdit_name = view_JEdit_name.findViewById(R.id.back_Edit_name);
        name_JEdit_name = view_JEdit_name.findViewById(R.id.name_Edit_name);
        done_JEdit_name = view_JEdit_name.findViewById(R.id.done_Edit_name);

        sharedPreferences = view_JEdit_name.getContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        // Progress Bar
        progressBar = view_JEdit_name.findViewById(R.id.spin_kit_edit_name);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);
        progressBar.setVisibility(View.GONE);


    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view_JEdit_name = inflater.inflate(R.layout.edit_name, container,false);

        // Initialization
        Init();

        set_name();
        // Back Button Listener
        back_JEdit_name.setOnClickListener(view -> {
            fragment_profile_edit frag_JEdit_name = new fragment_profile_edit();
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragmentContainer_BottomNavigation, frag_JEdit_name); // give your fragment container id in first parameter
            transaction.commit();
            fragmentManager.popBackStack();

        }); // Back Listener

        // Done Button Listener

        done_JEdit_name.setOnClickListener(view -> {
            // Code...
            progressBar.setVisibility(View.VISIBLE);
            Objects.requireNonNull(getActivity()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


            final String name = name_JEdit_name.getText().toString();
            final String email = sharedPreferences.getString("runningEmail", "");
            StringRequest stringRequest_JEdit_name = new StringRequest(Request.Method.POST, "https://pinnews.000webhostapp.com/updateAccount.php", response -> {
                progressBar.setVisibility(View.INVISIBLE);
                Objects.requireNonNull(getActivity()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.equals("Failed to connecting database!")) {
                    error_alert("J15P21E1M01");
                }
                else if (response.equals("Updated")) {
                    Toast.makeText(view_JEdit_name.getContext(), "Updated!", Toast.LENGTH_SHORT).show();
                    editor.putString("runningName", name);
                    editor.apply();
                    fragment_profile_edit frag_JEdit_name = new fragment_profile_edit();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragmentContainer_BottomNavigation, frag_JEdit_name);
                    fragmentManager.popBackStack();
                    transaction.commit();
                } else {
                    error_alert("J15P21E1M: " + response);
                }

            }, error -> {
                progressBar.setVisibility(View.INVISIBLE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                error_alert("J15P21E2M: " + error.getMessage());
            }) {
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String, String> hashMap_JEdit_name = new HashMap<>();
                    hashMap_JEdit_name.put("editName", name);
                    hashMap_JEdit_name.put("editEmail", email);
                    hashMap_JEdit_name.put("account", "name");
                    return hashMap_JEdit_name;

                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(view_JEdit_name.getContext());
            requestQueue.add(stringRequest_JEdit_name);

        });
        return view_JEdit_name;
    }

    private void set_name() {
        Bundle bundle = getArguments();
        assert bundle != null;
        name_JEdit_name.setText(bundle.getString("editname"));

    }

    private void error_alert(String code) {
        ConnectivityManager cm = (ConnectivityManager) view_JEdit_name.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            AlertDialog.Builder alert = new AlertDialog.Builder(view_JEdit_name.getContext());
            alert.setTitle("Error Found!");
            alert.setMessage("Error code: " + code + ". Report this error?");
            alert.setPositiveButton("Report", (dialog, which) -> {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"pinnews.pk@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Error Found ('" + code + " ')!!!");
                intent.setPackage("com.google.android.gm");
                startActivity(intent);
                set_name();
            });
            alert.setNegativeButton("Cancel", (dialog, which) -> {
                set_name();
            });
            alert.show();
        } else {
            Toast.makeText(getContext(), "Check Internet Connectivity!!!", Toast.LENGTH_SHORT).show();
        }
    }

}
