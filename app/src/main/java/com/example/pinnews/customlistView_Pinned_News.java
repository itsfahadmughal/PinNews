package com.example.pinnews;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class customlistView_Pinned_News extends RecyclerView.Adapter<customlistView_Pinned_News.ViewHolder>{

    private final String[] title_JSave;
    private final String[] image_JSave;
    private final String[] content_JSave;
    private final String[] likeText_JSave;
    private final String[] shareText_JSave;
    private final String[] date_entryText_JSave;
    private final String[] newstagText_JSave;
    private final String[] categoryText_JSave;
    private final Context context;

    customlistView_Pinned_News(AppCompatActivity context, String[] title_jSave, String[] image_jSave, String[] content_jSave, String[] likeText_jSave, String[] shareText_jSave, String[] date_entryText_jSave, String[] newstagText_jSave, String[] categoryText_JSave) {
        this.context = context;
        this.title_JSave = title_jSave;
        this.image_JSave = image_jSave;
        this.content_JSave = content_jSave;
        this.likeText_JSave = likeText_jSave;
        this.shareText_JSave = shareText_jSave;
        this.date_entryText_JSave = date_entryText_jSave;
        this.newstagText_JSave = newstagText_jSave;
        this.categoryText_JSave = categoryText_JSave;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.singleton_pinned_news, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Setting values
        holder.title_JSingleton_Pinned_News.setText(title_JSave[position].trim());
        holder.content_JSingleton_Pinned_News.setText(content_JSave[position].trim());
        Glide.with(context).load(image_JSave[position]).into(holder.circleImageView_JPinned);


        holder.setItemClickListener((view, position1) -> {
            Intent intent_See = new Intent(view.getContext(),home_Detailed.class);
            intent_See.putExtra("title",title_JSave[position1]);
            intent_See.putExtra("image",image_JSave[position1]);
            intent_See.putExtra("content",content_JSave[position1]);
            intent_See.putExtra("likeText",likeText_JSave[position1]);
            intent_See.putExtra("shareText",shareText_JSave[position1]);
            intent_See.putExtra("date_entry",date_entryText_JSave[position1]);
            intent_See.putExtra("newstag",newstagText_JSave[position1]);
            intent_See.putExtra("category",categoryText_JSave[position1]);
            intent_See.putExtra("pin_Status",true);
            view.getContext().startActivity(intent_See);
        });
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return title_JSave.length;
    }


    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final CircleImageView circleImageView_JPinned;
        final TextView title_JSingleton_Pinned_News;
        final TextView content_JSingleton_Pinned_News;
        ViewHolder.ItemClickListener itemClickListener;

        ViewHolder(View v) {
            super(v);
            circleImageView_JPinned = v.findViewById(R.id.image_singleton_Pinned_News);
            title_JSingleton_Pinned_News = v.findViewById(R.id.title_singleton_Pinned_News);
            content_JSingleton_Pinned_News = v.findViewById(R.id.content_singleton_Pinned_News);
            v.setOnClickListener(this);

        }

        void setItemClickListener(ItemClickListener itemClickListener)
        {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.on_click_view(view, getAdapterPosition());
        }

        interface ItemClickListener {
            void on_click_view(View view, int position);
        }
    }
}
