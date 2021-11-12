package demoapp.dronecontroller.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import demoapp.dronecontroller.interfaces.OnStateChangeListener;
import demoapp.dronecontroller.utils.Utils;
import dji.common.flightcontroller.FlightControllerState;
import dji.common.flightcontroller.GPSSignalLevel;
import dji.common.flightcontroller.LocationCoordinate3D;
import dji.common.product.Model;
import dji.sdk.airlink.AirLink;
import dji.sdk.battery.Battery;
import dji.sdk.flightcontroller.FlightController;
import dji.sdk.products.Aircraft;

public class DroneState {
  private final String TAG = this.getClass().getSimpleName();
  @Nullable Aircraft aircraft ;
  @Nullable
  OnStateChangeListener onRecordLog;
  private int uplinkSignalQuality = 0;
  private String gpsMsg = "";
  private short satelliteCount = 0;
  private int chargeRemainingInPercent = 0;
  private int chargeRemainingInMAh = 0;
  private int lifetimeRemainingInPercent = 0;
  private int numberOfDischarges = 0;
  private float temperature = 0;
  private float heading = 0;
  private int droneHeadingInDegrees = 0;
  private float speedInMeterPerSec = 0;
  private double longitude =0;
  private double latitude =0;
  private float altitude = 0;

  public int getmUplinkQuality() {
    return uplinkSignalQuality;
  }

  @Nullable
  public Aircraft getAircraft() {
    return aircraft;
  }

  public OnStateChangeListener getLogger() {
    return onRecordLog;
  }

  public DroneState (final Aircraft aircraft, final OnStateChangeListener onRecordLog){
    this.aircraft = aircraft;
    this.onRecordLog = onRecordLog;
  }

  public int getChargeRemainingInPercent() {
    synchronized (this){
      return chargeRemainingInPercent;
    }
  }

  public void initStateListener(@NonNull final Runnable r) {
    synchronized (DroneState.this){
      try {
        if (Utils.isNull(getLogger())){
          return;
        }else {
          getLogger().onLog("initStateListener Start");
          if (Utils.isNull(r)){
            getLogger().onLog("initStateListener stop cause r is null");
            return;
          }else{
            if (Utils.isNull(getAircraft())){
              getLogger().onLog("initStateListener stop cause aircraft is null");
              return;
            }else {
              if (getAircraft().getModel() == Model.UNKNOWN_AIRCRAFT) {
                // Model became UNKNOWN_AIRCRAFT when RC signal LOST and Recover and
                // Crash App
                getLogger().onLog("initStateListener stop cause Model.UNKNOWN_AIRCRAFT is null");
                return;
              }else {
                // Register RC Signal
                AirLink airLink = getAircraft().getAirLink();
                Battery battery = getAircraft().getBattery();
                FlightController mFlightController = getAircraft().getFlightController();
                if (Utils.isNull(airLink)){
                  getLogger().onLog("initStateListener stop cause airLink is null");
                  return;
                }else {
                  if (Utils.isNull(battery)){
                    getLogger().onLog("initStateListener stop cause battery is null");
                    return;
                  }else {
                    if (Utils.isNull(mFlightController)){
                      getLogger().onLog("initStateListener stop cause mFlightController is null");
                      return;
                    }else {
                      mFlightController.setStateCallback(state ->{
                        synchronized (DroneState.this){
                          try {
                            if (Utils.isNull(state)){
                              getLogger().onLog(" setStateCallback state is Null");
                              return;
                            }else {
                              LocationCoordinate3D location = state.getAircraftLocation();
                              if (Utils.isNull(location)){
                                getLogger().onLog(" setStateCallback location is Null");
                                return;
                              }else {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                altitude = location.getAltitude();
                              }
                            }
                          }catch (Exception e){
                            Utils.toLog(TAG, "setStateCallback",null,e);
                          }catch (Error er){
                            Utils.toLog(TAG, "setStateCallback",er,null);
                          }

                        }
                      });

                      airLink.setUplinkSignalQualityCallback(i -> {
                        // Not fired When signal is Constant
                        synchronized (DroneState.this){
                          try {
                            if (Utils.isNull(i)){
                              getLogger().onLog("initStateListener stop cause i is Null");
                              return;
                            }else {
                              uplinkSignalQuality = i;
                              updateState(()->{
                                // Update Onely
                              });
                            }
                          }catch (Exception e){
                            Utils.toLog(TAG, "setUplinkSignalQualityCallback",null,e);
                          }catch (Error er){
                            Utils.toLog(TAG, "setUplinkSignalQualityCallback",er,null);
                          }

                        }
                      });
                      // BATTERY STATE
                      battery.setStateCallback(batteryState -> {
                        synchronized (DroneState.this){
                          try {
                            if (Utils.isNull(batteryState)){
                              getLogger().onLog("initStateListener stop cause batteryState isNull");
                              return;
                            }else {
                              chargeRemainingInMAh            = batteryState.getChargeRemaining();
                              temperature                     = batteryState.getTemperature();
                              chargeRemainingInPercent        = batteryState.getChargeRemainingInPercent();
                              lifetimeRemainingInPercent      = batteryState.getLifetimeRemaining();
                              numberOfDischarges              = batteryState.getNumberOfDischarges();
                              updateState(()->{
                                // Update Onely
                              });
                            }
                          }catch (Exception e){
                            Utils.toLog(TAG, "setStateCallback",null,e);
                          }catch (Error er){
                            Utils.toLog(TAG, "setStateCallback",er,null);
                          }

                        }
                      });

                      updateState(r);
                    }

                  }
                }
              }

            }

          }
        }
      }catch (Exception e){
        Utils.toLog(TAG, "initStateListener",null,e);
      }catch (Error er){
        Utils.toLog(TAG, "initStateListener",er,null);
      }
    }

  }

  public void stopNotify(){
    try {
      this.onRecordLog = null;
      Utils.toLog(TAG, "stopNotify SUCCES onRecordLog toNull");
    }catch (Exception e){
      Utils.toLog(TAG, "stopNotify",null,e);
    }catch (Error er){
      Utils.toLog(TAG, "stopNotify",er,null);
    }

  }

  public void updateState(@NonNull Runnable r){
    synchronized (DroneState.this){
      try {
        if (Utils.isNull(getLogger())){
          return;
        }else {
          if (Utils.isNull(r)){
            getLogger().onLog("updateState stop cause r is null");
            return;
          }else{
            if (Utils.isNull(getAircraft())){
              getLogger().onLog("updateState stop cause aircraft is null");
              return;
            }else {
              if (getAircraft().getModel() == Model.UNKNOWN_AIRCRAFT) {
                // Model became UNKNOWN_AIRCRAFT when RC signal LOST and Recover and
                // Crash App
                getLogger().onLog("updateState stop cause Model.UNKNOWN_AIRCRAFT is null");
                return;
              }else {
                FlightController controller = getAircraft().getFlightController();
                if (Utils.isNull(controller)){
                  getLogger().onLog("initStateListener stop cause FlightController is null");
                  return;
                }else{
                  FlightControllerState state = controller.getState();
                  if (Utils.isNull(state)){
                    getLogger().onLog("initStateListener stop cause state is null");
                    return;
                  }else {
                    // GPS SIGNAL START
                    satelliteCount = (short) state.getSatelliteCount();

                    double yaw = state.getAttitude().yaw;
                    if (yaw < 0)
                      yaw += 360;
                    droneHeadingInDegrees = (int) yaw;

                    GPSSignalLevel gpsLevel = state.getGPSSignalLevel();
                    gpsMsg = "";
                    switch (gpsLevel){
                      case NONE:
                        gpsMsg ="NO_GPS_SIGNAL";
                        break;
                      case LEVEL_0:
                        gpsMsg ="VERY_BAD_GPS_SIGNAL_ALMOST_NO_SIGNAL";
                        break;
                      case LEVEL_1:
                        gpsMsg ="VERY_WEAK_GPS_SIGNAL";
                        break;
                      case LEVEL_2:
                        gpsMsg ="WEAK_GPS_SIGNAL";
                        break;
                      case LEVEL_3:
                      case LEVEL_4:
                        gpsMsg ="GOOD_GPS_SIGNAL";
                        break;
                      case LEVEL_5:
                        gpsMsg ="VERY_STRONG_GPS_SIGNAL";
                        break;
                    }
                    gpsMsg = gpsMsg +"satelliteCount: "+satelliteCount;


                    heading = controller.getCompass().getHeading();

                    // Mavlink: Current airspeed in m/s
                    // DJI: unclear whether getState() returns airspeed or groundspeed
                    speedInMeterPerSec = (float) (Math.sqrt(Math.pow(getAircraft().getFlightController().getState().getVelocityX(), 2) +
                      Math.pow(getAircraft().getFlightController().getState().getVelocityY(), 2)));

                    String msg = gpsMsg+ " uplinkSignalQuality:"+uplinkSignalQuality+ " Heading inDegrees: "+heading + "altitude: "+ altitude+
                      "Speed In Meters Per Sec: "+ speedInMeterPerSec + " chargeRemainingInMAh "+chargeRemainingInMAh + " temperature "+temperature+ " chargeRemainingInPercent"+
                      chargeRemainingInPercent+ " lifetimeRemainingInPercent "+
                      (lifetimeRemainingInPercent == 0? "UNSUPPORTED PRODUCTS":lifetimeRemainingInPercent)
                      + " numberOfDischarges "+numberOfDischarges;
                    // getLogger().onLog(msg);
                    getLogger().onChange( uplinkSignalQuality, satelliteCount,chargeRemainingInPercent, chargeRemainingInMAh ,
                      temperature,
                      altitude,
                      speedInMeterPerSec,
                      heading ,
                      numberOfDischarges,
                      lifetimeRemainingInPercent,
                      longitude,
                      latitude,
                      altitude,
                      droneHeadingInDegrees,
                      "");



                    r.run();
                  }
                }
              }
            }
          }
        }
      }catch (Exception e){
        Utils.toLog(TAG, "updateState",null,e);
      }catch (Error er){
        Utils.toLog(TAG, "updateState",er,null);
      }
    }
  }

  public interface OnRecordLog {
    void toLogUI(String msg);

  }
}
