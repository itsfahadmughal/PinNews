package com.example.pinnews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.style.FadingCircle;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class forget_password extends AppCompatActivity {

    // Declaration
    private ImageButton back_JForget_password;
    private EditText pass1_JForget_password;
    private EditText pass2_JForget_password;
    private SharedPreferences sharedPreferences;
    private ProgressBar progressBar;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[a-zA-Z0-9])" +      //any letter
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$");



    private void Init()
    {
        back_JForget_password = findViewById(R.id.back_forget_password);
        pass1_JForget_password = findViewById(R.id.pass1_forget_password);
        pass2_JForget_password = findViewById(R.id.pass2_forget_password);
        sharedPreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);

        // Progress Bar
        progressBar = findViewById(R.id.spin_kit_forget_password);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        // Initialization
        Init();

        // Back Button Listener
        back_JForget_password.setOnClickListener(view -> {
            Intent intent_back = new Intent(forget_password.this, SignIn.class);
            startActivity(intent_back);
            finish();
        }); // Back Listener

    }


    public void confirmInput6(View v) {
        if (!validatePassword()) {
            return;
        }
        createAccount();
    }

    private void createAccount() {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        // Code...
        if (pass1_JForget_password.getText().toString().equals(pass2_JForget_password.getText().toString()))
        {
            final String password = pass1_JForget_password.getText().toString();
            final String email = sharedPreferences.getString("runningEmail", "");
            StringRequest stringRequest_JEdit_password = new StringRequest(Request.Method.POST, "https://pinnews.000webhostapp.com/updateAccount.php", response -> {
                progressBar.setVisibility(View.INVISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if(response.equals("Failed to connecting database!"))
                {
                    error_alert("J19P21E1M1");
                }
                else if (response.equals("Updated")) {
                    Toast.makeText(forget_password.this, "Updated!", Toast.LENGTH_SHORT).show();
                    Intent intent_recover = new Intent(forget_password.this, SignIn.class);
                    startActivity(intent_recover);
                    finish();
                } else {
                    error_alert("J19P21E1M: " + response);
                }

            }, error -> {

                progressBar.setVisibility(View.INVISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                error_alert("J19P21E2M: " + error.getMessage());
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
            RequestQueue requestQueue = Volley.newRequestQueue(forget_password.this);
            requestQueue.add(stringRequest_JEdit_password);
        }
        else
        {
            Toast.makeText(forget_password.this, "Password not Matched!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validatePassword() {
        String passwordInput = pass1_JForget_password.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            pass1_JForget_password.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            pass1_JForget_password.setError("Password is Weak!");
            return false;
        } else {
            pass1_JForget_password.setError(null);
            return true;
        }
    }

    private void error_alert(String code) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            AlertDialog.Builder alert = new AlertDialog.Builder(forget_password.this);
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
            Toast.makeText(forget_password.this, "Check Internet Connectivity!!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent_back = new Intent(forget_password.this, SignIn.class);
        startActivity(intent_back);
        finish();
    }

}
