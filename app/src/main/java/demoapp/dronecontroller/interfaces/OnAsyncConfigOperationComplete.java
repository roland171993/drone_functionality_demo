package demoapp.dronecontroller.interfaces;

import androidx.annotation.Nullable;

public interface OnAsyncConfigOperationComplete {
    void onError(@Nullable String errorDetail);
    void onSucces(@Nullable String succesMsg);
    void onSucces(@Nullable int value);
    void onSucces(@Nullable long value);
    void onSucces(@Nullable float value);
    void onSucces(@Nullable double value);
}
