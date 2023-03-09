package ca.future.home.it.secure.home.automation;

public class AccountFragmentData {
    private int icon;
    private String title;
    private String subtitle;

    public AccountFragmentData(int iconRV, String titleRV, String detailsRV){
        this.icon = iconRV;
        this.title = titleRV;
        this.subtitle = detailsRV;
    }

    public int getIcon(){
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String tilte){
        this.title = title;
    }

    public String getDetail(){
        return subtitle;
    }

    public void setDetail(String details){
        this.subtitle = details;
    }

}
