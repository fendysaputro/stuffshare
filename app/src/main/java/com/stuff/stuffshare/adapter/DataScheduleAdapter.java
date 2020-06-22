package com.stuff.stuffshare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;
import com.stuff.stuffshare.R;
import com.stuff.stuffshare.StuffShareApp;
import com.stuff.stuffshare.model.Campaigner;
import com.stuff.stuffshare.model.CategoryBarang;

import java.util.ArrayList;

public class DataScheduleAdapter extends ArrayAdapter<Campaigner> {

    Context context;
    StuffShareApp stuffShareApp;
    private ArrayList<Campaigner> campaigners = null;

    public DataScheduleAdapter(Context context, int textViewResourceId, ArrayList <Campaigner> campaigners){
        super(context, textViewResourceId, campaigners);
        this.context = context;
        this.campaigners = campaigners;
        stuffShareApp = (StuffShareApp) this.context.getApplicationContext();
    }

    private class ViewHolder {
        ImageView iVDocumentation;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DataScheduleAdapter.ViewHolder holder = null;
        Campaigner campaigner = getItem(position);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_view_documentation, null);
            holder = new DataScheduleAdapter.ViewHolder();
            holder.iVDocumentation = (ImageView) convertView.findViewById(R.id.grid_item_image);
            convertView.setTag(holder);
        } else {
            holder = (DataScheduleAdapter.ViewHolder) convertView.getTag();
        }

        Picasso.with(context)
                .load(campaigner.getImageCampaign())
                .fit()
                .into(holder.iVDocumentation);

        return convertView;
    }

    //    private Context mContext;
//    public DataScheduleAdapter(Context context) {
//        mContext = context;
//    }
//    public int getCount() {
//        return thumbImages.length;
//    }
//    public Object getItem(int position) {
//        return null;
//    }
//    public long getItemId(int position) {
//        return 0;
//    }
//    // create a new ImageView for each item referenced by the Adapter
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ImageView imageView = new ImageView(mContext);
//        imageView.setLayoutParams(new GridView.LayoutParams(350, 250));
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView.setPadding(5, 5, 5, 5);
//        imageView.setImageResource(thumbImages[position]);
//        return imageView;
//    }
//    // Add all our images to arraylist
//    public Integer[] thumbImages = {
//            R.drawable.img, R.drawable.img
//    };
}
