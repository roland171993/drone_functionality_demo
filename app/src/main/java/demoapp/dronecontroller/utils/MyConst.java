package demoapp.dronecontroller.utils;

import androidx.annotation.StringRes;

public class MyConst implements Runnable {
    private String parameterStr;
    private @StringRes int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getParameterStr() {
        if (Utils.isNullOrEmpty(parameterStr)){
            parameterStr = "";
        }
        return parameterStr;
    }

    public void setParameterStr(String parameterStr) {
        this.parameterStr = parameterStr;
    }

    @Override
    public void run() {
    }

    public void run(String value){
        this.parameterStr = value;
    }
    public void run(@StringRes int value){
        this.value = value;
    }
}
