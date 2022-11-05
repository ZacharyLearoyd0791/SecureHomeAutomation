package ca.future.home.it.secure.home.automation;

import static android.content.ContentValues.TAG;

import android.net.Uri;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class FireBaseInfo extends Fragment {

    String personName,
            personGivenName,
            personFamilyName,
            personEmail,
            personId;

    Uri personPhoto;

    public void accountInfo() {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (acct != null) {
            personName = acct.getDisplayName();
            personGivenName = acct.getGivenName();
            personFamilyName = acct.getFamilyName();
            personEmail = acct.getEmail();
            personId = acct.getId();
            personPhoto = acct.getPhotoUrl();
            Log.d(TAG, personName);


        }
    }

    public String getpersonName() {

        return personName;
    }

    public String getGivenName() {

        return personGivenName;
    }

    public String getpersonFamilyName() {

        return personFamilyName;
    }

    public String getpersonEmail() {

        return personEmail;
    }

    public String getpersonId() {

        return personId;
    }

    public Uri getPersonPhoto() {

        return personPhoto;
    }

}