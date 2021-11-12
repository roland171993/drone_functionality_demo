package demoapp.dronecontroller.model;

public enum DroneModel {
    PHANTOM_4_ADVANCED(1),
    PHANTOM_4(2),
    UNKNOW(3);

    private int data;

    private DroneModel(int val){
        this.data = val;
    }

    public boolean _equals(int value) {
        return this.data == value;
    }


    public static DroneModel find(int value){
        DroneModel resp = UNKNOW;
        for(int var2 = 0; var2 < values().length; ++var2) {
            if(values()[var2]._equals(value)) {
                resp = values()[var2];
                break;
            }
        }

        return resp;
    }

    public int getValue() {
        return data;
    }

}
