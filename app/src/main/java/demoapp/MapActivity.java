package demoapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.dji.sdk.sample.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.jetbrains.annotations.NotNull;

import demoapp.dronecontroller.DroneManager;
import demoapp.dronecontroller.interfaces.OnAsyncOperationComplete;
import demoapp.dronecontroller.interfaces.OnAsyncOperationCompleteBool;
import demoapp.dronecontroller.interfaces.OnMissionRunningListener;
import demoapp.dronecontroller.interfaces.OnStateChangeListener;
import demoapp.mission.CameraParameter;
import demoapp.mission.GridSetting;
import demoapp.mapping.Grid;
import demoapp.mapping.Mapping;
import demoapp.mapping.PointLatLngAlt;
import demoapp.mapping.Utils;
import demoapp.dronecontroller.MappingApplication;
import demoapp.dronecontroller.interfaces.OnGPSStateChange;
import demoapp.dronecontroller.presenters.LocationManager;
import demoapp.dronecontroller.presenters.PermissionManager;
import demoapp.dronecontroller.utils.Constants;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import demoapp.mission.MissionDJI;
import dji.common.camera.SettingsDefinitions;
import dji.common.error.DJIError;
import dji.common.flightcontroller.LocationCoordinate3D;
import dji.common.flightcontroller.simulator.InitializationData;
import dji.common.mission.waypoint.Waypoint;
import dji.common.mission.waypoint.WaypointMission;
import dji.common.mission.waypoint.WaypointMissionDownloadEvent;
import dji.common.mission.waypoint.WaypointMissionExecutionEvent;
import dji.common.mission.waypoint.WaypointMissionFinishedAction;
import dji.common.mission.waypoint.WaypointMissionHeadingMode;
import dji.common.mission.waypoint.WaypointMissionState;
import dji.common.mission.waypoint.WaypointMissionUploadEvent;
import dji.common.model.LocationCoordinate2D;
import dji.common.product.Model;
import dji.common.util.CommonCallbacks;
import dji.sdk.base.BaseProduct;
import dji.sdk.camera.Camera;
import dji.sdk.codec.DJICodecManager;
import dji.sdk.flightcontroller.FlightController;
import dji.sdk.flightcontroller.Simulator;
import dji.sdk.mission.waypoint.WaypointMissionOperator;
import dji.sdk.mission.waypoint.WaypointMissionOperatorListener;
import dji.sdk.products.Aircraft;
import dji.sdk.sdkmanager.DJISDKManager;

public class MapActivity extends AppCompatActivity {
    private static final String TAG = MapActivity.class.getName();
    private GoogleMap map = null;
    public MapView mapView;
    private ImageButton btnClean, btnStart;
    Polyline polyline;
    PolylineOptions polylineOptions;
    protected DJICodecManager mCodecManager = null;
    private Aircraft mProduct = null;
    private Double currentDroneLatitude = Constants.DOUBLE_NULL;
    private Double currentDroneLongitude = Constants.DOUBLE_NULL;
    public static WaypointMission.Builder waypointMissionBuilder;
    private FlightController mFlightController;
    private WaypointMissionOperator instance;
    private WaypointMissionFinishedAction mFinishedAction = WaypointMissionFinishedAction.GO_HOME;
    private WaypointMissionHeadingMode mHeadingMode = WaypointMissionHeadingMode.AUTO;
    private final Map<Integer, Marker> mMarkers = new ConcurrentHashMap<Integer, Marker>();
    private Marker droneMarker = null;
    private CopyOnWriteArrayList<Waypoint> waypointList = new CopyOnWriteArrayList<>();
    private float ALTITUDE_PREFERED = 45.0f;
    public  static float distanceInMeter = 5;
    private float mSpeed = 2.0f;
    private float photoEvery = 0f;
    private boolean isHomeLocationSet = false;
    private static boolean showPermissionDialog;
    private static boolean isFirstTime = true;
    protected boolean isReqDone;
    protected double latitude, longitude;
    private boolean updatingUI = false, searchingForUserPosition= false;
    CopyOnWriteArrayList<LatLng> coords = new CopyOnWriteArrayList<>();
    CopyOnWriteArrayList<LatLng> photoPoints = new CopyOnWriteArrayList<>();
    private @Nullable GridSetting setting = null;
    public static MapActivity mapActivity = null;


    public boolean isSearchingForUserPosition() {
        return searchingForUserPosition;
    }

    public void setSearchingForUserPosition(boolean searchingForUserPosition) {
        this.searchingForUserPosition = searchingForUserPosition;
    }

    public boolean isUpdatingUI() {
        return updatingUI;
    }

    public void setUpdatingUI(boolean updatingUI) {
        this.updatingUI = updatingUI;
    }

    private void clearMap() {
        if (Utils.isNull(map)){
            return;
        }else{
            clearPolyline();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    map.clear();
                }
            });

        }
    }

    public void toLogUI(String msg){
        if (msg ==  null){
            Log.d(TAG, " toLogUI msg IS NULL");
            return;
        }else{
            Log.d(TAG, " toLogUI "+msg);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LinearLayoutCompat uiContainer =  (LinearLayoutCompat) findViewById(R.id.containerId);
                    TextView t = new TextView(MapActivity.this);
                    t.setText(msg);
                    uiContainer.addView(t);
                }
            });
        }
    }

    private void clearPolyline() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /*Remove hold polygone*/
                if (polyline != null){
                    polyline.remove();
                    polylineOptions = null;
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        toLogUI("onCreate Start ");
        mapActivity = this;

        Toolbar toolbar              = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        LocationManager.createInstance(MapActivity.this, new OnGPSStateChange() {
            @Override
            public void onGPSDisable() {

            }

            @Override
            public void onGPSEnable() {

            }

            @Override
            public void onPermissionNotGranted() {

            }

            @Override
            public void onUserPositionChanged(@Nullable Double uLatitude, @Nullable  Double uLongitude) {
                latitude = uLatitude;
                longitude = uLongitude;
            }
        });
        LocationManager.getInstance().startNotify();

        iniViews(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            iniListeners();
        }

        iniPermissions();

        mapView.getMapAsync(googleMap ->{
                    toLogUI("getMapAsync Start ");
                    map = googleMap;
                    map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    map.getUiSettings().setMapToolbarEnabled(false);
                    map.getUiSettings().setAllGesturesEnabled(true);
                    coords =    getPointsAsLatLng();

                    // First Time
                    addPolyLine(map);
                    moveCamera(coords);

                    new Handler(Looper.getMainLooper()).postDelayed(this::initFlightController, TimeUnit.SECONDS.toMillis(5));
                    DroneManager instance = DroneManager.getInstance();
                    if (Utils.isNull(instance)){
                        toLogUI("getMapAsync DroneManager instance isNull");
                    }else {

                    }
                }
        );
        toLogUI("onCreate End ");

    }


    private void iniViews(Bundle savedInstanceState) {


        btnStart            = (ImageButton) findViewById(R.id.startImgBtn);
        btnClean            = (ImageButton) findViewById(R.id.cleanImgBtn);
        mapView = (MapView) findViewById(R.id.mapView);
        try {
            mapView.onCreate(savedInstanceState);
            mapView.onResume(); // needed to get the map to display immediately
            MapsInitializer.initialize(this);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void iniPermissions() {
        toLogUI("iniPermissions Start ");
        PermissionManager pm = new PermissionManager(this);

        //check permission
        if(!pm.isPermissionsGranted()){
            toLogUI("iniPermissions Not Granted ");
            if(isFirstTime){
                showPermissionDialog = false;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            pm.requestAllPermissions();
                            isReqDone = true;
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, TimeUnit.SECONDS.toMillis(1));
                isFirstTime = false;
            }
        }else {
            toLogUI("iniPermissions Already granted ");
            isReqDone = true;
            LocationManager.createInstance(this, new OnGPSStateChange() {
                @Override
                public void onGPSDisable() {
                    toLogUI("LocationManager onGPSDisable ");
                }

                @Override
                public void onGPSEnable() {
                    toLogUI("LocationManager onGPSEnable ");
                }

                @Override
                public void onPermissionNotGranted() {
                    toLogUI("LocationManager onPermissionNotGranted ");
                }

                @Override
                public void onUserPositionChanged(@Nullable  Double uLatitude, @Nullable Double uLongitude) {
                    // toLogUI("onUserPositionChanged ");
                }
            });
            LocationManager locationManager = LocationManager.getInstance();
            if (demoapp.dronecontroller.utils.Utils.isNull(locationManager)){
                toLogUI("iniPermissions locationManager is Null ");
            }else {
                locationManager.startNotify();
                toLogUI("iniPermissions End ");
            }
        }
    }

    protected BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void iniListeners() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(MappingApplication.FLAG_CONNECTION_CHANGE);
        registerReceiver(mReceiver, filter);

        btnStart.setVisibility(View.VISIBLE);

        setUpdatingUI(false);
        setSearchingForUserPosition(false);

//        btnStart.setOnClickListener(onclick ->{
//
//        });
//        btnClean.setOnClickListener(onclick->{
//        });
        btnStart.setOnClickListener(onclick ->{
            toLogUI(" setStateCallback btnStart Pressed");
            DroneManager instance = DroneManager.getInstance();
            if (Utils.isNull(instance)){
                toLogUI("iniListeners DroneManager instance isNull");
            }else {
                if (Utils.isNull(MapActivity.this.photoPoints)){
                    MapActivity.this.photoPoints = new CopyOnWriteArrayList<>();
                }
                MapActivity.this.photoPoints.clear();
                instance.startWaypointMission(MapActivity.this, getPointsAsLatLng(), 70f, true, false, true,latitude,longitude,
                        new OnMissionRunningListener() {
                            @Override
                            public void onError(@Nullable String errorDetail) {
                                toLogUI(" startWaypointMission onError ");
                                toLogUI(" startWaypointMission onError "+errorDetail);
                            }

                            @Override
                            public void onCalibrationRequire(@Nullable @org.jetbrains.annotations.Nullable String detail) {

                            }

                            @Override
                            public void onStartSucces() {
                                toLogUI(" startWaypointMission onStartSucces ");
                            }

                            @Override
                            public void onStepSucces(int stepLeft) {
                                toLogUI(" startWaypointMission stepLeft :"+stepLeft);
                            }

                            @Override
                            public void onSucces() {
                                toLogUI(" startWaypointMission onSucces ");
                                // allow next Time
                                if (Utils.isNull(MapActivity.this.photoPoints)){
                                    MapActivity.this.photoPoints = new CopyOnWriteArrayList<>();
                                }
                                MapActivity.this.photoPoints.clear();
                            }

                            @Override
                            public void onGridProgressing(int progress) {
                                toLogUI("startWaypointMission onGridProgressing  progress:"+progress);
                            }

                            @Override
                            public void onNotifyState(int battery, int imageCount, float speed, CopyOnWriteArrayList<LatLng> flyPoints) {
                                toLogUI("startWaypointMission onNotifyState  battery:"+battery+ " imageCount:"+imageCount+ " speed:"+speed);
                                if (Utils.isNull(MapActivity.this.photoPoints)){
                                    MapActivity.this.photoPoints = new CopyOnWriteArrayList<>();
                                }
                                MapActivity.this.photoPoints.clear();
                                MapActivity.this.photoPoints.addAll(flyPoints);
                            }

                            @Override
                            public void mustGoHome() {
                                instance.returnToHome((new OnAsyncOperationComplete() {
                                    @Override
                                    public void onError(@Nullable @org.jetbrains.annotations.Nullable String errorDetail) {
                                        toLogUI("startWaypointMission returnToHome  Error");
                                    }

                                    @Override
                                    public void onSucces(@Nullable @org.jetbrains.annotations.Nullable String succesMsg) {
                                        toLogUI("startWaypointMission returnToHome  Succes");
                                    }
                                }));
                            }
                        });
            }
        });
        btnClean.setOnClickListener(onclick->{
            toLogUI(" setStateCallback btnClean Pressed");
            DroneManager instance = DroneManager.getInstance();
            if (Utils.isNull(instance)){
                toLogUI("iniListeners DroneManager instance isNull");
            }else {
                instance.continuWaypointMission(MapActivity.this,new OnMissionRunningListener() {
                    @Override
                    public void onError(@Nullable @org.jetbrains.annotations.Nullable String errorDetail) {
                        toLogUI(" continuWaypointMission onError ");
                    }

                    @Override
                    public void onCalibrationRequire(@Nullable @org.jetbrains.annotations.Nullable String detail) {

                    }

                    @Override
                    public void onStartSucces() {
                        toLogUI(" continuWaypointMission onStartSucces ");
                    }

                    @Override
                    public void onStepSucces(int stepLeft) {
                        toLogUI(" continuWaypointMission stepLeft :"+stepLeft);
                    }

                    @Override
                    public void onSucces() {
                        toLogUI(" continuWaypointMission onSucces ");
                        if (Utils.isNull(MapActivity.this.photoPoints)){
                            MapActivity.this.photoPoints = new CopyOnWriteArrayList<>();
                        }
                        MapActivity.this.photoPoints.clear();
                    }

                    @Override
                    public void onGridProgressing(int progress) {
                        toLogUI("continuWaypointMission onGridProgressing  progress:"+progress);
                    }

                    @Override
                    public void onNotifyState(int battery, int imageCount, float speed, CopyOnWriteArrayList<LatLng> flyPoints) {
                        toLogUI("continuWaypointMission onNotifyState  battery:"+battery+ " imageCount:"+imageCount+ " speed:"+speed);
                        if (Utils.isNull(MapActivity.this.photoPoints)){
                            MapActivity.this.photoPoints = new CopyOnWriteArrayList<>();
                        }
                        MapActivity.this.photoPoints.clear();
                        MapActivity.this.photoPoints.addAll(flyPoints);
                    }

                    @Override
                    public void mustGoHome() {
                        instance.returnToHome((new OnAsyncOperationComplete() {
                            @Override
                            public void onError(@Nullable @org.jetbrains.annotations.Nullable String errorDetail) {
                                toLogUI("continuWaypointMission returnToHome  Error");
                            }

                            @Override
                            public void onSucces(@Nullable @org.jetbrains.annotations.Nullable String succesMsg) {
                                toLogUI("continuWaypointMission returnToHome  Succes");
                            }
                        }));
                    }
                });
            }
        });
    }






    // Update the drone location based on states from MCU.
    private void updateDroneLocation(){
        // toLogUI("updateDroneLocation Start");
        synchronized (MapActivity.this){
            if (isUpdatingUI()){
                // toLogUI("updateDroneLocation Cancel Cause Another thread no ended");
                return;
            }else {
                setUpdatingUI(true);
                if (currentDroneLatitude == Constants.DOUBLE_NULL || currentDroneLongitude == Constants.DOUBLE_NULL){
                    toLogUI("updateDroneLocation Stop Cause currentDroneLatitude or currentDroneLongitude IS  DOUBLE_NULL");
                    setUpdatingUI(false);
                    return;
                }else {
                    if (Utils.isNull(map)){
                        toLogUI("updateDroneLocation Stop Cause map IS Null");
                        setUpdatingUI(false);
                        return;
                    }else {
                        clearMap();

                        LatLng pos = new LatLng(currentDroneLatitude, currentDroneLongitude);
                        //Create MarkerOptions object
                        final MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(pos);
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.aircraft));

                        runOnUiThread(()->{
                            if (droneMarker != null) {
                                droneMarker.remove();
                            }

                            addPolyLine(map);
                            setUpdatingUI(false);

                            if (!checkGpsCoordination(currentDroneLatitude, currentDroneLongitude)) {
                                // toLogUI("updateDroneLocation Bad currentDroneLatitude & currentDroneLongitude");
                                setUpdatingUI(false);
                            }else {
                                droneMarker = map.addMarker(markerOptions);
                                // toLogUI("updateDroneLocation Added SUCCES");
                            }

                        });
                    }
                }
            }
        }
    }

    public static boolean checkGpsCoordination(double latitude, double longitude) {
        return (latitude > -90 && latitude < 90 && longitude > -180 && longitude < 180) && (latitude != 0f && longitude != 0f);
    }

    private void markWaypoint(LatLng point){
        if (Utils.isNull(map)){
            toLogUI("markWaypoint Stop Cause map IS Null");
            return;
        }else {
            //Create MarkerOptions object
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(point);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            Marker marker = map.addMarker(markerOptions);
            mMarkers.put(mMarkers.size(), marker);
        }

    }

    private void addPolyLine(final GoogleMap map) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final CopyOnWriteArrayList<LatLng> points = getPointsAsLatLng();
                if (Utils.isNull(points)){
                    return;
                }else {
                    // draw pointsParcels or polylines
                    polylineOptions = new PolylineOptions();
                    polylineOptions.color(Color.GREEN).width(5);

                    polylineOptions.addAll(points);
                    polyline = map.addPolyline(polylineOptions);

                    // add Marker
                    addMarkerOnMap(points);

                    // moveCamera(points);
                }
            }
        });


    }

    private void addMarkerOnMap(@NonNull final CopyOnWriteArrayList<LatLng> list)  {
        if (list == null){
            return;
        }else {
            if (list.size() == 0){
                return;
            }else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    for (int i=0; i<list.size(); i++) {
                        LatLng item = list.get(i);
                        map.addMarker(new MarkerOptions()
                                .snippet(String.valueOf(i))
                                .title(String.valueOf(i))
                                .position(item)
                                .draggable(false)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                                .visible(true));
                    }

                }
            }
        }


    }

    private void moveCamera(@NonNull final CopyOnWriteArrayList<LatLng> points) {
        if (Utils.isNull(points)){
            return;
        }else {
            if (map != null){
                if (points.size() > 0){
                    final float MAP_MIN_ZOOM_PREFERENCE = 20.5f;
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            points.get(points.size() - 1), MAP_MIN_ZOOM_PREFERENCE));
                }
            }
        }

    }

    private CopyOnWriteArrayList<LatLng> getPointsAsLatLng() {
        if (Utils.isNull(photoPoints)){
            photoPoints = new CopyOnWriteArrayList<>();
        }
        if (photoPoints.size() == 0){

            // TRCI Points6.24 Hect
//            photoPoints.add(new LatLng(5.384115, -4.176556));
//            photoPoints.add(new LatLng(5.384627, -4.174369));
//            photoPoints.add(new LatLng(5.386807, -4.174827));
//            photoPoints.add(new LatLng(5.386335, -4.177043));


//            // office intro 0.6 hectare
            photoPoints.add(new LatLng(5.402561 ,-3.960237));
            photoPoints.add(new LatLng(5.403077,-3.959579));
            photoPoints.add(new LatLng(5.403588,-3.959934));
            photoPoints.add(new LatLng(5.403018,-3.960594));

            // Gestoci Big 10 hectares
//            photoPoints.add(new LatLng(5.402490,-3.961260));
//            photoPoints.add(new LatLng(5.404153,-3.959405));
//            photoPoints.add(new LatLng(5.402092,-3.957283));
//            photoPoints.add(new LatLng(5.399872,-3.959172));

            // Gestoci Big 50 hectares
//            photoPoints.add(new LatLng(5.399192 ,-3.963114));
//            photoPoints.add(new LatLng(5.396231,-3.958972));
//            photoPoints.add(new LatLng(5.398336,-3.952739));
//            photoPoints.add(new LatLng(5.404148,-3.959404));

//            // Gestoci Big 100 hectares
//            photoPoints.add(new LatLng(5.399193,-3.963111));
//            photoPoints.add(new LatLng(5.391413,-3.955525));
//            photoPoints.add(new LatLng(5.396766,-3.948247));
//            photoPoints.add(new LatLng(5.402404,-3.955710));

            // Gestoci Big 125 hectares
//            photoPoints.add(new LatLng(5.399193,-3.963111));
//            photoPoints.add(new LatLng(5.388766,-3.956098));
//            photoPoints.add(new LatLng(5.396766,-3.948247));
//            photoPoints.add(new LatLng(5.402404,-3.955710));
        }

        return photoPoints;
    }



    public WaypointMissionOperator getWaypointMissionOperator() {
        if (instance == null) {
            instance = DJISDKManager.getInstance().getMissionControl().getWaypointMissionOperator();
        }
        return instance;
    }


    private void initFlightController() {
        toLogUI(" initFlightController Start");
        runOnUiThread(()->{
            DroneManager instance = DroneManager.getInstance();
            if (Utils.isNull(instance)){
                toLogUI(" initFlightController instance is Null");
                return;
            }else {
                instance.ini(this);
                instance.connect(new OnAsyncOperationComplete() {
                    @Override
                    public void onError(@Nullable @org.jetbrains.annotations.Nullable String errorDetail) {
                        toLogUI(" initFlightController connect Error");
                    }

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onSucces(@Nullable @org.jetbrains.annotations.Nullable String succesMsg) {
                        toLogUI(" initFlightController connect onSucces");
                        instance.registerToListenDroneState(new OnStateChangeListener() {
                            @Override
                            public void onChange(int uplinkSignalQuality, short satelliteCount, int chargeRemainingInPercent, int chargeRemainingInMAh, float temperature, float altitudeInMeters, float speedInMeterPerSec, float heading, int numberOfDischarges, int lifetimeRemainingInPercent, double longitude, double latitude, float altitude, int droneHeadingInDegrees, @NonNull @NotNull String droneModelName) {
                                currentDroneLatitude = latitude;
                                currentDroneLongitude = longitude;
                                updateDroneLocation();
                            }

                            @Override
                            public void onLog(String msg) {
                                toLogUI(" initFlightController registerToListenDroneState: "+msg);
                            }

                            @Override
                            public void onConnectFailed() {
                                toLogUI(" initFlightController onConnectFailed");
                            }

                            @Override
                            public void onConnectSucces() {
                                toLogUI(" initFlightController onConnectSucces");
                            }
                        });
                        Double androidPhoneLat = latitude;
                        Double androidPhoneLng = longitude;
                        if (androidPhoneLat == Constants.DOUBLE_NULL || androidPhoneLng == Constants.DOUBLE_NULL){
                            toLogUI ("GPS home latitude or home longitude Is DOUBLE_NULL");
                            new Handler(Looper.getMainLooper()).postDelayed(MapActivity.this::initFlightController, TimeUnit.SECONDS.toMillis(5));
                            return;
                        }else {
                            if (androidPhoneLat.isNaN() || androidPhoneLng.isNaN()){
                                toLogUI ("GPS androidPhoneLat or androidPhoneLng Is NaN");
                                new Handler(Looper.getMainLooper()).postDelayed(MapActivity.this::initFlightController, TimeUnit.SECONDS.toMillis(5));
                            }else {
                                runOnUiThread(()->{
                                    CoordinatorLayout menu =  (CoordinatorLayout)findViewById(R.id.coordinatorMenu);
                                    if (Utils.isNull(menu)){
                                        toLogUI(" setStateCallback menu IS Null");
                                        return;
                                    }else {
                                        menu.setVisibility(View.VISIBLE);
                                        toLogUI(" setStateCallback menu IS NOW VISIBLE");
                                    }
                                    return;
                                });
                            }
                        }

                    }
                });
            }


        });
    }

//
//    private void prepareMission(final Aircraft mProduct, final FlightController controller, final LocationCoordinate2D homeLocation) {
//        toLogUI("performMission Start");
//        if (isSearchingForUserPosition()){
//            //toLogUI("performMission Another instance ALready running");
//            return;
//        }else {
//            setSearchingForUserPosition(true);
//            runOnUiThread(()->{
//                toLogUI("performMission Start runOnUiThread");
//                if (Utils.isNull(mProduct)){
//                    toLogUI("performMission mProduct is Null so Stop");
//                    setSearchingForUserPosition(false);
//                    return;
//                }else {
//                    if (Utils.isNull(controller)){
//                        toLogUI("performMission controller is Null so Stop");
//                        setSearchingForUserPosition(false);
//                        return;
//                    }else {
//                        if (Utils.isNull(homeLocation)){
//                            toLogUI("performMission homeLocation is Null so Stop");
//                            setSearchingForUserPosition(false);
//                            return;
//                        }else {
//                            if (mProduct.getModel() == Model.UNKNOWN_AIRCRAFT) {
//                                toLogUI(" mProduct Is UNKNOWN_AIRCRAFT");
//                                setSearchingForUserPosition(false);
//                                return;
//                            }else {
//                                controller.setHomeLocation(homeLocation,locationError ->{
//                                    if (!Utils.isNull(locationError)){
//                                        toLogUI(" setHomeLocation  locationError ");
//                                        toLogUI(" setHomeLocation  locationError Is Error happen :"+locationError.getDescription());
//                                        toLogUI(" setHomeLocation  WILL RETRY SOON "+locationError.getDescription());
//                                        if (!MapActivity.this.isFinishing()){
//                                            setSearchingForUserPosition(false);
//                                            new Handler().postDelayed(this::initFlightController,Constants.DELAY_SHORT);
//                                        }else{
//                                            toLogUI(" setHomeLocation  WILL NOT RETRY cause Activity is Destroy ");
//                                        }
//                                        return;
//                                    }else {
//                                        toLogUI(" setStateCallback Start");
//
//                                        runOnUiThread(()->{
//                                            CoordinatorLayout menu =  (CoordinatorLayout)findViewById(R.id.coordinatorMenu);
//                                            if (Utils.isNull(menu)){
//                                                toLogUI(" setStateCallback menu IS Null");
//                                                return;
//                                            }else {
//                                                menu.setVisibility(View.VISIBLE);
//                                                createGridAndShowIt(coords,map);
//                                                toLogUI(" setStateCallback menu IS NOW VISIBLE");
//                                            }
//                                        });
//                                        toLogUI(" setStateCallback menu IS NOW VISIBLE");
//                                        controller.setStateCallback(state ->{
//                                            if (Utils.isNull(state)){
//                                                toLogUI(" setStateCallback state is Null");
//                                                setSearchingForUserPosition(false);
//                                                return;
//                                            }else {
//                                                // toLogUI(" setStateCallback state fired ");
//                                                // toLogUI(" setStateCallback state fired Time:"+Utils.getToday());
//                                                LocationCoordinate3D location = state.getAircraftLocation();
//                                                if (Utils.isNull(location)){
//                                                    toLogUI(" setStateCallback location is Null");
//                                                    setSearchingForUserPosition(false);
//                                                    return;
//                                                }else {
//                                                    currentDroneLatitude = location.getLatitude();
//                                                    currentDroneLongitude= location.getLongitude();
//                                                    isHomeLocationSet    = state.isHomeLocationSet();
//                                                    updateDroneLocation();
//                                                }
//
//                                            }
//                                        });
//                                    }
//                                });
//
//                            }
//                        }
//                    }
//                }
//            });
//        }
//
//    }



//    private void initFlightController() {
//        toLogUI(" initFlightController Start");
//        runOnUiThread(()->{
//
//            toLogUI(" initFlightController runOnUiThread Start");
//            BaseProduct baseProduct = MappingApplication.getProductInstance();
//            DJISDKManager dji = DJISDKManager.getInstance();
//
//            toLogUI(" initFlightController runOnUiThread After Get Instance");
//            if (Utils.isNull(baseProduct) || Utils.isNull(dji)){
//                toLogUI(" baseProduct or  dji IS NULL");
//                return;
//            }else {
//                if (!Utils.isNull(mProduct)){
//                    mProduct = null;
//                }
//                mProduct = ((Aircraft) baseProduct);
//                if (mProduct == null || !mProduct.isConnected()) {
//                    mFlightController = null;
//                    toLogUI(" mProduct Is Null or No Connected");
//                    return;
//                }
//
//                mFlightController = mProduct.getFlightController();
//                if (Utils.isNull(mFlightController)){
//                    toLogUI(" mFlightController Is Null or No Connected");
//                    return;
//                }else {
//                    final Simulator simulator =  mFlightController.getSimulator();
//                    if (Utils.isNull(simulator)){
//                        toLogUI(" simulator Is Null");
//                        return;
//                    }else {
//                        Double androidPhoneLat = LocationManager.getuLatitude();
//                        Double androidPhoneLng = LocationManager.getuLongitude();
//                        if (androidPhoneLat == Constants.DOUBLE_NULL || androidPhoneLng == Constants.DOUBLE_NULL){
//                            toLogUI(" androidPhoneLat or androidPhoneLng Is DOUBLE_NULL");
//                            return;
//                        }else {
//                            Camera camera = mProduct.getCamera();
//                            if (Utils.isNull(camera)){
//                                toLogUI(" camera IS NULL");
//                                return;
//                            }else {
//                                camera.setPhotoFileFormat(SettingsDefinitions.PhotoFileFormat.JPEG, (DJIError cameraError) -> {
//                                    if (!Utils.isNull(cameraError)){
//                                        String detail = cameraError.getDescription();
//                                        if (Utils.isNull(detail)){
//                                            detail = "";
//                                        }
//                                        toLogUI(" camera setPhotoFileFormat Error Happen: "+detail);
//                                        return;
//                                    }else {
//                                        LatLng homeLatLng = new LatLng(LocationManager.getuLatitude(),LocationManager.getuLongitude());
//                                        LocationCoordinate2D homeLocation = new LocationCoordinate2D(homeLatLng.latitude, homeLatLng.longitude);
//                                        if (false){
//                                            // Case Simulator
//                                            simulator
//                                                    .start(InitializationData.createInstance(homeLocation, 10, 10),
//                                                            errorSim -> {
//                                                                if (errorSim != null) {
//                                                                    toLogUI(" Simulator ini Failed");
//                                                                    toLogUI(" Simulator ini Failed :"+errorSim.getDescription());
//                                                                }else
//                                                                {
//                                                                    toLogUI("Start Simulator Success");
//                                                                    prepareMission(mProduct,mFlightController,homeLocation);
//
//                                                                }
//                                                            });
//                                        }else {
//                                            prepareMission(mProduct,mFlightController,homeLocation);
//                                        }
//
//                                    }
//                                });
//                            }
//                        }
//
//
//                    }
//
//                }
//
//            }
//
//
//        });
//    }
//
//    private void prepareMission(final Aircraft mProduct, final FlightController controller, final LocationCoordinate2D homeLocation) {
//        toLogUI("performMission Start");
//        if (isSearchingForUserPosition()){
//            //toLogUI("performMission Another instance ALready running");
//            return;
//        }else {
//            setSearchingForUserPosition(true);
//            runOnUiThread(()->{
//                toLogUI("performMission Start runOnUiThread");
//                if (Utils.isNull(mProduct)){
//                    toLogUI("performMission mProduct is Null so Stop");
//                    setSearchingForUserPosition(false);
//                    return;
//                }else {
//                    if (Utils.isNull(controller)){
//                        toLogUI("performMission controller is Null so Stop");
//                        setSearchingForUserPosition(false);
//                        return;
//                    }else {
//                        if (Utils.isNull(homeLocation)){
//                            toLogUI("performMission homeLocation is Null so Stop");
//                            setSearchingForUserPosition(false);
//                            return;
//                        }else {
//                            if (mProduct.getModel() == Model.UNKNOWN_AIRCRAFT) {
//                                toLogUI(" mProduct Is UNKNOWN_AIRCRAFT");
//                                setSearchingForUserPosition(false);
//                                return;
//                            }else {
//                                controller.setHomeLocation(homeLocation,locationError ->{
//                                    if (!Utils.isNull(locationError)){
//                                        toLogUI(" setHomeLocation  locationError ");
//                                        toLogUI(" setHomeLocation  locationError Is Error happen :"+locationError.getDescription());
//                                        toLogUI(" setHomeLocation  WILL RETRY SOON "+locationError.getDescription());
//                                        if (!MapActivity.this.isFinishing()){
//                                            setSearchingForUserPosition(false);
//                                            new Handler().postDelayed(this::initFlightController,Constants.DELAY_SHORT);
//                                        }else{
//                                            toLogUI(" setHomeLocation  WILL NOT RETRY cause Activity is Destroy ");
//                                        }
//                                        return;
//                                    }else {
//                                        toLogUI(" setStateCallback Start");
//
//                                        runOnUiThread(()->{
//                                            CoordinatorLayout menu =  (CoordinatorLayout)findViewById(R.id.coordinatorMenu);
//                                            if (Utils.isNull(menu)){
//                                                toLogUI(" setStateCallback menu IS Null");
//                                                return;
//                                            }else {
//                                                menu.setVisibility(View.VISIBLE);
//                                                createGridAndShowIt(coords,map);
//                                                toLogUI(" setStateCallback menu IS NOW VISIBLE");
//                                            }
//                                        });
//                                        toLogUI(" setStateCallback menu IS NOW VISIBLE");
//                                        controller.setStateCallback(state ->{
//                                            if (Utils.isNull(state)){
//                                                toLogUI(" setStateCallback state is Null");
//                                                setSearchingForUserPosition(false);
//                                                return;
//                                            }else {
//                                                // toLogUI(" setStateCallback state fired ");
//                                                // toLogUI(" setStateCallback state fired Time:"+Utils.getToday());
//                                                LocationCoordinate3D location = state.getAircraftLocation();
//                                                if (Utils.isNull(location)){
//                                                    toLogUI(" setStateCallback location is Null");
//                                                    setSearchingForUserPosition(false);
//                                                    return;
//                                                }else {
//                                                    currentDroneLatitude = location.getLatitude();
//                                                    currentDroneLongitude= location.getLongitude();
//                                                    isHomeLocationSet    = state.isHomeLocationSet();
//                                                    updateDroneLocation();
//                                                }
//
//                                            }
//                                        });
//                                    }
//                                });
//
//                            }
//                        }
//                    }
//                }
//            });
//        }
//
//    }


    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onDestroy(){
        unregisterReceiver(mReceiver);
        DroneManager instance = DroneManager.getInstance();
        if (Utils.isNull(instance)){
            toLogUI("onDestroy instance isNull");
        }else {
            instance.onDestroy();
        }
        super.onDestroy();

    }



}