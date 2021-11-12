package demoapp.dronecontroller.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Obrina.KIMI on 11/23/2017.
 */

public class PermissionUtil {
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    boolean hasRequest = false;
    private String TAG = getClass().getSimpleName();

    private static boolean isPermissionNeed = false;


    public PermissionUtil(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("PERMISSION_PREFERENCE", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        isPermissionNeed = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }


    private  void requestGroupPermission(CopyOnWriteArrayList<String> permissions, Activity activity)
    {
        try{
            if(isPermissionNeed){
                if(permissions.size()>0){
                    String[] permissionList = new String[permissions.size()];
                    permissions.toArray(permissionList);
                    ActivityCompat.requestPermissions(activity, permissionList, Constants.REQUEST_GROUP_PERMISSION);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public CopyOnWriteArrayList<String> getAllPermissionsDenied(){
        CopyOnWriteArrayList<String> permissionNeeded = new CopyOnWriteArrayList<>();
        if (!hasRequest){
            try{
                if(isPermissionNeed){
                    CopyOnWriteArrayList<String> permissionAvailable = new CopyOnWriteArrayList<>();
                    permissionAvailable.add(Manifest.permission.BLUETOOTH);
                    permissionAvailable.add(Manifest.permission.BLUETOOTH_ADMIN);
                    permissionAvailable.add(Manifest.permission.VIBRATE);
                    permissionAvailable.add(Manifest.permission.INTERNET);
                    permissionAvailable.add(Manifest.permission.ACCESS_WIFI_STATE);
                    permissionAvailable.add(Manifest.permission.WAKE_LOCK);
                    permissionAvailable.add(Manifest.permission.ACCESS_COARSE_LOCATION);
                    permissionAvailable.add(Manifest.permission.ACCESS_NETWORK_STATE);
                    permissionAvailable.add(Manifest.permission.ACCESS_FINE_LOCATION);
                    permissionAvailable.add(Manifest.permission.CHANGE_WIFI_STATE);
                    permissionAvailable.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    permissionAvailable.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                    permissionAvailable.add(Manifest.permission.SYSTEM_ALERT_WINDOW);
                    permissionAvailable.add(Manifest.permission.READ_PHONE_STATE);
                    permissionAvailable.add(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);
                    permissionAvailable.add(Manifest.permission.CHANGE_CONFIGURATION);
                    permissionAvailable.add(Manifest.permission.WRITE_SETTINGS);

                    for(String permission: permissionAvailable){
                        if(ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                            permissionNeeded.add(permission);
                    }
                    return permissionNeeded;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return permissionNeeded;
    }


    public void requestAllPermissions(){
        if (!hasRequest){
            try{
                if(isPermissionNeed){
                    CopyOnWriteArrayList<String> permissionNeeded = new CopyOnWriteArrayList<>();
                    CopyOnWriteArrayList<String> permissionAvailable = new CopyOnWriteArrayList<>();
                    permissionAvailable.add(Manifest.permission.BLUETOOTH);
                    permissionAvailable.add(Manifest.permission.BLUETOOTH_ADMIN);
                    permissionAvailable.add(Manifest.permission.VIBRATE);
                    permissionAvailable.add(Manifest.permission.INTERNET);
                    permissionAvailable.add(Manifest.permission.ACCESS_WIFI_STATE);
                    permissionAvailable.add(Manifest.permission.WAKE_LOCK);
                    permissionAvailable.add(Manifest.permission.ACCESS_COARSE_LOCATION);
                    permissionAvailable.add(Manifest.permission.ACCESS_NETWORK_STATE);
                    permissionAvailable.add(Manifest.permission.ACCESS_FINE_LOCATION);
                    permissionAvailable.add(Manifest.permission.CHANGE_WIFI_STATE);
                    permissionAvailable.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    permissionAvailable.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                    permissionAvailable.add(Manifest.permission.SYSTEM_ALERT_WINDOW);
                    permissionAvailable.add(Manifest.permission.READ_PHONE_STATE);
                    permissionAvailable.add(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);
                    permissionAvailable.add(Manifest.permission.CHANGE_CONFIGURATION);
                    permissionAvailable.add(Manifest.permission.WRITE_SETTINGS);

                    for(String permission: permissionAvailable){
                        if(ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                            permissionNeeded.add(permission);
                    }
                    if(permissionNeeded.size() > 0){
                        requestGroupPermission(permissionNeeded, (Activity) context);
                        hasRequest = true;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }


    public boolean isPermissionsGranted(){
        CopyOnWriteArrayList<String> permissionAvailable = new CopyOnWriteArrayList<>();
        permissionAvailable.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissionAvailable.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionAvailable.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissionAvailable.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionAvailable.add(Manifest.permission.READ_PHONE_STATE);
        permissionAvailable.add(Manifest.permission.VIBRATE);
        for(String permission: permissionAvailable){
            if(ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    public @Nullable CopyOnWriteArrayList<String> getPermissionsDenied(){
        try{
            CopyOnWriteArrayList<String> permissionAvailable = new CopyOnWriteArrayList<>();
            CopyOnWriteArrayList<String> permissionDenied = new CopyOnWriteArrayList<>();

            permissionAvailable.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            permissionAvailable.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            permissionAvailable.add(Manifest.permission.ACCESS_FINE_LOCATION);
            permissionAvailable.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            permissionAvailable.add(Manifest.permission.READ_PHONE_STATE);
            permissionAvailable.add(Manifest.permission.VIBRATE);
            for(String permission: permissionAvailable){
                if(ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                    permissionDenied.add(permission);
            }
            return permissionDenied;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }



    public boolean isAllPermissionsGranded(boolean skipStatic){
        try{
            if (skipStatic){
                isPermissionNeed = true;
            }
            if(isPermissionNeed){
                CopyOnWriteArrayList<String> permissionNeeded = new CopyOnWriteArrayList<>();
                CopyOnWriteArrayList<String> permissionAvailable = new CopyOnWriteArrayList<>();

                permissionAvailable.add(Manifest.permission.BLUETOOTH);
                permissionAvailable.add(Manifest.permission.BLUETOOTH_ADMIN);
                permissionAvailable.add(Manifest.permission.VIBRATE);
                permissionAvailable.add(Manifest.permission.INTERNET);
                permissionAvailable.add(Manifest.permission.ACCESS_WIFI_STATE);
                permissionAvailable.add(Manifest.permission.WAKE_LOCK);
                permissionAvailable.add(Manifest.permission.ACCESS_COARSE_LOCATION);
                permissionAvailable.add(Manifest.permission.ACCESS_NETWORK_STATE);
                permissionAvailable.add(Manifest.permission.ACCESS_FINE_LOCATION);
                permissionAvailable.add(Manifest.permission.CHANGE_WIFI_STATE);
                permissionAvailable.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                permissionAvailable.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                permissionAvailable.add(Manifest.permission.READ_PHONE_STATE);
                for(String permission: permissionAvailable){
                    if(ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED){
                        permissionNeeded.add(permission);

                        Utils.toLog(TAG, "isAllPermissionsGranded Permission not granted "+ Utils.clone(permission));
                    }

                }
                if(permissionNeeded.size() > 0){
                    return false;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return true;
    }



}
