package com.example.pinnews;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baoyz.widget.PullRefreshLayout;

import java.util.Objects;

class pinned_news extends Fragment {


    // Declaration
    private ImageButton back_JPinned_News;
    private String[] title_JSave;
    private RecyclerView recyclerView_JPinned_News;
    private SQLite_Helper_Pinned_News sqLite_helper_pinned_news;
    private View view_JPinned_News;
    private PullRefreshLayout pullRefreshLayout_JPinned_News;

    private void Init()
    {
        back_JPinned_News = view_JPinned_News.findViewById(R.id.back_pinned_News);
        recyclerView_JPinned_News = view_JPinned_News.findViewById(R.id.recyclerview_Pinned_News);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView_JPinned_News.setLayoutManager(layoutManager);

        RecyclerView.ItemDecoration divider = new DividerItemDecoration(view_JPinned_News.getContext(),DividerItemDecoration.HORIZONTAL);
        recyclerView_JPinned_News.addItemDecoration(divider);
        
        sqLite_helper_pinned_news = new SQLite_Helper_Pinned_News(view_JPinned_News.getContext());
        pullRefreshLayout_JPinned_News = view_JPinned_News.findViewById(R.id.swipeRefreshLayout_Pinned_News);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view_JPinned_News = inflater.inflate(R.layout.pinned_news, container,false);

        // Initialization
        Init();

        store();

        // Back Button Listener
        back_JPinned_News.setOnClickListener(view -> {
            Fragment frag_JPinned_News = new fragment_profile();
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragmentContainer_BottomNavigation, frag_JPinned_News); // give your fragment container id in first parameter
            transaction.commit();
            fragmentManager.popBackStack();

        }); // Back Listener

        // Swipe Refresh Layout

        // listen refresh event
        // start refresh
        pullRefreshLayout_JPinned_News.setOnRefreshListener(this::store);



        // Swiping RecyclerView
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                int i = sqLite_helper_pinned_news.deleteResult(title_JSave[position]);
                if (i >= 1) {
                    Toast.makeText(getContext(),"Removed", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getContext(),"Try Again!!!", Toast.LENGTH_SHORT).show();
                }
                store();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView_JPinned_News);


        return view_JPinned_News;
    }

    /*public void onStart(){
        super.onStart();
        store(view_JPinned_News);
    }*/

    private void store() {
        Cursor cursor = sqLite_helper_pinned_news.getAllData();
        if(cursor.getCount()==0)
        {
            Toast.makeText(view_JPinned_News.getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
        }
        else
        {
            int i = 0;
            title_JSave = new String[cursor.getCount()];
            String[] image_JSave = new String[cursor.getCount()];
            String[] content_JSave = new String[cursor.getCount()];
            String[] likeText_JSave = new String[cursor.getCount()];
            String[] shareText_JSave = new String[cursor.getCount()];
            String[] date_entryText_JSave = new String[cursor.getCount()];
            String[] newstagText_JSave = new String[cursor.getCount()];
            String[] categoryText_JSave = new String[cursor.getCount()];

            while (cursor.moveToNext())
            {
                title_JSave[i] = cursor.getString(0);
                image_JSave[i] = cursor.getString(1);
                content_JSave[i] = cursor.getString(2);
                likeText_JSave[i] = cursor.getString(3);
                shareText_JSave[i] = cursor.getString(4);
                date_entryText_JSave[i] = cursor.getString(5);
                newstagText_JSave[i] = cursor.getString(6);
                categoryText_JSave[i] = cursor.getString(7);
                i++;
            }
            customlistView_Pinned_News lv = new customlistView_Pinned_News((AppCompatActivity) view_JPinned_News.getContext(), title_JSave, image_JSave, content_JSave, likeText_JSave, shareText_JSave, date_entryText_JSave, newstagText_JSave, categoryText_JSave);
            recyclerView_JPinned_News.setAdapter(lv);
        }
        pullRefreshLayout_JPinned_News.setRefreshing(false);

    }

}
