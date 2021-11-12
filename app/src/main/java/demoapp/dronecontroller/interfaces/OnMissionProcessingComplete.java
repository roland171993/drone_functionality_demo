package demoapp.dronecontroller.interfaces;

import androidx.annotation.NonNull;

import demoapp.mission.MissionStep;


public interface OnMissionProcessingComplete {
    void onError();
    void onSucces(@NonNull final MissionStep steps);
}
