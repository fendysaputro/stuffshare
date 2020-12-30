package com.stuff.stuffshare.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.squareup.picasso.Picasso;
import com.stuff.stuffshare.R;
import com.stuff.stuffshare.StuffShareApp;
import com.stuff.stuffshare.fragment.ConfirmationFragment;
import com.stuff.stuffshare.model.CategoryBarang;
import com.stuff.stuffshare.model.Donation;
import com.stuff.stuffshare.util.AppUtils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

import static com.stuff.stuffshare.MainActivity.ShowFragment;

public class MyDonationAdapter extends ArrayAdapter<Donation> {
    Context context;
    StuffShareApp stuffShareApp;
    AppUtils appUtils;
    private ArrayList<Donation> donations = null;

    public MyDonationAdapter(Context context, int resourceId, ArrayList<Donation> donations){
        super(context, resourceId, donations);
        this.context = context;
        this.donations = donations;
        stuffShareApp = (StuffShareApp) this.context.getApplicationContext();
        appUtils = new AppUtils();
    }

    private class ViewHolder {
        ImageView imageView;
        TextView txtDesc;
        Button statusBtn;
        TextView txtDate;
        TextView jmlUang;
        TextView jmlBarang;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public Donation getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = null;
//        Donation donation = getItem(position);
        Donation donation = (Donation) donations.get(position);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null){
            convertView = inflater.inflate(R.layout.list_view_my_donation, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageViewMyDonation);
            holder.txtDesc = (TextView) convertView.findViewById(R.id.textViewMyDonation);
            holder.statusBtn = (Button) convertView.findViewById(R.id.buttonMyDonation);
            holder.txtDate = (TextView) convertView.findViewById(R.id.textViewMyDonationDate);
            holder.jmlUang = (TextView) convertView.findViewById(R.id.textViewMyDonatonJmlUang);
            holder.jmlBarang = (TextView) convertView.findViewById(R.id.textViewMyDonationJmlBarang);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

            Picasso.with(context)
                    .load(donation.getGambar())
                    .fit()
                    .into(holder.imageView);
            holder.txtDesc.setText(donation.getPenggalangan());
            holder.statusBtn.setText(donation.getStatus());
        ViewHolder finalHolder = holder;
        holder.statusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (finalHolder.statusBtn.getText().equals("belum bayar")){
                        stuffShareApp.setSelectedDonation(donation);
                        ConfirmationFragment confirmationFragment = new ConfirmationFragment();
                        Activity activity = (Activity) context;
                        FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                        ShowFragment(R.id.fl_container, confirmationFragment, fragmentManager);
                    } else {
                        Toasty.info(getContext(), "donasi sudah terkonfirmasi", Toasty.LENGTH_SHORT, true).show();
                    }
                }
            });
            holder.txtDate.setText(donation.getDate());
            holder.jmlBarang.setText(donation.getTotalDonation() + " Barang");
            String jmlUang = appUtils.formatRupiah(Double.parseDouble(donation.getDonasiUang()));
            holder.jmlUang.setText(jmlUang);

        return convertView;
    }
}
