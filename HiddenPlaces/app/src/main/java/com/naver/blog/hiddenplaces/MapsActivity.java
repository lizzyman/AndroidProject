package com.naver.blog.hiddenplaces;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        requestPermission();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void requestPermission() {
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            // 사용자가 해당 권한을 이미 허용했는지 여부를 가져옴.
            int permission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);

            if ( permission != PackageManager.PERMISSION_GRANTED ) {
                // 사용자에게 권한을 요청함
                // 10001 : 요청코드
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 10001 );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if ( requestCode == 10001 && grantResults[0] == PackageManager.PERMISSION_DENIED ) {
            Toast.makeText(this, "위치 정보를 사용하지 않으면 지도를 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);

        // Add a marker in Sydney and move the camera
/*        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        if ( mMap != null ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
            }
        }
        List<Address> geocodeMatches = null;
        double latitude = 0;
        double longitude = 0;

        try {
            geocodeMatches = new Geocoder(this).getFromLocationName("Seoul",1);
            if ( !geocodeMatches.isEmpty() ){
                latitude = geocodeMatches.get(0).getLatitude();
                longitude = geocodeMatches.get(0).getLongitude();

                LatLng place = new LatLng(latitude,longitude);

                CameraPosition cameraPosition = new CameraPosition.Builder().target(place).zoom(15).build();

                //mMap.moveCamera(CameraUpdateFactory.newLatLng(place));
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                mMap.addMarker(new MarkerOptions().position(place).title("Seoul"));
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        Toast.makeText(MapsActivity.this, marker.getTitle(), Toast.LENGTH_SHORT).show();
                    }
                });
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Toast.makeText(MapsActivity.this, marker.getTitle(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MapsActivity.this, DetailActivity.class);
                        startActivity( intent);

                        return false;
                    }
                });


                Toast.makeText(this, "lat : "+ latitude+" , lon : " + longitude, Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Empty!" , Toast.LENGTH_SHORT).show();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }


    }
}