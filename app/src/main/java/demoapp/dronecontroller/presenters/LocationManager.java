package demoapp.dronecontroller.presenters;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;


import demoapp.dronecontroller.interfaces.OnGPSStateChange;
import demoapp.dronecontroller.utils.Constants;
import demoapp.dronecontroller.utils.MyConst;
import demoapp.dronecontroller.utils.Utils;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static android.content.Context.LOCATION_SERVICE;

public final class LocationManager {
  public static final String TAG = LocationManager.class.getSimpleName();
  private @Nullable Context context;
  private static volatile LocationManager instance = null;
  ScheduledExecutorService schLocation;

  @Nullable
  public OnGPSStateChange getGpsListener() {
    return gpsListener;
  }

  public void setGpsListener(@Nullable OnGPSStateChange gpsListener) {
    this.gpsListener = gpsListener;
  }

  private @Nullable OnGPSStateChange gpsListener;
  android.location.LocationManager locationManager;
  Location location;
  android.location.LocationManager locManager;
  Location loc;
  private volatile boolean locNotif;

  public static volatile Double uLatitude = Constants.DOUBLE_NULL;
  public static volatile Double uLongitude = Constants.DOUBLE_NULL;
  public static volatile Long ulastSignalValidTime = new Constants().LONG_INVALID;

  public static volatile Double dLatitude = Constants.DOUBLE_NULL;
  public static volatile Double dLongitude = Constants.DOUBLE_NULL;
  public static volatile Long dlastSignalValidTime = new Constants().LONG_INVALID;

  public static Double getuLatitude() {
    return uLatitude;
  }

  public static void setuLatitude(Double uLatitude) {
    LocationManager.uLatitude = uLatitude;
  }

  public static Double getuLongitude() {
    return uLongitude;
  }

  public static void setuLongitude(Double uLongitude) {
    LocationManager.uLongitude = uLongitude;
  }

  public static Long getUlastSignalValidTime() {
    return ulastSignalValidTime;
  }

  public static void setUlastSignalValidTime(Long ulastSignalValidTime) {
    LocationManager.ulastSignalValidTime = ulastSignalValidTime;
  }

  public static Double getdLatitude() {
    return dLatitude;
  }

  public static void setdLatitude(Double dLatitude) {
    LocationManager.dLatitude = dLatitude;
  }

  public static Double getdLongitude() {
    return dLongitude;
  }

  public static void setdLongitude(Double dLongitude) {
    LocationManager.dLongitude = dLongitude;
  }

  public static Long getDlastSignalValidTime() {
    return dlastSignalValidTime;
  }

  public static void setDlastSignalValidTime(Long dlastSignalValidTime) {
    LocationManager.dlastSignalValidTime = dlastSignalValidTime;
  }

  public void resetUserPosition() {
    setuLatitude(Constants.DOUBLE_NULL);
    setuLongitude(Constants.DOUBLE_NULL);
  }

  public void resetDronePosition() {
    setdLatitude(Constants.DOUBLE_NULL);
    setdLongitude(Constants.DOUBLE_NULL);
  }

  public static void saveNowUser(){
    synchronized (LocationManager.class){
      setUlastSignalValidTime(System.currentTimeMillis());
    }
  }

  public static void resetTimeUser(){
    synchronized (LocationManager.class){
      Constants c = new Constants();
      setUlastSignalValidTime(c.LONG_INVALID);
    }
  }

  public static void saveNowDrone(){
    synchronized (LocationManager.class){
      setDlastSignalValidTime(System.currentTimeMillis());
    }
  }

  public static void resetTimeDrone(){
    synchronized (LocationManager.class){
      Constants c = new Constants();
      setDlastSignalValidTime(c.LONG_INVALID);
    }
  }

  public LocationManager(final @NonNull Context context, final @NonNull OnGPSStateChange listener) {
    this.context = context;
    this.gpsListener = listener;
  }

  public static void createInstance(final @NonNull Context context, final @NonNull OnGPSStateChange listener){
    synchronized (LocationManager.class){
      if (instance == null) {
        instance = new LocationManager(context,listener);
      }
    }
  }
  public static @Nullable LocationManager getInstance(){
    synchronized (LocationManager.class){
      return instance;
    }
  }

  private boolean isLocNotif() {
    return locNotif;
  }

  private void setLocNotif(boolean locNotif) {
    this.locNotif = locNotif;
  }


  public synchronized void refreshPosition() {
    try {
      synchronized (this){
        setLocNotif(true);
        if (Utils.isNull(context)){
          return;
        }else{
          if (Utils.isNull(getGpsListener())){
            return;
          }else{
            MyConst playNotGranted = new MyConst(){
              @Override
              public void run() {
                getGpsListener().onPermissionNotGranted();
              }
            };
            PermissionManager pm = new PermissionManager(context);
            if (pm.isPermissionsGranted()) {
              try {
                boolean isGPSEnable, isNetworkEnable;
                locManager = (android.location.LocationManager) context.getSystemService(LOCATION_SERVICE);
                isGPSEnable = locManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
                isNetworkEnable = locManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER);
                LocationListener doNothing = new LocationListener() {
                  @Override
                  public void onLocationChanged(@NonNull Location location) {

                  }

                  @Override
                  public void onStatusChanged(String provider, int status, Bundle extras) {

                  }

                  @Override
                  public void onProviderEnabled(String provider) {

                  }

                  @Override
                  public void onProviderDisabled(String provider) {

                  }
                };

                if (!isGPSEnable && !isNetworkEnable) {
                  getGpsListener().onGPSDisable();
                } else {

                  if (isNetworkEnable) {
                    loc = null;
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                      // TODO: Consider calling
                      //    ActivityCompat#requestPermissions
                      // here to request the missing permissions, and then overriding
                      //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                      //                                          int[] grantResults)
                      // to handle the case where the user grants the permission. See the documentation
                      // for ActivityCompat#requestPermissions for more details.
                      playNotGranted.run();
                      return;
                    }
                    locManager.requestLocationUpdates(android.location.LocationManager.NETWORK_PROVIDER, 1000, 0, doNothing);
                    if (locManager!=null){
                      loc = locManager.getLastKnownLocation(android.location.LocationManager.NETWORK_PROVIDER);
                      if (loc!=null){
                        save(loc);
                      }
                    }

                  }

                  if (isGPSEnable) {
                    loc = null;
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                      // TODO: Consider calling
                      //    ActivityCompat#requestPermissions
                      // here to request the missing permissions, and then overriding
                      //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                      //                                          int[] grantResults)
                      // to handle the case where the user grants the permission. See the documentation
                      // for ActivityCompat#requestPermissions for more details.
                      playNotGranted.run();
                      return;
                    }
                    locManager.requestLocationUpdates(android.location.LocationManager.GPS_PROVIDER, 1000, 0,doNothing);
                    if (locManager!=null){
                      loc = locManager.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER);
                      if (loc!=null){
                        save(loc);
                      }
                    }
                  }

                }
              }catch (Exception e){
                e.printStackTrace();
              }catch (Error e){
                e.printStackTrace();
              }
            }else {
              playNotGranted.run();
              return;
            }
          }
        }
      }

    }catch (Exception e){
      e.printStackTrace();
    }catch (Error e){
      e.printStackTrace();
    }
  }


  private synchronized void save(@NonNull Location location) {
    try {
      setuLatitude(location.getLatitude());
      setuLongitude(location.getLongitude());
      saveNowUser();

      if (Utils.isNull(getGpsListener())){
        return;
      }else{
        getGpsListener().onUserPositionChanged(getuLatitude(), getuLongitude());
      }
    }catch (Exception e){
      e.printStackTrace();
    }catch (Error e){
      e.printStackTrace();
    }
  }

  public void stopNotify() {
    setLocNotif(false);
    if (schLocation != null){
      schLocation.shutdown();
      schLocation = null;
    }
  }

  public void startNotify() {
    stopNotify();
    schLocation =
      Executors.newSingleThreadScheduledExecutor();
    schLocation.scheduleAtFixedRate
      (new Runnable() {
        public void run() {
          // send time change Request
          // next time
          new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
              synchronized (LocationManager.this){
                refreshPosition();
              }

            }
          });
        }
      }, 0, 1, TimeUnit.SECONDS);
    //first Time
    refreshPosition();
  }

  public static void runTest(Context ctx){
    LocationManager.createInstance(ctx, new OnGPSStateChange() {
      @Override
      public void onGPSDisable() {
        Utils.toLog(TAG, "runTest onGPSDisable");
      }

      @Override
      public void onGPSEnable() {
        Utils.toLog(TAG, "runTest onGPSEnable");
      }

      @Override
      public void onPermissionNotGranted() {
        Utils.toLog(TAG, "runTest onPermissionNotGranted");
      }

      @Override
      public void onUserPositionChanged(@Nullable Double uLatitude, @Nullable Double uLongitude) {
        Utils.toLog(TAG, "runTest onUserPositionChanged");

        Utils.toLog(TAG, "runTest uLatitude "+uLatitude + " uLongitude "+uLongitude);
        Utils.toLog(TAG, "runTest Base uLatitude "+LocationManager.getuLatitude() + " BAse uLongitude "+LocationManager.getuLongitude() +
          " lasttime "+LocationManager.getUlastSignalValidTime());
        Objects.requireNonNull(LocationManager.getInstance()).stopNotify();
      }
    });
    Objects.requireNonNull(LocationManager.getInstance()).startNotify();
  }
}
