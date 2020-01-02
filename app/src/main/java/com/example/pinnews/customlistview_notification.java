package com.example.pinnews;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

class customlistview_notification extends BaseAdapter {

    // Declaration
    private final String[] title_JNotification;
    private final String[] image_JNotification;
    private final String[] content_JNotification;
    private final String[] date_entry_JNotification;
    private final String[] newstag_JNotification;
    private final String[] category_JNotification;
    private final AppCompatActivity context;


    // Constructor
    customlistview_notification(AppCompatActivity context, String[] title_JNotification, String[] image_JNotification, String[] content_JNotification, String[] date_entry_JNotification, String[] newstag_JNotification, String[] category_JNotification) {
        this.context = context;
        this.title_JNotification = title_JNotification;
        this.image_JNotification = image_JNotification;
        this.content_JNotification = content_JNotification;
        this.date_entry_JNotification = date_entry_JNotification;
        this.newstag_JNotification = newstag_JNotification;
        this.category_JNotification = category_JNotification;
    }


    @Override
    public int getCount() {
        return title_JNotification.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Setting Layout
        ViewHolder viewHolder_JNotification;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.singleton_notification,parent,false);
            viewHolder_JNotification = new customlistview_notification.ViewHolder(convertView);
            convertView.setTag(viewHolder_JNotification);
        }
        else
        {
            viewHolder_JNotification = (customlistview_notification.ViewHolder) convertView.getTag();
        }

        // Code...

        // For Title

        viewHolder_JNotification.tv_title.setText(title_JNotification[position]);

        // For Image
        Glide.with(context).load(image_JNotification[position]).into(viewHolder_JNotification.iv);

        convertView.setOnClickListener(v -> {
            Intent intent_See = new Intent(v.getContext(),home_Detailed.class);

            intent_See.putExtra("title",title_JNotification[position]);
            intent_See.putExtra("image",image_JNotification[position]);
            intent_See.putExtra("content",content_JNotification[position]);
            intent_See.putExtra("date_entry",date_entry_JNotification[position]);
            intent_See.putExtra("newstag",newstag_JNotification[position]);
            intent_See.putExtra("category",category_JNotification[position]);

            context.startActivity(intent_See);
        });



        return convertView;
    }

    class ViewHolder{
        final TextView tv_title;
        final CircleImageView iv;

        ViewHolder(View v)
        {
            tv_title = v.findViewById(R.id.title_singleton_notification);
            iv = v.findViewById(R.id.image_singleton_notification);
        }
    }

}
