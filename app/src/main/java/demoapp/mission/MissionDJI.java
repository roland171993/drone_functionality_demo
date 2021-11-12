package demoapp.mission;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import demoapp.dronecontroller.interfaces.OnAsyncOperationComplete;
import demoapp.dronecontroller.interfaces.OnAsyncOperationCompleteBool;
import demoapp.dronecontroller.interfaces.OnMissionProcessingComplete;
import demoapp.dronecontroller.interfaces.OnMissionRunningListener;
import demoapp.dronecontroller.model.DroneState;
import demoapp.dronecontroller.utils.Constants;
import demoapp.dronecontroller.utils.Save;
import demoapp.dronecontroller.utils.Utils;
import demoapp.mapping.Grid;
import demoapp.mapping.Mapping;
import demoapp.mapping.PointLatLngAlt;
import dji.common.error.DJIError;
import dji.common.mission.waypoint.Waypoint;
import dji.common.mission.waypoint.WaypointMission;
import dji.common.mission.waypoint.WaypointMissionDownloadEvent;
import dji.common.mission.waypoint.WaypointMissionExecutionEvent;
import dji.common.mission.waypoint.WaypointMissionFinishedAction;
import dji.common.mission.waypoint.WaypointMissionFlightPathMode;
import dji.common.mission.waypoint.WaypointMissionHeadingMode;
import dji.common.mission.waypoint.WaypointMissionState;
import dji.common.mission.waypoint.WaypointMissionUploadEvent;
import dji.common.util.CommonCallbacks;
import dji.sdk.mission.waypoint.WaypointMissionOperator;
import dji.sdk.mission.waypoint.WaypointMissionOperatorListener;
import dji.sdk.sdkmanager.DJISDKManager;

public class MissionDJI {
    private final String TAG = getClass().getSimpleName();

    private @Nullable OnMissionRunningListener callbackEnd = null;
    private @Nullable  MissionStep missionStepEnd = null;
    private @Nullable  MissionSetting missionSettingEnd = null;
    private @Nullable  Activity activityEnd = null;
    private @Nullable DroneState droneStateEnd = null;
    private @Nullable GridSetting settingEnd = null;
    private @Nullable WaypointMissionOperator waypointMissionOperator = null;
    private volatile boolean computing = false;

    public MissionDJI(){
        setComputing(false);
    }

    public static boolean isRightWaypoint(@NonNull final PointLatLngAlt item)throws Exception,Error {
        String tagTrimed = item.getTag().trim();
        if (tagTrimed.contentEquals("S") || tagTrimed.contentEquals("E") )
        {
            return true;
        }
        return false;
    }

    public boolean isComputing() {
        return computing;
    }

    public void setComputing(boolean computing) {
        this.computing = computing;
    }

    private WaypointMissionOperatorListener eventNotificationListener = new WaypointMissionOperatorListener() {
        @Override
        public void onDownloadUpdate(WaypointMissionDownloadEvent downloadEvent) {

        }

        @Override
        public void onUploadUpdate(WaypointMissionUploadEvent uploadEvent) {

        }

        @Override
        public void onExecutionUpdate(WaypointMissionExecutionEvent executionEvent) {
            if (Utils.isNull(executionEvent)){
                return;
            }else {
                if (Utils.isNull(executionEvent.getProgress())){
                    return;
                }else {
                    int targetWaypointIndex = executionEvent.getProgress().targetWaypointIndex;
                    int totalWaypointCount = executionEvent.getProgress().totalWaypointCount;
                    toLog("targetWaypointIndex:"+targetWaypointIndex);
                    toLog("totalWaypointCount:"+totalWaypointCount);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        shouldCancelOrContinu(true,missionSettingEnd,missionStepEnd,activityEnd,droneStateEnd, settingEnd,targetWaypointIndex, totalWaypointCount,callbackEnd);
                    }
                }

            }
        }

        @Override
        public void onExecutionStart() {

        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onExecutionFinish(@Nullable final DJIError error) {
            toLog("Execution finished: Start");
            try {
                if (!Utils.isNull(error)){
                    String description = error.getDescription();
                    if (Utils.isNull(description)){
                        description = "";
                    }
                    String msg = "Execution finished with Error: "+description;
                    toLog(msg);
                    if (Utils.isNull(callbackEnd)){
                        toLog("Execution finished: Error happen & callbackEnd is Null");
                    }else {
                        callbackEnd.onError(msg);
                    }
                }else {
                    // Case No Error
                    if (Utils.isNull(callbackEnd)){
                        toLog("Execution finished but callbackEnd is Null");
                        return;
                    }else {
                        if (Utils.isNull(missionStepEnd)){
                            toLog("Execution finished but Cannot continu to Next Step cause missionStepEnd Is Null");
                            callbackEnd.onError("Cannot continu to Next Step cause missionStepEnd Is Null");
                            return;
                        }else {
                            if (Utils.isNull(missionSettingEnd)){
                                toLog("Execution finished but Cannot continu to Next Step cause Camera Setting End Is Null");
                                callbackEnd.onError("Cannot continu to Next Step cause Camera Setting Is Null");
                                return;
                            }else {
                                if (Utils.isNull(activityEnd)){
                                    toLog("Execution finished but Cannot continu to Next Step cause activityEnd Is Null");
                                    callbackEnd.onError("Cannot continu to Next Step cause activityEnd Is Null");
                                    return;
                                }else {
                                    if (Utils.isNull(droneStateEnd)){
                                        toLog("Execution finished but Cannot continu to Next Step cause droneStateEnd Is Null");
                                        callbackEnd.onError("Cannot continu to Next Step cause droneStateEnd Is Null");
                                        return;
                                    }else {
                                        if (Utils.isNull(settingEnd)){
                                            toLog("Execution finished but Cannot continu to Next Step cause settingEnd Is Null");
                                            callbackEnd.onError("Cannot continu to Next Step cause settingEnd Is Null");
                                            return;
                                        }else {
                                            if (Utils.isNull(waypointMissionOperator)){
                                                toLog("Execution finished but Cannot continu to Next Step cause waypointMissionOperator Is Null");
                                                callbackEnd.onError("Cannot continu to Next Step cause waypointMissionOperator Is Null");
                                                return;
                                            }else {
                                                if (Utils.isNull(eventNotificationListener)){
                                                    toLog("Execution finished but Cannot continu to Next Step cause eventNotificationListener Is Null");
                                                    callbackEnd.onError("Cannot continu to Next Step cause eventNotificationListener Is Null");
                                                    return;
                                                }else {
                                                    waypointMissionOperator.clearMission();
                                                    waypointMissionOperator.removeListener(eventNotificationListener);
                                                    int totalStep = missionStepEnd.getSize();
                                                    if (totalStep == 0){
                                                        toLog("Execution finished :Unknow Error happen");
                                                        // Dont Reset If Error Happen
                                                        return;
                                                    }
                                                    if (totalStep == 1){
                                                        // Was Last
                                                        resetMission(activityEnd, new OnAsyncOperationComplete() {
                                                            @Override
                                                            public void onError(@Nullable String errorDetail) {
                                                                toLog("Error on Clean Mission after Execution complit");
                                                                callbackEnd.onError("Error on Clean Mission after Execution complit");
                                                            }

                                                            @Override
                                                            public void onSucces(@Nullable String succesMsg) {
                                                                callbackEnd.onSucces();
                                                                callbackEnd.mustGoHome();
                                                            }
                                                        });

                                                        return;
                                                    }else {
                                                        if (totalStep > 1){
                                                            if (!missionStepEnd.removeFirstOk()){
                                                                toLog("removeFirstOk Failed:Error Cannot remove old Step before continu");
                                                                callbackEnd.onError("Error Cannot remove old Step before continu");
                                                                return;
                                                            }else {
                                                                callbackEnd.onStepSucces(missionStepEnd.getSize());
                                                                shouldCancelOrContinu(null,missionSettingEnd,missionStepEnd,activityEnd,droneStateEnd, settingEnd,null,null,callbackEnd);
                                                                return;
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
                Utils.toLog(TAG, "onExecutionFinish",null,e);
            }catch (Error er){
                Utils.toLog(TAG, "onExecutionFinish",er,null);
            }
        }
    };

    public void createGridAndShowIt(@NonNull final Activity activity, final CopyOnWriteArrayList<LatLng> coords, @NonNull final MissionSetting missionSetting,final @NonNull GridSetting setting,final @NonNull DroneState droneState, final @NonNull OnMissionRunningListener callback) {
        try {
            setComputing(false);
            toLog("startWaypointMission createGridAndShowIt fired");
            if (Utils.isNull(callback)){
                toLog("createGridAndShowIt callback is Null");
                return;
            }else {
                this.callbackEnd = callback;
                if (Utils.isNull(activity)){
                    toLog(" createGridAndShowIt activity is null");
                    callback.onError(" createGridAndShowIt activity is null");
                    return;
                }else {
                    if (Utils.isNull(coords)){
                        toLog(" createGridAndShowIt points is null");
                        callback.onError(" createGridAndShowIt points is null");
                        return;
                    }else {
                        if (Utils.isNull(setting)){
                            toLog(" createGridAndShowIt Setting isNull");
                            callback.onError(" createGridAndShowIt Setting isNull");
                            return;
                        }else {
                            if (Utils.isNull(droneState)){
                                toLog(" createGridAndShowIt droneState is Null");
                                callback.onError("droneState is Null");
                                return;
                            }else {
                                if (Utils.isNull(missionSetting)){
                                    toLog(" createGridAndShowIt MissionSetting isNull");
                                    callback.onError(" createGridAndShowIt MissionSetting isNull");
                                    return;
                                }else {
                                    if (!missionSetting.isValid(null)){
                                        String msg = " createGridAndShowIt "+missionSetting.getErrorMsg();
                                        toLog(msg);
                                        callback.onError(msg);
                                        return;
                                    }else {
                                        if (Utils.isNull(missionSetting.getHomeLocation())){
                                            toLog(" createGridAndShowIt missionSetting.HomeLocation is Null");
                                            callback.onError("missionSetting.HomeLocation is Null");
                                            return;
                                        }else {
                                            PointLatLngAlt home = new PointLatLngAlt(missionSetting.getHomeLocation().latitude,missionSetting.getHomeLocation().longitude);
                                            CopyOnWriteArrayList<PointLatLngAlt> list =  new CopyOnWriteArrayList<>();
                                            for (LatLng current: coords) {
                                                list.add(new PointLatLngAlt(current.latitude, current.longitude,0));
                                            }
                                            GridSetting finalSetting = setting;
                                            toLog("startWaypointMission Will generateSurveyPath");
                                            toLog("startWaypointMission Altitude "+setting.getAltitude() +" Distance:"+setting.getDistance()+" Spacing:"+setting.getSpacing()+ " getAngle:"+setting.getAngle() +" OverShoot1:"+setting.getOverShoot1()
                                                    +" getOverShoot2:"+setting.getOverShoot2()+ " Leadin1"+setting.getLeadin1()+" Leadin2:"+setting.getLeadin2() +" homeLatitude "+home.getLat()+ " homeLong"+home.getLng());
                                            Mapping.generateSurveyPath(list, setting.getAltitude(), setting.getDistance(), setting.getSpacing(), setting.getAngle(), setting.getOverShoot1(), setting.getOverShoot2(),
                                                    Grid.StartPosition.Point, false, 0, setting.getLeadin1(), setting.getLeadin2(),home, false,
                                                    new Mapping.OnComputingComplete() {

                                                        @Override
                                                        public void OnError() {
                                                            toLog("generateSurveyPath OnError");
                                                            callback.onError(" Cannot Generate GRID PLEASE VERIFY POLYGONE AND CAMERA PARAMETER");
                                                            return;
                                                        }

                                                        @RequiresApi(api = Build.VERSION_CODES.N)
                                                        @Override
                                                        public void OnSucces(@NonNull CopyOnWriteArrayList<PointLatLngAlt> grid) {
                                                            try {
                                                                if (grid == null){
                                                                    toLog(" generateSurveyPath onCreate grid IS NULL");
                                                                    callback.onError(" Cannot Generate GRID");
                                                                    return;
                                                                }else{
                                                                    if (grid.size() == 0){
                                                                        toLog(" generateSurveyPath Is empty");
                                                                        callback.onError("Grid is Empty");
                                                                        return;
                                                                    }else {
                                                                        toLog(" generateSurveyPath grid SIZE "+grid.size());
                                                                        CopyOnWriteArrayList<LatLng> flyPoints = new CopyOnWriteArrayList<>();
                                                                        // Count All Step Images
                                                                        double distanceInMeters = Constants.DOUBLE_NULL;
                                                                        LatLng previous = null;
                                                                        for (PointLatLngAlt item: grid) {
                                                                            try {
                                                                                if (MissionDJI.isRightWaypoint(item)){
                                                                                    LatLng current = new LatLng(Utils.clone(item.getLat()), Utils.clone(item.getLng()));
                                                                                    flyPoints.add(current);
                                                                                    if (Utils.isNull(previous)){
                                                                                        // Case First Point
                                                                                    }else {
                                                                                        distanceInMeters += SphericalUtil.computeDistanceBetween(previous,current);
                                                                                    }
                                                                                    previous = null;
                                                                                    previous = new LatLng(current.latitude,current.longitude);
                                                                                }
                                                                            }catch (Exception e){
                                                                                Utils.toLog(TAG, "grid.forEach",null,e);
                                                                            }catch (Error er){
                                                                                Utils.toLog(TAG, "grid.forEach",er,null);
                                                                            }
                                                                        }
                                                                        if (distanceInMeters <= 0){
                                                                            toLog(" createGridAndShowIt distanceInMeters <= 0");
                                                                            callback.onError("BAD distanceInMeters <= 0");
                                                                            return;
                                                                        }else {
                                                                            // All Step TakeOff go To Altitude -> go to First Waypopint+ Mission
                                                                            // From LastPoint go to HomePoint + TakeOff
                                                                            double mappingTime = distanceInMeters / missionSetting.getSpeed();
                                                                            int batteriesCount = 0;
                                                                            if (mappingTime <= Constants.TIME_16_MINUTES_TO_SECONDES){
                                                                                // One Battery for 0 to 16 Min(960 Sec)
                                                                                batteriesCount = 1;
                                                                            }else {
                                                                                // Arrondi ceil(5.2) = 6.0
                                                                                double missionTime = Utils.clone(mappingTime); // Made Copy
                                                                                batteriesCount = (int) Math.ceil(missionTime / Constants.TIME_16_MINUTES_TO_SECONDES);
                                                                            }

                                                                            int imageCount = (int) Math.ceil(mappingTime / missionSetting.getShootIntervalInSec());
                                                                            toLog("MISSION Result distanceInMeters:"+distanceInMeters+ " batterie "+batteriesCount+" imageCount "+imageCount);
                                                                            toLog("MISSION Result Succes Speed():"+missionSetting.getSpeed()+ " shootIntervalInSec "+missionSetting.getShootIntervalInSec());

                                                                            missionSetting.setBattery(batteriesCount);
                                                                            missionSetting.setFlyPoints(flyPoints);
                                                                            missionSetting.setImageCount(imageCount);
                                                                            // Notify image Count
                                                                            callback.onNotifyState(missionSetting.getBattery(),missionSetting.getImageCount(),missionSetting.getSpeed(),missionSetting.getFlyPoints());
                                                                            MissionStep missionStep = new MissionStep();
                                                                            missionStep.addAllOk(grid, new OnAsyncOperationComplete() {
                                                                                @Override
                                                                                public void onError(@Nullable String errorDetail) {
                                                                                    toLog(" generateSurveyPath Convert Mission to StepList of 99 points failed");
                                                                                    callback.onError("Convert Mission to StepList of 99 points failed");
                                                                                    return;
                                                                                }

                                                                                @Override
                                                                                public void onSucces(@Nullable String succesMsg) {
                                                                                    shouldCancelOrContinu(null,missionSetting,missionStep,activity,droneState, finalSetting,null,null,callback);
                                                                                }
                                                                            });
                                                                        }
                                                                    }
                                                                }
                                                            }catch (Exception e){
                                                                Utils.toLog(TAG, "generateSurveyPath",null,e);
                                                                if (Utils.isNull(callback)){
                                                                    return;
                                                                }else {
                                                                    callback.onError("Error");
                                                                }
                                                            }catch (Error er){
                                                                Utils.toLog(TAG, "generateSurveyPath",er,null);
                                                                if (Utils.isNull(callback)){
                                                                    return;
                                                                }else {
                                                                    callback.onError("Error");
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void OnNotifyState(int percentGoToUIThread) {
                                                            callback.onGridProgressing(percentGoToUIThread);
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
            Utils.toLog(TAG, "createGridAndShowIt",null,e);
            if (Utils.isNull(callback)){
                return;
            }else {
                callback.onError("Error");
            }
        }catch (Error er){
            Utils.toLog(TAG, "createGridAndShowIt",er,null);
            if (Utils.isNull(callback)){
                return;
            }else {
                callback.onError("Error");
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public synchronized void shouldCancelOrContinu(@Nullable Boolean onExecutionUpdate,@NonNull final MissionSetting missionSetting, @NonNull final MissionStep missionStep, @NonNull final Activity activity, final @NonNull DroneState droneState, @NonNull GridSetting setting,@Nullable Integer targetWaypointIndex,@Nullable Integer totalWaypointCount,  final @NonNull OnMissionRunningListener callback) {
        toLog("shouldCancelOrContinu Start");
        try {
            if (isComputing()){
                // Skip
                toLog("shouldCancelOrContinu SKIPPED");
                return;
            }else{
                setComputing(true);
                if (Utils.isNull(targetWaypointIndex)){
                    targetWaypointIndex = 0;
                }
                if (Utils.isNull(totalWaypointCount)){
                    totalWaypointCount =0;
                }
                if (Utils.isNull(onExecutionUpdate)){
                    onExecutionUpdate = false;
                }
                if (Utils.isNull(callback)){
                    setComputing(false);
                    toLog("startWaypointMission callback is Null");
                    return;
                }else {
                    Integer finalTotalWaypointCount = totalWaypointCount;
                    Integer finalTargetWaypointIndex = targetWaypointIndex;
                    Boolean finalOnExecutionUpdate = onExecutionUpdate;
                    Utils.AsyncExecute(()->{
                        try {
                            if (Utils.isNull(activity)){
                                setComputing(false);
                                toLog(" startWaypointMission Activity is Null");
                                callback.onError("Activity is Null");
                                return;
                            }else {
                                if (Utils.isNull(missionStep)){
                                    setComputing(false);
                                    toLog(" startWaypointMission missionStep is Null");
                                    callback.onError("missionStep is Null");
                                    return;
                                }else {
                                    if (Utils.isNull(droneState)){
                                        setComputing(false);
                                        toLog(" startWaypointMission droneState is Null");
                                        callback.onError("droneState is Null");
                                        return;
                                    }else {
                                        if (Utils.isNull(missionSetting)){
                                            setComputing(false);
                                            toLog(" startWaypointMission missionSetting is Null");
                                            callback.onError("missionSetting is Null");
                                            return;
                                        }else {
                                            callback.onStepSucces(missionStep.getSize());
                                            if (!missionStep.hasNext()){
                                                setComputing(false);
                                                toLog(" startWaypointMission missionStep is EMPTY");
                                                // Dont Play Error ON_LOW_BATTERY AND RETURN
                                                // callback.onError("missionSetting is EMPTY");
                                                return;
                                            }else {
                                                if (Utils.isNull(droneState)){
                                                    setComputing(false);
                                                    toLog(" startWaypointMission droneState is Null IS NULL ----------->PLEASE RECONNECT DRONE AND ENABLE TELEMETRIE<-----------");
                                                    callback.onError("missionSetting is Null ----------->PLEASE RECONNECT DRONE AND ENABLE TELEMETRIE<-----------");
                                                    return;
                                                }else {
                                                    if (Utils.isNull(setting)){
                                                        setComputing(false);
                                                        toLog(" startWaypointMission Camera Setting is Null");
                                                        callback.onError("Camera Setting is Null");
                                                        return;
                                                    }else {
                                                        toLog(" startWaypointMission Battery ChargeRemainingInPercent:"+droneState.getChargeRemainingInPercent());
                                                        if (droneState.getChargeRemainingInPercent() < Constants.LOW_BATTERY_LEVEL){
                                                            // PauseMissionAndRechargeBattery + Send Signal
                                                            Runnable goHome = ()->{
                                                                missionStep.clearAll();
                                                                if (missionSetting.autoReturnOnLowBatteryEnable()){
                                                                    callback.mustGoHome();
                                                                }
                                                            };
                                                            if (!missionStep.hasNext()){
                                                                setComputing(false);
                                                                toLog(" startWaypointMission LOW_BATTERY_LEVEL Cannot Save Current State Cause hasNext is FALSE");
                                                                goHome.run();
                                                                callback.onError(" LOW_BATTERY_LEVEL Cannot Save Current State Cause hasNext is FALSE");
                                                                return;
                                                            }else {
                                                                if (finalTotalWaypointCount == 0 && finalTargetWaypointIndex == 0){
                                                                    setComputing(false);
                                                                    toLog(" startWaypointMission LOW_BATTERY_LEVEL No State To Save :totalWaypointCount == 0 && targetWaypointIndex == 0");
                                                                    goHome.run();
                                                                    callback.onError(" LOW_BATTERY_LEVEL No State To Save :totalWaypointCount == 0 && targetWaypointIndex == 0");
                                                                    return;
                                                                }else {
                                                                    if (finalTotalWaypointCount > finalTargetWaypointIndex &&  finalTotalWaypointCount > 0){
                                                                        if (finalTargetWaypointIndex == 0){
                                                                            setComputing(false);
                                                                            // Need retry all Step cause targetWaypointIndex is ZERO and total > 0
                                                                            goHome.run();
                                                                            toLog(" startWaypointMission LOW_BATTERY_LEVEL Need retry all Step cause targetWaypointIndex is ZERO and total > 0");
                                                                            return;
                                                                        }
                                                                        if (finalTargetWaypointIndex > 0){
                                                                            CopyOnWriteArrayList<LatLng> allpoints =missionStep.getFirst();
                                                                            if (Utils.isNull(allpoints)){
                                                                                setComputing(false);
                                                                                toLog(" startWaypointMission LOW_BATTERY_LEVEL allpoints is Null");
                                                                                goHome.run();
                                                                                callback.onError("LOW_BATTERY_LEVEL allpoints is Null");
                                                                                return;
                                                                            }else {
                                                                                if (allpoints.size() == 0){
                                                                                    setComputing(false);
                                                                                    toLog(" startWaypointMission LOW_BATTERY_LEVEL allpoints is Empty");
                                                                                    goHome.run();
                                                                                    callback.onError("LOW_BATTERY_LEVEL allpoints is Empty");
                                                                                    return;
                                                                                }else {
                                                                                    if (allpoints.size() != finalTotalWaypointCount){
                                                                                        setComputing(false);
                                                                                        toLog(" startWaypointMission LOW_BATTERY_LEVEL totalWaypointCount and allpoints are not Same ..must be");
                                                                                        toLog(" startWaypointMission LOW_BATTERY_LEVEL totalWaypointCount:"+finalTotalWaypointCount+" and allpoints "+allpoints.size()+"are not Same ..must be");
                                                                                        goHome.run();
                                                                                        callback.onError("LOW_BATTERY_LEVEL totalWaypointCount and allpoints are not Same ..must be");
                                                                                        return;
                                                                                    }else {
                                                                                        for (int i = 0; i < finalTargetWaypointIndex - 1; i++){
                                                                                            allpoints.remove(i);
                                                                                        }
                                                                                        missionStep.replaceFirst(allpoints);
                                                                                        saveMission(activity, missionStep, missionSetting, new OnMissionProcessingComplete() {
                                                                                            @Override
                                                                                            public void onError() {
                                                                                                setComputing(false);
                                                                                                toLog(" startWaypointMission LOW_BATTERY_LEVEL Save Step after remove Setp Done failed");
                                                                                                goHome.run();
                                                                                                callback.onError("LOW_BATTERY_LEVEL Save Step after remove StepDone failed");
                                                                                                return;
                                                                                            }

                                                                                            @Override
                                                                                            public void onSucces(@NonNull @NotNull MissionStep steps) {
                                                                                                setComputing(false);
                                                                                                toLog(" startWaypointMission LOW_BATTERY_LEVEL Save Succes");
                                                                                                goHome.run();
                                                                                                return;
                                                                                            }
                                                                                        });
                                                                                        return;
                                                                                    }
                                                                                }
                                                                            }

                                                                        }
                                                                    }
                                                                    // Default Case
                                                                    setComputing(false);
                                                                    toLog(" startWaypointMission LOW_BATTERY_LEVEL No Step to Save");
                                                                    goHome.run();
                                                                    callback.onError(" LOW_BATTERY_LEVEL No Step to Save");
                                                                    return;

                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                if (finalOnExecutionUpdate){
                                                    // Stop Here
                                                    setComputing(false);
                                                    return;
                                                }

                                                // Case Continu to Next Step
                                                // Copy to Continu at step End
                                                this.missionStepEnd = missionStep;
                                                this.missionSettingEnd = missionSetting;
                                                this.activityEnd = activity;
                                                this.droneStateEnd =droneState;
                                                this.settingEnd = setting;
                                                saveMission(activity, missionStep,missionSetting, new OnMissionProcessingComplete() {
                                                    @Override
                                                    public void onError() {
                                                        setComputing(false);
                                                        toLog(" generateSurveyPath saveMission failed");
                                                        callback.onError(" Save Mission Before Start Failed");
                                                        return;
                                                    }

                                                    @Override
                                                    public void onSucces(@NonNull MissionStep himSelf) {
                                                        try {
                                                            CopyOnWriteArrayList<LatLng> stepPoints = himSelf.getFirst();
                                                            if (Utils.isNull(stepPoints)){
                                                                setComputing(false);
                                                                toLog(" startWaypointMission step Waypoint is Null");
                                                                callback.onError("step Waypoint is Null");
                                                                return;
                                                            }else {
                                                                if (stepPoints.size() == 0){
                                                                    setComputing(false);
                                                                    toLog(" startWaypointMission step Waypoint is EMPTY");
                                                                    callback.onError("step Waypoint is EMPTY");
                                                                    return;
                                                                }else {
                                                                    // Continu Mission
                                                                    //Ini Mission Setting
                                                                    CopyOnWriteArrayList<Waypoint> waypointList = new CopyOnWriteArrayList<>();
                                                                    for (int i = 0 ; i< stepPoints.size() ; i++){
                                                                        LatLng point = stepPoints.get(i);
                                                                        Waypoint mWaypoint = new Waypoint(point.latitude, point.longitude, missionSetting.getAltitudePrefered());
                                                                        // mWaypoint.shootPhotoTimeInterval = photoEvery; // each 3 seconds
                                                                        // create a take photo each 3 seconds action
                                                                        // Rotate Camera to Specify angle 89,90 .. degrees
//                                                                        int picth = setting.getAngleAsPicth();
//                                                                        WaypointAction takePhoto = new WaypointAction(WaypointActionType.START_TAKE_PHOTO,0);
//                                                                        WaypointAction moveCameraTo90Degrees = new WaypointAction(WaypointActionType.GIMBAL_PITCH, picth);
//                                                                        toLog(" WaypointAction pitch:"+picth);
//                                                                        mWaypoint.addAction(moveCameraTo90Degrees);
//                                                                        mWaypoint.addAction(takePhoto);
                                                                        mWaypoint.shootPhotoTimeInterval = missionSetting.getShootIntervalInSec(); // each 3 seconds
                                                                        waypointList.add(mWaypoint);
                                                                    }
                                                                    toLog("addPoints Succes");
                                                                    toLog("addPoints stepPoints size "+stepPoints.size());
                                                                    toLog("addPoints waypointList size "+waypointList.size());
                                                                    WaypointMissionHeadingMode mHeadingMode = WaypointMissionHeadingMode.AUTO;
                                                                    WaypointMissionFinishedAction mFinishedAction = WaypointMissionFinishedAction.NO_ACTION;
                                                                    waypointMissionOperator = DJISDKManager.getInstance().getMissionControl().getWaypointMissionOperator();
                                                                    waypointMissionOperator.removeListener(eventNotificationListener);
                                                                    waypointMissionOperator.addListener(eventNotificationListener);
                                                                    WaypointMission.Builder waypointMissionBuilder = new WaypointMission.Builder().finishedAction(mFinishedAction)
                                                                            .setGimbalPitchRotationEnabled(false)
                                                                            .headingMode(mHeadingMode)
                                                                            .autoFlightSpeed(missionSetting.getSpeed())
                                                                            .maxFlightSpeed(missionSetting.getSpeed())
                                                                            .waypointList(waypointList)
                                                                            .waypointCount(waypointList.size())
                                                                            .setExitMissionOnRCSignalLostEnabled(missionSetting.isExitMissionOnRCSignalLostEnabled())
                                                                            .flightPathMode(WaypointMissionFlightPathMode.NORMAL);
                                                                    DJIError error = waypointMissionOperator.loadMission(waypointMissionBuilder.build());
                                                                    if (error != null) {
                                                                        setComputing(false);
                                                                        String description =  error.getDescription();
                                                                        if (Utils.isNull(description)){
                                                                            description = "";
                                                                        }
                                                                        String msg = "loadWaypoint failed " +description;
                                                                        toLog(msg);
                                                                        callback.onError(msg);
                                                                        return;
                                                                    } else {
                                                                        toLog("loadWaypoint succeeded");
                                                                        // If startMission Not not Working try uploadWayPointMission first Before startMission
                                                                        Handler handler = new Handler(Looper.getMainLooper());
                                                                        Runnable cancelCallBack = ()->{
                                                                            String msg = "Loading mission failed ----> PLEASE TRY AGAIN <------";
                                                                            setComputing(false);
                                                                            toLog(msg);
                                                                            callback.onError(msg);
                                                                            return;
                                                                        };
                                                                        waypointMissionOperator.uploadMission(uploadErr ->{
                                                                            try {
                                                                                handler.removeCallbacks(cancelCallBack);
                                                                                if (!Utils.isNull(uploadErr)){
                                                                                    setComputing(false);
                                                                                    String description = uploadErr.getDescription();
                                                                                    if (Utils.isNull(description)){
                                                                                        description = "";
                                                                                    }
                                                                                    toLog("uploadErr Happen Detail: " + description);
                                                                                    callback.onError("uploadErr :"+ description);
                                                                                    return;
                                                                                }else {
                                                                                    // We Need to Wait else State Will Stay UPLOADING
                                                                                    toLog("uploading Successfully");
                                                                                    toLog("Now Wait  "+Constants.DELAY+" Sec to Transferts to Drone to be ok");
                                                                                    Utils.runOnUIWithDelay(() ->{
                                                                                        WaypointMissionState current = waypointMissionOperator.getCurrentState();
                                                                                        if (WaypointMissionState.DISCONNECTED.equals(current)) {
                                                                                            setComputing(false);
                                                                                            String msg = "WaypointMissionState DISCONNECTED so Stop";
                                                                                            toLog(msg);
                                                                                            callback.onError(msg);
                                                                                            return;
                                                                                        }else if (WaypointMissionState.EXECUTING.equals(current)){
                                                                                            setComputing(false);
                                                                                            String msg = "WaypointMissionState EXECUTING so Stop";
                                                                                            toLog(msg);
                                                                                            callback.onError(msg);
                                                                                            return;
                                                                                        }else if (WaypointMissionState.EXECUTION_PAUSED.equals(current)){
                                                                                            setComputing(false);
                                                                                            String msg = "WaypointMissionState EXECUTING so Stop";
                                                                                            toLog(msg);
                                                                                            callback.onError(msg);
                                                                                            return;
                                                                                        }else if (WaypointMissionState.NOT_SUPPORTED.equals(current)){
                                                                                            setComputing(false);
                                                                                            String msg = "WaypointMissionState READY_TO_UPLOAD so Stop" ;
                                                                                            toLog(msg);
                                                                                            callback.onError(msg);
                                                                                            return;
                                                                                        }else if (WaypointMissionState.READY_TO_UPLOAD.equals(current)){
                                                                                            setComputing(false);
                                                                                            String msg = "WaypointMissionState READY_TO_UPLOAD so Stop cause already UPLOADED" ;
                                                                                            toLog(msg);
                                                                                            callback.onError(msg);
                                                                                            return;
                                                                                        }else if (WaypointMissionState.RECOVERING.equals(current)){
                                                                                            setComputing(false);
                                                                                            String msg = "WaypointMissionState RECOVERING so Stop ";
                                                                                            toLog(msg);
                                                                                            callback.onError(msg);
                                                                                            return;
                                                                                        }else if (WaypointMissionState.UNKNOWN.equals(current)){
                                                                                            setComputing(false);
                                                                                            String msg = "WaypointMissionState UNKNOWN so Stop ";
                                                                                            toLog(msg);
                                                                                            callback.onError(msg);
                                                                                            return;
                                                                                        }else if (WaypointMissionState.UPLOADING.equals(current)){
                                                                                            setComputing(false);
                                                                                            String msg = "WaypointMissionState UPLOADING so Stop ";
                                                                                            toLog(msg);
                                                                                            callback.onError(msg);
                                                                                            return;
                                                                                        }else if (WaypointMissionState.READY_TO_EXECUTE.equals(current)){
                                                                                            toLog("WaypointMissionState READY_TO_EXECUTE" );
                                                                                            waypointMissionOperator.startMission( startError ->{
                                                                                                        if (!Utils.isNull(startError)){
                                                                                                            setComputing(false);
                                                                                                            String description = startError.getDescription();
                                                                                                            if (Utils.isNull(description)){
                                                                                                                description = "";
                                                                                                            }
                                                                                                            String msg = "WaypointMissionState StartMission failed:"+description;
                                                                                                            toLog(msg);
                                                                                                            callback.onError(msg);
                                                                                                            return;
                                                                                                        }else {
                                                                                                            setComputing(false);
                                                                                                            callback.onStartSucces();
                                                                                                        }
                                                                                                    }
                                                                                            );
                                                                                        }
                                                                                    },activity);
                                                                                }
                                                                            }catch (Exception e){
                                                                                setComputing(false);
                                                                                Utils.toLog(TAG, "uploadMission",null,e);
                                                                                if (Utils.isNull(callback)){
                                                                                    return;
                                                                                }else {
                                                                                    callback.onError("Error");
                                                                                }
                                                                            }catch (Error er){
                                                                                setComputing(false);
                                                                                Utils.toLog(TAG, "uploadMission",er,null);
                                                                                if (Utils.isNull(callback)){
                                                                                    return;
                                                                                }else {
                                                                                    callback.onError("Error");
                                                                                }
                                                                            }
                                                                        });

                                                                        handler.postDelayed(cancelCallBack,TimeUnit.SECONDS.toMillis(Constants.DELAY_47_SECONDS));
                                                                    }
                                                                }
                                                            }
                                                        }catch (Exception e){
                                                            setComputing(false);
                                                            Utils.toLog(TAG, "saveMission onSucces",null,e);
                                                            if (Utils.isNull(callback)){
                                                                return;
                                                            }else {
                                                                callback.onError("Error");
                                                            }
                                                        }catch (Error er){
                                                            setComputing(false);
                                                            Utils.toLog(TAG, "saveMission onSucces",er,null);
                                                            if (Utils.isNull(callback)){
                                                                return;
                                                            }else {
                                                                callback.onError("Error");
                                                            }
                                                        }
                                                    }
                                                });

                                            }
                                        }
                                    }
                                }
                            }
                        }catch (Exception e){
                            setComputing(false);
                            Utils.toLog(TAG, "shouldCancelOrContinu",null,e);
                            if (Utils.isNull(callback)){
                                return;
                            }else {
                                callback.onError("Error");
                            }
                        }catch (Error er){
                            setComputing(false);
                            Utils.toLog(TAG, "shouldCancelOrContinu",er,null);
                            if (Utils.isNull(callback)){
                                return;
                            }else {
                                callback.onError("Error");
                            }
                        }
                    });

                }
            }
        }catch (Exception e){
            setComputing(false);
            Utils.toLog(TAG, "shouldCancelOrContinu",null,e);
        }catch (Error er){
            setComputing(false);
            Utils.toLog(TAG, "shouldCancelOrContinu",er,null);
        }
    }
    private void toLog( @NonNull String msg){
        Utils.toLog(TAG, msg);
    }

    // Phantom 4 TXT_senswidth =4.7
    //           TXT_sensheight= 6.3
    //           NUM_focallength= 20
    public static @Nullable GridSetting calcGridSetting(@NonNull CameraParameter params) throws Exception,Error
    {
        try
        {
            if (Utils.isNull(params)){
                return null;
            }else {
                if (!params.isValid()){
                    return null;
                }else {
                    float flyalt = (float) fromDistDisplayUnit((float)params.getAltitudeForGrid());

                    int overlap = (int)params.getOverlap();
                    int sidelap = (int)params.getSidelap();

                    double viewwidth = 0;
                    double viewheight = 0;

                    // Start getFOV
                    double focallen = (double)params.getFocalLengthInMM();
                    double sensorwidth = params.getSensorWidth();
                    double sensorheight = params.getSensorHeight();

                    // scale      mm / mm
                    double flscale = (1000 * flyalt) / focallen;

                    //   mm * mm / 1000
                    double viewwidthgetFOV = (sensorwidth * flscale / 1000);
                    double viewheightgetFOV = (sensorheight * flscale / 1000);

                    viewwidth = viewwidthgetFOV;
                    viewheight = viewheightgetFOV;
                    // End getFOV
                    double spacingValue = (double)((1 - (overlap / 100.0f)) * viewheight);
                    double distanceValue = (double)((1 - (sidelap / 100.0f)) * viewwidth);

                    GridSetting res = new GridSetting();
                    res.setSpacing(spacingValue);
                    res.setDistance(distanceValue);
                    res.setAltitude(params.getAltitudeForGrid());
                    res.setAngle(params.getAngleForGrid());
                    if (!res.isValid()){
                        return null;
                    }else {
                        return res;
                    }

                }
            }
        }
        catch (Exception e){
            Utils.toLog("MissionDJI", "calcGridSetting",null,e);

        }catch (Error er){
            Utils.toLog("MissionDJI", "v",er,null);

        }
        return null;
    }

    public static double fromDistDisplayUnit(double input)
    {
        return input / Utils.clone(multiplierdist);
    }
    public static float multiplierdist = 1;
    public static float multiplierspeed = 1;


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void saveMission(@NonNull final Context ctx, @NonNull MissionStep steps,@NonNull MissionSetting setting, final @NonNull OnMissionProcessingComplete callback){
        Utils.AsyncExecute(()->{
            if (Utils.isNull(callback)){
                toLog(" saveMission callback is null");
                return;
            }else {
                try {
                    if (Utils.isNull(steps)){
                        toLog(" saveMission step is null");
                        callback.onError();
                        return;
                    }else {
                        if (Utils.isNull(setting)){
                            toLog(" saveMission setting is null");
                            callback.onError();
                            return;
                        }else {
                            if (Utils.isNull(ctx)){
                                toLog(" saveMission Context is null");
                                callback.onError();
                                return;
                            }else {
                                // Clean old Save
                                resetMission(ctx, new OnAsyncOperationComplete() {
                                    @Override
                                    public void onError(@Nullable  String errorDetail) {
                                        toLog(" saveMission reset before Error happen");
                                        callback.onError();
                                        return;
                                    }

                                    @Override
                                    public void onSucces(@Nullable String succesMsg) {
                                        Utils.AsyncExecute(()->{
                                            try {
                                                if (steps.getSize() == 0){
                                                    toLog(" saveMission Step is empty");
                                                    callback.onError();
                                                    return;
                                                }else {
                                                    String parameterStr = setting.toStringJSON();
                                                    if (Utils.isNullOrEmpty(parameterStr)){
                                                        toLog(" saveMission parameterStr is Null or empty");
                                                        callback.onError();
                                                        return;
                                                    }else {
                                                        steps.toStringJSON(new OnAsyncOperationComplete() {
                                                            @Override
                                                            public void onError(@Nullable String errorDetail) {
                                                                toLog(" saveMission convert Mission List to String Failed");
                                                                callback.onError();
                                                                return;
                                                            }

                                                            @Override
                                                            public void onSucces(@Nullable String value) {
                                                                try {
                                                                    if (Utils.isNullOrEmpty(value)){
                                                                        toLog(" saveMission convert Mission List value is Null or Empty");
                                                                        callback.onError();
                                                                        return;
                                                                    }else {
                                                                        boolean isSaved = Save.defaultSaveString(Constants.PREF_WAYPOINT_MISSION, value, ctx);
                                                                        boolean isSavedParameter = Save.defaultSaveString(Constants.PREF_WAYPOINT_PARAMETER, parameterStr, ctx);
                                                                        if (!isSaved){
                                                                            toLog(" saveMission defaultSaveString Failed");
                                                                            callback.onError();
                                                                            return;
                                                                        }else {
                                                                            if (!isSavedParameter){
                                                                                toLog(" saveMission SaveString for parameter Failed");
                                                                                callback.onError();
                                                                                return;
                                                                            }else {
                                                                                toLog(" saveMission SaveString for parameter Succes");
                                                                                callback.onSucces(steps);
                                                                            }
                                                                        }
                                                                    }
                                                                }catch (Exception e){
                                                                    Utils.toLog(TAG, "toStringJSON onSucces",null,e);
                                                                }catch (Error er){
                                                                    Utils.toLog(TAG, "toStringJSON onSucces",er,null);
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            }catch (Exception e){
                                                Utils.toLog(TAG, "saveMission reset before",null,e);
                                                if (Utils.isNull(callback)){
                                                    return;
                                                }else {
                                                    callback.onError();
                                                }
                                            }catch (Error er){
                                                Utils.toLog(TAG, "saveMission reset before",er,null);
                                                if (Utils.isNull(callback)){
                                                    return;
                                                }else {
                                                    callback.onError();
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        }

                    }
                }catch (Exception e){
                    Utils.toLog(TAG, "saveMission",null,e);
                    if (Utils.isNull(callback)){
                        return;
                    }else {
                        callback.onError();
                    }
                }catch (Error er){
                    Utils.toLog(TAG, "saveMission",er,null);
                    if (Utils.isNull(callback)){
                        return;
                    }else {
                        callback.onError();
                    }
                }
            }

        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void loadMission(@NonNull final Context ctx,final @NonNull OnMissionProcessingComplete callback){
        Utils.AsyncExecute(()->{
            if (Utils.isNull(callback)){
                toLog(" loadMission callback is null");
                return;
            }else {
                try {
                    if (Utils.isNull(ctx)){
                        toLog(" loadMission Context is null");
                        callback.onError();
                        return;
                    }else {
                        String data = Save.defaultLoadString(Constants.PREF_WAYPOINT_MISSION,ctx);
                        if (Utils.isNullOrEmpty(data)){
                            toLog(" loadMission Data Saved is null or Empty");
                            callback.onError();
                            return;
                        }else {
                            MissionStep steps = new MissionStep();
                            steps.fillFromString(data, new OnMissionProcessingComplete() {
                                @Override
                                public void onError() {
                                    toLog(" loadMission fillFromString Error Happen");
                                    callback.onError();
                                    return;
                                }

                                @Override
                                public void onSucces(@NonNull final MissionStep himSelf) {
                                    if (Utils.isNull(himSelf)){
                                        toLog(" loadMission After Fill himSelf is Null");
                                        callback.onError();
                                        return;
                                    }else {
                                        callback.onSucces(himSelf);
                                    }
                                }
                            });
                        }
                    }
                }catch (Exception e){
                    Utils.toLog(TAG, "loadMission",null,e);
                    if (Utils.isNull(callback)){
                        return;
                    }else {
                        callback.onError();
                    }
                }catch (Error er){
                    Utils.toLog(TAG, "loadMission",er,null);
                    if (Utils.isNull(callback)){
                        return;
                    }else {
                        callback.onError();
                    }
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void resetMission(@NonNull final Context ctx,final @NonNull OnAsyncOperationComplete callback) {
        Utils.AsyncExecute(()->{
            if (Utils.isNull(callback)){
                toLog(" resetMission callback is null");
                return;
            }else {
                try {
                    if (Utils.isNull(ctx)){
                        toLog(" resetMission Context is null");
                        callback.onError("");
                        return;
                    }else {
                        boolean isDone = Save.defaultSaveString(Constants.PREF_WAYPOINT_MISSION,"",ctx);
                        boolean isDoneParameter = Save.defaultSaveString(Constants.PREF_WAYPOINT_PARAMETER,"",ctx);
                        if (!isDone){
                            toLog(" resetMission defaultSaveString PREF_WAYPOINT_MISSION Failed");
                            callback.onError("");
                        }else {
                            if (!isDoneParameter){
                                toLog(" resetMission defaultSaveString PREF_WAYPOINT_PARAMETER Failed");
                                callback.onError("");
                                return;
                            }else {
                                toLog(" resetMission CLEANING Succes");
                                callback.onSucces("");
                            }
                        }

                    }
                }catch (Exception e){
                    Utils.toLog(TAG, "resetMission",null,e);
                    if (Utils.isNull(callback)){
                        return;
                    }else {
                        callback.onError("");
                    }
                }catch (Error er){
                    Utils.toLog(TAG, "resetMission",er,null);
                    if (Utils.isNull(callback)){
                        return;
                    }else {
                        callback.onError("");
                    }
                }
            }

        });
    }


    public void cancelCurrentCauseGoHome(OnAsyncOperationComplete callBack) {
        synchronized (this){
            try {
                setComputing(false);
                if (Utils.isNull(callBack)){
                    return;
                }else {
                    if (Utils.isNull(waypointMissionOperator)){
                        // skip
                        toLog("cancelCurrentCauseGoHome waypointMissionOperator is Null");
                        callBack.onSucces("");
                        return;
                    }else {
                        waypointMissionOperator.stopMission((DJIError error) -> {
                            waypointMissionOperator.clearMission();
                            waypointMissionOperator.removeListener(eventNotificationListener);
                            waypointMissionOperator.destroy();
                            if (!Utils.isNull(error)){
                                String description = error.getDescription();
                                if (Utils.isNull(description)){
                                    description = "";
                                }
                                toLog("cancelCurrentCauseGoHome stopMission error: "+description);
                            }
                            if (Utils.isNull(missionStepEnd)){
                                // skip
                                toLog("cancelCurrentCauseGoHome missionStepEnd is Null");
                                // Continu If Never started missionStepEnd is Null
                                callBack.onSucces("");
                                return;
                            }else {
                                missionStepEnd.clearAll();
                                callBack.onSucces("");
                                return;
                            }

                        });
                    }

                }
            }catch (Exception e){
                Utils.toLog(TAG, "cancelCurrentCauseGoHome",null,e);
            }catch (Error er){
                Utils.toLog(TAG, "cancelCurrentCauseGoHome",er,null);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void continuMission(@NonNull final Activity activity,final @NonNull DroneState droneState, final @NonNull OnMissionRunningListener callback) {
        setComputing(false);
        try {
            if (Utils.isNull(callback)){
                toLog(" continuMission callback is null");
                return;
            }else {
                if (Utils.isNull(activity)){
                    toLog("continuMission activity is Null");
                    callback.onError("continuMission activity is Null");
                    return;
                }else {
                    if (Utils.isNull(droneState)){
                        toLog("continuMission droneState is Null");
                        callback.onError("continuMission droneState is Null");
                        return;
                    }else {
                        loadMission(activity, new OnMissionProcessingComplete() {
                            @Override
                            public void onError() {
                                toLog("continuMission load existing failed");
                                callback.onError("continuMission load existing failed");
                                return;
                            }

                            @Override
                            public void onSucces(@NonNull MissionStep steps) {
                                if (Utils.isNull(steps)){
                                    toLog("continuMission steps is Null");
                                    callback.onError("continuMission Cannot load Saved steps: Null");
                                    return;
                                }else {
                                    if (!steps.hasNext()){
                                        toLog("continuMission steps is EMPTY");
                                        callback.onError("continuMission Cannot load Saved steps: EMPTY");
                                        return;
                                    }else {
                                        Utils.AsyncExecute(()->{
                                            try {
                                                String data = Save.defaultLoadString(Constants.PREF_WAYPOINT_PARAMETER,activity);
                                                if (Utils.isNullOrEmpty(data)){
                                                    toLog("continuMission data is EMPTY or data is Nulll");
                                                    callback.onError("continuMission Cannot load Saved steps: Parameters is empty or Null");
                                                    return;
                                                }else {
                                                    MissionSetting setting = new MissionSetting(data);
                                                    if (!setting.isValid(true)){
                                                        toLog("continuMission Remove Me : "+data);
                                                        toLog("continuMission MissionSetting is not Valid Data: "+setting.getErrorMsg() );
                                                        callback.onError("continuMission Cannot load Saved steps: Parameters is Not valid:"+setting.getErrorMsg());
                                                        return;
                                                    }else {
                                                        callback.onNotifyState(setting.getBattery(),setting.getImageCount(),setting.getSpeed(),setting.getFlyPoints());
                                                        CameraParameter params = CameraParameter.getDJIPhantom4Advanced(setting.getAltitudePrefered());
                                                        if (Utils.isNull(params)){
                                                            toLog(" continuMission params is Null");
                                                            callback.onError(" continuMission homeLocation is BAD");
                                                            return;
                                                        }else {
                                                            GridSetting gridSetting = MissionDJI.calcGridSetting(params);
                                                            if (Utils.isNull(gridSetting)){
                                                                toLog(" continuMission gridSetting is Null");
                                                                callback.onError(" continuMission gridSetting is Null");
                                                                return;
                                                            }else {
                                                                toLog(" continuMission Will Play: shouldCancelOrContinu");
                                                                shouldCancelOrContinu(null,setting,steps,activity,droneState,gridSetting,null,null,callback);
                                                            }
                                                        }
                                                    }
                                                }
                                            }catch (Exception e){
                                                Utils.toLog(TAG, "continuMission",null,e);
                                                if (Utils.isNull(callback)){
                                                    return;
                                                }else {
                                                    callback.onError("");
                                                }
                                            }catch (Error er){
                                                Utils.toLog(TAG, "continuMission",er,null);
                                                if (Utils.isNull(callback)){
                                                    return;
                                                }else {
                                                    callback.onError("");
                                                }
                                            }
                                        });

                                    }
                                }
                            }
                        });
                    }
                }
            }
        }catch (Exception e){
            Utils.toLog(TAG, "continuMission",null,e);
        }catch (Error er){
            Utils.toLog(TAG, "continuMission",er,null);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void isMappingMissionExist(final Context ctx, OnAsyncOperationCompleteBool callback) {
        if (Utils.isNull(callback)){
            toLog(" isMappingMissionExist callback is null");
            return;
        }else {
            if (Utils.isNull(ctx)){
                toLog("isMappingMissionExist ctx is Null");
                callback.onResultNo("isMappingMissionExist ctx is Null");
                return;
            }else {
                loadMission(ctx, new OnMissionProcessingComplete() {
                    @Override
                    public void onError() {
                        toLog("isMappingMissionExist load existing failed");
                        callback.onResultNo("isMappingMissionExist load existing failed");
                        return;
                    }

                    @Override
                    public void onSucces(@NonNull MissionStep steps) {
                        if (Utils.isNull(steps)){
                            toLog("isMappingMissionExist steps is Null");
                            callback.onResultNo("isMappingMissionExist Cannot load Saved steps: Null");
                            return;
                        }else {
                            if (!steps.hasNext()){
                                toLog("isMappingMissionExistisMappingMissionExist steps is EMPTY");
                                callback.onResultNo("isMappingMissionExist Cannot load Saved steps: EMPTY");
                                return;
                            }else {
                                Utils.AsyncExecute(()->{
                                    try {
                                        String data = Save.defaultLoadString(Constants.PREF_WAYPOINT_PARAMETER,ctx);
                                        if (Utils.isNullOrEmpty(data)){
                                            toLog("isMappingMissionExist data is EMPTY or data");
                                            callback.onResultNo("isMappingMissionExist Cannot load Saved steps: Parameters is empty or Null");
                                            return;
                                        }else {
                                            MissionSetting setting = new MissionSetting(data);
                                            if (!setting.isValid(null)){
                                                toLog("isMappingMissionExist data is EMPTY or data");
                                                callback.onResultNo("isMappingMissionExist Cannot load Saved steps: Parameters is Not valid");
                                                return;
                                            }else {
                                                callback.onResultYes();
                                                return;
                                            }
                                        }
                                    }catch (Exception e){
                                        Utils.toLog(TAG, "isMappingMissionExist",null,e);
                                        if (Utils.isNull(callback)){
                                            return;
                                        }else {
                                            callback.onResultNo("");
                                        }
                                    }catch (Error er){
                                        Utils.toLog(TAG, "isMappingMissionExist",er,null);
                                        if (Utils.isNull(callback)){
                                            return;
                                        }else {
                                            callback.onResultNo("");
                                        }
                                    }
                                });

                            }
                        }
                    }
                });
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void cleanUserData(final Context ctx, OnAsyncOperationCompleteBool callback) {
        if (Utils.isNull(callback)){
            toLog(" cleanUserData callback is null");
            return;
        }else {
            if (Utils.isNull(ctx)){
                toLog("cleanUserData ctx is Null");
                callback.onResultNo("cleanUserData ctx is Null");
                return;
            }else {
                resetMission(ctx, new OnAsyncOperationComplete() {
                    @Override
                    public void onError(@Nullable String errorDetail) {
                        if (Utils.isNullOrEmpty(errorDetail)){
                            errorDetail = "";
                        }
                        callback.onResultNo(errorDetail);
                    }

                    @Override
                    public void onSucces(@Nullable String succesMsg) {
                        callback.onResultYes();
                    }
                });
            }
        }

    }
}
