package com.stuff.stuffshare.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.stuff.stuffshare.MainActivity;
import com.stuff.stuffshare.R;
import com.stuff.stuffshare.StuffShareApp;
import com.stuff.stuffshare.adapter.AutoCompleteAdapter;
import com.stuff.stuffshare.model.CampaignCategory;
import com.stuff.stuffshare.model.Campaigner;
import com.stuff.stuffshare.network.AsyncHttpTask;
import com.stuff.stuffshare.network.OnGetDataFinish;
import com.stuff.stuffshare.network.OnHttpResponseListener;
import com.stuff.stuffshare.util.AppUtils;
import com.stuff.stuffshare.util.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import es.dmoral.toasty.Toasty;

public class SubmissionActivity extends AppCompatActivity {

    StuffShareApp stuffShareApp;
    String[] target ={" Yayasan / Lembaga", " Bencana Alam", " Kesehatan", " Umum"};
    EditText receiverDonation, phoneReceiver,accident, addressReceiver, dateAccident;
    Button backBtn, nextBtn;
    String kategori, penerima, phoneRec, kejadian, addressRec, dateAct;
    SharedPrefManager sharedPrefManager;
    ArrayList<CampaignCategory> campaignCategories = null;
    AutoCompleteAdapter autoCompleteAdapter = null;
    private int year, month, day;
    private DatePicker datePicker;
    private Calendar calendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submission_activity);

        stuffShareApp = (StuffShareApp) this.getApplication();
        sharedPrefManager = new SharedPrefManager(this);

        campaignCategories = new ArrayList<CampaignCategory>();

        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setTextColor(getResources().getColor(R.color.colorAccent));
        toolbar_title.setTextSize(20);

        receiverDonation = (EditText) findViewById(R.id.edNameReceiveDonation);
        receiverDonation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                penerima = editable.toString();
            }
        });
        phoneReceiver = (EditText) findViewById(R.id.edPhoneReceiveDonation);
        phoneReceiver.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                phoneRec = editable.toString();
            }
        });
        accident = (EditText) findViewById(R.id.edAccident);
        accident.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                kejadian = editable.toString();
            }
        });
        addressReceiver = (EditText) findViewById(R.id.edAddressReceiveDonation);
        addressReceiver.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                addressRec = editable.toString();
            }
        });

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        dateAccident = (EditText) findViewById(R.id.edDateAccident);
        dateAccident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(view);
            }
        });
        backBtn = (Button) findViewById(R.id.btnCancel);
        nextBtn = (Button) findViewById(R.id.btnContinue);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_dropdown_item_1line, target);

        final AutoCompleteTextView actv =  (AutoCompleteTextView)findViewById(R.id.edNamereceive);
//        autoCompleteAdapter = new AutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line, campaignCategories);
//        actv.setAdapter(autoCompleteAdapter);

//        getDataCategoryCampaign("", campaignCategories, autoCompleteAdapter);


        actv.setText(actv.getText());
        actv.setThreshold(1);//will start working from first character
        actv.setDropDownVerticalOffset(1);
        actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        actv.setTextColor(Color.BLACK);
        actv.getDropDownBackground();
        actv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actv.showDropDown();
            }
        });
//
        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                actv.showDropDown();
                kategori = parent.getItemAtPosition(position).toString();
            }
        });



        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goMain = new Intent(getApplication(), MainActivity.class);
                startActivity(goMain);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                sharedPrefManager.saveSPString(SharedPrefManager.kategori, penerima);
                stuffShareApp.setKategori(kategori);
                stuffShareApp.setPenerima(penerima);
                stuffShareApp.setPhoneReceiver(phoneRec);
                stuffShareApp.setAccident(kejadian);
                stuffShareApp.setAddressReceiver(addressRec);
                stuffShareApp.setDateAccident(dateAct);
//                sharedPrefManager.saveSPString(SharedPrefManager.penerima, receiverDonation.getText().toString());
//                sharedPrefManager.saveSPString(SharedPrefManager.nohp, phoneReceiver.getText().toString());
//                sharedPrefManager.saveSPString(SharedPrefManager.kejadian, accident.getText().toString());
//                sharedPrefManager.saveSPString(SharedPrefManager.alamat_penerima, addressReceiver.getText().toString());
//                sharedPrefManager.saveSPString(SharedPrefManager.tglkejadian, dateAccident.getText().toString());
                Intent goDetailActivity = new Intent(getApplication(), DetailSubmissionActivity.class);
                startActivity(goDetailActivity);
            }
        });
    }

    public void getDataCategoryCampaign (String data, final ArrayList<CampaignCategory> list, final ArrayAdapter adapter){
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
                            list.add(campaignCategory);
                            Log.i(stuffShareApp.TAG, "response utils " + list.size());
                        }
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), stuffShareApp.TAG,
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        dateAccident.setText(new StringBuilder().append(day).append("-")
                .append(month).append("-").append(year));
        dateAct = dateAccident.getText().toString();

    }
}
