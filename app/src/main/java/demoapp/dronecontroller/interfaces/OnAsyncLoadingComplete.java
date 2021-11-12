package demoapp.dronecontroller.interfaces;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

public interface OnAsyncLoadingComplete  {
    void onError(@Nullable String errorDetail);
    void onSucces(@Nullable JSONArray value);
    void onSucces(@Nullable JSONObject value);
}
