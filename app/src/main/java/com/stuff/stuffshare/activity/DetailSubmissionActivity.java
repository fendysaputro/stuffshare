package com.stuff.stuffshare.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.stuff.stuffshare.MainActivity;
import com.stuff.stuffshare.R;
import com.stuff.stuffshare.StuffShareApp;
import com.stuff.stuffshare.adapter.DetailSubmissionAdapter;
import com.stuff.stuffshare.model.Campaigner;
import com.stuff.stuffshare.model.CategoryBarang;
import com.stuff.stuffshare.model.DetailDonation;
import com.stuff.stuffshare.network.AsyncHttpTask;
import com.stuff.stuffshare.network.OnGetDataFinish;
import com.stuff.stuffshare.network.OnHttpResponseListener;
import com.stuff.stuffshare.util.AppUtils;
import com.stuff.stuffshare.util.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class DetailSubmissionActivity extends AppCompatActivity {

    StuffShareApp stuffShareApp;
    GridView gridView = null;
    DetailSubmissionAdapter detailSubmissionAdapter = null;
    String[] peridoeGalang ={" 1 Bulan", " 2 Bulan", " 3 Bulan"};
    Button backBtn, continueBtn;
    String periode, titleCamp, needDon, forWh;
    EditText titleCampaign, needDonation, forWhat;
    EditText jml = null;
    Context context;
//    ArrayList<CategoryBarang> categoryBarangArrayList = null;
    SharedPrefManager sharedPrefManager;
    JSONArray donasiBarang;
    String donIdOne = null, donIdTwo = null, donIdThree = null;
    String donQtyOne = null, donQtyTwo = null, donQtyThree = null;
    LinearLayout ll = null;
    int i = 0;

    public Context getCtx(){
        return context;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_submission_activity);

        stuffShareApp = (StuffShareApp) this.getApplication();
        sharedPrefManager = new SharedPrefManager(this);

        donasiBarang = new JSONArray();

        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setTextColor(getResources().getColor(R.color.colorAccent));
        toolbar_title.setTextSize(20);

        gridView = (GridView) findViewById(R.id.itemCollectDetailSubmission);
        detailSubmissionAdapter = new DetailSubmissionAdapter(this, R.layout.grid_view_detail_submission, stuffShareApp.getCategoryBarangs());
        gridView.setAdapter(detailSubmissionAdapter);

        getDataCategoryBarang("", detailSubmissionAdapter);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_dropdown_item_1line,peridoeGalang);

        final AutoCompleteTextView actv =  (AutoCompleteTextView)findViewById(R.id.autoPeriode);
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

        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                periode = parent.getItemAtPosition(position).toString();
            }
        });

        titleCampaign = (EditText) findViewById(R.id.edDetailDonation);
        titleCampaign.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                titleCamp = editable.toString();
            }
        });
        needDonation = (EditText) findViewById(R.id.edNeedDonation);
        needDonation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                needDon = editable.toString();
            }
        });
        forWhat = (EditText) findViewById(R.id.edForWhat);
        forWhat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                forWh = editable.toString();
            }
        });
        backBtn = (Button) findViewById(R.id.btnCancelDetail);
        continueBtn = (Button) findViewById(R.id.btnContinueDetail);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goBack = new Intent(getApplication(), SubmissionActivity.class);
                startActivity(goBack);
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(titleCampaign.getText()) && TextUtils.isEmpty(needDonation.getText()) && TextUtils.isEmpty(forWhat.getText())){
                    Toasty.warning(getApplication(), "field tidak boleh kosong", Toasty.LENGTH_SHORT, true).show();
                } else {
                    Campaigner campaigner = new Campaigner();
//                sharedPrefManager.saveSPString(SharedPrefManager.periode, periode);
                    stuffShareApp.setPeriode(periode);
                    stuffShareApp.setTitleCampaign(titleCamp);
                    stuffShareApp.setNeedDonation(needDon);
                    stuffShareApp.setForWhat(forWh);
//                sharedPrefManager.saveSPString(SharedPrefManager.judul, titleCampaign.getText().toString());
//                sharedPrefManager.saveSPString(SharedPrefManager.kebutuhan_dana, needDonation.getText().toString());
//                sharedPrefManager.saveSPString(SharedPrefManager.digunakanuntuk, forWhat.getText().toString());

                    Intent goUploadSubmission = new Intent(getApplication(), UploadSubmissionActivity.class);
                    startActivity(goUploadSubmission);
                }
            }
        });
    }

    public void getDataCategoryBarang (String data, final DetailSubmissionAdapter adapter){
        final AppUtils appUtils = new AppUtils();
        appUtils.getDataCategory(getCtx(), stuffShareApp, adapter);
        appUtils.setOnGetDataFinish(new OnGetDataFinish() {
            @Override
            public void OnGetDataComplete() {
            }
        });
    }
}
