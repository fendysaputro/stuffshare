package com.stuff.stuffshare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;
import com.stuff.stuffshare.R;
import com.stuff.stuffshare.StuffShareApp;
import com.stuff.stuffshare.model.Campaigner;
import com.stuff.stuffshare.model.RowItem;
import com.stuff.stuffshare.model.ScheduleDonation;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class ScheduleAdapter extends ArrayAdapter<Campaigner> {

    Context context;
    StuffShareApp stuffShareApp;
    String totalBarang;

    public ScheduleAdapter(Context context, int resourceId, List<Campaigner> items){
        super(context, resourceId, items);
        this.context = context;
        stuffShareApp = (StuffShareApp) this.context.getApplicationContext();
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
        RelativeLayout relativeSisaHari, relativeStatus, relativeMasaDonasi,
                relativeScheduleDonation, relativeCountDonation;
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
            holder.relativeCountDonation = (RelativeLayout) convertView.findViewById(R.id.relativeCountDonation);
            holder.relativeMasaDonasi = (RelativeLayout) convertView.findViewById(R.id.relativeMasaDonasi);
            holder.relativeScheduleDonation = (RelativeLayout) convertView.findViewById(R.id.relativeScheduleDonation);
            holder.relativeSisaHari = (RelativeLayout) convertView.findViewById(R.id.relativeSisaHari);
            holder.relativeStatus = (RelativeLayout) convertView.findViewById(R.id.relativeStatus);
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
        String massDonasi = campaigner.getMasaDonasi();
        int waktuDonasi = Integer.parseInt(massDonasi);
        if (waktuDonasi <= 0){
            holder.txtSisaHari.setText("0");
        } else {
            holder.txtSisaHari.setText(campaigner.getMasaDonasi());
        }

        try {
            JSONArray donasiBarang;
            donasiBarang = campaigner.getDonasiBarang();
            int total = 0;
            for (int i = 0; i < donasiBarang.length(); i++) {
                if (donasiBarang.length() > 1) {
                    String data = donasiBarang.getJSONObject(i).getString("qty");
                    total += total+Integer.valueOf(data);
                    stuffShareApp.setQtyBarang(String.valueOf(total));
                } else {
                    stuffShareApp.setQtyBarang(donasiBarang.getJSONObject(i).getString("qty"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.txtStatus.setText(campaigner.getStatusCampaign());
//        holder.iVCommunity.setImageResource(scheduleDonation.getIconCommunityId());
        Picasso.with(context)
                .load(campaigner.getImageCom())
                .into(holder.iVCommunity);
        holder.txtRangeDonation.setText(campaigner.getSisaHari());
        holder.txtScheduleDonation.setText(campaigner.getTglBuat());
        holder.txtCountDonation.setText(stuffShareApp.getQtyBarang() + " Barang");

        return convertView;
    }

}
