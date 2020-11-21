package com.stuff.stuffshare.activity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;
import com.stuff.stuffshare.R;
import com.stuff.stuffshare.StuffShareApp;
import com.stuff.stuffshare.adapter.CollectDonationAdapter;
import com.stuff.stuffshare.adapter.CollectDonationListAdapter;
import com.stuff.stuffshare.model.Bank;
import com.stuff.stuffshare.model.Campaigner;
import com.stuff.stuffshare.model.CategoryBarang;
import com.stuff.stuffshare.model.CollectDonation;
import com.stuff.stuffshare.network.AsyncHttpTask;
import com.stuff.stuffshare.network.OnHttpResponseListener;
import com.stuff.stuffshare.network.UploadDonationTask;
import com.stuff.stuffshare.network.UploadSubmissionTask;
import com.stuff.stuffshare.util.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class CollectDonationListActivity extends AppCompatActivity {

    StuffShareApp stuffShareApp;
    SharedPrefManager sharedPrefManager;

    GridView gridView;
    ArrayList<CategoryBarang> categoryBarangs;
    CollectDonationListAdapter collectDonationListAdapter = null;
    Campaigner campaigner;
    TextView addressTo;
    ArrayList<Bank> bankArrayList = null;
    JSONArray donasiBarang;
    EditText nomUang, messageDonation;
    String message;
    ImageView imageBank;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collect_donation_list_activity);

        stuffShareApp = (StuffShareApp) getApplication();
        sharedPrefManager = new SharedPrefManager(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.love);
            getSupportActionBar().setTitle("");
        }

        ImageView imageView = (ImageView) findViewById(R.id.image);
        if (stuffShareApp.getSelectedCampaigner().getImageCampaign().equals("")){
            imageView.setImageResource(R.drawable.logo);
        } else {
            Picasso.with(this)
                    .load(stuffShareApp.getSelectedCampaigner().getImageCampaign())
                    .fit()
                    .into(imageView);
        }

        TextView titleCategory = (TextView) findViewById(R.id.txtCategory);
        titleCategory.setText("Pilih Kategori dan tentukan jumlahnya");

        TextView txtDonasiBarang = (TextView) findViewById(R.id.txtBarang);
        txtDonasiBarang.setText("Donasi Barang");

        categoryBarangs = new ArrayList<CategoryBarang>();

        gridView = (GridView) findViewById(R.id.itemCollectGrid);
        collectDonationListAdapter = new CollectDonationListAdapter(this, R.layout.grid_view_item_list_donation, stuffShareApp.getCategoryBarangs());
        gridView.setAdapter(collectDonationListAdapter);

//        getData("", categoryBarangs, collectDonationListAdapter);
        getDataDonasi(stuffShareApp.getSelectedCampaigner(), stuffShareApp.getCategoryBarangs(), collectDonationListAdapter);

        bankArrayList = new ArrayList<Bank>();

        getDataBank("", stuffShareApp, bankArrayList);

        imageBank = (ImageView) findViewById(R.id.btnBank);
        imageBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "cobacoba", Toast.LENGTH_LONG).show();
            }
        });

        TextView pengiriman = (TextView) findViewById(R.id.txtPengiriman);
        pengiriman.setText("Metode Pengiriman");

        TextView mtdKirim = (TextView) findViewById(R.id.txtMetodeKirim);
        mtdKirim.setText("Kirim Paket");

        TextView addressTitle = (TextView) findViewById(R.id.txtAddress);
        addressTitle.setText("Alamat Tujuan Pengiriman");

        addressTo = (TextView) findViewById(R.id.txtToAddress);
        addressTo.setText(stuffShareApp.getSelectedCampaigner().getAlamatPenyelenggara());

        TextView donasiUang = (TextView) findViewById(R.id.txtUang);
        donasiUang.setText("Donasi Uang");

        TextView titleTransfer = (TextView) findViewById(R.id.txtKirimUang);
        titleTransfer.setText("Metode Kirim Uang");

        TextView mtdTransfer = (TextView) findViewById(R.id.txtMetodeTransfer);
        mtdTransfer.setText("Transfer ATM");

        nomUang = (EditText) findViewById(R.id.edNominal);
        nomUang.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                stuffShareApp.setNominalDonation(editable.toString());
                Log.i(stuffShareApp.TAG, "nominal " + stuffShareApp.getNominalDonation());
            }
        });

        messageDonation = (EditText) findViewById(R.id.edMessageDonation);
        messageDonation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                message = editable.toString();
                stuffShareApp.setMessageDonation(message);
            }
        });

        Button prosesDonasi = (Button) findViewById(R.id.btnDonation);
        prosesDonasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onProsesDonation();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_nav_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                Toast.makeText(this, "click..!!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menuShare:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "jangan lupa untuk saling berbagi install stuffshare di playstore");
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, stuffShareApp.TAG);

                try {
                    startActivity(shareIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toasty.warning(getApplication(), "Your Apps have not been installed", Toasty.LENGTH_SHORT, true).show();
                }
                return true;
            case R.id.menuAbout:
                Toast.makeText(getApplicationContext(),"Item 1 Selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menuSettings:
                Toast.makeText(getApplicationContext(),"Item 2 Selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menuLogout:
                Toast.makeText(getApplicationContext(),"Item 3 Selected", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getDataDonasi (Campaigner campaigner, final ArrayList<CategoryBarang> categoryBarangs, CollectDonationListAdapter collectDonationListAdapter){
        if (campaigner.getDonasiBarang() != null){
            categoryBarangs.clear();
            try {
                for (int i = 0; i < campaigner.getDonasiBarang().length(); i++) {
                    CategoryBarang categoryBarang = new CategoryBarang();
                    categoryBarang.setId(campaigner.getDonasiBarang().getJSONObject(i).getString("id"));
                    categoryBarang.setProductName(campaigner.getDonasiBarang().getJSONObject(i).getString("name"));
                    categoryBarang.setCount(campaigner.getDonasiBarang().getJSONObject(i).getString("qty"));
                    categoryBarang.setImageId(campaigner.getDonasiBarang().getJSONObject(i).getString("url"));
                    categoryBarangs.add(categoryBarang);
                }
                collectDonationListAdapter.notifyDataSetChanged();
            } catch (JSONException e){
                e.getMessage();
            }
        }
    }

    public void getDataBank (String data, final StuffShareApp stuffShareApp, final ArrayList<Bank> banks) {
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
                            Picasso.with(getApplicationContext())
                                    .load(banks.get(0).getGambar())
                                    .fit()
                                    .into(imageBank);
                        }
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void onProsesDonation () {
        if (TextUtils.isEmpty(messageDonation.getText())){
            Toasty.warning(getApplication(), "Harap isi pesan kamu", Toasty.LENGTH_SHORT, true).show();
        } else {
            ArrayList<Object> varArgsList = new ArrayList<Object>();
            varArgsList.add(stuffShareApp.HOST + stuffShareApp.ADD_DONATION);
            varArgsList.add(sharedPrefManager.getSPUserid());
            varArgsList.add(stuffShareApp.getSelectedCampaigner().getId());
            varArgsList.add(stuffShareApp.getBank().getId());
            varArgsList.add(stuffShareApp.getNominalDonation());
            varArgsList.add(sharedPrefManager.getSPAlamatPenyelenggara());
            varArgsList.add(stuffShareApp.getSelectedCampaigner().getPhoneReceiver());
            varArgsList.add("Kirim Paket");
            varArgsList.add("Transfer ATM");
            varArgsList.add(stuffShareApp.getMessageDonation());
            for (int i = 0; i < stuffShareApp.getCategoryBarangs().size(); i++) {
                varArgsList.add(stuffShareApp.getCategoryBarangs().get(i).getId() + "," + stuffShareApp.getCategoryBarangs().get(i).getCount());
            }
            UploadDonationTask uploadDonationTask = new UploadDonationTask();
            uploadDonationTask.execute(varArgsList.toArray());
            uploadDonationTask.setOnHttpResponseListener(new OnHttpResponseListener() {
                @Override
                public void OnHttpResponse(String response) {
                    try {
                        JSONObject resUpload = new JSONObject(response);
                        if (resUpload.getBoolean("r")){
                            Toasty.success(getApplication(), "Upload Success " + resUpload.getString("m")).show();
                            Log.i(stuffShareApp.TAG, "dataToUpload " + resUpload.getString("d"));
                            Intent goThankActivity = new Intent(CollectDonationListActivity.this, ThankyouActivity.class);
                            startActivity(goThankActivity);
                            goThankActivity.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            finish();
                        } else {
                            Toasty.warning(getApplication(), resUpload.getString("m"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}

