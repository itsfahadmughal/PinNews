package com.example.pinnews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
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
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import org.json.JSONException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    // Declaration
    private EditText name_JSignup;
    private EditText otp_JSignup;
    private EditText email_JSignup;
    private ShowHidePasswordEditText password_JSignup;
    private Button have_JSignup;
    private Button g_JSignup;
    private Button f_JSignup;
    private Button otp_button_JSignup;

    private GoogleSignInClient gsc;

    private int RC_SIGN_IN;
    private SharedPreferences sp;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor e;
    private SharedPreferences.Editor editor;
    private String otp_code_JSignup;


    // For facebook
    private LoginButton fb_JSignup;
    private CallbackManager callbackManager;
    private ProgressBar progressBar;

    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s]*$");
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[a-zA-Z0-9])" +      //any letter
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$");



    private void Init() {
        name_JSignup = findViewById(R.id.name_Signup);
        email_JSignup = findViewById(R.id.email_Signup);
        password_JSignup = findViewById(R.id.password_Signup);
        have_JSignup = findViewById(R.id.have_Signup);
        fb_JSignup = findViewById(R.id.fb_Signup);
        g_JSignup = findViewById(R.id.g_Signup);
        f_JSignup = findViewById(R.id.f_Signup);
        otp_JSignup = findViewById(R.id.otp_Signup);
        otp_button_JSignup = findViewById(R.id.otp_button_Signup);

        // Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(SignUp.this, gso);

        // Shared Preferences
        sharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        sp = getSharedPreferences("Signup", MODE_PRIVATE);
        e = sp.edit();


        // Facebook sign in
        callbackManager = CallbackManager.Factory.create();

        // Progress Bar
        progressBar = findViewById(R.id.spin_kit_signup);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        try {
            @SuppressLint("PackageManagerGetSignatures") PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.d("key hash: ", hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            error_alert("J33P25E0M: " + e.getMessage());
        } catch (Exception e) {
            error_alert("J33P25E0M: " + e.getMessage());
        }

        // Facebook (Before setContentView
        //FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_sign_up);

        // Initialization
        Init();
        fbsignout();
        // Action Listeners\
        fb_JSignup.setPermissions("email");
        // Create Account


        // SignUp using Facebook
        fb_JSignup.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                progressBar.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                e.putString("Signup_with", "facebook");
                e.apply();

                final Intent fb_Signup_Intent = new Intent(SignUp.this, SignUp2.class);
                Profile profile = Profile.getCurrentProfile();
                fb_Signup_Intent.putExtra("namefb", profile.getName());
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        (object, response) -> {
                            try {
                                String s = object.getString("email");

                                // Fetch Image URL
                                String id = object.getString("id");
                                String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

                                // Check if exist
                                StringRequest stringRequest_JSignup_check = new StringRequest(Request.Method.POST, "https://pinnews.000webhostapp.com/emailExistenceVerification.php", response1 -> {
                                    response1 = response1.trim();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    try {
                                        switch (response1) {
                                            case "Failed to connecting database!":
                                                error_alert("J33P04E1M01");
                                                break;
                                            case "Retrieving Error!":
                                                error_alert("J33P04E1M02");
                                                break;
                                            case "false":
                                                fb_Signup_Intent.putExtra("dp_image", image_url);
                                                fb_Signup_Intent.putExtra("emailaddress", s);
                                                startActivity(fb_Signup_Intent);
                                                finish();
                                                break;
                                            case "google":
                                                Toast.makeText(SignUp.this, "Account Already Exist!", Toast.LENGTH_SHORT).show();
                                                break;
                                            case "create":
                                                Toast.makeText(SignUp.this, "Account Already Exist!", Toast.LENGTH_SHORT).show();
                                                break;
                                            case "facebook":
                                                // Log in
                                                Intent fb_JSignup_Intent = new Intent(SignUp.this, BottomNavigation.class);
                                                editor.putString("runningName", profile.getName());
                                                editor.putString("runningPicture", image_url);
                                                editor.putString("runningEmail", s);
                                                editor.putBoolean("is_Login", true);
                                                editor.putBoolean("is_Category", true);
                                                editor.apply();

                                                StringRequest stringRequest_JSignin_Category = new StringRequest(Request.Method.POST, "https://pinnews.000webhostapp.com/category_signin.php", response11 -> {
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                    switch (response11) {
                                                        case "Failed to connecting database!":
                                                            error_alert("J33P02E1M01");
                                                            break;
                                                        case "Retrieving Error!":
                                                            error_alert("J33P02E1M02");
                                                            break;
                                                        case "false":
                                                            error_alert("J33P02E1M03");
                                                            break;
                                                        default:
                                                            try {
                                                                String[] values = response11.split(",");
                                                                Set<String> set = new HashSet<>();
                                                                Collections.addAll(set, values);
                                                                editor.putStringSet("category_list", set);
                                                                editor.apply();
                                                                e.putString("Signup_with", "facebook");
                                                                e.apply();
                                                                startActivity(fb_JSignup_Intent);
                                                                finish();
                                                            } catch (Exception e) {
                                                                error_alert("J33P02E3M:" + e.getMessage());
                                                            }
                                                            break;
                                                    }
                                                }, error -> {
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                    error_alert("J33P02E2M: " + error.getMessage());
                                                }) {
                                                    @Override
                                                    protected Map<String, String> getParams() {
                                                        HashMap<String, String> hashMap_JSignin_Category = new HashMap<>();
                                                        hashMap_JSignin_Category.put("email", s);
                                                        return hashMap_JSignin_Category;

                                                    }
                                                };
                                                RequestQueue requestQueue = Volley.newRequestQueue(SignUp.this);
                                                requestQueue.add(stringRequest_JSignin_Category);
                                                break;
                                            default:
                                                error_alert("J33P04E1M00");
                                                break;
                                        }
                                    } catch (Exception e) {
                                        error_alert("J33P04E2M: " + e.getMessage());
                                    }
                                }, error -> {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    error_alert("J33P04E2M: " + error.getMessage());
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() {
                                        HashMap<String, String> hashMap_JSignup = new HashMap<>();
                                        hashMap_JSignup.put("email", s);
                                        return hashMap_JSignup;

                                    }
                                };
                                RequestQueue requestQueue = Volley.newRequestQueue(SignUp.this);
                                requestQueue.add(stringRequest_JSignup_check);
                            } catch (JSONException e1) {
                                progressBar.setVisibility(View.INVISIBLE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                error_alert("J33P24E2M: " + e1.getMessage());
                            }
                        });
                Bundle bundle = new Bundle();
                bundle.putString("fields", "name,email");
                request.setParameters(bundle);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                progressBar.setVisibility(View.INVISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Toast.makeText(SignUp.this, "Cancelled LogIn", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                progressBar.setVisibility(View.INVISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                error_alert("J33P24E4M: " + error.getMessage());
            }
        });

        // Signup using Google
        g_JSignup.setOnClickListener(view -> {
            //Code
            progressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            Intent google_Signup_Intent = gsc.getSignInIntent();
            e.putString("Signup_with", "google");
            e.apply();
            startActivityForResult(google_Signup_Intent, RC_SIGN_IN);
        });
        // Signin
        have_JSignup.setOnClickListener(view -> {
            //Code
            Intent have_Intent = new Intent(SignUp.this, SignIn.class);
            startActivity(have_Intent);
            finish();
        });

        // Verify button
        otp_button_JSignup.setOnClickListener(v -> {
            String a = otp_JSignup.getText().toString();
            String b = sharedPreferences.getString("otp_code_JSignup", "");
            if (a.equals(b)) {
                e.putString("Signup_with", "create");
                e.apply();

                // Intent
                Intent create_Intent = new Intent(SignUp.this, SignUp1.class);
                create_Intent.putExtra("name", name_JSignup.getText().toString());
                create_Intent.putExtra("newem", email_JSignup.getText().toString());
                create_Intent.putExtra("password", password_JSignup.getText().toString());

                name_JSignup.setText("");
                email_JSignup.setText("");
                password_JSignup.setText("");
                startActivity(create_Intent);
                finish();
            } else {
                Toast.makeText(SignUp.this, "Wrong Pin!!!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String temp1 = sp.getString("Signup_with", "facebook");
        assert temp1 != null;
        if (temp1.equals("google")) {
            // Google
            super.onActivityResult(requestCode, resultCode, data);
            // Result returned from launching the intent of google.getSigninintent()
            if (requestCode == RC_SIGN_IN) {
                // The task returned from his calling is always complete, no need to take any action listener
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
            }
            //  Toast.makeText(SignUp.this, "Failed to choose OnActivityResult2", Toast.LENGTH_SHORT).show();

        } else {
            // Facebook
            super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);
            // Toast.makeText(SignUp.this, "Failed to choose OnActivityResult1", Toast.LENGTH_SHORT).show();
        }
    }

    //Google
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            // Signin Successfully
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Move home Activity

            assert account != null;
            String email = account.getEmail();
            // Check if exist
            StringRequest stringRequest_JSignup_check = new StringRequest(Request.Method.POST, "https://pinnews.000webhostapp.com/emailExistenceVerification.php", response -> {
                response = response.trim();
                progressBar.setVisibility(View.INVISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                try {
                    switch (response) {
                        case "Failed to connecting database!":
                            error_alert("J33P04E1M01");
                            break;
                        case "Retrieving Error!":
                            error_alert("J33P04E1M02");
                            break;
                        case "false":
                            startActivity(new Intent(SignUp.this, SignUp2.class));
                            finish();
                            break;
                        case "facebook":
                            Toast.makeText(SignUp.this, "Account Already Exist!", Toast.LENGTH_SHORT).show();
                            break;
                        case "create":
                            Toast.makeText(SignUp.this, "Account Already Exist!", Toast.LENGTH_SHORT).show();
                            break;
                        case "google":
                            // Log in
                            editor.putString("runningName", account.getDisplayName());
                            String photo_url = "https://plus.google.com/photos/116516366860116344322/albums/profile/6618195841044461090?iso=false";
                            editor.putString("runningPicture", photo_url);
                            editor.putString("runningEmail", account.getEmail());
                            editor.putBoolean("is_Category", true);
                            editor.putBoolean("is_Login", true);
                            editor.apply();

                            StringRequest stringRequest_JSignin_Category = new StringRequest(Request.Method.POST, "https://pinnews.000webhostapp.com/category_signin.php", response1 -> {
                                response1 = response1.trim();
                                progressBar.setVisibility(View.INVISIBLE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                switch (response1) {
                                    case "Failed to connecting database!":
                                        error_alert("J33P02E1M01");
                                        break;
                                    case "Retrieving Error!":
                                        error_alert("J33P02E1M02");;
                                        break;
                                    case "false":
                                        error_alert("J33P02E1M03");
                                        break;
                                    default:
                                        try {
                                            String[] values = response1.split(",");
                                            Set<String> set = new HashSet<>();
                                            Collections.addAll(set, values);
                                            editor.putStringSet("category_list", set);
                                            editor.apply();
                                            e.putString("Signup_with", "google");
                                            e.apply();

                                            startActivity(new Intent(SignUp.this, BottomNavigation.class));
                                            finish();
                                        } catch (Exception e) {
                                            error_alert("J33P02E3M:" + e.getMessage());
                                        }
                                        break;
                                }

                            }, error -> {
                                progressBar.setVisibility(View.INVISIBLE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                error_alert("J33P02E2M: " + error.getMessage());
                            }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    HashMap<String, String> hashMap_JSignin_Category = new HashMap<>();
                                    hashMap_JSignin_Category.put("email", email);
                                    return hashMap_JSignin_Category;

                                }
                            };
                            RequestQueue requestQueue = Volley.newRequestQueue(SignUp.this);
                            requestQueue.add(stringRequest_JSignin_Category);
                            break;
                        default:
                            error_alert("J33P04E1M00");
                            break;
                    }
                } catch (Exception e) {
                    progressBar.setVisibility(View.INVISIBLE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    error_alert("J33P04E2M: " + e.getMessage());
                }
            }, error -> {
                progressBar.setVisibility(View.INVISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                error_alert("J33P04E1M: " + error.getMessage());
            }) {
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String, String> hashMap_JSignup = new HashMap<>();
                    hashMap_JSignup.put("email", email);
                    return hashMap_JSignup;

                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(SignUp.this);
            requestQueue.add(stringRequest_JSignup_check);


        } catch (ApiException e) {
            // If failed
            Log.w("Google Sign In Error", "Sign In Result: Failed Code: " + e.getStatusCode());
            progressBar.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Toast.makeText(SignUp.this, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void fb_call(View view) {
        if (view == f_JSignup) {
            fb_JSignup.performClick();
        }
    }

    // On Start
/*
    @Override
    protected void onStart() {
        // Check whether he is signin or not
        GoogleSignInAccount account =  GoogleSignIn.getLastSignedInAccount(SignUp.this);
        if(account != null)
        {
            finish();
            // Move to Home Activity
            // startActivity(new Intent(SignUp.this, SignUp2.class));
        }
        super.onStart();
    }*/

    // fb sign out
    private void fbsignout() {

        e.putString("Signup_with", "");
        e.apply();

        // Logout from Facebook
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            LoginManager.getInstance().logOut();
            Intent logout_Intent = new Intent(SignUp.this, SignUp.class);
            startActivity(logout_Intent);
            finish();
        }
    }


    // Validations
    private boolean validateEmail() {
        String emailInput = email_JSignup.getText().toString().trim();

        if (emailInput.isEmpty()) {
            email_JSignup.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            email_JSignup.setError("Please enter a valid email address");
            return false;
        } else {
            email_JSignup.setError(null);
            return true;
        }
    }

    private boolean validateUsername() {
        String usernameInput = name_JSignup.getText().toString().trim();

        if (usernameInput.isEmpty()) {
            name_JSignup.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > 25) {
            name_JSignup.setError("Username too long");
            return false;
        } else if (!NAME_PATTERN.matcher(usernameInput).matches()) {
            name_JSignup.setError("Only Characters are required");
            return false;
        } else {
            name_JSignup.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = password_JSignup.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            password_JSignup.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            password_JSignup.setError("Password is Weak!");
            return false;
        } else {
            password_JSignup.setError(null);
            return true;
        }
    }

    public void confirmInput(View v) {
        if (!validateEmail() | !validateUsername() | !validatePassword()) {
            return;
        }
        createAccount();
    }

    private void otpAuthentication() {
        fb_JSignup.setVisibility(View.GONE);
        f_JSignup.setVisibility(View.GONE);
        g_JSignup.setVisibility(View.GONE);
        have_JSignup.setVisibility(View.GONE);

        // Code Generator
        otp_code_JSignup = "" + ((int) (Math.random() * 9000) + 1000);
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        otp_JSignup.setVisibility(View.VISIBLE);
        otp_button_JSignup.setVisibility(View.VISIBLE);
        View view = findViewById(android.R.id.content);
        Snackbar.make(view, "Enter Verification code. Check Email!", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
        StringRequest stringRequest_forget_password_JSignin = new StringRequest(Request.Method.POST, "https://pinnews.000webhostapp.com/forget_password.php", response -> {
            response = response.trim();
            progressBar.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            if(response.equals("Failed to connecting database!"))
            {
                error_alert("J33P06E1M1");
            }
            else
            {
                editor.putString("otp_code_JSignup", response);
                editor.apply();
            }
        }, error -> {
            progressBar.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            error_alert("J33P06E2M: " + error.getMessage());
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap_forget_password = new HashMap<>();
                hashMap_forget_password.put("email", email_JSignup.getText().toString());
                hashMap_forget_password.put("otp_code", otp_code_JSignup);
                return hashMap_forget_password;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(SignUp.this);
        requestQueue.add(stringRequest_forget_password_JSignin);
    }

    private void createAccount() {
        // Code
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        // Fetching Values
        String e_JSignup = email_JSignup.getText().toString();


        StringRequest stringRequest_JSignup_check = new StringRequest(Request.Method.POST, "https://pinnews.000webhostapp.com/emailExistenceVerification.php", response -> {
            response = response.trim();
            progressBar.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            try {
                switch (response) {
                    case "Failed to connecting database!":
                        error_alert("J33P04E1M01");
                        break;
                    case "Retrieving Error!":
                        error_alert("J33P04E1M02");
                        break;
                    case "false":
                        otpAuthentication();
                        break;
                    default:
                        Toast.makeText(SignUp.this, "Email Already Exist!", Toast.LENGTH_SHORT).show();
                        break;
                }
            } catch (Exception e) {
                error_alert("J33P04E3M: " + e.getMessage());
            }
        }, error -> {
            progressBar.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            error_alert("J33P04E2M: " + error.getMessage());
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap_JSignup2 = new HashMap<>();
                hashMap_JSignup2.put("email", e_JSignup);
                return hashMap_JSignup2;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(SignUp.this);
        requestQueue.add(stringRequest_JSignup_check);

    }

    private void error_alert(String code) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            AlertDialog.Builder alert = new AlertDialog.Builder(SignUp.this);
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
            Toast.makeText(SignUp.this, "Check Internet Connectivity!!!", Toast.LENGTH_SHORT).show();
        }
    }

}