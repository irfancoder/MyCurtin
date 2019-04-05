package com.irfan.draft1.Maps;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
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

/**
 * Created by User on 5/26/2017.
 */

public class MapTabFragment extends Fragment implements OnMapReadyCallback, ValueEventListener, View.OnClickListener {


    private final double DEFAULT_LATITUDE = 4.501269;
    private final double DEFAULT_LONGITUDE = 114.0217789;
    private final float DEFAULT_ZOOM = 16;

    public final static String BUS_COORDINATES_FILE_NAME = "busStopCoordinate.cb";
    private List<Coordinates> BS_Coordinates = new ArrayList<>();
    private List<Marker> markerBS = new ArrayList<>();

    private String[] busStopName = {"Campus_BS", "Villa_BS", "Village_BS", "NamLeong_BS", "DesaSenadin_BS", "WaterI_BS", "WaterII_BS", "Bintang_BS", "Permaisuri_BS", "NewWorld_BS"};
    private GoogleMap mGoogleMap;
    private MapView mapView;
    private View view;
    private LocationManager locationManager;
    private Animation anim;
    private FloatingActionMenu fab;
    private com.github.clans.fab.FloatingActionButton fab_busA, fab_busB;
    private static final int MY_PERMISSION_FINE = 101;


    private CameraUpdate updateBusA, updateBusB, defaultCamera;
    private ImageButton fullscreen;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference busARefer, busBRefer;
    private DatabaseReference[] busStopCoordinates = new DatabaseReference[10];

    private Marker markerBusA, markerBusB;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.map_fragment, container, false);
        mapView = view.findViewById(R.id.mapView);


        if (mapView != null) {

            mapView.onCreate(savedInstanceState);
            mapView.onResume();
            mapView.getMapAsync(this);


        }


        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fab = view.findViewById(R.id.floatingActionMenu);
        fab_busA = view.findViewById(R.id.busA);
        fab_busB = view.findViewById(R.id.busB);
        fullscreen = view.findViewById(R.id.full_screen);

        anim = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_in);

        fab_busA.setOnClickListener(this);
        fab_busB.setOnClickListener(this);
        fullscreen.setOnClickListener(this);


    }


    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();

        busARefer = database.getReference("Bus A");
        busBRefer = database.getReference("Bus B");
        busARefer.addValueEventListener(this);
        busBRefer.addValueEventListener(this);
        for (int i = 0; i < busStopCoordinates.length; i++) {
            busStopCoordinates[i] = database.getReference(busStopName[i]);
            busStopCoordinates[i].addListenerForSingleValueEvent(this);
        }


    }

    @Override
    public void onStop() {
        super.onStop();
        if (busARefer != null || busBRefer != null) {
            busBRefer.removeEventListener(this);
            busARefer.removeEventListener(this);
            for (int i = 0; i < busStopCoordinates.length; i++) {
                busStopCoordinates[i].removeEventListener(this);

            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();

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


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(getContext());
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(getContext());
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(getContext());
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });
//        mGoogleMap.setLatLngBoundsForCameraTarget(boundaries);
        goToLocationZoom(DEFAULT_LATITUDE, DEFAULT_LONGITUDE, DEFAULT_ZOOM);


//        try {
//            // Customise the styling of the base map using a JSON object defined
//            // in a raw resource file.
//            boolean success = mGoogleMap.setMapStyle(
//                    MapStyleOptions.loadRawResourceStyle(
//                            getContext(), R.raw.map_style_1));
//
//            if (!success) {
//                Log.e("MapsActivityRaw", "Style parsing failed.");
//            }
//        } catch (Resources.NotFoundException e) {
//            Log.e("MapsActivityRaw", "Can't find style.", e);
//        }

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

//        mGoogleMap.setMyLocationEnabled(true);
//        View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
//        // and next place it, on bottom right (as Google Maps app)
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
//                locationButton.getLayoutParams();
//        // position on right bottom
//        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
//        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
//        layoutParams.setMargins(0, 0, 30, 120);


        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {

            mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
//                    askLocationEnabled();
                    return false;
                }
            });
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]
                    {
                            android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_FINE);

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_FINE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    }
//                    mGoogleMap.setMyLocationEnabled(true);

                    break;
                } else {

                }
        }
    }


//    public void setCoordinatesArguments(final Bundle args) {
//        getActivity().runOnUiThread(new Runnable() {
//            public void run() {
//                if (args != null) {
//
//                    CAMPUS_COORDINATE = (Coordinates) args.getSerializable("CAMPUS_BS");
//                    VILLA_COORDINATE = (Coordinates) args.getSerializable("VILLA_BS");
//                    VILLAGE_COORDINATE = (Coordinates) args.getSerializable("VILLAGE_BS");
//                    NAMLEONG_COORDINATE = (Coordinates) args.getSerializable("NAMLEONG_BS");
//                    DESASENADIN_COORDINATE = (Coordinates) args.getSerializable("DESASENADIN_BS");
//                    WATERI_COORDINATE = (Coordinates) args.getSerializable("WATERI_BS");
//                    WATERII_COORDINATE = (Coordinates) args.getSerializable("WATERII_BS");
//
//
//
//                }
//            }
//        });
//    }


    private void goToLocationZoom(double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat, lng);
        defaultCamera = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mGoogleMap.moveCamera(defaultCamera);


    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Coordinates update = dataSnapshot.getValue(Coordinates.class);
        String motion;

        switch (dataSnapshot.getKey()) {
            case "Bus A":
                if (update != null) {

                    LatLng busA = new LatLng(update.getLatitude(), update.getLongitude());


                    updateBusA = CameraUpdateFactory.newLatLngZoom(busA, DEFAULT_ZOOM);
                    if (markerBusA != null) {
                        markerBusA.remove();
                    }
                    MarkerOptions markerOptions = new MarkerOptions().position(busA).
                            title("Bus A");
//                    if (!update.isVacant())
//                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_rate));
//                    else if (update.isBrokenDown()) {
//                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_breakdown));
//
//                    } else {
//                 //       markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus_stop_set));
//                    }
//
//
//                    if (update.isMoving()) {
//                        motion = "Moving";
//                    } else if (update.isBrokenDown())
//                    {
//                        motion = "Breakdown";
//                    }
//                    else{
//                        motion = "Parking";
//                    }

//                    markerOptions.snippet("Status: " + motion +
//                            "\nSpeed: " + update.getSpeed() + " km/h");
//                    markerBusA = mGoogleMap.addMarker(markerOptions);
                }
                break;

            case "Bus B":
                if (update != null) {
                    LatLng busB = new LatLng(update.getLatitude(), update.getLongitude());


                    updateBusB = CameraUpdateFactory.newLatLngZoom(busB, DEFAULT_ZOOM);
                    if (markerBusB != null) {
                        markerBusB.remove();
                    }
                    MarkerOptions markerOptions = new MarkerOptions().position(busB).
                            title("Bus B");
//                    if (!update.isVacant())
//                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_rate));
//                    else if (update.isBrokenDown()) {
//                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_breakdown));
//                    } else {
//                        //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus_stop_set));
//                    }
//
//                    if (update.isMoving()) {
//                        motion = "Moving";
//                    } else if (update.isBrokenDown())
//                    {
//                        motion = "Breakdown";
//                    }
//                    else{
//                        motion = "Parking";
//                    }
//                    markerOptions.snippet("Status: " + motion +
//                            "\nSpeed: " + update.getSpeed() + " km/h");
                    markerBusB = mGoogleMap.addMarker(markerOptions);

                }
                break;
            default:
                if (update != null) {
//                    LatLng tempLatLng = new LatLng(update.getLatitude(), update.getLongitude());
//                    MarkerOptions markerOptions = new MarkerOptions().position(tempLatLng).
//                            title(update.getName()).
//                            icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_stop_icon));
//
//                    mGoogleMap.addMarker(markerOptions);
//
//                    mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                        @Override
//                        public boolean onMarkerClick(Marker marker) {
//                            CameraUpdate temp = CameraUpdateFactory.newLatLngZoom(marker.getPosition(), DEFAULT_ZOOM);
//                            mGoogleMap.animateCamera(temp);
//                            return false;
//                        }
//                    });


                }

        }


    }


    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.full_screen:
                Intent intent = new Intent(getActivity(), MapFullscreen.class);
                Pair<View, String> pairMenu = Pair.create(fab, "floatingActionMenu");
                Pair<View, String> pairMap = Pair.create(mapView, "fullscreenMap");
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(getActivity(), pairMap, pairMenu);
                startActivity(intent, optionsCompat.toBundle());
                getActivity().getWindow().setSharedElementsUseOverlay(true);
                break;
            case R.id.busA:
                if (updateBusA != null) {
                    mGoogleMap.animateCamera(updateBusA);
                    markerBusA.showInfoWindow();
                }
                break;
            case R.id.busB:
                if (updateBusB != null) {
                    mGoogleMap.animateCamera(updateBusB);
                    markerBusB.showInfoWindow();
                }

        }
    }


//    public void askLocationEnabled() {
//        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//            builder.setTitle(R.string.gps_not_found_title);  // GPS not found
//            builder.setMessage(R.string.gps_not_found_message); // Want to enable?
//            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                }
//            });
//            builder.setNegativeButton(R.string.no, null);
//            builder.show();
//        }
//    }


}




