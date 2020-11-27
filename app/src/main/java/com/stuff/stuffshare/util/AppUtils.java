package com.stuff.stuffshare.util;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;

import com.stuff.stuffshare.R;
import com.stuff.stuffshare.StuffShareApp;
import com.stuff.stuffshare.adapter.AutoCompleteAdapter;
import com.stuff.stuffshare.adapter.DetailSubmissionAdapter;
import com.stuff.stuffshare.adapter.ListDonationAdapter;
import com.stuff.stuffshare.adapter.ListInfoItemDonationAdapter;
import com.stuff.stuffshare.model.Bank;
import com.stuff.stuffshare.model.CampaignCategory;
import com.stuff.stuffshare.model.Campaigner;
import com.stuff.stuffshare.model.CategoryBarang;
import com.stuff.stuffshare.model.InfoItemDonation;
import com.stuff.stuffshare.model.Message;
import com.stuff.stuffshare.network.AsyncHttpTask;
import com.stuff.stuffshare.network.OnGetDataFinish;
import com.stuff.stuffshare.network.OnHttpResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.ZoneId;
import org.threeten.bp.temporal.ChronoUnit;
import org.threeten.bp.temporal.TemporalAdjusters;

import es.dmoral.toasty.Toasty;

public class AppUtils {

    OnGetDataFinish onGetDataFinish;

    public AppUtils() {

    }

    public void setOnGetDataFinish(OnGetDataFinish getDataListener) {
        onGetDataFinish = getDataListener;
    }

    public void getDataCategory (final Context context, final StuffShareApp stuffShareApp,
                                 final DetailSubmissionAdapter detailSubmissionAdapter) {
        AsyncHttpTask taskHttp = new AsyncHttpTask("");
        stuffShareApp.getCategoryBarangs().clear();
        taskHttp.execute(stuffShareApp.HOST + stuffShareApp.CATEGORY, "GET");
        taskHttp.setHttpResponseListener(new OnHttpResponseListener() {
            @Override
            public void OnHttpResponse(String response) {
                try {
                    JSONObject resObj = new JSONObject(response);
                    if (resObj.getBoolean("r")){
                        JSONArray resArray = resObj.getJSONArray("d");
                        for (int i = 0; i < resArray.length(); i++) {
                            JSONObject jObj = resArray.getJSONObject(i);
                            CategoryBarang categoryBarang = new CategoryBarang();
                            categoryBarang.setId(jObj.getString("id"));
                            categoryBarang.setProductName(jObj.getString("kategori"));
                            stuffShareApp.getCategoryBarangs().add(categoryBarang);
                        }
                        if (onGetDataFinish != null){
                            onGetDataFinish.OnGetDataComplete();
                        }
                    }
                    detailSubmissionAdapter.notifyDataSetChanged();
                } catch (JSONException e){
                    e.printStackTrace();
                    Toasty.error(context, e.getMessage(), Toasty.LENGTH_SHORT, true).show();
                }
            }
        });
    }

    public void getDataCampaign (final Context context, final StuffShareApp stuffShareApp,
                                 final ArrayList<Campaigner> campaigners,
                                 final ListDonationAdapter listDonationAdapter) {
        AsyncHttpTask campaignTask = new AsyncHttpTask("");
        campaigners.clear();
        campaignTask.execute(stuffShareApp.HOST + stuffShareApp.CAMPAIGN, "GET");
        campaignTask.setHttpResponseListener(new OnHttpResponseListener() {
            @Override
            public void OnHttpResponse(String response) {
                Log.i(stuffShareApp.TAG, "response " + response);
                try {
                    JSONObject resObj = new JSONObject(response);
                    if (resObj.getBoolean("r")){
                        JSONArray resArray = resObj.getJSONArray("d");
                        for (int i = 0; i < resArray.length(); i++) {
                            JSONObject jObj = resArray.getJSONObject(i);
//                            Log.i(stuffShareApp.TAG, "response " + jObj);
                            Campaigner campaigner = new Campaigner();
                            campaigner.setId(jObj.getString("id"));
                            if (jObj.getString("gambar").equals("")){
                                campaigner.setImageCampaign("");
                            } else {
                                campaigner.setImageCampaign(jObj.getString("gambar"));
                            }
                            campaigner.setTitleCampaign(jObj.getString("judul"));
                            campaigner.setDesc(jObj.getString("kejadian"));
                            campaigner.setTglBuat(jObj.getString("tgl_buat"));
                            campaigner.setTglSelesai(jObj.getString("tglselesai"));
                            campaigner.setOrganization(jObj.getString("organisasi"));
                            campaigner.setCountDonation(jObj.getString("banyakdonasi"));
                            campaigner.setAlamatPenyelenggara(jObj.getString("alamat_penyelenggara"));
                            campaigner.setImageCom(jObj.getString("foto_penyelenggara"));
                            String dateBeforeString = campaigner.getTglBuat();
                            String dateAfterString = campaigner.getTglSelesai();
                            LocalDate dateBefore = LocalDate.parse(dateBeforeString);
                            LocalDate dateAfter = LocalDate.parse(dateAfterString);
//                            long noOfDaysBetween = ChronoUnit.DAYS.between(dateBefore, dateAfter);
                            long noOfDaysBetween = dateAfter.until(dateBefore, org.threeten.bp.temporal.ChronoUnit.DAYS);
                            String dateString = DateFormat.format("yyyy-MM-dd", new Date(noOfDaysBetween)).toString();
                            String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                            LocalDate dayNow = LocalDate.parse(timeStamp);
                            LocalDate dateMass = LocalDate.parse(dateString);
//                            long massDonation = ChronoUnit.DAYS.between(dayNow, dateAfter);
                            long massDonation = dayNow.until(dateAfter, org.threeten.bp.temporal.ChronoUnit.DAYS);
                            campaigner.setMasaDonasi(String.valueOf(massDonation));
                            campaigner.setDonasiBarang(jObj.getJSONArray("donasibarang"));
                            for (int j = 0; j < campaigner.getDonasiBarang().length(); j++) {
                                CategoryBarang categoryBarang = new CategoryBarang();
                                categoryBarang.setId(campaigner.getDonasiBarang().getJSONObject(j).getString("id"));
                                categoryBarang.setProductName(campaigner.getDonasiBarang().getJSONObject(j).getString("name"));
                                categoryBarang.setCount(campaigner.getDonasiBarang().getJSONObject(j).getString("qty"));
                                categoryBarang.setImageId(campaigner.getDonasiBarang().getJSONObject(j).getString("url"));
                            }
                            stuffShareApp.setCampaigner(campaigner);
                            campaigners.add(campaigner);
                        }
                    }
                    listDonationAdapter.notifyDataSetChanged();
                } catch (JSONException e){
                    e.printStackTrace();
                    Toasty.error(context, e.getMessage(), Toasty.LENGTH_SHORT, true).show();
                }
            }
        });
    }

    public void getDataCategoryCampaign (final Context context, final StuffShareApp stuffShareApp,
                                         final ArrayList<CampaignCategory> campaignCategories,
                                         final AutoCompleteAdapter adapter){
        AsyncHttpTask categoryCampaingnTask = new AsyncHttpTask("");
        categoryCampaingnTask.execute(stuffShareApp.HOST + stuffShareApp.CATEGORY_CAMPAIGN, "GET");
        categoryCampaingnTask.setHttpResponseListener(new OnHttpResponseListener() {
            @Override
            public void OnHttpResponse(String response) {
                try {
                    JSONObject resObj = new JSONObject(response);
                    if (resObj.getBoolean("r")){
                        JSONArray resArray = resObj.getJSONArray("d");
                        for (int i = 0; i < resArray.length(); i++) {
                            JSONObject jObj = resArray.getJSONObject(i);
                            CampaignCategory campaignCategory = new CampaignCategory();
                            campaignCategory.setId(jObj.getString("id"));
                            campaignCategory.setCategory(jObj.getString("kategori"));
                            campaignCategories.add(campaignCategory);
                            Log.i(stuffShareApp.TAG, "response utils " + campaignCategories.size());
                        }
                        if (onGetDataFinish != null){
                            onGetDataFinish.OnGetDataComplete();
                        }
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e){
                    e.printStackTrace();
                    Toasty.error(context, e.getMessage(), Toasty.LENGTH_SHORT, true).show();
                }
            }
        });
    }

    public void getDataInfoBarangDonation (final Context context, final StuffShareApp stuffShareApp,
                                           final ArrayList<InfoItemDonation> infoItemDonations, final ListInfoItemDonationAdapter listInfoItemDonationAdapter){
        AsyncHttpTask getInfoBarangTask = new AsyncHttpTask("");
        getInfoBarangTask.execute(stuffShareApp.HOST + stuffShareApp.CATEGORY_BARANG, "GET");
        getInfoBarangTask.setHttpResponseListener(new OnHttpResponseListener() {
            @Override
            public void OnHttpResponse(String response) {
                try {
                    JSONObject resObj = new JSONObject(response);
                    if (resObj.getBoolean("r")){
                        JSONArray resArray = resObj.getJSONArray("d");
                        for (int i = 0; i < resArray.length(); i++) {
                            JSONObject jsonObject = resArray.getJSONObject(i);
                            InfoItemDonation infoItemDonation = new InfoItemDonation();
                            infoItemDonation.setId(jsonObject.getString("id"));
                            infoItemDonation.setImageId(jsonObject.getString("gambar"));
                            infoItemDonation.setTitle(jsonObject.getString("kategori"));
                            infoItemDonation.setDescription(jsonObject.getString("deskripsi"));
                            infoItemDonations.add(infoItemDonation);
                        }
                    }
                    listInfoItemDonationAdapter.notifyDataSetChanged();
                } catch (JSONException e){
                    e.printStackTrace();
                    Toasty.error(context, e.getMessage(), Toasty.LENGTH_SHORT, true).show();
                }
            }
        });
    }

    public void getDataBank (final Context context, final StuffShareApp stuffShareApp, final ArrayList<Bank> banks) {
        AsyncHttpTask mBankTask = new AsyncHttpTask("");
        mBankTask.execute(stuffShareApp.HOST + stuffShareApp.BANK, "GET");
        mBankTask.setHttpResponseListener(new OnHttpResponseListener() {
            @Override
            public void OnHttpResponse(String response) {
                try {
                    JSONObject resObj = new JSONObject(response);
                    if (resObj.getBoolean("r")){
                        JSONArray resArray = resObj.getJSONArray("d");
                        for (int i = 0; i < resArray.length(); i++) {
                            JSONObject jsonObject = resArray.getJSONObject(i);
                            Bank bank = new Bank();
                            bank.setId(jsonObject.getString("id"));
                            bank.setNameBank(jsonObject.getString("bank"));
                            bank.setNorek(jsonObject.getString("norek"));
                            bank.setGambar(jsonObject.getString("gambar"));
                            bank.setCabang(jsonObject.getString("cabang"));
                            bank.setTampil(jsonObject.getString("tampil"));
                            bank.setToken(jsonObject.getString("token"));
                            stuffShareApp.setBank(bank);
                            banks.add(bank);
                        }
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                    Toasty.error(context, e.getMessage(), Toasty.LENGTH_SHORT, true).show();
                }
            }
        });
    }

    public String formatRupiah(Double number){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }
}
