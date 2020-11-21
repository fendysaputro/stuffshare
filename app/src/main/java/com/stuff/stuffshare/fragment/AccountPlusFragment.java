package com.stuff.stuffshare.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aminography.choosephotohelper.ChoosePhotoHelper;
import com.aminography.choosephotohelper.callback.ChoosePhotoCallback;
import com.codekidlabs.storagechooser.Content;
import com.codekidlabs.storagechooser.StorageChooser;
import com.stuff.stuffshare.MainActivity;
import com.stuff.stuffshare.R;
import com.stuff.stuffshare.StuffShareApp;
import com.stuff.stuffshare.network.ApiConfig;
import com.stuff.stuffshare.network.ApiUtils;
import com.stuff.stuffshare.network.AsyncHttpTask;
import com.stuff.stuffshare.network.OnHttpResponseListener;
import com.stuff.stuffshare.network.ServerResponse;
import com.stuff.stuffshare.network.UploadFilesTask;
import com.stuff.stuffshare.util.FileUtils;
import com.stuff.stuffshare.util.SharedPrefManager;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Callback;
import retrofit2.Response;

import static com.stuff.stuffshare.MainActivity.ShowFragment;
import static pub.devrel.easypermissions.EasyPermissions.hasPermissions;

public class AccountPlusFragment extends Fragment {

    Button selectAkta, selectNpwp, selectImage, cancelBtn, finishBtn;
    EditText uploadAkta, uploadNpwp, uploadImage, nameCommunity, addressCommunity, npwpNumber, numberAktaCommunity, nameKetuaCommunity;
    private static final int REQUEST_CODE_AKTA = 1;
    private static final int REQUEST_CODE_NPWP = 2;
    private static final int REQUEST_CODE_IMAGE = 3;
    private static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 2;
    StuffShareApp stuffShareApp;
    SharedPrefManager sharedPrefManager;
    String[] extraMimeTypes = {"application/pdf", "image/*"};
    List<String> formats;
    String filePathAkta;
    String filePathNpwp;
    String filePathImage;
    File fileAkta, fileNpwp, fileImage;
    public static boolean allowBackPressed = false;
    private final static String TAG_FRAGMENT = "TAG_FRAGMENT";
    String[] mediaColumns = {MediaStore.Video.Media._ID};
    ProgressDialog progressDialog;
    public static final int FILEPICKER_PERMISSIONS = 1;
    ProgressBar progressBar;
    private ChoosePhotoHelper choosePhotoHelper;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_plus, container, false);
        stuffShareApp = (StuffShareApp) getActivity().getApplication();
        sharedPrefManager = new SharedPrefManager(getActivity());

        TextView toolbar_title = view.findViewById(R.id.toolbar_title);
        toolbar_title.setText("Registrasi Akun Plus");
        toolbar_title.setTextColor(getResources().getColor(R.color.textColorToolbar));
        toolbar_title.setTextSize(30);

        nameCommunity = (EditText) view.findViewById(R.id.edNameCommunity);
        addressCommunity = (EditText) view.findViewById(R.id.edAddressCommunity);
        numberAktaCommunity = (EditText) view.findViewById(R.id.edNomorAktaCommunity);
        nameKetuaCommunity = (EditText) view.findViewById(R.id.edNameKetuaCommunity);
        npwpNumber = (EditText) view.findViewById(R.id.edNomorNPWPKetuaCommunity);
        uploadAkta = (EditText) view.findViewById(R.id.edUploadAkta);
        uploadNpwp = (EditText) view.findViewById(R.id.edUploadNPWP);
        uploadImage = (EditText) view.findViewById(R.id.edUploadImage);
        selectAkta = (Button) view.findViewById(R.id.btnUploadAkta);
        selectNpwp = (Button) view.findViewById(R.id.btnUploadNPWP);
        selectImage = (Button) view.findViewById(R.id.btnUploadImage);
        cancelBtn = (Button) view.findViewById(R.id.btnCancel);
        finishBtn = (Button) view.findViewById(R.id.btnContinue);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        nameCommunity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                stuffShareApp.setNameCommunity(editable.toString());
            }
        });

        addressCommunity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                stuffShareApp.setAddressCommunity(editable.toString());
            }
        });

        numberAktaCommunity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                stuffShareApp.setNumberAktaCommunity(editable.toString());
            }
        });

        npwpNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                stuffShareApp.setNpwpKetua(editable.toString());
            }
        });

        nameKetuaCommunity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                stuffShareApp.setNameKetuaCommunity(editable.toString());
            }
        });

        selectAkta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startChooseFileAkta();
            }
        });

        selectNpwp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startChooseFileNPWP();
            }
        });

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startChooseFileImageCommunity();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goMain = new Intent(getActivity(), MainActivity.class);
                startActivity(goMain);
            }
        });

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFinishButton();
            }
        });

        return view;
    }

    public void startChooseFileAkta(){

        String[] PERMISSIONS = {
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
//
        if(hasPermissions(getActivity(), PERMISSIONS)){

            Intent intent=new Intent(Intent.ACTION_PICK);
            // Sets the type as image/*. This ensures only components of type image are selected
            intent.setType("image/*");
            //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
            String[] mimeTypes = {"image/jpeg", "image/png"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
            // Launching the Intent
            startActivityForResult(intent,REQUEST_CODE_AKTA);
        }
    }

    public void startChooseFileNPWP(){

        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent,REQUEST_CODE_NPWP);
    }

    public void startChooseFileImageCommunity(){

        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent,REQUEST_CODE_IMAGE);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case FILEPICKER_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                            getActivity(),
                            "Permission granted! Please click on pick a file once again.",
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    Toast.makeText(
                            getActivity(),
                            "Permission denied to read your External storage :(",
                            Toast.LENGTH_SHORT
                    ).show();
                }

                return;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_AKTA:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null){
                        Uri selectedImageAkta = data.getData();
                        String[] filePathColumn = { MediaStore.Images.Media.DATA };
//                         Get the cursor
                        Cursor cursor = getActivity().getContentResolver().query(selectedImageAkta, filePathColumn, null, null, null);
//                         Move to first row
                        cursor.moveToFirst();
//                        Get the column index of MediaStore.Images.Media.DATA
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                        Gets the String value in the column
                        String imgDecodableString = cursor.getString(columnIndex);
                        cursor.close();
//                         Set the Image in ImageView after decoding the String
                        uploadAkta.setText(imgDecodableString);
                        Bitmap aktaBmp = BitmapFactory.decodeFile(imgDecodableString);
                        stuffShareApp.setAkta(aktaBmp);
                        Log.i(stuffShareApp.TAG, "file akta " + stuffShareApp.getAkta());
                    }
                }
                break;
            case REQUEST_CODE_NPWP:
                if (resultCode == Activity.RESULT_OK){
                    if (data != null){
                        Uri selectedImageNpwp = data.getData();
                        String[] filePathColumn = { MediaStore.Images.Media.DATA };
                        // Get the cursor
                        Cursor cursor = getActivity().getContentResolver().query(selectedImageNpwp, filePathColumn, null, null, null);
                        // Move to first row
                        cursor.moveToFirst();
                        //Get the column index of MediaStore.Images.Media.DATA
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        //Gets the String value in the column
                        String imgDecodableString = cursor.getString(columnIndex);
                        cursor.close();
                        // Set the Image in ImageView after decoding the String
                        uploadNpwp.setText(imgDecodableString);
                        Bitmap npwpBmp = BitmapFactory.decodeFile(imgDecodableString);
                        stuffShareApp.setNpwp(npwpBmp);
                        Log.i(stuffShareApp.TAG, "file npwp " + stuffShareApp.getNpwp());
                    }
                }
                break;
            case REQUEST_CODE_IMAGE:
                if (resultCode == Activity.RESULT_OK){
                    if (data != null){
                        Uri selectedImageOrg = data.getData();
                        String[] filePathColumn = { MediaStore.Images.Media.DATA };
                        // Get the cursor
                        Cursor cursor = getActivity().getContentResolver().query(selectedImageOrg, filePathColumn, null, null, null);
                        // Move to first row
                        cursor.moveToFirst();
                        //Get the column index of MediaStore.Images.Media.DATA
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        //Gets the String value in the column
                        String imgDecodableString = cursor.getString(columnIndex);
                        cursor.close();
                        // Set the Image in ImageView after decoding the String
                        uploadImage.setText(imgDecodableString);
                        Bitmap imageOrgBmp = BitmapFactory.decodeFile(imgDecodableString);
                        stuffShareApp.setImageOrg(imageOrgBmp);
                        Log.i(stuffShareApp.TAG, "file image org " + stuffShareApp.getImageOrg());
                    }
                }
                break;
            default:
        }
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "AKTA_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void onFinishButton() {
        progressBar.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(nameCommunity.getText()) && TextUtils.isEmpty(addressCommunity.getText()) && TextUtils.isEmpty(numberAktaCommunity.getText()) &&
            TextUtils.isEmpty(nameKetuaCommunity.getText()) && TextUtils.isEmpty(nameKetuaCommunity.getText())){
            Toasty.warning(getActivity(), "field tidak boleh kosong", Toasty.LENGTH_SHORT, true).show();
        } else {
            UploadFilesTask uploadFilesTask = new UploadFilesTask();
            uploadFilesTask.execute(stuffShareApp.HOST+stuffShareApp.AKUN_PLUS_REGISTER_PATH,
                    sharedPrefManager.getSPUserid(), stuffShareApp.getNameCommunity(),stuffShareApp.getAddressCommunity(),
                    stuffShareApp.getNumberAktaCommunity(), stuffShareApp.getNameKetuaCommunity(), stuffShareApp.getNpwpKetua(),
                    stuffShareApp.getAkta(), stuffShareApp.getNpwp(), stuffShareApp.getImageOrg());
            uploadFilesTask.setOnHttpResponseListener(new OnHttpResponseListener() {
                @Override
                public void OnHttpResponse(String response) {
                    Log.i(stuffShareApp.TAG, "response " + response);
                    try {
                        JSONObject resObj = new JSONObject(response);
                        if (resObj.getBoolean("r")){
                            JSONObject dataObj = resObj.getJSONObject("d");
                            int akunplus = 1;
                            Log.i(stuffShareApp.TAG, "akunplus " + dataObj);
//                            sharedPrefManager.saveSPBoolean(SharedPrefManager.accountPlus, true);
//                        stuffShareApp.setAkunPlus(true);
                            sharedPrefManager.saveSPInt("akunplus", akunplus);
                            Toasty.success(getContext(), resObj.getString("m"), Toasty.LENGTH_SHORT, true).show();
                            ThankyouFragment thankyouFragment = new ThankyouFragment();
                            FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                            ShowFragment(R.id.fl_container, thankyouFragment, fragmentManager);
                            Objects.requireNonNull(getActivity()).finish();
                        }
                    } catch (JSONException e){
                        progressBar.setVisibility(View.GONE);
                        e.printStackTrace();
                        Toasty.error(getContext(), e.getMessage(), Toasty.LENGTH_SHORT, true).show();
                    }
                }
            });
        }
    }
}
