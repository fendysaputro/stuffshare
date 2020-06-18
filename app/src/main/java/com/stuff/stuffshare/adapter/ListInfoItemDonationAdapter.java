package com.stuff.stuffshare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;
import com.stuff.stuffshare.R;
import com.stuff.stuffshare.model.InfoItemDonation;

import java.util.List;

public class ListInfoItemDonationAdapter extends ArrayAdapter<InfoItemDonation> {

    Context context;

    public ListInfoItemDonationAdapter(Context context, int resourceId, List<InfoItemDonation> items){
        super(context, resourceId, items);
        this.context = context;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        TextView txtDesc;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = null;
        InfoItemDonation rowItem = getItem(position);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null){
            convertView = inflater.inflate(R.layout.list_view_item_donation, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
            holder.txtDesc = (TextView) convertView.findViewById(R.id.desc);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//        holder.imageView.setImageResource(rowItem.getImageId());
        Picasso.with(context).load(rowItem.getImageId()).into(holder.imageView);
        holder.txtTitle.setText(rowItem.getTitle());
        holder.txtDesc.setText(rowItem.getDescription());

        return convertView;
    }
}
