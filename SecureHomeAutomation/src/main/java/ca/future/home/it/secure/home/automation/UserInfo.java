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
            Log.d(TAG,"User login information isn't available now");
        }

    }
    private void LocalUsers() {
        Log.d(TAG, "User method has been selected for testing");//remove after testing

        name = user.getDisplayName();
        email = user.getEmail();
        uid = user.getUid();
        str = "Testing 001" + "\nName: " + name + "\nEmail: " + email + "\nUID: " + uid;
        Log.d(TAG, str);

        if (name!=null){
            infoLocalName="Name is "+name;
            Log.d(TAG,infoLocalName);
            returnLocalName();
        }
        if (email!=null){
            infoLocalEmail="Email is "+email;
            Log.d(TAG,infoLocalEmail);
            returnLocalEmail();
        }
        else{
            Log.d(TAG,"name is null");
        }
        returnLocalId();

    }

    private void googleLoginUsers(){
        personName = acct.getDisplayName();
        personId=acct.getId();
        personEmail= acct.getEmail();
        personPhoto = acct.getPhotoUrl();

        if (personName!=null){
            infoName="Name is "+personName;
            Log.d(TAG,infoName);
            returnName();
        }

        if (personEmail!=null){
            infoEmail="Email is "+personEmail;
            Log.d(TAG,infoEmail);
            returnEmail();
        }

        else{
            Log.d(TAG,"name is null");
        }

        if (personId!=null){
            infoID="Id of user is "+personId;

            Log.d(TAG,infoName);
            returnId();
        }
        else{
            Log.d(TAG,"Id is null");
        }

        if (personPhoto!=null){
            photos="Person Photo is Available ";

            Log.d(TAG,photos);
            returnPhoto();
        }
        else{
            Log.d(TAG,"Id is null");
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
