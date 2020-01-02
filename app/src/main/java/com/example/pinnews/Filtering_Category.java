package com.example.pinnews;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pinnews.model.DataItem;
import com.example.pinnews.model.SubCategoryItem;

import java.util.ArrayList;
import java.util.HashMap;

public class Filtering_Category extends AppCompatActivity {


    //ArrayList<ArrayList<SubCategoryItem>> arSubCategoryFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtering__category);

        ImageButton back_JFiltering_Category = findViewById(R.id.back_Filtering_Category);
        // Declaration
        Button done_JFiltering_Category = findViewById(R.id.done_Filtering_Category);

        back_JFiltering_Category.setOnClickListener(v -> {
            Intent intent_JFilteringCategory = new Intent(Filtering_Category.this,BottomNavigation.class);
            startActivity(intent_JFilteringCategory);
            finish();
        });

        done_JFiltering_Category.setOnClickListener(v -> {
            Intent intent_JFilteringCategory = new Intent(Filtering_Category.this,checked_Activity.class);
            startActivity(intent_JFilteringCategory);
            finish();
        });

        setupReferences();
    }


    private void setupReferences() {

        ExpandableListView lvCategory = findViewById(R.id.lvCategory);
        ArrayList<DataItem> arCategory = new ArrayList<>();
        ArrayList<SubCategoryItem> arSubCategory;
        ArrayList<HashMap<String, String>> parentItems = new ArrayList<>();
        ArrayList<ArrayList<HashMap<String, String>>> childItems = new ArrayList<>();

        // 1st
        DataItem dataItem = new DataItem();
        dataItem.setCategoryId("1");
        dataItem.setCategoryName("Sports");

        arSubCategory = new ArrayList<>();
        String[] sports_Subcategories = {"Cricket","Hockey","Football","Kabaddi","Badminton"};
        for(int i = 0; i < sports_Subcategories.length; i++) {
            SubCategoryItem subCategoryItem = new SubCategoryItem();
            subCategoryItem.setCategoryId(String.valueOf(i));
            subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            subCategoryItem.setSubCategoryName(sports_Subcategories[i]);
            arSubCategory.add(subCategoryItem);
        }
/*
        for(int i = 0; i < date.length; i++) {
            SubCategoryItem subCategoryItem = new SubCategoryItem();
            subCategoryItem.setCategoryId(String.valueOf(i));
            subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            subCategoryItem.setSubCategoryName(sports_Subcategories[i]);
            arSubCategory.add(subCategoryItem);
        }*/

        String[] date = {"2019-07-18","2019-07-17","2019-07-16"};
        dataItem.setSubCategory(arSubCategory);
        arCategory.add(dataItem);

        dataItem = new DataItem();
        dataItem.setCategoryId("2");
        dataItem.setCategoryName("Date");
        arSubCategory = new ArrayList<>();

        for(int j = 0; j < date.length; j++) {

            SubCategoryItem subCategoryItem = new SubCategoryItem();
            subCategoryItem.setCategoryId(String.valueOf(j));
            subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            subCategoryItem.setSubCategoryName(date[j]);
            arSubCategory.add(subCategoryItem);
        }
        dataItem.setSubCategory(arSubCategory);
        arCategory.add(dataItem);
/*
        dataItem = new DataItem();
        dataItem.setCategoryId("3");
        dataItem.setCategoryName("Cooking");
        arSubCategory = new ArrayList<>();
        for(int k = 1; k < 6; k++) {

            SubCategoryItem subCategoryItem = new SubCategoryItem();
            subCategoryItem.setCategoryId(String.valueOf(k));
            subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            subCategoryItem.setSubCategoryName("Cooking: "+k);
            arSubCategory.add(subCategoryItem);
        }

        dataItem.setSubCategory(arSubCategory);
        arCategory.add(dataItem);
*/
        Log.d("TAG", "setupReferences: "+ arCategory.size());

        for(DataItem data : arCategory){
//                        Log.i("Item id",item.id);
            ArrayList<HashMap<String, String>> childArrayList = new ArrayList<>();
            HashMap<String, String> mapParent = new HashMap<>();

            mapParent.put(ConstantManager.Parameter.CATEGORY_ID,data.getCategoryId());
            mapParent.put(ConstantManager.Parameter.CATEGORY_NAME,data.getCategoryName());

            int countIsChecked = 0;
            for(SubCategoryItem subCategoryItem : data.getSubCategory()) {

                HashMap<String, String> mapChild = new HashMap<>();
                mapChild.put(ConstantManager.Parameter.SUB_ID,subCategoryItem.getSubId());
                mapChild.put(ConstantManager.Parameter.SUB_CATEGORY_NAME,subCategoryItem.getSubCategoryName());
                mapChild.put(ConstantManager.Parameter.CATEGORY_ID,subCategoryItem.getCategoryId());
                mapChild.put(ConstantManager.Parameter.IS_CHECKED,subCategoryItem.getIsChecked());

                if(subCategoryItem.getIsChecked().equalsIgnoreCase(ConstantManager.CHECK_BOX_CHECKED_TRUE)) {

                    countIsChecked++;
                }
                childArrayList.add(mapChild);
            }

            if(countIsChecked == data.getSubCategory().size()) {

                data.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_TRUE);
            }else {
                data.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            }

            mapParent.put(ConstantManager.Parameter.IS_CHECKED,data.getIsChecked());
            childItems.add(childArrayList);
            parentItems.add(mapParent);

        }

        MyCategoriesExpandableListAdapter myCategoriesExpandableListAdapter = new MyCategoriesExpandableListAdapter(this, parentItems, childItems);
        lvCategory.setAdapter(myCategoriesExpandableListAdapter);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent_JFilteringCategory = new Intent(Filtering_Category.this,BottomNavigation.class);
        startActivity(intent_JFilteringCategory);
        finish();
    }
}
