package demoapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.dji.sdk.sample.R;

import demoapp.mapping.Utils;
import demoapp.dronecontroller.MappingApplication;

import dji.common.camera.SettingsDefinitions;
import dji.common.error.DJIError;
import dji.common.gimbal.Rotation;
import dji.common.gimbal.RotationMode;
import dji.sdk.base.BaseProduct;
import dji.sdk.camera.Camera;
import dji.sdk.flightcontroller.FlightController;
import dji.sdk.gimbal.Gimbal;
import dji.sdk.products.Aircraft;
import dji.sdk.sdkmanager.DJISDKManager;

public class GimbalActivity extends AppCompatActivity {
    private static final String TAG = GimbalActivity.class.getName();
    private FlightController mFlightController;
    private Aircraft mProduct = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gimbal);


        toLogUI(" initFlightController runOnUiThread Start");
        BaseProduct baseProduct = MappingApplication.getProductInstance();
        DJISDKManager dji = DJISDKManager.getInstance();

        toLogUI(" initFlightController runOnUiThread After Get Instance");
        if (Utils.isNull(baseProduct) || Utils.isNull(dji)){
            toLogUI(" baseProduct or  dji IS NULL");
            return;
        }else {
            if (!Utils.isNull(mProduct)){
                mProduct = null;
            }
            mProduct = ((Aircraft) baseProduct);
            if (mProduct == null || !mProduct.isConnected()) {
                mFlightController = null;
                toLogUI(" mProduct Is Null or No Connected");
                return;
            }
            mFlightController = mProduct.getFlightController();
            if (Utils.isNull(mFlightController)){
                toLogUI(" mFlightController Is Null or No Connected");
                return;
            }else {
                Camera camera = mProduct.getCamera();
                if (Utils.isNull(camera)){
                    toLogUI(" camera Is Null");
                    return;
                }
                camera.setPhotoFileFormat(SettingsDefinitions.PhotoFileFormat.JPEG, (DJIError cameraError) -> {
                    if (!Utils.isNull(cameraError)){
                        String detail = cameraError.getDescription();
                        if (Utils.isNull(detail)){
                            detail = "";
                        }
                        toLogUI(" camera setPhotoFileFormat Error Happen: "+detail);
                        return;
                    }else {
                        toLogUI(" setPhotoFileFormat Succes");
                        Gimbal gimbal = mProduct.getGimbal();
                        if (Utils.isNull(gimbal)){
                            toLogUI(" gimbal Is Null");
                            return;
                        }else {
                            gimbal.rotate(new Rotation.Builder()
                                    .pitch(-89)
                                    .mode(RotationMode.SPEED)
                                    .yaw(Rotation.NO_ROTATION)
                                    .roll(Rotation.NO_ROTATION)
                                    .time(0)
                                    .build(), rotateError -> {
                                if (!Utils.isNull(rotateError)){
                                    String detail = rotateError.getDescription();
                                    if (Utils.isNull(detail)){
                                        detail = "";
                                    }
                                    toLogUI(" rotateError Error Happen: "+detail);
                                    return;
                                }else {
                                    toLogUI(" rotate Succes");
                                }
                            });
                        }


                    }
                });

            }
        }
    }


    private void generatePath(){

    }

    public void toLogUI(String msg){
        if (msg ==  null){
            Log.d(TAG, " toLogUI msg IS NULL");
            return;
        }else{
            Log.d(TAG, " toLogUI "+msg);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LinearLayoutCompat uiContainer =  (LinearLayoutCompat) findViewById(R.id.containerId);
                    TextView t = new TextView(GimbalActivity.this);
                    t.setText(msg);
                    uiContainer.addView(t);
                }
            });
        }
    }
}