package com.example.pinnews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.material.snackbar.Snackbar;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SignIn extends AppCompatActivity {

    // Declaration
    private EditText email_JSignin, otp_JSignin;
    private ShowHidePasswordEditText password_JSignin;
    private Button login_JSignin, otp_enter_JSignin;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private SharedPreferences.Editor e;
    private ProgressBar progressBar;
    private String otp_code_JSignin;

    private void Init() {
        email_JSignin = findViewById(R.id.email_Signin);
        otp_JSignin = findViewById(R.id.otp_Signin);
        password_JSignin = findViewById(R.id.password_Signin);
        login_JSignin = findViewById(R.id.login_Signin);
        sharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Progress Bar
        progressBar = findViewById(R.id.spin_kit_login);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);
        progressBar.setVisibility(View.GONE);

        otp_enter_JSignin = findViewById(R.id.otp_enter_Signin);

        // Shared Preferences
        SharedPreferences sp = getSharedPreferences("Signup", MODE_PRIVATE);
        e = sp.edit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Initialization
        Init();


        // Action Listener
        // On Click
        login_JSignin.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            final String e_JSignin = email_JSignin.getText().toString();
            final String p_JSignin = password_JSignin.getText().toString();
            editor.putString("Signup_with", "create");
            editor.apply();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://pinnews.000webhostapp.com/emailExistenceVerification.php", response -> {
                response = response.trim();
                progressBar.setVisibility(View.INVISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                switch (response) {
                    case "Failed to connecting database!":
                        error_alert("J32P04E1M01");
                        break;
                    case "Retrieving Error!":
                        error_alert("J32P04E1M02");
                        break;
                    case "false":
                        Toast.makeText(SignIn.this, "User not registered yet!!!", Toast.LENGTH_SHORT).show();
                        break;
                    case "facebook":
                        Toast.makeText(SignIn.this, "Account Already Exist!", Toast.LENGTH_SHORT).show();
                        break;
                    case "google":
                        Toast.makeText(SignIn.this, "Account Already Exist!", Toast.LENGTH_SHORT).show();
                        break;
                    case "create":
                        progressBar.setVisibility(View.VISIBLE);
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        StringRequest stringRequest_JSignin_Category = new StringRequest(Request.Method.POST, "https://pinnews.000webhostapp.com/signin.php", response1 -> {
                            response1 = response1.trim();
                            progressBar.setVisibility(View.INVISIBLE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            switch (response1) {
                                case "Failed to connecting database!":
                                    error_alert("J32P17E1M01");
                                    break;
                                case "Retrieving Error!":
                                    error_alert("J32P17E1M02");
                                    break;
                                case "Username or Password is not correct!":
                                    Toast.makeText(SignIn.this, "Incorrect Username or password!", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    try {
                                        JSONArray jsonArray = new JSONArray(response1);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            editor.putString("runningName", jsonObject.getString("runningname"));
                                            editor.putString("runningPicture", jsonObject.getString("runningdp"));
                                            editor.putString("runningEmail", e_JSignin);
                                            editor.putBoolean("is_Category", true);
                                            editor.putBoolean("is_Login", true);
                                            editor.apply();
                                        }
                                        progressBar.setVisibility(View.VISIBLE);
                                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                        StringRequest stringRequest_JSignin_Category1 = new StringRequest(Request.Method.POST, "https://pinnews.000webhostapp.com/category_signin.php", response11 -> {
                                            response11 = response11.trim();
                                            progressBar.setVisibility(View.INVISIBLE);
                                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                            switch (response11) {
                                                case "Failed to connecting database!":
                                                    error_alert("J32P02E1M01");
                                                    break;
                                                case "Retrieving Error!":
                                                    error_alert("J32P02E1M02");
                                                    break;
                                                case "false":
                                                    error_alert("J32P02E1M03");
                                                    break;
                                                default:
                                                    String[] values = response11.split(",");
                                                    Set<String> set = new HashSet<>();
                                                    Collections.addAll(set, values);
                                                    editor.putStringSet("category_list", set);
                                                    editor.apply();
                                                    e.putString("Signup_with", "create");
                                                    e.apply();
                                                    Intent login_Intent = new Intent(SignIn.this, BottomNavigation.class);
                                                    finish();
                                                    startActivity(login_Intent);
                                                    break;
                                            }
                                        }, error -> {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                            error_alert("J32P02E2M: " + error.getMessage());
                                        }) {
                                            @Override
                                            protected Map<String, String> getParams() {
                                                HashMap<String, String> hashMap_JSignin_Category = new HashMap<>();
                                                hashMap_JSignin_Category.put("email", e_JSignin);
                                                return hashMap_JSignin_Category;

                                            }
                                        };
                                        RequestQueue requestQueue = Volley.newRequestQueue(SignIn.this);
                                        requestQueue.add(stringRequest_JSignin_Category1);

                                    } catch (JSONException e) {
                                        error_alert("J32P17E3M: " + e.getMessage());
                                    }
                                    break;
                            }
                        }, error -> {
                            progressBar.setVisibility(View.INVISIBLE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            error_alert("J32P17E2M: " + error.getMessage());
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                HashMap<String, String> hashMap_JSignin_Category = new HashMap<>();
                                hashMap_JSignin_Category.put("em", e_JSignin);
                                hashMap_JSignin_Category.put("pwd", p_JSignin);
                                return hashMap_JSignin_Category;

                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(SignIn.this);
                        requestQueue.add(stringRequest_JSignin_Category);

                        break;
                    default:
                        error_alert("J32P04E1M00");
                        break;
                }
            }, error -> {
                progressBar.setVisibility(View.INVISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                error_alert("J32P04E2M: " + error.getMessage());
            }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("email", e_JSignin);
                    return hashMap;
                }
            };

            // RequestQueue
            RequestQueue requestQueue = Volley.newRequestQueue(SignIn.this);
            requestQueue.add(stringRequest);

        });


        // On Click (Verification Code)

        otp_enter_JSignin.setOnClickListener(v -> {
            String a = otp_JSignin.getText().toString();
            String b = sharedPreferences.getString("otp_code_JSignin", "");
            if (a.equals(b)) {
                Intent intent = new Intent(SignIn.this, forget_password.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(SignIn.this, "Wrong Pin!!!", Toast.LENGTH_SHORT).show();
            }
        });

    }// On Create


    public void forgetPassword(View view) {
        String email_temp = email_JSignin.getText().toString();
        if (email_temp.equals("")) {
            Snackbar.make(view, "Enter Valid Email!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else {
            // Code Generator
            otp_code_JSignin = "" + ((int) (Math.random() * 9000) + 1000);
            progressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


            otp_JSignin.setVisibility(View.VISIBLE);
            otp_enter_JSignin.setVisibility(View.VISIBLE);
            Snackbar.make(view, "Enter Verification code. Check Email!", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            StringRequest stringRequest_forget_password_JSignin = new StringRequest(Request.Method.POST, "https://pinnews.000webhostapp.com/forget_password.php", response -> {
                response = response.trim();
                progressBar.setVisibility(View.INVISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if(response.equals("Failed to connecting database!"))
                {
                    error_alert("J32P06E1M1");
                }
                else
                {
                    editor.putString("otp_code_JSignin", response);
                    editor.apply();
                }
            }, error -> {
                progressBar.setVisibility(View.INVISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                error_alert("J32P06E2M: " + error.getMessage());
            }) {
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String, String> hashMap_forget_password = new HashMap<>();
                    hashMap_forget_password.put("email", email_temp);
                    hashMap_forget_password.put("otp_code", otp_code_JSignin);
                    return hashMap_forget_password;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(SignIn.this);
            requestQueue.add(stringRequest_forget_password_JSignin);
        }
    }

    private void error_alert(String code) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            AlertDialog.Builder alert = new AlertDialog.Builder(SignIn.this);
            alert.setTitle("Error Found!");
            alert.setMessage("Error code: " + code + ". Report this error?");
            alert.setPositiveButton("Report", (dialog, which) -> {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"pinnews.pk@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Error Found ('" + code + " ')!!!");
                intent.setPackage("com.google.android.gm");
                startActivity(intent);
                startActivity(getIntent());
                finish();
            });
            alert.setNegativeButton("Cancel", (dialog, which) -> {
                startActivity(getIntent());
                finish();
            });
            alert.show();
        } else {
            Toast.makeText(SignIn.this, "Check Internet Connectivity!!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent have_Intent = new Intent(SignIn.this, SignUp.class);
        startActivity(have_Intent);
        finish();
    }


}
