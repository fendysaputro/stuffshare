package com.stuff.stuffshare.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.stuff.stuffshare.R;
import com.stuff.stuffshare.StuffShareApp;
import com.stuff.stuffshare.model.CampaignCategory;

import java.util.ArrayList;

public class AutoCompleteAdapter extends ArrayAdapter<CampaignCategory> implements Filterable {

    Context context;
    StuffShareApp stuffShareApp;

    public AutoCompleteAdapter(Context context, int textViewResourceId, ArrayList<CampaignCategory> campaignCategoryArrayList){
        super(context, textViewResourceId, campaignCategoryArrayList);
        this.context = context;
        stuffShareApp = (StuffShareApp) this.context.getApplicationContext();
    }

    private static class ViewHolder {
        AutoCompleteTextView autoCompleteTextView;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        CampaignCategory campaignCategory = getItem(position);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_dropdown_item_1line, null);
            holder = new ViewHolder();
            holder.autoCompleteTextView = (AutoCompleteTextView) convertView.findViewById(R.id.edNamereceive);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
            holder.autoCompleteTextView.setThreshold(1);
            holder.autoCompleteTextView.setDropDownVerticalOffset(0);
            holder.autoCompleteTextView.setText(campaignCategory.getCategory());
            holder.autoCompleteTextView.getDropDownBackground();
            holder.autoCompleteTextView.setTextColor(R.color.colorAccent);
            holder.autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.autoCompleteTextView.showDropDown();
                }
            });
            holder.autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                    holder.autoCompleteTextView.showDropDown();
                    String penerima = parent.getItemAtPosition(position).toString();
                    Log.i(stuffShareApp.TAG, "kategori " + penerima);
                }
            });
//        actv.setThreshold(1);//will start working from first character
//        actv.setDropDownVerticalOffset(1);
//        actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
//        actv.setTextColor(Color.BLACK);
//        actv.getDropDownBackground();
//        actv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                actv.showDropDown();
//            }
//        });
//
//        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
//                actv.showDropDown();
//                penerima = parent.getItemAtPosition(position).toString();
//                Log.i(stuffShareApp.TAG, "kategori " + penerima);
//            }
//        });

        return convertView;
    }
}
