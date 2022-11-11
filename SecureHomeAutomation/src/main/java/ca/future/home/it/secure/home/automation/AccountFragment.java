/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/
package ca.future.home.it.secure.home.automation;

import static com.google.firebase.crashlytics.internal.Logger.TAG;

import android.animation.Animator;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class AccountFragment extends Fragment {

    UserInfo userInfo=new UserInfo();
    String name;
    private TextView personName;

    private Button signOutButton;
    View view;
//    ImageView imgAcc;
    TextView  emailAcc;
    ImageView profileImage;
    final Handler handler = new Handler();
    Animation fadeInAnimation;
    LottieAnimationView animationView;
    Uri userImage;
    //Edit profile
    FloatingActionButton editProfileButton;

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
        init();
        userinfo();
        btnSteps();
        imageHandler();

    }

    private void init() {
        personName=view.findViewById(R.id.tv_account_person_name);
        emailAcc = view.findViewById(R.id.tv_account_person_email);
        profileImage = view.findViewById(R.id.profile_image);
        animationView = view.findViewById(R.id.animationView);
        signOutButton = view.findViewById(R.id.Settings_signOut_button);
        editProfileButton = view.findViewById(R.id.editProfileIcon);
    }

    private void btnSteps() {

        signOutButton.setOnClickListener(view1 -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            Toast.makeText(getContext(), R.string.signed_out, Toast.LENGTH_SHORT).show();
        });
    }

    private void imageHandler(){

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (userImage!=null){
                            animationView.setVisibility(View.INVISIBLE);

                            Picasso.get().load(userImage).into(profileImage);

                        }
                        else {
                            animationView.setVisibility(View.VISIBLE);
                        }
                    }
                }, 3000);

            Log.d(TAG, getString(R.string.log_uid_null));
            imageAnimation();
            //Opening profile edit frag
            editProfileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                handler.postDelayed(()-> getParentFragmentManager().beginTransaction().replace(R.id.flFragment,MainActivity.profileEditFragment).commit(),300);
                }
            });
        }

    private void userinfo() {

        userInfo.typeAccount();

        if (userInfo.localEmail!=null){
            emailAcc.setText(userInfo.localEmail);
        }

        if (userInfo.personEmail!=null){
            emailAcc.setText(userInfo.emailInfo);
        }
        else
            emailAcc.setText(R.string.noInfo);

        if (userInfo.localName!=null){
            personName.setText(userInfo.localName);
        }

        if (userInfo.nameInfo!=null){
            personName.setText(userInfo.nameInfo);
            if(userInfo.personPhoto!=null) {
                userImage=userInfo.personPhoto;
            }
            else {
                userImage=null;
            }
        }
        else{
            personName.setText(R.string.noInfo);
        }
        imageHandler();
    }

    public void imageAnimation(){
        profileImage.setVisibility(View.VISIBLE);
        fadeInAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.profile_image_anim);
        profileImage.startAnimation(fadeInAnimation);
    }
}