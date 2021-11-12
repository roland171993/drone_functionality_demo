package demoapp.dronecontroller.model;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import demoapp.dronecontroller.interfaces.OnAsyncOperationComplete;
import demoapp.dronecontroller.interfaces.OnAsyncOperationCompleteBool;
import demoapp.dronecontroller.utils.Utils;


public abstract class Drone {
  protected String name;
  protected double longitude =0;
  protected double latitude =0;
  protected float altitude = 0;
  protected int uplinkSignalQuality = 0;
  protected short satelliteCount = 0;
  protected int chargeRemainingInPercent = 0;
  protected int chargeRemainingInMAh = 0;
  protected float temperature = 0;
  protected float speedInMeterPerSec = 0;

  public int getUplinkSignalQuality() {
    return uplinkSignalQuality;
  }

  public void setUplinkSignalQuality(int uplinkSignalQuality) {
    this.uplinkSignalQuality = uplinkSignalQuality;
  }

  public short getSatelliteCount() {
    return satelliteCount;
  }

  public void setSatelliteCount(short satelliteCount) {
    this.satelliteCount = satelliteCount;
  }

  public int getChargeRemainingInPercent() {
    return chargeRemainingInPercent;
  }

  public void setChargeRemainingInPercent(int chargeRemainingInPercent) {
    this.chargeRemainingInPercent = chargeRemainingInPercent;
  }

  public int getChargeRemainingInMAh() {
    return chargeRemainingInMAh;
  }

  public void setChargeRemainingInMAh(int chargeRemainingInMAh) {
    this.chargeRemainingInMAh = chargeRemainingInMAh;
  }

  public float getTemperature() {
    return temperature;
  }

  public void setTemperature(float temperature) {
    this.temperature = temperature;
  }

  public float getSpeedInMeterPerSec() {
    return speedInMeterPerSec;
  }

  public void setSpeedInMeterPerSec(float speedInMeterPerSec) {
    this.speedInMeterPerSec = speedInMeterPerSec;
  }

  public String getName() {
    if (Utils.isNull(name)){
      name = "";
    }
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public float getAltitude() {
    return altitude;
  }

  public void setAltitude(float altitude) {
    this.altitude = altitude;
  }

  public abstract void ini(@NonNull Context ctx);
  public abstract void connect(OnAsyncOperationComplete callback);
  public abstract void disconnect(OnAsyncOperationComplete callback);
  public abstract void doTest(@NonNull final Activity activity, final OnAsyncOperationComplete callback);
  public abstract void onDestroy();
  public abstract void takeOff(@NonNull OnAsyncOperationComplete callback);
  public abstract void landing(@NonNull OnAsyncOperationComplete callback);
  public abstract void returnToHome(@NonNull OnAsyncOperationComplete callback);
  public abstract boolean isValid(boolean caseToFly,@Nullable Boolean caseJoystick,@Nullable Boolean landingOrTakeOff);

  public abstract void isMappingMissionExist(Context ctx, OnAsyncOperationCompleteBool callback);
  public abstract void cleanUserData(Context ctx, OnAsyncOperationCompleteBool callback);
}
