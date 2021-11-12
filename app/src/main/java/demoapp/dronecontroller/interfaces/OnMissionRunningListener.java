package demoapp.dronecontroller.interfaces;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.util.concurrent.CopyOnWriteArrayList;

public interface OnMissionRunningListener {
    void onError(@Nullable String errorDetail);
    void onCalibrationRequire(@Nullable String detail);
    void onStartSucces();
    void onStepSucces(int stepLeft);
    void onSucces();
    void onGridProgressing(int progress);
    void onNotifyState(int battery,int imageCount,float speed,CopyOnWriteArrayList<LatLng> flyPoints);
    void mustGoHome(); // Case Mission Finish Or Low Battery
}
