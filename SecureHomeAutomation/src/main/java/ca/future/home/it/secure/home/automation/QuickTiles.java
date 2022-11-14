/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/

package ca.future.home.it.secure.home.automation;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.N)
public class QuickTiles extends TileService {

    UserInfo userInfo=new UserInfo();
    // Called when the user adds your tile.
    @Override
    public void onTileAdded() {
        super.onTileAdded();
    }

    // Called when your app can update your tile.
    @Override
    public void onStartListening() {
        super.onStartListening();
        Tile title=getQsTile();
        title.setLabel(getString(R.string.app_name));
        title.setState(Tile.STATE_ACTIVE);
        title.setIcon(Icon.createWithResource(this,R.drawable.appicon));
        title.updateTile();



        Log.d(TAG, String.valueOf(title));

    }


    // Called when your app can no longer update your tile.
    @Override
    public void onStopListening() {
        super.onStopListening();
        userInfo.typeAccount();

        if (isLocked()){
//            Log.d(TAG,"Device Locked");

        }
        else{
//            Log.d(TAG,"Device UnLocked");
            if(userInfo.userId!=null||userInfo.idInfo!=null){
//                Log.d(TAG,"Connected using accounts");
            }
            else{
//                Log.d(TAG,"Not connected using accounts");

            }
            Intent intent=new Intent(this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
    }

    // Called when the user taps on your tile in an active or inactive state.
    @Override
    public void onClick() {
        super.onClick();
    }

    // Called when the user removes your tile.
    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
    }

}