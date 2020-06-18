package com.stuff.stuffshare.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        stuffShareApp = (StuffShareApp) getActivity().getApplication();
        sharedPrefManager = new SharedPrefManager(getActivity());

        TextView toolbar_title = view.findViewById(R.id.toolbar_title);
        toolbar_title.setText("Akun");
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

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                            Bitmap profileBmp = BitmapFactory.decodeFile(path);
                            stuffShareApp.setImgProfile(profileBmp);
                            profileImage.setImageBitmap(stuffShareApp.getImgProfile());
                            Log.i(stuffShareApp.TAG, "file akta " + stuffShareApp.getImgProfile());
                        }
                    });

                    // 3. Display File Picker !
                    chooser.show();
                    if (profileImage != null){
                        profileImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                                alertDialog.setTitle("Ubah foto");
                                alertDialog.setMessage("Apakah anda yakin untuk ganti foto profil?");
                                alertDialog.setPositiveButton("YA",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                onSendPhoto();
                                            }
                                        });
                                alertDialog.setNegativeButton("TIDAK",
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
            }
        });

        userName = (TextView) view.findViewById(R.id.tvName);
        userName.setText(sharedPrefManager.getSPName());

        whatsappTitle = (TextView) view.findViewById(R.id.txtWhatsapp);
        whatsappTitle.setText("Whatsapp");

        whatsappNumber = (TextView) view.findViewById(R.id.txtWhatsappNumber);
        whatsappNumber.setText(sharedPrefManager.getSPPhone());

        emailTitle = (TextView) view.findViewById(R.id.txtEmailTitle);
        emailTitle.setText("Email");

        email = (TextView) view.findViewById(R.id.txtEmail);
        email.setText(sharedPrefManager.getSPEmail());

        statuAccountTitle = (TextView) view.findViewById(R.id.txtstatusAccountTitle);
        statuAccountTitle.setText("Status Akun");

        statusAccountDonatur = (TextView) view.findViewById(R.id.txtstatusAccountDonatur);
        statusAccountCampaigner = (TextView) view.findViewById(R.id.txtstatusAccountCampaign);
        if (sharedPrefManager.getSPAkunplus() == 1){
            statusAccountDonatur.setText("Donatur");
            statusAccountDonatur.setTextColor(getResources().getColor(R.color.colorAccent));
            statusAccountCampaigner.setText("Penggalang");
        } else {
            statusAccountDonatur.setText("Donatur");
            statusAccountCampaigner.setText("Penggalang");
            statusAccountCampaigner.setTextColor(getResources().getColor(R.color.colorAccent));
        }

        changePasswordTitle = (TextView) view.findViewById(R.id.txtChangePassword);
        changePasswordTitle.setText("Ubah Password");
        changePasswordTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
                FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                ShowFragment(R.id.fl_container, changePasswordFragment, fragmentManager);
            }
        });

        rememberDonationTitle = (TextView) view.findViewById(R.id.txtRememberDonation);
        rememberDonationTitle.setText("Ingatkan Donasi");
        rememberDonationTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Notification");
                alertDialog.setMessage("Apakah anda yakin set notification?");
                alertDialog.setIcon(R.drawable.logo);
                alertDialog.setPositiveButton("YA",
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
                alertDialog.setNegativeButton("TIDAK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();

            }
        });

        ratingTitle = (TextView) view.findViewById(R.id.txtGiveRating);
        ratingTitle.setText("Beri Rating Aplikasi");
        ratingTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toasty.info(getActivity(), "Fitur ini sedang di update ", Toasty.LENGTH_SHORT, true).show();
            }
        });

        logoutTitle = (TextView) view.findViewById(R.id.txtLogout);
        logoutTitle.setText("Keluar");
        logoutTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Keluar");
                alertDialog.setMessage("Apakah anda yakin untuk keluar?");
                alertDialog.setIcon(R.drawable.logout);
                alertDialog.setPositiveButton("YA",
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
                alertDialog.setNegativeButton("TIDAK",
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
}
