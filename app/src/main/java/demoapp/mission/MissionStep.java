package demoapp.mission;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.gms.maps.model.LatLng;
import com.google.common.collect.Lists;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import demoapp.dronecontroller.interfaces.OnAsyncOperationComplete;
import demoapp.dronecontroller.interfaces.OnMissionProcessingComplete;
import demoapp.dronecontroller.utils.Constants;
import demoapp.dronecontroller.utils.Utils;
import demoapp.mapping.PointLatLngAlt;


public class MissionStep {
    private final String TAG = getClass().getSimpleName();
    private volatile CopyOnWriteArrayList<CopyOnWriteArrayList<LatLng>> lists = new CopyOnWriteArrayList<>();
    public synchronized boolean hasNext(){
        return lists.size() > 0;
    }
    public boolean removeFirstOk(){
        try {
            synchronized (this){
                if (hasNext()){
                    lists.remove(0);
                    return true;
                }else {
                    return false;
                }
            }

        }catch (Exception e){
            Utils.toLog(TAG,"removeFirstOk",null, e);
        }catch (Error er){
            Utils.toLog(TAG,"removeFirstOk",er,null);
        }
        return false;
    }

    public synchronized int getSize()throws Exception,Error{
        return lists.size();
    }
    public CopyOnWriteArrayList<CopyOnWriteArrayList<LatLng>> getMainList(){
        return lists;
    }
    public synchronized @Nullable CopyOnWriteArrayList<LatLng> getFirst() throws Exception,Error{
        if (hasNext()){
            return lists.get(0);
        }
        return null;
    }
    public boolean addOk(@NonNull CopyOnWriteArrayList<LatLng> newList){
        synchronized (this){
            try {
                if (Utils.isNull(newList)){
                    return false;
                }else {
                    if (newList.size() > Constants.MAX_WAYPOINT_ITEMS){
                        return false;
                    }else {
                        if (Utils.isNull(lists)){
                            return false;
                        }else {
                            lists.add(newList);
                            return true;
                        }

                    }
                }
            }catch (Exception e){
                Utils.toLog(TAG, "addOk",null,e);

            }catch (Error er){
                Utils.toLog(TAG, "addOk",er,null);
            }
            return false;
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addAllOk(final @NonNull CopyOnWriteArrayList<PointLatLngAlt> grid, final @NonNull OnAsyncOperationComplete callBack){
        Utils.AsyncExecute(() ->{
            try {
                synchronized (MissionStep.this){
                    if (Utils.isNull(callBack)){
                        Utils.toLog(TAG, " addAllOk callBack is Null");
                        return;
                    }else {
                        if (Utils.isNull(grid)){
                            callBack.onError("");
                            Utils.toLog(TAG, " addAllOk grid is Null");
                            return;
                        }else {
                            if (grid.size() == 0){
                                callBack.onError("");
                                Utils.toLog(TAG, " addAllOk grid is Empty");
                                return;
                            }else {
                                // Create Array Copy Deep Copy
                                CopyOnWriteArrayList<LatLng> cloneList = new CopyOnWriteArrayList<>();
                                grid.forEach(item ->{
                                    try {
                                        if (MissionDJI.isRightWaypoint(item)){
                                            LatLng current = new LatLng(Utils.clone(item.getLat()), Utils.clone(item.getLng()));
                                            cloneList.add(current);
                                        }
                                    }catch (Exception e){
                                        Utils.toLog(TAG, "grid.forEach N2",null,e);
                                    }catch (Error er){
                                        Utils.toLog(TAG, "grid.forEach N2",er,null);
                                    }

                                });
                                if (cloneList.size() == 0){
                                    callBack.onError("");
                                    Utils.toLog(TAG, " addAllOk cloneList is Empty");
                                    return;
                                }else {
                                    // Split 3 array or 99 item
                                    List<List<LatLng>> output = Lists.partition(cloneList, Utils.clone(Constants.MAX_WAYPOINT_ITEMS));
                                    if (Utils.isNull(output)){
                                        callBack.onError("");
                                        Utils.toLog(TAG, " addAllOk output Is Null");
                                        return;
                                    }else {
                                        if (output.size() == 0){
                                            callBack.onError("");
                                            Utils.toLog(TAG, " addAllOk output Is EMPTY");
                                            return;
                                        }else {
                                            if (Utils.isNull(this.lists)){
                                                callBack.onError("");
                                                Utils.toLog(TAG, " addAllOk lists MainList Is null");
                                                return;
                                            }else {
                                                output.forEach(item ->{
                                                    CopyOnWriteArrayList<LatLng> newList = new CopyOnWriteArrayList<>();
                                                    newList.addAll(item);
                                                    addOk(newList);
                                                });
                                                callBack.onSucces("");
                                            }

                                        }
                                    }
                                }


                            }
                        }
                    }
                }
            }catch (Exception e){
                Utils.toLog(TAG, "addAllOk",null,e);

            }catch (Error er){
                Utils.toLog(TAG, "addAllOk",er,null);
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean isValid(){
        synchronized (this){
            try {
                if (hasNext()){
                    AtomicInteger badItems = new AtomicInteger();
                    lists.parallelStream().forEach(item ->{
                        if (Utils.isNull(item)){
                            Utils.toLog(TAG,"isValid  item Is Null");
                            badItems.getAndIncrement();
                        }else {
                            item.parallelStream().forEach(son ->{
                                if (Utils.isNull(son)){
                                    badItems.getAndIncrement();
                                }
                            });
                        }
                        if (item.size() > Constants.MAX_WAYPOINT_ITEMS){
                            Utils.toLog(TAG,"isValid  item Is MAX_WAYPOINT_ITEMS Reached Above 99 Points for Waymission 1.0");
                            badItems.getAndIncrement();
                        }
                    });
                    if (badItems.get() > 0){
                        return false;
                    }else {
                        return true;
                    }
                }
            }catch (Exception e){
                Utils.toLog(TAG, "isValid",null,e);
            }catch (Error er){
                Utils.toLog(TAG, "isValid",er,null);
            }
            return false;
        }
    }

    public void onDestroy(){
        synchronized (this){
            try {
                if (Utils.isNull(lists)){
                    return;
                }else {
                    lists.clear();
                }
            }catch (Exception e){
                Utils.toLog(TAG, "onDestroy",null,e);
            }catch (Error er){
                Utils.toLog(TAG, "onDestroy",er,null);
            }

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void fillFromString(@NonNull String data,@NonNull OnMissionProcessingComplete callBack)throws Exception,Error{
        Utils.AsyncExecute(() ->{
            synchronized (this){
                try {
                    if (Utils.isNull(callBack)){
                        Utils.toLog(TAG,"fillFromString  item Is callBack");
                        return;
                    }else {
                        try {
                            if (Utils.isNullOrEmpty(data)){
                                callBack.onError();
                                Utils.toLog(TAG,"fillFromString  data Is Null or Empty");
                                return;
                            }else {
                                JSONArray arrayMain = new JSONArray(data);
                                if (Utils.isNull(arrayMain)){
                                    callBack.onError();
                                    Utils.toLog(TAG,"fillFromString  arrayMain Is Null");
                                    return;
                                }else {
                                    for(int i = 0; i<arrayMain.length(); i++){
                                        JSONArray itemArray = arrayMain.getJSONArray(i);
                                        if (Utils.isNull(itemArray)){
                                            // skip
                                        }else {
                                            CopyOnWriteArrayList<LatLng> arrayOfLatLng =  new CopyOnWriteArrayList<>();
                                            for (int a = 0; a<itemArray.length(); a++){
                                                JSONObject obj = itemArray.getJSONObject(a);
                                                if (Utils.isNull(obj)){
                                                    // skip
                                                }else {
                                                    double latitude = obj.getDouble(Constants.JSON_KEY_LATITUDE);
                                                    double longitude = obj.getDouble(Constants.JSON_KEY_LONGITUDE);
                                                    LatLng latLng = new LatLng(latitude,longitude);
                                                    arrayOfLatLng.add(latLng);
                                                }
                                            }
                                            lists.add(arrayOfLatLng);
                                        }
                                    }
                                    if (!hasNext()){
                                        Utils.toLog(TAG,"fillFromString  list is Empty");
                                        callBack.onError();
                                        return;
                                    }else {
                                        callBack.onSucces(this);
                                        return;
                                    }

                                }
                            }
                        }catch (Exception e){
                            Utils.toLog(TAG, "fillFromString",null,e);
                        }catch (Error er){
                            Utils.toLog(TAG, "fillFromString",er,null);
                        }
                    }
                }catch (Exception e){
                    Utils.toLog(TAG, "fillFromString",null,e);
                }catch (Error er){
                    Utils.toLog(TAG, "fillFromString",er,null);
                }

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void toStringJSON(@NonNull OnAsyncOperationComplete callBack)throws Exception,Error{
        Utils.AsyncExecute(() ->{
            try {
                synchronized (this){
                    if (Utils.isNull(callBack)){
                        Utils.toLog(TAG,"toStringJSON callBack is Null");
                    }else {
                        String resNull = null;
                        if (!hasNext()){
                            callBack.onError("");
                            Utils.toLog(TAG,"toStringJSON  items Is Empty");
                            return;
                        }else {
                            JSONArray arrayMain = new JSONArray();
                            lists.forEach(item ->{
                                JSONArray itemArray = new JSONArray();
                                item.forEach(sonLatlng ->{
                                    JSONObject obj = new JSONObject();
                                    try {
                                        obj.put(Constants.JSON_KEY_LATITUDE,sonLatlng.latitude);
                                        obj.put(Constants.JSON_KEY_LONGITUDE,sonLatlng.longitude);
                                        itemArray.put(obj);
                                    }
                                    catch (Exception e){
                                        Utils.toLog(TAG, "toStringJSON",null,e);
                                    }catch (Error er){
                                        Utils.toLog(TAG, "toStringJSON",er,null);
                                    }
                                });
                                arrayMain.put(itemArray);
                            });
                            callBack.onSucces(arrayMain.toString());
                            return;
                        }
                    }

                }
            }catch (Exception e){
                Utils.toLog(TAG, "toStringJSON",null,e);
            }catch (Error er){
                Utils.toLog(TAG, "toStringJSON",er,null);
            }
        });


    }

    public void clearAll() {
        synchronized (this){
            try {
                lists.clear();
            }catch (Exception e){
                Utils.toLog(TAG, "clearAll",null,e);
            }catch (Error er){
                Utils.toLog(TAG, "clearAll",er,null);
            }

        }
    }

    public void replaceFirst(CopyOnWriteArrayList<LatLng> allpoints) {
        try {
            lists.set(0,allpoints);
        }catch (Exception e){
            Utils.toLog(TAG, "replaceFirst",null,e);
        }catch (Error er){
            Utils.toLog(TAG, "replaceFirst",er,null);
        }

    }
}
