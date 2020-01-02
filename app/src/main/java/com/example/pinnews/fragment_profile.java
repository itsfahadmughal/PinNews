package com.example.pinnews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

class fragment_profile extends Fragment {

    // Declaration
    private TextView username_JProfile;
    private TextView email_JProfile;
    private ListView listView_JProfile;
    private String[] items;
    private SharedPreferences sharedPreferences;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private SharedPreferences.Editor e;
    private CircleImageView dp_JProfile;

    private GoogleSignInClient gsc;

    // Progress bar
    private ProgressBar progressBar;
    private View view_JProfile;


    // Initialization
    @SuppressLint("CommitPrefEdits")
    private void Init()
    {
        dp_JProfile = view_JProfile.findViewById(R.id.dp_Profile);
        username_JProfile = view_JProfile.findViewById(R.id.username_Profile);
        email_JProfile = view_JProfile.findViewById(R.id.email_Profile);
        listView_JProfile = view_JProfile.findViewById(R.id.listview_Profile);
        items = new String[]{"Edit Profile", "Pinned News", "Edit Category", "Get Help", "Feedback", "Terms & Conditions", "About Us", "Log out"};

        // Shared Preferences
        sharedPreferences = view_JProfile.getContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        sp = view_JProfile.getContext().getSharedPreferences("Signup", Context.MODE_PRIVATE);
        e = sp.edit();

        // Google logout
        // Google Logout
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(view_JProfile.getContext(), gso);

        // Progress Bar
        progressBar = view_JProfile.findViewById(R.id.spin_kit_profile);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);
        progressBar.setVisibility(View.GONE);

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view_JProfile = inflater.inflate(R.layout.fragment_profile, container,false);

        // Initialization
        Init();

        // Setting name, email and image
        username_JProfile.setText(sharedPreferences.getString("runningName", ""));
        email_JProfile.setText(sharedPreferences.getString("runningEmail", ""));

        // Setting Picture into image view.
        String pic_String = sharedPreferences.getString("runningPicture", "");
        String temp = sp.getString("Signup_with", "create");
        assert temp != null;
        if(temp.equals("create"))
        {
            Glide.with(view_JProfile.getContext()).load(Base64.decode(pic_String, Base64.DEFAULT)).into(dp_JProfile);
        }
        else
        {
            Glide.with(view_JProfile.getContext())
                    .load(pic_String)
                    .into(dp_JProfile);
        }


        ArrayAdapter<String> arrayAdapter_JProfile = new ArrayAdapter<>(view_JProfile.getContext(), android.R.layout.simple_list_item_1, items);
        listView_JProfile.setAdapter(arrayAdapter_JProfile);


        // Action Listener
        listView_JProfile.setOnItemClickListener((adapterView, view, i, l) -> {
            switch (i)
            {
                case 0:
                    // Edit Profile
                    fragment_profile_edit frag_JProfile = new fragment_profile_edit();
                    assert getFragmentManager() != null;
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainer_BottomNavigation, frag_JProfile, null).addToBackStack(null); // give your fragment container id in first parameter
                    transaction.commit();
                    break;
                case 1:
                    // Saved News
                    pinned_news frag_JPinned_News = new pinned_news();
                    FragmentManager fragmentManager = ((FragmentActivity) Objects.requireNonNull(getContext())).getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragmentContainer_BottomNavigation, frag_JPinned_News,null).addToBackStack(null).commit();
                    break;
                case 2:
                    // Edit Category
                    progressBar.setVisibility(View.VISIBLE);
                    Fragment frag_JEdit_Category = new edit_category();
                    assert getFragmentManager() != null;
                    FragmentTransaction transaction1 = getFragmentManager().beginTransaction();
                    transaction1.replace(R.id.fragmentContainer_BottomNavigation, frag_JEdit_Category, null); // give your fragment container id in first parameter
                    transaction1.addToBackStack(null);
                    transaction1.commit();
                    break;
                case 3:
                    // Get Help
                    Intent intent = new Intent (Intent.ACTION_SEND);
                    intent.setType("message/rfc822");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"pinnews.pk@gmail.com"});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Need Help!!!");
                    intent.setPackage("com.google.android.gm");
                    startActivity(intent);
                    break;
                case 4:
                    // Feedback
                    if(checkConnectivity(view_JProfile)) {
                        feedback(view_JProfile);
                    }
                    else
                    {
                        Toast.makeText(view_JProfile.getContext(), "Check Internet Connectivity!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 5:
                    // Terms and Condition
                    Intent term_Intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://pinnews.000webhostapp.com/T&C.html"));
                    startActivity(term_Intent);
                    break;
                case 6:
                    Fragment about_us_fragment = new about_us();
                    assert getFragmentManager() != null;
                    FragmentTransaction transaction2 = getFragmentManager().beginTransaction();
                    transaction2.replace(R.id.fragmentContainer_BottomNavigation, about_us_fragment, null); // give your fragment container id in first parameter
                    transaction2.addToBackStack(null);
                    transaction2.commit();
                    break;
                case 7:
                    // Logout
                    if(checkConnectivity(view_JProfile))
                    {
                        String temp1 = sp.getString("Signup_with", "");

                        assert temp1 != null;
                        switch (temp1) {
                            case "create":
                                Intent logout_Intent = new Intent(view_JProfile.getContext(), SignUp.class);
                                editor.putString("runningName", "");
                                editor.putString("runningEmail", "");
                                editor.putString("runningPicture", "");
                                editor.putBoolean("is_Login", false);
                                editor.apply();
                                startActivity(logout_Intent);
                                Objects.requireNonNull(getActivity()).finish();
                                break;
                            case "facebook":
                                editor.putString("runningName", "");
                                editor.putString("runningEmail", "");
                                editor.putString("runningPicture", "");
                                editor.putBoolean("is_Login", false);
                                editor.apply();
                                Objects.requireNonNull(getActivity()).finish();
                                fbsignout(view_JProfile);
                                break;
                            case "google":
                                editor.putString("runningName", "");
                                editor.putString("runningEmail", "");
                                editor.putString("runningPicture", "");
                                editor.putBoolean("is_Login", false);
                                editor.apply();
                                gsignout(view_JProfile);
                                Objects.requireNonNull(getActivity()).finish();
                                break;
                            default:
                                Toast.makeText(view_JProfile.getContext(), "Something went wrong, Try again!!!", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                    else
                    {
                        Toast.makeText(view_JProfile.getContext(), "Check Internet Connectivity!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    Toast.makeText(view_JProfile.getContext(), "Got Wrong Input", Toast.LENGTH_SHORT).show();
                    break;
            }
        });


        return view_JProfile;
    }

    private boolean checkConnectivity(View view_JProfile) {
        ConnectivityManager connectivity = (ConnectivityManager) view_JProfile.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (NetworkInfo networkInfo : info)
                    if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }


    private void feedback(final View view) {
        final String[] feedback_JProfile = {null};
        // Alert Dialogue
        final AlertDialog.Builder alert_Profile = new AlertDialog.Builder(view.getContext());
        alert_Profile.setTitle("Enter Your Feedback");

        // EditText for input
        final EditText feedbackBox_Profile = new EditText(view.getContext());

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(60,2,60,2);
        feedbackBox_Profile.setLayoutParams(lp);
        RelativeLayout container = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams rlParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        container.setLayoutParams(rlParams);
        container.addView(feedbackBox_Profile);
        //now set view to dialog
        alert_Profile.setView(container);



        Date c = Calendar.getInstance().getTime();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);

        alert_Profile.setPositiveButton("Submit", (dialogInterface, i) -> {
            progressBar.setVisibility(View.VISIBLE);
            feedback_JProfile[0] = feedbackBox_Profile.getText().toString();
            final String email = sharedPreferences.getString("runningEmail", "");
            // Uploading Feedback
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://pinnews.000webhostapp.com/feedback.php", response -> {
                response = response.trim();
                progressBar.setVisibility(View.INVISIBLE);
                if (response.equals("Failed to connecting database!")) {
                    error_alert("J23P05E1M01");
                }
                else if (response.equals("Feedback Submitted! Thanks."))
                {
                    Toast.makeText(view.getContext(), response, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    error_alert("J23P05E1M00");
                }
            }, error -> error_alert("J23P05E2M: " + error.getMessage())
            ){
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String,String> hashMap_JProfile = new HashMap<>();
                    hashMap_JProfile.put("feedback", feedback_JProfile[0]);
                    hashMap_JProfile.put("email_feedback", email);
                    hashMap_JProfile.put("date", formattedDate);
                    return hashMap_JProfile;
                }
            };

            // RequestQueue
            RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
            requestQueue.add(stringRequest);

        });

        alert_Profile.setNegativeButton("Cancel", (dialogInterface, i) -> {

        });

        alert_Profile.show();
    }   // Function (Feedback)

    // Sign out
    private void gsignout(final View view_JProfile)
    {
        gsc.signOut().addOnCompleteListener(Objects.requireNonNull(getActivity()), task -> {
            e.putString("Signup_with", "");
            e.apply();
            Intent logout_Intent = new Intent(view_JProfile.getContext(), SignUp.class);
            startActivity(logout_Intent);
            getActivity().finish();
        });
    }
    private void fbsignout(View view_JProfile)
    {
        e.putString("Signup_with", "");
        e.apply();

        // Logout from Facebook
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if(accessToken != null) {
            LoginManager.getInstance().logOut();
            Intent logout_Intent = new Intent(view_JProfile.getContext(), SignUp.class);
            startActivity(logout_Intent);
            Objects.requireNonNull(getActivity()).finish();
        }
    }

    private void error_alert(String code) {
        ConnectivityManager cm = (ConnectivityManager) view_JProfile.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            AlertDialog.Builder alert = new AlertDialog.Builder(view_JProfile.getContext());
            alert.setTitle("Error Found!");
            alert.setMessage("Error code: " + code + ". Report this error?");
            alert.setPositiveButton("Report", (dialog, which) -> {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"pinnews.pk@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Error Found ('" + code + " ')!!!");
                intent.setPackage("com.google.android.gm");
                startActivity(intent);
                feedback(view_JProfile);
            });
            alert.setNegativeButton("Cancel", (dialog, which) -> feedback(view_JProfile));
            alert.show();
        } else {
            Toast.makeText(getContext(), "Check Internet Connectivity!!!", Toast.LENGTH_SHORT).show();
        }
    }

}
