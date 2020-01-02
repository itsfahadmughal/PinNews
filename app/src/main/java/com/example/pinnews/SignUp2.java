package com.example.pinnews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.HashMap;
import java.util.Map;

public class SignUp2 extends AppCompatActivity {

    // Declaration
    private EditText phone_JSignup2;
    private EditText address_JSignup2;
    private ImageButton image_JSignup2;
    private Spinner date_JSignup2;
    private Spinner month_JSignup2;
    private Spinner year_JSignup2;
    private Spinner country_JSignup2;
    private RadioGroup gender_JSignup2;
    private String[] date_arr;
    private String[] month_arr;
    private String[] year_arr;
    private String[] country_arr;
    private Intent submit_Intent;
    private String dp_Signup2;

    // Progress Bar
    private ProgressBar progressBar;

    // Shared Preferences

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private GoogleSignInAccount account;

    private void Init()
    {
        phone_JSignup2 = findViewById(R.id.phone_Signup2);
        address_JSignup2 = findViewById(R.id.address_Signup2);
        date_JSignup2 = findViewById(R.id.date_Signup2);
        month_JSignup2 = findViewById(R.id.month_Signup2);
        year_JSignup2 = findViewById(R.id.year_Signup2);
        country_JSignup2 = findViewById(R.id.country_Signup2);
        gender_JSignup2 = findViewById(R.id.gender_Signup2);
        image_JSignup2 = findViewById(R.id.image_Signup2);
        submit_Intent = new Intent(SignUp2.this,Category.class);

        // Google Sign in

        // For Google
        account = GoogleSignIn.getLastSignedInAccount(SignUp2.this);


        // Shared Preferences
        sp = getSharedPreferences("Signup",MODE_PRIVATE);
        SharedPreferences sharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Progress Bar
        progressBar = findViewById(R.id.spin_kit);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);
        progressBar.setVisibility(View.GONE);

    }
    private void Init_Array()
    {
        date_arr = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
        month_arr = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"};
        year_arr = new String[]{"1960", "1961", "1962", "1963", "1964", "1965", "1966", "1967", "1968", "1969", "1970", "1971", "1972", "1973", "1974", "1975", "1976", "1977", "1978", "1979", "1980", "1981", "1982", "1983", "1984", "1985", "1986", "1987", "1988", "1989", "1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998", "1999", "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015"};
        country_arr = new String[]{"Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegowina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory", "Brunei Darussalam", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo", "Congo, the Democratic Republic of the", "Cook Islands", "Costa Rica", "Cote d'Ivoire", "Croatia (Hrvatska)", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland", "France", "France Metropolitan", "French Guiana", "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Heard and Mc Donald Islands", "Holy See (Vatican City State)", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran (Islamic Republic of)", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea, Democratic People's Republic of", "Korea, Republic of", "Kuwait", "Kyrgyzstan", "Lao, People's Democratic Republic", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libyan Arab Jamahiriya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia, The Former Yugoslav Republic of", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia, Federated States of", "Moldova, Republic of", "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn", "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russian Federation", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Seychelles", "Sierra Leone", "Singapore", "Slovakia (Slovak Republic)", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Georgia and the South Sandwich Islands", "Spain", "Sri Lanka", "St. Helena", "St. Pierre and Miquelon", "Sudan", "Suriname", "Svalbard and Jan Mayen Islands", "Swaziland", "Sweden", "Switzerland", "Syrian Arab Republic", "Taiwan, Province of China", "Tajikistan", "Tanzania, United Republic of", "Thailand", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "United States Minor Outlying Islands", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam", "Virgin Islands (British)", "Virgin Islands (U.S.)", "Wallis and Futuna Islands", "Western Sahara", "Yemen", "Yugoslavia", "Zambia", "Zimbabwe"};
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        //Initialiazation
        Init();

        // Initializationing Array
        Init_Array();


        // Set Spinners (Adopter)
        ArrayAdapter<String> date_adp = new ArrayAdapter<>(SignUp2.this, R.layout.spinner_item, date_arr);
        date_JSignup2.setAdapter(date_adp);
        // Changing forecolor
        date_JSignup2.setSelection(0, true);
        View v1 = date_JSignup2.getSelectedView();
        ((TextView)v1).setTextColor(Color.parseColor("#000000"));
        date_JSignup2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                //Change the selected item's text color
                ((TextView) view).setTextColor(Color.parseColor("#000000"));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        ArrayAdapter<String> month_adp = new ArrayAdapter<>(SignUp2.this, R.layout.spinner_item, month_arr);
        month_JSignup2.setAdapter(month_adp);
        // Changing forecolor
        month_JSignup2.setSelection(0, true);
        View v2 = month_JSignup2.getSelectedView();
        ((TextView)v2).setTextColor(Color.parseColor("#000000"));
        month_JSignup2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                //Change the selected item's text color
                ((TextView) view).setTextColor(Color.parseColor("#000000"));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        ArrayAdapter<String> year_adp = new ArrayAdapter<>(SignUp2.this, R.layout.spinner_item, year_arr);
        year_JSignup2.setAdapter(year_adp);
        // Changing forecolor
        year_JSignup2.setSelection(0, true);
        View v3 = year_JSignup2.getSelectedView();
        ((TextView)v3).setTextColor(Color.parseColor("#000000"));
        year_JSignup2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                //Change the selected item's text color
                ((TextView) view).setTextColor(Color.parseColor("#000000"));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        ArrayAdapter<String> country_adp = new ArrayAdapter<>(SignUp2.this, R.layout.spinner_item, country_arr);
        country_JSignup2.setAdapter(country_adp);
        // Changing forecolor
        country_JSignup2.setSelection(0, true);
        View v4 = country_JSignup2.getSelectedView();
        ((TextView)v4).setTextColor(Color.parseColor("#000000"));
        country_JSignup2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                //Change the selected item's text color
                ((TextView) view).setTextColor(Color.parseColor("#000000"));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });


        // Action Listeners

        // On Back
        image_JSignup2.setOnClickListener(view -> {
            String temp = sp.getString("Signup_with", "create");
            assert temp != null;
            if(temp.equals("create"))
            {
                Intent back_Intent = new Intent(SignUp2.this, SignUp1.class);
                startActivity(back_Intent);
                finish();
            }
            else
            {
                Intent back_Intent = new Intent(SignUp2.this, SignUp.class);
                startActivity(back_Intent);
                finish();
            }
        });

        // For Google


        // On Submit


    }  // On Create

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        progressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        Intent back_JSignup2 = new Intent(SignUp2.this, SignUp1.class);
        startActivity(back_JSignup2);
        finish();
    }

    // Validations
    private boolean validatePhone() {
        String phoneInput = phone_JSignup2.getText().toString().trim();
        if (phoneInput.isEmpty()) {
            phone_JSignup2.setError("Field can't be empty");
            return false;
        } else if (phoneInput.matches("^[0-9]{0,10}")) {
            phone_JSignup2.setError("Please enter a valid Phone Number");
            return false;
        } else
        {
            return true;
        }
    }


    public void confirmInput2(View v) {
        if (!validatePhone() || !validateAddress()) {
            return;
        }
        //Boolean b = sharedPreferences.getBoolean("verifiedPhone",false);
        createAccount2();
        /*if (b == true)
        {
            Toast.makeText(this, "a2", Toast.LENGTH_SHORT).show();
            createAccount2();
        }
        else
        {
            Toast.makeText(this, "a3", Toast.LENGTH_SHORT).show();
            Snackbar.make(v,"Verify Phone First!",Snackbar.LENGTH_LONG).setAction("Verify", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_verify = new Intent(SignUp2.this, verify_Phone.class);
                    intent_verify.putExtra("phone",phone_JSignup2.getText().toString());
                    startActivity(intent_verify);
                }
            }).show();
        }*/
    }

    private boolean validateAddress() {
        String addressInput = address_JSignup2.getText().toString().trim();

        if (addressInput.isEmpty()) {
            address_JSignup2.setError("Field can't be empty");
            return false;
        }else
        {
            return true;
        }
    }

    private void createAccount2() {


        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        String n_JSignup2 = null, e_JSignup2 = null, p_JSignup2 = null;
        String temp = sp.getString("Signup_with", "create");
        assert temp != null;
        switch (temp) {
            case "google":
                if (account != null) {
                    n_JSignup2 = account.getDisplayName();
                    e_JSignup2 = account.getEmail();
                    String photo = account.getId();
                    dp_Signup2 = "https://plus.google.com/photos/116516366860116344322/albums/profile/6618195841044461090?iso=false" + photo;
                    p_JSignup2 = "google";
                }
                break;
            case "facebook":
                Intent fb_Signup_Intent = getIntent();
                n_JSignup2 = fb_Signup_Intent.getStringExtra("namefb");
                e_JSignup2 = fb_Signup_Intent.getStringExtra("emailaddress");
                dp_Signup2 = fb_Signup_Intent.getStringExtra("dp_image");
                p_JSignup2 = "facebook";
                //Toast.makeText(SignUp2.this, dp_Signup2, Toast.LENGTH_SHORT).show();
                break;
            case "create":
                Intent next_Intent = getIntent();
                n_JSignup2 = next_Intent.getStringExtra("name");
                e_JSignup2 = next_Intent.getStringExtra("newem");
                p_JSignup2 = next_Intent.getStringExtra("password");
                dp_Signup2 = next_Intent.getStringExtra("dp_image");
                break;
        }
        final String ph_JSignup2 = phone_JSignup2.getText().toString();
        final String a_JSignup2 = address_JSignup2.getText().toString();
        final String g_JSignup2;
        int radioId = gender_JSignup2.getCheckedRadioButtonId();
        RadioButton radioButton_JSignup2 = findViewById(radioId);
        if(radioButton_JSignup2.getText().toString().equals("Male")|| radioButton_JSignup2.getText().toString().equals("male"))
        {
            g_JSignup2 = "Male";
        }
        else
        {
            g_JSignup2 = "Female";
        }
        final String c_JSignup2 = country_JSignup2.getSelectedItem().toString();
        String d_JSignup2 = date_JSignup2.getSelectedItem().toString();
        String m_JSignup2 = month_JSignup2.getSelectedItem().toString();
        String y_JSignup2 = year_JSignup2.getSelectedItem().toString();
        final String dob_JSignup2 = d_JSignup2 + "/"+ m_JSignup2 +"/"+ y_JSignup2;


        // Sending to Server
        final String finalE_JSignup = e_JSignup2;
        final String finalN_JSignup = n_JSignup2;
        final String finalP_JSignup = p_JSignup2;
        StringRequest stringRequest_JSignup2 = new StringRequest(Request.Method.POST, "https://pinnews.000webhostapp.com/signup.php", response -> {
            progressBar.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            switch (response) {
                case "Failed to connecting database!":
                    error_alert("J35P18E1M01");
                    break;
                case "Failed! Try Again.":
                    error_alert("J35P18E1M04");
                    break;
                case "Data Inserted Successfully!!!":
                    phone_JSignup2.setText("");
                    address_JSignup2.setText("");
                    country_JSignup2.setSelection(0);
                    date_JSignup2.setSelection(0);
                    month_JSignup2.setSelection(0);
                    year_JSignup2.setSelection(0);

                    // Shared Preferences
                    editor.putBoolean("is_Login", true);
                    editor.putBoolean("is_Category", false);
                    editor.putString("runningEmail", finalE_JSignup);
                    editor.putString("runningName", finalN_JSignup);
                    editor.putString("runningPicture", dp_Signup2);
                    editor.apply();
                    // Moving
                    startActivity(submit_Intent);
                    finish();
                    break;
                default:
                    error_alert("J35P18E1M00");
                    break;
            }

        }, error -> {
            progressBar.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            error_alert("J35P18E2M: "+ error.getMessage());
        })
        {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap_JSignup2 = new HashMap<>();
                hashMap_JSignup2.put("name", finalN_JSignup);
                hashMap_JSignup2.put("email", finalE_JSignup);
                hashMap_JSignup2.put("password", finalP_JSignup);
                hashMap_JSignup2.put("gender", g_JSignup2);
                hashMap_JSignup2.put("phone",ph_JSignup2);
                hashMap_JSignup2.put("dob",dob_JSignup2);
                hashMap_JSignup2.put("address",a_JSignup2);
                hashMap_JSignup2.put("country",c_JSignup2);
                hashMap_JSignup2.put("displayPicture", dp_Signup2);
                return hashMap_JSignup2;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(SignUp2.this);
        requestQueue.add(stringRequest_JSignup2);
    }



    private void error_alert(String code) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            AlertDialog.Builder alert = new AlertDialog.Builder(SignUp2.this);
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
            Toast.makeText(SignUp2.this, "Check Internet Connectivity!!!", Toast.LENGTH_SHORT).show();
        }
    }

}// Class
