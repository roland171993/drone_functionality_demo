package demoapp.dronecontroller.model;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.dji.sdk.sample.internal.controller.DJISampleApplication;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import demoapp.dronecontroller.MappingApplication;
import demoapp.dronecontroller.interfaces.OnAsyncOperationComplete;
import demoapp.dronecontroller.interfaces.OnAsyncOperationCompleteBool;
import demoapp.dronecontroller.interfaces.OnMissionRunningListener;
import demoapp.dronecontroller.interfaces.OnStateChangeListener;
import demoapp.dronecontroller.presenters.LocationManager;
import demoapp.dronecontroller.presenters.PermissionManager;
import demoapp.dronecontroller.utils.Constants;
import demoapp.dronecontroller.utils.Dummy;
import demoapp.dronecontroller.utils.Utils;
import demoapp.mapping.PointLatLngAlt;
import demoapp.mission.CameraParameter;
import demoapp.mission.GridSetting;
import demoapp.mission.MissionDJI;
import demoapp.mission.MissionSetting;
import dji.common.camera.DJICameraCalibrateResult;
import dji.common.camera.DJICameraCalibrateState;
import dji.common.camera.SettingsDefinitions;
import dji.common.camera.WhiteBalance;
import dji.common.error.DJIError;
import dji.common.error.DJISDKError;
import dji.common.flightcontroller.imu.IMUState;
import dji.common.flightcontroller.simulator.InitializationData;
import dji.common.flightcontroller.virtualstick.FlightControlData;
import dji.common.gimbal.GimbalState;
import dji.common.gimbal.Rotation;
import dji.common.gimbal.RotationMode;
import dji.common.mission.waypoint.Waypoint;
import dji.common.mission.waypoint.WaypointAction;
import dji.common.mission.waypoint.WaypointActionType;
import dji.common.mission.waypoint.WaypointMission;
import dji.common.mission.waypoint.WaypointMissionDownloadEvent;
import dji.common.mission.waypoint.WaypointMissionExecutionEvent;
import dji.common.mission.waypoint.WaypointMissionFinishedAction;
import dji.common.mission.waypoint.WaypointMissionFlightPathMode;
import dji.common.mission.waypoint.WaypointMissionHeadingMode;
import dji.common.mission.waypoint.WaypointMissionState;
import dji.common.mission.waypoint.WaypointMissionUploadEvent;
import dji.common.model.LocationCoordinate2D;
import dji.common.product.Model;
import dji.common.util.CommonCallbacks;
import dji.keysdk.FlightControllerKey;
import dji.keysdk.KeyManager;
import dji.keysdk.callback.ActionCallback;
import dji.log.DJILog;
import dji.sdk.base.BaseComponent;
import dji.sdk.base.BaseProduct;
import dji.sdk.camera.Camera;
import dji.sdk.flightcontroller.FlightController;
import dji.sdk.flightcontroller.Simulator;
import dji.sdk.gimbal.Gimbal;
import dji.sdk.mission.waypoint.WaypointMissionOperator;
import dji.sdk.mission.waypoint.WaypointMissionOperatorListener;
import dji.sdk.products.Aircraft;
import dji.sdk.sdkmanager.DJISDKInitEvent;
import dji.sdk.sdkmanager.DJISDKManager;
import dji.sdk.sdkmanager.LDMModule;
import dji.sdk.sdkmanager.LDMModuleType;

public class DroneDJI extends Drone {
  private float heading = 0;
  private int numberOfDischarges = 0;
  private int lifetimeRemainingInPercent = 0;
  private @Nullable Context context;
  private @Nullable OnStateChangeListener listener;
  private final String TAG = this.getClass().getSimpleName();
  private boolean isRegisted;
  private @Nullable Aircraft aircraft ;
  private @Nullable DroneState  droneState;
  private FlightController mFlightController;
  private CopyOnWriteArrayList<Waypoint> waypointList = new CopyOnWriteArrayList<>();
  private volatile boolean notify =false,joystickEnable;
  private @Nullable
  GridSetting setting = null;
  private @Nullable
  MissionDJI mission = null;


  public boolean isAlreadyNotify() {
    return notify;
  }

  public void setAlreadyNotify(boolean notify) {
    this.notify = notify;
  }

  public boolean isJoystickEnable() {
    return joystickEnable;
  }

  public void setJoystickEnable(boolean joystickEnable) {
    this.joystickEnable = joystickEnable;
  }

  @Nullable
  public Aircraft getAircraft() {
    if (aircraft == null) {
      // Refresh aircraft
      makeBind();
    }
    return aircraft;
  }

  public void setAircraft(@Nullable Aircraft aircraft) {
    this.aircraft = aircraft;
  }

  public boolean isRegisted() {
    return isRegisted;
  }

  public void setRegisted(boolean registed) {
    isRegisted = registed;
  }

  public DroneDJI (){
    setRegisted(false);
    setAlreadyNotify(false);
    setJoystickEnable(false);
    makeBind();

  }

  private void makeBind() {
    BaseProduct baseProduct = DJISampleApplication.getProductInstance();
    if (Utils.isNull(baseProduct)){
      toLog(" baseProduct  IS NULL ----------->PLEASE RECONNECT DRONE <----------- ");
      return;
    }else {
      setAircraft ((Aircraft) baseProduct);
    }
  }

  @Nullable
  public Context getContext() {
    return context;
  }

  public void setContext(@Nullable Context context) {
    this.context = context;
  }


  private AtomicBoolean isRegistrationInProgress = new AtomicBoolean(false);
  private static final int REQUEST_PERMISSION_CODE = 12345;

  @Override
  public void ini(@NonNull Context ctx) {
    try {
      setContext(ctx);
      if (Utils.isNull(getContext())){
        toLog("ini  getContext() Is Null");
        return;
      }
    }catch (Exception e){
      Utils.toLog(TAG, "ini",null,e);
    }catch (Error er){
      Utils.toLog(TAG, "ini",er,null);
    }
  }

  public void setGoBackHomePosition(OnAsyncOperationComplete callback){
    try {
      if (Utils.isNull(callback)){
        toLog("setGoBackHomePosition callback Is Null");
        return;
      }else {
        if (Utils.isNull(getContext())){
          toLog("setGoBackHomePosition is Null");
          callback.onError("setGoBackHomePosition is Null");
          return;
        }else {
          if (!isValid(false, null, null)){
            toLog("setGoBackHomePosition isValid FALSE");
            callback.onError("setGoBackHomePosition isValid FALSE");
            return;
          }else {
            Double androidPhoneLat = LocationManager.getuLatitude();
            Double androidPhoneLng = LocationManager.getuLongitude();
            if (androidPhoneLat == Constants.DOUBLE_NULL || androidPhoneLng == Constants.DOUBLE_NULL){
              toLog(" androidPhoneLat or androidPhoneLng Is DOUBLE_NULL");
              callback.onError(" androidPhoneLat or androidPhoneLng Is DOUBLE_NULL");
              return;
            }else {
              if (androidPhoneLat.isNaN() || androidPhoneLng.isNaN()){
                toLog(" androidPhoneLat or androidPhoneLng Is NaN");
                callback.onError(" androidPhoneLat or androidPhoneLng Is DOUBLE_NULL");
                return;
              }else {
                LatLng homeLatLng = new LatLng(LocationManager.getuLatitude(),LocationManager.getuLongitude());
                LocationCoordinate2D homeLocation = new LocationCoordinate2D(homeLatLng.latitude, homeLatLng.longitude);
                mFlightController.setHomeLocation(homeLocation,locationError ->{
                  try {
                    if (!Utils.isNull(locationError)){
                      String detail = locationError.getDescription();
                      if (Utils.isNull(detail)){
                        detail ="";
                      }
                      toLog(" setHomeLocation  Failed "+detail);
                      callback.onError(detail);
                    }else {
                      callback.onSucces("");
                    }
                  }catch (Exception e){
                    Utils.toLog(TAG, "setHomeLocation",null,e);
                  }catch (Error er){
                    Utils.toLog(TAG, "setHomeLocation",er,null);
                  }
                });
              }
            }
          }
        }
      }
    }catch (Exception e){
      Utils.toLog(TAG, "setGoBackHomePosition",null,e);
    }catch (Error er){
      Utils.toLog(TAG, "setGoBackHomePosition",er,null);
    }
  }

  public void connect(final @NonNull OnAsyncOperationComplete callback) {

    if (Utils.isNull(callback)){
      toLog("connect callback Is Null");
      return;
    }else {
      try {
        if (Utils.isNull(getContext())){
          toLog("getContext() is Null");
          callback.onError("getContext() is Null");
          return;
        }else {
          startSDKRegistration(callback);
        }
      }catch (Exception e){
        Utils.toLog(TAG, "connect",null,e);
        callback.onError("Error");
      }catch (Error er){
        Utils.toLog(TAG, "connect",er,null);
        callback.onError("Error");
      }
    }

  }
  private void startSDKRegistration(final @NonNull OnAsyncOperationComplete callback) {
    String msg = "";
    if (Utils.isNull(callback)){
      toLog("startSDKRegistration listener is Null");
      return;
    }else {
      try {
        if (Utils.isNull(getContext())){
          msg = "startSDKRegistration  getContext() Is Null";
          toLog(msg);
          callback.onError(msg);
          return;
        }else {
          if (isRegistrationInProgress.compareAndSet(false, true)) {
            AsyncTask.execute(new Runnable() {
              @Override
              public void run() {
                // ToastUtils.setResultToToast(mContext.getString(R.string.sdk_registration_doing_message));
                //if we hope the Firmware Upgrade module could access the network under LDM mode, we need call the setModuleNetworkServiceEnabled()
                //method before the registerAppForLDM() method
                DJISDKManager.getInstance().getLDMManager().setModuleNetworkServiceEnabled(new LDMModule.Builder().moduleType(
                  LDMModuleType.FIRMWARE_UPGRADE).enabled(false).build());
                DJISDKManager.getInstance().registerApp(getContext().getApplicationContext(), new DJISDKManager.SDKManagerCallback() {
                  @Override
                  public void onRegister(DJIError djiError) {
                    try {
                      toLog("startSDKRegistration onRegister start");
                      if (djiError == DJISDKError.REGISTRATION_SUCCESS) {
                        DJILog.e("App registration", DJISDKError.REGISTRATION_SUCCESS.getDescription());
                        DJISDKManager.getInstance().startConnectionToProduct();
                        toLog("startSDKRegistration Register Success");
                        return;
                      } else {
                        // ToastUtils.setResultToToast(mContext.getString(R.string.sdk_registration_message) + djiError.getDescription());
                        String description = djiError.getDescription();
                        if (Utils.isNull(description)){
                          description = "";
                        }
                        toLog("startSDKRegistration Register sdk fails, check network is available");
                        setRegisted(false);
                        callback.onError("startSDKRegistration Register sdk fails, check network is available.. Detail:"+description);
                        return;
                      }
                    }catch (Exception e){
                      setRegisted(false);
                      Utils.toLog(TAG, "onRegister",null,e);
                      if (Utils.isNull(callback)){
                        return;
                      }else {
                        callback.onError("Error");
                      }
                    }catch (Error er){
                      setRegisted(false);
                      Utils.toLog(TAG, "onRegister",er,null);
                      if (Utils.isNull(callback)){
                        return;
                      }else {
                        callback.onError("Error");
                      }
                    }

                  }
                  @Override
                  public void onProductDisconnect() {
                    Log.d(TAG, "onProductDisconnect");
                    toLog("startSDKRegistration Product Disconnected");
                    notifyStatusChange(callback);
                  }
                  @Override
                  public void onProductConnect(BaseProduct baseProduct) {
                    Log.d(TAG, String.format("onProductConnect newProduct:%s", baseProduct));
                    toLog("startSDKRegistration Product onProductConnect start");
                    notifyStatusChange(callback);
                  }

                  @Override
                  public void onProductChanged(BaseProduct baseProduct) {
                    toLog("startSDKRegistration onProductChanged start");
                    notifyStatusChange(null);
                  }

                  @Override
                  public void onComponentChange(BaseProduct.ComponentKey componentKey,
                                                BaseComponent oldComponent,
                                                BaseComponent newComponent) {
                    toLog("startSDKRegistration onComponentChange start");
                    if (newComponent != null) {
                      newComponent.setComponentListener(new BaseComponent.ComponentListener() {

                        @Override
                        public void onConnectivityChange(boolean isConnected) {
                          Log.d("TAG", "onComponentConnectivityChanged: " + isConnected);
                          notifyStatusChange(null);
                        }
                      });
                    }
                  }

                  @Override
                  public void onInitProcess(DJISDKInitEvent djisdkInitEvent, int i) {
                    toLog("startSDKRegistration onInitProcess start");
                  }

                  @Override
                  public void onDatabaseDownloadProgress(long current, long total) {
                    toLog("startSDKRegistration onDatabaseDownloadProgress");
                  }
                });
              }


            });
          }else {
            playSucces(callback);

            return;
          }

        }
      }catch (Exception e){
        Utils.toLog(TAG, "startSDKRegistration",null,e);
        callback.onError("Error");
      }catch (Error er){
        Utils.toLog(TAG, "startSDKRegistration",er,null);
        callback.onError("Error");
      }
    }
  }

  private void notifyStatusChange(@Nullable OnAsyncOperationComplete callback) {
    if (Utils.isNull(callback)){
      return;
    }else {
      // BIN TO GET Aircraft instance
      if (Utils.isNull(getAircraft())){
        toLog("notifyStatusChange Stop cause getAircraft is Null ----------->PLEASE RECONNECT DRONE <-----------");
        callback.onError("notifyStatusChange Stop cause getAircraft is Null ----------->PLEASE RECONNECT DRONE <-----------");
        return;
      }else {
        playSucces(callback);
      }
    }
  }
  private void playSucces(OnAsyncOperationComplete callback) {
    try {
      new Handler(Looper.getMainLooper()).postDelayed(()->{
        try {
          toLog("connect BIND ---AFTER DONE");
          if (Utils.isNull(getAircraft())){
            toLog("connect getAircraft() is Null ----------->PLEASE RECONNECT DRONE <-----------");
            callback.onError("connect getAircraft() is Null ----------->PLEASE RECONNECT DRONE <-----------");
            return;
          }else {
            mFlightController = getAircraft().getFlightController();
            if (Utils.isNull(mFlightController)){
              toLog(" mFlightController Is Null or No Connected ----------->PLEASE RECONNECT DRONE <-----------");
              callback.onError(" mFlightController Is Null or No Connected ----------->PLEASE RECONNECT DRONE <-----------");
              return;
            }else {
              setRegisted(true);
              callback.onSucces("");
            }
          }
        }catch (Exception e){
          Utils.toLog(TAG, "playSucces",null,e);
          callback.onError("Error");
        }catch (Error er){
          Utils.toLog(TAG, "playSucces",er,null);
          callback.onError("Error");
        }
      }, 400);
    }catch (Exception e){
      Utils.toLog(TAG, "playSucces",null,e);
      callback.onError("Error");
    }catch (Error er){
      Utils.toLog(TAG, "playSucces",er,null);
      callback.onError("Error");
    }


  }

  @Override
  public void disconnect(@NonNull OnAsyncOperationComplete callback) {
    toLog("disconnect   start ");
    setJoystickEnable(false);

  }

  public void registerToListenDroneState(@NonNull OnStateChangeListener callBack) {
    this.listener = callBack;
    toLog("registerToListenDroneState  SET OKAY ");

    refreshSDKAndFlightState();
  }

  public void stopNotifyTelemetry(@NonNull OnAsyncOperationComplete callback){
    synchronized (DroneDJI.this){
      toLog("stopNotifyTelemetry Start");
      try {
        if (Utils.isNull(callback)){
          toLog("callback is Null");
          callback.onError("Error");
          return;
        }else {
          if (Utils.isNull(droneState)){
            toLog("droneState is Null");
            callback.onError("Error");
            return;
          }else {
            setAlreadyNotify(false);
            droneState.stopNotify();
            droneState = null;
            toLog("stopNotifyTelemetry SUCCES droneState to NULL");
            callback.onSucces("");
          }
        }
      }catch (Exception e){
        Utils.toLog(TAG, "stopNotifyTelemetry",null,e);
        if (Utils.isNull(callback)){
          return;
        }else {
          callback.onError("Error");
        }
      }catch (Error er){
        Utils.toLog(TAG, "stopNotifyTelemetry",er,null);
        if (Utils.isNull(callback)){
          return;
        }else {
          callback.onError("Error");
        }
      }
    }
  }

  @Override
  public void doTest(@NonNull final Activity activity, final OnAsyncOperationComplete callback) {
    try {
      if (Utils.isNull(callback)){
        toLog("callback is Null");
        return;
      }else {
        if (Utils.isNull(activity)){
          String msg = " Activity is Null";
          toLog(msg);
          callback.onError("Activity is Null  ----------->PLEASE RECONNECT DRONE <-----------");
          return;
        }else {
          if (!isValid(true, null, null)){
            String msg = " isValid FALSE not ready to FLY";
            toLog(msg);
            callback.onError("Config Not Valid ----------->PLEASE RECONNECT DRONE <-----------");
            return;
          }else {
            // doDummyMapping();
            goTo70MetersWaypoint(activity,callback);

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

  @Override
  public void onDestroy() {
    if (Utils.isNull(getContext())){
      toLog("onDestroy  getContext( Is Null");
      return;
    }else {
      try {
        setRegisted(false);
        setAlreadyNotify(false);
        setJoystickEnable(false);
        setting = null;
        LocationManager locationManager =LocationManager.getInstance();
        if (Utils.isNull(locationManager)){
          toLog("onDestroy locationManager is Null CANNOT Get Current Android POSITION");
          return;
        }else {
          locationManager.stopNotify();
        }
      }catch (Exception e){
        Utils.toLog(TAG, "onDestroy",null,e);
      }catch (Error er){
        Utils.toLog(TAG, "onDestroy",er,null);
      }
    }
  }

  public synchronized void sendJoystickData(float leftY, float leftX, float rightY, float rightX, final @NonNull OnAsyncOperationComplete callback){
//    var radians: Float = 0.0
//    let velocity: Float = 0.1
//    var x: Float = 0.0
//    var y: Float = 0.0
//    var z: Float = 0.0
//    var yaw: Float = 0.0
//    var yawSpeed: Float = 30.0
//    var throttle: Float = 0.0
//    var roll: Float = 0.0
//    var pitch: Float = 0.0
    if (Utils.isNull(callback)){
      toLog("sendJoystickData callback callback is Null");
      return;
    }else {
      try {
        String errorMSg = "";
        if (!isJoystickEnable()){
          errorMSg ="Joystick Is not enable ----------->PLEASE ENABLE JOYSTICK <-----------";
          toLog("sendJoystickData "+errorMSg);
          callback.onError(errorMSg);
          return;
        }else {
          if (!isValid(false,true, null)){
            toLog("sendJoystickData Stop cause isValid IS FALSE ");
            callback.onError("Parameter not valid  ----------->PLEASE RECONNECT DRONE <-----------");
            return;
          }else {
            if (Utils.isNull(mFlightController)){
              toLog("sendJoystickData mFlightController Is Null or No Connected 3----------->PLEASE RECONNECT DRONE <-----------");
              callback.onError(" mFlightController Is Null or No Connected ----------->PLEASE RECONNECT DRONE <-----------");
              return;
            }else {
              float pitch = 0.0f;
              float yawSpeed = 30.0f;
              float throttle = 0.0f;
              float roll =  0.0f;
              float yaw = (float) leftX * yawSpeed;

              throttle = ((float) leftY) * 5.0f * -1.0f; // inverting joystick for throttle

              pitch = ((float)rightY) * 1.0f;
              roll = ((float)rightX) * 1.0f;

              toLog(" sendJoystickData  yaw:"+Utils.clone(yaw )+ " roll:"+Utils.clone(roll) +" pitch "+Utils.clone(pitch) + " throttle:"+Utils.clone(throttle)+" yawSpeed "+Utils.clone(yawSpeed ));

              FlightControlData data = new FlightControlData(pitch,  roll, yaw, throttle);
              // data.setPitch();
              if (Utils.isNull(data)){
                toLog("sendJoystickData data Is Null ");
                callback.onError(" FlightControlData error ----------->PLEASE RETRY OR RESTART APP <-----------");
                return;
              }else {
                mFlightController.sendVirtualStickFlightControlData(data, (DJIError error) -> {
                  if (Utils.isNull(error)){
                    callback.onSucces("");
                    return;
                  }else {
                    String description = error.getDescription();
                    if (Utils.isNull(description)){
                      description = "";
                    }
                    toLog("sendJoystickData Error sending Joystick command "+description);
                    callback.onError(" Error sending Joystick command  ----------->PLEASE RETRY LATER <-----------");
                    return;
                  }
                });
              }
            }
          }
        }
      }catch (Exception e){
        Utils.toLog(TAG, "sendJoystickData",null,e);
        if (Utils.isNull(callback)){
          return;
        }else {
          callback.onError("Error");
        }
      }catch (Error er){
        Utils.toLog(TAG, "sendJoystickData",er,null);
        if (Utils.isNull(callback)){
          return;
        }else {
          callback.onError("Error");
        }
      }

    }

  }

  public synchronized void enableJoystickCommand(final @NonNull OnAsyncOperationComplete callback){
    if (Utils.isNull(callback)){
      toLog("enableJoystickCommand callback callback is Null");
      return;
    }else {
      try {
        if (!isValid(false, null,null)){
          toLog("enableJoystickCommand callback callback is Null");
          callback.onError("Parameter not valid  ----------->PLEASE RECONNECT DRONE <-----------");
          return;
        }else {
          if (Utils.isNull(mFlightController)){
            toLog("enableJostickCommand mFlightController Is Null or No Connected 2----------->PLEASE RECONNECT DRONE <-----------");
            callback.onError(" mFlightController Is Null or No Connected ----------->PLEASE RECONNECT DRONE <-----------");
            return;
          }else {
            if(isJoystickEnable()){
              toLog("enableJostickCommand isJoystickEnable ALREADY ENABLE");
              callback.onSucces("");
              return;
            }else {
              setJoystickEnable(false);
              connect(new OnAsyncOperationComplete() {
                @Override
                public void onError(@Nullable String errorDetail) {
                  setJoystickEnable(false);
                  if (Utils.isNull(errorDetail)){
                    errorDetail = "";
                  }
                  toLog("enableJostickCommand connect onError CAN NOT CONNETC TO DRONE "+errorDetail);
                  callback.onError("cannot connect to drone  ----------->PLEASE RECONNECT DRONE <-----------"+errorDetail);
                  return;
                }

                @Override
                public void onSucces(@Nullable String succesMsg) {
                  mFlightController.setVirtualStickModeEnabled(true, (DJIError error) -> {
                    if (Utils.isNull(error)){
                      setJoystickEnable(true);
                      callback.onSucces("");
                      return;
                    }else {
                      String description = error.getDescription();
                      if (Utils.isNull(description)){
                        description = "";
                      }
                      setJoystickEnable(false);
                      toLog("enableJostickCommand Failed Error Description:"+description);
                      callback.onError("enable joystick Failed Error detail: "+description);
                    }
                  });
                }
              });
            }
          }

        }
      }catch (Exception e){
        Utils.toLog(TAG, "enableJoystickCommand",null,e);
        if (Utils.isNull(callback)){
          return;
        }else {
          callback.onError("Error");
        }
      }catch (Error er){
        Utils.toLog(TAG, "enableJoystickCommand",er,null);
        if (Utils.isNull(callback)){
          return;
        }else {
          callback.onError("Error");
        }
      }
    }
  }

  public synchronized void disableJoystickCommand(final @NonNull OnAsyncOperationComplete callback){
    if (Utils.isNull(callback)){
      toLog("disableJoystickCommand callback callback is Null");
      return;
    }else {
      try {
        if (!isValid(false, null,null)){
          toLog("disableJoystickCommand callback callback is Null");
          callback.onError("Parameter not valid  ----------->PLEASE RECONNECT DRONE <-----------");
          return;
        }else {
          if (Utils.isNull(mFlightController)){
            toLog("disableJoystickCommand  mFlightController Is Null or No Connected 2----------->PLEASE RECONNECT DRONE <-----------");
            callback.onError(" mFlightController Is Null or No Connected ----------->PLEASE RECONNECT DRONE <-----------");
            return;
          }else {
            if(!isJoystickEnable()){
              toLog("disableJoystickCommand ALREADY DISABLED");
              callback.onSucces("");
              return;
            }else {
              connect(new OnAsyncOperationComplete() {
                @Override
                public void onError(@Nullable String errorDetail) {
                  setJoystickEnable(false);
                  if (Utils.isNull(errorDetail)){
                    errorDetail = "";
                  }
                  toLog("disableJoystickCommand connect onError CAN NOT CONNETC TO DRONE "+errorDetail);
                  callback.onError("cannot connect to drone  ----------->PLEASE RECONNECT DRONE <-----------"+errorDetail);
                  return;
                }

                @Override
                public void onSucces(@Nullable String succesMsg) {
                  mFlightController.setVirtualStickModeEnabled(false, (DJIError error) -> {
                    if (Utils.isNull(error)){
                      setJoystickEnable(false);
                      callback.onSucces("");
                    }else {
                      String description = error.getDescription();
                      if (Utils.isNull(description)){
                        description = "";
                      }
                      setJoystickEnable(true);
                      toLog("disableJoystickCommand Failed Error Description:"+description);
                      callback.onError("disable joystick Failed Error detail: "+description);
                    }
                  });
                }
              });
            }
          }
        }
      }catch (Exception e){
        Utils.toLog(TAG, "disableJoystickCommand",null,e);
        if (Utils.isNull(callback)){
          return;
        }else {
          callback.onError("Error");
        }
      }catch (Error er){
        Utils.toLog(TAG, "disableJoystickCommand",er,null);
        if (Utils.isNull(callback)){
          return;
        }else {
          callback.onError("Error");
        }
      }
    }
  }

  @Override
  public synchronized void takeOff(final @NonNull OnAsyncOperationComplete callback) {
    if (Utils.isNull(callback)){
      toLog("takeOff callback is Null");
      return;
    }else {
      try {
        if (!isValid(true,null,true)){
          toLog("takeOff Parameter not valid");
          callback.onError("Parameter not valid  ----------->PLEASE RECONNECT DRONE <-----------");
          return;
        }else {
          FlightControllerKey keyTakeOff = FlightControllerKey.create(FlightControllerKey.TAKE_OFF);
          if (Utils.isNull(keyTakeOff)){
            toLog("takeOff keyTakeOff is Null");
            callback.onError("keyTakeOff is Null  ----------->CANNOT PERFORM TAKE OFF<-----------");
            return;
          }else {
            performDroneAction(keyTakeOff,callback);
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
  }

  @Override
  public synchronized void landing(final @NonNull OnAsyncOperationComplete callback) {
    if (Utils.isNull(callback)){
      toLog("landing callback is Null");
      return;
    }else {
      try {
        if (!isValid(true,null,true)){
          toLog("landing Parameter not valid");
          callback.onError("Parameter not valid  ----------->PLEASE RECONNECT DRONE <-----------");
          return;
        }else {
          if (Utils.isNull(mFlightController)){
            toLog("mFlightController is Null");
            callback.onError("mFlightController is Null  ----------->PLEASE RECONNECT DRONE <-----------");
            return;
          }else {
            mFlightController.startLanding((DJIError djiError) ->{
              if (djiError == null){
                toLog("startLanding  onResult LANDINg SUCCES");
                callback.onSucces("");
                return;
              }else {
                String description = djiError.getDescription();
                if (Utils.isNull(description)){
                  description = "";
                }
                toLog("startLanding  onResult Error Happen  "+ description);
                callback.onError("startLanding Failed "+description);
                return;
              }
            });
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
  }

  @Override
  public synchronized void returnToHome(final @NonNull OnAsyncOperationComplete callback) {
    if (Utils.isNull(callback)){
      toLog("returnToHome callback is Null");
      return;
    }else {
      try {
        Runnable actionReturn = ()->{
          if (!isValid(true,null,true)){
            toLog("returnToHome Parameter not valid");
            callback.onError("Parameter not valid  ----------->PLEASE RECONNECT DRONE <-----------");
            return;
          }else {
            FlightControllerKey keyGoHome = FlightControllerKey.create(FlightControllerKey.START_GO_HOME);
            if (Utils.isNull(keyGoHome)){
              toLog("returnToHome keyGoHome is Null");
              callback.onError("keyGoHome is Null  ----------->CANNOT PERFORM GO HOME<-----------");
              return;
            }else {
              performDroneAction(keyGoHome,callback);
            }
          }
        };
        // Case Mission occcur
        if (!Utils.isNull(mission)){
          // Cancel all Next Step
          mission.cancelCurrentCauseGoHome(new OnAsyncOperationComplete() {
            @Override
            public void onError(@Nullable @org.jetbrains.annotations.Nullable String errorDetail) {
              toLog("returnToHome Cannot Stop Current Mission");
              callback.onError("returnToHome Cannot Stop Current Mission  ----------->CANNOT PERFORM GO HOME<-----------");
              return;
            }

            @Override
            public void onSucces(@Nullable @org.jetbrains.annotations.Nullable String succesMsg) {
              actionReturn.run();
            }
          });
        }else {
          // Not Cartho Mission
          actionReturn.run();
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

  }

  private  void performDroneAction(@NonNull FlightControllerKey keyGoHome, @NonNull OnAsyncOperationComplete callback) {
    synchronized (this){
      try {
        if (Utils.isNull(callback)){
          toLog("performDroneAction callback is Null");
          return;
        }else {
          if (Utils.isNull(callback)){
            toLog("performDroneAction keyGoHome is Null");
            callback.onError("keyGoHome is Null 2 ----------->CANNOT PERFORM ACTION<-----------");
            return;
          }else {
            if (KeyManager.getInstance() != null) {
              KeyManager.getInstance().performAction(keyGoHome, new ActionCallback() {
                public void onSuccess() {
                  callback.onSucces("");
                }

                public void onFailure(@NonNull DJIError error) {
                  if (Utils.isNull(error)){
                    callback.onError("keyGoHome Error ");
                    return;
                  }else {
                    String des = error.getDescription();
                    if (Utils.isNull(des)){
                      des = "";
                    }
                    callback.onError(des);
                  }
                }
              }, new Object[0]);
            }else {
              toLog("performDroneAction KeyManager.getInstance() is Null");
              callback.onError("KeyManager instance is Null ----------->CANNOT PERFORM ACTION<-----------");
              return;
            }
          }
        }
      }catch (Exception e){
        Utils.toLog(TAG, "performDroneAction",null,e);
      }catch (Error er){
        Utils.toLog(TAG, "performDroneAction",er,null);
      }
    }
  }

  public static boolean isModelSupported(@NonNull Model model)throws Exception,Error{
    if (Utils.isNull(model)){
      return false;
    }else {
      if (model != Model.PHANTOM_4 && model != Model.PHANTOM_4_ADVANCED){
        Utils.toLog("DroneDJI"," Drone Model Not Supported ----------->PLEASE USE PHONTOM 4 ADVANCED OR ADD CAMERA SETTING FOR NEW DRONE <-----------");
        return false;
      }else {
        return true;
      }
    }

  }


  public synchronized void startWaypointMission(@NonNull final Activity activity, @NonNull CopyOnWriteArrayList<LatLng> points, final @NonNull MissionSetting missionSetting, final @NonNull OnMissionRunningListener callback) {
    toLog(" startWaypointMission start DRONE DJI OBject");
    if (Utils.isNull(callback)){
      toLog("startWaypointMission callback is Null");
      return;
    }else {
      try {
        if (Utils.isNull(activity)){
          toLog(" startWaypointMission activity is null");
          callback.onError(" startWaypointMission activity is null");
          return;
        }else {
          if (Utils.isNull(points)){
            toLog(" startWaypointMission points is null");
            callback.onError(" startWaypointMission points is null");
            return;
          }else {
            if (points.size() == 0){
              toLog(" startWaypointMission points is EMPTY");
              callback.onError(" startWaypointMission points is EMPTY");
              return;
            }else {
              if (points.size() < Constants.POLYGON_SIZE){
                toLog(" startWaypointMission points is Not a Polygon");
                callback.onError(" startWaypointMission points is Not a Polygon");
                return;
              }else {
                if (Utils.isNull(missionSetting)){
                  toLog(" startWaypointMission Mission Setting isNull");
                  callback.onError(" startWaypointMission Mission Setting isNull");
                  return;
                }else {
                  if (Utils.isNull(droneState)){
                    toLog(" startWaypointMission droneState isNull");
                    callback.onError(" Mission Telemetry is Null");
                    return;
                  }else {
                    if (!missionSetting.isValid(null)){
                      String msg = " startWaypointMission "+missionSetting.getErrorMsg();
                      toLog(msg);
                      callback.onError(msg);
                      return;
                    }else {
                      if (!isValid(true,null,true)){
                        toLog("startWaypointMission Parameter not valid");
                        callback.onError("Parameter not valid  ----------->PLEASE RECONNECT DRONE AND SET SET GO BACK HOME LOCATION");
                        return;
                      }else {
                        if (Utils.isNull(mFlightController)){
                          toLog("startWaypointMission mFlightController is Null");
                          callback.onError("mFlightController is Null  ----------->PLEASE RECONNECT DRONE <-----------");
                          return;
                        }else {
                          if (Utils.isNull(getAircraft())){
                            toLog("startWaypointMission getAircraft() is Null ----------->PLEASE RECONNECT DRONE <-----------");
                            callback.onError("startWaypointMission getAircraft() is Null ----------->PLEASE RECONNECT DRONE <-----------");
                            return;
                          }else {
                            toLog(" startWaypointMission MODEL CHECKING");

                            Model model = getAircraft().getModel();
                            if (Utils.isNull(model)){
                              toLog("startWaypointMission Drone Model is Null ----------->PLEASE RECONNECT DRONE <-----------");
                              callback.onError("startWaypointMission Drone Model is Null ----------->PLEASE RECONNECT DRONE <-----------");
                              return;
                            }else {
                              if (!isModelSupported(model)){
                                toLog("startWaypointMission Drone Model Not Supported ----------->PLEASE USE PHONTOM 4 ADVANCED OR ADD CAMERA SETTING FOR NEW DRONE <-----------");
                                callback.onError("startWaypointMission Drone Model Not Supported ----------->PLEASE USE PHONTOM 4 ADVANCED OR ADD CAMERA SETTING FOR NEW DRONE <-----------");
                                return;
                              }else {
                                if (Utils.isNull(missionSetting.getHomeLocation())){
                                  toLog(" startWaypointMission missionSetting.HomeLocation is Null");
                                  callback.onError("missionSetting.HomeLocation is Null");
                                  return;
                                }else {
                                  CameraParameter params = CameraParameter.getDJIPhantom4Advanced(missionSetting.getAltitudePrefered());
                                  if (Utils.isNull(params)){
                                    toLog(" createGridAndShowIt params is Null");
                                    callback.onError(" createGridAndShowIt homeLocation is BAD");
                                    return;
                                  }else {
                                    setting = null;
                                    setting = MissionDJI.calcGridSetting(params);
                                    if (Utils.isNull(setting)){
                                      toLog(" createGridAndShowIt setting is Null");
                                      callback.onError(" createGridAndShowIt setting is Null");
                                      return;
                                    }else {
                                      Camera camera =  getAircraft().getCamera();
                                      if (Utils.isNull(camera)){
                                        toLog("camera is Null");
                                        callback.onError("camera is Null  ----------->PLEASE RECONNECT DRONE <-----------");
                                        return;
                                      }else {
                                        configShutterSpeedAndAngle(callback,camera,()->{
                                          Runnable executeMission = ()->{
                                            try {
                                              mission =  new MissionDJI();
                                              if (missionSetting.useSimulator()){
                                                // Case Simulator
                                                final Simulator simulator =  mFlightController.getSimulator();
                                                LocationCoordinate2D homeLocation = new LocationCoordinate2D(missionSetting.getHomeLocation().latitude, missionSetting.getHomeLocation().longitude);
                                                if (Utils.isNull(simulator)){
                                                  toLog("startWaypointMission simulator Object Is Null");
                                                  callback.onError("startWaypointMission simulator Object Is Null");
                                                  return;
                                                }else {
                                                  toLog("startWaypointMission simulator Will be enable");
                                                  Runnable enableSimulator = () ->{
                                                    simulator
                                                            .start(InitializationData.createInstance(homeLocation, 10, 10),
                                                                    errorSim -> {
                                                                      if (errorSim != null) {
                                                                        toLog(" Simulator ini Failed");
                                                                        toLog(" Simulator ini Failed :"+errorSim.getDescription());
                                                                        callback.onError(" Simulator ini Failed :"+errorSim.getDescription());
                                                                        return;
                                                                      }else
                                                                      {
                                                                        toLog("Start Simulator Success");
                                                                        mission.createGridAndShowIt(activity,points,missionSetting,setting,droneState,callback);
                                                                      }
                                                                    });
                                                  };
                                                  if (simulator.isSimulatorActive()){
                                                    simulator.stop((DJIError error) -> {
                                                      if (!Utils.isNull(error)){
                                                        String detail = error.getDescription();
                                                        if (Utils.isNull(detail)){
                                                          detail = "";
                                                        }
                                                        toLog("startWaypointMission Stop simulation failed: "+detail);
                                                        callback.onError("startWaypointMission Stop simulation failed: "+detail);
                                                        return;
                                                      }else {
                                                        enableSimulator.run();
                                                      }

                                                    });
                                                  }else {
                                                    enableSimulator.run();
                                                  }

                                                }
                                              }else {
                                                toLog("startWaypointMission simulator DISABLED");
                                                mission.createGridAndShowIt(activity,points,missionSetting,setting,droneState,callback);
                                              }
                                            }catch (Exception e){
                                              Utils.toLog(TAG, "executeMission",null,e);
                                              if (Utils.isNull(callback)){
                                                return;
                                              }else {
                                                callback.onError("");
                                              }
                                            }catch (Error er){
                                              Utils.toLog(TAG, "executeMission",er,null);
                                              if (Utils.isNull(callback)){
                                                return;
                                              }else {
                                                callback.onError("");
                                              }
                                            }
                                          };
                                          if (!Utils.isNull(mission)){
                                            // May Be other mission occur
                                            mission.cancelCurrentCauseGoHome(new OnAsyncOperationComplete() {
                                              @Override
                                              public void onError(@Nullable String errorDetail) {
                                                toLog("startWaypointMission  cancelCurrentCauseGoHome onError");
                                                callback.onError("startWaypointMission  cancelCurrentCauseGoHome onError");
                                                return;
                                              }

                                              @Override
                                              public void onSucces(@Nullable String succesMsg) {
                                                toLog("startWaypointMission cancelCurrentCauseGoHome onSucces");
                                                executeMission.run();
                                              }
                                            });
                                          }else {
                                            executeMission.run();
                                          }
                                        });
                                      }
                                    }
                                  }
                                }
                              }

                            }

                          }

                        }
                      }
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
  }

  private void configShutterSpeedAndAngle(@NonNull OnMissionRunningListener callback,@NonNull Camera camera, Runnable actionContinu)throws Exception,Error {
    if (Utils.isNull(callback)){
      return;
    }else {
      if (Utils.isNull(camera)){
        toLog("configShutterSpeedAndAngle Camera Is Null ");
        callback.onError("error: CANNOT config Camera cause Object is Null");
        return;
      }else {
        if (Utils.isNull(actionContinu)){
          toLog("configShutterSpeedAndAngle actionContinu Is Null ");
          callback.onError("error: CANNOT config Camera cause actionContinu is Null");
          return;
        }else {
          camera.setExposureMode(SettingsDefinitions.ExposureMode.SHUTTER_PRIORITY, (DJIError exposureModeError) -> {
            if (!Utils.isNull(exposureModeError)){
              String detail = exposureModeError.getDescription();
              if (Utils.isNull(detail)){
                detail = "";
              }
              toLog(" error: CANNOT SET ExposureMode TO SHUTTER_PRIORITY detail: "+detail);
              callback.onError("error: CANNOT SET ExposureMode TO SHUTTER_PRIORITY   ----------->DRONE MAY BE NOT SUPPORTED <-----------");
              return;
            }else {
              toLog("GIMBAL Rotation setExposureMode Succes");
              camera.setShutterSpeed(SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_800, shutterSpeedError -> {
                if (!Utils.isNull(shutterSpeedError)){
                  String detail = shutterSpeedError.getDescription();
                  if (Utils.isNull(detail)){
                    detail = "";
                  }
                  toLog(" error: CANNOT SET ShutterSpeed TO SHUTTER_PRIORITY detail: "+detail);
                  callback.onError("error: CANNOT SET ShutterSpeed   ----------->DRONE MAY BE NOT SUPPORTED <-----------");
                  return;
                }else {
                  camera.setPhotoFileFormat(SettingsDefinitions.PhotoFileFormat.JPEG, (@Nullable DJIError cameraError) -> {
                    if (!Utils.isNull(cameraError)){
                      String detail = cameraError.getDescription();
                      if (Utils.isNull(detail)){
                        detail = "";
                      }
                      toLog("startWaypointMission camera setPhotoFileFormat Error Happen: "+detail);
                      callback.onError("Cannot set Photo file format to JPEG Please Retry later");
                      return;
                    }else {
                      if (Utils.isNull(getAircraft())){
                        toLog(" getAircraft() is Null ----------->PLEASE RECONNECT DRONE <-----------");
                        callback.onError(" getAircraft() is Null ----------->PLEASE RECONNECT DRONE <-----------");
                        return;
                      }else {
                        Gimbal gimbal = getAircraft().getGimbal();
                        if (Utils.isNull(gimbal)){
                          toLog("gimbal is Null");
                          callback.onError("gimbal is Null  ----------->PLEASE RECONNECT DRONE <-----------");
                          return;
                        }else {
                          gimbal.rotate(new Rotation.Builder()
                                  .pitch(-90)
                                  .mode(RotationMode.ABSOLUTE_ANGLE)
                                  .yaw(Rotation.NO_ROTATION)
                                  .roll(Rotation.NO_ROTATION)
                                  .time(0)
                                  .build(), rotateError -> {
                            if (!Utils.isNull(rotateError)){
                              String detail = rotateError.getDescription();
                              if (Utils.isNull(detail)){
                                detail = "";
                              }
                              toLog("CANNOT move GIMBAL to Angle ");
                              callback.onError("CANNOT move GIMBAL to Angle   ----------->DRONE MAY BE NOT SUPPORTED <-----------");
                              return;
                            }else {
                              actionContinu.run();
                            }
                          });
                        }
                      }
                    }
                  });
                }
              });
            }
          });
        }
      }
    }

  }

  @RequiresApi(api = Build.VERSION_CODES.N)
  public synchronized void continuWaypointMission(@NonNull final Activity activity, final @NonNull OnMissionRunningListener callback) {
    toLog(" continuWaypointMission ");
    try {
      if (Utils.isNull(callback)){
        toLog("continuWaypointMission callback is Null");
        return;
      }else {
        if (Utils.isNull(activity)){
          toLog(" continuWaypointMission activity is null");
          callback.onError(" continuWaypointMission activity is null");
          return;
        }else {
          if (!isValid(true,null,true)){
            toLog("continuWaypointMission Parameter not valid");
            callback.onError("Parameter not valid  ----------->PLEASE RECONNECT DRONE THEN SET SET GO BACK HOME LOCATION AND LISTEN TELEMETRY");
            return;
          }else {
            if (Utils.isNull(droneState)){
              toLog(" continuWaypointMission droneState isNull");
              callback.onError(" Mission Telemetry is Null");
              return;
            }else {
              if (Utils.isNull(getAircraft())){
                toLog("continuWaypointMission getAircraft() is Null ----------->PLEASE RECONNECT DRONE <-----------");
                callback.onError("continuWaypointMission getAircraft() is Null ----------->PLEASE RECONNECT DRONE <-----------");
                return;
              }else {
                Model model = getAircraft().getModel();
                if (Utils.isNull(model)){
                  toLog("continuWaypointMission Drone Model is Null ----------->PLEASE RECONNECT DRONE <-----------");
                  callback.onError("continuWaypointMission Drone Model is Null ----------->PLEASE RECONNECT DRONE <-----------");
                  return;
                }else {
                  if (!isModelSupported(model)){
                    toLog("continuWaypointMission Drone Model Not Supported ----------->PLEASE USE PHONTOM 4 ADVANCED OR ADD CAMERA SETTING FOR NEW DRONE <-----------");
                    callback.onError("continuWaypointMission Drone Model Not Supported ----------->PLEASE USE PHONTOM 4 ADVANCED OR ADD CAMERA SETTING FOR NEW DRONE <-----------");
                    return;
                  }else {
                    Camera camera =  getAircraft().getCamera();
                    if (Utils.isNull(camera)){
                      toLog("camera is Null");
                      callback.onError("camera is Null  ----------->PLEASE RECONNECT DRONE <-----------");
                      return;
                    }else {
                      configShutterSpeedAndAngle(callback,camera, ()->{
                        Runnable actionContinu = () ->{
                          mission = new MissionDJI();
                          mission.continuMission(activity,droneState,callback);
                        };
                        if (!Utils.isNull(mission)){
                          // May Be other mission occur
                          mission.cancelCurrentCauseGoHome(new OnAsyncOperationComplete() {
                            @Override
                            public void onError(@Nullable String errorDetail) {
                              toLog("continuWaypointMission cancelCurrentCauseGoHome onError");
                            }

                            @Override
                            public void onSucces(@Nullable String succesMsg) {
                              toLog("continuWaypointMission cancelCurrentCauseGoHome onSucces");
                              actionContinu.run();
                            }
                          });
                        }else {
                          actionContinu.run();
                        }
                      });
                    }
                  }
                }
              }
            }
          }
        }
      }
    }catch (Exception e){
      Utils.toLog(TAG, "continuWaypointMission",null,e);
    }catch (Error er){
      Utils.toLog(TAG, "continuWaypointMission",er,null);
    }

  }

  @Override
  @RequiresApi(api = Build.VERSION_CODES.N)
  public void isMappingMissionExist(final Context ctx, OnAsyncOperationCompleteBool callback) {
    if (Utils.isNull(callback)){
      toLog("isMappingMissionExist callback is Null");
      return;
    }else {
      if (Utils.isNull(ctx)){
        toLog(" isMappingMissionExist context is null");
        callback.onResultNo(" isMappingMissionExist activity is null");
        return;
      }else {
        MissionDJI missionDJI =  new MissionDJI();
        missionDJI.isMappingMissionExist(ctx,callback);
      }
    }

  }

  @Override
  @RequiresApi(api = Build.VERSION_CODES.N)
  public void cleanUserData(Context ctx, OnAsyncOperationCompleteBool callback) {
    if (Utils.isNull(callback)){
      toLog("isMappingMissionExist callback is Null");
      return;
    }else {
      if (Utils.isNull(ctx)){
        toLog(" isMappingMissionExist context is null");
        callback.onResultNo(" isMappingMissionExist activity is null");
        return;
      }else {
        MissionDJI missionDJI =  new MissionDJI();
        missionDJI.cleanUserData(ctx,callback);
      }
    }
  }


  public boolean isValidWithouRegister(){
    try {
      if (Utils.isNull(getContext())){
        return false;
      }else{
        if (getAircraft() == null) {
          toLog(" isValid getAircraft()  IS NULL ----------->PLEASE RECONNECT DRONE <-----------");
          return false;
        }else {
          if (!getAircraft().isConnected()){
            toLog(" isValid getAircraft() Is Not Connected  IS NULL ----------->PLEASE RECONNECT DRONE <-----------");
            return false;
          }else {
            // Try again
            if (Utils.isNull(mFlightController)){
              mFlightController = getAircraft().getFlightController();
            }
            if (Utils.isNull(mFlightController)){
              toLog(" mFlightController IS NULL ----------->PLEASE RECONNECT DRONE <-----------");
              return false;
            }else{
              if (getAircraft().getModel() == Model.UNKNOWN_AIRCRAFT) {
                toLog("isValid  getAircraft() Is UNKNOWN_AIRCRAFT ----------->PLEASE RECONNECT DRONE <-----------");
                return false;
              }else {
                PermissionManager pm =new PermissionManager(getContext());
                // If there is enough permission, we will start the registration
                if (!pm.isPermissionsGranted()){
                  toLog("Missing permissions!!!");
                  return false;
                }else {
                  return true;
                }

              }
            }

          }
        }
      }
    }catch (Exception e){
      Utils.toLog(TAG, "isValidWithouRegister",null,e);
    }catch (Error er){
      Utils.toLog(TAG, "isValidWithouRegister",er,null);
    }
    return false;
  }


  @Override
  public boolean isValid(boolean caseToFly,@Nullable Boolean caseJoystick,@Nullable Boolean landingOrTakeOff) {
    try {
      if (Utils.isNull(caseJoystick)){
        caseJoystick = false;
      }
      if (Utils.isNull(landingOrTakeOff)){
        landingOrTakeOff = false;
      }
      if (caseJoystick){
        if (!isValid(true,null, null)){
          toLog("isValid  caseJoystick  !isValid(true,null) IS FALSE");
          return false;
        }else {
          if (!isJoystickEnable()){
            toLog("isValid  caseJoystick  JoystickEnable IS NOT Enable");
            return false;
          }else {
            return true;
          }
        }
      }
      if (caseToFly){
        if (Utils.isNull(listener)){
          toLog("listener is Null");
          return false;
        }else {
          if (!isRegisted()){
            toLog("isValid  is Not registed");
            return false;
          }else {
            if (Utils.isNull(mFlightController)){
              toLog("mFlightController is Null ----------->PLEASE RECONNECT DRONE <-----------");
              return false;
            }else {
              if (landingOrTakeOff){
                return isValidWithouRegister();
              }else {
                if (!mFlightController.getState().isHomeLocationSet()){
                  toLog("isHomeLocationSet is False ----------->PLEASE RECONNECT DRONE <-----------");
                  return false;
                }else {
                  return isValidWithouRegister();
                }
              }

            }
          }
        }
      }else {
        return isValidWithouRegister();
      }
    }catch (Exception e){
      Utils.toLog(TAG, "isValid",null,e);
    }catch (Error er){
      Utils.toLog(TAG, "isValid",er,null);
    }
    return false;

  }

  public void attachBaseContext(@NonNull Context paramContext,@NonNull Application app){
    Utils.toLog(TAG, " attachBaseContext SetSUCCES");
  }

  public void onCreate() {

    Utils.toLog(TAG, " onCreate SUCCES");
  }



  private void refreshSDKAndFlightState() {
    try {
      synchronized (DroneDJI.this){
        toLog("refreshSDKAndFlightState Start");
        if (isAlreadyNotify()){
          toLog("refreshSDKAndFlightState Stop cause Not isAlreadyNotify");
          return;
        }else {
          if (Utils.isNull(getAircraft())){
            toLog("refreshSDKAndFlightState Stop cause getAircraft is Null ----------->PLEASE RECONNECT DRONE <-----------");
            setAlreadyNotify(false);
            return;
          }else {
            if (!isValid(false,null, null)){
              toLog("refreshSDKAndFlightState Stop cause Not valid");
              setAlreadyNotify(false);
              return;
            }else {
              Model model = getAircraft().getModel();
              if (Utils.isNull(model)){
                toLog("refreshSDKAndFlightState Stop cause model is Null ----------->PLEASE RECONNECT DRONE <-----------");
                setAlreadyNotify(false);
                return;
              }else {
                String displayName = getAircraft().getModel().getDisplayName();
                if (Utils.isNull(displayName)){
                  toLog("refreshSDKAndFlightState Stop cause displayName is Null ----------->PLEASE RECONNECT DRONE <-----------");
                  setAlreadyNotify(false);
                  return;
                }else {
                  setName(displayName);
                  setAlreadyNotify(true);
                  droneState = null;
                  droneState = new DroneState(getAircraft(), new OnStateChangeListener() {
                    @Override
                    public void onChange(int uplinkSignalQuality, short satelliteCount, int chargeRemainingInPercent, int chargeRemainingInMAh, float temperature, float altitudeInMeters, float speedInMeterPerSec, float heading, int numberOfDischarges, int lifetimeRemainingInPercent, double longitude, double latitude, float altitude, int droneHeadingInDegrees, @NonNull String droneModelName) {
                      synchronized (DroneDJI.this){
                        try {
                          // Copy to Object
                          setLatitude(latitude);
                          setLongitude(longitude);
                          setAltitude(altitude);
                          setUplinkSignalQuality(uplinkSignalQuality);
                          setSatelliteCount(satelliteCount);
                          setChargeRemainingInPercent(chargeRemainingInPercent);
                          setChargeRemainingInMAh(chargeRemainingInMAh);
                          setTemperature(temperature);
                          setSpeedInMeterPerSec(speedInMeterPerSec);
                          // Notify Listener
                          if (Utils.isNull(listener)){
                            toLog("refreshSDKAndFlightState onChange stop cause listener is Null");
                            return;
                          }else {
                            listener.onChange(uplinkSignalQuality,satelliteCount,chargeRemainingInPercent,chargeRemainingInMAh,temperature,altitudeInMeters,speedInMeterPerSec, heading, numberOfDischarges, lifetimeRemainingInPercent, longitude,latitude,altitude,droneHeadingInDegrees,getName());
                          }
                        }catch (Exception e){
                          Utils.toLog(TAG, "refreshSDKAndFlightState onChange",null,e);

                        }catch (Error er){
                          Utils.toLog(TAG, "refreshSDKAndFlightState onChange",er,null);
                        }
                      }
                    }

                    @Override
                    public void onLog(String msg) {
                      toLog(msg);
                    }

                    @Override
                    public void onConnectFailed() {
                      try {
                        if (Utils.isNull(listener)){
                          toLog("refreshSDKAndFlightState onChange stop cause listener is Null 2");
                          return;
                        }else {
                          listener.onConnectFailed();
                        }
                      }catch (Exception e){
                        Utils.toLog(TAG, "refreshSDKAndFlightState onChange",null,e);
                      }catch (Error er){
                        Utils.toLog(TAG, "refreshSDKAndFlightState onChange",er,null);
                      }

                    }

                    @Override
                    public void onConnectSucces() {
                      try {
                        if (Utils.isNull(listener)){
                          toLog("refreshSDKAndFlightState onChange stop cause listener is Null 2");
                          return;
                        }else {
                          // Will Not fired
                          listener.onConnectFailed();
                        }
                      }catch (Exception e){
                        Utils.toLog(TAG, "refreshSDKAndFlightState onChange",null,e);
                      }catch (Error er){
                        Utils.toLog(TAG, "refreshSDKAndFlightState onChange",er,null);
                      }
                    }
                  });
                  droneState.initStateListener(()->{

                  });


                }
              }

            }
          }
        }
      }
    }catch (Exception e){
      Utils.toLog(TAG, "refreshSDKAndFlightState",null,e);

    }catch (Error er){
      Utils.toLog(TAG, "refreshSDKAndFlightState",er,null);
    }

  }
  private void toLog( @NonNull String msg){
    Utils.toLog(TAG, msg);
  }


  private void goTo70MetersWaypoint(@NonNull final Activity activity,OnAsyncOperationComplete callback) {
    if (Utils.isNull(callback)){
      toLog("goTo70MetersWaypoint Stop cause callback");
      return;
    }else {
      try {
        if (Utils.isNull(activity)){
          callback.onError("Activity is Null is Null");
          return;
        }else {
          CopyOnWriteArrayList<LatLng> pointList = new Dummy().getPointsAsLatLng();
          WaypointMissionHeadingMode mHeadingMode = WaypointMissionHeadingMode.AUTO;
          float mSpeed = 10.0f;
          float ALTITUDE_PREFERED = 70.0f;

          if (Utils.isNull(pointList)){
            toLog("addPoints Stop cause pointList IS Null");
            callback.onError("pointList is Null");
            return;
          }else {
            for (LatLng point :pointList) {
              Waypoint mWaypoint = new Waypoint(point.latitude, point.longitude, ALTITUDE_PREFERED);
              waypointList.add(mWaypoint);
            }
            toLog("addPoints Succes");
          }

          WaypointMission.Builder waypointMissionBuilder;
          WaypointMissionFinishedAction mFinishedAction = WaypointMissionFinishedAction.GO_HOME;
          waypointMissionBuilder = new WaypointMission.Builder().finishedAction(mFinishedAction)
                  .headingMode(mHeadingMode)
                  .autoFlightSpeed(mSpeed)
                  .maxFlightSpeed(mSpeed)
                  .waypointList(waypointList)
                  .waypointCount(waypointList.size())
                  .setExitMissionOnRCSignalLostEnabled(true)
                  .flightPathMode(WaypointMissionFlightPathMode.NORMAL);
          if (Utils.isNull(waypointMissionBuilder)){
            toLog("goTo70MetersWaypoint stop cause waypointMissionBuilder IS Null");
            callback.onError("goTo70MetersWaypoint stop cause waypointMissionBuilder IS Null");
            return;
          }else{
            if (Utils.isNull(mFlightController)){
              toLog("goTo70MetersWaypoint mFlightController IS Null ----------->PLEASE RECONNECT DRONE <-----------");
              callback.onError("goTo70MetersWaypoint mFlightController IS Null ----------->PLEASE RECONNECT DRONE <-----------");
              return;
            }else {
              if (!mFlightController.getState().isHomeLocationSet()){
                toLog("goTo70MetersWaypoint Home Location not Set POSITION Not found YET");
                callback.onError("goTo70MetersWaypoint Home Location not Set POSITION Not found YET");
                return;
              }else {
                toLog("loadWaypoint Before getMaxFlightSpeed: "+ Utils.clone(waypointMissionBuilder.getMaxFlightSpeed()));
                toLog("loadWaypoint Before getAutoFlightSpeed: "+ Utils.clone(waypointMissionBuilder.getAutoFlightSpeed()));
                toLog("loadWaypoint Before getWaypointCount : "+ Utils.clone(waypointMissionBuilder.getWaypointCount()));
                toLog("loadWaypoint Before LastPoint Altitude : "+ Utils.clone(waypointMissionBuilder.getWaypointList().get(waypointMissionBuilder.getWaypointList().size() -1).altitude));
                toLog("loadWaypoint Before LastPoint Latitude: "+ Utils.clone(waypointMissionBuilder.getWaypointList().get(waypointMissionBuilder.getWaypointList().size() -1).coordinate.getLatitude()));
                toLog("loadWaypoint Before LastPoint Latitude: "+ Utils.clone(waypointMissionBuilder.getWaypointList().get(waypointMissionBuilder.getWaypointList().size() -1).coordinate.getLongitude()));

                WaypointMissionOperator waypointMissionOperator = DJISDKManager.getInstance().getMissionControl().getWaypointMissionOperator();

                DJIError error = waypointMissionOperator.loadMission(waypointMissionBuilder.build());
                if (error == null) {
                  toLog("loadWaypoint succeeded");
                } else {
                  toLog("loadWaypoint failed " + error.getDescription());
                }

                /// If startMission Not not Working try uploadWayPointMission first Before startMission
                waypointMissionOperator.uploadMission(uploadErr ->{
                  try {
                    if (!Utils.isNull(uploadErr)){
                      toLog("uploadErr Happen Detail: " + uploadErr.getDescription());
                      callback.onError("uploadErr Happen Detail: " + uploadErr.getDescription());
                      return;
                    }else {
                      // We Need to Wait else State Will Stay UPLOADING
                      toLog("uploading Successfully");
                      toLog("Now Wait  "+Constants.DELAY+" Sec to Transferts to Drone to be ok");

                      Utils.runAfterWait(activity,() ->{
                        try {
                          WaypointMissionState current = waypointMissionOperator.getCurrentState();
                          if (WaypointMissionState.DISCONNECTED.equals(current)) {
                            toLog("WaypointMissionState DISCONNECTED so Stop");
                            callback.onError("WaypointMissionState DISCONNECTED so Stop");
                            return;
                          }else if (WaypointMissionState.EXECUTING.equals(current)){
                            toLog("WaypointMissionState EXECUTING so Stop");
                            callback.onError("WaypointMissionState EXECUTING so Stop");
                            return;
                          }else if (WaypointMissionState.EXECUTION_PAUSED.equals(current)){
                            toLog("WaypointMissionState EXECUTING so Stop" );
                            callback.onError("WaypointMissionState EXECUTING so Stop");
                            return;
                          }else if (WaypointMissionState.NOT_SUPPORTED.equals(current)){
                            toLog("WaypointMissionState NOT_SUPPORTED so Stop" );
                            callback.onError("WaypointMissionState NOT_SUPPORTED so Stop");
                            return;
                          }else if (WaypointMissionState.READY_TO_UPLOAD.equals(current)){
                            toLog("WaypointMissionState READY_TO_UPLOAD so Stop cause already UPLOADED");
                            callback.onError("WaypointMissionState READY_TO_UPLOAD so Stop cause already UPLOADED");
                            return;
                          }else if (WaypointMissionState.RECOVERING.equals(current)){
                            toLog("WaypointMissionState RECOVERING so Stop ");
                            callback.onError("WaypointMissionState RECOVERING so Stop ");
                            return;
                          }else if (WaypointMissionState.UNKNOWN.equals(current)){
                            toLog("WaypointMissionState UNKNOWN so Stop ");
                            callback.onError("WaypointMissionState UNKNOWN so Stop ");
                            return;
                          }else if (WaypointMissionState.UPLOADING.equals(current)){
                            toLog("WaypointMissionState UPLOADING so Stop ");
                            callback.onError("WaypointMissionState UPLOADING so Stop ");
                            return;
                          }else if (WaypointMissionState.READY_TO_EXECUTE.equals(current)){
                            toLog("WaypointMissionState READY_TO_EXECUTE" );
                            waypointMissionOperator.startMission( startError ->{
                                      try {
                                        if (startError == null ){
                                          toLog("Mission Start: Successfully");
                                          callback.onSucces("");
                                        }else {
                                          toLog("Mission Start: Failed "+startError.getDescription());
                                          callback.onError("Mission Start: Failed "+startError.getDescription());
                                        }
                                      }catch (Exception e){
                                        Utils.toLog(TAG, "startMission",null,e);
                                        if (Utils.isNull(callback)){
                                          return;
                                        }else {
                                          callback.onError("Error");
                                        }
                                      }catch (Error er){
                                        Utils.toLog(TAG, "startMission",er,null);
                                        if (Utils.isNull(callback)){
                                          return;
                                        }else {
                                          callback.onError("Error");
                                        }
                                      }
                                    }
                            );
                          }
                        }catch (Exception e){
                          Utils.toLog(TAG, "runAfterWait",null,e);
                          if (Utils.isNull(callback)){
                            return;
                          }else {
                            callback.onError("Error");
                          }
                        }catch (Error er){
                          Utils.toLog(TAG, "runAfterWait",er,null);
                          if (Utils.isNull(callback)){
                            return;
                          }else {
                            callback.onError("Error");
                          }
                        }
                      });
                    }
                  }catch (Exception e){
                    Utils.toLog(TAG, "uploadMission",null,e);
                    if (Utils.isNull(callback)){
                      return;
                    }else {
                      callback.onError("Error");
                    }
                  }catch (Error er){
                    Utils.toLog(TAG, "uploadMission",er,null);
                    if (Utils.isNull(callback)){
                      return;
                    }else {
                      callback.onError("Error");
                    }
                  }
                });
              }

            }
          }
        }
      }catch (Exception e){
        Utils.toLog(TAG, "goTo70MetersWaypoint",null,e);
        if (Utils.isNull(callback)){
          return;
        }else {
          callback.onError("Error");
        }
      }catch (Error er){
        Utils.toLog(TAG, "goTo70MetersWaypoint",er,null);
        if (Utils.isNull(callback)){
          return;
        }else {
          callback.onError("Error");
        }
      }
    }
  }


  public static double fromSpeedDisplayUnit(double input)
  {
    return input / Utils.clone(MissionDJI.multiplierspeed);
  }
  public float getPhotoEveryDouble(double flySpeed,double spacing){
    double flyspeedms = fromSpeedDisplayUnit((double)flySpeed);
    double photoEvery = ((double)spacing / flyspeedms);
    toLog(" getPhotoEveryDouble: "+photoEvery);
    return (float) photoEvery;
  }

  public String getFlyTime(@NonNull WaypointMission.Builder builder) throws Exception,Error{
    //        double seconds = ((routetotal * 1000.0) / ((flyspeedms) * 0.8));
    //        reduce flying speed by 20 %
    //        lbl_flighttime.Text = secondsToNice(seconds);
    String empty = "";
    if (Utils.isNull(builder)){
      return empty;
    }else {
      double seconds = builder.calculateTotalTime();
      String flighttime = secondsToNice(seconds);
      if (Utils.isNull(flighttime)){
        flighttime = "";
      }
      return flighttime;
    }
  }

  public int getPicturesSize(@NonNull CopyOnWriteArrayList<PointLatLngAlt> grid)throws Exception,Error{
    if (Utils.isNull(grid)){
      return 0;
    }else {
      if (grid.size() == 0){
        return 0;
      }else{
        AtomicInteger images = new AtomicInteger();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
          grid.forEach(item ->{
            if (item.getTag().trim().contentEquals("M") )
            {
              images.getAndIncrement();
            }
          });
        }
        return images.get();

      }
    }


  }
  private String secondsToNice(double seconds)
  {
    if (seconds < 0)
      return "Infinity Seconds";

    double secs = seconds % 60;
    int mins = (int)(seconds / 60) % 60;
    int hours = (int)(seconds / 3600);// % 24;

    if (hours > 0)
    {
      return hours + ":" + String.format(Locale.FRANCE,"%02d", mins) + ":" + String.format(Locale.FRANCE,"%02f", secs)+ " Hours";
    }
    else if (mins > 0)
    {
      return mins + ":" + String.format(Locale.FRANCE,"%02f", secs) + " Minutes";
    }
    else
    {
      return String.format(Locale.FRANCE,"%02f", secs) + " Seconds";
    }
  }
}
