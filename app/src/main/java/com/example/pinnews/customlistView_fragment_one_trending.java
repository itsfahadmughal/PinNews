package com.example.pinnews;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class customlistView_fragment_one_trending extends RecyclerView.Adapter<customlistView_fragment_one_trending.ViewHolder>{

    private final String[] title;
    private final String[] image;
    private final String[] content;
    private final String[] likeText;
    private final String[] shareText;
    private final String[] view;
    private final String[] date_entry;
    private final String[] newstag;
    private final String[] category;
    private final Context context;
    private final String email_Home;
    private final String[] like_temp;
    private final String[] pin_temp;
    private ViewHolder viewHolder;
    private final ProgressDialog loading;
    private final SQLite_Helper_Pinned_News sqLite_helper_pinned_news;

    private Cursor[] cursor;


    @SuppressLint("CommitPrefEdits")
    customlistView_fragment_one_trending(AppCompatActivity context, String[] title, String[] image, String[] content, String[] likesText, String[] shareText, String[] t_temp, String[] l_temp, String[] date_entry, String[] newstag, String[] category, String[] view) {
        this.context = context;
        this.title = title;
        this.image = image;
        this.content = content;
        this.likeText = likesText;
        this.shareText = shareText;
        this.category = category;
        SharedPreferences sharedPreferences = context.getSharedPreferences("myPref", Context.MODE_PRIVATE);
        email_Home = sharedPreferences.getString("runningEmail", "");
        like_temp = new String[title.length];
        pin_temp = new String[title.length];
        this.date_entry = date_entry;
        this.newstag = newstag;
        this.view = view;
        loading = new ProgressDialog(context);
        //loading.setCancelable(true);
        loading.setMessage("Fetching");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        // if no entry against user exist in like_history table.
        if (t_temp[0].equals("no") || l_temp[0].equals("no"))
        {
            for (int r = 0; r<title.length; r++)
            {
                like_temp[r] = "un-do";
            }
        }
        else
        {
            ArrayList<String> like = new ArrayList<>();
            for (int i = 0; i<title.length; i++)
            {
                for (int j = 0; j<t_temp.length; j++)
                {
                    boolean b = (title[i].equals(t_temp[j]) && l_temp[j].equals("do"));
                    if(b)
                    {
                        like.add(String.valueOf(i));
                    }
                }
            }
            for (int l = 0; l < title.length; l++)
            {
                like_temp[l]="un-do";
            }

            for (int k = 0; k < title.length; k++)
            {
                for (int p = 0; p < like.size(); p++)
                {
                    boolean b = String.valueOf(k).equals(like.get(p));
                    if (b)
                    {
                        like_temp[k]="do";
                    }
                }
            }
        }
        sqLite_helper_pinned_news = new SQLite_Helper_Pinned_News(context);

    }

    @NonNull
    @Override
    public customlistView_fragment_one_trending.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View convertView_one_trending = LayoutInflater.from(context).inflate(R.layout.singleton_home, parent, false);
        // On Click
        viewHolder = new ViewHolder(convertView_one_trending, new ViewHolder.MyClickListener() {

            @Override
            public String onSeeMore(int position) {
                loading.show();

                int nov = Integer.parseInt(view[position]) + 1;
                String no_of_view;
                if (nov == 0 || nov == 1)
                {
                    no_of_view = Integer.toString(nov).concat(" view");
                }
                else
                {
                    no_of_view = Integer.toString(nov).concat(" views");
                }
                view[position] = String.valueOf(nov);

                StringRequest stringRequest_seemore = new StringRequest(Request.Method.POST, "https://pinnews.000webhostapp.com/view.php", response -> {
                    if (response.equals("Failed to connecting database!")) {
                        error_alert("J06P23E1M01");
                    }
                    else if (response.equals("done"))
                    {
                        loading.dismiss();
                        Intent intent_See = new Intent(convertView_one_trending.getContext(), home_Detailed.class);
                        intent_See.putExtra("title", title[position]);
                        intent_See.putExtra("image", image[position]);
                        intent_See.putExtra("content", content[position]);
                        intent_See.putExtra("likeText", likeText[position]);
                        intent_See.putExtra("shareText", shareText[position]);
                        intent_See.putExtra("date_entry", date_entry[position]);
                        intent_See.putExtra("newstag", newstag[position]);
                        intent_See.putExtra("category", category[position]);
                        context.startActivity(intent_See);
                    }
                    else
                    {
                        error_alert("J06P23E1M: " + response);
                    }
                }, error -> {
                    loading.dismiss();
                    error_alert("J06P23E2M: " + error.getMessage());
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        HashMap<String, String> hashMap_JSignup2 = new HashMap<>();
                        hashMap_JSignup2.put("email", email_Home);
                        hashMap_JSignup2.put("title", title[position]);
                        hashMap_JSignup2.put("category",category[position]);
                        return hashMap_JSignup2;

                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest_seemore);

                return no_of_view;
            }

            @Override
            public String onLike(int position, View v2) {
                if (like_temp[position].equals("un-do")) {
                    v2.findViewById(R.id.likeButton_singleton_Home).setBackgroundResource(R.drawable.like);
                    int nol = Integer.parseInt(likeText[position]) + 1;
                    final String no_of_likes = Integer.toString(nol);
                    likeText[position] = no_of_likes;
                    like_temp[position] = "do";

                    String temp_like = likeText[position];


                    StringRequest stringRequest_like = new StringRequest(Request.Method.POST, "https://pinnews.000webhostapp.com/likes.php", response -> {
                        if (response.equals("Failed to connecting database!")) {
                            error_alert("J06P27E1M1");
                        }
                    }, error -> error_alert("J06P27E2M: " + error.getMessage())) {
                        @Override
                        protected Map<String, String> getParams() {
                            HashMap<String, String> hashMap_JSignup2 = new HashMap<>();
                            hashMap_JSignup2.put("email", email_Home);
                            hashMap_JSignup2.put("likes", no_of_likes);
                            hashMap_JSignup2.put("title", title[position]);
                            hashMap_JSignup2.put("like_checker", like_temp[position]);
                            hashMap_JSignup2.put("category", category[position]);
                            return hashMap_JSignup2;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(stringRequest_like);
                    return temp_like;
                } else {
                    // UNDO
                    int nol = Integer.parseInt(likeText[position]) - 1;
                    final String no_of_likes = Integer.toString(nol);
                    likeText[position] = no_of_likes;

                    like_temp[position] = "un-do";
                    String temp_like = likeText[position];

                    v2.findViewById(R.id.likeButton_singleton_Home).setBackgroundResource(R.drawable.unlike);

                    StringRequest stringRequest_like = new StringRequest(Request.Method.POST, "https://pinnews.000webhostapp.com/likes.php", response -> {
                        if (response.equals("Failed to connecting database!")) {
                            error_alert("J06P27E1M1");
                        }
                    }, error -> error_alert("J06P27E2M: " + error.getMessage())) {
                        @Override
                        protected Map<String, String> getParams() {
                            HashMap<String, String> hashMap_JSignup2 = new HashMap<>();
                            hashMap_JSignup2.put("email", email_Home);
                            hashMap_JSignup2.put("likes", no_of_likes);
                            hashMap_JSignup2.put("title", title[position]);
                            hashMap_JSignup2.put("like_checker", like_temp[position]);
                            hashMap_JSignup2.put("category", category[position]);
                            return hashMap_JSignup2;

                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(stringRequest_like);
                    return temp_like;
                }
            }

            @Override
            public String onShare(int position) {
                int nos = Integer.parseInt(shareText[position]);
                nos++;
                final String no_of_share = Integer.toString(nos);
                viewHolder.tv_share.setText(no_of_share);

                StringRequest stringRequest_share = new StringRequest(Request.Method.POST, "https://pinnews.000webhostapp.com/share.php", response -> {
                    ApplicationInfo app = convertView_one_trending.getContext().getApplicationInfo();
                    String filepath = app.publicSourceDir;
                    Intent action_Send = new Intent(Intent.ACTION_SEND);
                    Uri uri = Uri.parse(filepath);
                    action_Send.setType("text/plain");

                    action_Send.putExtra(Intent.EXTRA_STREAM, uri);
                    context.startActivity(Intent.createChooser(action_Send, "Share News"));

                }, error -> error_alert("J06P27E2M: " + error.getMessage())) {
                    @Override
                    protected Map<String, String> getParams() {
                        HashMap<String, String> hashMap_JSignup2 = new HashMap<>();
                        hashMap_JSignup2.put("email", email_Home);
                        hashMap_JSignup2.put("share", no_of_share);
                        hashMap_JSignup2.put("title", title[position]);
                        return hashMap_JSignup2;

                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest_share);
                return no_of_share;
            }

            @Override
            public void onPin(int position, View v2) {
                // Code...
                if (pin_temp[position].equals("un-do")) {
                    SQLite_Helper_Pinned_News sqLite_helper_pinned_news = new SQLite_Helper_Pinned_News(context);
                    boolean b = sqLite_helper_pinned_news.insertData(title[position], image[position], content[position], likeText[position], shareText[position], date_entry[position], newstag[position], category[position]);
                    if (b) {
                        cursor[0] = sqLite_helper_pinned_news.getData(title[position]);
                        v2.findViewById(R.id.pin_singleton_Home).setBackgroundResource(R.drawable.pin);
                        pin_temp[position]="do";
                        Snackbar snackbar;
                        snackbar = Snackbar.make(convertView_one_trending, Html.fromHtml("<b>Pinned</b>"), Snackbar.LENGTH_SHORT);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(ContextCompat.getColor(convertView_one_trending.getContext(), R.color.red));
                        TextView textView = snackBarView.findViewById(R.id.snackbar_text);
                        textView.setTextSize(18);
                        textView.setTextColor(ContextCompat.getColor(convertView_one_trending.getContext(), R.color.white));
                        snackbar.show();
                    } else {
                        v2.findViewById(R.id.pin_singleton_Home).setBackgroundResource(R.drawable.unpin);
                        Snackbar snackbar;
                        snackbar = Snackbar.make(convertView_one_trending, Html.fromHtml("<b>Error Found! Try Again.</b>"), Snackbar.LENGTH_SHORT);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(ContextCompat.getColor(convertView_one_trending.getContext(), R.color.red));
                        TextView textView = snackBarView.findViewById(R.id.snackbar_text);
                        textView.setTextSize(18);
                        textView.setTextColor(ContextCompat.getColor(convertView_one_trending.getContext(), R.color.white));
                        snackbar.show();
                    }
                } else {
                    //Unpin
                    int i = sqLite_helper_pinned_news.deleteResult(title[position]);
                    if (i >= 1) {
                        v2.findViewById(R.id.pin_singleton_Home).setBackgroundResource(R.drawable.unpin);
                        cursor[0] = sqLite_helper_pinned_news.getData(title[position]);
                        Snackbar snackbar;
                        snackbar = Snackbar.make(convertView_one_trending, Html.fromHtml("<b>Un-Pinned</b>"), Snackbar.LENGTH_SHORT);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(ContextCompat.getColor(convertView_one_trending.getContext(), R.color.red));
                        TextView textView = snackBarView.findViewById(R.id.snackbar_text);
                        textView.setTextSize(18);
                        pin_temp[position]="un-do";
                        textView.setTextColor(ContextCompat.getColor(convertView_one_trending.getContext(), R.color.white));
                        snackbar.show();
                    } else {
                        v2.findViewById(R.id.pin_singleton_Home).setBackgroundResource(R.drawable.pin);
                        Snackbar snackbar;
                        snackbar = Snackbar.make(convertView_one_trending, Html.fromHtml("<b>No News has been affected!</b>"), Snackbar.LENGTH_SHORT);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(ContextCompat.getColor(convertView_one_trending.getContext(), R.color.red));
                        TextView textView = snackBarView.findViewById(R.id.snackbar_text);
                        textView.setTextSize(18);
                        textView.setTextColor(ContextCompat.getColor(convertView_one_trending.getContext(), R.color.white));
                        snackbar.show();
                    }
                }

            }
        });

        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // Setting values
        holder.tv_title.setText(title[position].trim());

        if (like_temp[position].equals("do")) {
            holder.likeButton_JSingleton_Home.setBackgroundResource(R.drawable.like);
        } else {
            holder.likeButton_JSingleton_Home.setBackgroundResource(R.drawable.unlike);
        }

        //next work
        holder.tv_content.setText(content[position].trim());
        holder.tv_likes.setText(likeText[position]);
        holder.tv_share.setText(shareText[position]);
        if (view[position].equals("0") || view[position].equals("1"))
        {
            holder.tv_view.setText(view[position] + " view");
        }
        else
        {
            holder.tv_view.setText(view[position] + " views");
        }

        Glide.with(context).load(image[position]).into(holder.iv);

        // Pinned News (LOGO SETTING)
        cursor = new Cursor[]{sqLite_helper_pinned_news.getData(title[position])};
        if (cursor[0].getCount() == 0) {
            holder.pin_JSingleton_Home.setBackgroundResource(R.drawable.unpin);
            pin_temp[position] = "un-do";
        } else {
            holder.pin_JSingleton_Home.setBackgroundResource(R.drawable.pin);
            pin_temp[position] = "do";
        }
    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return title.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        final ViewHolder.MyClickListener listener;

        final TextView tv_title;
        final TextView tv_content;
        final TextView tv_likes;
        final TextView tv_share;
        final TextView tv_view;
        final ImageView iv;
        final ImageButton likeButton_JSingleton_Home;
        final ImageButton shareButton_JSingleton_Home;
        final ImageButton pin_JSingleton_Home;
        final View seemore_view_JHome;

        ViewHolder(View v, ViewHolder.MyClickListener listener) {
            super(v);
            tv_title = v.findViewById(R.id.title_singleton_Home);
            tv_content = v.findViewById(R.id.content_singleton_Home);
            tv_likes = v.findViewById(R.id.likeText_singleton_Home);
            tv_share = v.findViewById(R.id.shareText_singleton_Home);
            tv_view = v.findViewById(R.id.tv_view_Home);
            iv = v.findViewById(R.id.image_singleton_Home);
            seemore_view_JHome = v.findViewById(R.id.seemore_view_Home);
            shareButton_JSingleton_Home = v.findViewById(R.id.shareButton_singleton_Home);
            likeButton_JSingleton_Home = v.findViewById(R.id.likeButton_singleton_Home);
            pin_JSingleton_Home = v.findViewById(R.id.pin_singleton_Home);

            this.listener = listener;
            seemore_view_JHome.setOnClickListener(this);
            likeButton_JSingleton_Home.setOnClickListener(this);
            shareButton_JSingleton_Home.setOnClickListener(this);
            pin_JSingleton_Home.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.seemore_view_Home:
                    String s2 = listener.onSeeMore(this.getLayoutPosition());
                    tv_view.setText(s2);
                    break;
                case R.id.likeButton_singleton_Home:
                    String s = listener.onLike(this.getLayoutPosition(), view);
                    tv_likes.setText(s);
                    break;
                case R.id.shareButton_singleton_Home:
                    String s1 = listener.onShare(this.getLayoutPosition());
                    tv_share.setText(s1);
                    break;
                case R.id.pin_singleton_Home:
                    listener.onPin(this.getLayoutPosition(), view);
                    break;
                default:
                    break;
            }
        }

        // Interface

        interface MyClickListener {
            String onSeeMore(int layoutPosition);

            String onLike(int layoutPosition,View view);

            String onShare(int layoutPosition);

            void onPin(int layoutPosition,View view);
        }


    }

    private void error_alert(String code) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setTitle("Error Found!");
            alert.setMessage("Error code: " + code + ". Report this error?");
            alert.setPositiveButton("Report", (dialog, which) -> {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"pinnews.pk@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Error Found ('" + code + " ')!!!");
                intent.setPackage("com.google.android.gm");
                context.startActivity(intent);
            });
            alert.setNegativeButton("Cancel", (dialog, which) -> {

            });
            alert.show();
        } else {
            Toast.makeText(context, "Check Internet Connectivity!!!", Toast.LENGTH_SHORT).show();
        }
    }

}
