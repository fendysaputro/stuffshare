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
import com.stuff.stuffshare.model.Donation;
import com.stuff.stuffshare.util.AppUtils;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import static com.stuff.stuffshare.MainActivity.ShowFragment;

public class MyDonationAdapter extends ArrayAdapter<Donation> {
    Context context;
    StuffShareApp stuffShareApp;
    AppUtils appUtils;

    public MyDonationAdapter(Context context, int resourceId, List<Donation> donations){
        super(context, resourceId, donations);
        this.context = context;
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

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = null;
        Donation donation = getItem(position);
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

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stuffShareApp.setSelectedDonation(donation);
                    ConfirmationFragment confirmationFragment = new ConfirmationFragment();
                    Activity activity = (Activity) context;
                    FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                    ShowFragment(R.id.fl_container, confirmationFragment, fragmentManager);
                }
            });

            Picasso.with(context)
                    .load(donation.getGambar())
                    .fit()
                    .into(holder.imageView);
            holder.txtDesc.setText(donation.getPenggalangan());
            holder.statusBtn.setText("Done");
            holder.txtDate.setText(donation.getDate());
            holder.jmlBarang.setText(donation.getDonasiBarang().length() + " Barang");
            String jmlUang = appUtils.formatRupiah(Double.parseDouble(donation.getDonasiUang()));
            holder.jmlUang.setText(jmlUang);

        return convertView;
    }
}
