package com.stuff.stuffshare.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
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

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (!hasPermissions(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
//            EasyPermissions.requestPermissions(this,  "android.permission.READ_EXTERNAL_STORAGE", EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
//        }
    }

//    @Override
//    public void onPermissionsGranted(int requestCode, List perms) {
//        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//        } else {
//            ActivityCompat.requestPermissions(getActivity(),new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//        }
//
//        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
//                == PackageManager.PERMISSION_GRANTED) {
//        } else {
//            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
//        }
//
//        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.INTERNET)
//                == PackageManager.PERMISSION_GRANTED) {
//        } else {
//            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.INTERNET}, 3);
//        }
//    }
//
//    @Override
//    public void onPermissionsDenied(int requestCode, List perms) {
//        // Add your logic here
//    }


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
                public void onSelect(String path) {
                    Toast.makeText(getActivity(), "The selected path is : " + path, Toast.LENGTH_SHORT).show();
                    uploadAkta.setText(path);
                    Bitmap aktaBmp = BitmapFactory.decodeFile(path);
                    stuffShareApp.setAkta(aktaBmp);
                    Log.i(stuffShareApp.TAG, "file akta " + stuffShareApp.getAkta());
                }
            });

            // 3. Display File Picker !
            chooser.show();
        }else{
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, FILEPICKER_PERMISSIONS);
        }
    }

    public void startChooseFileNPWP(){

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
                uploadNpwp.setText(npwpPath);
                Bitmap npwpBmp = BitmapFactory.decodeFile(npwpPath);
                stuffShareApp.setNpwp(npwpBmp);
                Log.i(stuffShareApp.TAG, "file npwp " + stuffShareApp.getNpwp());
            }
        });

        // 3. Display File Picker !
        chooser.show();
    }

    public void startChooseFileImageCommunity(){
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
            public void onSelect(String imagePath) {
                Toast.makeText(getActivity(), "The selected path is : " + imagePath, Toast.LENGTH_SHORT).show();
                uploadImage.setText(imagePath);
                Bitmap imageBmp = BitmapFactory.decodeFile(imagePath);
                stuffShareApp.setImageOrg(imageBmp);
                Log.i(stuffShareApp.TAG, "file image " + stuffShareApp.getImageOrg());
            }
        });

        // 3. Display File Picker !
        chooser.show();
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
                        Uri uriAkta = data.getData();
//                        try {
//                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uriAkta);
//                            uploadAkta.setText(uriAkta.getPath());
//                            stuffShareApp.setAkta(bitmap);
//                            Log.i(stuffShareApp.TAG, "file akta " + stuffShareApp.getAkta());
                            String[] filePath = {MediaStore.Images.Media.DATA};
                            Cursor cursor = getActivity().getContentResolver().query(uriAkta, filePath, null, null, null);
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePath[0]);
                            String myPath = cursor.getString(columnIndex);
                            cursor.close();

                            uploadAkta.setText(myPath);
                            Bitmap aktaBmp = BitmapFactory.decodeFile(myPath);
                            stuffShareApp.setAkta(aktaBmp);

//                        }
//                        catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        String[] filePath = {MediaStore.Images.Media.DATA};
//                        Cursor cursor = getActivity().getContentResolver().query(uriAkta, filePath, null, null, null);
//                        cursor.moveToFirst();
//                        if (cursor != null && cursor.moveToFirst()){
//                            int columnIndex = cursor.getColumnIndex(filePath[0]);
//                            String myPath = cursor.getString(columnIndex);
//                            Log.i(stuffShareApp.TAG, "URI " + myPath);

//                        filePathAkta = uriAkta.getPath();
//                        fileAkta = new File(filePathAkta);
//                            uploadAkta.setText(myPath);
//                            Bitmap aktaBmp = BitmapFactory.decodeFile(myPath);
//                            stuffShareApp.setAkta(aktaBmp);
//                            Log.i(stuffShareApp.TAG, "file akta " + stuffShareApp.getAkta());
//                        }
                    }
                }
                break;
            case REQUEST_CODE_NPWP:
                if (resultCode == Activity.RESULT_OK){
                    if (data != null){
                        Uri uriNpwp = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uriNpwp);
                            uploadNpwp.setText(uriNpwp.getPath());
                            stuffShareApp.setNpwp(bitmap);
                            Log.i(stuffShareApp.TAG, "file npwp " + stuffShareApp.getNpwp());
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
//                        String[] filePath = {MediaStore.Images.Media.DATA};
//                        Cursor cursor = getActivity().getContentResolver().query(uriNpwp, filePath, null, null, null);
//                        cursor.moveToFirst();
//                        if (cursor != null && cursor.moveToFirst()){
//                            int columnIndex = cursor.getColumnIndex(filePath[0]);
//                            String myPath = cursor.getString(columnIndex);
//                        filePathNpwp = uriNpwp.getPath();
//                        fileNpwp = new File(filePathNpwp);
//                        Log.i(stuffShareApp.TAG, "file npwp" + fileNpwp);
//                            uploadNpwp.setText(myPath);
//                            Bitmap npwpBmp = BitmapFactory.decodeFile(myPath);
//                            stuffShareApp.setNpwp(npwpBmp);
//                            Log.i(stuffShareApp.TAG, "file npwp " + stuffShareApp.getNpwp());
//                        }
                    }
                }
                break;
            case REQUEST_CODE_IMAGE:
                if (resultCode == Activity.RESULT_OK){
                    if (data != null){
                        Uri uriImage = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uriImage);
                            uploadImage.setText(uriImage.getPath());
                            stuffShareApp.setImageOrg(bitmap);
                            Log.i(stuffShareApp.TAG, "file Image " + stuffShareApp.getImageOrg());
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
//                        String[] filePath = {MediaStore.Images.Media.DATA};
//                        Cursor cursor = getActivity().getContentResolver().query(uriImage, filePath, null, null, null);
//                        cursor.moveToFirst();
//                        if (cursor != null && cursor.moveToFirst()){
//                            int columnIndex = cursor.getColumnIndex(filePath[0]);
//                            String myPath = cursor.getString(columnIndex);
//                        filePathImage = uriImage.getPath();
//                        fileImage = new File(filePathImage);
//                        Log.i(stuffShareApp.TAG, "file image" + fileImage);
//                            uploadImage.setText(myPath);
//                            Bitmap imageOrgBmp = BitmapFactory.decodeFile(myPath);
//                            stuffShareApp.setImageOrg(imageOrgBmp);
//                            Log.i(stuffShareApp.TAG, "file image " + stuffShareApp.getImageOrg());
//                        }
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
                        }
                    } catch (JSONException e){
                        progressBar.setVisibility(View.GONE);
                        e.printStackTrace();
                        Toasty.error(getContext(), e.getMessage(), Toasty.LENGTH_SHORT, true).show();
                    }
                }
            });
        }

//        AsyncHttpTask mAccountPlusTask = new AsyncHttpTask("userid="+sharedPrefManager.getSPUserid()
//                                        +"&organization="+stuffShareApp.getNameCommunity()
//                                        +"&address="+stuffShareApp.getAddressCommunity()
//                                        +"&npwp="+stuffShareApp.getNpwpKetua()
//                                        +"&akta="+stuffShareApp.getNumberAktaCommunity()
//                                        +"&img_akta="+filePathAkta
//                                        +"&img_npwp="+filePathNpwp
//                                        +"&img_organization="+filePathImage);
//        Log.i(stuffShareApp.TAG, "link " + stuffShareApp.HOST + stuffShareApp.AKUN_PLUS_REGISTER_PATH);
//        mAccountPlusTask.execute(stuffShareApp.HOST + stuffShareApp.AKUN_PLUS_REGISTER_PATH, "POST");
//        mAccountPlusTask.setHttpResponseListener(new OnHttpResponseListener() {
//            @Override
//            public void OnHttpResponse(String response) {
//                Log.i(stuffShareApp.TAG, "response " + response);
//                try {
//                    JSONObject resObj = new JSONObject(response);
//                    if (resObj.getBoolean("r")){
//                        Toasty.success(getContext(), resObj.getString("m"), Toasty.LENGTH_SHORT, true).show();
//                        ThankyouFragment thankyouFragment = new ThankyouFragment();
//                        FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
//                        ShowFragment(R.id.fl_container, thankyouFragment, fragmentManager);
//                    }
//                } catch (JSONException e){
//                    e.printStackTrace();
//                }
//            }
//        });
    }
}
