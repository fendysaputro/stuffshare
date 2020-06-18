package com.stuff.stuffshare.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.stuff.stuffshare.R;
import com.stuff.stuffshare.StuffShareApp;
import com.stuff.stuffshare.adapter.ListInfoItemDonationAdapter;
import com.stuff.stuffshare.model.InfoItemDonation;
import com.stuff.stuffshare.network.OnGetDataFinish;
import com.stuff.stuffshare.util.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class InformationItemDonationFragment extends Fragment {
    StuffShareApp stuffShareApp;
    public static final String[] descriptions = new String[] {
            "Pakaian dapat berupa celana maupun baju bisa anda donasikan kepada mereka yang berhak",
            "Sepatu dan sendal asal masih layak pakai bisa anda donasikan agar menambah keberkahan",
            "Barang-barang elektronik seperti tv, radio, kulkas, mesin cuci yang tidak terpakai bisa didonasikan",
            "Alat musik dapat didonasikan untuk mereka yang berbakat namun tak memiliki biaya untuk membeli",
            "Buku Pelajara, Novel, Cerita Nabi dan buku-buku lainnya sangat bagus untuk didonasikan kepada anak-anak yang putus sekolah"
    };

    public static final String[] title = new String[]{
            "Pakaian", "Sepatu", "Elektronik", "Alat Musik", "Buku"
    };

    public static final Integer[] images = {
            R.drawable.shirt,
            R.drawable.shoes,
            R.drawable.television,
            R.drawable.guitar,
            R.drawable.book
    };

    ListView listView;
    ArrayList<InfoItemDonation> infoItemDonationList;
    ListInfoItemDonationAdapter listInfoItemDonationAdapter = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information_item_donation, container, false);
        stuffShareApp = (StuffShareApp) getActivity().getApplication();

        TextView toolbar_title = view.findViewById(R.id.toolbar_title);
        toolbar_title.setText("Informasi Barang Donasi");
        toolbar_title.setTextColor(R.color.textColorToolbar);
        toolbar_title.setTextSize(30);

        infoItemDonationList = new ArrayList<InfoItemDonation>();


        listView = (ListView) view.findViewById(R.id.itemListView);
        listInfoItemDonationAdapter = new ListInfoItemDonationAdapter(getContext(), R.layout.list_view_donation, infoItemDonationList);
        listView.setAdapter(listInfoItemDonationAdapter);

        getDataCategoryBarang("", listInfoItemDonationAdapter);

        return view;
    }

    public void getDataCategoryBarang (String data, final ListInfoItemDonationAdapter adapter) {
        final AppUtils appUtils = new AppUtils();
        appUtils.getDataInfoBarangDonation(getContext(), stuffShareApp,infoItemDonationList, adapter);
        appUtils.setOnGetDataFinish(new OnGetDataFinish() {
            @Override
            public void OnGetDataComplete() {

            }
        });
    }
}
