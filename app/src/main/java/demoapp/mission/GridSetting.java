package demoapp.mission;

public class GridSetting {
    private float altitude;
    private double distance;
    private double spacing;
    private double overShoot1;
    private double overShoot2;
    private double angle;
    private float leadin1;
    private float leadin2;

    public GridSetting(){
        this.altitude = 0;
        this.distance = 0;
        this.spacing = 0;
        this.overShoot1 = 0;
        this.overShoot2 = 0;
        this.angle = 0;
        this.leadin1 = 0;
        this.leadin2 = 0;

    }

    public float getAltitude() {
        return altitude;
    }

    public void setAltitude(float altitude) {
        this.altitude = altitude;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getSpacing() {
        return spacing;
    }

    public void setSpacing(double spacing) {
        this.spacing = spacing;
    }

    public double getOverShoot1() {
        return overShoot1;
    }

    public void setOverShoot1(double overShoot1) {
        this.overShoot1 = overShoot1;
    }

    public double getOverShoot2() {
        return overShoot2;
    }

    public void setOverShoot2(double overShoot2) {
        this.overShoot2 = overShoot2;
    }

    public float getLeadin1() {
        return leadin1;
    }

    public void setLeadin1(float leadin1) {
        this.leadin1 = leadin1;
    }

    public float getLeadin2() {
        return leadin2;
    }

    public void setLeadin2(float leadin2) {
        this.leadin2 = leadin2;
    }

    public double getAngle() {
        return angle;
    }

    public int getAngleAsPitch() {
        if (getAngle()> 0){
            return (int) -getAngle();
        }
        return (int) angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public boolean isValid(){
        if (getAltitude()== Constant.FLOAT_NULL
                || getAltitude()== Constant.FLOAT_NULL_2){
            return false;
        }else {
            // altitude Can Be Over 450 over Constant.MIN_ALTITUDE_IN_CI & Constant.MAX_ALTITUDE_IN_CI
            // Distance can be 0
            // Spacing can be 0
            // OverShoot and leadIn can be 0
            return true;
        }
    }
}
