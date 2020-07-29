package com.stuff.stuffshare.adapter;

import android.content.Context;
import android.util.Log;
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
import com.stuff.stuffshare.model.CampaignCategory;
import com.stuff.stuffshare.model.Campaigner;
import com.stuff.stuffshare.model.RowItem;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class ListDonationAdapter extends ArrayAdapter<Campaigner> {

    Context context;

    public ListDonationAdapter(Context context, int resourceId, List<Campaigner> items){
        super(context, resourceId, items);
        this.context = context;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView txtDesc;
        TextView txtDays;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = null;
        Campaigner rowItem = getItem(position);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null){
            convertView = inflater.inflate(R.layout.list_view_donation, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.image);
            holder.txtDesc = (TextView) convertView.findViewById(R.id.txtDesc);
            holder.txtDays = (TextView) convertView.findViewById(R.id.txtDays);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
            if (rowItem.getImageCampaign().equals("")){
                holder.imageView.setImageResource(R.drawable.logo);
            } else {
                Picasso.with(context)
                        .load(rowItem.getImageCampaign())
                        .fit()
                        .into(holder.imageView);
            }
            holder.txtDesc.setText(rowItem.getDesc());
            String massDonasi = rowItem.getMasaDonasi();
            int waktuDonasi = Integer.parseInt(massDonasi);
            if (waktuDonasi <= 0){
                holder.txtDays.setText("Waktu Donasi Habis");
            } else {
                holder.txtDays.setText(waktuDonasi + " hari");
            }

        return convertView;
    }
}
