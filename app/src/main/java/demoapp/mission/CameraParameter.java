package demoapp.mission;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import demoapp.dronecontroller.model.DroneModel;
import demoapp.dronecontroller.utils.Constants;
import demoapp.dronecontroller.utils.Utils;


public class CameraParameter {
    private float altitudeForGrid;
    private int overlap;
    private int sidelap;
    private double focalLengthInMM;
    private double sensorWidth;
    private double sensorHeight;
    private double angle;
    private double angleForGrid;


    public double getAngleForGrid() {
        return angleForGrid;
    }

    public void setAngleForGrid(double angleForGrid) {
        this.angleForGrid = angleForGrid;
    }

    public float getAltitudeForGrid() {
        return altitudeForGrid;
    }

    public void setAltitudeForGrid(float altitudeForGrid) {
        this.altitudeForGrid = altitudeForGrid;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }


    public int getOverlap() {
        return overlap;
    }

    public void setOverlap(int overlap) {
        this.overlap = overlap;
    }

    public int getSidelap() {
        return sidelap;
    }

    public void setSidelap(int sidelap) {
        this.sidelap = sidelap;
    }

    public double getFocalLengthInMM() {
        return focalLengthInMM;
    }

    public void setFocalLengthInMM(double focalLengthInMM) {
        this.focalLengthInMM = focalLengthInMM;
    }

    public double getSensorWidth() {
        return sensorWidth;
    }

    public void setSensorWidth(double sensorWidth) {
        this.sensorWidth = sensorWidth;
    }

    public double getSensorHeight() {
        return sensorHeight;
    }

    public void setSensorHeight(double sensorHeight) {
        this.sensorHeight = sensorHeight;
    }

    public boolean isValid(){
        if (getAltitudeForGrid()== Constant.FLOAT_NULL
                || getAltitudeForGrid()== Constant.FLOAT_NULL_2){
            toLog("isValid getAltitude() NOT VALID");
            return false;
        }else {
            if (getAngle() == Constant.INTEGER_NULL){
                toLog("isValid getAngle() NOT VALID");
                return false;
            }else {
                if (getAngleForGrid() == Constant.INTEGER_NULL){
                    toLog("isValid getAngleForGrid() NOT VALID");
                    return false;
                }else {
                    if (getAltitudeForGrid() == Constant.INTEGER_NULL){
                        toLog("isValid getAltitudeForGrid() NOT VALID");
                        return false;
                    }else {
                        if (getOverlap() == Constant.INTEGER_NULL){
                            toLog("isValid getOverlap() NOT VALID");
                            return false;
                        }else {
                            if (getSidelap() == Constant.INTEGER_NULL){
                                toLog("isValid getSidelap() NOT VALID");
                                return false;
                            }else {
                                if (getFocalLengthInMM() == Constant.DOUBLE_NULL || getFocalLengthInMM() == Constant.DOUBLE_NULL_2){
                                    toLog("isValid getFocalLengthInMM() NOT VALID");
                                    return false;
                                }else {
                                    if (getSensorHeight() == Constant.DOUBLE_NULL || getSensorHeight() == Constant.DOUBLE_NULL_2){
                                        toLog("isValid getSensorHeight() NOT VALID");
                                        return false;
                                    }else {
                                        if (getSensorWidth() == Constant.DOUBLE_NULL || getSensorWidth() == Constant.DOUBLE_NULL_2 ){
                                            toLog("isValid getSensorWidth() NOT VALID");
                                            return false;
                                        }else {
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }

    private double getMPAngle(DroneModel model)throws Exception,Error{
        switch (model){
            case PHANTOM_4_ADVANCED:
                // Obtain After Training From Drone Deploy
                return 53;
        }
        return 0;
    }

    private float getMPAltitude(DroneModel model, float baseAltitude) throws Exception,Error {
        // Based on DroneDeploy 40      50     60       70    80     90     100
        // Ration forSameResult 7,75   5,86    5,80     6,42   5,66  6,11   5,6
        switch (model){
            case PHANTOM_4_ADVANCED:
                if (baseAltitude >= Constants.ALTITUDE_100 ){
                    return (float) (baseAltitude * 5.6);
                }else {
                    if (baseAltitude >= Constants.ALTITUDE_90){
                        return (float) (baseAltitude * 6.11);
                    }else {
                        if (baseAltitude >= Constants.ALTITUDE_80){
                            return (float) (baseAltitude * 5.66);
                        }else {
                            if (baseAltitude >= Constants.ALTITUDE_70){
                                return (float) (baseAltitude * 6.42);
                            }else {
                                if (baseAltitude >= Constants.ALTITUDE_60){
                                    return (float) (baseAltitude * 5.80);
                                }else {
                                    if (baseAltitude >= Constants.ALTITUDE_50){
                                        return (float) (baseAltitude * 5.86);
                                    }
                                }
                            }
                        }
                    }
                }

        }
        return 0;
    }

    public static @Nullable CameraParameter getDJIPhantom4Advanced(float altitude)throws Error,Exception{
        CameraParameter setting = new CameraParameter();
        setting.setOverlap(70);
        setting.setSidelap(70);
        setting.setFocalLengthInMM(20);
        setting.setSensorWidth(4.7);
        setting.setSensorHeight(6.3);

        setting.setAltitudeForGrid(setting.getMPAltitude(DroneModel.PHANTOM_4_ADVANCED,altitude));
        setting.setAngleForGrid(setting.getMPAngle(DroneModel.PHANTOM_4_ADVANCED));
        setting.setAngle(90);
        if (!setting.isValid()){
            return null;
        }else {
            return setting;
        }
    }
    private void toLog( @NonNull String msg){
        Utils.toLog(getClass().getSimpleName(), msg);
    }
}
