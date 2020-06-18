package com.stuff.stuffshare.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.stuff.stuffshare.R;
import com.stuff.stuffshare.StuffShareApp;

public class RememberDonationFragment extends Fragment {

    StuffShareApp stuffShareApp;
    Button seninBtn;
    boolean check = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_remember_donation, container, false);
        stuffShareApp = (StuffShareApp) getActivity().getApplication();

        TextView toolbar_title = view.findViewById(R.id.toolbar_title);
        toolbar_title.setText("Pengingat Donasi");
        toolbar_title.setTextColor(getResources().getColor(R.color.textColorToolbar));
        toolbar_title.setTextSize(30);

        seninBtn = (Button) view.findViewById(R.id.btnSenin);

        return view;
    }
}