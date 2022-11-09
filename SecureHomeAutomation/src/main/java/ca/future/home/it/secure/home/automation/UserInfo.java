package ca.future.home.it.secure.home.automation;

import static android.content.ContentValues.TAG;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserInfo extends Fragment {
    private GoogleSignInAccount acct;
    private FirebaseUser user;
    String infoName,name,email,uid,str,personName,personId,infoLocalName;
    String idInfo,nameInfo,userId,localName;
    static Context context;



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
        else{
            Log.d(TAG,"name is null");

        }
        returnLocalId();

    }



    private void googleLoginUsers(){
        personName = acct.getDisplayName();
        personId=acct.getId();

        if (personName!=null){
            infoName="Name is "+personName;
            Log.d(TAG,infoName);
            returnName();
        }
        else{
            Log.d(TAG,"name is null");

    }
        if (personId!=null){
            infoName="Id of user is "+personId;

            Log.d(TAG,infoName);
            returnId();
        }
        else{
            Log.d(TAG,"Id is null");

        }

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
