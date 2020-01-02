package com.example.pinnews;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

class about_us extends Fragment {


    private ImageButton back_JAbout_Us;
    private ImageButton sohaib_Jphone, sohaib_Jfb, sohaib_Jgmail;
    private ImageButton fahad_Jphone, fahad_Jfb, fahad_Jgmail;
    private ImageButton talha_Jphone, talha_Jfb, talha_Jgmail;
    private ImageButton zuhaib_Jphone, zuhaib_Jfb, zuhaib_Jgmail;
    


    private void initialization(View view)
    {
        back_JAbout_Us = view.findViewById(R.id.back_about_us);

        sohaib_Jphone = view.findViewById(R.id.sohaib_phone);
        sohaib_Jfb = view.findViewById(R.id.sohaib_fb);
        sohaib_Jgmail = view.findViewById(R.id.sohaib_gmail);

        fahad_Jphone = view.findViewById(R.id.fahad_phone);
        fahad_Jfb = view.findViewById(R.id.fahad_fb);
        fahad_Jgmail = view.findViewById(R.id.fahad_gmail);

        talha_Jphone = view.findViewById(R.id.talha_phone);
        talha_Jfb = view.findViewById(R.id.talha_fb);
        talha_Jgmail = view.findViewById(R.id.talha_gmail);

        zuhaib_Jphone = view.findViewById(R.id.zuhaib_phone);
        zuhaib_Jfb = view.findViewById(R.id.zuhaib_fb);
        zuhaib_Jgmail = view.findViewById(R.id.zuhaib_gmail);
    }
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view_JAbout_Us =  inflater.inflate(R.layout.about_us, container, false);

        // Initialization
        initialization(view_JAbout_Us);


        // Back Button
        back_JAbout_Us.setOnClickListener(view -> {
            fragment_profile frag_JAbout_Us = new fragment_profile();
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragmentContainer_BottomNavigation, frag_JAbout_Us); // give your fragment container id in first parameter
            transaction.commit();
            fragmentManager.popBackStack();

        }); // Back Listener



        // For All Calls
        sohaib_Jphone.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "03206313745"));
            startActivity(intent);
        });
        fahad_Jphone.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "03028863134"));
            startActivity(intent);
        });
        talha_Jphone.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "03086446306"));
            startActivity(intent);
        });
        zuhaib_Jphone.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "03228002747"));
            startActivity(intent);
        });

        // For all Gmail
        sohaib_Jgmail.setOnClickListener(v -> {
            Intent intent = new Intent (Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"sohaibmughal93@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Query: ");
            intent.setPackage("com.google.android.gm");
            startActivity(intent);
        });
        fahad_Jgmail.setOnClickListener(v -> {
            Intent intent = new Intent (Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"itsfahadmughal@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Query: ");
            intent.setPackage("com.google.android.gm");
            startActivity(intent);
        });
        talha_Jgmail.setOnClickListener(v -> {
            Intent intent = new Intent (Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"talhasahi86@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Query: ");
            intent.setPackage("com.google.android.gm");
            startActivity(intent);
        });
        zuhaib_Jgmail.setOnClickListener(v -> {
            Intent intent = new Intent (Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"zuhaib.imtiaz@uogsialkot.edu.pk"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Query: ");
            intent.setPackage("com.google.android.gm");
            startActivity(intent);
        });

        // For All Fb
        sohaib_Jfb.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/100007290421682"));
                startActivity(intent);
            }
            catch(Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/sohaibmughal93")));
            }
        });
        fahad_Jfb.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/100002211889880"));
                startActivity(intent);
            }
            catch(Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/f03249646036")));
            }
        });
        talha_Jfb.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/100009159331419"));
                startActivity(intent);
            }
            catch(Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/talhasahi86")));
            }
        });
        zuhaib_Jfb.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/100001255942451"));
                startActivity(intent);
            }
            catch(Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/zuhaibbajwa90")));
            }
        });


        return view_JAbout_Us;
    }

}
