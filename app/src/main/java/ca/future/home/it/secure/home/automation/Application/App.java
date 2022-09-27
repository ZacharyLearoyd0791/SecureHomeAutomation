package ca.future.home.it.secure.home.automation.Application;
import android.app.Application;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPref.init(this);
    }



}
