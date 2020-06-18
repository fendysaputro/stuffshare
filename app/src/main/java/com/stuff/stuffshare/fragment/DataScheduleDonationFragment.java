package com.stuff.stuffshare.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.stuff.stuffshare.R;
import com.stuff.stuffshare.StuffShareApp;
import com.stuff.stuffshare.adapter.CollectDonationListAdapter;
import com.stuff.stuffshare.adapter.DataScheduleAdapter;
import com.stuff.stuffshare.model.CollectDonation;
import com.stuff.stuffshare.util.GeoLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class DataScheduleDonationFragment extends Fragment implements OnMapReadyCallback {
    StuffShareApp stuffShareApp;
    public static final String[] titleBarang = new String[]{
            " ",
            " "
    };

    public static final Integer[] images = {
            R.drawable.dok1,
            R.drawable.dok1
    };
    GridView gridView;
    ArrayList<CollectDonation> collectDonationList;
    CollectDonationListAdapter collectDonationListAdapter = null;
    MapView mapView;
    GoogleMap googleMap;
    TextView penggalangName, masaDonation, jadwalDonation, countDonation, sisaHari, statusCampaign, story;
    String locationAddress;;
    double lat = 0;
    double lng = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_schedule_donation, container, false);
        stuffShareApp = (StuffShareApp) getActivity().getApplication();

        TextView toolbar_title = view.findViewById(R.id.toolbar_title);
        toolbar_title.setText("Data Jadwal Donasi");
        toolbar_title.setTextColor(getResources().getColor(R.color.textColorToolbar));
        toolbar_title.setTextSize(30);

        ImageView imageView = (ImageView) view.findViewById(R.id.iVdataPenggalang);
        String imageId = this.getArguments().getString("IMAGE_NAME");
        if (imageId.equals("")){
            imageView.setImageResource(R.drawable.logo);
        } else {
            Picasso.with(getActivity())
                    .load(imageId)
                    .fit()
                    .into(imageView);
        }

        ImageView iVComunity = (ImageView) view.findViewById(R.id.imageViewCommunity);
        Picasso.with(getActivity())
                .load(stuffShareApp.getSelectedCampaigner().getImageCom())
                .fit()
                .into(iVComunity);

        penggalangName = (TextView) view.findViewById(R.id.tVPenggalangName);
        masaDonation = (TextView) view.findViewById(R.id.tVRangeDonation);
        jadwalDonation = (TextView) view.findViewById(R.id.tVScheduleDonation);
        countDonation = (TextView) view.findViewById(R.id.tVCountDonation);
        sisaHari = (TextView) view.findViewById(R.id.tVjangkaDonasi);
        statusCampaign = (TextView) view.findViewById(R.id.tVStatusDonation);
        story = (TextView) view.findViewById(R.id.storyBody);
        penggalangName.setText(stuffShareApp.getSelectedCampaigner().getOrganization());
        masaDonation.setText(stuffShareApp.getSelectedCampaigner().getSisaHari());
        jadwalDonation.setText(stuffShareApp.getSelectedCampaigner().getTglBuat());
        countDonation.setText(stuffShareApp.getSelectedCampaigner().getCountDonation());
        sisaHari.setText(stuffShareApp.getSelectedCampaigner().getMasaDonasi());
        statusCampaign.setText(stuffShareApp.getSelectedCampaigner().getStatusCampaign());
        story.setText(stuffShareApp.getSelectedCampaigner().getStory());

        gridView = (GridView) view.findViewById(R.id.itemGridViewDocumentation);
        gridView.setAdapter(new DataScheduleAdapter(getContext()));

        String address = stuffShareApp.getSelectedCampaigner().getAlamatPenyelenggara();
        Log.i(stuffShareApp.TAG, "alamat penyelenggara " + address);

        GeoLocation location = new GeoLocation();
        location.getAddressFromLocation(address, getActivity(), new GeocoderHandler());

        onViewCreated(view, savedInstanceState);

        return view;
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
//            latLongTV.setText(locationAddress);
            Log.i(stuffShareApp.TAG, "locationAddresss " + locationAddress);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = (MapView) view.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.setMinZoomPreference(12);
//        LatLng ny = new LatLng(lat, lng);
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(ny));
//        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
//        googleMap.setMyLocationEnabled(true);
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(43.1, -87.9), 10);
//        googleMap.animateCamera(cameraUpdate);
//        googleMap = map;

        // Add a marker in Sydney and move the camera
        Log.i(stuffShareApp.TAG, "location " + locationAddress);
        if (locationAddress != null){
            String[] address = locationAddress.split("\n");
            lat = Double.parseDouble(address[0]);
            lng = Double.parseDouble(address[1]);
            LatLng newAddress = new LatLng(lat, lng);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(newAddress));
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            googleMap.setMyLocationEnabled(true);
            googleMap.addMarker(new MarkerOptions().position(newAddress).title("Marker new Address"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(newAddress));
        } else {
            Toasty.warning(getActivity(), "Location " + locationAddress, Toasty.LENGTH_SHORT, true).show();
        }
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
