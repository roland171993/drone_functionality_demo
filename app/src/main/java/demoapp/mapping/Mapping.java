package demoapp.mapping;

import androidx.annotation.NonNull;

import java.util.concurrent.CopyOnWriteArrayList;

public final class Mapping {
    public static void generateSurveyPath(CopyOnWriteArrayList<PointLatLngAlt> polygon, double altitude, double distance, double spacing, double angle, double overshoot1, double overshoot2, Grid.StartPosition startpos, boolean shutter, float minLaneSeparation, float leadin1, float leadin2, PointLatLngAlt HomeLocation, boolean useextendedendpoint,
                                          final OnComputingComplete callBack){
        //ORIGINAL LINE: public static async Task<List<PointLatLngAlt>> CreateGridAsync(List<PointLatLngAlt> polygon, double altitude, double distance, double spacing, double angle, double overshoot1, double overshoot2, StartPosition startpos, bool shutter, float minLaneSeparation, float leadin1,float leadin2, PointLatLngAlt HomeLocation, bool useextendedendpoint = true)
        Utils.AsyncExecute(() ->{
            if (Utils.isNull(callBack)){
                return;
            }else {
                CopyOnWriteArrayList<PointLatLngAlt> arr = Grid.CreateGridAsync(polygon, altitude, distance, spacing, angle, overshoot1, overshoot2, startpos, shutter, minLaneSeparation, leadin1, leadin2, HomeLocation, useextendedendpoint,callBack);
                if (Utils.isNull(arr)){
                    callBack.OnError();
                }else {
                    callBack.OnSucces(arr);
                }
            }
        });


    }

    public interface OnComputingComplete
    {
        void OnError();
        void OnSucces(@NonNull CopyOnWriteArrayList<PointLatLngAlt> gridNotNull);
        void OnNotifyState(int percentGoToUIThread);
    }
}
