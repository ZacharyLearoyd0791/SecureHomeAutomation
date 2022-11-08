/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/
package ca.future.home.it.secure.home.automation;

import static com.google.firebase.crashlytics.internal.Logger.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class AccountFragment extends Fragment {
    private TextView personName;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private Button signOutButton;
    View view;
    ImageView imgAcc;
    TextView  emailAcc;
    final Handler handler = new Handler();


    public AccountFragment() {
        // Required empty public constructor
    }

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
        //nameAcc = view.findViewById(R.id.Name);
        emailAcc = view.findViewById(R.id.Email);
//        nameAcc.setText(name);
        emailAcc.setText(email);

        signOutButton = view.findViewById(R.id.Settings_signOut_button);
        signOutButton.setOnClickListener(view1 -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            Toast.makeText(getContext(), "Signed out!", Toast.LENGTH_SHORT).show();
        });
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentFirebaseUser != null) {

            Log.d(TAG, "onComplete: currentUserEmail---->" + currentFirebaseUser.getEmail());
            Log.d(TAG, "onComplete: currentUserDisplayName---->" + currentFirebaseUser.getDisplayName());
            if (currentFirebaseUser.getDisplayName() == null) {
                //nameAcc.append(getString(R.string.noVal));
            } else {
               // nameAcc.append(currentFirebaseUser.getDisplayName());
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

                //nameAcc.setText(name + personName);
                emailAcc.setText(email + personEmail);


                //ImageView imgAcc = view.findViewById(R.id.imgAcc);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LottieAnimationView animationView
                                = view.findViewById(R.id.animationView);
                        animationView.setVisibility(View.INVISIBLE);

                        Picasso.get().load(personPhoto).into(imgAcc);
                    }
                }, 3000);


            }
            Log.d(TAG, "onComplete: currentUserUid is null");


        }
    }
}