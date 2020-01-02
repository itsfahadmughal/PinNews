package com.example.pinnews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.github.ybq.android.spinkit.style.FadingCircle;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class edit_dob extends Fragment {

    // Declaration
    private ImageButton back_JEdit_dob;
    private Button done_JEdit_dob;
    private SharedPreferences sharedPreferences;
    private Spinner date_JEdit_dob;
    private Spinner month_JEdit_dob;
    private Spinner year_JEdit_dob;
    private String[] date_arr;
    private String[] month_arr;
    private String[] year_arr;
    private View view_JEdit_dob;


    private ProgressBar progressBar;


    private void Init()
    {
        back_JEdit_dob= view_JEdit_dob.findViewById(R.id.back_Edit_dob);
        done_JEdit_dob = view_JEdit_dob.findViewById(R.id.done_Edit_dob);
        date_JEdit_dob = view_JEdit_dob.findViewById(R.id.date_Edit_dob);
        month_JEdit_dob = view_JEdit_dob.findViewById(R.id.month_Edit_dob);
        year_JEdit_dob = view_JEdit_dob.findViewById(R.id.year_Edit_dob);
        date_arr = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
        month_arr = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"};
        year_arr = new String[]{"1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998", "1999", "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015"};

        sharedPreferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences("myPref", Context.MODE_PRIVATE);

        // Progress Bar
        progressBar = view_JEdit_dob.findViewById(R.id.spin_kit_edit_dob);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);
        progressBar.setVisibility(View.GONE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view_JEdit_dob = inflater.inflate(R.layout.edit_dob, container,false);

        // Initialization
        Init();
        set_Dob();

        // Back Button Listener
        back_JEdit_dob.setOnClickListener(view -> {
            fragment_profile_edit frag_JEdit_dob = new fragment_profile_edit();
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragmentContainer_BottomNavigation, frag_JEdit_dob); // give your fragment container id in first parameter
            transaction.commit();
            fragmentManager.popBackStack();

        }); // Back Listener



        // Done Button Listener

        done_JEdit_dob.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            Objects.requireNonNull(getActivity()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            // Code...
            String d_temp = date_JEdit_dob.getSelectedItem().toString();
            String m_temp = month_JEdit_dob.getSelectedItem().toString();
            String y_temp = year_JEdit_dob.getSelectedItem().toString();
            final String dob = d_temp + "/"+ m_temp +"/"+ y_temp;
            final String email = sharedPreferences.getString("runningEmail", "");
            StringRequest stringRequest_JEdit_location = new StringRequest(Request.Method.POST, "https://pinnews.000webhostapp.com/updateAccount.php", response -> {
                response = response.trim();
                progressBar.setVisibility(View.INVISIBLE);
                Objects.requireNonNull(getActivity()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.equals("Failed to connecting database!")) {
                    error_alert("J11P21E1M01");
                }
                else if (response.equals("Updated")) {
                    Toast.makeText(view_JEdit_dob.getContext(), "Updated!", Toast.LENGTH_SHORT).show();
                    fragment_profile_edit frag_JEdit_location = new fragment_profile_edit();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragmentContainer_BottomNavigation, frag_JEdit_location);
                    fragmentManager.popBackStack();
                    transaction.commit();
                } else {
                    error_alert("J11P21E1M: " + response);
                }

            }, error -> {
                progressBar.setVisibility(View.INVISIBLE);
                Objects.requireNonNull(getActivity()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                error_alert("J11P21E2M: " + error.getMessage());

            }) {
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String, String> hashMap_JEdit_dob = new HashMap<>();
                    hashMap_JEdit_dob.put("editDob", dob);
                    hashMap_JEdit_dob.put("editEmail", email);
                    hashMap_JEdit_dob.put("account", "dob");
                    return hashMap_JEdit_dob;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
            requestQueue.add(stringRequest_JEdit_location);


        });



        return view_JEdit_dob;
    }

    private void set_Dob() {
        Bundle bundle = getArguments();
        assert bundle != null;
        String dob = bundle.getString("editdob");
        assert dob != null;
        String[] items1 = dob.split("/");
        String d = items1[0];
        String m = items1[1];
        String y = items1[2];

        // Adaptors
        // Set Spinners (Adopter)
        ArrayAdapter<String> date_adp = new ArrayAdapter<>(view_JEdit_dob.getContext(), R.layout.spinner_item, date_arr);
        date_JEdit_dob.setAdapter(date_adp);
        // Changing forecolor
        date_JEdit_dob.setSelection(0, true);
        View v1 = date_JEdit_dob.getSelectedView();
        ((TextView)v1).setTextColor(Color.parseColor("#000000"));
        date_JEdit_dob.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                //Change the selected item's text color
                ((TextView) view).setTextColor(Color.parseColor("#000000"));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        ArrayAdapter<String> month_adp = new ArrayAdapter<>(view_JEdit_dob.getContext(), R.layout.spinner_item, month_arr);
        month_JEdit_dob.setAdapter(month_adp);
        // Changing forecolor
        month_JEdit_dob.setSelection(0, true);
        View v2 = month_JEdit_dob.getSelectedView();
        ((TextView)v2).setTextColor(Color.parseColor("#000000"));
        month_JEdit_dob.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                //Change the selected item's text color
                ((TextView) view).setTextColor(Color.parseColor("#000000"));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        ArrayAdapter<String> year_adp = new ArrayAdapter<>(view_JEdit_dob.getContext(), R.layout.spinner_item, year_arr);
        year_JEdit_dob.setAdapter(year_adp);
        // Changing forecolor
        year_JEdit_dob.setSelection(0, true);
        View v3 = year_JEdit_dob.getSelectedView();
        ((TextView)v3).setTextColor(Color.parseColor("#000000"));
        year_JEdit_dob.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                //Change the selected item's text color
                ((TextView) view).setTextColor(Color.parseColor("#000000"));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });


        int d_spinnerPosition = date_adp.getPosition(d);
        date_JEdit_dob.setSelection(d_spinnerPosition);

        int m_spinnerPosition = month_adp.getPosition(m);
        month_JEdit_dob.setSelection(m_spinnerPosition);

        int y_spinnerPosition = year_adp.getPosition(y);
        year_JEdit_dob.setSelection(y_spinnerPosition);

    }

    private void error_alert(String code) {
        ConnectivityManager cm = (ConnectivityManager) view_JEdit_dob.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            AlertDialog.Builder alert = new AlertDialog.Builder(view_JEdit_dob.getContext());
            alert.setTitle("Error Found!");
            alert.setMessage("Error code: " + code + ". Report this error?");
            alert.setPositiveButton("Report", (dialog, which) -> {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"pinnews.pk@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Error Found ('" + code + " ')!!!");
                intent.setPackage("com.google.android.gm");
                startActivity(intent);
                set_Dob();
            });
            alert.setNegativeButton("Cancel", (dialog, which) -> set_Dob());
            alert.show();
        } else {
            Toast.makeText(getContext(), "Check Internet Connectivity!!!", Toast.LENGTH_SHORT).show();
        }
    }

}
