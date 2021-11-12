package demoapp.mapping;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import demoapp.MapActivity;
import demoapp.dronecontroller.utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class Utils {
    private static String TAG = "Utils";
    private static Object object = new Object();


    public static boolean isNull(Object obj){
        return obj == null;
    }
    public static boolean isNullOrEmpty(String str){
        if (isNull(str)){
            return true;
        }else{
            return str.trim().contentEquals("");
        }
    }
    public static void showToast(@NonNull Context ctx, String txt){
        if (Utils.isNull(ctx)){
            return;
        }else {
            if (Utils.isNullOrEmpty(txt)){
                return;
            }else {
                Toast.makeText(ctx, txt, Toast.LENGTH_LONG).show();
            }
        }
    }

    public static long getCurrentTime(){
        long timeInMillis       = System.currentTimeMillis();
        return timeInMillis;
    }

    public static void showToastValue(@NonNull Context ctx, @StringRes int valueString){
        if (Utils.isNull(ctx)){
            return;
        }else {
            Toast.makeText(ctx, valueString, Toast.LENGTH_LONG).show();
        }
    }

    public static void logContent(String tag, String funcName, @Nullable Exception exception,  @Nullable Error error) {
        Log.v(tag, " EXCEPTION OR  HAPPEN  funcName "+funcName);
        // Log Error before Add To DB
        if (!Utils.isNull(exception)){
            demoapp.dronecontroller.utils.Utils.toLog(tag, "logContent",null,exception);
        }
        if (!Utils.isNull(error)){
            demoapp.dronecontroller.utils.Utils.toLog(tag, "logContent",error,null);
        }

    }

    public static boolean isNullOrEmpty(JSONObject obj){
        if (isNull(obj)){
            return true;
        }else{
            JSONObject emptyObj = new JSONObject();
            if (obj.toString().toLowerCase().trim().contentEquals(emptyObj.toString().toLowerCase().trim())){
                return true;
            }else{
                return false;
            }
        }
    }
    public static String toStringNonNull(String value) {
        if (isNullOrEmpty(value)){
            return "";
        }else {
            return value;
        }
    }

//    public void myGenericMethod(Runnable runnable){
//        common task1;
//        common task2;
//        //consider checking if runnable != null to avoid NPE
//        runnable.run();
//    }
    public static @Nullable JSONArray clone (final JSONArray array)throws Exception,Error{
        if (isNull(array)){
            return null;
        }else{
            String data = array.toString();
            if (isNull(data)){
                return null;
            }else {
                JSONArray body = new JSONArray(data);
                return body;
            }
        }
    }
    public static @Nullable JSONObject clone (final JSONObject object)throws Exception,Error{
        if (isNull(object)){
            return null;
        }else{
            String data = object.toString();
            if (isNull(data)){
                return null;
            }else {
                JSONObject body = new JSONObject(data);
                return body;
            }
        }
    }

    public static @Nullable String clone (final String value)throws Exception,Error{
        if (isNull(value)){
            return null;
        }else{
            // get New Address
            String data = new String(value.getBytes());
            return data;
        }
    }

    public static @Nullable Integer clone (final Integer value)throws Exception,Error{
        if (isNull(value)){
            return null;
        }else{
            // get New Address
            Integer data = Integer.valueOf((value+""));
            return data;
        }
    }
    public static int clone (int value){
          // get New Address
        Integer data = Integer.valueOf((value+""));
        return data;
    }

    public static void runOnUIWithDelay(final Runnable runnable, MapActivity activity) {
        if (Utils.isNull(activity)){
            Log.v(TAG,"runWithDelay stop cause activity IS Null");
            return;
        }else {
            if (Utils.isNull(runnable)){
                activity.toLogUI("runWithDelay stop cause runnable IS Null");
                return;
            }else {
                new Handler(activity.getMainLooper()).postDelayed(runnable, TimeUnit.SECONDS.toMillis(Constants.DELAY));
            }
        }

    }

    public static @Nullable Double clone (final Double value)throws Exception,Error{
        if (isNull(value)){
            return null;
        }else{
            // get New Address
            Double data = Double.valueOf((value+""));
            return data;
        }
    }
    public static double clone (final double value){
        // get New Address
        return Double.parseDouble((value+""));
    }

    public static void AsyncExecute(@NonNull Runnable runnable){
        if (isNull(runnable)){
            return;
        }else{
            AsyncTask.execute(runnable);
        }
    }


}
