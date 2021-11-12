package demoapp.dronecontroller.interfaces;

import androidx.annotation.Nullable;

import org.json.JSONArray;

public interface OnAsyncJSONArrayBuildDone {
    void onError();
    void onSucces(@Nullable JSONArray list);
}
