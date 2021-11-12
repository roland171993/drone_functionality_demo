package demoapp.dronecontroller.interfaces;

import androidx.annotation.Nullable;

public interface OnGPSStateChange {
    void onGPSDisable();
    void onGPSEnable();
    void onPermissionNotGranted();
    void onUserPositionChanged(@Nullable Double uLatitude, @Nullable Double uLongitude);
}
