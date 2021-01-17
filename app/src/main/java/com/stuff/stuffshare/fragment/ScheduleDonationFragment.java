package com.stuff.stuffshare.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.stuff.stuffshare.R;
import com.stuff.stuffshare.StuffShareApp;
import com.stuff.stuffshare.activity.CollectDonationActivity;
import com.stuff.stuffshare.adapter.ListDonationAdapter;
import com.stuff.stuffshare.adapter.ListInfoItemDonationAdapter;
import com.stuff.stuffshare.adapter.ScheduleAdapter;
import com.stuff.stuffshare.model.Campaigner;
import com.stuff.stuffshare.model.CategoryBarang;
import com.stuff.stuffshare.model.DonasiBayar;
import com.stuff.stuffshare.model.RowItem;
import com.stuff.stuffshare.model.ScheduleDonation;
import com.stuff.stuffshare.model.TotalDonasiBarang;
import com.stuff.stuffshare.network.AsyncHttpTask;
import com.stuff.stuffshare.network.OnGetDataFinish;
import com.stuff.stuffshare.network.OnHttpResponseListener;
import com.stuff.stuffshare.util.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.ZoneId;
import org.threeten.bp.temporal.ChronoUnit;
import org.threeten.bp.temporal.TemporalAdjusters;

import static com.stuff.stuffshare.MainActivity.ShowFragment;

public class ScheduleDonationFragment extends Fragment {

    StuffShareApp stuffShareApp;
    ListView listView;
    ArrayList<Campaigner> scheduleDonationList = null;
    ScheduleAdapter scheduleAdapter = null;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_donation, container, false);
        stuffShareApp = (StuffShareApp) getActivity().getApplication();

        TextView toolbar_title = view.findViewById(R.id.toolbar_title);
        toolbar_title.setText(R.string.txt_schedule_title);
        toolbar_title.setTextColor(getResources().getColor(R.color.textColorToolbar));
        toolbar_title.setTextSize(30);

        scheduleDonationList = new ArrayList<Campaigner>();
        listView = (ListView) view.findViewById(R.id.itemListView);
        scheduleAdapter = new ScheduleAdapter(getContext(), R.layout.list_view_schedule_donation, scheduleDonationList);
        listView.setAdapter(scheduleAdapter);

        getData("", scheduleDonationList, scheduleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Campaigner campaigner = (Campaigner) scheduleDonationList.get(position);
                DataScheduleDonationFragment dataScheduleDonationFragment = new DataScheduleDonationFragment();
                Activity activity = (Activity) context;
                Bundle bundle = new Bundle();
                stuffShareApp.setSelectedCampaigner(campaigner);
                bundle.putString("IMAGE_NAME", campaigner.getImageCampaign());
                FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                ShowFragment(R.id.fl_container, dataScheduleDonationFragment, fragmentManager);
                dataScheduleDonationFragment.setArguments(bundle);
            }
        });

        return view;
    }

    public void getData (String data, ArrayList<Campaigner> campaigners, ScheduleAdapter scheduleAdapter) {
        AsyncHttpTask campaignTask = new AsyncHttpTask("");
        campaignTask.execute(StuffShareApp.HOST + StuffShareApp.CAMPAIGN, "GET");
        campaignTask.setHttpResponseListener(new OnHttpResponseListener() {
            @Override
            public void OnHttpResponse(String response) {
                try {
                    JSONObject resObj = new JSONObject(response);
                    if (resObj.getBoolean("r")){
                        JSONArray resArray = resObj.getJSONArray("d");
                        for (int i = 0; i < resArray.length(); i++) {
                            JSONObject jObj = resArray.getJSONObject(i);
                            Campaigner campaigner = new Campaigner();
                            campaigner.setId(jObj.getString("id"));
                            campaigner.setImageCampaign(jObj.getString("gambar"));
                            campaigner.setDesc(jObj.getString("kejadian"));
                            campaigner.setTglBuat(jObj.getString("tgl_buat"));
                            campaigner.setTglSelesai(jObj.getString("tglselesai"));
                            campaigner.setOrganization(jObj.getString("organisasi"));
                            campaigner.setCountDonation(jObj.getString("banyakdonasi"));
                            campaigner.setAddressReceiver(jObj.getString("alamat_penerima"));
                            campaigner.setImageCom(jObj.getString("foto_penyelenggara"));
                            campaigner.setAlamatPenyelenggara(jObj.getString("alamat_penyelenggara"));
                            String dateBeforeString = campaigner.getTglBuat();
                            String dateAfterString = campaigner.getTglSelesai();
                            LocalDate dateBefore = LocalDate.parse(dateBeforeString);
                            LocalDate dateAfter = LocalDate.parse(dateAfterString);
                            long noOfDaysBetween = ChronoUnit.DAYS.between(dateBefore, dateAfter);
                            String dateString = DateFormat.format("yyyy-MM-dd", new Date(noOfDaysBetween)).toString();
                            String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                            LocalDate dayNow = LocalDate.parse(timeStamp);
                            LocalDate dateMass = LocalDate.parse(dateString);
                            long massDonation = ChronoUnit.DAYS.between(dayNow, dateAfter);
                            campaigner.setMasaDonasi(String.valueOf(massDonation));
                            campaigner.setSisaHari(String.valueOf(noOfDaysBetween));
                            if (!campaigner.getMasaDonasi().equals("0")){
                                campaigner.setStatusCampaign("On Progress");
                            } else {
                                campaigner.setStatusCampaign("Done");
                            }
                            campaigner.setStory(jObj.getString("cerita"));
                            campaigner.setDonasiBarang(jObj.getJSONArray("donasibarang"));
                            for (int j = 0; j < campaigner.getDonasiBarang().length(); j++) {
                                CategoryBarang categoryBarang = new CategoryBarang();
                                categoryBarang.setId(campaigner.getDonasiBarang().getJSONObject(j).getString("id"));
                                categoryBarang.setProductName(campaigner.getDonasiBarang().getJSONObject(j).getString("name"));
                                categoryBarang.setCount(campaigner.getDonasiBarang().getJSONObject(j).getString("qty"));
                                categoryBarang.setImageId(campaigner.getDonasiBarang().getJSONObject(j).getString("url"));
                                categoryBarang.setPenerimaan(campaigner.getDonasiBarang().getJSONObject(j).getString("penerimaan"));
                            }
                            campaigner.setBanyakDonasi(jObj.getJSONArray("banyakdonasi"));
                            campaigners.add(campaigner);
                        }
                    }
                    scheduleAdapter.notifyDataSetChanged();
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
