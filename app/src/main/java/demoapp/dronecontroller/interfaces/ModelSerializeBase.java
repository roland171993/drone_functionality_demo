package demoapp.dronecontroller.interfaces;

import androidx.annotation.Nullable;

import org.json.JSONObject;

public interface ModelSerializeBase {
    boolean isValid();
    @Nullable String toStringJSON();
    @Nullable JSONObject toJSON();
    @Nullable Object clone();
}
