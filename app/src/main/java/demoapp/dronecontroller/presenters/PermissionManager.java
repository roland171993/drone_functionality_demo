package demoapp.dronecontroller.presenters;

import android.app.Activity;
import android.content.Context;
import android.os.PowerManager;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import demoapp.dronecontroller.utils.PermissionUtil;
import demoapp.dronecontroller.utils.Utils;

import static android.content.Context.POWER_SERVICE;

public class PermissionManager {
  private @Nullable
  PermissionUtil pm =null;
  public static boolean isAllPermissionGranted = false;
  private PowerManager.WakeLock wakeLock = null;
  private String TAG = getClass().getSimpleName();

  public PermissionManager(@NonNull Context ctx){
    if (Utils.isNull(ctx)){
      return;
    }else{
      this.pm = new PermissionUtil(ctx);
    }
  }


  public boolean isSDKreadyForMappingProcess(){
    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N){
      return false;
    }else {
      return true;
    }
  }
  public boolean isPermissionsGranted(){
    if (Utils.isNull(this.pm)){
      return false;
    }else {
      isAllPermissionGranted = this.pm.isAllPermissionsGranded(false);
      return isAllPermissionGranted;
    }

  }

  public void keepOnWithScreen(@NonNull Activity activity) {
    try {
      if (Utils.isNull(activity)){
        return;
      }else {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        keepOn(activity);
      }


    }catch (Exception e){
      e.printStackTrace();
    }catch (Error er){
      er.printStackTrace();
    }
  }
  public void keepOn(@NonNull Context ctx) {
    try {
      if (Utils.isNull(ctx)){
        return;
      }else {
        PowerManager powerManager = (PowerManager) ctx.getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
          TAG);
        if (wakeLock != null){
          if (!wakeLock.isHeld()){
            wakeLock.acquire();
          }
        }
      }

    }catch (Exception e){
      e.printStackTrace();
    }catch (Error er){
      er.printStackTrace();
    }
  }

  public void removeKeepOnWithScreen(@NonNull Activity activity) {
    try {
      if (Utils.isNull(activity)){
        return;
      }else {
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        removeKeepOn(activity);
      }

    }catch (Exception e){
      Utils.toLog(TAG, "removeKeepOnWithScreen",null,e);
    }catch (Error er){
      Utils.toLog(TAG, "removeKeepOnWithScreen",er,null);
    }
  }

  public void removeKeepOn(@NonNull Context ctx) {
    try {
      if (Utils.isNull(ctx)){
        return;
      }else {
        if(wakeLock != null){
          wakeLock.release();
        }
      }

    }catch (Exception e){
      e.printStackTrace();
    }catch (Error er){
      er.printStackTrace();
    }
  }

  public void requestAllPermissions(){
    if (Utils.isNull(this.pm)){
      return;
    }else {
      this.pm.requestAllPermissions();
    }
  }

}
