/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/
package ca.future.home.it.secure.home.automation;

import static android.content.Context.MODE_PRIVATE;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.animation.Animator;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class AccountFragment extends Fragment {
    UserInfo userInfo=new UserInfo();
    String name;
    private TextView personName,personPhone;
    private Button signOutButton;
    View view;
    String key,localKey,personalKey,profileKey,sensorKey,userKey,userData, accountKey, emailKey,
        finalEmailKey, nameKey, finalNameKey;
    private int signInType;
//    ImageView imgAcc;
    String dbName, dbEmail, dbPhone;
    TextView  emailAcc;
    Boolean dbProfileEdited;
    ImageView profileImage;
    final Handler handler = new Handler();
    Animation fadeInAnimation;
    LottieAnimationView animationView;
    Uri userImage;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    //Edit profile
    FloatingActionButton editProfileButton;
    GoogleSignInClient mClient;

    //Database
    DatabaseReference databaseReference;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_account, container, false);
        //Google details
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mClient = GoogleSignIn.getClient(getContext(),gso);
        GoogleSignInAccount googleAccount = GoogleSignIn.getLastSignedInAccount(getContext());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        userinfo();
        btnSteps();
        imageHandler();

        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.user_data_new),MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Boolean profileEdited = sharedPreferences.getBoolean(getString(R.string.editing_profile),false);

        if (profileEdited){
            personName.setText(sharedPreferences.getString(getString(R.string.name_new_user),getString(R.string.no_info)));
            emailAcc.setText(sharedPreferences.getString(getString(R.string.email_new_user),getString(R.string.info_no)));
            personPhone.setText(sharedPreferences.getString(getString(R.string.phone_new_user),getString(R.string.non_info)));
        }

     getDB();

    }
    private String dbID(){
        userInfo.typeAccount();
        userKey=getApplicationContext().getString(R.string.userKey);
        userData=getString(R.string.user_info);

        localKey=userInfo.userId;
        personalKey=userInfo.idInfo;

        if(localKey!=null){
            key=localKey;
        }
        if(personalKey!=null) {
            key= personalKey;
        }
        profileKey=key+userData;
        sensorKey=profileKey;

        return userKey+profileKey;
    }

    private void init() {
        personName=view.findViewById(R.id.tv_account_person_name);
        emailAcc = view.findViewById(R.id.tv_account_person_email);
        personPhone = view.findViewById(R.id.tv_account_person_phone);
        profileImage = view.findViewById(R.id.profile_image);
        animationView = view.findViewById(R.id.animationView);
        signOutButton = view.findViewById(R.id.Settings_signOut_button);
        editProfileButton = view.findViewById(R.id.editProfileIcon);
        emailKey=getString(R.string.slash_email);
        nameKey=getString(R.string.name_info);
    }

    public void getDB() {
        accountKey=dbID();
        finalEmailKey=accountKey+emailKey;
        finalNameKey=accountKey+nameKey;

        //Users email
        databaseReference = FirebaseDatabase.getInstance().getReference().child((finalEmailKey));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    emailAcc.setText(Objects.requireNonNull(snapshot.getValue()).toString());
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Users name
        databaseReference = FirebaseDatabase.getInstance().getReference().child((finalNameKey));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    personName.setText(Objects.requireNonNull(snapshot.getValue()).toString());
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void btnSteps() {

        signOutButton.setOnClickListener(view1 -> {
            if(UserInfo.getSignInType()==0) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences(LoginActivity.PREFS_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(getString(R.string.logged),false).apply();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                Toast.makeText(getContext(), R.string.signed_out, Toast.LENGTH_SHORT).show();
            }else if(signInType == 1){
                mClient.signOut().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(getContext(), R.string.signedOut, Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                Toast.makeText(getContext(),  R.string.signedOut, Toast.LENGTH_SHORT).show();
            }

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