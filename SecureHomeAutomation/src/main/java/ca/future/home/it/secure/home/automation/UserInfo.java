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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserInfo extends Fragment {
    private static GoogleSignInAccount acct;
    private static FirebaseUser user;
    String infoID,infoName,name,email,uid,str,photos,personName,personId,infoLocalName,personEmail,infoEmail,infoLocalEmail;
    String idInfo,nameInfo,userId,localName,localEmail,emailInfo;
    static Context context;
    Uri personPhoto,photOut;
    private int signInMethod;

    public void typeAccount() {
        UserInfo.context = getApplicationContext();

        user = FirebaseAuth.getInstance().getCurrentUser();
        acct = GoogleSignIn.getLastSignedInAccount(context);

        if (user != null) {
            signInMethod = 1;
            LocalUsers();
        }
        else if(acct!=null){
            googleLoginUsers();
            signInMethod = 2;
        }
        else{
            signInMethod = 0;
        }

    }

    public static int getSignInType(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        acct = GoogleSignIn.getLastSignedInAccount(context);

        if (user != null) {
            return 0;
        }
        else if(acct!=null){
            return 1;
        }else{
            return -1;
        }
    }
    private void LocalUsers() {

        name = user.getDisplayName();
        email = user.getEmail();
        uid = user.getUid();

        if (name!=null){
            returnLocalName();
        }
        if (email!=null){
            returnLocalEmail();
        }
        else{
        }
        returnLocalId();
    }

    private void googleLoginUsers(){
        personName = acct.getDisplayName();
        personId=acct.getId();
        personEmail= acct.getEmail();
        personPhoto = acct.getPhotoUrl();

        if (personName!=null){
            returnName();
        }

        if (personEmail!=null){
            returnEmail();
        }

        else{
        }

        if (personId!=null){

            returnId();
        }
        else{
        }

        if (personPhoto!=null){

            returnPhoto();
        }
        else{
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
