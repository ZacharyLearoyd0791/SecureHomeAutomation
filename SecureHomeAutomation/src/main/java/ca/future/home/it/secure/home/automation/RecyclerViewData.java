package ca.future.home.it.secure.home.automation;

import java.util.ArrayList;

public class RecyclerViewData {
    private String wAlertDescription;
    private String wAlertType;
    private String wAlertStatus;
    private String wAlertTimings;
    private Boolean wAlertOnline;
    private static int lastAlertId = 0;

    public RecyclerViewData(String wAlertType,Boolean wAlertOnline){
        this.wAlertType = wAlertType;
        this.wAlertOnline = wAlertOnline;

    }

    public String getwAlertType() {
        return wAlertType;
    }
    public boolean isOnline(){
        return wAlertOnline;
    }
    public static ArrayList<RecyclerViewData> createWindowsAlertList(int num){
        ArrayList<RecyclerViewData> data = new ArrayList<RecyclerViewData>();
        for(int i = 1; i<= num;i++){
            data.add(new RecyclerViewData("Alert Type "+ ++lastAlertId, i<=num/2));
        }

        return data;
    }
}
