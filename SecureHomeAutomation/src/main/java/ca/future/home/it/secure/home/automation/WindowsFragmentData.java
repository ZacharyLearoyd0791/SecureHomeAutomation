package ca.future.home.it.secure.home.automation;

public class WindowsFragmentData {
    //Declarations
    private int icon;
    private String activityAlarm;
    private String activityDate;

    public WindowsFragmentData(int iconRV, String activityAlarmRV, String activityDateRV){
        this.icon = iconRV;
        this.activityAlarm = activityAlarmRV;
        this.activityDate = activityDateRV;
    }

    public int getActivityIcon(){ return icon;}
    public void setActivityIcon(int iconRV){this.icon = iconRV;}
    public String getActivityAlarmTitle(){return activityAlarm;}
    public void setActivityAlarmTitle(String activityAlarmRV){this.activityAlarm= activityAlarmRV;}
    public String getActivityDate(){return activityDate;}
    public void setActivityAlarmDate(String activityDateRV){this.activityDate = activityDateRV;}



}
