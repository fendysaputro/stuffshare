package com.stuff.stuffshare.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codekidlabs.storagechooser.StorageChooser;
import com.stuff.stuffshare.MainActivity;
import com.stuff.stuffshare.R;
import com.stuff.stuffshare.StuffShareApp;
import com.stuff.stuffshare.activity.ThankyouCampaignerActivity;
import com.stuff.stuffshare.model.Bank;
import com.stuff.stuffshare.network.AsyncHttpTask;
import com.stuff.stuffshare.network.OnHttpResponseListener;
import com.stuff.stuffshare.network.UploadConfirmationTask;
import com.stuff.stuffshare.util.AppUtils;
import com.stuff.stuffshare.util.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static com.stuff.stuffshare.MainActivity.ShowFragment;
import static com.stuff.stuffshare.fragment.AccountPlusFragment.FILEPICKER_PERMISSIONS;
import static com.stuff.stuffshare.fragment.AccountPlusFragment.hasPermissions;

public class ConfirmationFragment extends Fragment {

    StuffShareApp stuffShareApp;
    AppUtils appUtils;
    SharedPrefManager sharedPrefManager;
    TextView nomTitle, nominal, senderTitle, sender, metodeTitle, metode, bankNameTitle,
            bankName, bankRekTitle, bankRek;
    String dateBayar, idBank, nameBank, rekBank;
    ArrayList<Bank> bankArrayList = null;
    Button chooseFile, uploadConfirmation;
    ImageView ivConfirmation;
    List<String> formats;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_confirmation, container, false);

        stuffShareApp = (StuffShareApp) getActivity().getApplication();
        appUtils = new AppUtils();
        sharedPrefManager = new SharedPrefManager(getActivity());

        TextView toolbar_title = view.findViewById(R.id.toolbar_title);
        toolbar_title.setText("Konfirmasi Pembayaran");
        toolbar_title.setTextColor(getResources().getColor(R.color.textColorToolbar));
        toolbar_title.setTextSize(30);

        bankArrayList = new ArrayList<Bank>();

        getDataBank("", stuffShareApp, bankArrayList);

        nomTitle = (TextView) view.findViewById(R.id.txtNominalTitle);
        nominal = (TextView) view.findViewById(R.id.txtNominal);
        senderTitle = (TextView) view.findViewById(R.id.txtSenderTitle);
        sender = (TextView) view.findViewById(R.id.txtSender);
        metodeTitle = (TextView) view.findViewById(R.id.txtMetodeTitle);
        metode = (TextView) view.findViewById(R.id.txtMetode);
        bankNameTitle = (TextView) view.findViewById(R.id.txtBankNameTitle);
        bankName = (TextView) view.findViewById(R.id.txtBankName);
        bankRekTitle = (TextView) view.findViewById(R.id.txtBankRekTitle);
        bankRek = (TextView) view.findViewById(R.id.txtBankRek);

        nomTitle.setText("Jumlah Transfer");
        String jmlUang = appUtils.formatRupiah(Double.parseDouble(stuffShareApp.getSelectedDonation().getDonasiUang()));
        nominal.setText(jmlUang);
        senderTitle.setText("Pengirim");
        sender.setText(sharedPrefManager.getSPName());
        metodeTitle.setText("Metode Kirim");
        metode.setText(stuffShareApp.getSelectedDonation().getMetodeBayar());
        bankNameTitle.setText("Nama Bank");
        bankRekTitle.setText("Nomor Rekening");

        ivConfirmation = (ImageView) view.findViewById(R.id.imageViewUpload);

        chooseFile = (Button) view.findViewById(R.id.btnChooseFileConfirm);
        chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChooseFile();
                uploadConfirmation.setEnabled(true);
            }
        });

        uploadConfirmation = (Button) view.findViewById(R.id.btnSentConfirmation);
        uploadConfirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stuffShareApp.isPicture()){
                    OnUploadConfirmation();
                } else {
                    Toasty.warning(getActivity(), "Harus pilih file dulu ", Toasty.LENGTH_SHORT).show();
                }
            }
        });

        return view;
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
                            idBank = bank.getId();
                            bank.setNameBank(jsonObject.getString("bank"));
                            nameBank = bank.getNameBank();
                            bankName.setText(bank.getNameBank());
                            bank.setNorek(jsonObject.getString("norek"));
                            rekBank = bank.getNorek();
                            bankRek.setText(bank.getNorek());
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
                }
            }
        });
    }

    public void setChooseFile(){
        String[] PERMISSIONS = {
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        if(hasPermissions(getActivity(), PERMISSIONS)){
            formats = new ArrayList<>();
            formats.add("jpg");
            formats.add("png");
            formats.add("jpeg");
            final StorageChooser chooser = new StorageChooser.Builder()
                    .withActivity(getActivity())
                    .withFragmentManager(getActivity().getFragmentManager())
                    .withMemoryBar(false)
                    .allowCustomPath(true)
                    .showFoldersInGrid(true)
                    .customFilter(formats)
                    .setType(StorageChooser.FILE_PICKER)
                    .build();

            // 2. Retrieve the selected path by the user and show in a toast !
            chooser.setOnSelectListener(new StorageChooser.OnSelectListener() {
                @Override
                public void onSelect(String npwpPath) {
                    Toast.makeText(getActivity(), "The selected path is : " + npwpPath, Toast.LENGTH_SHORT).show();
                    Bitmap imageUpload = BitmapFactory.decodeFile(npwpPath);
                    stuffShareApp.setImgConfirmation(imageUpload);
                    stuffShareApp.setPicture(true);
                    ivConfirmation.setImageBitmap(imageUpload);
                    chooseFile.setVisibility(View.INVISIBLE);
                    Log.i(stuffShareApp.TAG, "file confirmation " + stuffShareApp.getImgConfirmation());
                }
            });

            // 3. Display File Picker !
            chooser.show();
        }else{
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, FILEPICKER_PERMISSIONS);
        }
    }

    public void OnUploadConfirmation() {
        UploadConfirmationTask uploadConfirmationTask = new UploadConfirmationTask();
        uploadConfirmationTask.execute(stuffShareApp.HOST + stuffShareApp.CONFIRMATION_DONATION,
                sharedPrefManager.getSPUserid(), stuffShareApp.getSelectedDonation().getId(), idBank,
                stuffShareApp.getSelectedDonation().getMetodeBayar(), sharedPrefManager.getSPName(),
                nameBank, rekBank, stuffShareApp.getSelectedDonation().getDonasiUang(), stuffShareApp.getImgConfirmation());
        uploadConfirmationTask.setOnHttpResponseListener(new OnHttpResponseListener() {
            @Override
            public void OnHttpResponse(String response) {
                Log.i(stuffShareApp.TAG, "response " + response);
                try {
                    JSONObject resObj = new JSONObject(response);
                    if (resObj.getBoolean("r")){
                        Toasty.success(getContext(), resObj.getString("m"), Toasty.LENGTH_SHORT, true).show();
                        Intent goMain = new Intent(getActivity(), MainActivity.class);
                        startActivity(goMain);
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }
}