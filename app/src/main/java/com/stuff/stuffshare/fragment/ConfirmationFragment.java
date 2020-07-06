package com.stuff.stuffshare.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aminography.choosephotohelper.ChoosePhotoHelper;
import com.aminography.choosephotohelper.callback.ChoosePhotoCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.stuff.stuffshare.MainActivity;
import com.stuff.stuffshare.R;
import com.stuff.stuffshare.StuffShareApp;
import com.stuff.stuffshare.activity.ThankyouActivity;
import com.stuff.stuffshare.activity.ThankyouCampaignerActivity;
import com.stuff.stuffshare.activity.ThankyouConfirmationActivity;
import com.stuff.stuffshare.model.Bank;
import com.stuff.stuffshare.network.AsyncHttpTask;
import com.stuff.stuffshare.network.OnHttpResponseListener;
import com.stuff.stuffshare.network.UploadConfirmationTask;
import com.stuff.stuffshare.util.AppUtils;
import com.stuff.stuffshare.util.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
            bankName, bankRekTitle, bankRek, addressTitle, addressSent, jmlBarangTitle, jmlBarang;
    String dateBayar, idBank, nameBank, rekBank, noResi, metodeBayar;
    ArrayList<Bank> bankArrayList = null;
    Button chooseFile, uploadConfirmation, chooseFileBarang;
    ImageView ivConfirmation, ivConfirmationBarang;
    RelativeLayout relativeNomimal, relativeSender, relativeMetode, relativeBank, relativeRek, relativeBtnImageTransfer;
    EditText eDResi;
    List<String> formats;
    static final int REQUEST_GALLERY_PHOTO = 2;
    static final int REQUEST_GALLERY_PHOTO_RESI = 3;
    private ChoosePhotoHelper choosePhotoHelper;
    ProgressBar progressBar;
    Bitmap myLogo = null;

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

        relativeNomimal = (RelativeLayout) view.findViewById(R.id.relativeLayoutNominal);
        relativeSender = (RelativeLayout) view.findViewById(R.id.relativeLayoutSender);
        relativeMetode = (RelativeLayout) view.findViewById(R.id.relativeLayoutMetode);
        relativeBank = (RelativeLayout) view.findViewById(R.id.relativeLayoutBankName);
        relativeRek = (RelativeLayout) view.findViewById(R.id.relativeLayoutBankRek);
        relativeBtnImageTransfer = (RelativeLayout) view.findViewById(R.id.relativeBtn);

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
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        addressTitle = (TextView) view.findViewById(R.id.txtAlamatTitle);
        addressSent = (TextView) view.findViewById(R.id.txtAddressKirim);
        jmlBarangTitle = (TextView) view.findViewById(R.id.txtJmlBarangTitle);
        jmlBarang = (TextView) view.findViewById(R.id.txtJmlBarang);
        eDResi = (EditText) view.findViewById(R.id.edResiPengiriman);

        nomTitle.setText("Jumlah Transfer");
        String jmlUang = appUtils.formatRupiah(Double.parseDouble(stuffShareApp.getSelectedDonation().getDonasiUang()));
        nominal.setText(jmlUang);
        senderTitle.setText("Pengirim");
        sender.setText(sharedPrefManager.getSPName());
        metodeTitle.setText("Metode Kirim");
        metodeBayar = "Transfer ATM";
        metode.setText(metodeBayar);
        bankNameTitle.setText("Nama Bank");
        bankRekTitle.setText("Nomor Rekening");
        addressTitle.setText("Alamat");
        addressSent.setText(stuffShareApp.getSelectedDonation().getAlamatPenyelenggara());
        jmlBarangTitle.setText("Jumlah Barang");
        jmlBarang.setText(stuffShareApp.getSelectedDonation().getTotalDonation() + " Barang");

        ivConfirmation = (ImageView) view.findViewById(R.id.imageViewUpload);
        ivConfirmationBarang = (ImageView) view.findViewById(R.id.imageViewUploadBarang);

        eDResi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                noResi = editable.toString();
            }
        });

        chooseFile = (Button) view.findViewById(R.id.btnChooseFileConfirm);
        chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChooseFile();
//                choosePhotoHelper.chooseFromGallery();
                uploadConfirmation.setEnabled(true);
            }
        });

        chooseFileBarang = (Button) view.findViewById(R.id.btnChooseFileConfirmBarang);
        chooseFileBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChooseFileBarang();
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

        if (stuffShareApp.getSelectedDonation().getTotalDonation() == 0) {
            eDResi.setEnabled(false);
            ivConfirmationBarang.setEnabled(false);
            chooseFileBarang.setEnabled(false);
        }

        if (stuffShareApp.getSelectedDonation().getDonasiUang().equals("0")){
            ivConfirmation.setEnabled(false);
            chooseFile.setEnabled(false);
        }

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
            Intent intent=new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            String[] mimeTypes = {"image/jpeg", "image/png"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
            startActivityForResult(intent, REQUEST_GALLERY_PHOTO);
        }
    }

    public void setChooseFileBarang(){
        String[] PERMISSIONS = {
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        if(hasPermissions(getActivity(), PERMISSIONS)){
            Intent intent=new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            String[] mimeTypes = {"image/jpeg", "image/png"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
            startActivityForResult(intent, REQUEST_GALLERY_PHOTO_RESI);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY_PHOTO && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri selectedImageUpload = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(selectedImageUpload, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                Bitmap imageUpload = BitmapFactory.decodeFile(imgDecodableString);
                stuffShareApp.setImgConfirmation(imageUpload);
                stuffShareApp.setPicture(true);
                ivConfirmation.setImageBitmap(imageUpload);
                chooseFile.setVisibility(View.INVISIBLE);
            }
        } else if (requestCode == REQUEST_GALLERY_PHOTO_RESI && resultCode == Activity.RESULT_OK){
            if (data != null){
                Uri selectedImageUploadResi = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(selectedImageUploadResi, filePath, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePath[0]);
                String imgDecode = cursor.getString(columnIndex);
                cursor.close();
                Bitmap resiUpload = BitmapFactory.decodeFile(imgDecode);
                stuffShareApp.setResiConfirmation(resiUpload);
                stuffShareApp.setPicture(true);
                ivConfirmationBarang.setImageBitmap(resiUpload);
                chooseFileBarang.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        choosePhotoHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public static ConfirmationFragment newInstance() {
        return new ConfirmationFragment();
    }

    public void OnUploadConfirmation() {
        Log.i(stuffShareApp.TAG, "pesan " + stuffShareApp.getMessageDonation());
        String message = stuffShareApp.getMessageDonation();
//        Bitmap myLogo = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.logo_title);
        Drawable myDrawable = getResources().getDrawable(R.drawable.logo_title);
        myLogo      = ((BitmapDrawable) myDrawable).getBitmap();

        progressBar.setVisibility(View.VISIBLE);
        String totalDonation = String.valueOf(stuffShareApp.getSelectedDonation().getTotalDonation());
        if (totalDonation.equals("0")){
            UploadConfirmationTask uploadConfirmationTask = new UploadConfirmationTask();
            uploadConfirmationTask.execute(stuffShareApp.HOST + stuffShareApp.CONFIRMATION_DONATION,
                    sharedPrefManager.getSPUserid(), stuffShareApp.getSelectedDonation().getId(), idBank,
                    metodeBayar, sharedPrefManager.getSPName(),
                    nameBank, rekBank, stuffShareApp.getSelectedDonation().getDonasiUang(),
                    stuffShareApp.getImgConfirmation(), noResi, stuffShareApp.getSelectedDonation().getAlamatPenyelenggara(),
                    totalDonation, myLogo);
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

        if (stuffShareApp.getSelectedDonation().getDonasiUang().equals("0")){
            UploadConfirmationTask uploadConfirmationTask = new UploadConfirmationTask();
            uploadConfirmationTask.execute(stuffShareApp.HOST + stuffShareApp.CONFIRMATION_DONATION,
                    sharedPrefManager.getSPUserid(), stuffShareApp.getSelectedDonation().getId(), idBank,
                    metodeBayar, sharedPrefManager.getSPName(),
                    nameBank, rekBank, stuffShareApp.getSelectedDonation().getDonasiUang(),
                    myLogo, noResi, stuffShareApp.getSelectedDonation().getAlamatPenyelenggara(),
                    totalDonation, stuffShareApp.getResiConfirmation());
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

        UploadConfirmationTask uploadConfirmationTask = new UploadConfirmationTask();
        uploadConfirmationTask.execute(stuffShareApp.HOST + stuffShareApp.CONFIRMATION_DONATION,
                sharedPrefManager.getSPUserid(), stuffShareApp.getSelectedDonation().getId(), idBank,
                metodeBayar, sharedPrefManager.getSPName(),
                nameBank, rekBank, stuffShareApp.getSelectedDonation().getDonasiUang(),
                stuffShareApp.getImgConfirmation(), noResi, stuffShareApp.getSelectedDonation().getAlamatPenyelenggara(),
                totalDonation, stuffShareApp.getResiConfirmation());
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