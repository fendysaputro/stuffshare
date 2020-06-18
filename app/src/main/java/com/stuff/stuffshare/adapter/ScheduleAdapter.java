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
import com.stuff.stuffshare.model.Campaigner;
import com.stuff.stuffshare.model.RowItem;
import com.stuff.stuffshare.model.ScheduleDonation;

import java.util.List;

public class ScheduleAdapter extends ArrayAdapter<Campaigner> {

    Context context;

    public ScheduleAdapter(Context context, int resourceId, List<Campaigner> items){
        super(context, resourceId, items);
        this.context = context;
    }

    private class ViewHolder {
        ImageView iVPenggalang;
        TextView txtSisaHari;
        TextView txtStatus;
        ImageView iVCommunity;
        TextView txtPenggalangName;
        TextView txtRangeDonation;
        TextView txtScheduleDonation;
        TextView txtCountDonation;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ScheduleAdapter.ViewHolder holder = null;
        Campaigner campaigner = getItem(position);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null){
            convertView = inflater.inflate(R.layout.list_view_schedule_donation, null);
            holder = new ScheduleAdapter.ViewHolder();
            holder.iVPenggalang = (ImageView) convertView.findViewById(R.id.imageViewPenggalang);
            holder.txtSisaHari = (TextView) convertView.findViewById(R.id.tvSisaHari);
            holder.txtStatus = (TextView) convertView.findViewById(R.id.tvStatus);
            holder.iVCommunity = (ImageView) convertView.findViewById(R.id.imageViewCommunity);
            holder.txtPenggalangName = (TextView) convertView.findViewById(R.id.tVPenggalangName);
            holder.txtRangeDonation = (TextView) convertView.findViewById(R.id.tVRangeDonation);
            holder.txtScheduleDonation = (TextView) convertView.findViewById(R.id.tVScheduleDonation);
            holder.txtCountDonation = (TextView) convertView.findViewById(R.id.tVCountDonation);
            convertView.setTag(holder);
        } else {
            holder = (ScheduleAdapter.ViewHolder) convertView.getTag();
        }

        if (campaigner.getImageCampaign().equals("")){
            holder.iVPenggalang.setImageResource(R.drawable.logo);
        } else {
            Picasso.with(context)
                    .load(campaigner.getImageCampaign())
                    .fit()
                    .into(holder.iVPenggalang);
        }

        holder.txtPenggalangName.setText(campaigner.getOrganization());
        holder.txtSisaHari.setText(campaigner.getMasaDonasi());
        holder.txtStatus.setText(campaigner.getStatusCampaign());
//        holder.iVCommunity.setImageResource(scheduleDonation.getIconCommunityId());
        Picasso.with(context)
                .load(campaigner.getImageCom())
                .into(holder.iVCommunity);
        holder.txtRangeDonation.setText(campaigner.getSisaHari());
        holder.txtScheduleDonation.setText(campaigner.getTglBuat());
        holder.txtCountDonation.setText(campaigner.getCountDonation() + " Barang");

        return convertView;
    }

}
