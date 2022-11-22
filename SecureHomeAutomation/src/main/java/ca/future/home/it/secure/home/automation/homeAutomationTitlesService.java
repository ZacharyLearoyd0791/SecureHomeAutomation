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
        Log.d(LOG_TAG,getString(R.string.log_tile_add));
    }

    @Override
    public void onTileRemoved() {
        Log.d(LOG_TAG,getString(R.string.log_tile_rem));
    }

    @Override
    public void onStartListening() {
        Log.d(LOG_TAG,getString(R.string.log_start_listen));
    }

    @Override
    public void onStopListening() {
        Log.d(LOG_TAG,getString(R.string.log_stop_listen));
    }

    @Override
    public void onClick() {
        Log.d(LOG_TAG,getString(R.string.log_onclick_state)+ Integer.toString(getQsTile().getState()));
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
