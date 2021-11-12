package demoapp.dronecontroller.interfaces;

import androidx.annotation.Nullable;

public interface OnAsyncOperationCompleteBool {
    void onResultNo(@Nullable String errorDetail);
    void onResultYes();
}
