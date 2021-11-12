package demoapp.dronecontroller.utils;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by admin on 21/03/2018.
 */

public class Constants {

    public static int DELAY = 24;
    public static int DELAY_10_SECONDS = 10;
    public static int DELAY_SHORT = 500;
    public static int DELAY_47_SECONDS = 47;
    public static final double DOUBLE_NULL          = 0.0d;
    public static final double DOUBLE_NULL_2        = 0d;
    public final Long LONG_INVALID                  = -10L;

    public static final int REQUEST_GROUP_PERMISSION                    = 435;
    public static final int REQUEST_APP_PERMISSION                      = 435;
    public static final boolean ENABLE_SIMILATOR                 = false;

    public static int CODE_ON_BACK_PRESSED = 7;
    public static int CODE_DRONE_LOW_BATTERY = 10;

    public static final String SAVE_PREFERENCE_NAME                 = "dronecontroller.save";
    public static final String JSON_KEY_LATITUDE                    = "latitude";
    public static final String JSON_KEY_LONGITUDE                   = "longitude";
    public static final String PREF_WAYPOINT_MISSION                = "waypointmission";
    public static final String PREF_WAYPOINT_PARAMETER              = "waypointmission.parameters";
    public static final String JS_KEY_ALTITUDE_PREFERED             = "altitudePrefered";
    public static final String JS_KEY_USE_SIMULATOR                 = "useSimulator";
    public static final String JS_KEY_AUTO_RETURN_ON_LOW_BATTERY    = "autoGohomeOnlowbattery";
    public static final String JS_KEY_SPEED                         = "speed";
    public static final String JS_KEY_FLY_POINTS                    = "flyPoints";
    public static final String JS_KEY_BATTERY_NEED                  = "batteryNeeds";
    public static final String JS_KEY_IMAGE_COUNT                   = "imageCount";
    public static final String JS_KEY_EXIT_MISSION                  = "exitMissionOnRCsignalLostenabled";
    public static final String JS_KEY_HOME_LOCATION_LATITUDE        = "homeLocationLat";
    public static final String JS_KEY_HOME_LOCATION_LONGITUDE       = "homeLocationLong";
    public static final String JS_KEY_SHOOT_INTERVAL                = "shootingInterval";
    public static int POLYGON_SIZE                                  = 3;
    public static float SPEED_MIN                                   = 2.0f;
    public static float SPEED_MAX                                   = 15.0f;
    public static final int MAX_WAYPOINT_ITEMS                      = 99;
    public static final int LOW_BATTERY_LEVEL                       = 30;

    public static final short HECTARE_0                             = 0;
    public static final float HECTARE_ZERO_POINT_FIVE               = 0.5f;
    public static final short HECTARE_1                             = 1;
    public static final short HECTARE_2                             = 2;

    public static final int ALTITUDE_100                          = 100;
    public static final int ALTITUDE_90                           = 90;
    public static final int ALTITUDE_80                           = 80;
    public static final int ALTITUDE_70                           = 70;
    public static final int ALTITUDE_60                           = 60;
    public static final int ALTITUDE_50                           = 50;
    public static final double TIME_16_MINUTES_TO_SECONDES        = 960; // 1Battery = 16 Min max
    public static final short DJI_JPEG_INTERVAL_LIMIT_SECONDS     = 2; // 2 Seconds


}
