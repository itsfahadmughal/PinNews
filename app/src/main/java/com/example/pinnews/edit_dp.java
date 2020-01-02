package com.example.pinnews;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
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
import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.style.FadingCircle;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.appcompat.app.AppCompatActivity.RESULT_OK;

class edit_dp extends Fragment {


    // Declaration
    private ImageButton back_JEdit_dp;
    private Button done_JEdit_dp;
    private Button choose_JEdit_dp;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private CircleImageView dp_JEdit_dp;
    private Bitmap bitmap_JEdit_dp;
    private final int request_codeGallery = 999;
    private View view_JEdit_dp;
    private ProgressBar progressBar;

    @SuppressLint("CommitPrefEdits")
    private void Init()
    {
        back_JEdit_dp = view_JEdit_dp.findViewById(R.id.back_Edit_dp);
        choose_JEdit_dp = view_JEdit_dp.findViewById(R.id.choose_Edit_dp);
        done_JEdit_dp = view_JEdit_dp.findViewById(R.id.done_Edit_dp);
        dp_JEdit_dp = view_JEdit_dp.findViewById(R.id.dp_Edit_dp);
        sharedPreferences = view_JEdit_dp.getContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        bitmap_JEdit_dp = null;
        
        // Progress Bar
        progressBar = view_JEdit_dp.findViewById(R.id.spin_kit_edit_dp);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);
        progressBar.setVisibility(View.GONE);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view_JEdit_dp = inflater.inflate(R.layout.edit_dp, container,false);

        // Initialization
        Init();
        set_dp();

        // Back Button Listener
        back_JEdit_dp.setOnClickListener(view -> {
            fragment_profile_edit frag_JEdit_dp = new fragment_profile_edit();
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragmentContainer_BottomNavigation, frag_JEdit_dp); // give your fragment container id in first parameter
            transaction.commit();
            fragmentManager.popBackStack();

        }); // Back Listener

        // Choose Button
        choose_JEdit_dp.setOnClickListener(view -> {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},request_codeGallery);
            // # functions (imageViewToByte) + (onRequestPermissionsResult) + (onActivityResult).
        });


        // Done Button Listener

        done_JEdit_dp.setOnClickListener(view -> {
            // Code..
            progressBar.setVisibility(View.VISIBLE);
            Objects.requireNonNull(getActivity()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            if (bitmap_JEdit_dp == null)
            {
                Toast.makeText(view_JEdit_dp.getContext(), "Select Image first!!!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                dp_JEdit_dp.buildDrawingCache();
                Bitmap bitmap_Scaled = scaleDownBitmap(bitmap_JEdit_dp, view_JEdit_dp.getContext());
                final String imageData = imageViewToByte(bitmap_Scaled);
                final String email = sharedPreferences.getString("runningEmail", "");
                StringRequest stringRequest_JEdit_name = new StringRequest(Request.Method.POST, "https://pinnews.000webhostapp.com/updateAccount.php", response -> {
                    progressBar.setVisibility(View.INVISIBLE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    if (response.equals("Failed to connecting database!")) {
                        error_alert("J12P21E1M01");
                    }
                    else if (response.equals("Updated")) {
                        Toast.makeText(view_JEdit_dp.getContext(), "Updated!", Toast.LENGTH_SHORT).show();
                        editor.putString("runningPicture", imageData);
                        editor.apply();
                        fragment_profile_edit frag_JEdit_dp = new fragment_profile_edit();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.fragmentContainer_BottomNavigation, frag_JEdit_dp);
                        fragmentManager.popBackStack();
                        transaction.commit();
                    } else {
                        error_alert("J12P21E1M: " + response);
                    }

                }, error -> {
                    progressBar.setVisibility(View.INVISIBLE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    error_alert("J12P21E2M: " + error.getMessage());
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        HashMap<String, String> hashMap_JEdit_dp = new HashMap<>();
                        hashMap_JEdit_dp.put("editDp", imageData);
                        hashMap_JEdit_dp.put("editEmail", email);
                        hashMap_JEdit_dp.put("account", "dp");
                        return hashMap_JEdit_dp;

                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(view_JEdit_dp.getContext());
                requestQueue.add(stringRequest_JEdit_name);
            }


            });
        return view_JEdit_dp;
    }

    private void set_dp() {
        Bundle bundle = getArguments();
        assert bundle != null;
        String dp_temp = bundle.getString("editdp");
        Glide.with(view_JEdit_dp.getContext()).load(Base64.decode(dp_temp, Base64.DEFAULT)).into(dp_JEdit_dp);

    }

    // Scaling
    private static Bitmap scaleDownBitmap(Bitmap photo, Context context) {
        final float densityMultiplier = context.getResources().getDisplayMetrics().density;
        int h= (int) (150 *densityMultiplier);
        int w= (int) (h * photo.getWidth()/((double) photo.getHeight()));
        photo=Bitmap.createScaledBitmap(photo, w, h, true);
        return photo;
    }

    // Upload Image # 1
    private String imageViewToByte(Bitmap imageBitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    // Upload Image # 2
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == request_codeGallery)
        {
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,request_codeGallery);
            }
            else
            {
                Toast.makeText(view_JEdit_dp.getContext(), "You don't have permission to excess file location", Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // Upload Image # 3
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == request_codeGallery && resultCode == RESULT_OK && data != null)
        {
            Uri uri = data.getData();
            try {
                assert uri != null;
                InputStream inputStream = Objects.requireNonNull(getActivity()).getApplicationContext().getContentResolver().openInputStream(uri);
                bitmap_JEdit_dp = BitmapFactory.decodeStream(inputStream);
                dp_JEdit_dp.setImageBitmap(bitmap_JEdit_dp);
            } catch (FileNotFoundException e) {
                progressBar.setVisibility(View.INVISIBLE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                error_alert("J12P26E3M: " + e.getMessage());
            }
            return;
        }
        else
        {
            Toast.makeText(view_JEdit_dp.getContext(), "Couldn't Upload Image!", Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void error_alert(String code) {
        ConnectivityManager cm = (ConnectivityManager) view_JEdit_dp.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            AlertDialog.Builder alert = new AlertDialog.Builder(view_JEdit_dp.getContext());
            alert.setTitle("Error Found!");
            alert.setMessage("Error code: " + code + ". Report this error?");
            alert.setPositiveButton("Report", (dialog, which) -> {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"pinnews.pk@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Error Found ('" + code + " ')!!!");
                intent.setPackage("com.google.android.gm");
                startActivity(intent);
                set_dp();

            });
            alert.setNegativeButton("Cancel", (dialog, which) -> set_dp());
            alert.show();
        } else {
            Toast.makeText(getContext(), "Check Internet Connectivity!!!", Toast.LENGTH_SHORT).show();
        }
    }

}
