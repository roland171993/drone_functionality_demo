//package demoapp.dronecontroller;
//
//import android.app.Application;
//import android.content.Context;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import com.getcapacitor.JSObject;
//import com.getcapacitor.Plugin;
//import com.getcapacitor.PluginCall;
//import com.getcapacitor.PluginMethod;
//import com.getcapacitor.annotation.CapacitorPlugin;
//import com.jool.joolplane.dronecontroller.interfaces.OnAsyncGPSOperationComplete;
//import com.jool.joolplane.dronecontroller.interfaces.OnAsyncOperationComplete;
//import com.jool.joolplane.dronecontroller.interfaces.OnStateChangeListener;
//import com.jool.joolplane.dronecontroller.presenters.PermissionManager;
//import com.jool.joolplane.dronecontroller.utils.Constants;
//import com.jool.joolplane.dronecontroller.utils.Utils;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//@CapacitorPlugin(name = "DroneController")
//public class DroneControllerPlugin extends Plugin {
//  private  final String TAG = DroneControllerPlugin.this.getClass().getSimpleName();
//
//  private boolean hasSave = false;
//  private @Nullable Context ctx = null;
//  private @Nullable static Application mainApp= null;
//  private @Nullable String callBackIdConnect,callBackIdDisconnect,
//    callBackIdwatchPhone,callBackIdwatchDrone,callBackIdsetGoBackHome,
//    callBackIdDoTest,callBackIdstopWatchPhone,callBackIdstopWatchDrone,
//    callBackIdsEnableJoystick,callBackIdsDisableJoystick,callBackIdsReturnToHome,
//    callBackIdsLanding,callBackIdsTakeOff,callBackIdsSendCommand;
//
//  private @Nullable PluginCall currentBridge = null;
//
//  @Nullable
//  public PluginCall getCurrentBridge() throws NullPointerException {
//    return currentBridge;
//  }
//
//  public void setCurrentBridge(@Nullable PluginCall currentBridge) {
//    this.currentBridge = currentBridge;
//  }
//
//  @Nullable
//  public static Application getMainApp()throws NullPointerException {
//    return mainApp;
//  }
//
//  public static void setMainApp(@Nullable Application mainApp) {
//    DroneControllerPlugin.mainApp = mainApp;
//  }
//
//  @Subscribe(threadMode = ThreadMode.MAIN)
//  public void onMessageEvent(MessageEvent event) {
//    if (Utils.isNull(event)){
//      toLog(TAG, "onMessageEvent event is Null ");
//    }else {
//      JSObject ret = new JSObject();
//      ret.put("value", "some value");
//      if (event.getCode() == Constants.CODE_ON_BACK_PRESSED){
//        notifyListeners("backPressEvent", ret);
//        return;
//      }
//      if (event.getCode() == Constants.CODE_DRONE_LOW_BATTERY){
//        notifyListeners("droneLowBatteryEvent", ret);
//        return;
//      }
//    }
//  }
//
//  @Override
//  protected void handleOnStop() {
//    super.handleOnStop();
//    // Remove EventBus listener
//    EventBus.getDefault().unregister(this);
//  }
//
//  @Override
//  public void load() {
//    super.load();
//
//    // Register EventBus
//    EventBus.getDefault().register(this);
//    ctx = this.getContext();
//
//    PermissionManager pm = new PermissionManager(ctx);
//    if (!pm.isPermissionsGranted()){
//      toLog(TAG, "MISSING PERMISSION ");
//    }
//    if (!pm.isSDKreadyForMappingProcess()){
//      toLog(TAG, "SDK Not Ready for Mapping Process MIN IS API 24 ");
//    }
//
//  }
//
//  @PluginMethod
//  public void echo(PluginCall call) {
//    String value = call.getString("value");
//
//    JSObject ret = new JSObject();
//    ret.put("value", "implementation.echo(value) Examle");
//    call.resolve(ret);
//  }
//
//  @PluginMethod
//  public void ini(PluginCall call){
//    toLog(TAG, "ini start");
//    try {
//      if (Utils.isNull(ctx)){
//        toLog(TAG, "ini stop cause ctx is Null");
//        return;
//      }else {
//        if (Utils.isNull(call)){
//          toLog(TAG, "ini stop cause call is Null");
//          return;
//        }else {
//          PermissionManager pm = new PermissionManager(ctx);
//          if (!pm.isPermissionsGranted()){
//            toLog(TAG, "ini stop cause REQUIRE ALL PERMISSIONS");
//            call.reject("REQUIRE ALL PERMISSIONS");
//            return;
//          }else{
//            if (!pm.isSDKreadyForMappingProcess()){
//              toLog(TAG, "SDK Not Ready for Mapping Process MIN IS API 24 ");
//              call.reject("SDK Not Ready for Mapping Process MIN IS API 24");
//              return;
//            }else {
//              DroneManager manager = DroneManager.getInstance();
//              if (Utils.isNull(manager)){
//                toLog(TAG, "ini stop cause manager is Null");
//                call.reject("DroneManager is Null");
//                return;
//              }else {
//                if (Utils.isNull(getMainApp())){
//                  toLog(TAG, "ini stop cause getMainApp() is Null");
//                  call.reject("PLEASE SET MainApp BEFORE RUN Apps (setMainApp) ...MainApp IS NULL RIGHT NOW");
//                  return;
//                }else {
//                  manager.ini(ctx);
//                  call.resolve();
//                  toLog(TAG, "ini Launch Succes");
//                }
//              }
//            }
//          }
//        }
//
//      }
//    }catch (Exception e){
//      Utils.toLog(TAG, "ini",null,e);
//      call.reject(e.getLocalizedMessage(), e);
//    }catch (Error er){
//      Utils.toLog(TAG, "ini",er,null);
//      call.reject(er.getLocalizedMessage());
//    }
//
//  }
//
//  @PluginMethod
//  public void attachBaseContext(PluginCall call){
//    toLog(TAG, "attachBaseContext start");
//    try {
//      if (Utils.isNull(ctx)){
//        toLog(TAG, "attachBaseContext stop cause ctx is Null");
//        return;
//      }else {
//        if (Utils.isNull(call)){
//          toLog(TAG, "attachBaseContext stop cause call is Null");
//          return;
//        }else {
//          PermissionManager pm = new PermissionManager(ctx);
//          if (!pm.isPermissionsGranted()){
//            toLog(TAG, "attachBaseContext stop cause REQUIRE ALL PERMISSIONS");
//            call.reject("REQUIRE ALL PERMISSIONS");
//            return;
//          }else{
//            if (!pm.isSDKreadyForMappingProcess()){
//              toLog(TAG, "SDK Not Ready for Mapping Process MIN IS API 24 ");
//              call.reject("SDK Not Ready for Mapping Process MIN IS API 24");
//              return;
//            }else {
//              DroneManager manager = DroneManager.getInstance();
//              if (Utils.isNull(manager)){
//                toLog(TAG, "attachBaseContext stop cause manager is Null");
//                call.reject("DroneManager is Null");
//                return;
//              }else {
//                if (Utils.isNull(getMainApp())){
//                  toLog(TAG, "attachBaseContext stop cause getMainApp() is Null");
//                  call.reject("PLEASE SET MainApp BEFORE RUN Apps (setMainApp) ...MainApp IS NULL RIGHT NOW");
//                  return;
//                }else {
//                  manager.attachBaseContext(ctx,getMainApp());
//                  call.resolve();
//                  toLog(TAG, "attachBaseContext Success launch");
//                }
//              }
//            }
//          }
//        }
//      }
//    }catch (Exception e){
//      Utils.toLog(TAG, "attachBaseContext",null,e);
//      call.reject(e.getLocalizedMessage(), e);
//    }catch (Error er){
//      Utils.toLog(TAG, "attachBaseContext",er,null);
//      call.reject(er.getLocalizedMessage());
//    }
//  }
//
//  @PluginMethod
//  public void onCreate(PluginCall call){
//    toLog(TAG, "onCreate start");
//    try {
//      if (Utils.isNull(ctx)){
//        toLog(TAG, "onCreate stop cause ctx is Null");
//        return;
//      }else {
//        if (Utils.isNull(call)){
//          toLog(TAG, "onCreate stop cause call is Null");
//          return;
//        }else {
//          PermissionManager pm = new PermissionManager(ctx);
//          if (!pm.isPermissionsGranted()){
//            toLog(TAG, "onCreate stop cause REQUIRE ALL PERMISSIONS");
//            call.reject("REQUIRE ALL PERMISSIONS");
//            return;
//          }else{
//            if (!pm.isSDKreadyForMappingProcess()){
//              toLog(TAG, "SDK Not Ready for Mapping Process MIN IS API 24 ");
//              call.reject("SDK Not Ready for Mapping Process MIN IS API 24");
//              return;
//            }else {
//              DroneManager manager = DroneManager.getInstance();
//              if (Utils.isNull(manager)){
//                toLog(TAG, "onCreate stop cause manager is Null");
//                call.reject("DroneManager is Null");
//                return;
//              }else {
//                if (Utils.isNull(getMainApp())){
//                  toLog(TAG, "onCreate stop cause getMainApp() is Null");
//                  call.reject("PLEASE SET MainApp BEFORE RUN Apps (setMainApp) ...MainApp IS NULL RIGHT NOW");
//                  return;
//                }else {
//                  manager.onCreate();
//                  call.resolve();
//                  toLog(TAG, "onCreate Success launch");
//                }
//              }
//            }
//          }
//        }
//      }
//    }catch (Exception e){
//      Utils.toLog(TAG, "onCreate",null,e);
//      call.reject(e.getLocalizedMessage(), e);
//    }catch (Error er){
//      Utils.toLog(TAG, "onCreate",er,null);
//      call.reject(er.getLocalizedMessage());
//    }
//  }
//
//  @PluginMethod
//  public void connect(PluginCall call){
//    toLog(TAG, "connect start");
//    try {
//      if (Utils.isNull(ctx)){
//        toLog(TAG, "connect stop cause ctx is Null");
//        return;
//      }else {
//        if (Utils.isNull(call)){
//          toLog(TAG, "connect stop cause call is Null");
//          return;
//        }else {
//          PermissionManager pm = new PermissionManager(ctx);
//          if (!pm.isPermissionsGranted()){
//            toLog(TAG, "connect stop cause REQUIRE ALL PERMISSIONS");
//            call.reject("REQUIRE ALL PERMISSIONS");
//            return;
//          }else{
//            if (!pm.isSDKreadyForMappingProcess()){
//              toLog(TAG, "SDK Not Ready for Mapping Process MIN IS API 24 ");
//              call.reject("SDK Not Ready for Mapping Process MIN IS API 24");
//              return;
//            }else {
//              DroneManager manager = DroneManager.getInstance();
//              if (Utils.isNull(manager)){
//                toLog(TAG, "connect stop cause manager is Null");
//                call.reject("DroneManager is Null");
//                return;
//              }else {
//                if (Utils.isNull(getMainApp())){
//                  toLog(TAG, "connect stop cause getMainApp() is Null");
//                  call.reject("PLEASE SET MainApp BEFORE RUN Apps (setMainApp) ...MainApp IS NULL RIGHT NOW");
//                  return;
//                }else {
//                  // Keep At the End of function
//                  callBackIdConnect = null;
//                  call.save();
//                  callBackIdConnect = Utils.clone(call.getCallbackId());
//
//                  manager.connect(new OnAsyncOperationComplete() {
//                    @Override
//                    public void onError(@Nullable String errorDetail) {
//                      if (Utils.isNull(errorDetail)){
//                        errorDetail = "";
//                      }
//                      PluginCall call = bridge.getSavedCall(callBackIdConnect);
//                      if (call == null) {
//                        toLog(TAG , "connect onError Bridge Is Null ");
//                        return;
//                      }else {
//                        call.reject(errorDetail);
//                        call.release(bridge);
//                        callBackIdConnect = null;
//                      }
//
//
//                    }
//
//                    @Override
//                    public void onSucces(@Nullable String succesMsg) {
//                      // bridge.getSavedCall(id) if not working
//                      PluginCall call = bridge.getSavedCall(callBackIdConnect);
//                      if (call == null) {
//                        toLog(TAG , "connect onError Bridge Is Null ");
//                        return;
//                      }else {
//                        if (Utils.isNull(succesMsg)){
//                          succesMsg = "";
//                        }
//                        call.resolve();
//                        call.release(bridge);
//                        callBackIdConnect =null;
//                      }
//
//                    }
//                  });
//                  toLog(TAG, "connect Success launch");
//                }
//              }
//            }
//          }
//        }
//      }
//    }catch (Exception e){
//      Utils.toLog(TAG, "connect",null,e);
//      call.reject(e.getLocalizedMessage(), e);
//    }catch (Error er){
//      Utils.toLog(TAG, "connect",er,null);
//      call.reject(er.getLocalizedMessage());
//    }
//  }
//
//  @PluginMethod
//  public void disconnect(PluginCall call){
//    toLog(TAG, "disconnect start");
//    try {
//      if (Utils.isNull(ctx)){
//        toLog(TAG, "disconnect stop cause ctx is Null");
//        return;
//      }else {
//        if (Utils.isNull(call)){
//          toLog(TAG, "disconnect stop cause call is Null");
//          return;
//        }else {
//          PermissionManager pm = new PermissionManager(ctx);
//          if (!pm.isPermissionsGranted()){
//            toLog(TAG, "disconnect stop cause REQUIRE ALL PERMISSIONS");
//            call.reject("REQUIRE ALL PERMISSIONS");
//            return;
//          }else{
//            if (!pm.isSDKreadyForMappingProcess()){
//              toLog(TAG, "SDK Not Ready for Mapping Process MIN IS API 24 ");
//              call.reject("SDK Not Ready for Mapping Process MIN IS API 24");
//              return;
//            }else {
//              DroneManager manager = DroneManager.getInstance();
//              if (Utils.isNull(manager)){
//                toLog(TAG, "disconnect stop cause manager is Null");
//                call.reject("DroneManager is Null");
//                return;
//              }else {
//                if (Utils.isNull(getMainApp())){
//                  toLog(TAG, "disconnect stop cause getMainApp() is Null");
//                  call.reject("PLEASE SET MainApp BEFORE RUN Apps (setMainApp) ...MainApp IS NULL RIGHT NOW");
//                  return;
//                }else {
//                  // Keep At the End of function
//                  callBackIdDisconnect = null;
//                  call.save();
//                  callBackIdDisconnect = Utils.clone(call.getCallbackId());
//
//                  manager.disconnect(new OnAsyncOperationComplete() {
//                    @Override
//                    public void onError(@Nullable String errorDetail) {
//                      PluginCall call = bridge.getSavedCall(callBackIdDisconnect);
//                      if (call == null) {
//                        toLog(TAG , "disconnect onError Bridge Is Null ");
//                        return;
//                      }else {
//                        if (Utils.isNull(errorDetail)){
//                          errorDetail = "";
//                        }
//                        call.reject(errorDetail);
//                        call.release(bridge);
//                        callBackIdDisconnect = null;
//                      }
//
//                    }
//
//                    @Override
//                    public void onSucces(@Nullable String succesMsg) {
//                      PluginCall call = bridge.getSavedCall(callBackIdDisconnect);
//                      if (call == null) {
//                        toLog(TAG , "disconnect onSucces Bridge Is Null ");
//                        return;
//                      }else {
//                        if (Utils.isNull(succesMsg)){
//                          succesMsg = "";
//                        }
//                        call.resolve();
//                        call.release(bridge);
//                        callBackIdDisconnect = null;
//                      }
//
//                    }
//                  });
//                  toLog(TAG, "disconnect Success launch");
//                }
//              }
//            }
//          }
//        }
//      }
//    }catch (Exception e){
//      Utils.toLog(TAG, "disconnect",null,e);
//      call.reject(e.getLocalizedMessage(), e);
//    }catch (Error er){
//      Utils.toLog(TAG, "disconnect",er,null);
//      call.reject(er.getLocalizedMessage());
//    }
//  }
//
//  @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
//  public void watchPhonePosition(PluginCall call){
//    toLog(TAG, "watchPhonePosition start");
//    try {
//      if (Utils.isNull(ctx)){
//        toLog(TAG, "watchPhonePosition stop cause ctx is Null");
//        return;
//      }else {
//        if (Utils.isNull(call)){
//          toLog(TAG, "watchPhonePosition stop cause call is Null");
//          return;
//        }else {
//          PermissionManager pm = new PermissionManager(ctx);
//          if (!pm.isPermissionsGranted()){
//            toLog(TAG, "watchPhonePosition stop cause REQUIRE ALL PERMISSIONS");
//            call.reject("REQUIRE ALL PERMISSIONS");
//            return;
//          }else{
//            if (!pm.isSDKreadyForMappingProcess()){
//              toLog(TAG, "SDK Not Ready for Mapping Process MIN IS API 24 ");
//              call.reject("SDK Not Ready for Mapping Process MIN IS API 24");
//              return;
//            }else {
//              DroneManager manager = DroneManager.getInstance();
//              if (Utils.isNull(manager)){
//                toLog(TAG, "watchPhonePosition stop cause manager is Null");
//                call.reject("DroneManager is Null");
//                return;
//              }else {
//                if (Utils.isNull(getMainApp())){
//                  toLog(TAG, "watchPhonePosition stop cause getMainApp() is Null");
//                  call.reject("PLEASE SET MainApp BEFORE RUN Apps (setMainApp) ...MainApp IS NULL RIGHT NOW");
//                  return;
//                }else {
//                  // Keep At the End of function
//                  callBackIdwatchPhone = null;
//                  call.save();
//                  callBackIdwatchPhone = Utils.clone(call.getCallbackId());
//
//                  manager.registerToListenPhoneLocation(new OnAsyncGPSOperationComplete() {
//                    @Override
//                    public void onError(@Nullable  String errorDetail) {
//                      PluginCall call = bridge.getSavedCall(callBackIdwatchPhone);
//                      if (call == null) {
//                        toLog(TAG , "watchPhonePosition onError Bridge Is Null ");
//                        return;
//                      }else {
//                        if (Utils.isNull(errorDetail)){
//                          errorDetail = "";
//                        }
//                        call.reject(errorDetail);
//                        toLog(TAG, "watchPhonePosition onConnectFailed");
//                        // callBackIdwatchPhone =null; require For other callBack
//                      }
//
//                    }
//
//                    @Override
//                    public void onLocationChanged(@Nullable Double uLatitude, @Nullable Double uLongitude) {
//                      synchronized (DroneControllerPlugin.this){
//                        PluginCall call = bridge.getSavedCall(callBackIdwatchPhone);
//                        if (call == null) {
//                          toLog(TAG , "watchPhonePosition onLocationChanged Bridge Is Null ");
//                          return;
//                        }else {
//                          if (Utils.isNull(uLatitude) || Utils.isNull(uLongitude)){
//                            toLog("watchPhonePosition","onUserPositionChanged uLatitude or uLongitude is Null");
//                            return;
//                          }else {
//                            if (uLatitude == Constants.DOUBLE_NULL || uLongitude == Constants.DOUBLE_NULL){
//                              toLog("watchPhonePosition","onUserPositionChanged BAD POSITION uLatitude or uLongitude DOUBLE_NULL");
//                              return;
//                            }else {
//                              JSObject ret = new JSObject();
//                              ret.put("latitude", uLatitude);
//                              ret.put("longitude", uLongitude);
//                              call.success(ret);
//                              // toLog(TAG, "watchPhonePosition onLocationChanged uLatitude:"+uLatitude+" uLongitude:"+uLongitude);
//                              // callBackIdwatchPhone =null; require For other callBack
//                            }
//                          }
//                        }
//                      }
//                    }
//                  });
//                  toLog(TAG, "watchPhonePosition Success launch");
//                }
//              }
//            }
//          }
//        }
//      }
//    }catch (Exception e){
//      Utils.toLog(TAG, "watchPhonePosition",null,e);
//      call.reject(e.getLocalizedMessage(), e);
//    }catch (Error er){
//      Utils.toLog(TAG, "watchPhonePosition",er,null);
//      call.reject(er.getLocalizedMessage());
//    }
//  }
//
//  @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
//  public void watchDroneTelemetry(PluginCall call){
//    toLog(TAG, "watchDroneTelemetry start");
//    try {
//      if (Utils.isNull(ctx)){
//        toLog(TAG, "watchDroneTelemetry stop cause ctx is Null");
//        return;
//      }else {
//        if (Utils.isNull(call)){
//          toLog(TAG, "watchDroneTelemetry stop cause call is Null");
//          return;
//        }else {
//          PermissionManager pm = new PermissionManager(ctx);
//          if (!pm.isPermissionsGranted()){
//            toLog(TAG, "watchDroneTelemetry stop cause REQUIRE ALL PERMISSIONS");
//            call.reject("REQUIRE ALL PERMISSIONS");
//            return;
//          }else{
//            if (!pm.isSDKreadyForMappingProcess()){
//              toLog(TAG, "SDK Not Ready for Mapping Process MIN IS API 24 ");
//              call.reject("SDK Not Ready for Mapping Process MIN IS API 24");
//              return;
//            }else {
//              DroneManager manager = DroneManager.getInstance();
//              if (Utils.isNull(manager)){
//                toLog(TAG, "watchDroneTelemetry stop cause manager is Null");
//                call.reject("DroneManager is Null");
//                return;
//              }else {
//                if (Utils.isNull(getMainApp())){
//                  toLog(TAG, "watchDroneTelemetry stop cause getMainApp() is Null");
//                  call.reject("PLEASE SET MainApp BEFORE RUN Apps (setMainApp) ...MainApp IS NULL RIGHT NOW");
//                  return;
//                }else {
//                  // Keep At the End of function
//                  callBackIdwatchDrone = null;
//                  call.save();
//                  callBackIdwatchDrone = Utils.clone(call.getCallbackId());
//
//                  manager.registerToListenDroneState(new OnStateChangeListener() {
//                    @Override
//                    public void onChange(int uplinkSignalQuality, short satelliteCount, int chargeRemainingInPercent, int chargeRemainingInMAh, float temperature, float altitudeInMeters, float speedInMeterPerSec, float heading, int numberOfDischarges, int lifetimeRemainingInPercent, double longitude, double latitude, float altitude, int droneHeadingInDegrees, @NonNull String droneModelName) {
//                      synchronized (DroneControllerPlugin.this){
//                        PluginCall call = bridge.getSavedCall(callBackIdwatchDrone);
//                        if (call == null) {
//                          toLog(TAG , "watchDroneTelemetry onChange Bridge Is Null ");
//                          return;
//                        }else {
//                          if (Utils.isNull(droneModelName)){
//                            droneModelName = "";
//                          }
//                          Double mLatitude =latitude;
//                          Double mLongitude =longitude;
//                          if (mLatitude.isNaN()){
//                            mLatitude = Constants.DOUBLE_NULL_2;
//                          }
//                          if (mLongitude.isNaN()){
//                            mLongitude = Constants.DOUBLE_NULL_2;
//                          }
//                          JSObject ret = new JSObject();
//                          ret.put("uplinkSignalQuality", uplinkSignalQuality);
//                          ret.put("satelliteCount", satelliteCount);
//                          ret.put("chargeRemainingInPercent",chargeRemainingInPercent );
//                          ret.put("chargeRemainingInMAh", chargeRemainingInMAh);
//                          ret.put("temperature", temperature);
//                          ret.put("altitudeInMeters", altitudeInMeters);
//                          ret.put("speedInMeterPerSec", speedInMeterPerSec);
//                          ret.put("heading", heading);
//                          ret.put("numberOfDischarges", numberOfDischarges);
//                          ret.put("lifetimeRemainingInPercent", lifetimeRemainingInPercent);
//                          ret.put("longitude", mLongitude);
//                          ret.put("latitude", mLatitude);
//                          ret.put("altitude", altitude);
//                          ret.put("droneHeadingInDegrees", droneHeadingInDegrees);
//                          ret.put("droneModelName", droneModelName);
//                          call.success(ret);
//                          toLog(TAG, "onChange uplinkSignalQuality:"+uplinkSignalQuality+" satelliteCount:"+satelliteCount+" chargeRemainingInPercent:"+chargeRemainingInPercent
//                                  +" chargeRemainingInMAh:"+chargeRemainingInMAh+" temperature:"+temperature+" altitudeInMeters:"+altitudeInMeters+" speedInMeterPerSec"+speedInMeterPerSec
//                                  +" heading:"+heading+" numberOfDischarges:"+numberOfDischarges+" lifetimeRemainingInPercent:"+lifetimeRemainingInPercent+" longitude:"+longitude+
//                                  " latitude:"+latitude+" altitude:"+altitude+" droneHeadingInDegrees:"+droneHeadingInDegrees+ " droneModelName: "+droneModelName);
//                          // callBackIdwatchDrone =null; require For other callBack
//                        }
//                      }
//                    }
//
//                    @Override
//                    public void onLog(String msg) {
//                      toLog(TAG, msg);
//                    }
//
//                    @Override
//                    public void onConnectFailed() {
//                      PluginCall call = bridge.getSavedCall(callBackIdwatchDrone);
//                      if (call == null) {
//                        toLog(TAG , "watchDroneTelemetry onConnectFailed Bridge Is Null ");
//                        return;
//                      }else {
//                        call.resolve();
//                        toLog(TAG, "registerToListenDroneState onConnectFailed");
//                        // callBackIdwatchDrone =null; require For other callBack
//                      }
//                    }
//
//                    @Override
//                    public void onConnectSucces() {
//                      PluginCall call = bridge.getSavedCall(callBackIdwatchDrone);
//                      if (call == null) {
//                        toLog(TAG , "watchDroneTelemetry onConnectSucces Bridge Is Null ");
//                        return;
//                      }else {
//                        call.resolve();
//                        toLog(TAG, "registerToListenDroneState onConnectSucces");
//                        // callBackIdwatchDrone =null; require For other callBack
//                      }
//                    }
//                  });
//                  toLog(TAG, "registerToListenDroneState Success launch");
//                }
//              }
//            }
//          }
//        }
//      }
//    }catch (Exception e){
//      Utils.toLog(TAG, "watchDroneTelemetry",null,e);
//      call.reject(e.getLocalizedMessage(), e);
//    }catch (Error er){
//      Utils.toLog(TAG, "watchDroneTelemetry",er,null);
//      call.reject(er.getLocalizedMessage());
//    }
//  }
//
//  @PluginMethod
//  public void setGoBackHomePosition(PluginCall call){
//    toLog(TAG, "setGoBackHomePosition start");
//    try {
//      if (Utils.isNull(ctx)){
//        toLog(TAG, "setGoBackHomePosition stop cause ctx is Null");
//        return;
//      }else {
//        if (Utils.isNull(call)){
//          toLog(TAG, "setGoBackHomePosition stop cause call is Null");
//          return;
//        }else {
//          PermissionManager pm = new PermissionManager(ctx);
//          if (!pm.isPermissionsGranted()){
//            toLog(TAG, "setGoBackHomePosition stop cause REQUIRE ALL PERMISSIONS");
//            call.reject("REQUIRE ALL PERMISSIONS");
//            return;
//          }else{
//            if (!pm.isSDKreadyForMappingProcess()){
//              toLog(TAG, "SDK Not Ready for Mapping Process MIN IS API 24 ");
//              call.reject("SDK Not Ready for Mapping Process MIN IS API 24");
//              return;
//            }else {
//              DroneManager manager = DroneManager.getInstance();
//              if (Utils.isNull(manager)){
//                toLog(TAG, "setGoBackHomePosition stop cause manager is Null");
//                call.reject("DroneManager is Null");
//                return;
//              }else {
//                if (Utils.isNull(getMainApp())){
//                  toLog(TAG, "setGoBackHomePosition stop cause getMainApp() is Null");
//                  call.reject("PLEASE SET MainApp BEFORE RUN Apps (setMainApp) ...MainApp IS NULL RIGHT NOW");
//                  return;
//                }else {
//                  // Keep At the End of function
//                  callBackIdsetGoBackHome = null;
//                  call.save();
//                  callBackIdsetGoBackHome = Utils.clone(call.getCallbackId());
//
//                  manager.setGoBackHomePosition(new OnAsyncOperationComplete() {
//                    @Override
//                    public void onError(@Nullable String errorDetail) {
//                      PluginCall call = bridge.getSavedCall(callBackIdsetGoBackHome);
//                      if (call == null) {
//                        toLog(TAG , "setGoBackHomePosition onError Bridge Is Null ");
//                        return;
//                      }else {
//                        if (Utils.isNull(errorDetail)){
//                          errorDetail = "";
//                        }
//                        call.reject(errorDetail);
//                        call.release(bridge);
//                        callBackIdsetGoBackHome = null;
//                      }
//
//                    }
//
//                    @Override
//                    public void onSucces(@Nullable String succesMsg) {
//                      PluginCall call = bridge.getSavedCall(callBackIdsetGoBackHome);
//                      if (call == null) {
//                        toLog(TAG , "setGoBackHomePosition onSucces Bridge Is Null ");
//                        return;
//                      }else {
//                        if (Utils.isNull(succesMsg)){
//                          succesMsg = "";
//                        }
//                        call.resolve();
//                        call.release(bridge);
//                        callBackIdsetGoBackHome = null;
//                      }
//
//                    }
//                  });
//                  toLog(TAG, "setGoBackHomePosition Success launch");
//                }
//              }
//            }
//          }
//        }
//      }
//    }catch (Exception e){
//      Utils.toLog(TAG, "setGoBackHomePosition",null,e);
//      call.reject(e.getLocalizedMessage(), e);
//    }catch (Error er){
//      Utils.toLog(TAG, "setGoBackHomePosition",er,null);
//      call.reject(er.getLocalizedMessage());
//    }
//  }
//
//  @PluginMethod
//  public void stopWatchDroneTelemetry(PluginCall call){
//    toLog(TAG, "stopWatchDroneTelemetry start");
//    try {
//      if (Utils.isNull(ctx)){
//        toLog(TAG, "stopWatchDroneTelemetry stop cause ctx is Null");
//        return;
//      }else {
//        if (Utils.isNull(call)){
//          toLog(TAG, "stopWatchDroneTelemetry stop cause call is Null");
//          return;
//        }else {
//          PermissionManager pm = new PermissionManager(ctx);
//          if (!pm.isPermissionsGranted()){
//            toLog(TAG, "stopWatchDroneTelemetry stop cause REQUIRE ALL PERMISSIONS");
//            call.reject("REQUIRE ALL PERMISSIONS");
//            return;
//          }else{
//            if (!pm.isSDKreadyForMappingProcess()){
//              toLog(TAG, "SDK Not Ready for Mapping Process MIN IS API 24 ");
//              call.reject("SDK Not Ready for Mapping Process MIN IS API 24");
//              return;
//            }else {
//              DroneManager manager = DroneManager.getInstance();
//              if (Utils.isNull(manager)){
//                toLog(TAG, "stopWatchDroneTelemetry stop cause manager is Null");
//                call.reject("DroneManager is Null");
//                return;
//              }else {
//                if (Utils.isNull(getMainApp())){
//                  toLog(TAG, "stopWatchDroneTelemetry stop cause getMainApp() is Null");
//                  call.reject("PLEASE SET MainApp BEFORE RUN Apps (setMainApp) ...MainApp IS NULL RIGHT NOW");
//                  return;
//                }else {
//                  // Keep At the End of function
//                  callBackIdstopWatchDrone = null;
//                  call.save();
//                  callBackIdstopWatchDrone = Utils.clone(call.getCallbackId());
//
//                  manager.stopWatchDroneTelemetry(new OnAsyncOperationComplete() {
//                    @Override
//                    public void onError(@Nullable String errorDetail) {
//                      PluginCall call = bridge.getSavedCall(callBackIdstopWatchDrone);
//                      if (call == null) {
//                        toLog(TAG , "stopWatchDroneTelemetry onError Bridge Is Null ");
//                        return;
//                      }else {
//                        if (Utils.isNull(errorDetail)){
//                          errorDetail = "";
//                        }
//                        call.reject(errorDetail);
//                        call.release(bridge);
//                        callBackIdstopWatchDrone = null;
//                      }
//
//                    }
//
//                    @Override
//                    public void onSucces(@Nullable String succesMsg) {
//                      PluginCall call = bridge.getSavedCall(callBackIdstopWatchDrone);
//                      if (call == null) {
//                        toLog(TAG , "stopWatchDroneTelemetry onSucces Bridge Is Null ");
//                        return;
//                      }else {
//                        if (Utils.isNull(succesMsg)){
//                          succesMsg = "";
//                        }
//                        call.resolve();
//                        call.release(bridge);
//                        callBackIdstopWatchDrone = null;
//                      }
//
//                    }
//                  });
//                  toLog(TAG, "stopWatchDroneTelemetry Success launch");
//                }
//              }
//            }
//          }
//        }
//      }
//    }catch (Exception e){
//      Utils.toLog(TAG, "stopWatchDroneTelemetry",null,e);
//      call.reject(e.getLocalizedMessage(), e);
//    }catch (Error er){
//      Utils.toLog(TAG, "stopWatchDroneTelemetry",er,null);
//      call.reject(er.getLocalizedMessage());
//    }
//  }
//
//  @PluginMethod
//  public void stopWatchPhonePosition(PluginCall call){
//    toLog(TAG, "stopWatchPosition start");
//    try {
//      if (Utils.isNull(ctx)){
//        toLog(TAG, "stopWatchPosition stop cause ctx is Null");
//        return;
//      }else {
//        if (Utils.isNull(call)){
//          toLog(TAG, "stopWatchPosition stop cause call is Null");
//          return;
//        }else {
//          PermissionManager pm = new PermissionManager(ctx);
//          if (!pm.isPermissionsGranted()){
//            toLog(TAG, "stopWatchPosition stop cause REQUIRE ALL PERMISSIONS");
//            call.reject("REQUIRE ALL PERMISSIONS");
//            return;
//          }else{
//            if (!pm.isSDKreadyForMappingProcess()){
//              toLog(TAG, "SDK Not Ready for Mapping Process MIN IS API 24 ");
//              call.reject("SDK Not Ready for Mapping Process MIN IS API 24");
//              return;
//            }else {
//              DroneManager manager = DroneManager.getInstance();
//              if (Utils.isNull(manager)){
//                toLog(TAG, "stopWatchPosition stop cause manager is Null");
//                call.reject("DroneManager is Null");
//                return;
//              }else {
//                if (Utils.isNull(getMainApp())){
//                  toLog(TAG, "stopWatchPosition stop cause getMainApp() is Null");
//                  call.reject("PLEASE SET MainApp BEFORE RUN Apps (setMainApp) ...MainApp IS NULL RIGHT NOW");
//                  return;
//                }else {
//                  // Keep At the End of function
//                  callBackIdstopWatchPhone = null;
//                  call.save();
//                  callBackIdstopWatchPhone = Utils.clone(call.getCallbackId());
//
//                  manager.stopWatchPhoneLocation(new OnAsyncOperationComplete() {
//                    @Override
//                    public void onError(@Nullable String errorDetail) {
//                      PluginCall call = bridge.getSavedCall(callBackIdstopWatchPhone);
//                      if (call == null) {
//                        toLog(TAG , "stopWatchPosition onError Bridge Is Null ");
//                        return;
//                      }else {
//                        if (Utils.isNull(errorDetail)){
//                          errorDetail = "";
//                        }
//                        call.reject(errorDetail);
//                        call.release(bridge);
//                        callBackIdstopWatchPhone = null;
//                      }
//
//                    }
//
//                    @Override
//                    public void onSucces(@Nullable String succesMsg) {
//                      PluginCall call = bridge.getSavedCall(callBackIdstopWatchPhone);
//                      if (call == null) {
//                        toLog(TAG , "stopWatchPosition onSucces Bridge Is Null ");
//                        return;
//                      }else {
//                        if (Utils.isNull(succesMsg)){
//                          succesMsg = "";
//                        }
//                        call.resolve();
//                        call.release(bridge);
//                        callBackIdstopWatchPhone = null;
//                      }
//
//                    }
//                  });
//                  toLog(TAG, "stopWatchPosition Success launch");
//                }
//              }
//            }
//          }
//        }
//      }
//    }catch (Exception e){
//      Utils.toLog(TAG, "stopWatchPosition",null,e);
//      call.reject(e.getLocalizedMessage(), e);
//    }catch (Error er){
//      Utils.toLog(TAG, "stopWatchPosition",er,null);
//      call.reject(er.getLocalizedMessage());
//    }
//  }
//
//  @PluginMethod
//  public void doTest(PluginCall call){
//    toLog(TAG, "doTest start");
//    try {
//      if (Utils.isNull(ctx)){
//        toLog(TAG, "doTest stop cause ctx is Null");
//        return;
//      }else {
//        if (Utils.isNull(call)){
//          toLog(TAG, "doTest stop cause call is Null");
//          return;
//        }else {
//          PermissionManager pm = new PermissionManager(ctx);
//          if (!pm.isPermissionsGranted()){
//            toLog(TAG, "doTest stop cause REQUIRE ALL PERMISSIONS");
//            call.reject("REQUIRE ALL PERMISSIONS");
//            return;
//          }else{
//            if (!pm.isSDKreadyForMappingProcess()){
//              toLog(TAG, "SDK Not Ready for Mapping Process MIN IS API 24 ");
//              call.reject("SDK Not Ready for Mapping Process MIN IS API 24");
//              return;
//            }else {
//              DroneManager manager = DroneManager.getInstance();
//              if (Utils.isNull(manager)){
//                toLog(TAG, "doTest stop cause manager is Null");
//                call.reject("DroneManager is Null");
//                return;
//              }else {
//                if (Utils.isNull(getMainApp())){
//                  toLog(TAG, "doTest stop cause getMainApp() is Null");
//                  call.reject("PLEASE SET MainApp BEFORE RUN Apps (setMainApp) ...MainApp IS NULL RIGHT NOW");
//                  return;
//                }else {
//                  // Keep At the End of function
//                  callBackIdDoTest = null;
//                  call.save();
//                  callBackIdDoTest = Utils.clone(call.getCallbackId());
//
//                  manager.doTest(this.getActivity(),new OnAsyncOperationComplete() {
//                    @Override
//                    public void onError(@Nullable String errorDetail) {
//                      PluginCall call = bridge.getSavedCall(callBackIdDoTest);
//                      if (call == null) {
//                        toLog(TAG , "doTest onError Bridge Is Null ");
//                        return;
//                      }else {
//                        if (Utils.isNull(errorDetail)){
//                          errorDetail = "";
//                        }
//                        call.reject(errorDetail);
//                        call.release(bridge);
//                        callBackIdDoTest = null;
//                      }
//                    }
//
//                    @Override
//                    public void onSucces(@Nullable String succesMsg) {
//                      PluginCall call = bridge.getSavedCall(callBackIdDoTest);
//                      if (call == null) {
//                        toLog(TAG , "doTest onSucces Bridge Is Null ");
//                        return;
//                      }else {
//                        if (Utils.isNull(succesMsg)){
//                          succesMsg = "";
//                        }
//                        call.resolve();
//                        call.release(bridge);
//                        callBackIdDoTest = null;
//                      }
//
//                    }
//                  });
//                  toLog(TAG, "doTest Success launch");
//                }
//              }
//            }
//          }
//        }
//      }
//    }catch (Exception e){
//      Utils.toLog(TAG, "doTest",null,e);
//      call.reject(e.getLocalizedMessage(), e);
//    }catch (Error er){
//      Utils.toLog(TAG, "doTest",er,null);
//      call.reject(er.getLocalizedMessage());
//    }
//  }
//
//
//  @PluginMethod
//  public void enableJoystick(PluginCall call){
//    toLog(TAG, "enableJoystick start");
//    try {
//      if (Utils.isNull(ctx)){
//        toLog(TAG, "enableJoystick stop cause ctx is Null");
//        return;
//      }else {
//        if (Utils.isNull(call)){
//          toLog(TAG, "enableJoystick stop cause call is Null");
//          return;
//        }else {
//          PermissionManager pm = new PermissionManager(ctx);
//          if (!pm.isPermissionsGranted()){
//            toLog(TAG, "enableJoystick stop cause REQUIRE ALL PERMISSIONS");
//            call.reject("REQUIRE ALL PERMISSIONS");
//            return;
//          }else{
//            if (!pm.isSDKreadyForMappingProcess()){
//              toLog(TAG, "SDK Not Ready for Mapping Process MIN IS API 24 ");
//              call.reject("SDK Not Ready for Mapping Process MIN IS API 24");
//              return;
//            }else {
//              DroneManager manager = DroneManager.getInstance();
//              if (Utils.isNull(manager)){
//                toLog(TAG, "enableJoystick stop cause manager is Null");
//                call.reject("DroneManager is Null");
//                return;
//              }else {
//                if (Utils.isNull(getMainApp())){
//                  toLog(TAG, "enableJoystick stop cause getMainApp() is Null");
//                  call.reject("PLEASE SET MainApp BEFORE RUN Apps (setMainApp) ...MainApp IS NULL RIGHT NOW");
//                  return;
//                }else {
//                  // Keep At the End of function
//                  callBackIdsEnableJoystick= null;
//                  call.save();
//                  callBackIdsEnableJoystick = Utils.clone(call.getCallbackId());
//
//                  manager.enableJoystick(new OnAsyncOperationComplete() {
//                    @Override
//                    public void onError(@Nullable String errorDetail) {
//                      PluginCall call = bridge.getSavedCall(callBackIdsEnableJoystick);
//                      if (call == null) {
//                        toLog(TAG , "enableJoystick onError Bridge Is Null ");
//                        return;
//                      }else {
//                        if (Utils.isNull(errorDetail)){
//                          errorDetail = "";
//                        }
//                        call.reject(errorDetail);
//                        call.release(bridge);
//                        callBackIdsEnableJoystick = null;
//                      }
//
//                    }
//
//                    @Override
//                    public void onSucces(@Nullable String succesMsg) {
//                      PluginCall call = bridge.getSavedCall(callBackIdsEnableJoystick);
//                      if (call == null) {
//                        toLog(TAG , "enableJoystick onSucces Bridge Is Null ");
//                        return;
//                      }else {
//                        if (Utils.isNull(succesMsg)){
//                          succesMsg = "";
//                        }
//                        call.resolve();
//                        call.release(bridge);
//                        callBackIdsEnableJoystick = null;
//                      }
//
//                    }
//                  });
//                  toLog(TAG, "enableJoystick Success launch");
//                }
//              }
//            }
//          }
//        }
//      }
//    }catch (Exception e){
//      Utils.toLog(TAG, "enableJoystick",null,e);
//      call.reject(e.getLocalizedMessage(), e);
//    }catch (Error er){
//      Utils.toLog(TAG, "enableJoystick",er,null);
//      call.reject(er.getLocalizedMessage());
//    }
//  }
//
//  @PluginMethod
//  public void disableJoystick(PluginCall call){
//    toLog(TAG, "disableJoystick start");
//    try {
//      if (Utils.isNull(ctx)){
//        toLog(TAG, "disableJoystick stop cause ctx is Null");
//        return;
//      }else {
//        if (Utils.isNull(call)){
//          toLog(TAG, "disableJoystick stop cause call is Null");
//          return;
//        }else {
//          PermissionManager pm = new PermissionManager(ctx);
//          if (!pm.isPermissionsGranted()){
//            toLog(TAG, "disableJoystick stop cause REQUIRE ALL PERMISSIONS");
//            call.reject("REQUIRE ALL PERMISSIONS");
//            return;
//          }else{
//            if (!pm.isSDKreadyForMappingProcess()){
//              toLog(TAG, "SDK Not Ready for Mapping Process MIN IS API 24 ");
//              call.reject("SDK Not Ready for Mapping Process MIN IS API 24");
//              return;
//            }else {
//              DroneManager manager = DroneManager.getInstance();
//              if (Utils.isNull(manager)){
//                toLog(TAG, "disableJoystick stop cause manager is Null");
//                call.reject("DroneManager is Null");
//                return;
//              }else {
//                if (Utils.isNull(getMainApp())){
//                  toLog(TAG, "disableJoystick stop cause getMainApp() is Null");
//                  call.reject("PLEASE SET MainApp BEFORE RUN Apps (setMainApp) ...MainApp IS NULL RIGHT NOW");
//                  return;
//                }else {
//                  // Keep At the End of function
//                  callBackIdsDisableJoystick= null;
//                  call.save();
//                  callBackIdsDisableJoystick = Utils.clone(call.getCallbackId());
//
//                  manager.disableJoystick(new OnAsyncOperationComplete() {
//                    @Override
//                    public void onError(@Nullable String errorDetail) {
//                      PluginCall call = bridge.getSavedCall(callBackIdsDisableJoystick);
//                      if (call == null) {
//                        toLog(TAG , "disableJoystick onError Bridge Is Null ");
//                        return;
//                      }else {
//                        if (Utils.isNull(errorDetail)){
//                          errorDetail = "";
//                        }
//                        call.reject(errorDetail);
//                        call.release(bridge);
//                        callBackIdsDisableJoystick = null;
//                      }
//
//                    }
//
//                    @Override
//                    public void onSucces(@Nullable String succesMsg) {
//                      PluginCall call = bridge.getSavedCall(callBackIdsDisableJoystick);
//                      if (call == null) {
//                        toLog(TAG , "disableJoystick onSucces Bridge Is Null ");
//                        return;
//                      }else {
//                        if (Utils.isNull(succesMsg)){
//                          succesMsg = "";
//                        }
//                        call.resolve();
//                        call.release(bridge);
//                        callBackIdsDisableJoystick = null;
//                      }
//
//                    }
//                  });
//                  toLog(TAG, "disableJoystick Success launch");
//                }
//              }
//            }
//          }
//        }
//      }
//    }catch (Exception e){
//      Utils.toLog(TAG, "disableJoystick",null,e);
//      call.reject(e.getLocalizedMessage(), e);
//    }catch (Error er){
//      Utils.toLog(TAG, "disableJoystick",er,null);
//      call.reject(er.getLocalizedMessage());
//    }
//  }
//
//  @PluginMethod
//  public void sendJoystickCommand(PluginCall call){
//    toLog(TAG, "sendJoystickCommand start");
//    try {
//      if (Utils.isNull(ctx)){
//        toLog(TAG, "sendJoystickCommand stop cause ctx is Null");
//        return;
//      }else {
//        if (Utils.isNull(call)){
//          toLog(TAG, "sendJoystickCommand stop cause call is Null");
//          return;
//        }else {
//          PermissionManager pm = new PermissionManager(ctx);
//          if (!pm.isPermissionsGranted()){
//            toLog(TAG, "sendJoystickCommand stop cause REQUIRE ALL PERMISSIONS");
//            call.reject("REQUIRE ALL PERMISSIONS");
//            return;
//          }else{
//            if (!pm.isSDKreadyForMappingProcess()){
//              toLog(TAG, "SDK Not Ready for Mapping Process MIN IS API 24 ");
//              call.reject("SDK Not Ready for Mapping Process MIN IS API 24");
//              return;
//            }else {
//              DroneManager manager = DroneManager.getInstance();
//              if (Utils.isNull(manager)){
//                toLog(TAG, "sendJoystickCommand stop cause manager is Null");
//                call.reject("DroneManager is Null");
//                return;
//              }else {
//                if (Utils.isNull(getMainApp())){
//                  toLog(TAG, "sendJoystickCommand stop cause getMainApp() is Null");
//                  call.reject("PLEASE SET MainApp BEFORE RUN Apps (setMainApp) ...MainApp IS NULL RIGHT NOW");
//                  return;
//                }else {
//                  if ((!call.getData().has("leftY")) ||
//                          (!call.getData().has("leftX")) ||
//                          (!call.getData().has("rightY")) ||
//                          (!call.getData().has("rightX")) ) {
//                    toLog(TAG, " sendJoystickCommand Must provide an interval ");
//
//                    call.reject("Must provide : leftY, leftX, rightY, rightX");
//                    return;
//                  }else {
//                    Float leftY = call.getFloat("leftY");
//                    Float leftX = call.getFloat("leftX");
//                    Float rightY = call.getFloat("rightY");
//                    Float rightX = call.getFloat("rightX");
//                    if (Utils.isNull(leftY) || Utils.isNull(leftX)
//                            || Utils.isNull(rightY) || Utils.isNull(rightX)){
//                      toLog(TAG, "sendJoystickCommand stop cause leftY or leftX or rightY or rightX is Null");
//                      call.reject("DroneManager is Null");
//                      return;
//                    }else {
//                      // Keep At the End of function
//                      callBackIdsSendCommand = null;
//                      call.save();
//                      callBackIdsSendCommand = Utils.clone(call.getCallbackId());
//
//                      manager.sendJoystickCommand(leftY, leftX, rightY, rightX,new OnAsyncOperationComplete() {
//                        @Override
//                        public void onError(@Nullable String errorDetail) {
//                          PluginCall call = bridge.getSavedCall(callBackIdsSendCommand);
//                          if (call == null) {
//                            toLog(TAG , "sendJoystickCommand onError Bridge Is Null ");
//                            return;
//                          }else {
//                            if (Utils.isNull(errorDetail)){
//                              errorDetail = "";
//                            }
//                            call.reject(errorDetail);
//                            call.release(bridge);
//                            callBackIdsSendCommand = null;
//                          }
//
//                        }
//
//                        @Override
//                        public void onSucces(@Nullable String succesMsg) {
//                          PluginCall call = bridge.getSavedCall(callBackIdsSendCommand);
//                          if (call == null) {
//                            toLog(TAG , "sendJoystickCommand onSucces Bridge Is Null ");
//                            return;
//                          }else {
//                            if (Utils.isNull(succesMsg)){
//                              succesMsg = "";
//                            }
//                            call.resolve();
//                            call.release(bridge);
//                            callBackIdsSendCommand = null;
//                          }
//
//                        }
//                      });
//                      toLog(TAG, "sendJoystickCommand Success launch");
//                    }
//                  }
//                }
//              }
//            }
//          }
//        }
//      }
//    }catch (Exception e){
//      Utils.toLog(TAG, "sendJoystickCommand",null,e);
//      call.reject(e.getLocalizedMessage(), e);
//    }catch (Error er){
//      Utils.toLog(TAG, "sendJoystickCommand",er,null);
//      call.reject(er.getLocalizedMessage());
//    }
//  }
//
//  @PluginMethod
//  public void takeOff(PluginCall call){
//    toLog(TAG, "takeOff start");
//    try {
//      if (Utils.isNull(ctx)){
//        toLog(TAG, "takeOff stop cause ctx is Null");
//        return;
//      }else {
//        if (Utils.isNull(call)){
//          toLog(TAG, "takeOff stop cause call is Null");
//          return;
//        }else {
//          PermissionManager pm = new PermissionManager(ctx);
//          if (!pm.isPermissionsGranted()){
//            toLog(TAG, "takeOff stop cause REQUIRE ALL PERMISSIONS");
//            call.reject("REQUIRE ALL PERMISSIONS");
//            return;
//          }else{
//            if (!pm.isSDKreadyForMappingProcess()){
//              toLog(TAG, "SDK Not Ready for Mapping Process MIN IS API 24 ");
//              call.reject("SDK Not Ready for Mapping Process MIN IS API 24");
//              return;
//            }else {
//              DroneManager manager = DroneManager.getInstance();
//              if (Utils.isNull(manager)){
//                toLog(TAG, "takeOff stop cause manager is Null");
//                call.reject("DroneManager is Null");
//                return;
//              }else {
//                if (Utils.isNull(getMainApp())){
//                  toLog(TAG, "takeOff stop cause getMainApp() is Null");
//                  call.reject("PLEASE SET MainApp BEFORE RUN Apps (setMainApp) ...MainApp IS NULL RIGHT NOW");
//                  return;
//                }else {
//                  // Keep At the End of function
//                  callBackIdsTakeOff= null;
//                  call.save();
//                  callBackIdsTakeOff = Utils.clone(call.getCallbackId());
//
//                  manager.takeOff(new OnAsyncOperationComplete() {
//                    @Override
//                    public void onError(@Nullable String errorDetail) {
//                      PluginCall call = bridge.getSavedCall(callBackIdsTakeOff);
//                      if (call == null) {
//                        toLog(TAG , "takeOff onError Bridge Is Null ");
//                        return;
//                      }else {
//                        if (Utils.isNull(errorDetail)){
//                          errorDetail = "";
//                        }
//                        call.reject(errorDetail);
//                        call.release(bridge);
//                        callBackIdsTakeOff = null;
//                      }
//
//                    }
//
//                    @Override
//                    public void onSucces(@Nullable String succesMsg) {
//                      PluginCall call = bridge.getSavedCall(callBackIdsTakeOff);
//                      if (call == null) {
//                        toLog(TAG , "takeOff onSucces Bridge Is Null ");
//                        return;
//                      }else {
//                        if (Utils.isNull(succesMsg)){
//                          succesMsg = "";
//                        }
//                        call.resolve();
//                        call.release(bridge);
//                        callBackIdsTakeOff = null;
//                      }
//
//                    }
//                  });
//                  toLog(TAG, "takeOff Success launch");
//                }
//              }
//            }
//          }
//        }
//      }
//    }catch (Exception e){
//      Utils.toLog(TAG, "takeOff",null,e);
//      call.reject(e.getLocalizedMessage(), e);
//    }catch (Error er){
//      Utils.toLog(TAG, "takeOff",er,null);
//      call.reject(er.getLocalizedMessage());
//    }
//  }
//  @PluginMethod
//  public void returnToHome(PluginCall call){
//    toLog(TAG, "returnToHome start");
//    try {
//      if (Utils.isNull(ctx)){
//        toLog(TAG, "returnToHome stop cause ctx is Null");
//        return;
//      }else {
//        if (Utils.isNull(call)){
//          toLog(TAG, "returnToHome stop cause call is Null");
//          return;
//        }else {
//          PermissionManager pm = new PermissionManager(ctx);
//          if (!pm.isPermissionsGranted()){
//            toLog(TAG, "returnToHome stop cause REQUIRE ALL PERMISSIONS");
//            call.reject("REQUIRE ALL PERMISSIONS");
//            return;
//          }else{
//            if (!pm.isSDKreadyForMappingProcess()){
//              toLog(TAG, "SDK Not Ready for Mapping Process MIN IS API 24 ");
//              call.reject("SDK Not Ready for Mapping Process MIN IS API 24");
//              return;
//            }else {
//              DroneManager manager = DroneManager.getInstance();
//              if (Utils.isNull(manager)){
//                toLog(TAG, "returnToHome stop cause manager is Null");
//                call.reject("DroneManager is Null");
//                return;
//              }else {
//                if (Utils.isNull(getMainApp())){
//                  toLog(TAG, "returnToHome stop cause getMainApp() is Null");
//                  call.reject("PLEASE SET MainApp BEFORE RUN Apps (setMainApp) ...MainApp IS NULL RIGHT NOW");
//                  return;
//                }else {
//                  // Keep At the End of function
//                  callBackIdsReturnToHome= null;
//                  call.save();
//                  callBackIdsReturnToHome = Utils.clone(call.getCallbackId());
//
//                  manager.returnToHome(new OnAsyncOperationComplete() {
//                    @Override
//                    public void onError(@Nullable String errorDetail) {
//                      PluginCall call = bridge.getSavedCall(callBackIdsReturnToHome);
//                      if (call == null) {
//                        toLog(TAG , "returnToHome onError Bridge Is Null ");
//                        return;
//                      }else {
//                        if (Utils.isNull(errorDetail)){
//                          errorDetail = "";
//                        }
//                        call.reject(errorDetail);
//                        call.release(bridge);
//                        callBackIdsReturnToHome = null;
//                      }
//
//                    }
//
//                    @Override
//                    public void onSucces(@Nullable String succesMsg) {
//                      PluginCall call = bridge.getSavedCall(callBackIdsReturnToHome);
//                      if (call == null) {
//                        toLog(TAG , "returnToHome onSucces Bridge Is Null ");
//                        return;
//                      }else {
//                        if (Utils.isNull(succesMsg)){
//                          succesMsg = "";
//                        }
//                        call.resolve();
//                        call.release(bridge);
//                        callBackIdsReturnToHome = null;
//                      }
//
//                    }
//                  });
//                  toLog(TAG, "returnToHome Success launch");
//                }
//              }
//            }
//          }
//        }
//      }
//    }catch (Exception e){
//      Utils.toLog(TAG, "returnToHome",null,e);
//      call.reject(e.getLocalizedMessage(), e);
//    }catch (Error er){
//      Utils.toLog(TAG, "returnToHome",er,null);
//      call.reject(er.getLocalizedMessage());
//    }
//  }
//
//  @PluginMethod
//  public void landing(PluginCall call){
//    toLog(TAG, "landing start");
//    try {
//      if (Utils.isNull(ctx)){
//        toLog(TAG, "landing stop cause ctx is Null");
//        return;
//      }else {
//        if (Utils.isNull(call)){
//          toLog(TAG, "landing stop cause call is Null");
//          return;
//        }else {
//          PermissionManager pm = new PermissionManager(ctx);
//          if (!pm.isPermissionsGranted()){
//            toLog(TAG, "landing stop cause REQUIRE ALL PERMISSIONS");
//            call.reject("REQUIRE ALL PERMISSIONS");
//            return;
//          }else{
//            if (!pm.isSDKreadyForMappingProcess()){
//              toLog(TAG, "SDK Not Ready for Mapping Process MIN IS API 24 ");
//              call.reject("SDK Not Ready for Mapping Process MIN IS API 24");
//              return;
//            }else {
//              DroneManager manager = DroneManager.getInstance();
//              if (Utils.isNull(manager)){
//                toLog(TAG, "landing stop cause manager is Null");
//                call.reject("DroneManager is Null");
//                return;
//              }else {
//                if (Utils.isNull(getMainApp())){
//                  toLog(TAG, "landing stop cause getMainApp() is Null");
//                  call.reject("PLEASE SET MainApp BEFORE RUN Apps (setMainApp) ...MainApp IS NULL RIGHT NOW");
//                  return;
//                }else {
//                  // Keep At the End of function
//                  callBackIdsLanding= null;
//                  call.save();
//                  callBackIdsLanding = Utils.clone(call.getCallbackId());
//
//                  manager.landing(new OnAsyncOperationComplete() {
//                    @Override
//                    public void onError(@Nullable String errorDetail) {
//                      PluginCall call = bridge.getSavedCall(callBackIdsLanding);
//                      if (call == null) {
//                        toLog(TAG , "landing onError Bridge Is Null ");
//                        return;
//                      }else {
//                        if (Utils.isNull(errorDetail)){
//                          errorDetail = "";
//                        }
//                        call.reject(errorDetail);
//                        call.release(bridge);
//                        callBackIdsLanding = null;
//                      }
//
//                    }
//
//                    @Override
//                    public void onSucces(@Nullable String succesMsg) {
//                      PluginCall call = bridge.getSavedCall(callBackIdsLanding);
//                      if (call == null) {
//                        toLog(TAG , "landing onSucces Bridge Is Null ");
//                        return;
//                      }else {
//                        if (Utils.isNull(succesMsg)){
//                          succesMsg = "";
//                        }
//                        call.resolve();
//                        call.release(bridge);
//                        callBackIdsLanding = null;
//                      }
//
//                    }
//                  });
//                  toLog(TAG, "landing Success launch");
//                }
//              }
//            }
//          }
//        }
//      }
//    }catch (Exception e){
//      Utils.toLog(TAG, "landing",null,e);
//      call.reject(e.getLocalizedMessage(), e);
//    }catch (Error er){
//      Utils.toLog(TAG, "landing",er,null);
//      call.reject(er.getLocalizedMessage());
//    }
//  }
//
//
//
//  private void toLog(@NonNull String tag, @NonNull String msg){
//    Utils.toLog(tag, msg);
//  }
//}
