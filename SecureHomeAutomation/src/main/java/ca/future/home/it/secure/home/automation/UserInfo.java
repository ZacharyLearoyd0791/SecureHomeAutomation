/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/

package ca.future.home.it.secure.home.automation;

import static android.content.ContentValues.TAG;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserInfo extends Fragment {
    private GoogleSignInAccount acct;
    private FirebaseUser user;
    String infoID,infoName,name,email,uid,str,photos,personName,personId,infoLocalName,personEmail,infoEmail,infoLocalEmail;
    String idInfo,nameInfo,userId,localName,localEmail,emailInfo;
    static Context context;
    Uri personPhoto,photOut;

    public void typeAccount() {
        UserInfo.context = getApplicationContext();

        user = FirebaseAuth.getInstance().getCurrentUser();
        acct = GoogleSignIn.getLastSignedInAccount(context);

        if (user != null) {
            LocalUsers();
        }
        else if(acct!=null){
            googleLoginUsers();
        }
        else{
            Log.d(TAG,getString(R.string.login_info_not_avail));
        }
    }

    private void LocalUsers() {
        Log.d(TAG, getString(R.string.method_selected_testing));//remove after testing

            name = user.getDisplayName();
            email = user.getEmail();
            uid = user.getUid();
            str = getString(R.string.testing_001) + getString(R.string.name_) + name + getString(R.string.email_) + email + getString(R.string.uid) + uid;
            Log.d(TAG, str);

        if (name!=null){
            infoLocalName=getString(R.string.name_is)+name;
            Log.d(TAG,infoLocalName);
            returnLocalName();
        }

        if (email!=null){
            infoLocalEmail=getString(R.string.email_is)+email;
            Log.d(TAG,infoLocalEmail);
            returnLocalEmail();
        }
        else{
            Log.d(TAG,getString(R.string.name_null));
        }
        returnLocalId();
    }

    private void googleLoginUsers(){
        personName = acct.getDisplayName();
        personId=acct.getId();
        personEmail= acct.getEmail();
        personPhoto = acct.getPhotoUrl();

        if (personName!=null){
            infoName=getString(R.string.name_is)+personName;
            Log.d(TAG,infoName);
            returnName();
        }

        if (personEmail!=null){
            infoEmail=getString(R.string.email_is)+personEmail;
            Log.d(TAG,infoEmail);
            returnEmail();
        }
        else{
            Log.d(TAG,getString(R.string.name_null));
        }

        if (personId!=null){
            infoID=getString(R.string.id_is)+personId;

            Log.d(TAG,infoName);
            returnId();
        }
        else{
            Log.d(TAG,getString(R.string.id_is_null));
        }

        if (personPhoto!=null){
            photos=getString(R.string.person_photo_avail);

            Log.d(TAG,photos);
            returnPhoto();
        }
        else{
            Log.d(TAG,getString(R.string.id_is_null));
        }
    }

    private String returnEmail() {
        emailInfo=personEmail;
        return emailInfo;
    }
    private String returnLocalEmail() {
        localEmail=email;
        return localEmail;
    }

    private Uri returnPhoto() {
        photOut=personPhoto;
        return photOut;
    }

    private String returnLocalName() {
        localName=infoLocalName;
        return localName;
    }

    private String returnLocalId() {
        userId=uid;
        return userId;
    }
    private String returnId() {
        idInfo=personId;
        return idInfo;
    }

    private String returnName() {
        nameInfo=personName;

        return nameInfo;
    }
}
