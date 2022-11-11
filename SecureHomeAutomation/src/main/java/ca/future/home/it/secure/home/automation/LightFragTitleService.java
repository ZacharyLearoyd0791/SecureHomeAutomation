package ca.future.home.it.secure.home.automation;

import android.graphics.drawable.Icon;
import android.os.Build;
import android.service.quicksettings.TileService;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.N)
public class LightFragTitleService extends TileService {
    private final String LOG_TAG = getString(R.string.light_frag_tiles_service);
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
            icon = Icon.createWithResource(getApplicationContext(),R.drawable.ic_tiles_service_light_off);
            Toast.makeText(this, "Lights are Off!", Toast.LENGTH_SHORT).show();
        }
        else{
            toggleState = STATE_ON;
            icon = Icon.createWithResource(getApplicationContext(),R.drawable.ic_tiles_service_lights_on);
            Toast.makeText(this, "Lights are On!", Toast.LENGTH_SHORT).show();
        }
        getQsTile().setIcon(icon);
        getQsTile().updateTile();
    }
}
