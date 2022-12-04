/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/

package ca.future.home.it.secure.home.automation;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.service.quicksettings.TileService;
import android.util.Log;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.N)
public class homeAutomationTitlesService extends TileService {
    private final String LOG_TAG = "myTileService";
    private final  int STATE_ON = 1;
    private final int STATE_OFF = 0;
    private  int toggleState = STATE_ON;
    @Override
    public void onTileAdded() {
    }

    @Override
    public void onTileRemoved() {
    }

    @Override
    public void onStartListening() {

    }

    @Override
    public void onStopListening() {
    }

    @Override
    public void onClick() {
        Icon icon;
        if(toggleState == STATE_ON){
            toggleState = STATE_OFF;
            icon = Icon.createWithResource(getApplicationContext(),R.drawable.ic_window_icon);
        }else{
            toggleState = STATE_ON;
            icon = Icon.createWithResource(getApplicationContext(),R.drawable.ic_window_icon);

        }
        getQsTile().setIcon(icon);
        getQsTile().updateTile();
    }
}
