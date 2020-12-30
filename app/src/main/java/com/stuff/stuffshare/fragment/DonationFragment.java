package com.stuff.stuffshare.fragment;

import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import com.stuff.stuffshare.R;
import com.stuff.stuffshare.StuffShareApp;
import com.stuff.stuffshare.activity.CollectDonationActivity;
import com.stuff.stuffshare.activity.LoginActivity;
import com.stuff.stuffshare.adapter.DetailSubmissionAdapter;
import com.stuff.stuffshare.adapter.ListDonationAdapter;
import com.stuff.stuffshare.model.Campaigner;
import com.stuff.stuffshare.model.Client;
import com.stuff.stuffshare.model.RowItem;
import com.stuff.stuffshare.network.AsyncHttpTask;
import com.stuff.stuffshare.network.OnGetDataFinish;
import com.stuff.stuffshare.network.OnHttpResponseListener;
import com.stuff.stuffshare.util.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static android.app.Activity.RESULT_OK;

/**
 * created by niko and team
 */
public class DonationFragment extends Fragment {

    StuffShareApp stuffShareApp;;

    ListView listView;
    List<RowItem> rowItemList;
    ArrayList<Campaigner> arrayList = null;
    ListDonationAdapter listDonationAdapter = null;
    SwipeRefreshLayout swipeRefreshLayout = null;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donation, container, false);
        stuffShareApp = (StuffShareApp) getActivity().getApplication();

        TextView toolbar_title = view.findViewById(R.id.toolbar_title);
        toolbar_title.setText(R.string.txt_donasi_title);
        toolbar_title.setTextColor(getResources().getColor(R.color.textColorToolbar));
        toolbar_title.setTextSize(30);

        progressBar = view.findViewById(R.id.progressBar);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);

        listView = (ListView) view.findViewById(R.id.itemListView);
        arrayList = new ArrayList<Campaigner>();
        listDonationAdapter = new ListDonationAdapter(getContext(), R.layout.list_view_donation, arrayList);
        listView.setAdapter(listDonationAdapter);

        if (listDonationAdapter != null) {
            progressBar.setVisibility(View.GONE);
        }

        getDataCampaign("", listDonationAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Campaigner rowItem = (Campaigner) arrayList.get(position);
                String massDonasi = rowItem.getMasaDonasi();
                int waktuDonasi = Integer.parseInt(massDonasi);
                if (waktuDonasi <= 0){
                    view.setEnabled(false);
                    Toasty.info(getActivity(), "Masa donasi sudah habis", Toasty.LENGTH_SHORT, true).show();
                } else {
                    stuffShareApp.setSelectedCampaigner(rowItem);
                    Intent goIntent = new Intent(getActivity(), CollectDonationActivity.class);
                    goIntent.putExtra("IMAGE_NAME", rowItem.getImageCampaign());
                    stuffShareApp.setImgCampaign(rowItem.getImageCampaign());
                    startActivity(goIntent);
                }
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if (listView.getChildAt(0) != null) {
                    swipeRefreshLayout.setEnabled(listView.getFirstVisiblePosition() == 0 &&
                            listView.getChildAt(0).getTop() == 0);
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataCampaign("", listDonationAdapter);
            }
        });


        return view;
    }

    public void getDataCampaign (String data, final ListDonationAdapter adapter){
        final AppUtils appUtils = new AppUtils();
        appUtils.getDataCampaign(getContext(), stuffShareApp, arrayList, adapter);
        appUtils.setOnGetDataFinish(() -> {
            swipeRefreshLayout.setRefreshing(false);
        });
    }
}
