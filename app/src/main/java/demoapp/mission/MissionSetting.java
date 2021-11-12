package demoapp.mission;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.CopyOnWriteArrayList;

import demoapp.dronecontroller.model.DroneModel;
import demoapp.dronecontroller.utils.Constants;
import demoapp.dronecontroller.utils.Utils;

public class MissionSetting {
    private float altitudePrefered;
    private boolean useSimulator;
    private float speed;
    private float shootIntervalInSec;
    private boolean exitMissionOnRCSignalLostEnabled; // use True as Default
    private boolean autoReturnOnLowBattery; // use True as Default
    private String errorMsg;
    private @Nullable LatLng homeLocation;
    private String TAG = this.getClass().getSimpleName();
    private CopyOnWriteArrayList<LatLng> flyPoints = new CopyOnWriteArrayList<>();
    private int imageCount;
    private int battery;

    public MissionSetting(float altitudePrefered, double area, boolean useSimulator, boolean exitMissionOnRCSignalLostEnabled, @Nullable LatLng homeLocation,boolean autoReturnOnLowBattery){
        this.altitudePrefered=altitudePrefered;
        this.useSimulator = useSimulator;
        this.exitMissionOnRCSignalLostEnabled = exitMissionOnRCSignalLostEnabled;
        this.homeLocation = homeLocation;
        this.autoReturnOnLowBattery = autoReturnOnLowBattery;

        Utils.toLog( TAG," MissionSetting altitudePrefered: "+altitudePrefered+ " area:"+area);
        FlyParameter flyParams = new FlyParameter(altitudePrefered,area, DroneModel.PHANTOM_4_ADVANCED);
        if (!flyParams.isValid()){
            Utils.toLog( TAG," MissionSetting flyParams Not Valid Check Altitude and Speed");
        }else {
            this.speed              = flyParams.getSpeed();
            this.shootIntervalInSec = flyParams.getShootIntervalInSec();
        }
    }

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public CopyOnWriteArrayList<LatLng> getFlyPoints() {
        if (Utils.isNull(flyPoints)){
            flyPoints = new CopyOnWriteArrayList<>();
        }
        return flyPoints;
    }

    public void setFlyPoints(CopyOnWriteArrayList<LatLng> flyPoints) {
        this.flyPoints = flyPoints;
    }

    public float getShootIntervalInSec() {
        return shootIntervalInSec;
    }

    public void setShootIntervalInSec(float shootIntervalInSec) {
        this.shootIntervalInSec = shootIntervalInSec;
    }

    public MissionSetting(String data)throws Exception,Error{
        fillFromStringOk(data);
    }

    public float getAltitudePrefered() {
        return altitudePrefered;
    }

    public void setAltitudePrefered(float altitudePrefered) {
        this.altitudePrefered = altitudePrefered;
    }

    public boolean useSimulator() {
        return useSimulator;
    }

    public void setUseSimulator(boolean useSimulator) {
        this.useSimulator = useSimulator;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean isExitMissionOnRCSignalLostEnabled() {
        return exitMissionOnRCSignalLostEnabled;
    }

    public void setExitMissionOnRCSignalLostEnabled(boolean exitMissionOnRCSignalLostEnabled) {
        this.exitMissionOnRCSignalLostEnabled = exitMissionOnRCSignalLostEnabled;
    }

    public String getErrorMsg() {
        if (Utils.isNull(errorMsg)){
            errorMsg = "";
        }
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Nullable
    public LatLng getHomeLocation() {
        return homeLocation;
    }

    public void setHomeLocation(@Nullable LatLng homeLocation) {
        this.homeLocation = homeLocation;
    }

    public boolean autoReturnOnLowBatteryEnable() {
        return autoReturnOnLowBattery;
    }

    public void setAutoReturnOnLowBattery(boolean autoReturnOnLowBattery) {
        this.autoReturnOnLowBattery = autoReturnOnLowBattery;
    }

    public boolean isValid(@Nullable Boolean includeFlyPoints){
        try {
            if (Utils.isNull(includeFlyPoints)){
                includeFlyPoints = false;
            }
            Utils.toLog(TAG, " isValid start");
            if (altitudePrefered <= Constant.INTEGER_NULL){
                setErrorMsg( " startWaypointMission Bad Altitude <=0");
                return false;
            }else {
                if (speed < Constants.SPEED_MIN || speed > Constants.SPEED_MAX){
                    setErrorMsg( " startWaypointMission Speed should be [2,15]");
                    return false;
                }else {
                    if (shootIntervalInSec <= Constant.INTEGER_NULL){
                        setErrorMsg( " startWaypointMission shootIntervalInSec should be Greater than 0");
                        return false;
                    }else {
                        if (shootIntervalInSec <Constants.DJI_JPEG_INTERVAL_LIMIT_SECONDS){
                            setErrorMsg( " startWaypointMission shootSpeed is less than 2 seconds No Picture will be take");
                            return false;
                        }else {
                            if (Utils.isNull(homeLocation)){
                                setErrorMsg ("homeLocation is Null");
                                return false;
                            }else {
                                Double androidPhoneLat = homeLocation.latitude;
                                Double androidPhoneLng = homeLocation.longitude;
                                if (androidPhoneLat == Constants.DOUBLE_NULL || androidPhoneLng == Constants.DOUBLE_NULL){
                                    setErrorMsg ("home latitude or home longitude Is DOUBLE_NULL");
                                    return false;
                                }else {
                                    if (androidPhoneLat.isNaN() || androidPhoneLng.isNaN()){
                                        setErrorMsg ("androidPhoneLat or androidPhoneLng Is NaN");
                                        return false;
                                    }else {
                                        if (!includeFlyPoints){
                                            return true;
                                        }else {
                                            if (flyPoints.size() <= 0){
                                                setErrorMsg ("Waypoints List is Empty");
                                                return false;
                                            }else {
                                                return true;
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
            Utils.toLog(TAG, "isValid",null,e);
        }catch (Error er){
            Utils.toLog(TAG, "isValid",er,null);
        }
        return false;
    }

    public @Nullable String toStringJSON() throws Exception,Error{
        try {
            if (!isValid(null)){
                return null;
            }else {
                JSONArray arrayForFlyPoints = new JSONArray();
                for (LatLng item:getFlyPoints()) {
                    JSONObject son = new JSONObject();
                    son.put(Constants.JSON_KEY_LATITUDE,item.latitude);
                    son.put(Constants.JSON_KEY_LONGITUDE,item.longitude);
                    arrayForFlyPoints.put(son);
                }
                JSONObject o = new JSONObject();
                o.put(Constants.JS_KEY_ALTITUDE_PREFERED,getAltitudePrefered());
                o.put(Constants.JS_KEY_USE_SIMULATOR,useSimulator());
                o.put(Constants.JS_KEY_SPEED,getSpeed());
                o.put(Constants.JS_KEY_SHOOT_INTERVAL,getShootIntervalInSec());
                o.put(Constants.JS_KEY_EXIT_MISSION,isExitMissionOnRCSignalLostEnabled());
                o.put(Constants.JS_KEY_HOME_LOCATION_LATITUDE,getHomeLocation().latitude);
                o.put(Constants.JS_KEY_HOME_LOCATION_LONGITUDE,getHomeLocation().longitude);
                o.put(Constants.JS_KEY_AUTO_RETURN_ON_LOW_BATTERY, autoReturnOnLowBatteryEnable());
                o.put(Constants.JS_KEY_FLY_POINTS,arrayForFlyPoints.toString());
                o.put(Constants.JS_KEY_BATTERY_NEED,getBattery());
                o.put(Constants.JS_KEY_IMAGE_COUNT,getImageCount());
                return o.toString();
            }
        }catch (Exception e){
            Utils.toLog(TAG, "toStringJSON",null,e);
        }catch (Error er){
            Utils.toLog(TAG, "toStringJSON",er,null);
        }
        return null;
    }

    public boolean fillFromStringOk(@NonNull String data){
        if (Utils.isNullOrEmpty(data)){
            return false;
        }else {
            try {
                JSONObject o = new JSONObject(data);
                setAltitudePrefered(        (float) o.getDouble(Constants.JS_KEY_ALTITUDE_PREFERED));
                setUseSimulator(                    o.getBoolean(Constants.JS_KEY_USE_SIMULATOR));
                setSpeed(                   (float) o.getDouble(Constants.JS_KEY_SPEED));
                setShootIntervalInSec(      (float) o.getDouble(Constants.JS_KEY_SHOOT_INTERVAL));
                setExitMissionOnRCSignalLostEnabled(o.getBoolean(Constants.JS_KEY_EXIT_MISSION));
                double latitude =                   o.getDouble(Constants.JS_KEY_HOME_LOCATION_LATITUDE);
                double longitude =                  o.getDouble(Constants.JS_KEY_HOME_LOCATION_LONGITUDE);
                setHomeLocation(new LatLng(latitude,longitude));
                setAutoReturnOnLowBattery(          o.getBoolean(Constants.JS_KEY_AUTO_RETURN_ON_LOW_BATTERY));
                setBattery(                         o.getInt(Constants.JS_KEY_BATTERY_NEED));
                setImageCount(                      o.getInt(Constants.JS_KEY_IMAGE_COUNT));
                String flyPointsData =              o.getString(Constants.JS_KEY_FLY_POINTS);
                JSONArray itemArray =  new JSONArray(flyPointsData);
                for (int a = 0; a<itemArray.length(); a++){
                    JSONObject obj = itemArray.getJSONObject(a);
                    if (Utils.isNull(obj)){
                        // skip
                    }else {
                        double itemLatitude = obj.getDouble(Constants.JSON_KEY_LATITUDE);
                        double itemLongitude = obj.getDouble(Constants.JSON_KEY_LONGITUDE);
                        LatLng latLng = new LatLng(itemLatitude,itemLongitude);
                        getFlyPoints().add(latLng);
                    }
                }

                return isValid(null);
            }catch (Exception e){
                Utils.toLog(TAG, "fillFromStringOk",null,e);

            }catch (Error er){
                Utils.toLog(TAG, "fillFromStringOk",er,null);

            }
            return false;
        }

    }
}
