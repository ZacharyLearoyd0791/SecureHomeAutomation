/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/
package ca.future.home.it.secure.home.automation;

import static android.app.Activity.RESULT_OK;
import static com.google.firebase.crashlytics.internal.Logger.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;

public class AccountFragment extends Fragment {
    private TextView personName;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private Button signOutButton;
    private SharedPreferences sharedPreferencesEmail;
    private SharedPreferences sharedPreferencesGoogle;
    private SharedPreferences sharedPreferencesFacebook;
    private boolean loggedInMethodEmail;
    private boolean loggedInMethodGoogle;
    private boolean loggedInMethodFacebook;
    private ImageView editProfile;
    int SELECT_PHOTO = 1;
    Uri uri;
    View view;
    ImageView imgAcc;
    TextView nameAcc, emailAcc;
    final Handler handler = new Handler();





    public AccountFragment() {
        // Required empty public constructor
    }
    /*@Override
    public void onStart(){
        super.onStart();
        nameAcc = view.findViewById(R.id.Name);
        emailAcc = view.findViewById(R.id.Email);
        nameAcc.setText(null);
        emailAcc.setText(null);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_account, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Declarations
        String name = getString(R.string.name);
        String email = getString(R.string.email);
        imgAcc = view.findViewById(R.id.imgAcc);
        nameAcc = view.findViewById(R.id.Name);
        emailAcc = view.findViewById(R.id.Email);
        nameAcc.setText(name);
        emailAcc.setText(email);

        editProfile = view.findViewById(R.id.profile_image_edit);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentEditProfile = new Intent(Intent.ACTION_PICK);
                intentEditProfile.setType("image/*");
                startActivityForResult(intentEditProfile, SELECT_PHOTO);
            }
        });


        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
        gsc = GoogleSignIn.getClient(getContext(),gso);

        sharedPreferencesEmail = getContext().getSharedPreferences(LoginActivity.LOGGED_IN_METHOD_EMAIL,0);
        sharedPreferencesGoogle = getContext().getSharedPreferences(LoginActivity.LOGGED_IN_METHOD_GOOGLE,0);
        sharedPreferencesFacebook = getContext().getSharedPreferences(LoginActivity.LOGGED_IN_METHOD_FACEBOOK,0);
        loggedInMethodEmail = sharedPreferencesEmail.getBoolean("LoggedInMethodEmail",false);
        loggedInMethodGoogle = sharedPreferencesGoogle.getBoolean("LoggedInMethodGoogle",false);
        loggedInMethodFacebook = sharedPreferencesFacebook.getBoolean("LoggedInMethodFacebook",false);
        signOutButton = view.findViewById(R.id.Settings_signOut_button);

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(loggedInMethodEmail) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(getContext(), "Signed out with Email", Toast.LENGTH_SHORT).show();
                }
                else if(loggedInMethodGoogle){
                    gsc.signOut()
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getContext(), "Signed out with Google", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else if (loggedInMethodFacebook){
                    LoginManager.getInstance().logOut();
                    Toast.makeText(getContext(), "Signed out with Facebook", Toast.LENGTH_SHORT).show();
                }else{

                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(getContext(), "Signed out with Email", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (currentFirebaseUser != null) {

            Log.d(TAG, "onComplete: currentUserEmail---->" + currentFirebaseUser.getEmail());
            Log.d(TAG, "onComplete: currentUserDisplayName---->" + currentFirebaseUser.getDisplayName());
            if (currentFirebaseUser.getDisplayName() == null) {
                nameAcc.append(getString(R.string.noVal));
            } else {
                nameAcc.append(currentFirebaseUser.getDisplayName());
            }
            if (currentFirebaseUser.getEmail() == null) {
                emailAcc.append(getString(R.string.noVal));
            } else {
                emailAcc.append(currentFirebaseUser.getEmail());
            }


        } else {
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();


                // Toast.makeText(getActivity(), personName, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onComplete: personName---->" + personName);
                Log.d(TAG, "onComplete: personGivenName---->" + personGivenName);
                Log.d(TAG, "onComplete: personFamilyName---->" + personFamilyName);
                Log.d(TAG, "onComplete: personEmail---->" + personEmail);
                Log.d(TAG, "onComplete: personId---->" + personId);
                Log.d(TAG, "onComplete: personPhoto---->" + personPhoto);

                nameAcc.setText(name + personName);
                emailAcc.setText(email + personEmail);


                ImageView imgAcc = view.findViewById(R.id.imgAcc);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null){
            uri = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uri);
              //  bitmap = getContext().getCircularBitmap(bitmap);
                imgAcc.setImageBitmap(bitmap);
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}