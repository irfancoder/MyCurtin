package com.irfan.draft1.Maps;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.transition.Transition;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.irfan.draft1.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by irfan on 02/07/2017.
 */

public class MapFullscreen extends AppCompatActivity implements OnMapReadyCallback, ValueEventListener, Transition.TransitionListener, GoogleMap.OnMarkerClickListener {

    private static final double DEFAULT_LATITUDE = 4.501269;
    private static final double DEFAULT_LONGITUDE = 114.0217789;
    private final float DEFAULT_ZOOM = 16;
    private Transition fade;

    private FloatingActionMenu fam;
    private MapView mapView;
    private GoogleMap googleMapS;

    private List<BusInfo> busCoordinate = new ArrayList<>();
    private List<Marker> busMarkers = new ArrayList<>();
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference busReference, busStopReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_fullscreen);
        mapView = findViewById(R.id.fullscreenMapID);
        if (mapView != null) {

            mapView.onCreate(savedInstanceState);
            mapView.onResume();
            mapView.getMapAsync(this);
            fam = findViewById(R.id.floatingActionMenu);


            fade = getWindow().getSharedElementEnterTransition();
            fade.addListener(this);

        }
        com.github.clans.fab.FloatingActionButton feedback = findViewById(R.id.post_feedback);
        feedback.setOnClickListener(view -> {
            FeedbackFragment fragment = new FeedbackFragment();
            fragment.setEnterTransition(new Slide(Gravity.END));
            fragment.setExitTransition(new Slide(Gravity.END));
            getSupportFragmentManager().beginTransaction().replace(R.id.map_container,fragment).addToBackStack(null).commit();
        });

        com.github.clans.fab.FloatingActionButton info = findViewById(R.id.post_info);
        info.setOnClickListener(view -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Live Bus Tracking (Beta)");
            alert.setIcon(R.drawable.mycurtinlogo);
            alert.setMessage("The bus tracking feature is currently on beta and we are looking for feedbacks & suggestions to make it better. Thank you and sorry for the inconvenience. \n\nWith love, MyCurtin team");
            alert.setPositiveButton("OK", null);
            alert.show();
        });

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMapS = googleMap;
        googleMapS.getUiSettings().setMapToolbarEnabled(false);
        googleMapS.setOnMarkerClickListener(this);


        try {
            boolean success = googleMapS.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.map_style_1));

            if (!success) {
                Log.e("MapsActivityRaw", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivityRaw", "Can't find style.", e);
        }
        goToLocationZoom(DEFAULT_LATITUDE, DEFAULT_LONGITUDE, DEFAULT_ZOOM);


    }

    private void goToLocationZoom(double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate defaultCamera = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        googleMapS.animateCamera(defaultCamera);


    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();


        busReference = database.getReference("LiveBusTracking");
        busStopReference = database.getReference("CurtinBusStops");
        busReference.addValueEventListener(this);
        busStopReference.addListenerForSingleValueEvent(this);


    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
        if (busReference != null) {
            busReference.removeEventListener(this);
            busStopReference.removeEventListener(this);


        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


        switch (Objects.requireNonNull(dataSnapshot.getKey())) {
            case "LiveBusTracking":
                busCoordinate.clear();
                busMarkers.clear();
                googleMapS.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    BusInfo update = postSnapshot.getValue(BusInfo.class);
                    busCoordinate.add(update);
                    MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(Objects.requireNonNull(update).getCoordinates().getLatitude(), update.getCoordinates().getLongitude()))
                                .icon(BitmapDescriptorFactory.fromBitmap(getBitmap(getApplicationContext(), R.drawable.ic_bus_icon)));
                    if (update.getCoordinates().getBearing()!=0)
                    {
                        markerOptions.rotation(update.getCoordinates().getBearing());
                    }
                    Marker initialMarker = googleMapS.addMarker(markerOptions);
                    //changePositionSmoothly(initialMarker,initialMarker.getPosition());

                    initialMarker.setTag(update);
                    busMarkers.add(initialMarker);
                }
                updateBusSelection(busCoordinate);

                Log.d("LOG","busCoordinateSize: "+busCoordinate.size()+"\nbusMarkersSize: "+busMarkers.size());
                break;
            case "CurtinBusStops":
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    BusStop busStopPoint = postSnapshot.getValue(BusStop.class);
                    if (busStopPoint != null) {
                        LatLng tempLatLng = new LatLng(busStopPoint.getLatitude(), busStopPoint.getLongitude());
                        MarkerOptions markerOptions = new MarkerOptions().position(tempLatLng).
                                title(busStopPoint.getName());

                        Marker temp = googleMapS.addMarker(markerOptions);
                        temp.showInfoWindow();


                        googleMapS.setOnMarkerClickListener(marker -> {
                            marker.showInfoWindow();

                            return true;
                        });
                    }
                }

            default:
//                if (update != null) {
//                    LatLng tempLatLng = new LatLng(update.getLatitude(), update.getLongitude());
//                    MarkerOptions markerOptions = new MarkerOptions().position(tempLatLng).
//                            title(update.getName()).
//                            icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_stop));
//                    googleMapS.addMarker(markerOptions);
//
//
//                    googleMapS.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                        @Override
//                        public boolean onMarkerClick(Marker marker) {
//                            CameraUpdate temp = CameraUpdateFactory.newLatLngZoom(marker.getPosition(), DEFAULT_ZOOM);
//                            googleMapS.animateCamera(temp);
//
//
//                            return false;
//                        }
//                    });
//                }
        }


    }

    private void changePositionSmoothly(Marker marker, LatLng newLatLng) {
        if (marker == null) {
            return;
        }
        ValueAnimator animation = ValueAnimator.ofFloat(0f, 100f);
        final float[] previousStep = {0f};
        double deltaLatitude = newLatLng.latitude - marker.getPosition().latitude;
        double deltaLongitude = newLatLng.longitude - marker.getPosition().latitude;
        animation.setDuration(1500);
        animation.addUpdateListener(animation1 -> {
            float deltaStep = (Float) animation1.getAnimatedValue() - previousStep[0];
            previousStep[0] = (Float) animation1.getAnimatedValue();
            marker.setPosition(new LatLng(marker.getPosition().latitude + deltaLatitude * deltaStep * 1 / 100, marker.getPosition().latitude + deltaStep * deltaLongitude * 1 / 100));
        });
        animation.start();
    }

    private void updateBusSelection(List<BusInfo> busAvailable) {
        TabLayout busChooser = findViewById(R.id.bus_chooser);

        if (busChooser.getTabCount()==0) {
            for (BusInfo bus : busAvailable) {
                busChooser.addTab(busChooser.newTab().setText(bus.getName()));
            }
        }
        busChooser.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showBusInfo(busMarkers.get(tab.getPosition()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                showBusInfo(busMarkers.get(tab.getPosition()));

            }
        });
       showBusInfo(busMarkers.get(busChooser.getSelectedTabPosition()));


    }


    //private void updateBusInfo(List<Marker> busMarkers) {
//        if (busMarkers != null) {
//            for (Marker update : busMarkers) {
//                Marker tempMarker = googleMapS.addMarker()
//                LatLng temp = new LatLng(update.getCoordinates().getLatitude(), update.getCoordinates().getLongitude());
//                MarkerOptions markerOptions = new MarkerOptions().position(temp)
//                                .rotation(update.getCoordinates().getBearing())
//                                .icon(BitmapDescriptorFactory.fromBitmap(getBitmap(getApplicationContext(), R.drawable.ic_bus_icon)));

//                for (Marker initialMarker : busMarkers) {
//                    if (Objects.equals(initialMarker.getTag(), update)) {
//                        initialMarker.remove();
//                        MarkerOptions markerOptions = new MarkerOptions()
//                                .position(temp)
//                                .rotation(update.getCoordinates().getBearing())
//                                .icon(BitmapDescriptorFactory.fromBitmap(getBitmap(getApplicationContext(), R.drawable.ic_bus_icon)));
//                        Marker tempMarker = googleMapS.addMarker(markerOptions);
//                        tempMarker.setTag(update);
//                        busMarkers.set(busMarkers.indexOf(initialMarker), tempMarker);
//
//
//                    }
//                }
//            }
//        }
//    }

    private static Bitmap getBitmap(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (drawable instanceof BitmapDrawable) {
            return BitmapFactory.decodeResource(context.getResources(), drawableId);
        } else if (drawable instanceof VectorDrawable) {
            return getBitmap((VectorDrawable) drawable);
        } else {
            throw new IllegalArgumentException("unsupported drawable type");
        }
    }

    private static Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        // Log.e(TAG, "getBitmap: 1");
        return bitmap;
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }


    @Override
    public void onTransitionStart(Transition transition) {

    }

    @Override
    public void onTransitionEnd(Transition transition) {
        fam.open(true);
        fade.removeListener(this);

    }

    @Override
    public void onTransitionCancel(Transition transition) {

    }

    @Override
    public void onTransitionPause(Transition transition) {

    }

    @Override
    public void onTransitionResume(Transition transition) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        CameraUpdate updateBusCamera = CameraUpdateFactory.newLatLngZoom(marker.getPosition(), DEFAULT_ZOOM);
        showBusInfo(marker);
        googleMapS.animateCamera(updateBusCamera);
        return true;
    }

    private void showBusInfo(Marker marker) {
        marker.showInfoWindow();
        Double lat,lng;
        lat = marker.getPosition().latitude;
        lng = marker.getPosition().longitude;
        goToLocationZoom(lat,lng,DEFAULT_ZOOM);


        CircleImageView busImage = findViewById(R.id.bus_image);
        Glide.with(this).load(((BusInfo) Objects.requireNonNull(marker.getTag())).getImageURL()).into(busImage);
        TextView busNamePanel = findViewById(R.id.busName_panel);
        //TextView busDriverPanel = findViewById(R.id.busdriver_name);
        TextView busPlateNumber = findViewById(R.id.plateNumber);
        TextView busSpeed = findViewById(R.id.bus_speed);
        busNamePanel.setText(((BusInfo) Objects.requireNonNull(marker.getTag())).getName());
        //busDriverPanel.setText("Driven by "+((BusInfo) Objects.requireNonNull(marker.getTag())).getDriverName());
        busPlateNumber.setText(((BusInfo) Objects.requireNonNull(marker.getTag())).getPlateNumber());
        busSpeed.setText(String.valueOf(((BusInfo) Objects.requireNonNull(marker.getTag())).getCoordinates().getSpeed()));


    }
}