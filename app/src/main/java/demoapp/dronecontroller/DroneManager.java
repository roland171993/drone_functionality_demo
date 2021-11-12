package demoapp.dronecontroller;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.dji.sdk.sample.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;


import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.CopyOnWriteArrayList;

import demoapp.dronecontroller.interfaces.OnAsyncGPSOperationComplete;
import demoapp.dronecontroller.interfaces.OnAsyncOperationComplete;
import demoapp.dronecontroller.interfaces.OnAsyncOperationCompleteBool;
import demoapp.dronecontroller.interfaces.OnGPSStateChange;
import demoapp.dronecontroller.interfaces.OnMissionRunningListener;
import demoapp.dronecontroller.interfaces.OnStateChangeListener;
import demoapp.dronecontroller.model.DroneDJI;
import demoapp.dronecontroller.presenters.LocationManager;
import demoapp.dronecontroller.presenters.PermissionManager;
import demoapp.dronecontroller.utils.Constants;
import demoapp.dronecontroller.utils.Utils;
import demoapp.mission.MissionSetting;


public class DroneManager {
  private final String TAG = this.getClass().getSimpleName();
  private @Nullable Context ctx;
  private @Nullable
  DroneDJI droneDJI;
  //private @Nullable Drone drone;

  public @Nullable DroneDJI getDroneDJI() {
    return droneDJI;
  }

  public void setDroneDJI(DroneDJI droneDJI) {
    this.droneDJI = droneDJI;
  }

  private static DroneManager instance;
  @Nullable PermissionManager pm = null;
  public DroneManager(final @NonNull Context ctx){
    this.ctx = ctx;
    droneDJI = new DroneDJI();
    pm =new PermissionManager(ctx);
    pm.keepOn(ctx);
  }

  public static void createInstance(final @NonNull Context ctx){
    synchronized (DroneManager.class){
      if (instance == null) {
        instance = new DroneManager(ctx);
        // Catch Error And Write to HDD
        LogManager.caughtExceptionOrError(ctx);
      }
    }

  }

  public static @Nullable DroneManager getInstance(){
    synchronized (DroneManager.class){
      return instance;
    }
  }

  public void attachBaseContext(Context paramContext, Application app){
    if (Utils.isNull(getDroneDJI())){
      toLog("attachBaseContext"," getDroneDJI is Null");
      return;
    }else {
      getDroneDJI().attachBaseContext(paramContext,app);
    }
  }

  public void onCreate(){
    if (Utils.isNull(getDroneDJI())){
      toLog("onCreate"," getDroneDJI is Null");
      return;
    }else {
      getDroneDJI().onCreate();
    }
  }


  public void ini(@NonNull Context ctx) {
    if (Utils.isNull(getDroneDJI())){
      toLog("ini"," getDroneDJI is Null");
      return;
    }else {
      getDroneDJI().ini(ctx);
    }
  }

  public void connect(final @NonNull OnAsyncOperationComplete callback) {
    Utils.AsyncExecute(()->{
      String msg = "";
      if (Utils.isNull(callback)){
        toLog("connect"," callback is Null");
        return;
      }else {
        try {
          if (Utils.isNull(getDroneDJI())){
            msg = " getDroneDJI is Null";
            toLog("connect",msg);
            callback.onError(msg);
            return;
          }else {
            if (!isValid(null)){
              msg = " case is NOT VALID";
              toLog("connect",msg);
              callback.onError(msg);
              return;
            }else {
              getDroneDJI().connect(callback);
            }
          }
        }catch (Exception e){
          Utils.toLog(TAG, "",null,e);
          if (Utils.isNull(callback)){
            return;
          }else {
            callback.onError("Error");
          }
        }catch (Error er){
          Utils.toLog(TAG, "",er,null);
          if (Utils.isNull(callback)){
            return;
          }else {
            callback.onError("Error");
          }
        }
      }
    });
  }

  public void disconnect(@NonNull OnAsyncOperationComplete callback) {
    Utils.AsyncExecute(()->{
      if (Utils.isNull(callback)){
        toLog("disconnect","callback is Null");
        return;
      }else {
        try {
          if (Utils.isNull(getDroneDJI())){
            toLog("disconnect","getDroneDJI is Null");
            return;
          }else {
            if (!isValid(null)){
              toLog("disconnect"," case is NOT VALID");
              return;
            }else {
              getDroneDJI().disconnect(callback);
            }
          }
        }catch (Exception e){
          Utils.toLog(TAG, "disconnect",null,e);
          if (Utils.isNull(callback)){
            return;
          }else {
            callback.onError("Error");
          }
        }catch (Error er){
          Utils.toLog(TAG, "disconnect",er,null);
          if (Utils.isNull(callback)){
            return;
          }else {
            callback.onError("Error");
          }
        }
      }
    });
  }

  public void stopWatchPhoneLocation(@NonNull OnAsyncOperationComplete callback) {
    Utils.AsyncExecute(()->{
      if (Utils.isNull(callback)){
        toLog("stopWatchPhoneLocation","callback is Null");
        return;
      }else {
        try {
          if (!isValid(true)){
            toLog("stopWatchPhoneLocation"," case is NOT VALID");
            return;
          }else {
            LocationManager locationManager =LocationManager.getInstance();
            if (Utils.isNull(locationManager)){
              toLog("stopWatchPhoneLocation","locationManager is Null CANNOT Get Current Android POSITION");
              callback.onError("locationManager is Null CANNOT Get Current Android POSITION");
              return;
            }else {
              locationManager.stopNotify();
              callback.onSucces("");
              return;
            }
          }
        }catch (Exception e){
          Utils.toLog(TAG, "stopWatchPhoneLocation",null,e);
          if (Utils.isNull(callback)){
            return;
          }else {
            callback.onError("Error");
          }
        }catch (Error er){
          Utils.toLog(TAG, "stopWatchPhoneLocation",er,null);
          if (Utils.isNull(callback)){
            return;
          }else {
            callback.onError("Error");
          }
        }
      }
    });
  }

  public void stopWatchDroneTelemetry(@NonNull OnAsyncOperationComplete callback) {
    Utils.AsyncExecute(()->{
      if (Utils.isNull(callback)){
        toLog("stopWatchDroneState","callback is Null");
        return;
      }else {
        try {
          if (Utils.isNull(getDroneDJI())){
            toLog("stopWatchDroneState","getDroneDJI is Null");
            return;
          }else {
            if (!isValid(null)){
              toLog("stopWatchDroneState"," case is NOT VALID");
              return;
            }else {
              getDroneDJI().stopNotifyTelemetry(callback);
            }
          }
        }catch (Exception e){
          Utils.toLog(TAG, "stopWatchPhoneLocation",null,e);
          if (Utils.isNull(callback)){
            return;
          }else {
            callback.onError("Error");
          }
        }catch (Error er){
          Utils.toLog(TAG, "stopWatchPhoneLocation",er,null);
          if (Utils.isNull(callback)){
            return;
          }else {
            callback.onError("Error");
          }
        }
      }
    });
  }

  public void registerToListenPhoneLocation(@NonNull OnAsyncGPSOperationComplete callback){
    String errorMsg = "";
    if (Utils.isNull(callback)){
      toLog("registerToListenAndroidLocation","callback is Null");
      return;
    }else {
      try {
        if (Utils.isNull(this.ctx)){
          errorMsg = "registerToListenAndroidLocation is Null CANNOT Get Current Android POSITION";
          toLog("registerToListenAndroidLocation",errorMsg);
          callback.onError(errorMsg);
          return;
        }else {
          if (!isValid(true)){
            errorMsg = "registerToListenAndroidLocation case is NOT VALID";
            toLog("registerToListenAndroidLocation",errorMsg);
            callback.onError(errorMsg);
            return;
          }else {
            LocationManager.createInstance(this.ctx, new OnGPSStateChange() {
              @Override
              public void onGPSDisable() {
                String msg = "registerToListenAndroidLocation LocationManager onGPSDisable ";
                toLog("registerToListenAndroidLocation",msg);
                callback.onError(msg);
                return;
              }

              @Override
              public void onGPSEnable() {
                toLog("registerToListenAndroidLocation","LocationManager onGPSEnable ");
              }

              @Override
              public void onPermissionNotGranted() {
                String msg = "registerToListenAndroidLocation LocationManager onPermissionNotGranted ";
                toLog("registerToListenAndroidLocation",msg);
                callback.onError(msg);
                return;
              }

              @Override
              public void onUserPositionChanged(@Nullable  Double uLatitude, @Nullable Double uLongitude) {
                // toLog("registerToListenAndroidLocation","onUserPositionChanged ");
                if (Utils.isNull(uLatitude) || Utils.isNull(uLongitude)){
                  toLog("registerToListenAndroidLocation","onUserPositionChanged uLatitude or uLongitude is Null");
                  return;
                }else {
                  if (uLatitude == Constants.DOUBLE_NULL || uLongitude == Constants.DOUBLE_NULL){
                    toLog("registerToListenAndroidLocation","onUserPositionChanged BAD POSITION uLatitude or uLongitude DOUBLE_NULL");
                    return;
                  }else {
                    callback.onLocationChanged(uLatitude,uLongitude);
                  }
                }
              }
            });
            LocationManager locationManager =LocationManager.getInstance();
            if (Utils.isNull(locationManager)){
              toLog("registerToListenAndroidLocation","locationManager is Null CANNOT Get Current Android POSITION");
              callback.onError("locationManager is Null CANNOT Get Current Android POSITION");
              return;
            }else {
              locationManager.startNotify();
              toLog("registerToListenAndroidLocation","connect locationManager End ");
              return;
            }

          }
        }
      }catch (Exception e){
        Utils.toLog(TAG, "registerToListenAndroidLocation",null,e);
        if (Utils.isNull(callback)){
          return;
        }else {
          callback.onError("Error");
          return;
        }
      }catch (Error er){
        Utils.toLog(TAG, "registerToListenAndroidLocation",er,null);
        if (Utils.isNull(callback)){
          return;
        }else {
          callback.onError("Error");
          return;
        }
      }
    }

  }

  public void registerToListenDroneState(@NonNull OnStateChangeListener callback) {
    Utils.AsyncExecute(()->{
      if (Utils.isNull(callback)){
        toLog("registerToListenDroneState","callback is Null");
        return;
      }else {
        try {
          if (Utils.isNull(getDroneDJI())){
            toLog("registerToListenDroneState","getDroneDJI is Null");
            return;
          }else {
            if (!isValid(null)){
              toLog("registerToListenDroneState"," case is NOT VALID");
              return;
            }else {
              getDroneDJI().registerToListenDroneState(callback);
            }
          }
        }catch (Exception e){
          Utils.toLog(TAG, "registerToListenDroneState",null,e);
          if (Utils.isNull(callback)){
            return;
          }else {
            callback.onConnectFailed();
          }
        }catch (Error er){
          Utils.toLog(TAG, "registerToListenDroneState",er,null);
          if (Utils.isNull(callback)){
            return;
          }else {
            callback.onConnectFailed();
          }
        }
      }
    });
  }

  public void setGoBackHomePosition(OnAsyncOperationComplete callback){
    Utils.AsyncExecute(()->{
      String msg = "";
      if (Utils.isNull(callback)){
        toLog("setGoBackHomePosition"," callback is null");
        return;
      }else {
        try {
          if (Utils.isNull(getDroneDJI())){
            msg = "getDroneDJI is Null";
            toLog("setGoBackHomePosition",msg);
            callback.onError(msg);
            return;
          }else {
            if (!isValid(null)){
              msg = "case is NOT VALID";
              toLog("setGoBackHomePosition",msg);
              callback.onError(msg);
              return;
            }else {
              getDroneDJI().setGoBackHomePosition(callback);
            }
          }
        }catch (Exception e){
          Utils.toLog(TAG, "setGoBackHomePosition",null,e);
          if (Utils.isNull(callback)){
            return;
          }else {
            callback.onError("Error");
          }
        }catch (Error er){
          Utils.toLog(TAG, "setGoBackHomePosition",er,null);
          if (Utils.isNull(callback)){
            return;
          }else {
            callback.onError("Error");
          }
        }
      }
    });
  }

  public void sendJoystickCommand(float leftY, float leftX, float rightY, float rightX,final OnAsyncOperationComplete callback){
    Utils.AsyncExecute(()->{
      String msg = "";
      if (Utils.isNull(callback)){
        toLog("sendJoystickCommand"," callback is null");
        return;
      }else {
        try {
          if (Utils.isNull(getDroneDJI())){
            msg = "getDroneDJI is Null";
            toLog("sendJoystickCommand",msg);
            callback.onError(msg);
            return;
          }else {
            if (!isValid(null)){
              msg = "case is NOT VALID";
              toLog("sendJoystickCommand",msg);
              callback.onError(msg);
              return;
            }else {
              getDroneDJI().sendJoystickData(leftY, leftX,rightY, rightX, callback);
            }
          }
        }catch (Exception e){
          Utils.toLog(TAG, "sendJoystickCommand",null,e);
          if (Utils.isNull(callback)){
            return;
          }else {
            callback.onError("Error");
          }
        }catch (Error er){
          Utils.toLog(TAG, "sendJoystickCommand",er,null);
          if (Utils.isNull(callback)){
            return;
          }else {
            callback.onError("Error");
          }
        }
      }
    });
  }

  public void takeOff(OnAsyncOperationComplete callback){
    Utils.AsyncExecute(()->{
      String msg = "";
      if (Utils.isNull(callback)){
        toLog("takeOff"," callback is null");
        return;
      }else {
        try {
          if (Utils.isNull(getDroneDJI())){
            msg = "getDroneDJI is Null";
            toLog("takeOff",msg);
            callback.onError(msg);
            return;
          }else {
            if (!isValid(null)){
              msg = "case is NOT VALID";
              toLog("takeOff",msg);
              callback.onError(msg);
              return;
            }else {
              getDroneDJI().takeOff(callback);
            }
          }
        }catch (Exception e){
          Utils.toLog(TAG, "takeOff",null,e);
          if (Utils.isNull(callback)){
            return;
          }else {
            callback.onError("Error");
          }
        }catch (Error er){
          Utils.toLog(TAG, "takeOff",er,null);
          if (Utils.isNull(callback)){
            return;
          }else {
            callback.onError("Error");
          }
        }
      }
    });
  }

  public void landing(OnAsyncOperationComplete callback){
    Utils.AsyncExecute(()->{
      String msg = "";
      if (Utils.isNull(callback)){
        toLog("landing"," callback is null");
        return;
      }else {
        try {
          if (Utils.isNull(getDroneDJI())){
            msg = "getDroneDJI is Null";
            toLog("landing",msg);
            callback.onError(msg);
            return;
          }else {
            if (!isValid(null)){
              msg = "case is NOT VALID";
              toLog("landing",msg);
              callback.onError(msg);
              return;
            }else {
              getDroneDJI().landing(callback);
            }
          }
        }catch (Exception e){
          Utils.toLog(TAG, "landing",null,e);
          if (Utils.isNull(callback)){
            return;
          }else {
            callback.onError("Error");
          }
        }catch (Error er){
          Utils.toLog(TAG, "landing",er,null);
          if (Utils.isNull(callback)){
            return;
          }else {
            callback.onError("Error");
          }
        }
      }
    });
  }

  public void returnToHome(OnAsyncOperationComplete callback){
    Utils.AsyncExecute(()->{
      String msg = "";
      if (Utils.isNull(callback)){
        toLog("returnToHome"," callback is null");
        return;
      }else {
        try {
          if (Utils.isNull(getDroneDJI())){
            msg = "getDroneDJI is Null";
            toLog("returnToHome",msg);
            callback.onError(msg);
            return;
          }else {
            if (!isValid(null)){
              msg = "case is NOT VALID";
              toLog("returnToHome",msg);
              callback.onError(msg);
              return;
            }else {
              getDroneDJI().returnToHome(callback);
            }
          }
        }catch (Exception e){
          Utils.toLog(TAG, "returnToHome",null,e);
          if (Utils.isNull(callback)){
            return;
          }else {
            callback.onError("Error");
          }
        }catch (Error er){
          Utils.toLog(TAG, "returnToHome",er,null);
          if (Utils.isNull(callback)){
            return;
          }else {
            callback.onError("Error");
          }
        }
      }
    });
  }

  public void disableJoystick(OnAsyncOperationComplete callback){
    Utils.AsyncExecute(()->{
      String msg = "";
      if (Utils.isNull(callback)){
        toLog("performDisableJoystick"," callback is null");
        return;
      }else {
        try {
          if (Utils.isNull(getDroneDJI())){
            msg = "getDroneDJI is Null";
            toLog("performDisableJoystick",msg);
            callback.onError(msg);
            return;
          }else {
            if (!isValid(null)){
              msg = "case is NOT VALID";
              toLog("performDisableJoystick",msg);
              callback.onError(msg);
              return;
            }else {
              getDroneDJI().disableJoystickCommand(callback);
            }
          }
        }catch (Exception e){
          Utils.toLog(TAG, "performDisableJoystick",null,e);
          if (Utils.isNull(callback)){
            return;
          }else {
            callback.onError("Error");
          }
        }catch (Error er){
          Utils.toLog(TAG, "performDisableJoystick",er,null);
          if (Utils.isNull(callback)){
            return;
          }else {
            callback.onError("Error");
          }
        }
      }
    });
  }

  public void enableJoystick(OnAsyncOperationComplete callback){
    Utils.AsyncExecute(()->{
      String msg = "";
      if (Utils.isNull(callback)){
        toLog("performEnableJoystick"," callback is null");
        return;
      }else {
        try {
          if (Utils.isNull(getDroneDJI())){
            msg = "getDroneDJI is Null";
            toLog("performEnableJoystick",msg);
            callback.onError(msg);
            return;
          }else {
            if (!isValid(null)){
              msg = "case is NOT VALID";
              toLog("performEnableJoystick",msg);
              callback.onError(msg);
              return;
            }else {
              getDroneDJI().enableJoystickCommand(callback);
            }
          }
        }catch (Exception e){
          Utils.toLog(TAG, "performEnableJoystick",null,e);
          if (Utils.isNull(callback)){
            return;
          }else {
            callback.onError("Error");
          }
        }catch (Error er){
          Utils.toLog(TAG, "performEnableJoystick",er,null);
          if (Utils.isNull(callback)){
            return;
          }else {
            callback.onError("Error");
          }
        }
      }
    });
  }

  public void onDestroy(){
    if (Utils.isNull(getDroneDJI())){
      toLog("onDestroy"," getDroneDJI()");
      return;
    }else {
      try {
        getDroneDJI().onDestroy();
        if (Utils.isNull(pm)){
          return;
        }else {
          if (Utils.isNull(ctx)){
            return;
          }else {
            pm.removeKeepOn(ctx);
          }
        }
      }catch (Exception e){
        Utils.toLog(TAG, "onDestroy",null,e);

      }catch (Error er){
        Utils.toLog(TAG, "onDestroy",er,null);
      }
    }
  }

  public void doTest(@NonNull final Activity activity, final OnAsyncOperationComplete callback) {
    String msg = "";
    if (Utils.isNull(callback)){
      toLog("doTest"," callback is null");
      return;
    }else {
      try {
        if (Utils.isNull(activity)){
          msg = "Activity is  is Null";
          toLog("doTest",msg);
          callback.onError(msg);
          return;
        }else {
          if (Utils.isNull(getDroneDJI())){
            msg = "getDroneDJI is Null";
            toLog("doTest",msg);
            callback.onError(msg);
            return;
          }else {
            if (!isValid(null)){
              msg = " case is NOT VALID";
              toLog("doTest",msg);
              callback.onError(msg);
              return;
            }else {
              getDroneDJI().doTest(activity,callback);
            }
          }
        }
      }catch (Exception e){
        Utils.toLog(TAG, "doTest",null,e);
        if (Utils.isNull(callback)){
          return;
        }else {
          callback.onError("Error");
        }
      }catch (Error er){
        Utils.toLog(TAG, "doTest",er,null);
        if (Utils.isNull(callback)){
          return;
        }else {
          callback.onError("Error");
        }
      }
    }
  }
  private boolean isValid(@Nullable Boolean withoutDrone){
    if (Utils.isNull(withoutDrone)){
      withoutDrone = false;
    }
    if (Utils.isNull(this.ctx)){
      toLog("isValid"," this.ctx Is Null");
      return false;
    }else {

      // If there is enough permission, we will start the registration
      if (Utils.isNull(pm)){
        toLog("isValid"," isPermissionsGranted Object Is Null");
        return false;
      }else {
        if (!pm.isPermissionsGranted()){
          toLog("isValid"," isPermissionsGranted Is NOT Granted");
          return false;
        }else {
          if (withoutDrone){
            return true;
          }else {
            if (Utils.isNull(getDroneDJI())){
              toLog("isValid"," getDroneDJI() Is Null");
              return false;
            }else {
              return true;
            }
          }
        }
      }
    }
  }

  public void writeLogOnDeviceDocument(String fileContent){
    Utils.AsyncExecute(()->{
      try {
        if (Utils.isNull(ctx)){
          toLog(TAG," writeLogOnDeviceDocument ctx Is Null");
          return;
        }else {
          final int ERROR = 1;

          if (fileContent != null){

            // Get Directory
            File directory  = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS+"/"+ctx.getString(R.string.app_name)).getAbsoluteFile();
            if(!directory.exists() && !directory.mkdirs()){
              //Log.v(Constants.APP_NAME, TAG + "Error creating directory " + directory);
              return;
            }
            String backupName = "CrashLog for "+Utils.getToday();
            String FILE_EXTENSION_BKP = ".txt";


            String bkpPath = backupName + FILE_EXTENSION_BKP;
            File bkp = new File (directory, bkpPath);

            //Add new file
            bkp = new File (directory, bkpPath);

            File finalBkp = bkp;
            if (fileContent.trim().contentEquals(""))
              return;


            FileOutputStream fos = new FileOutputStream(finalBkp, true); // save
            fos.write(fileContent.getBytes());
            fos.close();

            // Auto refresh
            MediaScannerConnection msc =  new MediaScannerConnection(ctx, new MediaScannerConnection.MediaScannerConnectionClient() {
              @Override
              public void onMediaScannerConnected() {
                MediaScannerConnection.scanFile(ctx, new String[]{finalBkp.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                  public void onScanCompleted(String path, Uri uri) {
                    //Log.v(Constants.APP_NAME, TAG + " KML BUILD SAVE OK PATH "+ kml.getAbsolutePath() +" IS EXIST "+kml.exists());
                  }
                });
              }

              @Override
              public void onScanCompleted(String s, Uri uri) {
              }
            });
            if(!msc.isConnected()){
              msc.connect();
            }

          }
        }
      }catch (Exception e){
        e.printStackTrace();
      }catch (Error er){
        er.printStackTrace();
      }
    });

  }

  private void toLog(@NonNull String tag, @NonNull String msg){
    Utils.toLog(tag, msg);
  }

  @RequiresApi(api = Build.VERSION_CODES.N)
  public void startWaypointMission(@NonNull final Activity activity,@NonNull CopyOnWriteArrayList<LatLng> points, float altitudePrefered,boolean exitMissionOnRCSignalLostEnabled, boolean inSimulator,boolean autoGoHomeOnLowBattery, double phoneLatitude, double phoneLongitude, final @NonNull OnMissionRunningListener callback) {
    toLog(TAG," startWaypointMission Start");
    Utils.AsyncExecute(()->{
      if (Utils.isNull(callback)){
        toLog(TAG," startWaypointMission callback is null");
        return;
      }else {
        try {
          String errorMsg = "Error startWaypointMission failed:";
          String logMsg = " startWaypointMission activity is null";
          if (Utils.isNull(activity)){
            toLog(TAG,logMsg);
            callback.onError(errorMsg+logMsg);
            return;
          }else {
            if (Utils.isNull(points)){
              logMsg = " startWaypointMission points is null";
              toLog(TAG,logMsg);
              callback.onError(errorMsg+logMsg);
              return;
            }else {
              if (points.size() == 0){
                logMsg = " startWaypointMission points is EMPTY";
                toLog(TAG,logMsg);
                callback.onError(errorMsg+logMsg);
                return;
              }else {
                if (points.size() < Constants.POLYGON_SIZE){
                  toLog(TAG," startWaypointMission points is Not a Polygon");
                  callback.onError(" startWaypointMission points is Not a Polygon");
                  return;
                }else {
                  double area = SphericalUtil.computeArea(points)* 0.0001;
                  MissionSetting setting = new MissionSetting(altitudePrefered,area,inSimulator,exitMissionOnRCSignalLostEnabled,null,autoGoHomeOnLowBattery);
                  setting.setHomeLocation(new LatLng(phoneLatitude,phoneLongitude));
                  if (!setting.isValid(null)){
                    logMsg = " startWaypointMission "+setting.getErrorMsg();
                    toLog(TAG,logMsg);
                    callback.onError(errorMsg+logMsg);
                    return;
                  }else {
                    if (Utils.isNull(getDroneDJI())){
                      logMsg = "getDroneDJI is Null";
                      toLog(TAG,logMsg);
                      callback.onError(errorMsg+logMsg);
                      return;
                    }else {
                      if (!isValid(null)){
                        logMsg = "getDroneDJI isVALID sis false";
                        toLog(TAG,logMsg);
                        callback.onError(errorMsg+logMsg);
                      }else {
                        getDroneDJI().startWaypointMission(activity,points,setting,callback);
                        return;
                      }
                    }
                  }
                }
              }

            }

          }
        }catch (Exception e){
          Utils.toLog(TAG, "startWaypointMission",null,e);
          if (Utils.isNull(callback)){
            return;
          }else {
            callback.onError("");
          }
        }catch (Error er){
          Utils.toLog(TAG, "startWaypointMission",er,null);
          if (Utils.isNull(callback)){
            return;
          }else {
            callback.onError("");
          }
        }
      }

    });
  }

  @RequiresApi(api = Build.VERSION_CODES.N)
  public void continuWaypointMission(@NonNull final Activity activity, final @NonNull OnMissionRunningListener callback){
    toLog(TAG," continuWaypointMission Start");
    Utils.AsyncExecute(()->{
      if (Utils.isNull(callback)){
        toLog(TAG," continuWaypointMission callback is null");
        return;
      }else {
        try {
          String errorMsg = "Error startWaypointMission failed:";
          String logMsg = " continuWaypointMission activity is null";
          if (Utils.isNull(activity)){
            toLog(TAG,logMsg);
            callback.onError(errorMsg+logMsg);
            return;
          }else {
            if (Utils.isNull(getDroneDJI())){
              logMsg = "getDroneDJI is Null";
              toLog(TAG,logMsg);
              callback.onError(errorMsg+logMsg);
              return;
            }else {
              if (!isValid(null)){
                logMsg = "getDroneDJI isVALID sis false";
                toLog(TAG,logMsg);
                callback.onError(errorMsg+logMsg);
              }else {
                getDroneDJI().continuWaypointMission(activity,callback);
              }
            }
          }
        }catch (Exception e){
          Utils.toLog(TAG, "continuWaypointMission",null,e);
          if (Utils.isNull(callback)){
            return;
          }else {
            callback.onError("");
          }
        }catch (Error er){
          Utils.toLog(TAG, "continuWaypointMission",er,null);
          if (Utils.isNull(callback)){
            return;
          }else {
            callback.onError("");
          }
        }
      }
    });


  }


  @RequiresApi(api = Build.VERSION_CODES.N)
  public void isMissionExist( final @NonNull OnAsyncOperationCompleteBool callback){
    toLog(TAG," isMissionExist Start");
    Utils.AsyncExecute(()->{
      if (Utils.isNull(callback)){
        toLog(TAG," isMissionExist callback is null");
        return;
      }else {
        try {
          String logMsg = " isMissionExist ctx is null";
          if (Utils.isNull(ctx)){
            toLog(TAG,logMsg);
            callback.onResultNo(logMsg);
            return;
          }else {
            if (Utils.isNull(getDroneDJI())){
              logMsg = "getDroneDJI is Null";
              toLog(TAG,logMsg);
              callback.onResultNo(logMsg);
              return;
            }else {
              // Check Permission and Context
              if (!isValid(true)){
                logMsg = "getDroneDJI isVALID sis false";
                toLog(TAG,logMsg);
                callback.onResultNo(logMsg);
              }else {
                getDroneDJI().isMappingMissionExist(ctx,callback);
              }
            }
          }
        }catch (Exception e){
          Utils.toLog(TAG, "isMissionExist",null,e);
          if (Utils.isNull(callback)){
            return;
          }else {
            callback.onResultNo("");
          }
        }catch (Error er){
          Utils.toLog(TAG, "isMissionExist",er,null);
          if (Utils.isNull(callback)){
            return;
          }else {
            callback.onResultNo("");
          }
        }
      }
    });
  }

  @RequiresApi(api = Build.VERSION_CODES.N)
  public void cleanUserData( final @NonNull OnAsyncOperationCompleteBool callback){
    toLog(TAG," cleanUserData Start");
    Utils.AsyncExecute(()->{
      if (Utils.isNull(callback)){
        toLog(TAG," cleanUserData callback is null");
        return;
      }else {
        try {
          String logMsg = " cleanUserData activity is null";
          if (Utils.isNull(ctx)){
            toLog(TAG,logMsg);
            callback.onResultNo(logMsg);
            return;
          }else {
            if (Utils.isNull(getDroneDJI())){
              logMsg = "getDroneDJI is Null";
              toLog(TAG,logMsg);
              callback.onResultNo(logMsg);
              return;
            }else {
              // Check Permission and Context
              if (!isValid(true)){
                logMsg = "getDroneDJI isVALID sis false";
                toLog(TAG,logMsg);
                callback.onResultNo(logMsg);
              }else {
                getDroneDJI().cleanUserData(ctx,callback);
              }
            }
          }
        }catch (Exception e){
          Utils.toLog(TAG, "cleanUserData",null,e);
          if (Utils.isNull(callback)){
            return;
          }else {
            callback.onResultNo("");
          }
        }catch (Error er){
          Utils.toLog(TAG, "cleanUserData",er,null);
          if (Utils.isNull(callback)){
            return;
          }else {
            callback.onResultNo("");
          }
        }
      }
    });
  }

}
