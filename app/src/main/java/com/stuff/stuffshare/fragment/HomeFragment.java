package com.stuff.stuffshare.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.stuff.stuffshare.R;
import com.stuff.stuffshare.adapter.HomeAdapter;
import com.stuff.stuffshare.model.Item;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    GridView itemList;
    ArrayList itemHomeList = new ArrayList<>();
    HomeAdapter adapter = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView toolbar_title = view.findViewById(R.id.toolbar_title);
        toolbar_title.setText("StuffShare");
        toolbar_title.setTextColor(getResources().getColor(R.color.textColorToolbar));
        toolbar_title.setTextSize(30);

        itemHomeList.clear();

        itemList = (GridView) view.findViewById(R.id.itemHomeGrid);
        itemHomeList.add(new Item("Donasi"));
        itemHomeList.add(new Item("Pengajuan Donasi"));
        itemHomeList.add(new Item("Akun Plus"));
        itemHomeList.add(new Item("Jadwal Donasi"));
        itemHomeList.add(new Item("Informasi Barang Donasi"));
        itemHomeList.add(new Item("Status Donasi"));

        adapter = new HomeAdapter(getContext(), R.layout.grid_view_items, itemHomeList);
        itemList.setAdapter(adapter);

        return view;
    }
}
