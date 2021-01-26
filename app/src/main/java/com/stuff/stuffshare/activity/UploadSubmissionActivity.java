package com.stuff.stuffshare.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.codekidlabs.storagechooser.StorageChooser;
import com.stuff.stuffshare.BuildConfig;
import com.stuff.stuffshare.R;
import com.stuff.stuffshare.StuffShareApp;
import com.stuff.stuffshare.adapter.DetailSubmissionAdapter;
import com.stuff.stuffshare.model.CategoryBarang;
import com.stuff.stuffshare.network.AsyncHttpTask;
import com.stuff.stuffshare.network.OnHttpResponseListener;
import com.stuff.stuffshare.network.UploadSubmissionTask;
import com.stuff.stuffshare.util.AppUtils;
import com.stuff.stuffshare.util.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static com.stuff.stuffshare.fragment.AccountPlusFragment.FILEPICKER_PERMISSIONS;
import static com.stuff.stuffshare.fragment.AccountPlusFragment.hasPermissions;
import static java.security.AccessController.getContext;

public class UploadSubmissionActivity extends AppCompatActivity {

    StuffShareApp stuffShareApp;
    Button backUpload, finishUpload, btnUploadImage, chooseFile;
    EditText story;
    ImageView imageStory;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_GALLERY_PHOTO = 2;
    File mediaFile = null;
    Context context;
    SharedPrefManager sharedPrefManager;
    String[] extraMimeTypes = {"application/pdf", "image/*"};
    List<String> formats;
    JSONArray donasiBarang;
    ArrayList<CategoryBarang> categoryBarangs;
    CategoryBarang categoryBarang = null;
    ProgressBar progressBar;

    public Context getCtx(){
        return context;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_submission_activity);

        stuffShareApp = (StuffShareApp) this.getApplication();
        sharedPrefManager = new SharedPrefManager(this);
//        categoryBarang = new CategoryBarang();

        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setTextColor(getResources().getColor(R.color.colorAccent));
        toolbar_title.setTextSize(20);

        categoryBarangs = new ArrayList<CategoryBarang>();
        donasiBarang = new JSONArray();
        for (int i = 0; i < donasiBarang.length(); i++) {
            CategoryBarang categoryBarang = new CategoryBarang();
            try {
                categoryBarang.setId(donasiBarang.getJSONObject(i).getString("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            categoryBarang.getId();
        }

        story = (EditText) findViewById(R.id.edStoryDonation);
        story.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                stuffShareApp.setCerita(editable.toString());
            }
        });
//        sharedPrefManager.saveSPString(SharedPrefManager.cerita, story.getText().toString());
        imageStory = (ImageView) findViewById(R.id.imageViewUpload);

        categoryBarangs = new ArrayList<CategoryBarang>();
        donasiBarang = new JSONArray();

        btnUploadImage = (Button) findViewById(R.id.btnUpload);
        btnUploadImage.setVisibility(View.INVISIBLE);
        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeMedia(MediaStore.ACTION_IMAGE_CAPTURE, REQUEST_TAKE_PHOTO);
            }
        });

        chooseFile = (Button) findViewById(R.id.btnChooseFile);
        chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChooseFile();
            }
        });

        backUpload = (Button) findViewById(R.id.btnBackUpload);
        finishUpload = (Button) findViewById(R.id.btnFinishUpload);

        backUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goBack = new Intent(getApplication(), DetailSubmissionActivity.class);
                startActivity(goBack);
            }
        });

        finishUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onUploadButton();

            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    public void takeMedia (String actionType, int codeRequest) {
        Intent takeMediaIntent = new Intent(actionType);
        if (takeMediaIntent.resolveActivity(this.getPackageManager()) != null){
            try {
                if (codeRequest == REQUEST_TAKE_PHOTO){
                    mediaFile = createImageFile();
                    if (mediaFile != null){
                        Uri photoURI = FileProvider.getUriForFile(this,
                                "com.stuff.stuffshare",
                                mediaFile);
                        takeMediaIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takeMediaIntent, codeRequest);
                    }
                }
            } catch (IOException ex){
                Log.e("newmms", ex.getMessage());
            }
        }
    }

    String mCurrentPhotoPath;
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void setChooseFile(){
        String[] PERMISSIONS = {
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        if(hasPermissions(this, PERMISSIONS)){

            Intent intent=new Intent(Intent.ACTION_PICK);
            // Sets the type as image/*. This ensures only components of type image are selected
            intent.setType("image/*");
            //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
            String[] mimeTypes = {"image/jpeg", "image/png"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
            // Launching the Intent
            startActivityForResult(intent,REQUEST_GALLERY_PHOTO);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            stuffShareApp.setImageFileDoc(mediaFile);
            Bitmap bitmap = BitmapFactory.decodeFile(stuffShareApp.getImageFileDoc().getAbsolutePath());
            Point point = new Point();
            point.set(20, 150);
            OutputStream fOut = null;
            try {
                fOut = new FileOutputStream(mediaFile);
            } catch (FileNotFoundException fex) {
                fex.printStackTrace();
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            try {
                fOut.flush();
                fOut.close();
            } catch (IOException j) {
                j.printStackTrace();
            }
            setImagePreview();

        }
        if (requestCode == REQUEST_GALLERY_PHOTO && resultCode == Activity.RESULT_OK) {
            if (data != null){
                Uri selectedImageUpload = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                // Get the cursor
                Cursor cursor = this.getContentResolver().query(selectedImageUpload, filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();
                //Get the column index of MediaStore.Images.Media.DATA
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                //Gets the String value in the column
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                // Set the Image in ImageView after decoding the String
                Bitmap imageUpload = BitmapFactory.decodeFile(imgDecodableString);
                stuffShareApp.setImageUpload(imageUpload);
                Log.i(stuffShareApp.TAG, "file akta " + stuffShareApp.getImageUpload());
                imageStory.setImageURI(selectedImageUpload);
                chooseFile.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void setImagePreview(){
        Bitmap bmp = BitmapFactory.decodeFile(stuffShareApp.getImageFileDoc().getPath());
        imageStory.setImageBitmap(bmp);
        imageStory.setVisibility(View.VISIBLE);
        btnUploadImage.setVisibility(View.INVISIBLE);
        chooseFile.setVisibility(View.INVISIBLE);
    }


    public void onUploadButton () {
        if (TextUtils.isEmpty(story.getText())){
            Toasty.warning(getApplication(), "field tidak boleh kosong", Toasty.LENGTH_SHORT, true).show();
        } else {
            ArrayList<Object> varArgsList = new ArrayList<Object>();
            varArgsList.add(stuffShareApp.HOST + stuffShareApp.ADD_CAMPAIGN);
            varArgsList.add(sharedPrefManager.getSPUserid());
            varArgsList.add(stuffShareApp.getKategori());
            Log.i(stuffShareApp.TAG, "data " + varArgsList.get(2).toString());
            varArgsList.add(stuffShareApp.getPenerima());
            varArgsList.add(stuffShareApp.getPhoneReceiver());
            varArgsList.add(stuffShareApp.getAddressReceiver());
            varArgsList.add(stuffShareApp.getAccident());
            varArgsList.add(stuffShareApp.getDateAccident());
            varArgsList.add(stuffShareApp.getTitleCampaign());
            varArgsList.add("5.000.000");
            varArgsList.add(stuffShareApp.getForWhat());
            varArgsList.add(stuffShareApp.getPeriode());
            varArgsList.add(stuffShareApp.getCerita());
            varArgsList.add(stuffShareApp.getImageUpload());
            for (int i = 0; i < stuffShareApp.getCategoryBarangs().size(); i++) {
                varArgsList.add(stuffShareApp.getCategoryBarangs().get(i).getCount());
            }
            UploadSubmissionTask uploadSubmissionTask = new UploadSubmissionTask();
            uploadSubmissionTask.execute(varArgsList.toArray());
            uploadSubmissionTask.setOnHttpResponseListener(new OnHttpResponseListener() {
                @Override
                public void OnHttpResponse(String response) {
                    try {
                        JSONObject resObj = new JSONObject(response);
                        if (resObj.getBoolean("r")){
                            Log.i(stuffShareApp.TAG, "response " + resObj.getString("d"));
                            Toasty.success(getApplication(), resObj.getString("m"), Toasty.LENGTH_SHORT, true).show();
                            Intent goThankyou = new Intent(getApplication(), ThankyouCampaignerActivity.class);
                            startActivity(goThankyou);
                            finishAffinity();
                        } else {
                            Toasty.warning(getApplication(), resObj.getString("m"), Toasty.LENGTH_SHORT, true).show();
                        }
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
