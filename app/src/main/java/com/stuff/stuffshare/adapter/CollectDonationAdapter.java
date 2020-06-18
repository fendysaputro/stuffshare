package com.stuff.stuffshare.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;
import com.stuff.stuffshare.R;
import com.stuff.stuffshare.StuffShareApp;
import com.stuff.stuffshare.model.CampaignCategory;
import com.stuff.stuffshare.model.Campaigner;
import com.stuff.stuffshare.model.CategoryBarang;

import org.json.JSONException;

import java.util.ArrayList;

public class CollectDonationAdapter extends ArrayAdapter<CategoryBarang> {

    Context context;
    StuffShareApp stuffShareApp;

    public CollectDonationAdapter(Context context, int textViewResourceId, ArrayList<CategoryBarang> categoryBarangs){
        super(context, textViewResourceId, categoryBarangs);
        this.context = context;
        stuffShareApp = (StuffShareApp) this.context.getApplicationContext();
    }

    private class ViewHolder {
        ImageView imageView;
        TextView txtTitleBarang;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = null;
        CategoryBarang campaigner = getItem(position);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_view_collect_donation_activity, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.listview_image);
            holder.txtTitleBarang = (TextView) convertView.findViewById(R.id.listview_item_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Picasso.with(context).load(campaigner.getImageId()).into(holder.imageView);
        holder.txtTitleBarang.setText(campaigner.getProductName() + " " +
                campaigner.getCount());

        return convertView;
    }
}
