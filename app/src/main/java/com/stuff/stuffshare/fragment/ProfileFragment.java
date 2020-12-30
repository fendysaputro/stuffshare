package com.stuff.stuffshare.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codekidlabs.storagechooser.StorageChooser;
import com.squareup.picasso.Picasso;
import com.stuff.stuffshare.MainActivity;
import com.stuff.stuffshare.R;
import com.stuff.stuffshare.StuffShareApp;
import com.stuff.stuffshare.activity.LoginActivity;
import com.stuff.stuffshare.network.AsyncHttpTask;
import com.stuff.stuffshare.network.OnHttpResponseListener;
import com.stuff.stuffshare.network.UploadPhotoProfileTask;
import com.stuff.stuffshare.util.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static com.stuff.stuffshare.MainActivity.ShowFragment;
import static com.stuff.stuffshare.fragment.AccountPlusFragment.FILEPICKER_PERMISSIONS;

public class ProfileFragment extends Fragment {
    StuffShareApp stuffShareApp;
    TextView whatsappTitle, whatsappNumber, emailTitle, email, userName, statuAccountTitle, statusAccountDonatur,
            statusAccountCampaigner, rememberDonationTitle, changePasswordTitle, ratingTitle, logoutTitle;
    ImageView profileImage;
    SharedPrefManager sharedPrefManager;
    List<String> formats;
    Context context;
    int akunPlus;
    String status;
    static final int REQUEST_GALLERY_IMAGE = 4545;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        stuffShareApp = (StuffShareApp) getActivity().getApplication();
        sharedPrefManager = new SharedPrefManager(getActivity());

        TextView toolbar_title = view.findViewById(R.id.toolbar_title);
        toolbar_title.setText(R.string.txt_akun_title);
        toolbar_title.setTextColor(getResources().getColor(R.color.textColorToolbar));
        toolbar_title.setTextSize(30);

        profileImage = (ImageView) view.findViewById(R.id.imageViewProfile);
        if (sharedPrefManager.getSPAkunplus() == 1){
            String imageCommunity = sharedPrefManager.getSPImageCom();
            Picasso.with(getActivity())
                    .load(imageCommunity)
                    .fit()
                    .into(profileImage);
//            profileImage.setImageResource(R.drawable.user);
        }
        else if (sharedPrefManager.getSPImage().equals("")){
            profileImage.setImageResource(R.drawable.user);
        }
        else if (sharedPrefManager.getSPAkunplus() != 1 && !sharedPrefManager.getSPImage().equals("")){
            Picasso.with(getActivity())
                    .load(sharedPrefManager.getSPImage())
                    .fit()
                    .into(profileImage);
        }

        profileImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String[] PERMISSIONS = {
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                };

                if(hasPermissions(getActivity(), PERMISSIONS)){
                    Intent intent=new Intent(Intent.ACTION_PICK);
                    // Sets the type as image/*. This ensures only components of type image are selected
                    intent.setType("image/*");
                    //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
                    String[] mimeTypes = {"image/jpeg", "image/png"};
                    intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
                    // Launching the Intent
                    startActivityForResult(intent,REQUEST_GALLERY_IMAGE);
                    if (profileImage != null){
                        profileImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                                alertDialog.setTitle(R.string.txt_ubah_foto_title);
                                alertDialog.setMessage(R.string.txt_confirm_dialog);
                                alertDialog.setPositiveButton(R.string.txt_yes,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                onSendPhoto();
                                            }
                                        });
                                alertDialog.setNegativeButton(R.string.txt_no,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                alertDialog.show();
                            }
                        });
                    } else {
                        Toasty.warning(getActivity(), "foto profil belum terganti", Toasty.LENGTH_SHORT, true).show();
                    }
                }else{
                    ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, FILEPICKER_PERMISSIONS);
                }
                return false;
            }
        });

        userName = (TextView) view.findViewById(R.id.tvName);
        userName.setText(sharedPrefManager.getSPName());

        whatsappTitle = (TextView) view.findViewById(R.id.txtWhatsapp);
        whatsappTitle.setText(R.string.txt_wa_title);

        whatsappNumber = (TextView) view.findViewById(R.id.txtWhatsappNumber);
        whatsappNumber.setText(sharedPrefManager.getSPPhone());

        emailTitle = (TextView) view.findViewById(R.id.txtEmailTitle);
        emailTitle.setText(R.string.txt_email_title);

        email = (TextView) view.findViewById(R.id.txtEmail);
        email.setText(sharedPrefManager.getSPEmail());

        statuAccountTitle = (TextView) view.findViewById(R.id.txtstatusAccountTitle);
        statuAccountTitle.setText(R.string.txt_status_akun);

        statusAccountDonatur = (TextView) view.findViewById(R.id.txtstatusAccountDonatur);
        statusAccountCampaigner = (TextView) view.findViewById(R.id.txtstatusAccountCampaign);
        if (sharedPrefManager.getSPAkunplus() == 1 || akunPlus == 1 && status.equals("1")){
            statusAccountDonatur.setText(R.string.txt_donatur);
            statusAccountDonatur.setTextColor(getResources().getColor(R.color.colorAccent));
            statusAccountCampaigner.setText(R.string.txt_penggalang);
        } else {
            statusAccountDonatur.setText(R.string.txt_donatur);
            statusAccountCampaigner.setText(R.string.txt_penggalang);
            statusAccountCampaigner.setTextColor(getResources().getColor(R.color.colorAccent));
        }

        changePasswordTitle = (TextView) view.findViewById(R.id.txtChangePassword);
        changePasswordTitle.setText(R.string.txt_ubah_password);
        changePasswordTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
                FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                ShowFragment(R.id.fl_container, changePasswordFragment, fragmentManager);
            }
        });

        rememberDonationTitle = (TextView) view.findViewById(R.id.txtRememberDonation);
        rememberDonationTitle.setText(R.string.txt_remember_donasi);
        rememberDonationTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle(R.string.txt_notification_title);
                alertDialog.setMessage(R.string.txt_confirm_notif);
                alertDialog.setIcon(R.drawable.logo);
                alertDialog.setPositiveButton(R.string.txt_yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
//                                sharedPrefManager.saveSPBoolean(SharedPrefManager.notif, true);
                                stuffShareApp.setNotification(true);
                                Toasty.success(getContext(), "notification saved!!!", Toasty.LENGTH_SHORT, true).show();
                                Intent goMain = new Intent(getContext(), MainActivity.class);
                                goMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                goMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(goMain);
                                getActivity().finish();
                            }
                        });
                alertDialog.setNegativeButton(R.string.txt_no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();

            }
        });

        ratingTitle = (TextView) view.findViewById(R.id.txtGiveRating);
        ratingTitle.setText(R.string.txt_rating_title);
        ratingTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toasty.info(getActivity(), "Fitur ini sedang di update ", Toasty.LENGTH_SHORT, true).show();
            }
        });

        logoutTitle = (TextView) view.findViewById(R.id.txtLogout);
        logoutTitle.setText(R.string.txt_exit_title);
        logoutTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle(R.string.txt_exit_title);
                alertDialog.setMessage(R.string.txt_confirm_exit);
                alertDialog.setIcon(R.drawable.logout);
                alertDialog.setPositiveButton(R.string.txt_yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                stuffShareApp.setUser(null);
                                sharedPrefManager.saveSPBoolean(SharedPrefManager.login, false);
                                Intent loginIntent = new Intent(getContext(), LoginActivity.class);
                                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(loginIntent);
                                getActivity().finish();
                            }
                        });
                alertDialog.setNegativeButton(R.string.txt_no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri selectedImageUpload = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                // Get the cursor
                Cursor cursor = getActivity().getContentResolver().query(selectedImageUpload, filePathColumn, null, null, null);
//                // Move to first row
                cursor.moveToFirst();
//                //Get the column index of MediaStore.Images.Media.DATA
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                //Gets the String value in the column
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
////                // Set the Image in ImageView after decoding the String
                Bitmap profileBmp = BitmapFactory.decodeFile(imgDecodableString);
                stuffShareApp.setImgProfile(profileBmp);
                profileImage.setImageBitmap(stuffShareApp.getImgProfile());
                Log.i(stuffShareApp.TAG, "file image " + stuffShareApp.getImgProfile());
            }
        }
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

    public void onSendPhoto () {
        UploadPhotoProfileTask uploadPhotoProfileTask = new UploadPhotoProfileTask();
        Log.i(stuffShareApp.TAG, "id " + sharedPrefManager.getSPUserid());
        uploadPhotoProfileTask.execute(stuffShareApp.HOST + stuffShareApp.EDIT_PHOTO,
                sharedPrefManager.getSPUserid(), stuffShareApp.getImgProfile());
        uploadPhotoProfileTask.setOnHttpResponseListener(new OnHttpResponseListener() {
            @Override
            public void OnHttpResponse(String response) {
                try {
                    JSONObject resObj = new JSONObject(response);
                    if (resObj.getBoolean("r")){
                        Toasty.success(getActivity(), resObj.getString("m"), Toasty.LENGTH_SHORT, true).show();
                        Intent goLoginActivity = new Intent(getActivity(), MainActivity.class);
                        startActivity(goLoginActivity);
                    } else {
                        Toasty.warning(getActivity(), resObj.getString("m"), Toasty.LENGTH_SHORT, true).show();
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
//        AsyncHttpTask sentPhotoTask = new AsyncHttpTask("id="+sharedPrefManager.getSPUserid()+
//                "&image="+stuffShareApp.getImgProfile());
//        sentPhotoTask.execute(stuffShareApp.HOST + stuffShareApp.EDIT_PHOTO, "POST");
//        sentPhotoTask.setHttpResponseListener(new OnHttpResponseListener() {
//            @Override
//            public void OnHttpResponse(String response) {
//                try {
//                    JSONObject resObj = new JSONObject(response);
//                    if (resObj.getBoolean("r")){
//                        Toasty.success(getActivity(), resObj.getString("m"), Toasty.LENGTH_SHORT, true).show();
//                        Intent goLoginActivity = new Intent(getActivity(), MainActivity.class);
//                        startActivity(goLoginActivity);
//                    } else {
//                        Toasty.warning(getActivity(), resObj.getString("m"), Toasty.LENGTH_SHORT, true).show();
//                    }
//                } catch (JSONException e){
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    @Override
    public void onStart() {
        super.onStart();
        AsyncHttpTask getUserTask = new AsyncHttpTask("");
        getUserTask.execute(stuffShareApp.HOST + stuffShareApp.GET_USER + sharedPrefManager.getSPUserid(), "GET");
        getUserTask.setHttpResponseListener(new OnHttpResponseListener() {
            @Override
            public void OnHttpResponse(String response) {
                try {
                    JSONObject resObj = new JSONObject(response);
                    if (resObj.getBoolean("r")){
                        JSONObject dataObj = resObj.getJSONObject("d");
                        akunPlus = dataObj.getInt("akunplus");
                        status = dataObj.getString("status_akunplus");
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
