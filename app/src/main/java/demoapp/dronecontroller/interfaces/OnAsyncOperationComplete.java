package demoapp.dronecontroller.interfaces;

import androidx.annotation.Nullable;

public interface OnAsyncOperationComplete {
    void onError(@Nullable String errorDetail);
    void onSucces(@Nullable String succesMsg);
}
